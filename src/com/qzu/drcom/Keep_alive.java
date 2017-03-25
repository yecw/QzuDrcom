package com.qzu.drcom;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import com.qzu.drcom.config.Config;
import com.qzu.drcom.utils.ConnectivityUtil;

public class Keep_alive {

	public static void keep_alive01(byte[] salt, byte[] tail, String pwd, String server)
			throws NumberFormatException, Exception {

		DatagramSocket client = new DatagramSocket();
		try {
			InetAddress addr = InetAddress.getByName(server);
			Random random = new Random();
			byte foo_0 = (byte) ((random.nextInt(0xF) + 0xF0) % 0xFFFF);

			byte[] foo = ConnectivityUtil.intToByteArray(foo_0);
			byte[] data_0 = { (byte) 0xff };
			byte[] data_1 = { 0x00, 0x00, 0x00 };
			byte[] data_1_1 = { 0x00, 0x00, 0x00, 0x00 };
			byte[] foo_1 = { 0x03, 0x01 };
			byte[] merge_1 = ConnectivityUtil.byteMerger(foo_1, salt);
			byte[] merge_2 = ConnectivityUtil.byteMerger(merge_1, pwd.getBytes());
			byte[] data_2 = ConnectivityUtil.MD5_lite(merge_2); // md5a
			byte[] data_3 = ConnectivityUtil.byteMerger(data_0, data_2);
			byte[] data_4 = ConnectivityUtil.byteMerger(data_3, data_1);
			byte[] data_5 = ConnectivityUtil.byteMerger(data_4, tail);
			byte[] data_6 = ConnectivityUtil.byteMerger(data_5, foo);
			byte[] data_7 = ConnectivityUtil.byteMerger(data_6, data_1_1);

			DatagramPacket sendPacket = new DatagramPacket(data_7, data_7.length, addr, 61440);
			client.send(sendPacket);
			System.out.println("'[keep_alive1] send' " + ConnectivityUtil.printHexString(data_7));
			while (true) {
				byte[] v = new byte[76];
				DatagramPacket packet = new DatagramPacket(v, 76);
				client.receive(packet);
				// System.out.println("'[challenge] recv
				// '"+ConnectivityUtil.printHexString(v));
				// System.out.println("'[DEBUG]
				// challenge:'"+ConnectivityUtil.printHexString(packet.getData()));
				if (v[0] == 0x07) {
					// mHandler.obtainMessage(1005,
					// 02).sendToTarget();
					System.out.println("'[keep-alive1] recv '" + ConnectivityUtil.printHexString(v));
					break;
				} else {
					System.out.println(
							"'[keep-alive1]recv/not expected' " + ConnectivityUtil.printHexString(packet.getData()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

	}

	public static void keep_alive02(String server) throws NumberFormatException, Exception {
		DatagramSocket client = new DatagramSocket();
		try {
			InetAddress addr = InetAddress.getByName(server);
			Random random = new Random();
			byte[] tail = new byte[4];

			int ran = random.nextInt(0xFFFF);
			ran = ran + 1 + random.nextInt(9);
			byte[] tail_foo = { 0x00, 0x00, 0x00, 0x00 };
			int svr_num = 0;
			byte[] packet;

			// send1
			packet = Keep_alive_package_builder.build(svr_num, ConnectivityUtil.dr_dump(String.valueOf(ran)), tail_foo,
					1, true);
			while (true) {
				DatagramPacket sendPacket1 = new DatagramPacket(packet, packet.length, addr, 61440);
				client.send(sendPacket1);
				System.out.println("'[keep_alive2] send1 ' " + ConnectivityUtil.printHexString(packet));
				byte[] v = new byte[76];
				DatagramPacket recvpacket = new DatagramPacket(v, 76);
				client.receive(recvpacket);
				System.out.println("'[keep_alive2] recv1 ' " + ConnectivityUtil.printHexString(recvpacket.getData()));

				//////// TODO test
				// byte[] test02={0x07,0x00,0x28,0x00};

				if ((v[0] == 0x07 & v[1] == 0x01) || (v[0] == 0x07 & v[1] == 0x00)) {
					System.out.println("break send1" + svr_num);
					break;
				} else if (v[0] == 0x07 && v[2] == 0x10) {
					System.out.println("'[keep_alive2] recv file, resending..' ");
					svr_num = svr_num + 1;
					packet = Keep_alive_package_builder.build(svr_num, ConnectivityUtil.dr_dump(String.valueOf(ran)),
							tail_foo, 1, false);
				} else {
					System.out.println("'[keep_alive2] recv1/unexpected' "
							+ ConnectivityUtil.printHexString(recvpacket.getData()));
				}
			}
			// send2
			ran = ran + 1 + random.nextInt(9);
			packet = Keep_alive_package_builder.build(svr_num, ConnectivityUtil.dr_dump(String.valueOf(ran)), tail_foo,
					1, false);
			System.out.println("'[keep_alive2] send2' " + ConnectivityUtil.printHexString(packet));
			DatagramPacket sendPacket2 = new DatagramPacket(packet, packet.length, addr, 61440);
			client.send(sendPacket2);
			while (true) {
				byte[] v = new byte[76];
				DatagramPacket recvpacket = new DatagramPacket(v, 76);
				client.receive(recvpacket);
				if (v[0] == 0x07) {
					svr_num = svr_num + 1;
					System.out
							.println("'[keep_alive2] recv2' " + ConnectivityUtil.printHexString(recvpacket.getData()));

					for (int h = 0; h < 4; h++) {
						tail[h] = v[16 + h];
					}
					break;
				} else {
					System.out.println("'[keep_alive2] recv2/unexpected' "
							+ ConnectivityUtil.printHexString(recvpacket.getData()));
				}
			}
			// send3
			ran = ran + 1 + random.nextInt(9);
			packet = Keep_alive_package_builder.build(svr_num, ConnectivityUtil.dr_dump(String.valueOf(ran)), tail, 3,
					false);
			System.out.println("'[keep_alive2] send3' " + ConnectivityUtil.printHexString(packet));
			DatagramPacket sendPacket3 = new DatagramPacket(packet, packet.length, addr, 61440);
			client.send(sendPacket3);
			while (true) {

				byte[] v = new byte[76];
				DatagramPacket recvpacket = new DatagramPacket(v, 76);
				client.receive(recvpacket);
				System.out.println("'[keep_alive2] recv3' " + ConnectivityUtil.printHexString(recvpacket.getData()));
				if (v[0] == 0x07) {
					svr_num = svr_num + 1;
					for (int a = 0; a < 4; a++) {
						tail[a] = v[16 + a];
					}
					System.out.println("'[keep_alive2] keep-alive2 loop was in daemon.' ");
					break;
				} else {
					System.out.println("'[keep_alive2] recv3/unexpected' "
							+ ConnectivityUtil.printHexString(recvpacket.getData()));
				}

			}
			int x = svr_num;
			while (true) {
				// Thread.sleep(1000);
				ran = ran + 1 + random.nextInt(9);
				packet = Keep_alive_package_builder.build(x, ConnectivityUtil.dr_dump(String.valueOf(ran)), tail, 1,
						false);
				System.out.println("'[keep_alive2] send'" + x + " " + ConnectivityUtil.printHexString(packet));
				DatagramPacket sendPacket4 = new DatagramPacket(packet, packet.length, addr, 61440);
				client.send(sendPacket4);

				byte[] v = new byte[76];
				DatagramPacket recvpacket = new DatagramPacket(v, 76);
				client.receive(recvpacket);
				System.out.println("'[keep_alive2] recv ' " + ConnectivityUtil.printHexString(recvpacket.getData()));
				for (int m = 0; m < 4; m++) {
					tail[m] = v[16 + m];
				}

				ran = ran + 1 + random.nextInt(9);
				packet = Keep_alive_package_builder.build((x + 1), ConnectivityUtil.dr_dump(String.valueOf(ran)), tail,
						3, false);
				System.out.println("'[keep_alive2] send'" + (x + 1) + " " + ConnectivityUtil.printHexString(packet));
				DatagramPacket sendPacket5 = new DatagramPacket(packet, packet.length, addr, 61440);
				client.send(sendPacket5);

				byte[] v1 = new byte[76];
				DatagramPacket recvpacket5 = new DatagramPacket(v1, 76);
				client.receive(recvpacket5);
				System.out.println("'[keep_alive2] recv ' " + ConnectivityUtil.printHexString(recvpacket.getData()));
				for (int j = 0; j < 4; j++) {
					tail[j] = v1[16 + j];
				}
				x = x + 2;
				Thread.sleep(20000);
				Keep_alive.keep_alive01(Config.SALT, Config.tail, Config.password, Config.server);
				System.out.println("---------------");
				System.out.println("保持成功" + x);
				System.out.println("---------------");
			}

		} catch (Exception e) {

		} finally {
			client.close();
		}
	}

}
