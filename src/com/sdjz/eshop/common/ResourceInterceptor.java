package com.sdjz.eshop.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sdjz.eshop.action.base.BaseAction;
import com.sdjz.eshop.entity.Login;
import com.sdjz.eshop.entity.Role;
import com.sdjz.eshop.service.sys.LoginService;
import com.sdjz.eshop.service.sys.VisitService;
import com.sdjz.eshop.util.EhCache;

/**
 * 拦截器 - 验证资源权限
 * 
 */
public class ResourceInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;
	@Resource
	private LoginService loginService;
	@Resource
	private VisitService visitService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext context = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
		Map<String, Object> parameters = context.getParameters();

		Login login = (Login) context.getSession().get(BaseAction.LOGIN);
		
		List<Role> roleList = loginService.get(login.getId()).getRoleList();
		if(roleList==null||roleList.size()==0){
			return "noresource";
		}
		String url = request.getRequestURI().replace(request.getContextPath(), "");
		boolean op = EhCache.getInstance().validateResource(url, parameters, roleList, request);
		/*String ip = request.getHeader("X-Forwarded-For");  
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("Proxy-Client-IP");     
	     } 
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("WL-Proxy-Client-IP");     
	      } 
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	          ip = request.getRemoteAddr();     
	     }
		Visit visit = new Visit();
		
		visit.setIp(ip);
		visit.setIspass(op);
		visit.setLogin(login);
		visit.setUrl(url);
		visitService.save(visit);
		
		visitService.flush();
		visitService.clear();*/
		if (!op) {
			//return "noresource";
		}

		return invocation.invoke();

	}

}
