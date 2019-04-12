package com.mht.common.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;

public class MD5Utils {

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static String make(String source) {
		return make(source.getBytes());
	}

	public static String make(byte[] source) {
		try {
			java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			md5.update(source);
			return toHexString(md5.digest());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String makeFile(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 手机端登录产生的token
	 * 
	 * @param loginName
	 * @return
	 */
	public static String generateToken(String loginName) {
		return make("generateToken" + loginName + new Date());
	}

}
