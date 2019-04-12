package com.mht.common.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @ClassName: BackupUtils
 * @Description: 数据库备份还原工具
 * @author com.mhout.xyb
 * @date 2017年5月17日 下午3:36:38
 * @version 1.0.0
 */
public class BackupUtils {

	/**
	 * @Title: backup
	 * @Description: 数据库备份
	 * @param mysqlPath
	 *            数据库备份执行文件
	 * @param host
	 *            数据库地址
	 * @param username
	 *            数据库用户名
	 * @param password
	 *            数据库密码
	 * @param dataname
	 *            备份数据库名称
	 * @param targetfile
	 *            备份路径
	 * @author com.mhout.xyb
	 */
	public static boolean backup(String mysqlPath, String host, String username, String password, String dataname,
			String targetfile) {
		Runtime rt = Runtime.getRuntime();
		Process child = null;
		InputStream in = null;
		InputStreamReader inputStreamReader = null;
		FileOutputStream outputStream = null;
		OutputStreamWriter writer = null;
		BufferedReader bufferedReader = null;
		try {
			// 命令
			String cmd = mysqlPath + " -h " + host + " -u" + username + " -p" + password + " " + dataname;
			child = rt.exec(cmd);
			in = child.getInputStream();
			inputStreamReader = new InputStreamReader(in, "utf-8");
			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((inStr = bufferedReader.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();
			// 目标文件
			outputStream = new FileOutputStream(targetfile);
			writer = new OutputStreamWriter(outputStream, "utf-8");
			writer.write(outStr);
			writer.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (writer != null) {
					writer.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * @Title: restore
	 * @Description: 数据库还原
	 * @param mysqlPath
	 *            数据库备份执行文件
	 * @param host
	 *            数据库地址
	 * @param username
	 *            数据库用户名
	 * @param password
	 *            数据库密码
	 * @param dataname
	 *            备份数据库名称
	 * @param targetfile
	 *            执行sql文件路径
	 * @return
	 * @author com.mhout.xyb
	 */
	public static boolean restore(String mysqlPath, String host, String username, String password, String dataname,
			String targetfile) {
		Runtime runtime = Runtime.getRuntime();
		String cmd = mysqlPath + " -h" + host + " -u" + username + " -p" + password + " --default-character-set=utf8 "
				+ dataname;
		Process process;
		OutputStream outputStream = null;
		BufferedReader brReader = null;
		OutputStreamWriter writer = null;
		try {
			// 命令
			process = runtime.exec(cmd);
			outputStream = process.getOutputStream();
			// 目标文件
			brReader = new BufferedReader(new InputStreamReader(new FileInputStream(targetfile), "utf-8"));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while ((str = brReader.readLine()) != null) {
				sb.append(str + "\r\n");
			}
			str = sb.toString();
			writer = new OutputStreamWriter(outputStream, "utf-8");
			writer.write(str);
			writer.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}
				if (brReader != null) {
					brReader.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
