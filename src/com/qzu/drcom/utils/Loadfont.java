package com.qzu.drcom.utils;

import java.awt.Font;
import java.io.File;
import com.sun.javafx.tk.FontLoader;

public class Loadfont {
	private static Font loadFont(String fontFileName, float fontSize) // 第一个参数是外部字体名，第二个是字体大小
	{
		try {
			String pathString = FontLoader.class.getClass().getResource(fontFileName).getFile();
			Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, new File(pathString));
			Font dynamicFontPt = dynamicFont.deriveFont(fontSize);
			return dynamicFontPt;
		} catch (Exception e)// 异常处理
		{
			e.printStackTrace();
			return new Font("宋体", Font.PLAIN, 14);
		}
	}

	public static Font LagerFont() {
		Font font = Loadfont.loadFont("/font/xjlFont.ttf", 40f);// 调用
		return font;// 返回字体
	}

	public static Font diyFont(String fontFileName, float fontSize) {
		// String root = System.getProperty("user.dir");// 项目根目录路径
		Font font = Loadfont.loadFont(fontFileName, fontSize);
		return font;// 返回字体
	}

	public static Font defaultFont() {
		Font font = Loadfont.loadFont("/font/xjlFont.ttf", 18f);
		return font;// 返回字体
	}

	public static Font otherFont() {
		String root = System.getProperty("user.dir");// 项目根目录路径
		Font font = Loadfont.loadFont(root + "/font/xjlFont.ttf", 16f);
		return font;// 返回字体
	}
}
