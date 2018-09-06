package com.sdjz.eshop.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sdjz.eshop.action.base.BaseAction;
import com.sdjz.eshop.entity.Login;

/**
 * 拦截器 - 验证登录
 */
public class LoginInterceptor extends AbstractInterceptor {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		ActionContext context = invocation.getInvocationContext();
		
		Login login = (Login)context.getSession().get(BaseAction.LOGIN);
		
		if(login == null ){
			boolean op = isAjaxRequest((HttpServletRequest)context.get(ServletActionContext.HTTP_REQUEST));
			if(op){
				return null;
			}else{
				return "nologin";
			}
		}
		
		return invocation.invoke();
	}
	
	/**
	 * 判断是否异步请求
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) { 
	    String header = request.getHeader("X-Requested-With"); 
	    if (header != null && "XMLHttpRequest".equals(header)) 
	        return true; 
	    else 
	        return false; 
	} 

}
