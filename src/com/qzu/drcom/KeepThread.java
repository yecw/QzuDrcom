package com.qzu.drcom;

import com.qzu.drcom.config.Config;
import com.qzu.drcom.ui.Login;

public class KeepThread implements Runnable { // 心跳线程
	private static KeepThread keepThread;

	public static synchronized KeepThread getInstance() {
		if (keepThread == null) {
			keepThread = new KeepThread();
			return keepThread;
		} else
			return keepThread;

	}

	private KeepThread() {
	}

	@Override
	public void run() {

		try {
			// keep_alive.keep(Config.svr);
			Keep_alive.keep_alive01(Login.SALT, Login.tail, Config.password, Config.server);
			Keep_alive.keep_alive02(Config.server);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
