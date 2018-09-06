package com.sdjz.eshop.common;

import java.util.List;

import com.sdjz.eshop.entity.Login;
import com.sdjz.eshop.entity.Role;

public interface GetRoleInterface {

	/**
	 * 根据当前登录人 取出角色模版中的角色
	 * 
	 * @param login
	 * @return
	 */
	public List<Role> getRoles(Login login);
	
}
