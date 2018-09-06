package com.sdjz.eshop.action.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sdjz.eshop.bean.Pager;
import com.sdjz.eshop.bean.Pager.OrderType;
import com.sdjz.eshop.bean.TreeNode;
import com.sdjz.eshop.entity.Dictionary;
import com.sdjz.eshop.entity.Login;
import com.sdjz.eshop.entity.Role;
import com.sdjz.eshop.service.base.BaseService;
import com.sdjz.eshop.service.sys.LoginService;
import com.sdjz.eshop.util.JZStringUtils;
import com.sdjz.eshop.util.JsonColumn.PlatFormType;
import com.sdjz.eshop.util.JsonUtil;

/**
 * 后台Action类 - 管理中心基类
 */

public abstract class BaseAction<T> extends ActionSupport {

	private static final long serialVersionUID = 6718838822334455667L;
	protected enum ContentType{PLAIN,JSON,HTML,XML};//格式化输出格式
	/** 消息状态枚举(Error,Warn,Success) */
	protected enum Message{ERROR,WARN,SUCCESS};//返回信息类型

	public static final String VIEW = "view";
	public static final String LIST = "list";
	
	public static final String LOGIN = "LOGIN";
	public static final String STYLE = "STYLE";
	
	public static final String SYSUSER = "SYSUSER";//当前用户
	public static final String SYSORGAN = "SYSORGAN";//当前用户所在机构
	public static final String SUPERADMIN = "SUPERADMIN";//是否超级管理员
	public static final String ADMIN = "ADMIN";//是否管理员
	
	//导出Excel
	//选中导出列
	protected String exportList;
	protected String excelFileName;
	protected InputStream excelFile;
	
	protected List<Object> rows = new ArrayList<Object>();
	protected List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	protected Map<String,Object> results;
	protected String id;
	protected String[] ids;
	protected Pager pager;
	protected String sort;
	protected String direction;
	protected String redirectionUrl;// 操作提示后的跳转URL,为null则返回前一页
	protected String validateValue;
	protected String validateKey;
	protected String resourceLeftId;
	//业务新加
	protected String topIndex="0";//tab标签下标选择
	protected Boolean topView=false;//是否Top展示界面
	
	@Resource
	protected SessionFactory sessionFactory;
	
	
	/**
	 * 通过ID获取JSON格式对象
	 * @return
	 */
	public String get(){
		return ajaxJson(getBaseService().get(id));
	}
	/**
	 * 通过一个字段值获取一个对象
	 * @param String propertyName 数据库对应JavaBean中的一个变量名
	 * @param String value 变量对应的值
	 * @return
	 */
	public String getEntity(){
		String propertyName = getParameter("key");
		String value = getParameter("value");
		return ajaxJson(getBaseService().get(propertyName, value));
	}
	
