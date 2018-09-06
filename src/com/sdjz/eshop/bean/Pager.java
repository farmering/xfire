package com.sdjz.eshop.bean;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Bean类 - 分页
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class Pager {
	
	// 排序方式
	public enum OrderType{
		asc, desc
	}
	/** 每页最大记录数限制 */
	public static final Integer MAX_PAGE_SIZE = 500;

	private Integer pageNo = 1;// 当前页码
	private Integer pageSize = 20;// 每页记录数
	private Integer totalRows = 0;// 总记录数
	private Integer totalPages = 0;// 总页数
	//private String property;// 查找属性名称
	//private String keyword;// 查找关键字
	private String orderBy = "createDate";// 排序字段
	private OrderType orderType = OrderType.desc;// 排序方式 --单一字段排序时使用
	private LinkedHashMap<String, OrderType> orderByList ; //排序方式 --多字段按顺序排序时使用
	private List<?> list;// 数据List

	/***********  以下是为了查询标准设置的字段 2013-05-20  ******************/
		
	private boolean findAll = false ;//2012-10-11 李杨添加 标志是否获取所有记录
	private String loseKeyword;// 查找关键字
	private String pro;// 备用。。。。。。1
	private String[] orderByItems = null;// 排序字段
	private Object object;//用来存储对象
	private String typeKeyword;
	private String value;//字段的值
	private String keys;//字段的名称
	
	private String value1;//字段1的值
	private String keys1;//字段1的名称
	
	private String property1;
	private String keyword1;
	private String property2;
	private String keyword2;
	private String property3;
	private String keyword3;
	private String property4;
	private String keyword4;
	private String property5;
	private String keyword5;
	private String property6;
	private String keyword6;
	private String property7;
	private String keyword7;
	private String property8;
	private String keyword8;
	private String property9;
	private String keyword9;
	private String property10;
	private String keyword10;
	private String property11;
	private String keyword11;
	private String property12;
	private String keyword12;
	private String property13;
	private String keyword13;
	private String property14;
	private String keyword14;
	private String property15;
	private String keyword15;
		
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		if (pageNo < 1) {
			pageNo = 1;
		}
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		} else if(pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}
	
	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getTotalPages() {
		totalPages = totalPages / pageSize;
		if (totalPages % pageSize > 0) {
			totalPages ++;
		}
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public LinkedHashMap<String, OrderType> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(LinkedHashMap<String, OrderType> orderByList) {
		this.orderByList = orderByList;
	}

	public boolean isFindAll() {
		return findAll;
	}

	public void setFindAll(boolean findAll) {
		this.findAll = findAll;
	}

	public String getLoseKeyword() {
		return loseKeyword;
	}

	public void setLoseKeyword(String loseKeyword) {
		this.loseKeyword = loseKeyword;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public String[] getOrderByItems() {
		return orderByItems;
	}

	public void setOrderByItems(String[] orderByItems) {
		this.orderByItems = orderByItems;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getTypeKeyword() {
		return typeKeyword;
	}

	public void setTypeKeyword(String typeKeyword) {
		this.typeKeyword = typeKeyword;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getKeys1() {
		return keys1;
	}

	public void setKeys1(String keys1) {
		this.keys1 = keys1;
	}

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getProperty3() {
		return property3;
	}

	public void setProperty3(String property3) {
		this.property3 = property3;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}

	public String getProperty4() {
		return property4;
	}

	public void setProperty4(String property4) {
		this.property4 = property4;
	}

	public String getKeyword4() {
		return keyword4;
	}

	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
	}

	public String getProperty5() {
		return property5;
	}

	public void setProperty5(String property5) {
		this.property5 = property5;
	}

	public String getKeyword5() {
		return keyword5;
	}

	public void setKeyword5(String keyword5) {
		this.keyword5 = keyword5;
	}

	public String getProperty6() {
		return property6;
	}

	public void setProperty6(String property6) {
		this.property6 = property6;
	}

	public String getKeyword6() {
		return keyword6;
	}

	public void setKeyword6(String keyword6) {
		this.keyword6 = keyword6;
	}

	public String getProperty7() {
		return property7;
	}

	public void setProperty7(String property7) {
		this.property7 = property7;
	}

	public String getKeyword7() {
		return keyword7;
	}

	public void setKeyword7(String keyword7) {
		this.keyword7 = keyword7;
	}

	public String getProperty8() {
		return property8;
	}

	public void setProperty8(String property8) {
		this.property8 = property8;
	}

	public String getKeyword8() {
		return keyword8;
	}

	public void setKeyword8(String keyword8) {
		this.keyword8 = keyword8;
	}

	public String getProperty9() {
		return property9;
	}

	public void setProperty9(String property9) {
		this.property9 = property9;
	}

	public String getKeyword9() {
		return keyword9;
	}

	public void setKeyword9(String keyword9) {
		this.keyword9 = keyword9;
	}

	public String getProperty10() {
		return property10;
	}

	public void setProperty10(String property10) {
		this.property10 = property10;
	}

	public String getKeyword10() {
		return keyword10;
	}

	public void setKeyword10(String keyword10) {
		this.keyword10 = keyword10;
	}

	public String getProperty11() {
		return property11;
	}

	public void setProperty11(String property11) {
		this.property11 = property11;
	}

	public String getKeyword11() {
		return keyword11;
	}

	public void setKeyword11(String keyword11) {
		this.keyword11 = keyword11;
	}

	public String getProperty12() {
		return property12;
	}

	public void setProperty12(String property12) {
		this.property12 = property12;
	}

	public String getKeyword12() {
		return keyword12;
	}

	public void setKeyword12(String keyword12) {
		this.keyword12 = keyword12;
	}

	public String getProperty13() {
		return property13;
	}

	public void setProperty13(String property13) {
		this.property13 = property13;
	}

	public String getKeyword13() {
		return keyword13;
	}

	public void setKeyword13(String keyword13) {
		this.keyword13 = keyword13;
	}

	public String getProperty14() {
		return property14;
	}

	public void setProperty14(String property14) {
		this.property14 = property14;
	}

	public String getKeyword14() {
		return keyword14;
	}

	public void setKeyword14(String keyword14) {
		this.keyword14 = keyword14;
	}

	public String getProperty15() {
		return property15;
	}

	public void setProperty15(String property15) {
		this.property15 = property15;
	}

	public String getKeyword15() {
		return keyword15;
	}

	public void setKeyword15(String keyword15) {
		this.keyword15 = keyword15;
	}


}