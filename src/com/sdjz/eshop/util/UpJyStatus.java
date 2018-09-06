package com.sdjz.eshop.util;

public enum UpJyStatus {
	NOTYET("未更新"), FINISH("更新完成"),TESTNONE("检验信息不符"),
	UNCONFORMITY("设备信息不符"), DATE("日期信息不符"), NONE("无此设备"),
	MULTI("设备重复"), UNKNOWN("未知原因"), NONEED("无需更新"),TESTVERDICT("检验结论未填"),
	UPDATEPIPE("更新管道检验信息失败"),UPDATEFAIL("更新失败"),NOUSER("用户名或者密码不正确");

	private String name;

	private UpJyStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {

		return super.toString();
	}

}
