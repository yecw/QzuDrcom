package com.qzu.drcom.utils;

import java.security.MessageDigest;
import java.util.Random;

public class ConnectivityUtil {

	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public final static String MD5(byte[] s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(s);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static byte[] MD5_lite(byte[] s) {
		try {
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(s);
			byte[] md = mdInst.digest();
			return md;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	public static byte[] dr_checksum(byte[] check) throws Exception {
		long ret = 1234;

		byte[] check_foo = new byte[4];
		int j_foo = 1;
		for (int j = 0; j < check.length; j = j + 4) {
			int i_foo = 0;
			for (int i = 0; i < 4; i++) {
				if (4 * j_foo - 1 - i_foo > check.length) {
					break;
				}
				check_foo[i] = check[4 * j_foo - 1 - i_foo];
				++i_foo;
			}

			j_foo++;
			ret ^= Long.parseLong(byteArr2HexStr(check_foo), 16);
		}
		long ret_foo = 4294967295L;
		ret = (1968 * ret) & ret_foo;
		byte[] pack = intToByteArray(ret);

		return pack;

	}

	public static byte[] dr_dump(String mac) throws NumberFormatException, Exception {
		long foo = Long.parseLong(mac, 16);
		;
		String s = Long.toHexString(foo);
		if (s.length() % 2 == 1) {
			String str_foo = "0" + s;
			return hexStr2ByteArr(str_foo);
		}
		System.out.println(s);
		return hexStr2ByteArr(s);
	}

	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public static byte[] hexStr2ByteArrl(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Long.parseLong(strTmp, 16);
		}
		return arrOut;
	}

	public static String printHexString(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			a = a + hex;
		}
		return a;
	}

	public static byte[] intToByteArray(long i) {
		byte[] result = new byte[4];
		result[3] = (byte) ((i >> 24) & 0xFF);
		result[2] = (byte) ((i >> 16) & 0xFF);
		result[1] = (byte) ((i >> 8) & 0xFF);
		result[0] = (byte) (i & 0xFF);
		return result;
	}

	public static byte[] dr_ror(byte[] md5, String pwd) throws Exception {
		String ret = "";
		char[] pwd_foo = pwd.toCharArray();

		for (int i = 0; i < pwd.length(); i++) {
			byte[] gg = { md5[i] };
			int x = Integer.valueOf(byteArr2HexStr(gg), 16) ^ Integer.valueOf(pwd_foo[i]);
			int a = (x << 3) & 0xFF;
			int b = x >> 5;
			int c = (a + b) & 0xFF;
			if (Integer.toHexString(c).length() == 1) {
				String foo = "0" + Integer.toHexString(c);
				ret += foo;
			} else {
				ret = ret + Integer.toHexString(c);

			}

		}
		return hexStr2ByteArr(ret);

	}

	public static long ipToLong(String ipAddress) {

		// ipAddressInArray[0] = 192
		String[] ipAddressInArray = ipAddress.split("\\.");

		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {

			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);

			// 1. 192 * 256^3
			// 2. 168 * 256^2
			// 3. 1 * 256^1
			// 4. 2 * 256^0
			result += ip * Math.pow(256, power);

		}

		return result;

	}

	public static String Long2Ip(long ip) {
		StringBuilder sb = new StringBuilder(15);

		for (int i = 0; i < 4; i++) {
			sb.insert(0, Long.toString(ip & 0xff));

			if (i < 3) {
				sb.insert(0, '.');
			}
			ip = ip >> 8;

		}

		return sb.toString();
	}

	public static String ipIncreasing(String ipAddress) {

		String[] ipAddressInArray = ipAddress.split("\\.");
		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {
			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);
		}
		long ip2 = new Random().nextInt(250) + result;

		StringBuilder sb = new StringBuilder(15);

		for (int i = 0; i < 4; i++) {
			sb.insert(0, Long.toString(ip2 & 0xff));

			if (i < 3) {
				sb.insert(0, '.');
			}
			ip2 = ip2 >> 8;
		}
		return sb.toString();
	}
	public static String svrIpIncreasing2(String ipAddress) {

		String[] ipAddressInArray = ipAddress.split("\\.");
		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {
			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);
		}
		long ip2 = 8 + result;

		StringBuilder sb = new StringBuilder(15);

		for (int i = 0; i < 4; i++) {
			sb.insert(0, Long.toString(ip2 & 0xff));

			if (i < 3) {
				sb.insert(0, '.');
			}
			ip2 = ip2 >> 8;
		}
		return sb.toString();
	}

}
