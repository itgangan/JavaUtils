package com.javautils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密用于替换DES加密，其密钥长度的最少支持为128、192、256。在java中，只支持128位密钥长度。
 * 
 * @author ganxiangyong
 * @date 2015年7月22日 下午4:55:54
 */
public class AESUtils {
	private static final String AES = "AES";
	public static final String DEFAULT_KEY = "this_default_key"; // 128位（16个字符串）

	public static byte[] encrypt(byte[] data, byte[] aesKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(AES);
		kgen.init(128, new SecureRandom(aesKey));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
		Cipher cipher = Cipher.getInstance(AES);// 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		return cipher.doFinal(data); // 加密
	}

	public static byte[] decrypt(byte[] data, byte[] aesKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(AES);
		kgen.init(128, new SecureRandom(aesKey));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
		Cipher cipher = Cipher.getInstance(AES);// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		return cipher.doFinal(data); // 解密
	}

	public static void main(String[] args) throws Exception {
		byte[] a = encrypt("甘祥勇".getBytes(), DEFAULT_KEY.getBytes());
		String result = HexUtils.enHexString(a);
		System.out.println(result);

		String b = new String(decrypt(HexUtils.deHexString(result),
				DEFAULT_KEY.getBytes()));
		System.out.println(new String(b));
	}
}
