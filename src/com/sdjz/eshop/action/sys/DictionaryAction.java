package com.sdjz.eshop.action.sys;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sdjz.eshop.action.base.BaseAction;
import com.sdjz.eshop.bean.Pager;
import com.sdjz.eshop.bean.TreeNode;
import com.sdjz.eshop.bean.Pager.OrderType;
import com.sdjz.eshop.entity.Dictionary;
import com.sdjz.eshop.entity.User;
import com.sdjz.eshop.service.base.BaseService;
import com.sdjz.eshop.service.sys.DictionaryService;
import com.sdjz.eshop.service.sys.UserService;
import com.sdjz.eshop.util.JZStringUtils;

public class DictionaryAction extends BaseAction<Dictionary> {

	private static final long serialVersionUID = -1584749211521718323L;
	private Dictionary dictionary;
	private String parentId;
	private String mycode;
	private Integer dsort;
	private String code;
	private boolean isdelete;
	
	/** 是否一致加载所有数据 */
	private boolean all = true;//是否一致加载所有数据，
	/** 是否开启异步 */
	private boolean async = false;//是否开启异步
	/** 使用code字段座位select的key值，div的id=code值 */
	private boolean iscode = false;//使用code字段座位select的key值，div的id=code值

	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private UserService userService;

	public String list(){
		return LIST;
	}
	
