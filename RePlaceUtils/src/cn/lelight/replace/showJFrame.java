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
 * @version ����ʱ�䣺2016��12��30�� ����3:18:29 ��˵��
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
		rePlaceItems.add(new RePlaceItem("����ҳ", "ic_splash_bg.png"));
		rePlaceItems.add(new RePlaceItem("����ҳ1", "ic_1.png"));
		rePlaceItems.add(new RePlaceItem("����ҳ2", "ic_2.png"));
		rePlaceItems.add(new RePlaceItem("����ҳ3", "ic_3.png"));
		rePlaceItems.add(new RePlaceItem("��¼ҳ logo", "ic_icon.png"));
		rePlaceItems.add(new RePlaceItem("�豸,�ֲ�ͼ", "ic_devcies_header.png"));

		JFrame frame = new subJFrame("App 3.0+ �汾�滻");
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
		inputSrcFile.setToolTipText("����Դ�ļ���");
		srcFileButton = new JButton("ѡ���ļ���");
		srcFileButton.addActionListener(this);
		panel.add(inputSrcFile);
		panel.add(srcFileButton);
		containPane.add(panel);

		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		inputDrcFile = new JTextField(25);
		drcFileButton = new JButton("ѡ���ļ���");
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
		rePlaceItem.actionButton = new JButton("ѡ��ͼƬ");
		rePlaceItem.actionButton.addActionListener(this);

		fileSelectPanel.add(rePlaceItem.actionText);
		fileSelectPanel.add(rePlaceItem.actionButton);
		return fileSelectPanel;
	}

	private void showFileChooserAndSetTextFild(JTextField textField) {
		String srcFileDir = inputSrcFile.getText().toString().trim();
		JFileChooser jFileChooser;
		if (!"".equals(srcFileDir)) {
			// Դ�ļ��в�Ϊ��
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
		jFileChooser.showDialog(new JLabel(), "ѡ��");
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
				"-----------------------------------------  ��  ʼ  ��  ��  -----------------------------------------");
		startButton.addActionListener(this);
		panel.add(startButton);

		containPane.add(panel);
	}

	/**
	 * �����¼�
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == srcFileButton) {
			showFileChooserAndSetTextFild(inputSrcFile);
		} else if (e.getSource() == drcFileButton) {
			showFileChooserAndSetTextFild(inputDrcFile);
		} else if (e.getSource() == startButton) {
			// ��ʼ�滻
			// ����Ƿ�ȫ����Ϣ��д���
			String srcFilePath = inputSrcFile.getText().trim();
			String drcFilePath = inputDrcFile.getText().trim();

			if (srcFilePath.equals("")) {
				hintLable.setText("Դ�ļ���·������Ϊ��");
				return;
			}
			if (drcFilePath.equals("")) {
				hintLable.setText("Ŀ���ļ���·������Ϊ��");
				return;
			}

//			for (int i = 0; i < rePlaceItems.size(); i++) {
//				if (rePlaceItems.get(i).actionText.getText().trim().equals("")) {
//					hintLable.setText(rePlaceItems.get(i).name + "·������Ϊ��");
//					return;
//				}
//			}
			
			String filePath = rePlaceItems.get(0).actionText.getText();
			RePlaceUtils.reNameFile(filePath, rePlaceItems.get(0).fileName);

			// ��ʼ��������Դ
//			for (int i = 0; i < rePlaceItems.size(); i++) {
//				String filePath = rePlaceItems.get(i).actionText.getText();
//				RePlaceUtils.reNameFile(filePath, rePlaceItems.get(i).fileName);
//			}
			// ��ʼ�滻�ļ�

			RePlaceUtils.startRePlace(srcFilePath, drcFilePath);

			// ���?
			hintLable.setText(
					"                                                                                                                                      �滻���                                                                                                                                                                                                         ");

		} else {
			for (int i = 0; i < rePlaceItems.size(); i++) {
				if (e.getSource() == rePlaceItems.get(i).actionButton) {
					System.out.println("�����˵�" + i + "��");
					showFileChooserAndSetTextFild(rePlaceItems.get(i).actionText);
				}
			}
		}
	}

}
