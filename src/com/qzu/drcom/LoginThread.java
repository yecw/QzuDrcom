package com.qzu.drcom;

import com.qzu.drcom.config.Config;
import com.qzu.drcom.ui.Login;
import com.qzu.drcom.utils.ConnectivityUtil;

public class LoginThread implements Runnable { // 登录线程

	private static LoginThread loginThread;

	public static synchronized LoginThread getInstance() {
		if (loginThread == null) {
			loginThread = new LoginThread();
			return loginThread;
		} else
			return loginThread;

	}

	private LoginThread() {
	}

	@Override
	public void run() {
		Config.host_ip = ConnectivityUtil.ipIncreasing(Config.host_ip);
		System.out.println("本机ip:\n" + Config.host_ip);
		try {
			Login.dr_login(Config.username, Config.host_ip, Config.dhcp_server, Config.password, Config.server,
					Config.mac);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}