	/**
	 * 通过一个字段值获取一组对象
	 * @param String propertyName 数据库对应JavaBean中的一个变量名
	 * @param String value 变量对应的值
	 * @return
	 */
	public String getList(){
		String propertyName = getParameter("key");
		String value = getParameter("value");
		return ajaxJson(getBaseService().getList(propertyName, value));
	}
	private  static byte[] gzip(String source){
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 GZIPOutputStream gzos = null;
		 try {
			// System.out.println(source.getBytes().length);
		     gzos = new GZIPOutputStream(baos);
		     gzos.write(source.getBytes("UTF-8"));
		 } catch (IOException e) {
		     e.printStackTrace();
		 } finally {
		     if (gzos != null) try { gzos.close(); } catch (IOException ignore) {};
		 }
		// System.out.println(baos.toByteArray().length);
		 return baos.toByteArray();
	}
	/**
	 * AJAX输出，返回null
	 * @param content
	 * @param type
	 * @return
	 */
	public String ajax(String content, ContentType type) {
		try {
			String contenttype= "text/html";
			if(ContentType.JSON == type){ contenttype = "application/json"; }
			if(ContentType.XML == type){ contenttype = "text/xml"; }
			if(ContentType.HTML == type){ contenttype = "text/html"; }
			if(ContentType.PLAIN == type){ contenttype = "text/plain"; }
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(contenttype + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Content-encoding", "gzip");
			response.setDateHeader("Expires", 0);
			response.getOutputStream().write((gzip(content)));
			response.getOutputStream().flush();
            response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AJAX输出文本字符串，返回null
	 * @param text
	 * @return
	 */
	public String ajaxText(String text) {
		return ajax(text, ContentType.PLAIN);
	}

	/**
	 * AJAX输出HTML字符串，返回null
	 * @param html
	 * @return
	 */
	public String ajaxHtml(String html) {
		return ajax(html, ContentType.HTML);
	}

	/**
	 * AJAX输出XML字符串，返回null
	 * @param xml
	 * @return
	 */
	public String ajaxXml(String xml) {
		return ajax(xml, ContentType.XML);
	}

	/**
	 * 根据字符串输出CeontType=html的JSON字符串，返回null
	 * @param jsonString
	 * @return
	 */
	public String ajaxJson(String jsonString) {
		return ajax(jsonString, ContentType.HTML);
	}
	
	/**
	 * 数据格式化
	 * @param object
	 * @return
	 */
	public String ajaxPagerJson(Object object, Class<?> T){
		if (!JZStringUtils.isNullOrBlankFull(getParameter("deviceid"))) {
			String json = JsonUtil.toJSONString(object, T, PlatFormType.APP);
			return ajax(json, ContentType.JSON);
		} else {
			String json = JsonUtil.toJSONString(object, T, PlatFormType.WEB);
			return ajax(json, ContentType.JSON);
		}
	}
	
	/**
	 * 数据格式化
	 * @param object
	 * @return
	 */
	public String ajaxPagerJson(Object object, Class<?> T, String format){
		if (!JZStringUtils.isNullOrBlankFull(getParameter("deviceid"))) {
			String json = JsonUtil.toJSONString(object, T, PlatFormType.APP, format);
			return ajax(json, ContentType.JSON);
		} else {
			String json = JsonUtil.toJSONString(object, T, PlatFormType.WEB, format);
			return ajax(json, ContentType.JSON);
		}
	}
	
	/**
	 * 数据格式化
	 * @param object
	 * @return
	 */
	public String ajaxJson(Object object){
		String json = JsonUtil.toJSONString(object);
		return ajax(json, ContentType.JSON);
	}
	
	/**
	 * 数据格式化
	 * @param object
	 * @return
	 */
	public String ajaxHtml(Object object){
		String json = JsonUtil.toJSONString(object);
		return ajax(json, ContentType.HTML);
	}
	
	/**
	 * 数据格式化
	 * @param object 
	 * @param format
	 * @return
	 */
	public String ajaxJson(Object object, String format){
		String json = JsonUtil.toJSONString(object, format);
		return ajax(json, ContentType.JSON);
	}
	
	/**
	 * 唯一性验证
	 * @return
	 */
	public String validata() {
		if(JZStringUtils.isNullOrBlankFull(id)){
			return ajaxJsonValidata(getBaseService().isExist(validateKey, validateValue));
		}else{
			return ajaxJsonValidata(getBaseService().isExist(validateKey, validateValue, id));
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getRedirectionUrl() {
		return redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}

	public List<Object> getRows() {
		return rows;
	}

	public void setRows(List<Object> rows) {
		this.rows = rows;
	}

	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		
		if(this.pager == null ){
			pager = new Pager();
		}
		if(sort == null || "".equals(sort)){
			sort = pager.getOrderBy();
		}
		pager.setOrderBy(sort);
		this.sort = sort;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		if(this.pager == null ){
			pager = new Pager();
		}
		if(direction == null || "".equals(direction)){
			direction = pager.getOrderType().toString();
		}
		pager.setOrderType(OrderType.valueOf(direction));
		this.direction = direction;
	}

	/**
	 * 将Pager 对象数据转换为Map数据
	 * @param pager
	 * @return
	 */
	public Map<String, Object> getResults(Pager pager) {
		results = new HashMap<String, Object>();
		results.put("pager.pageNo", pager.getPageNo());
		results.put("pager.totalRows", pager.getTotalRows());
		results.put("rows", pager.getList());
		results.put("sort", pager.getOrderBy());
		results.put("direction", pager.getOrderType().toString());
		return results;
	}

	public void setResults(Map<String, Object> results) {
		this.results = results;
	}

	public String getValidateValue() {
		return validateValue;
	}

	public void setValidateValue(String validateValue) {
		this.validateValue = validateValue;
	}

	public String getValidateKey() {
		return validateKey;
	}

	public void setValidateKey(String validateKey) {
		this.validateKey = validateKey;
	}

	public String getResourceLeftId() {
		return resourceLeftId;
	}

	public void setResourceLeftId(String resourceLeftId) {
		this.resourceLeftId = resourceLeftId;
	}

	public String getTopIndex() {
		return topIndex;
	}

	public void setTopIndex(String topIndex) {
		this.topIndex = topIndex;
	}

	public Boolean getTopView() {
		return topView;
	}

	public void setTopView(Boolean topView) {
		this.topView = topView;
	}
	
	public String getExportList() {
		return exportList;
	}

	public void setExportList(String exportList) {
		this.exportList = exportList;
	}

	public String getExcelFileName() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd ");
		String fileName = (sf.format(new Date()).toString()) + "export.xlsx";
		try {
			excelFileName = new String(fileName.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public InputStream getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(InputStream excelFile) {
		this.excelFile = excelFile;
	}
	
	/**
	 * 输出指定status的消息，返回null
	 * @param message
	 * @param {@link Message} msgType
	 * @return
	 */
	protected String ajaxJsonMessage(String message, Message msgType) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		String contenttype = "success";
		if(Message.WARN == msgType){ contenttype = "warn"; }
		if(Message.ERROR == msgType){ contenttype = "error"; }
		if(Message.SUCCESS == msgType){ contenttype = "success"; }
		
		jsonMap.put("status", contenttype);
		jsonMap.put("message", message);
		return ajaxHtml(jsonMap);
	}
	
	/**
	 * 输出JSON未通过验证，返回null
	 * @param op
	 * @return
	 */
	protected String ajaxJsonValidata(boolean op) {
		Map<String, Map<String, Object>> jsonMap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("valid", !op);//TODO 取反
		jsonMap.put("validateResult", map);
		return ajaxJson(jsonMap);
	}
	/**
	 * 输出JSON未通过验证，返回null
	 * @param op1,op2
	 * @return
	 */
	protected String ajaxJsonValidatas(boolean op1,boolean op2,boolean op3) {
		Map<String, Map<String, Object>> jsonMap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("valid1", !op1);
		map.put("valid2", !op2);
		map.put("valid3", !op3);
		jsonMap.put("validateResult", map);
		return ajaxJson(jsonMap);
	}
	protected String ajaxJsonValidatas(boolean op1,boolean op2) {
		Map<String, Map<String, Object>> jsonMap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("valid1", !op1);
		map.put("valid2", !op2);
		jsonMap.put("validateResult", map);
		return ajaxJson(jsonMap);
	}
	/**
	 * 获取字典列表对应name,建议采用新方式关联显示行政区划。及其他字典
	 * @param dList
	 * @param code
	 * @return
	 */
	@Deprecated
	protected String getDictionaryName(List<Dictionary> dList, String code) {
		if(!JZStringUtils.isNullOrBlankFull(code)){
			Dictionary _dic = null;
			for (Dictionary dic : dList) {
				if(dic.getCode().equals(code)){
					return dic.getName();
				}else if(dic.getChildren()!= null && dic.getChildren().size()>0){
					_dic = getDictionary(dic.getChildren(), code);
					if(_dic != null) return _dic.getName();
				}
			}
			return "";
		}
		return "";
	}
	
	/**
	 * 获取字典列表对应字典项,建议采用新方式关联显示行政区划。及其他字典
	 * @param dList
	 * @param code
	 * @return
	 */
	@Deprecated
	protected Dictionary getDictionary(List<Dictionary> dList, String code) {
		if(!JZStringUtils.isNullOrBlankFull(code)){
			Dictionary _dic = null;
			for (Dictionary dic : dList) {
				if(dic.getCode().equals(code)){
					return dic;
				}else if(dic.getChildren()!= null && dic.getChildren().size()>0){
					_dic = getDictionary(dic.getChildren(), code);
					if(_dic != null) return _dic;
				}
			}
			return _dic;
		}
		return null;
	}
	
	/**
	 * 获取当前登录人员,
	 * 若未登陆抛出运行时异常
	 * @return
	 */
	protected Login getLogin() {
		try {
			Login login = (Login)getSession(LOGIN);
			return login;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 获取当前登录人员角色，若未登录则返回null
	 * @return
	 */
	protected List<Role> getLoginRoleList() {
		Login login = getLogin();
		List<Role> roleList = loginService.get(login.getId()).getRoleList();
		if(roleList.size()>0)
			return roleList;
		return null;
	}
	
	/**
	 * 获取Session
	 * @param name
	 * @return
	 */
	protected Object getSession(String name) {
		ActionContext actionContext = ActionContext.getContext();
		Map<String, Object> session = actionContext.getSession();
		return session.get(name);
	}

	/**
	 * 获取Session
	 * @return
	 */
	protected Map<String, Object> getSession() {
		ActionContext actionContext = ActionContext.getContext();
		Map<String, Object> session = actionContext.getSession();
		return session;
	}

	/**
	 * 设置Session
	 * @param name
	 * @param value
	 */
	protected void setSession(String name, Object value) {
		ActionContext actionContext = ActionContext.getContext();
		Map<String, Object> session = actionContext.getSession();
		session.put(name, value);
	}

	/**
	 * 获取Request
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获取Response
	 * @return
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 获取Application
	 * @return
	 */
	protected ServletContext getApplication() {
		return ServletActionContext.getServletContext();
	}
	
	/**
	 * 获取Attribute
	 * @param name
	 * @return
	 */
	protected Object getAttribute(String name) {
		return ServletActionContext.getRequest().getAttribute(name);
	}

	/**
	 * 设置Attribute
	 * @param name
	 * @param value
	 */
	protected void setAttribute(String name, Object value) {
		ServletActionContext.getRequest().setAttribute(name, value);
	}

	/**
	 * 获取Parameter
	 * @param name
	 * @return
	 */
	protected String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * 获取Parameter数组
	 * @param name
	 * @return
	 */
	protected String[] getParameterValues(String name) {
		return getRequest().getParameterValues(name);
	}
	
	/**
	 * 获取访问系统 的 平台类型
	 * @return PlatFormType
	 */
	protected PlatFormType getPlatForm(){
		if(!JZStringUtils.isNullOrBlankFull(getParameter("deviceid"))){
			return PlatFormType.APP;
		} else {
			return PlatFormType.WEB;
		}
	}
	
	/**
	 * 通过ID获取对象
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	protected <T> T get(String id){
		return (T)getBaseService().get(id);
	}
	
	/**
	 * 设置Serivce
	 * @return
	 */
	public abstract BaseService<T, String> getBaseService();

	
}