	//ajax 获取数据JSON格式
	public String getDictionaryPager(){
		
		if(pager == null ){
			pager = new Pager();
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
		criteria.add(Restrictions.eq("isdelete", false));
		if(dictionary != null ){
			if(!JZStringUtils.isNullOrBlankFull(dictionary.getName())){
				criteria.add(Restrictions.like("name", dictionary.getName(), MatchMode.ANYWHERE));
			}
		}

		if(!JZStringUtils.isNullOrBlankFull(parentId)){
			criteria.add(Restrictions.eq("parent.id", parentId));
		}else{
			criteria.add(Restrictions.isNull("parent"));
		}
		
		pager = dictionaryService.findByPager(pager,criteria);
		
		return ajaxJson(getResults(pager));
	}

	public String add(){
		
		pager = new Pager();
		pager.setPageSize(1);
		pager.setOrderBy("sort");
		pager.setOrderType(OrderType.desc);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
		criteria.add(Restrictions.eq("isdelete", false));
		
		if(!JZStringUtils.isNullOrBlankFull(parentId)){
			criteria.add(Restrictions.eq("parent.id", parentId));
		}else{
			criteria.add(Restrictions.isNull("parent"));
		}
		
		pager = dictionaryService.findByPager(pager,criteria);
		
		if(pager!=null && pager.getList() != null 
				&& pager.getList().size() > 0 && pager.getList().get(0)!=null 
				&& ((Dictionary)pager.getList().get(0)).getSort() != null ){
			dsort = ((Dictionary)pager.getList().get(0)).getSort().intValue()+1;
		}else{
			dsort = 0;
		}
		Dictionary father =  dictionaryService.get(parentId);
		mycode = father.getCode()+String.format("%03d", (dsort+1));

		return INPUT;
	}
	
	public String edit(){
		dictionary = dictionaryService.get(id);
		return INPUT;
	}

	public String save(){
		
		try {
			
			if(!JZStringUtils.isNullOrBlankFull(parentId)){
				Dictionary dictionary1 = dictionaryService.get(parentId);
				if(dictionary1 != null){
					dictionary.setParent(dictionary1);
					dictionary.setParentCode(dictionary1.getCode());
					if(dictionary1.getParent() == null ){
						dictionary.setType(dictionary.getCode());
					}else{
						dictionary.setType(dictionary1.getType());
					}
				}
			}
			
			if(dictionary.getSort() >= dsort){
				dictionary.setSort(dsort);
				dictionaryService.save(dictionary);
			}else{
				
				DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
				criteria.add(Restrictions.eq("isdelete", false));
				criteria.addOrder(Order.asc("sort"));
				criteria.add(Restrictions.ge("sort", dictionary.getSort()));
				if(dictionary.getParent() != null ){
					criteria.add(Restrictions.eq("parent", dictionary.getParent()));
				}else{
					criteria.add(Restrictions.isNull("parent"));
				}
				
				List<Dictionary> dictionaryList = dictionaryService.getListByCriteria(criteria);
				dictionaryService.saveAll(dictionary,dictionaryList);
				
			}
			return ajaxJsonMessage("添加成功",Message.SUCCESS);
		} catch (Exception e) {
			return ajaxJsonMessage("添加失败",Message.ERROR);
		}
	}

	public String update(){

		try {
			
			if(!JZStringUtils.isNullOrBlankFull(parentId)){
				Dictionary dictionary1 = dictionaryService.get(parentId);
				if(dictionary1 != null ){
					dictionary.setParent(dictionary1);
					dictionary.setParentCode(dictionary1.getCode());
					if(dictionary1.getParent() == null ){
						dictionary.setType(dictionary.getCode());
					}else{
						dictionary.setType(dictionary1.getType());
					}
				}
			}
			
			if(dictionary.getSort() < dsort){
				
				DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
				criteria.add(Restrictions.eq("isdelete", false));
				criteria.addOrder(Order.asc("sort"));
				criteria.add(Restrictions.ge("sort", dictionary.getSort()));
				criteria.add(Restrictions.lt("sort", dsort));
				if(dictionary.getParent() != null ){
					criteria.add(Restrictions.eq("parent", dictionary.getParent()));
				}else{
					criteria.add(Restrictions.isNull("parent"));
				}
				
				List<Dictionary> dictionaryList = dictionaryService.getListByCriteria(criteria);
				
				dictionaryService.updateAllA(dictionary, dictionaryList);
				
			}else if(dictionary.getSort() == dsort){
				
				dictionaryService.update(dictionary);
				
			}else{
				
				DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
				criteria.add(Restrictions.eq("isdelete", false));
				criteria.addOrder(Order.asc("sort"));
				criteria.add(Restrictions.le("sort", dictionary.getSort()));
				criteria.add(Restrictions.gt("sort", dsort));
				if(dictionary.getParent() != null ){
					criteria.add(Restrictions.eq("parent", dictionary.getParent()));
				}else{
					criteria.add(Restrictions.isNull("parent"));
				}
				
				List<Dictionary> dictionaryList = dictionaryService.getListByCriteria(criteria);
				
				dictionaryService.updateAllC(dictionary,dictionaryList,dsort);
			}

			return ajaxJsonMessage("修改成功",Message.SUCCESS);

		} catch (Exception e) {
			return ajaxJsonMessage("修改失败",Message.ERROR);
		}
	}
	
	public String delete(){
		
		List<Dictionary> list = dictionaryService.get(ids);
		
		for(Dictionary dictionary : list){
			if(dictionary.getChildren() != null && dictionary.getChildren().size() > 0){
				return ajaxJsonMessage("此分类下包含子分类，请先删除子分类",Message.ERROR);
			}
		}
		try {
			dictionaryService.deleteAll(ids);
			return ajaxJsonMessage("删除成功",Message.SUCCESS);
		} catch (Exception e) {
			return ajaxJsonMessage("删除失败",Message.ERROR);
		}
		
	}
	
	/***
	 * 获取树形组织结构树
	 * @return
	 */
	public String getDepartmentsTree(){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
		criteria.add(Restrictions.eq("isdelete", false));
		criteria.addOrder(Order.asc("sort"));
		if(JZStringUtils.isNullOrBlankFull(parentId)){
			criteria.add(Restrictions.isNull("parent"));
		}else{
			criteria.add(Restrictions.eq("parent.id", parentId));
		}
		List<Dictionary> list = dictionaryService.getListByCriteria(criteria);
		for(Dictionary dictionary : list){
			
			TreeNode treeNode = new TreeNode();
			treeNode.setId(dictionary.getId());
			treeNode.setParentId(dictionary.getParent()!=null ? dictionary.getParent().getId() : "0");
			treeNode.setName(dictionary.getName());
			//默认打开
			treeNode.setOpen(false);
			if(dictionary.getChildren() != null && dictionary.getChildren().size() > 0){
				treeNode.setIsParent(true);
			}else{
				treeNode.setIsParent(false);
			}
			
			treeNodes.add(treeNode);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("treeNodes", treeNodes);
		
		return ajaxJson(map);
	}
	
	/**
	 * 通用查询字典
	 * @return
	 */
	protected List<Dictionary> findDictionary(){
		DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
		criteria.add(Restrictions.eq("isdelete", false));
		criteria.addOrder(Order.asc("sort"));
		criteria.createAlias("parent", "p");
		
		if (!JZStringUtils.isNullOrBlankFull(id)){
			criteria.add(Restrictions.eq("p.code", id));
	    }else{
	    	//查询全部数据
	    	if(all){
	    		criteria.add(Restrictions.eq("p.code", code));
	    	}else{
				//添加地区过滤
				User user = userService.get(getLogin().getId());//获取登录用户
				if(user.getOrgan()!= null && !JZStringUtils.isNullOrBlankFull(user.getOrgan().getRegionno())){
					String localeCode = user.getOrgan().getRegionno();
					//criteria.add(Restrictions.eq("p.code", localeCode));
					criteria.add(Restrictions.or(
						Restrictions.eq("p.code", localeCode), 
						Restrictions.eq("code", localeCode)
					));
					//String localeCode = user.getOrgan().getRegionno().substring(0,4);
					//criteria.add(Restrictions.eq("p.code", localeCode + "00"));
				}
	    	}
			
	    }
		return dictionaryService.getListByCriteria(criteria);
	}
	
	/**
	 * tree型解析树，
	 * async 异步是否开启，默认不开启
	 * iscode tree型ID值是否code，默认false，存ID
	 * @param list
	 */
	protected void setCh(List<Dictionary> list) {

		if (list != null) {
			for (Dictionary dictionary : list) {

				TreeNode treeNode = new TreeNode();
				if(iscode){
					treeNode.setId(dictionary.getCode());
					treeNode.setParentId(dictionary.getParent() != null ? dictionary.getParent().getCode() : code);
				} else {
					treeNode.setId(dictionary.getId());
					treeNode.setParentId(dictionary.getParent() != null ? dictionary.getParent().getId() : "0");
				}
				treeNode.setName(dictionary.getName());
				// 默认不开启异步
				if(async){
					//判断是否存在子节点
					if ((dictionary.getChildren() != null) && (dictionary.getChildren().size() > 0)) {
						treeNode.setIsParent(true);
					} else {
						treeNode.setIsParent(false);
					}
					treeNode.setOpen(false);
					treeNodes.add(treeNode);
				}else{
					treeNode.setOpen(true);
					treeNodes.add(treeNode);
					setCh(dictionary.getChildren());
				}
			}
		}
	}

	/**
	 * 获取字典树,异步树型选择器
	 * @return
	 */
	public String getDictionaryTree(){
		List<Dictionary> list = this.findDictionary();
		
		setCh(list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("treeNodes", treeNodes);
		return ajaxJson(map);
	}
	
	/**
	 * 行政区划查询，
	 * 可根据用户所在单位的行政区划查询字典，
	 * 适用于异步树型选择器    获取数据。
	 * @return
	 */
	public String getTreeArea(){
		this.async = true;
		this.iscode = true;
		List<Dictionary> list = this.findDictionary();
		setCh(list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("treeNodes", treeNodes);
		
		return ajaxJson(map);
	}
	
	/**
	 * 业务内，通用字典查询。(PS：所有业务用到的字典都在“其他字典”下。)
	 * 适用于 select元素异步获取数据。
	 * @return
	 */
	public String getDictionaryTree2(){
		List<Dictionary> list = this.findDictionary();
		
		List<HashMap<String,String>> listMap = new ArrayList<HashMap<String,String>>();
		if (list != null) {
			for (Dictionary dictionary : list) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("value", dictionary.getCode());
				map.put("key", dictionary.getName());
				listMap.add(map);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", listMap);
		
		return ajaxJson(map);
	}
	
	/**
	 * 业务内，通用字典查询。
	 * 适用于 双向选择器
	 * @return
	 */
	public String getDictionaryTree3(){
		
		List<Dictionary> list = this.findDictionary();
		
		List<HashMap<String,String>> listMap = new ArrayList<HashMap<String,String>>();
		if (list != null) {
			for (Dictionary dictionary : list) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("value", dictionary.getCode());
				map.put("key", dictionary.getName());
				listMap.add(map);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromList", listMap);
		
		return ajaxJson(map);
	}
	/**
	 * 获取字典中代码编号
	 * @return
	 */
	public String getDictionaryNo(){
		List<Dictionary> list = this.findDictionary();
		List<HashMap<String,String>> listMap = new ArrayList<HashMap<String,String>>();
		if (list != null) {
			for (Dictionary dictionary : list) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("value", dictionary.getCode());
				map.put("key", dictionary.getName());
				map.put("codeNo", dictionary.getCodeNo());
				map.put("value1", dictionary.getCode1());
				listMap.add(map);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", listMap);
		
		return ajaxJson(map);
	}
	/**
	 * 行政区划查询，
	 * 可根据用户所在单位的行政区划查询字典
	 * 适用于 select元素异步获取数据。
	 * @return
	 */
	public String getSelectDicArea(){
		
		List<Dictionary> list = this.findDictionary();
		
		List<HashMap<String,String>> listMap = new ArrayList<HashMap<String,String>>();
		if (list != null) {
			for (Dictionary dictionary : list) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("value", dictionary.getCode());
				map.put("key", dictionary.getName());
				listMap.add(map);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", listMap);
		
		return ajaxJson(map);
	}
	
	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getDsort() {
		return dsort;
	}

	public void setDsort(Integer dsort) {
		this.dsort = dsort;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}
	
	public String getMycode() {
		return mycode;
	}

	public void setMycode(String mycode) {
		this.mycode = mycode;
	}

	public boolean isAll() {
		return all;
	}

	public void setAll(boolean all) {
		this.all = all;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isIscode() {
		return iscode;
	}

	public void setIscode(boolean iscode) {
		this.iscode = iscode;
	}

	@Override
	public BaseService<Dictionary, String> getBaseService() {
		return dictionaryService;
	}
}
