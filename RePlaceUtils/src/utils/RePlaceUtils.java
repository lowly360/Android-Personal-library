package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Lelight
 *
 * All right reserved.
 *
 * @author itlowly
 * @version 创建时间：2016年12月30日 下午2:10:24 类说明
 */
public class RePlaceUtils {
	// 源目录下所有文件名已经路径
	private static List<FileBean> srcFiles;
	// 目标目录下所有文件名已经路径
	private static List<FileBean> drcFiles;

	public static void startRePlace(String srcPath, String drsPath) {
		srcFiles = getFilesNameAndPath(srcPath);
		drcFiles = getFilesNameAndPath(drsPath);
		// 两者对比,并替换源文件到目标文件
		for (int i = 0; i < srcFiles.size(); i++) {
			for (int j = 0; j < drcFiles.size(); j++) {
				System.out.println("比较两者名字:"+srcFiles.get(i).name+":"+drcFiles.get(j).name);
				if (srcFiles.get(i).name.equals(drcFiles.get(j).name)) {
					System.out.println("两者相等-----------------------");
					try {
						// 两者名字一样,替换
						File srcfile = new File(srcFiles.get(i).path);
						File drcfile = new File(drcFiles.get(j).path);
						System.out.println("开始替换:\n" + srcFiles.get(i).path +
								"\n" + drcFiles.get(j).path);
						copyFile(srcfile, drcfile);
						System.out.println("替换成功: " + srcFiles.get(i).name);
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("发生异常:" + srcFiles.get(i).name + "\n" + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * 获取目录下所有文件名字以及路径
	 */
	private static List<FileBean> getFilesNameAndPath(String path) {
		List<FileBean> list = new ArrayList<>();
		File file = new File(path);
		if (file.exists()) {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				if (listFiles[i].isFile()) {
					FileBean fileBean = new FileBean();
					fileBean.name = listFiles[i].getName();
					fileBean.path = listFiles[i].getAbsolutePath();
					list.add(fileBean);
				} else if (listFiles[i].isDirectory()) {
					System.out.println("文件夹:" + listFiles[i].getAbsolutePath());
					list.addAll(getFilesNameAndPath(listFiles[i].getAbsolutePath()));
				}
			}
		} else {
			System.out.println("源目录不存在");
		}
		return list;
	}

	public static void copyFile(File fromFile, File toFile) throws IOException {
		FileInputStream ins = new FileInputStream(fromFile);
		FileOutputStream out = new FileOutputStream(toFile);
		byte[] b = new byte[1024];
		int n = 0;
		while ((n = ins.read(b)) != -1) {
			out.write(b, 0, b.length);
		}
		ins.close();
		out.close();
	}

	public static void reNameFile(String srcPath, String newname) {
		String oldname;
		File file = new File(srcPath);
		oldname = file.getName();

		srcPath = srcPath.replace(oldname, "");
		System.out.println(srcPath + "======" + oldname + " 新名字:" + newname);

		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(srcPath + "/" + oldname);
			File newfile = new File(srcPath + "/" + newname);
			if (!oldfile.exists()) {
				return;// 重命名文件不存在
			}
			if (newfile.exists()) // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
				System.out.println(newname + "已经存在！");
			else {
				oldfile.renameTo(newfile);
			}
		} else {
			System.out.println("新文件名和旧文件名相同...");
		}

	}

}
