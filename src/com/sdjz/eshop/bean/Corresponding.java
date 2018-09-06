package com.sdjz.eshop.bean;

public class Corresponding {
	
	private Class<?> clazz; // 关联实体类的Class
	private String name; //原实体类属性表
	private String referencedColumnName; //关联实体类的属性名
	private String attribute;//页面取值的属性名称
	private String where; //关联查询条件

	public Corresponding() {
		super();
	}
	
	/**
	 * 页面取值名不写。默认采用关联实体类的名称(小写)
	 * @param clazz 关联实体类的Class
	 * @param name 原实体类属性表
	 * @param referencedColumnName 关联实体类的属性名
	 */
	public Corresponding(Class<?> clazz, String name, String referencedColumnName) {
		super();
		this.clazz = clazz;
		this.name = name;
		this.referencedColumnName = referencedColumnName;
		this.attribute = clazz.getSimpleName().toLowerCase();
	}

	/**
	 * 
	 * @param clazz 关联实体类的Class
	 * @param name 原实体类属性表
	 * @param referencedColumnName 关联实体类的属性名
	 * @param attribute 页面取值的属性名称
	 * @param where 关联查询条件
	 */
	public Corresponding(Class<?> clazz, String name, String referencedColumnName, String attribute, String where) {
		super();
		this.clazz = clazz;
		this.name = name;
		this.referencedColumnName = referencedColumnName;
		this.attribute = attribute;
		this.where = where;
	}
	
	
	
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReferencedColumnName() {
		return referencedColumnName;
	}
	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
}
