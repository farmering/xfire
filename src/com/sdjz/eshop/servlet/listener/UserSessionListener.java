package com.sdjz.eshop.servlet.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.sdjz.eshop.entity.User;
public class UserSessionListener implements HttpSessionListener{
	public void sessionCreated(HttpSessionEvent event) {
		
	}
	@SuppressWarnings("unchecked")
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		ServletContext application = session.getServletContext();
		User user=(User)session.getAttribute("SYSUSER");
		List<String> list = (List<String>) application.getAttribute("userlist");
		if(list!=null&&user!=null&&list.contains(user.getId())){
			list.remove(user.getId());
		}
	}
}