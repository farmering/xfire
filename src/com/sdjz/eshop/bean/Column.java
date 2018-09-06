package com.sdjz.eshop.bean;

public class Column {
	/**  列名 **/
	private String columnName;
	/*** 属性名  **/
	private String attributeName;
	/** 首字母大写的属性名  **/
	private String upperAttributeName;
	/***  类名  ***/
	private String classType;
	/** 字段长度 */
	private int length;
	private int precision;
	private int scale;
	/*** 属性类型 ***/
	private String type;
	/***  备注  ***/
	private String remarks;
	

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getUpperAttributeName() {
		return upperAttributeName;
	}

	public void setUpperAttributeName(String upperAttributeName) {
		this.upperAttributeName = upperAttributeName;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
	

}
