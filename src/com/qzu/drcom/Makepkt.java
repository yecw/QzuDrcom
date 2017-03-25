package com.qzu.drcom;

import com.qzu.drcom.utils.ConnectivityUtil;

public class Makepkt {
	
	public static byte[] dr_mkpkt(byte[] salt, String host_ip,String dhcp_server, String username, String pwd, String mac) throws Exception{
		
		
		//data_3 36
		byte[] data_3 = new byte[36];
		byte[] data_5 = new byte[6];
		byte[] data_8 = new byte[16];
		byte[] data_9 = new byte[8];
		//byte[] data_11 = new byte [32];
		
		String ip = host_ip;   
		ip +=".192.168.211.1";
		ip +=".192.168.191.1";
		ip +=".192.168.92.1";
		String[] ip_foo = ip.split("\\.");
		
		int account_length = username.length() + 20;
		byte[] account = username.getBytes();
		byte[] mac_addr = ConnectivityUtil.hexStr2ByteArr(mac);
		
		//merge_1
		byte[] foo_1 = {0x03, 0x01};
		
		byte[] foo_2 = {0x01};
		byte[] foo_3 = {0x00, 0x00, 0x00, 0x00};
		//data += md5sum(data + '\x14\x00\x07\x0b')[:8] #md53
		byte[] foo_4 = {0x14, 0x00, 0x07, 0x0b};
//		byte[] foo_5 = {0x03, 0x01};
		//checksum
		byte[] foo_6 = {0x01, 0x26, 0x07, 0x11, 0x00, 0x00};
		
		byte[] foo_7 = {0x00, 0x00};
		
		byte[] merge_1 = ConnectivityUtil.byteMerger(foo_1, salt);
		byte[] merge_2 = ConnectivityUtil.byteMerger(merge_1, pwd.getBytes());
		
		byte[] merge_3 = ConnectivityUtil.byteMerger(foo_2, pwd.getBytes());
		byte[] merge_4 = ConnectivityUtil.byteMerger(merge_3, salt);
		byte[] merge_5 = ConnectivityUtil.byteMerger(merge_4, foo_3);
//		byte[] merge_6 = ConnectivityUtil.byteMerger(foo_5, salt);
//		byte[] merge_7 = ConnectivityUtil.byteMerger(merge_6, pwd.getBytes());
		
		//账号转为byte
		byte[] data_1 = {0x03, 0x01, 0x00, (byte) account_length};    //code, type, EOF, username_length
		//密码转为byte
		byte[] data_2 = ConnectivityUtil.MD5_lite(merge_2);    //md5a
		          
		for(int data3_j=0;data3_j<account.length;data3_j++){
			data_3[data3_j] = account[data3_j];     //username
			
		}
		
		for(int data3_i=account.length;data3_i<36;data3_i++){
			   data_3[data3_i] = 0x00;
			
		}
		
		
		
		
		//data += CONTROLCHECKSTATUS
	    //data += ADAPTERNUM
		byte[] data_4 = {0x20, 0x02};    //unknow, mac_flag
		
		for(int i=0;i<6;i++){
			   data_5[i] = (byte) (data_2[i] ^ mac_addr[i]);    //mac xor md5a
		}
		byte[] data_6 = ConnectivityUtil.MD5_lite(merge_5);    //md5b
		//# number of ip
		byte[] data_7 = {0x01};    //nic count

		//data += ''.join([chr(int(i)) for i in host_ip.split('.')]) # x.x.x.x ->
		for(int ip_i=0;ip_i<ip_foo.length;ip_i++){
			   data_8[ip_i] = (byte) Integer.parseInt(ip_foo[ip_i]);    //ips
		}
		byte[] sum_1 = ConnectivityUtil.byteMerger(data_1, data_2);
		byte[] sum_2 = ConnectivityUtil.byteMerger(sum_1, data_3);
		
		byte[] sum_3 = ConnectivityUtil.byteMerger(sum_2, data_4);
		
		byte[] sum_4 = ConnectivityUtil.byteMerger(sum_3, data_5);
		
		byte[] sum_5 = ConnectivityUtil.byteMerger(sum_4, data_6);
		
		byte[] sum_6 = ConnectivityUtil.byteMerger(sum_5, data_7);
		
		byte[] sum_7 = ConnectivityUtil.byteMerger(sum_6, data_8);
		
		byte[] sum_8 = ConnectivityUtil.byteMerger(sum_7, foo_4);
		
		//byte[] foo_4 = {0x14, 0x00, 0x07, 0x0b};
		//data += md5sum(data + '\x14\x00\x07\x0b')[:8] #md53
		byte[] sum_foo = ConnectivityUtil.MD5_lite(sum_8);
		
		for(int sum_i=0;sum_i<8;sum_i++){
			data_9[sum_i] = sum_foo[sum_i];    //checksum1
			
		}
		//data += IPDOG
		//data += '\x00'*4 #delimeter
	    byte[] data_10 = {0x01, 0x00, 0x00, 0x00, 0x00};    //ipdog, zeros

	    //data += '\x00'*4 #delimeter
	    //TODO hostname(WINDIAOS)
	    byte[] host_name = {0x57, 0x49, 0x4E, 0x44, 0x49, 0x41, 0x4F, 0x53};
	    byte[] host_namel = new byte[24];
        for(int hi=0;hi<24;hi++){
        	host_namel[hi] = 0x00;
		}
        byte[] data_host = ConnectivityUtil.byteMerger(host_name, host_namel);
	    
	    /*for(int host_j=0;host_j<8;host_j++){
	        data_11[host_j] = 0x69;
	    }                                                   //hostname:iiiiiiii
		//data += host_name.ljust(32, '\x00')
	    for(int host_i=8;host_i<32;host_i++){		
			data_11[host_i] = 0x00;
		}*/
	    
	    byte[] data_12 = new byte[20];    //zero
	    String primary_dns="114.114.114.114";
	    String secondary_dns=".202.100.192.68";
	    String dhcp_svr = "."+dhcp_server;
	    
	    primary_dns +=secondary_dns;
	    primary_dns +=dhcp_svr;
	    
	    String[] dns_foo = primary_dns.split("\\.");
	    
	    for(int dns_i=0;dns_i<dns_foo.length;dns_i++){
			   data_12[dns_i] = (byte) Integer.parseInt(ip_foo[dns_i]);    //primary_dns
			
		}
	    //data += '\x00' * 8 # delimeter
        for(int dns_j=12;dns_j<20;dns_j++){
			
			data_12[dns_j] = 0x00;
		}
        byte[] data_13_1 = {(byte) 0x94, 0x00, 0x00, 0x00};
        byte[] data_13_2 = {0x06, 0x00, 0x00, 0x00};
        byte[] data_13_3 = {0x02, 0x00, 0x00, 0x00};
        byte[] data_13_4 = {(byte) 0xf0, 0x23, 0x00, 0x00};
        byte[] data_13_5 = {0x02, 0x00, 0x00, 0x00};
        byte[] data_13_6 = {0x44, 0x72, 0x43, 0x4f,0x4d,0x00,(byte) 0xcf,0x07,0x26};
        
        byte[] data_13_7 = new byte[55];
        for(int d_j=0;d_j<55;d_j++){
        	data_13_7[d_j] = 0x00;
		}
        byte[] data_13_8 = {0x64,0x63,0x35,0x65,0x66,0x32,0x30,0x37,0x61,0x62,0x65,0x65,0x65,0x65,0x30,0x33,0x30,0x35,0x36,0x31,0x32,0x38,0x30,0x63,0x61,0x31,0x66,0x31,0x35,0x61,0x39,0x62,0x39,0x38,0x30,0x37,0x35,0x63,0x39,0x63};
        
        byte[] data_13_9 = new byte[24];
        for(int d_k=0;d_k<24;d_k++){
        	data_13_9[d_k] = 0x00;
		}
        byte[] sum_13_1 = ConnectivityUtil.byteMerger(data_13_1, data_13_2);
        byte[] sum_13_2 = ConnectivityUtil.byteMerger(sum_13_1, data_13_3);
        byte[] sum_13_3 = ConnectivityUtil.byteMerger(sum_13_2, data_13_4);
        byte[] sum_13_4 = ConnectivityUtil.byteMerger(sum_13_3, data_13_5);
        byte[] sum_13_5 = ConnectivityUtil.byteMerger(sum_13_4, data_13_6);
        byte[] sum_13_6 = ConnectivityUtil.byteMerger(sum_13_5, data_13_7);
        byte[] sum_13_7 = ConnectivityUtil.byteMerger(sum_13_6, data_13_8);
        byte[] data_13 = ConnectivityUtil.byteMerger(sum_13_7, data_13_9);
        
        
		//byte[] data_12 = {0x0a, 0x0a, 0x0a, 0x0a};    //dns
		//byte[] data_13 = new byte[164];    //zero
		
		//data += AUTH_VERSION
		byte[] data_14 = {0x18, 0x00, 0x00, (byte)pwd.length()};    //unknown
		//byte[] data_14 = {0x6e, 0x00, 0x00, (byte)pwd.length()};    //unknown
		byte[] data_15 = ConnectivityUtil.dr_ror(ConnectivityUtil.MD5_lite(merge_2), pwd);    //md5 ror pwd
		
		// 0x02 0x0c
		byte[] data_16 = {0x02, 0x0c};
		
		byte[] sum_7_1 = ConnectivityUtil.byteMerger(sum_7, data_9);
		byte[] sum_7_2 = ConnectivityUtil.byteMerger(sum_7_1, data_10);
		byte[] sum_7_3 = ConnectivityUtil.byteMerger(sum_7_2, data_host);
		byte[] sum_7_4 = ConnectivityUtil.byteMerger(sum_7_3, data_12);
		byte[] sum_7_5 = ConnectivityUtil.byteMerger(sum_7_4, data_13);
		byte[] sum_7_6 = ConnectivityUtil.byteMerger(sum_7_5, data_14);
		byte[] sum_7_7 = ConnectivityUtil.byteMerger(sum_7_6, data_15);
		
		// 0x02 0x0c
		byte[] sum_7_8 = ConnectivityUtil.byteMerger(sum_7_7, data_16);
		
		//byte[] foo_6 = {0x01, 0x26, 0x07, 0x11, 0x00, 0x00};
		byte[] merge_foo_6 = ConnectivityUtil.byteMerger(sum_7_8, foo_6);
		
		//data += checksum(data+'\x01\x26\x07\x11\x00\x00'+dump(mac))
		byte[] merge_foo_mac = ConnectivityUtil.byteMerger(merge_foo_6, ConnectivityUtil.dr_dump(mac));
		
		//checksum
		byte[] data_17 = ConnectivityUtil.dr_checksum(merge_foo_mac);
		
		//byte[] foo_7 = {0x00, 0x00};
		//data += dump(mac)
		byte[] data_18 = ConnectivityUtil.byteMerger(foo_7, ConnectivityUtil.dr_dump(mac));
		
		byte[] merge_data_1 = ConnectivityUtil.byteMerger(sum_7_8, data_17);
		
		byte[] data_end = {0x00, 0x00,(byte) 0xe4,(byte) 0xc7};
		
		byte[] data_19 = ConnectivityUtil.byteMerger(data_18, data_end);
		
		byte[] data = ConnectivityUtil.byteMerger(merge_data_1, data_19);
		
		System.out.println("[mkpkt]"+ConnectivityUtil.printHexString(data));
		return data;
		
	}
}
