package com.javautils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DSAUtils {
	private static final String DSA = "DSA";
	private static String priKey = "3081c60201003081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca404160214206e9ed83791acf3424c64db07355ccd456c073a";
	private static String pubKey = "3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100860ea08305eaf2e4767a4d6fcae621221f7febe4166423fc41d351ae0075bb8011e578f9df14392fdd84aa5a34e578cf1bf3d759f25fb9c41302b8ab50d283e7";

	/**
	 * 签名
	 * 
	 * @param data
	 * @param priKey
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String priKey) throws Exception {
		KeyFactory kf = KeyFactory.getInstance(DSA);
		KeySpec ks = new PKCS8EncodedKeySpec(HexUtils.deHexString(priKey));
		PrivateKey pk = kf.generatePrivate(ks);

		Signature signature = Signature.getInstance(DSA);
		signature.initSign(pk);
		signature.update(data);
		return HexUtils.enHexString(signature.sign());
	}

	/**
	 * 校验信息的真实性与完整性
	 * 
	 * @param data
	 * @param pubKey
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String pubKey, String signature)
			throws Exception {
		KeyFactory kf = KeyFactory.getInstance(DSA);
		KeySpec ks = new X509EncodedKeySpec(HexUtils.deHexString(pubKey));
		PublicKey pk = kf.generatePublic(ks);

		Signature s = Signature.getInstance(DSA);
		s.initVerify(pk);
		s.update(data);
		return s.verify(HexUtils.deHexString(signature));
	}

	/**
	 * 生成一对公、私钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String[] getDSAKeyPair() throws NoSuchAlgorithmException {
		String[] result = new String[2];
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(DSA);
		kpg.initialize(512, new SecureRandom());// 512, 768, or 1024
		KeyPair kp = kpg.genKeyPair();
		PrivateKey privateKey = kp.getPrivate();
		PublicKey publicKey = kp.getPublic();

		byte[] b = privateKey.getEncoded();
		result[0] = HexUtils.enHexString(b);
		b = publicKey.getEncoded();
		result[1] = HexUtils.enHexString(b);

		return result;
	}

	public static void main(String[] args) throws Exception {
		String data = "甘祥勇";

		String signature = sign(data.getBytes(), priKey);
		System.out.println(signature);

		boolean result = verify(data.getBytes(), pubKey, signature);
		System.out.println(result);

		String[] keyPair = getDSAKeyPair();
		System.out.println(keyPair[0]);
		System.out.println(keyPair[1]);
	}
}
