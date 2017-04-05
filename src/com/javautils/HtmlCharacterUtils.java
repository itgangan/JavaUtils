package com.javautils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlCharacterUtils {

	private static final Map<String, String> HTML_TAG = new HashMap<>();

	static {
		HTML_TAG.put("&nbsp;", " ");
		HTML_TAG.put("&quot;", "\"");
		HTML_TAG.put("&lt;", "<");
		HTML_TAG.put("&gt;", ">");
	}

	/**
	 * 转义常用html标签
	 * 
	 * @param html
	 * @return
	 */
	public static String transferHtmlTag(String html) {
		if (html == null) {
			return null;
		}

		StringBuffer result = new StringBuffer();

		Pattern pattern = Pattern.compile(genRegex());
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			String replacement = HTML_TAG.get(matcher.group());
			matcher.appendReplacement(result, replacement);
		}
		matcher.appendTail(result);

		return result.toString();
	}

	// 根据HTML_TAG配置，生成正则表达式（&nbsp;|&quot;|&gt;|&lt;）
	private static String genRegex() {
		StringBuilder sb = new StringBuilder();
		for (String key : HTML_TAG.keySet()) {
			sb.append(key);
			sb.append("|");
		}
		if ('|' == sb.charAt(sb.length() - 1)) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		String html = "&lt;出版时间:&nbsp;2010年07月书名:&nbsp;鸟哥的Linux私房菜(基础学习篇第3版)是否是套装:&nbsp;否出版社名称:&nbsp;人民邮电出版社编者:&nbsp;王世江定价:&nbsp;88.00元开本:&nbsp;16ISBN编号:&nbsp;9787115226266&gt;";
		String result = transferHtmlTag(html);
		System.out.println(result);
	}
}
