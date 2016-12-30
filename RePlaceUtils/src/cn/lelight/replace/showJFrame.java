package cn.lelight.replace;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.RePlaceUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Copyright 2016 Lelight
 *
 * All right reserved.
 *
 * @author itlowly
 * @version 创建时间：2016年12月30日 下午3:18:29 类说明
 */
public class showJFrame implements ActionListener {
	private java.util.List<RePlaceItem> rePlaceItems;
	private Container containPane;
	private JTextField inputSrcFile;
	private JButton srcFileButton;
	private JTextField inputDrcFile;
	private JButton drcFileButton;
	private JButton startButton;
	private Label hintLable;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		showJFrame obj = new showJFrame();
		obj.testJFrame();
	}

	private void testJFrame() {
		rePlaceItems = new ArrayList<>();
		rePlaceItems.add(new RePlaceItem("App Logo", "ic_launcher.png"));
		rePlaceItems.add(new RePlaceItem("闪屏页", "ic_splash_bg.png"));
		rePlaceItems.add(new RePlaceItem("引导页1", "ic_1.png"));
		rePlaceItems.add(new RePlaceItem("引导页2", "ic_2.png"));
		rePlaceItems.add(new RePlaceItem("引导页3", "ic_3.png"));
		rePlaceItems.add(new RePlaceItem("登录页 logo", "ic_icon.png"));
		rePlaceItems.add(new RePlaceItem("设备,轮播图", "ic_devcies_header.png"));

		JFrame frame = new subJFrame("App 3.0+ 版本替换");
		containPane = frame.getContentPane();
		containPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		initScrDrcFilePathPane();
		initSelectImgPane();

		initBottomButton();

		frame.setSize(850, 550);
		frame.setVisible(true);
	}

	private void initScrDrcFilePathPane() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);

		inputSrcFile = new JTextField(25);
		inputSrcFile.setToolTipText("输入源文件夹");
		srcFileButton = new JButton("选择文件夹");
		srcFileButton.addActionListener(this);
		panel.add(inputSrcFile);
		panel.add(srcFileButton);
		containPane.add(panel);

		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		inputDrcFile = new JTextField(25);
		drcFileButton = new JButton("选择文件夹");
		drcFileButton.addActionListener(this);
		panel2.add(inputDrcFile);
		panel2.add(drcFileButton);
		containPane.add(panel2);
	}

	private void initSelectImgPane() {
		for (int i = 0; i < rePlaceItems.size(); i++) {
			containPane.add(initFileSelectPanel(rePlaceItems.get(i)));
		}
	}

	private JPanel initFileSelectPanel(RePlaceItem rePlaceItem) {
		JPanel fileSelectPanel = new JPanel();
		fileSelectPanel.setBackground(Color.GRAY);
		Label label = new Label();
		label.setText(rePlaceItem.name);
		fileSelectPanel.add(label);
		rePlaceItem.actionText = new JTextField(30);
		rePlaceItem.actionButton = new JButton("选择图片");
		rePlaceItem.actionButton.addActionListener(this);

		fileSelectPanel.add(rePlaceItem.actionText);
		fileSelectPanel.add(rePlaceItem.actionButton);
		return fileSelectPanel;
	}

	private void showFileChooserAndSetTextFild(JTextField textField) {
		String srcFileDir = inputSrcFile.getText().toString().trim();
		JFileChooser jFileChooser;
		if (!"".equals(srcFileDir)) {
			// 源文件夹不为空
			File file = new File(srcFileDir);
			if (file.exists() && file.isDirectory()) {
				jFileChooser = new JFileChooser(srcFileDir);
			} else {
				jFileChooser = new JFileChooser();
			}
		} else {
			jFileChooser = new JFileChooser();
		}

		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jFileChooser.showDialog(new JLabel(), "选择");
		File file = jFileChooser.getSelectedFile();
		if (file.isDirectory()) {
			textField.setText(file.getAbsolutePath());
		} else if (file.isFile()) {
			textField.setText(file.getAbsolutePath());
		}
	}

	private void initBottomButton() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);

		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.gray);

		hintLable = new Label(
				"                                                                                                                                                                                                        ");
		panel2.add(hintLable);
		containPane.add(panel2);

		startButton = new JButton(
				"-----------------------------------------  开  始  替  换  -----------------------------------------");
		startButton.addActionListener(this);
		panel.add(startButton);

		containPane.add(panel);
	}

	/**
	 * 监听事件
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == srcFileButton) {
			showFileChooserAndSetTextFild(inputSrcFile);
		} else if (e.getSource() == drcFileButton) {
			showFileChooserAndSetTextFild(inputDrcFile);
		} else if (e.getSource() == startButton) {
			// 开始替换
			// 检查是否全部信息填写完毕
			String srcFilePath = inputSrcFile.getText().trim();
			String drcFilePath = inputDrcFile.getText().trim();

			if (srcFilePath.equals("")) {
				hintLable.setText("源文件夹路径不能为空");
				return;
			}
			if (drcFilePath.equals("")) {
				hintLable.setText("目标文件夹路径不能为空");
				return;
			}

//			for (int i = 0; i < rePlaceItems.size(); i++) {
//				if (rePlaceItems.get(i).actionText.getText().trim().equals("")) {
//					hintLable.setText(rePlaceItems.get(i).name + "路径不能为空");
//					return;
//				}
//			}
			
			String filePath = rePlaceItems.get(0).actionText.getText();
			RePlaceUtils.reNameFile(filePath, rePlaceItems.get(0).fileName);

			// 开始重命名资源
//			for (int i = 0; i < rePlaceItems.size(); i++) {
//				String filePath = rePlaceItems.get(i).actionText.getText();
//				RePlaceUtils.reNameFile(filePath, rePlaceItems.get(i).fileName);
//			}
			// 开始替换文件

			RePlaceUtils.startRePlace(srcFilePath, drcFilePath);

			// 完成?
			hintLable.setText(
					"                                                                                                                                      替换完成                                                                                                                                                                                                         ");

		} else {
			for (int i = 0; i < rePlaceItems.size(); i++) {
				if (e.getSource() == rePlaceItems.get(i).actionButton) {
					System.out.println("你点击了第" + i + "个");
					showFileChooserAndSetTextFild(rePlaceItems.get(i).actionText);
				}
			}
		}
	}

}
