package com.sdjz.eshop.bean;

import java.util.List;

public class Bean {

	/*** 工作空间 ***/
	private String wookspace;

	/** entity 名称 */
	private String entityName;
	
	/** entity 名称 */
	private String tableName;

	// ** entity ***/
	private List<Column> columns;

	/*** 主键类型   **/
	private String idType;

	/** 模块名 */
	private String module;

	/** ParentPackage */
	private String packa;

	/** NameSpace */
	private String nameSpace;

	/** 首字母小写的bean名称 ***/
	private String lowerName;

	/*** 字母小写 大写字母改为_小写模式 ***/
	private String lowerName_;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getPacka() {
		return packa;
	}

	public void setPacka(String packa) {
		this.packa = packa;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getLowerName() {
		return lowerName;
	}

	public void setLowerName(String lowerName) {
		this.lowerName = lowerName;
	}

	public String getWookspace() {
		return wookspace;
	}

	public void setWookspace(String wookspace) {
		this.wookspace = wookspace;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getLowerName_() {
		return lowerName_;
	}

	public void setLowerName_(String lowerName) {
		lowerName_ = lowerName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
