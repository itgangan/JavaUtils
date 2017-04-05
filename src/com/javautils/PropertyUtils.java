package com.javautils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ganxiangyong
 * @date 2015年1月22日 上午10:09:18
 * 
 */
public class PropertyUtils {
	/**
	 * 加载属性文件
	 * 
	 * @param path
	 *            文件路径
	 */
	public static Properties loadProperties(final String path) {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 得到属性文件对应key的值
	 * 
	 * @param properties
	 * @param key
	 */
	public static String getValueByKey(Properties properties, String key) {
		return properties.getProperty(key);
	}

}
