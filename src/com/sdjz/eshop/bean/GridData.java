package com.sdjz.eshop.bean;

import java.util.ArrayList;
import java.util.List;

public class GridData {
	private String id;
	private String name;
	private List<GridData> children = new ArrayList<GridData>();
	private String version;
	private String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GridData> getChildren() {
		return children;
	}

	public void setChildren(List<GridData> children) {
		this.children = children;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
