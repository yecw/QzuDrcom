package com.qzu.drcom;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Challenge {

	public static byte[] dr_challenge(String server) {
		DatagramSocket client = null;
		InetAddress addr;
		byte[] v = new byte[76];
		Random random = new Random();
		byte t = (byte) ((random.nextInt(0xF) + 0xF0) % 0xFFFF);
		byte[] foo = { 0x01, 0x02, t, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00 };
		try {
			client = new DatagramSocket();
			addr = InetAddress.getByName(server);
			DatagramPacket sendPacket = new DatagramPacket(foo, foo.length, addr, 61440);
			client.send(sendPacket);

			DatagramPacket packet = new DatagramPacket(v, 76);
			client.receive(packet);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		if (v[0] == 0x02) {
			System.out.println("challenge packet received");
		}

		byte[] pack = new byte[4];
		System.arraycopy(v, 4, pack, 0, 4);

		return pack;
	}
}