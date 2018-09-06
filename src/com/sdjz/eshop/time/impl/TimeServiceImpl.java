package com.sdjz.eshop.time.impl;

import com.sdjz.eshop.time.TimeService;

public class TimeServiceImpl implements TimeService {
	public void push() {
		try {
			System.out.println("定时任务..." );
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}