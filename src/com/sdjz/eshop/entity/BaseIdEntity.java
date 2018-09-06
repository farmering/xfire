package com.sdjz.eshop.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
import com.sdjz.eshop.util.JsonColumn;

/**
 * 实体类 - 基类
 * 
 */
@MappedSuperclass
public class BaseIdEntity implements Serializable {

	private static final long serialVersionUID = -3516335736674260627L;
	@JsonColumn
	private String id;// ID
	@JsonColumn
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createDate;// 创建日期
	@JsonColumn
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date modifyDate;// 修改日期
	/*
	 * 非数据库参数，
	 * 仅限于详细页面中显示非实体类的文本，以及查询条件未在实体类中定义字段时使用。
	 */
	private String attribute0;
	private String attribute1;
	private String attribute2;
	private String attribute6;
	private String attribute7;
	private String attribute8;
	@JSONField(format="yyyy-MM-dd")
	private Date attribute3;
	@JSONField(format="yyyy-MM-dd")
	private Date attribute4;
	@JSONField(format="yyyy-MM-dd")
	private Date attribute5;
	@Id
	@Column(length = 32, nullable = true)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "assigned")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(updatable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass().getPackage() != obj.getClass().getPackage()) {
			return false;
		}
		final BaseIdEntity other = (BaseIdEntity) obj;
		if (id == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!id.equals(other.getId())) {
			return false;
		}
		return true;
	}
	@Transient
	public String getAttribute0() {
		return attribute0;
	}

	public void setAttribute0(String attribute0) {
		this.attribute0 = attribute0;
	}
	@Transient
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	@Transient
	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	@Transient
	public Date getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(Date attribute3) {
		this.attribute3 = attribute3;
	}
	@Transient
	public Date getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(Date attribute4) {
		this.attribute4 = attribute4;
	}
	@Transient
	public Date getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(Date attribute5) {
		this.attribute5 = attribute5;
	}
	@Transient
	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
	@Transient
	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}
	@Transient
	public String getAttribute8() {
		return attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}

}