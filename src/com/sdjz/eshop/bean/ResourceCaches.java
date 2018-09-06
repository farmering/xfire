package com.sdjz.eshop.bean;

import java.util.List;
import java.util.Map;

import com.sdjz.eshop.entity.Role;

public class ResourceCaches {
	
	private String id;
	
	private Map<String, List<String>> map;
	
	private List<Role> roleList;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, List<String>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<String>> map) {
		this.map = map;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	

}
