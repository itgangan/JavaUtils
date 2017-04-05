package com.javautils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * @author ganxiangyong
 * @date 2015年1月27日 下午7:05:40
 */
public class JSONUtils {

	/**
	 * 将JSON串解析为指定java对象
	 * 
	 * @param text
	 *            json字符串
	 * @param classType
	 *            class对象
	 * @return json串所转换java对象
	 */
	public static <T> T parseObject(String text, Class<T> classType) {
		T result = null;
		if (!StringUtils.isBlank(text)) {
			result = JSON.parseObject(text, classType);
		}
		return result;
	}

	/**
	 * 将JSON串解析为指定java对象集合
	 * 
	 * @param text
	 *            json字符串
	 * @param classType
	 *            class对象
	 * @return json串所转换java对象集合
	 */
	public static <T> List<T> parseArray(String text, Class<T> classType) {
		List<T> result = new ArrayList<T>();
		if (!StringUtils.isBlank(text)) {
			result.addAll(JSON.parseArray(text, classType));
		}
		return result;
	}

	/**
	 * 将java对象转换成json串
	 * 
	 * @param object
	 *            java对象(可以是任何java对象)
	 * @return
	 */
	public static String toJSON(Object object) {
		if (object != null) {
			return JSON.toJSONString(object);
		}
		return "";
	}

}
