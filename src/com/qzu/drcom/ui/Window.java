package com.qzu.drcom.ui;

import java.io.File;
import javax.swing.JOptionPane;

public class Window {
	public static String separator = File.separator;
	public static String directory = "myDrcom";
	public static String fileName = "myDrcom.log";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		if (isAppActive(directory + separator + fileName)) {
			JOptionPane.showMessageDialog(null, "已有一个程序在运行,程序退出 ");
		} else {
			LoginFrame.getInstance().setVisible(true);
		}

		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// try {
		// Window window = new Window();
		// window.frame.setVisible(true);
		// //updateState(window);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
	}

	// private static void updateState(Window window) {
	// Timer timer = new Timer();
	// timer.schedule(window.new updateState(), 0, 1000 * 2400);
	// }

	private static boolean isAppActive(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			System.out.println("+++++++++++++++++++++++");
			return false;
		}

		// // 如果dir不以文件分隔符结尾，自动添加文件分隔符
		// if (!dir.endsWith(File.separator)) {
		// dir = dir + File.separator;
		// }
		// File dirFile = new File(dir);
		// // 如果dir对应的文件不存在，或者不是一个目录，则退出
		// if (!dirFile.exists() || !dirFile.isDirectory()) {
		// System.out.println("删除目录失败" + dir + "目录不存在！");
		// return false;
		// } else
		return true;
	}

	// public class LoginThread01 implements Runnable {
	// @Override
	// public void run() {
	// new Thread(LoginThread.getInstance()).start(); // 登录的操作
	// try {
	// Thread.sleep(1200);
	// if (Login.logined) {
	// System.out.println("-----------開始發送心跳包-------------");
	// new Thread(KeepThread.getInstance()).start(); // 心跳
	// } else
	// System.out.println("-------------登陸失敗----------------");
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}
