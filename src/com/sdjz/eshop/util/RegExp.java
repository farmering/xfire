package com.sdjz.eshop.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * 
 * @author Lee
 * 
 */
public class RegExp {
	
	/**
	 * 判断是否符合要求
	 * @param exp 正则表达式
	 * @param target 目标字符串
	 * @return
	 */
	public static boolean test(String exp, String target) {
		boolean isMatch = false;
		Pattern pattern = Pattern.compile(exp);
		Matcher matcher = pattern.matcher(target);
		if (matcher.matches()) {
			isMatch = true;
		}
		return isMatch;
	}

	/**
	 * 获取符合要求的字符串集合
	 * @param exp 正则表达式
	 * @param target 目标字符串
	 * @return
	 */
	public static List<String> execute(String exp, String target) {
		ArrayList<String> ms = new ArrayList<String>();
		Matcher matcher = Pattern.compile(exp).matcher(target);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			if (start != end)
				ms.add(target.substring(start, end));
		}
		return ms;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean isMatch = RegExp.test("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)",
				"123.123.123.123");
		System.out.println(isMatch);
		// ArrayList<String> rs=(ArrayList<String>) RegExp.execute("",
		// "ssassbssc");
		// for(String r : rs){
		// System.out.println(r);
		// }
	}

}
