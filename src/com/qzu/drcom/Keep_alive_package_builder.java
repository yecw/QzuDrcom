package com.qzu.drcom;

import com.qzu.drcom.config.Config;
import com.qzu.drcom.utils.ConnectivityUtil;

public class Keep_alive_package_builder {
	public static byte[] build(int number, byte[] random, byte[] tail, int type, boolean first) throws NumberFormatException, Exception{
		byte[] data_1_1 = {0x07, (byte) number};
		byte[] data_1_2 = {0x28, 0x00, 0x0b, (byte)type};
		byte[] data_1 = ConnectivityUtil.byteMerger(data_1_1, data_1_2);
		
		byte[] data_2_1 = {0x0F, 0x27};
		byte[] data_2_2 = {(byte) 0xdc, 0x02};
		byte[] data_2;
		if (first == true){
			//data += '\x0f\x27'
			data_2 = ConnectivityUtil.byteMerger(data_1, data_2_1);
		}else{
			//data += KEEP_ALIVE_VERSION
			data_2 = ConnectivityUtil.byteMerger(data_1, data_2_2);
		}
		//data += '\x2f\x12' + '\x00' * 6
//		byte[] data_3_1={0x0f,0x27,0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		byte[] data_3_1={0x2f,0x12,0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		byte[] data_3 = ConnectivityUtil.byteMerger(data_2, data_3_1);
		
		byte[] data_4 = ConnectivityUtil.byteMerger(data_3, tail);
		
		byte[] data_5_1 = {0x00, 0x00, 0x00, 0x00};
		byte[] data_5 = ConnectivityUtil.byteMerger(data_4, data_5_1);
		
		byte[] data_6;
		
		String ip =Config.host_ip;
		String[] ip_foo = ip.split("\\.");
		byte[] foo = new byte[4];
		
		for(int ip_i=0;ip_i<ip_foo.length;ip_i++){
			   foo[ip_i] = (byte) Integer.parseInt(ip_foo[ip_i]);    //ips
		}
		
		if(type==3){
			//byte[] foo = {0x31, (byte) 0x8c, 0x62, 0x31};
			byte[] crc = {0x00, 0x00, 0x00, 0x00};
			byte[] data_6_1 = {0x00, 0x00 ,0x00, 0x00 ,0x00, 0x00 ,0x00, 0x00};
			byte[] data_6_2 = ConnectivityUtil.byteMerger(crc, foo);
			byte[] data_6_3 = ConnectivityUtil.byteMerger(data_6_2, data_6_1);
			data_6 = ConnectivityUtil.byteMerger(data_5, data_6_3);
		}else{
			byte[] data_6_1 = {0x00, 0x00 ,0x00, 0x00 ,0x00, 0x00 ,0x00, 0x00, 0x00, 0x00 ,0x00, 0x00 ,0x00, 0x00 ,0x00, 0x00};
			data_6 = ConnectivityUtil.byteMerger(data_5, data_6_1);
		}
		return data_6;
	}
}
