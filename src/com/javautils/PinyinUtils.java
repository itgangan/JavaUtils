package com.javautils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ganxiangyong
 * @date 2015年1月28日 上午11:50:59
 */
public class PinyinUtils {

	private static final String CHINESE_PATTERN = "[\u4E00-\u9FA5]+";

	/**
	 * 获取指定字符串中的所有的中文字符的拼音
	 * 
	 * @param input
	 *            需要获取拼音的字符串
	 * @return 拼音字符串（比如：甘祥勇缘吕返回ganxiangyongyuanlv）
	 */
	public static String getPinyin(String input) {
		if (StringUtils.isBlank(input)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		String[] pinyin = null;
		StringBuilder sb = new StringBuilder();
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			try {
				if (String.valueOf(c[i]).matches(CHINESE_PATTERN)) {
					pinyin = PinyinHelper
							.toHanyuPinyinStringArray(c[i], format);
					sb.append(pinyin[0]);
				} else {
					sb.append(c[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

}
