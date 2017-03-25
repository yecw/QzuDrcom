package com.qzu.drcom.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import com.qzu.drcom.KeepThread;
import com.qzu.drcom.LoginThread;
import com.qzu.drcom.config.Config;
import com.qzu.drcom.utils.Loadfont;

public class LoginFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static TrayIcon trayIcon = null; // 托盘图标

	private static SystemTray tray = null; // 本操作系统托盘的实例

	private JPanel mainPanel; /* 主面板 */
	private JTextField userIdField;
	private JPasswordField userPwdField;
	private static JButton logBut;
	private JButton exitBut;
	private JButton xbtn;
	private ImageIcon background; /* 背景图片 */
	private JLabel backgroundCon; /* 背景图片容器 */
	private JLabel label1;
	private JLabel label2, label3;
	private JLabel label; /* 标题 */
	private ButtonGroup group;

	private static LoginFrame login;

	public static synchronized LoginFrame getInstance() {
		if (login == null) {
			login = new LoginFrame();
			return login;
		} else
			return login;
	}
// TODO
	class MouseHandler extends MouseAdapter {

		Point origin;
		// 鼠标拖拽想要移动的目标组件
		LoginFrame frame;

		public MouseHandler(LoginFrame frame) {
			this.frame = frame;
			origin = new Point();
		}

		/**
		 * 记录鼠标按下时的点
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			origin.x = e.getX();
			origin.y = e.getY();
		}

		/**
		 * 鼠标在标题栏拖拽时，设置窗口的坐标位置 窗口新的坐标位置 = 移动前坐标位置+（鼠标指针当前坐标-鼠标按下时指针的位置）
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = this.frame.getLocation();
			this.frame.setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
		}
	}

	private LoginFrame() {

		creatFile(Window.directory, Window.fileName);
		// creatFile(null,"drcom.properties");
		init();

		// 设置框架
		setTitle("欢迎登陆");
		setSize(600, 400);
		// setLocation(220, 120);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//getX();
		//getY() TODO;
		MouseHandler mouseListener = new MouseHandler(this);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		// this.addMouseListener(new MouseHandler());
		// addMouseMotionListener();

		setResizable(false);
		setUndecorated(true); // 隐藏标题栏
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/ali.jpg"));
		this.setIconImage(imageIcon.getImage());

		// 设置背景图片
		background = new ImageIcon(getClass().getResource("/images/dis.jpg"));
		backgroundCon = new JLabel(background);
		backgroundCon.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		getLayeredPane().add(backgroundCon, new Integer(Integer.MIN_VALUE));
		JPanel jp = (JPanel) getContentPane();
		jp.setOpaque(false);

		// 初始化mainPanel
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(null);
		setContentPane(mainPanel);

		JRadioButton radioButton1 = new JRadioButton("男1");// 创建单选按钮
		JRadioButton radioButton2 = new JRadioButton("女5");// 创建单选按钮
		JRadioButton radioButton3 = new JRadioButton("其他");// 创建单选按钮
		group = new ButtonGroup();// 创建单选按钮组
		group.add(radioButton1);// 将radioButton1增加到单选按钮组中
		group.add(radioButton2);// 将radioButton2增加到单选按钮组中
		group.add(radioButton3);// 将radioButton3增加到单选按钮组中

		// 设置标签
		label1 = new JLabel("账号:");
		label2 = new JLabel("密码:");
		label3 = new JLabel("位置:");

		// 自定义字体
		Font font = Loadfont.defaultFont();
		label = new JLabel("drcom拨号客户端");
		label.setFont(Loadfont.LagerFont());
		label1.setFont(font);
		label1.setFont(font);
		label2.setFont(font);
		label3.setFont(font);
		userIdField = new JTextField();
		userPwdField = new JPasswordField();
		try {
			Properties prop = new Properties();
			File propfile = new File("drcom.properties");
			if (propfile.exists()) {
				InputStream in = new BufferedInputStream(new FileInputStream("drcom.properties"));
				prop.load(in);/// 加载属性列表
				String location = prop.getProperty("location");
				String username = prop.getProperty("username");
				String password = prop.getProperty("password");

				if (location.equals("2")) {
					System.out.println("读取配置文件完成");
					radioButton2.setSelected(true);
				} else {
					radioButton1.setSelected(true);
				}
				userPwdField.setText(password);
				userIdField.setText(username);
			}
		} catch (IOException ex) {
			deleteFile(Window.directory + File.separator + Window.fileName);
			ex.printStackTrace();
		}

		radioButton1.setFont(font);
		radioButton2.setFont(font);
		radioButton3.setFont(font);
		radioButton1.setForeground(Color.red);
		radioButton2.setForeground(Color.red);
		radioButton3.setForeground(Color.WHITE);

		label.setBounds(170, 0, 450, 150);
		label.setForeground(Color.blue);
		label1.setForeground(Color.BLACK);
		label2.setForeground(Color.BLACK);
		label3.setForeground(Color.black);
		label1.setBounds(164, 130, 80, 30);
		label2.setBounds(164, 170, 80, 30);
		label3.setBounds(164, 210, 80, 30);

		// radioButton1.setBounds(165, 上下, 60, 20);
		radioButton1.setBounds(240, 215, 60, 20);
		radioButton2.setBounds(300, 215, 60, 20);
		radioButton3.setBounds(360, 215, 60, 20);
		mainPanel.add(label1);
		mainPanel.add(label2);
		mainPanel.add(label3);
		mainPanel.add(label);
		// radioButton1.setSelected(true);
		radioButton3.setEnabled(false);
		radioButton1.setContentAreaFilled(false);
		radioButton2.setContentAreaFilled(false);
		radioButton3.setContentAreaFilled(false);
		mainPanel.add(radioButton1);// 应用单选按钮
		mainPanel.add(radioButton2);// 应用单选按钮
		mainPanel.add(radioButton3);// 应用单选按钮
		// radioButton2.onc

		// 设置TextField

		userIdField.setBounds(235, 125, 200, 30);
		userPwdField.setBounds(235, 165, 200, 30);
		userIdField.setForeground(Color.pink);
		userPwdField.setForeground(Color.pink);
		userIdField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		userPwdField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		// userPwdField.setBorder(null);
		userIdField.setOpaque(false);
		userPwdField.setOpaque(false);
		mainPanel.add(userIdField);
		mainPanel.add(userPwdField);

		// 设置按钮位置
		logBut = new JButton();
		exitBut = new JButton();
		xbtn = new JButton();
		xbtn.setOpaque(false);
		logBut.setFont(font);
		exitBut.setFont(font);
		xbtn.setFont(font);
		logBut.setBounds(170, 270, 120, 36);
		exitBut.setBounds(320, 270, 120, 36);
		xbtn.setBounds(570, 1, 30, 26);
		logBut.setText("登陆");
		exitBut.setText("退出");
		// xbtn.setText("x");
		logBut.setContentAreaFilled(false); /* 异常 */
		exitBut.setContentAreaFilled(false); /* 异常 */
		xbtn.setContentAreaFilled(false); /* 异常 */
		logBut.setIcon(new ImageIcon("images/login.png"));
		exitBut.setIcon(new ImageIcon("images/exit.png"));
		ImageIcon x = new ImageIcon(getClass().getResource("/images/X.png"));
		xbtn.setIcon(x);
		// logBut.setBorder(null);
		// exitBut.setBorder(null);
		xbtn.setBorder(null);
		mainPanel.add(logBut);
		mainPanel.add(exitBut);
		mainPanel.add(xbtn);

		// 按钮交互
		exitBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				// System.out.println("退出");
				exit();
			}
		});
		exitBut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					dispose();
					exit();
				}
			}
		});

		xbtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onVisible(true);
			}
		});

		logBut.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {

				Config.username = userIdField.getText().trim();
				Config.password = userPwdField.getText().trim();

				// TODO
				if (radioButton2.isSelected()) {
					Config.location = 2;
					Config.server = Config.server_f5;
					Config.host_ip = Config.host_ip_f5;
					Config.dhcp_server = Config.dhcp_server_f5;
					System.out.println("server:" + Config.server);
					System.out.println("host_ip:" + Config.host_ip);
					System.out.println("dhcp_server:" + Config.dhcp_server);
				}

				new Thread(LoginThread01.getInstance()).start();

				// UserService service = new UserServiceImpl();
				// if (userIdField.getText() != null &&
				// String.valueOf(userPwdField.getPassword()) != null) {
				// boolean flag = service.logUser(userIdField.getText(),
				// String.valueOf(userPwdField.getPassword()));
				// if (flag) {
				// User user = new User();
				// user = userService.findUserByUserId(userIdField.getText());//
				// 未作判断
				// Container.register("user", user);// 注册一下user，方便后面使用
				// dispose();
				// new InnerSystem().setVisible(true);
				//
				// } else {
				// JOptionPane.showMessageDialog(null, "登陆失败", "登陆异常",
				// JOptionPane.ERROR_MESSAGE);
				// }
				// }

			}
		});
		logBut.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					new Thread(LoginThread01.getInstance()).start();
					System.out.println("这是什么？e.getKeyChar()");
					// UserService service = new UserServiceImpl();
					// if (userIdField.getText() != null &&
					// String.valueOf(userPwdField.getPassword()) != null) {
					// boolean flag = service.logUser(userIdField.getText(),
					// String.valueOf(userPwdField.getPassword()));
					// if (flag) {
					// User user = new User();
					// user =
					// userService.findUserByUserId(userIdField.getText());//
					// 未作判断
					// Container.register("user", user);// 注册一下user，方便后面使用
					// dispose();
					// new InnerSystem().setVisible(true);
					//
					// } else {
					// JOptionPane.showMessageDialog(null, "登陆失败", "登陆异常",
					// JOptionPane.ERROR_MESSAGE);
					// }
					// }
				}
			}
		});

	}

	public static class LoginThread01 implements Runnable {

		private static LoginThread01 t;

		public static synchronized LoginThread01 getInstance() {
			if (t == null) {
				t = new LoginThread01();
				return t;
			} else
				return t;

		}

		@Override
		public void run() {
			new Thread(LoginThread.getInstance()).start(); // 登录的操作
			try {
				Thread.sleep(1000);
				if (Login.logined) {
					setProp();
					logBut.setEnabled(false);
					onVisible(true);
					System.out.println("-----------開始發送心跳包-------------");
					new Thread(KeepThread.getInstance()).start(); // 心跳
				} else
					System.out.println("-------------登陸失敗----------------");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void init() {
		if (SystemTray.isSupported()) { // 如果操作系统支持托盘
			this.tray();
		}
		this.setSize(500, 300);
		this.setResizable(false);
		// 窗口关闭时触发事件
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}

			public void windowIconified(WindowEvent e) {
				onVisible(true);
			}
		});

	}

	/**
	 * 将窗体最小化到任务栏
	 * 
	 * @param b
	 */
	private static void onVisible(boolean b) {
		if (b)
			try {
				tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
				login.setVisible(false); // 使窗口不可视
				login.dispose();
			} catch (AWTException ex) {
				ex.printStackTrace();
			}
		else {
			tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
			login.setExtendedState(JFrame.NORMAL);
			login.setVisible(true); // 显示窗口
			login.toFront();
		}

	}

	private void exit() {
		deleteDirectory(Window.directory);
		System.exit(0); // 退出程序
	}

	private void tray() {

		tray = SystemTray.getSystemTray(); // 获得本操作系统托盘的实例
		// tray.
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/test.png")); // 将要显示到托盘中的图标

		PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
		MenuItem show = new MenuItem("open(o)");
		MenuItem exit = new MenuItem("exit(x)");
		pop.add(show);
		pop.add(exit);
		trayIcon = new TrayIcon(icon.getImage(), "Drcom", pop);

		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 鼠标双击
					onVisible(false);
				}
			}
		});
		show.addActionListener(new ActionListener() { // 点击“显示窗口”菜单后将窗口显示出来
			public void actionPerformed(ActionEvent e) {
				onVisible(false);
			}
		});
		exit.addActionListener(new ActionListener() { // 点击“退出演示”菜单后退出程序
			public void actionPerformed(ActionEvent e) {
				exit(); // 退出程序
			}
		});

	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			Boolean succeedDelete = file.delete();
			if (succeedDelete) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return true;
			}
		} else {
			System.out.println("删除单个文件" + fileName + "失败！");
			return false;
		}
	}

	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("删除目录失败" + dir + "目录不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}
		// if (!flag) {
		// System.out.println("删除目录失败");
		// return false;
		// }
		// 删除当前目录
		// if (dirFile.delete()) {
		// System.out.println("删除目录" + dir + "成功！");
		// return true;
		// } else {
		// System.out.println("删除目录" + dir + "失败！");
		// return false;
		// }
		return true;
	}

	/**
	 * 保存登录信息到配置文件中
	 */
	private static void setProp() {
		Properties prop = new Properties();
		File f = new File("drcom.properties");
		try {
			FileOutputStream oFile = new FileOutputStream("drcom.properties", false);// true表示追加打开
			prop.setProperty("password", Config.password);
			prop.setProperty("username", Config.username);
			prop.setProperty("location", Config.location + "");
			prop.store(oFile, "This is Drcom properties file");
			String string = " attrib +H  " + f.getAbsolutePath(); // 设置文件夹属性为隐藏
			Runtime.getRuntime().exec(string); //
			oFile.close();
		} catch (Exception e) {
			deleteFile(Window.directory + File.separator + Window.fileName);
			System.out.println(e);
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 */
	private void creatFile(String path, String fileName) {

		// String fileName = "myDrcom.txt";
		// 在内存中创建一个文件对象，注意：此时还没有在硬盘对应目录下创建实实在在的文件
		File f;
		if (path == null) {
			f = new File(fileName);
		} else {
			f = new File(path, fileName);
		}
		if (f.exists()) {
			// 文件已经存在，输出文件的相关信息
			System.out.println(f.getAbsolutePath());
			System.out.println(f.getName());
			System.out.println(f.length());
		} else {
			// 先创建文件所在的目录
			f.getParentFile().mkdirs();
			String string = " attrib +H  " + f.getParentFile(); // 设置文件夹属性为隐藏
			try {
				Runtime.getRuntime().exec(string); //
				// 创建新文件
				f.createNewFile();
			} catch (IOException e) {
				deleteFile(Window.directory + File.separator + Window.fileName);
				System.out.println("创建新文件时出现了错误。。。");
				e.printStackTrace();
			}
		}

	}
}
