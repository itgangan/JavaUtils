package com.javautils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ganxiangyong
 * @date 2015年1月28日 下午5:11:02
 */
public class MD5Utils {

	/**
	 * MD5加密
	 * 
	 * @param data
	 *            要加密的字符串
	 * @return
	 */
	public static String md5(String data) {
		if (!StringUtils.isBlank(data)) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] buf = md.digest(data.getBytes());

				return HexUtils.enHexString(buf);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 加盐的MD5加密
	 * 
	 * @param data
	 *            要加密的字符串
	 * @param salt
	 *            盐
	 * @return
	 */
	public static String md5(String data, String salt) {
		data += (StringUtils.isBlank(salt) ? "" : "$$" + salt);
		return md5(data);
	}

}
