package com.sdjz.eshop.util;



/**
 * Service层中使用该runtimeExcepiton回滚事物
 * @author ChaoWang
 *
 */
public class AppException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public AppException (String msg){
		super(msg);
	}
	
}
