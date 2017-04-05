package com.javautils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ganxiangyong
 * @date 2015年1月27日 下午5:12:06
 */
public class CookieUtils {

	// =================================================================
	// 获取Cookie
	/**
	 * 得到指定键的值
	 * 
	 * @param request
	 * @param key
	 *            指定的键
	 * @return key所对应value
	 */
	public static String getCookieValueByKey(HttpServletRequest request,
			String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (key.equalsIgnoreCase(cookies[i].getName()))
					return cookies[i].getValue();
			}
		}

		return null;
	}

	// =====================================================================
	// 添加cookie

	/**
	 * 添加默认时长的cookie
	 * 
	 * @param response
	 * @param key
	 * @param value
	 */
	public static void addCookie(HttpServletResponse response, String key,
			String value) {
		Cookie cookie = new Cookie(key, value);
		response.addCookie(cookie);
	}

	/**
	 * 添加指定时长的cookie
	 * 
	 * @param response
	 * @param key
	 * @param value
	 * @param maxAge
	 *            以秒为单位的正数，正值表示多长时间后该cookie过期，负值表示不持久化存储（浏览器关闭时cookie消失），
	 *            设为0时浏览器直接删除该cookie
	 */
	public static void addCookie(HttpServletResponse response, String key,
			String value, int maxAge) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * 添加详细设置的的cookie
	 * 
	 * @param response
	 * @param key
	 * @param value
	 * @param maxAge
	 * @param path
	 * @param domain
	 */
	public static void addCookie(HttpServletResponse response, String key,
			String value, int maxAge, String path, String domain) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(maxAge);
		if (!StringUtils.isBlank(domain)) {
			cookie.setDomain(domain);
		}
		if (!StringUtils.isBlank(path)) {
			cookie.setPath(path);
		}
		response.addCookie(cookie);
	}

	// ===========================================================================
	// 修改cookie

	/**
	 * 修改cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @param value
	 */

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String key, String value) {
		Cookie cookie = getCookie(request, key);
		if (cookie != null) {
			cookie.setValue(value);
			response.addCookie(cookie);
		} else {
			addCookie(response, key, value);
		}
	}

	/**
	 * 修改cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @param value
	 * @param maxAge
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String key, String value, int maxAge) {
		Cookie cookie = getCookie(request, key);
		if (cookie != null) {
			cookie.setValue(value);
			cookie.setMaxAge(maxAge);
			response.addCookie(cookie);
		} else {
			addCookie(response, key, value, maxAge);
		}
	}

	/**
	 * 修改cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 * @param value
	 * @param maxAge
	 * @param path
	 * @param domain
	 */
	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String key, String value, int maxAge,
			String path, String domain) {
		Cookie cookie = getCookie(request, key);
		if (cookie != null) {
			cookie.setValue(value);
			cookie.setMaxAge(maxAge);
			cookie.setPath(path);
			cookie.setDomain(domain);
			response.addCookie(cookie);
		} else {
			addCookie(response, key, value, maxAge, path, domain);
		}
	}

	// ==============================================================================
	// 删除cookie

	/**
	 * 删除指定key的Cookie
	 * 
	 * @param request
	 * @param response
	 * @param key
	 */
	public static void delCookie(HttpServletRequest request,
			HttpServletResponse response, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (key.equalsIgnoreCase(cookies[i].getName())) {
					Cookie cookie = cookies[i];
					// maxAge设置为0，浏览器直接删除该cookie
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	/**
	 * 删除所有的cookie
	 * 
	 * @param request
	 * @param response
	 */
	public static void delCookie(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				// maxAge设置为0，浏览器直接删除该cookie
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
	}

	// ===========================================================================
	// private

	// 获取指定key的Cookie对象
	private static Cookie getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (key.equalsIgnoreCase(cookies[i].getName()))
					return cookies[i];
			}
		}

		return null;
	}

}
