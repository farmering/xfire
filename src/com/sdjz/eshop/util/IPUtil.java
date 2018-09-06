package com.sdjz.eshop.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户端 IP获取工具
 * @author Lee
 *
 */
public class IPUtil {

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static final String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
