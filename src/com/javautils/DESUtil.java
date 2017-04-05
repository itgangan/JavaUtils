package com.javautils;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES加密算法，key只能是64位（8个字符），其中有效的密钥只有56位，因为其中第8、16、24、32、40、48、56、64位作为奇偶校验位。
 * 
 * @author ganxiangyong
 * @date 2015年7月22日 下午4:42:38
 */
public class DESUtil {
	private final static String DEFAULT_KEY = "_default";
	private final static String DES = "DES";

	/**
	 * DES加密
	 * 
	 * @param src
	 *            待加密字符串
	 * @param desKey
	 *            密钥 ，不少于64位（8个字符，当desKey>64位时，大于的部分会被忽略）
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] src, byte[] desKey) throws Exception {
		// 生成密钥
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		KeySpec keySpec = new DESKeySpec(desKey);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		// 加密
		Cipher cipher = Cipher.getInstance(DES);// 创建密码器
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);// 初始化
		return cipher.doFinal(src);
	}

	/**
	 * DES解密
	 * 
	 * @param src
	 *            待加密字符串
	 * @param desKey
	 *            密钥 ，不少于64位（8个字符，当desKey>64位时，大于的部分会被忽略）
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] src, byte[] desKey) throws Exception {
		// 生成密钥
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		KeySpec keySpec = new DESKeySpec(desKey);
		SecretKey securekey = keyFactory.generateSecret(keySpec);
		// 解密
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		return cipher.doFinal(src);
	}

	/**
	 * 使用DES算法加密data
	 * 
	 * @param data
	 * @param desKey
	 * @return
	 */
	public static String encrypt(String data, String desKey) {
		try {
			byte[] b = encrypt(data.getBytes(), desKey.getBytes());
			return HexUtils.enHexString(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用DES算法解密data
	 * 
	 * @param data
	 * @param desKey
	 *            8个字符
	 * @return
	 */
	public static String decrypt(String data, String desKey) {
		try {
			byte[] b = HexUtils.deHexString(data);
			b = decrypt(b, desKey.getBytes());
			return new String(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用默认的密钥进行DES算法加密data
	 * 
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		return encrypt(data, DEFAULT_KEY);
	}

	/**
	 * 使用默认的密钥进行使用DES算法解密data
	 * 
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		return decrypt(data, DEFAULT_KEY);
	}

	public static void main(String[] args) throws Exception {
		String result = encrypt("甘祥勇");
		System.out.println(result);

		String bb = new String(decrypt(result));
		System.out.println(bb);
	}

}
