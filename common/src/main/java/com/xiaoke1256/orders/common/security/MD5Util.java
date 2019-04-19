package com.xiaoke1256.orders.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
	// 用来将字节转换成 16 进制表示的字符
	static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getFileMD5(InputStream fis) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		byte[] buffer = new byte[2048];
		int length = -1;
		long s = System.currentTimeMillis();
		try {
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				logger.warn(ex.getMessage(), ex);
				//can ignore it.
			}
		}
		System.err.println("last: " + (System.currentTimeMillis() - s));
		byte[] b = md.digest();
		return byteToHexStringSingle(b);
	}

	/**
	 * /** 对文件全文生成MD5摘要
	 * 
	 * @param file
	 *            要加密的文件
	 * @return MD5摘要码
	 * @throws FTPException
	 */
	public static String getFileMD5(File file) {
		try {
			return getFileMD5(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/** */
	/**
	 * 对一段String生成MD5加密信息
	 * 
	 * @param message
	 *            要加密的String
	 * @return 生成的MD5信息
	 */
	public static String getMD5(String message) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			logger.info("MD5摘要长度：" + md.getDigestLength());
			byte[] b = md.digest(message.getBytes("utf-8"));
			return byteToHexStringSingle(b);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 独立把byte[]数组转换成十六进制字符串表示形式
	 * 
	 * @author Bill
	 * @create 2010-2-24 下午03:26:53
	 * @since
	 * @param byteArray
	 * @return
	 */
	public static String byteToHexStringSingle(byte[] byteArray) {
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
}