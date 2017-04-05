package com.javautils;

import org.apache.commons.lang3.StringUtils;

public class EmojiFilter {

	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return 一旦含有就抛出
	 */
	public static boolean containsEmoji(String source) {
		if (StringUtils.isNotBlank(source)) {
			for (int i = 0; i < source.length(); i++) {
				char codePoint = source.charAt(i);
				if (!isCommonCharacter(codePoint)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isCommonCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filterEmoji(String source) {
		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}
		// 到这里铁定包含
		StringBuilder buf = new StringBuilder(source.length());
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (isCommonCharacter(codePoint)) {
				buf.append(codePoint);
			}
		}

		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(containsEmoji("🔢🔠"));
		// 火星来客 邀请 清澈,水纹盗🔢🔠 加入群聊
		String source = "898_!@#$%^&*()_+~-=|\\	[]{}/?.>,<,水纹盗🔢🔠 加入群聊";
		String hehe = "All_微雨有声 ☀ ❤d★○●◇◆℃‰€¤〓↓↑←→※▲△△■▲※▲＃＠＼＾＿―♂♀ㄐㄗㄧㄛㄜㄋㄝㄝ";
		String result = filterEmoji(hehe);
		System.out.println("result:" + result + ";");
		
		System.out.println(StringUtils.strip("All_微雨有声 ☀ ❤d★○●◇◆℃‰€¤〓↓↑←→※▲△△■▲※▲＃＠＼＾＿―♂♀ㄐㄗㄧㄛㄜㄋㄝㄝ"));
	}
}