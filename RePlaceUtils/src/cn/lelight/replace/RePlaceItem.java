package cn.lelight.replace;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Copyright 2016 Lelight
 *
 * All right reserved.
 *
 * @author itlowly
 * @version 创建时间：2016年12月30日 下午4:07:00 类说明
 */
public class RePlaceItem {
	public String name;
	public String fileName;
	public String drspath;
	public JButton actionButton;
	public JTextField actionText;

	public RePlaceItem(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
	}
}
