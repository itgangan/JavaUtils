package com.javautils;

public class HexUtils {
	private static final String HEX_STRING = "0123456789ABCDEF";
	private static final int HEX = 16;

	/**
	 * 将原始字节数组b转换成十六制的字符串表示
	 * 
	 * @param input
	 * @return
	 */
	public static String enHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_STRING.charAt(b[i] >>> 4 & 0x0F));// 这里一定要再次&0x0F,因为buy[i]>>>4之后是一个int，前面会多出很多位，当buf[i]是负数（补码表示）时会有问题
			sb.append(HEX_STRING.charAt(b[i] & 0x0F));
		}
		return sb.toString();
	}

	/**
	 * 将原始字符串input转换成十六制的字符串表示
	 * 
	 * @param input
	 * @return
	 */
	public static String enHexString(String input) {
		byte[] b = input.getBytes();
		return enHexString(b);
	}

	/**
	 * 将十六进制字符串表示的内容input转正原始字符串
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] deHexString(String input) {
		byte[] buf = new byte[input.length() / 2];
		int j = 0;
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) ((Character.digit(input.charAt(j++), HEX) << 4) | Character
					.digit(input.charAt(j++), HEX));
		}
		return buf;
	}

}
