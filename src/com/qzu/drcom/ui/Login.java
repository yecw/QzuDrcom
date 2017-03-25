package com.qzu.drcom.ui;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.qzu.drcom.Challenge;
import com.qzu.drcom.Makepkt;
import com.qzu.drcom.config.Config;
import com.qzu.drcom.utils.ConnectivityUtil;

public class Login {
	public static boolean logined = false;
	public static byte[] SALT;
	public static byte[] tail;

	public static byte[] dr_login(String username, String host_ip, String dhcp_server, String password, String server, String mac) {
		SALT = Challenge.dr_challenge(server);
		Config.SALT=SALT;
		byte[] packet;
		tail = new byte[16];
		DatagramSocket client = null;
		byte[] receive = new byte[45];
		try {
			packet = Makepkt.dr_mkpkt(SALT, host_ip, dhcp_server, username, password, mac);

			client = new DatagramSocket();
			InetAddress addr = InetAddress.getByName(server);
			DatagramPacket sendPacket = new DatagramPacket(packet, packet.length, addr, 61440);
			client.send(sendPacket);

			DatagramPacket re_packet = new DatagramPacket(receive, 45);
			client.receive(re_packet);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		System.out.println("[login] recv " + ConnectivityUtil.printHexString(receive));
		System.out.println("[login] sent ");
		if (receive[0] == 0x04) {
			System.out.println("[login] loged in ");
			logined = true;
		} else {
			logined = false;
			System.out.println("[login] loged failed ");
		}
		
		for (int i = 23; i < 39; i++) {
			tail[i - 23] = receive[i];
		}
		Config.tail=tail;
		return tail;
	}
}
