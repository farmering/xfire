package com.sdjz.eshop.util;

import java.util.ResourceBundle;

/**
 * 获取资源文件
 * 
 * @author jz
 */
public class Resources {

	/**
	 * Method getString.
	 * 
	 * @param key
	 * @return String
	 */
	public static String getString(final String key) {
		String str = "";
		try {
			str = ResourceBundle.getBundle("logDesc").getString(key);
		} catch (Exception e) {
			str = ResourceBundle.getBundle("logDesc").getString("error");
		}
		return str;
	}

	/**
	 * Method getString.
	 * 
	 * @param key
	 * @param baseName
	 * @return String
	 */
	public static String getString(final String key, final String baseName) {
		try {
			return ResourceBundle.getBundle(baseName).getString(key);
		} catch (Exception e) {
			return "EXCEPTION_STRING";
		}
	}
}