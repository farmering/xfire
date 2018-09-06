package com.sdjz.eshop.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.alibaba.fastjson.JSON;
import com.sdjz.eshop.bean.ResourceCaches;
import com.sdjz.eshop.entity.Dictionary;
import com.sdjz.eshop.entity.Login;
import com.sdjz.eshop.entity.Resourceitem;
import com.sdjz.eshop.entity.Role;
import com.sdjz.eshop.service.sys.ResourceitemService;
import com.sdjz.eshop.service.sys.impl.DictionaryServiceImpl;

public class EhCache {

	private Cache resourceCache = (Cache)SpringUtil.getBean("resourceCache");
	private ResourceitemService resourceitemService = (ResourceitemService)SpringUtil.getBean("resourceitemServiceImpl");

	private static EhCache ehCache;
	
	private EhCache(){
		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void lizy(){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Resourceitem.class);
		
		criteria.add(Restrictions.eq("isdelete", false));
		
		criteria.add(Restrictions.isNotNull("url"));
		//Oracle can not Support.
		//criteria.add(Restrictions.ne("url", ""));
		
		List<Resourceitem> resourceitemList = resourceitemService.getListByCriteriaInitialize(criteria);
		
		for(Resourceitem resourceitem : resourceitemList){
			
			String url = resourceitem.getUrl();
			
			if(!JZStringUtils.isNullOrBlankFull(url)){
				
				ResourceCaches rc = new ResourceCaches();
				rc.setId(resourceitem.getId());
				rc.setRoleList(resourceitem.getRoleList());
				
				if(url.indexOf("?") >= 0){
					
					String mapStr = url.substring(url.indexOf("?")+1);
					
					if(!JZStringUtils.isNullOrBlankFull(mapStr)){
						
						String[] mapStrs = mapStr.split("&");
						
						if(mapStrs != null && mapStrs.length > 0){
							
							Map<String, List<String>> map = new HashMap<String, List<String>>();
							
							for(String s : mapStrs){
								
								if(!JZStringUtils.isNullOrBlankFull(s)){
									
									String[] ss = s.split("=");
									
									if(ss != null && ss.length > 1){
										
										List<String> vList = new ArrayList<String>();
										
										if(map.get(ss[0]) != null && map.get(ss[0]).size() > 0){
											
											vList = map.get(ss[0]);
											
										}
										if(!vList.contains(ss[1].trim())){
											
											vList.add(ss[1].trim());
										}
										
										map.put(ss[0], vList);
									}
									
								}
								
							}
							
							if(map != null && map.size() > 0){
								
								rc.setMap(map);
							}
							
						}
						
					}
					
					url = url.substring(0,url.indexOf("?"));
					
				}
				
				Element element = resourceCache.get(url);
				
				List<ResourceCaches> rcList = new ArrayList<ResourceCaches>();
				
				if(element != null && element.getValue() != null){
					
					rcList = (List<ResourceCaches>)element.getValue();
					
				}
				
				rcList.add(rc);
				element = new Element(url, rcList);
				
				resourceCache.put(element);
			}
			
		}
		
	}
	
	public void fush(){
		
		resourceitemService.flush();
		resourceitemService.clear();
		
		resourceCache.removeAll();
		
		ehCache.lizy();
		
	}
	
	public static EhCache getInstance(){
		
		if(ehCache == null ){
			
			ehCache = new EhCache();
			
			ehCache.lizy();
		}
		
		return ehCache;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean validateResource(String url,Map<String, Object> map,List<Role> roleList,HttpServletRequest request){
		
		boolean validate = true;
		
		List<String> list = resourceCache.getKeys();
		
		boolean rsisf = list.contains(url);
		
		request.setAttribute("rsisf", rsisf);
		validateDic(roleList,request);//角色管理字典表设备
		if(rsisf){
			
			validate = false;
			
			List<ResourceCaches> resourceCatchesList = (List<ResourceCaches>)resourceCache.get(url).getValue();
			
			Object obid = map.get("resourceLeftId");
			
			if(obid!=null){
				
				Object[] obids = (Object[])obid;
				
				if(obids !=null && obids.length > 0){
					
					String id = obids[0].toString().trim();
					for(ResourceCaches resourceCaches : resourceCatchesList){
						
						if(id.equals(resourceCaches.getId())){
							
							DetachedCriteria criteria = DetachedCriteria.forClass(Resourceitem.class);
							criteria.add(Restrictions.eq("isdelete", false));
							criteria.add(Restrictions.eq("parent.id", id));
							List<Resourceitem> resourceitempageList = resourceitemService.getListByCriteria(criteria);
							
							List<String> rsunList = new ArrayList<String>();
							
							for(Role role : roleList){
								
								if(resourceCaches.getRoleList() != null 
										&& resourceCaches.getRoleList().size() > 0 
										&& resourceCaches.getRoleList().contains(role)){
									
									for(Resourceitem rsi : resourceitempageList){
										
										if(role.getResourceitemList().contains(rsi)){
											
											String un = rsi.getName()+rsi.getUrl();
											
											if(!rsunList.contains(un)){
												
												rsunList.add(un);
											}
										}
										
									}
									
									validate = true;
									
								}
								
							}
							
							request.setAttribute("rsunJson", JSON.toJSON(rsunList));
							
							return validate;
						}
						
					}
					
				}
				
			}else{
				
				List<String> rsunList = new ArrayList<String>();
				
				for(ResourceCaches resourceCaches : resourceCatchesList){
					
					Map<String, List<String>> rmap = resourceCaches.getMap();
					
					if(comMap(rmap,map)){
						
						DetachedCriteria criteria = DetachedCriteria.forClass(Resourceitem.class);
						criteria.add(Restrictions.eq("isdelete", false));
						criteria.add(Restrictions.eq("parent.id", resourceCaches.getId()));
						List<Resourceitem> resourceitempageList = resourceitemService.getListByCriteria(criteria);
						for(Role role : roleList){
							
							if(resourceCaches.getRoleList() != null 
									&& resourceCaches.getRoleList().size() > 0 
									&& resourceCaches.getRoleList().contains(role)){
								
								for(Resourceitem rsi : resourceitempageList){
									
									if(role.getResourceitemList().contains(rsi)){
										
										String un = rsi.getName()+rsi.getUrl();
										
										if(!rsunList.contains(un)){
											
											rsunList.add(un);
										}
									}
									
								}
								
								validate = true;
								
							}
							
						}
						
					}
					
				}
				
				request.setAttribute("rsunJson", JSON.toJSON(rsunList));
				
			}
			
		}
		return validate;
	}
	/**
	 * 角色管理字典表设备
	 */
	@SuppressWarnings("unchecked")
	public void validateDic(List<Role> roleList,HttpServletRequest request){
		DictionaryServiceImpl dictionaryService = (DictionaryServiceImpl)SpringUtil.getBean("dictionaryServiceImpl");
		List<String> rdicLis=(List<String>) request.getSession().getAttribute("roleDictonaryList");
		List<Role> roleSessionList=(List<Role>) request.getSession().getAttribute("roleSessionList");
		if(roleSessionList==null){
			roleSessionList=new ArrayList<Role>();
		}
		if(rdicLis==null||!roleList.containsAll(roleSessionList)){
			List<String> rdicList = new ArrayList<String>();
			DetachedCriteria criteria = DetachedCriteria.forClass(Dictionary.class);
			criteria.add(Restrictions.eq("isdelete", false));
			criteria.addOrder(Order.asc("sort"));
			criteria.add(Restrictions.eq("type", "SBZL"));
			List<Dictionary> dicList = dictionaryService.getListByCriteria(criteria);
			for(Role role : roleList){
				for(Dictionary dic : dicList){
					if(role.getDictionaryList().contains(dic)){
						String code = dic.getCode();
						if(!rdicList.contains(code)){
							rdicList.add(code);
						}
					}
					
				}
			}
			request.getSession().setAttribute("roleDictonaryList", rdicList);
			request.getSession().setAttribute("roleSessionList", roleList);
			request.setAttribute("rdicJson", JSON.toJSON(rdicList));
		}else{
			request.setAttribute("rdicJson", JSON.toJSON(rdicLis));
		}
	}
	private boolean comMap(Map<String, List<String>> rmap , Map<String , Object> map){
		
		if(rmap != null && rmap.size() > 0 ){
			
			if(map != null && map.size() > 0){
				
				if(map.keySet().containsAll(rmap.keySet())){
					
					for(String key : rmap.keySet()){
						
						List<String> rvlist = rmap.get(key);
						List<String> vlist = new ArrayList<String>();
						
						Object o = map.get(key);
						
						if(o!=null){
							
							Object[] os = (Object[])o;
							
							if(os !=null && os.length > 0){
								
								for(Object obj : os){
									
									if(obj != null){
										
										if(!vlist.contains(obj.toString().trim())){
											
											vlist.add(obj.toString().trim());
										}
										
									}
									
								}
								
							}
							
						}
						
						if(!vlist.containsAll(rvlist)){
							
							return false;
						}
					}
					
				}else{
					
					return false;
				}
				
			}else{
				
				return false;
			}
			
		}
		
		return true;
	}

	
	public static void main(String[] args) {
		List<ResourceCaches> list1 = new ArrayList<ResourceCaches>();
		ResourceCaches caches1 = new ResourceCaches();
		caches1.setId("1");
		ResourceCaches caches2 = new ResourceCaches();
		caches2.setId("2");
		ResourceCaches caches3 = new ResourceCaches();
		caches3.setId("3");
		list1.add(caches1);
		list1.add(caches2);
		list1.add(caches3);
		
		List<ResourceCaches> list2 = new ArrayList<ResourceCaches>();
		list2.addAll(list1);
		
		for(ResourceCaches rc : list1){
			
			if("2".equals(rc.getId())){
				
				list2.remove(rc);
			}
			
		}
		
		System.out.println(list1.size());
		System.out.println(list2.size());
	}
	

	@SuppressWarnings({ "unchecked", "deprecation" })
	public  void saveResource(Resourceitem resourceitem){
		
		String url = resourceitem.getUrl();
		
		if(!JZStringUtils.isNullOrBlankFull(url)){
			
			ResourceCaches rc = new ResourceCaches();
			rc.setId(resourceitem.getId());
			rc.setRoleList(new ArrayList<Role>());
			
			if(url.indexOf("?") >= 0){
				
				String mapStr = url.substring(url.indexOf("?")+1);
				
				if(!JZStringUtils.isNullOrBlankFull(mapStr)){
					
					String[] mapStrs = mapStr.split("&");
					
					if(mapStrs != null && mapStrs.length > 0){
						
						Map<String, List<String>> map = new HashMap<String, List<String>>();
						
						for(String s : mapStrs){
							
							if(!JZStringUtils.isNullOrBlankFull(s)){
								
								String[] ss = s.split("=");
								
								if(ss != null && ss.length > 1){
									
									List<String> vList = new ArrayList<String>();
									
									if(map.get(ss[0]) != null && map.get(ss[0]).size() > 0){
										
										vList = map.get(ss[0]);
										
									}
									if(!vList.contains(ss[1].trim())){
										
										vList.add(ss[1].trim());
									}
									
									map.put(ss[0], vList);
								}
								
							}
							
						}
						
						if(map != null && map.size() > 0){
							
							rc.setMap(map);
						}
						
					}
					
				}
				
				url = url.substring(0,url.indexOf("?"));
				
			}
			
			Element element = resourceCache.get(url);
			
			List<ResourceCaches> rcList = new ArrayList<ResourceCaches>();
			
			if(element != null && element.getValue() != null){
				
				rcList = (List<ResourceCaches>)element.getValue();
				
			}
			
			rcList.add(rc);
			element = new Element(url, rcList);
			
			resourceCache.put(element);
			
		}
		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void updateResource(Resourceitem resourceitem){
		
		boolean op = true;
		
		List<String> keyList = resourceCache.getKeys();
		
		for(String key : keyList){
			
			if(op){
				
				Element element = resourceCache.get(key);
				
				if(element != null && element.getValue() != null){
					
					List<ResourceCaches> rcList = (List<ResourceCaches>)element.getValue();
					
					List<ResourceCaches> rcListCopy = new ArrayList<ResourceCaches>();
					rcListCopy.addAll(rcList);
					
					for(ResourceCaches rc : rcList){
						
						if(op){
							
							if(resourceitem.getId().equals(rc.getId())){
								
								op = false;
								
								String url = resourceitem.getUrl();
								
								if(!JZStringUtils.isNullOrBlankFull(url)){
									
									ResourceCaches rc1 = new ResourceCaches();
									rc1.setId(resourceitem.getId());
									rc1.setRoleList(rc.getRoleList());
									
									if(url.indexOf("?") >= 0){
										
										String mapStr = url.substring(url.indexOf("?")+1);
										
										if(!JZStringUtils.isNullOrBlankFull(mapStr)){
											
											String[] mapStrs = mapStr.split("&");
											
											if(mapStrs != null && mapStrs.length > 0){
												
												Map<String, List<String>> map = new HashMap<String, List<String>>();
												
												for(String s : mapStrs){
													
													if(!JZStringUtils.isNullOrBlankFull(s)){
														
														String[] ss = s.split("=");
														
														if(ss != null && ss.length > 1){
															
															List<String> vList = new ArrayList<String>();
															
															if(map.get(ss[0]) != null && map.get(ss[0]).size() > 0){
																
																vList = map.get(ss[0]);
																
															}
															if(!vList.contains(ss[1].trim())){
																
																vList.add(ss[1].trim());
															}
															
															map.put(ss[0], vList);
														}
														
													}
													
												}
												
												if(map != null && map.size() > 0){
													
													rc1.setMap(map);
												}
												
											}
											
										}
										
										url = url.substring(0,url.indexOf("?"));
										
									}
									
									if(url.equals(key)){
										
										rcListCopy.remove(rc);
										rcListCopy.add(rc1);
										
										element = new Element(key,rcListCopy);
										
										resourceCache.put(element);
										
									}else{
										
										rcListCopy.remove(rc);
										
										element = new Element(key,rcListCopy);
										
										resourceCache.put(element);
										
										Element element1 = resourceCache.get(url);
										
										List<ResourceCaches> rcList1 = new ArrayList<ResourceCaches>();
										
										if(element1 != null && element1.getValue() != null){
											
											rcList1 = (List<ResourceCaches>)element1.getValue();
											
										}
										
										rcList1.add(rc1);
										element1 = new Element(url, rcList1);
										
										resourceCache.put(element1);
										
									}
									
									
								}else{
									
									rcListCopy.remove(rc);
									
									element = new Element(key,rcListCopy);
									
									resourceCache.put(element);
									
								}
								
								break;
							}
							
						}
						
					}
					
				}
				
			}else{
				
				break;
			}
			
		}
		
		if(!op){
			
			String url = resourceitem.getUrl();
			
			if(!JZStringUtils.isNullOrBlankFull(url)){
				
				ResourceCaches rc = new ResourceCaches();
				rc.setId(resourceitem.getId());
				rc.setRoleList(resourceitem.getRoleList());
				
				if(url.indexOf("?") >= 0){
					
					String mapStr = url.substring(url.indexOf("?")+1);
					
					if(!JZStringUtils.isNullOrBlankFull(mapStr)){
						
						String[] mapStrs = mapStr.split("&");
						
						if(mapStrs != null && mapStrs.length > 0){
							
							Map<String, List<String>> map = new HashMap<String, List<String>>();
							
							for(String s : mapStrs){
								
								if(!JZStringUtils.isNullOrBlankFull(s)){
									
									String[] ss = s.split("=");
									
									if(ss != null && ss.length > 1){
										
										List<String> vList = new ArrayList<String>();
										
										if(map.get(ss[0]) != null && map.get(ss[0]).size() > 0){
											
											vList = map.get(ss[0]);
											
										}
										if(!vList.contains(ss[1].trim())){
											
											vList.add(ss[1].trim());
										}
										
										map.put(ss[0], vList);
									}
									
								}
								
							}
							
							if(map != null && map.size() > 0){
								
								rc.setMap(map);
							}
							
						}
						
					}
					
					url = url.substring(0,url.indexOf("?"));
					
				}
				
				Element element = resourceCache.get(url);
				
				List<ResourceCaches> rcList = new ArrayList<ResourceCaches>();
				
				if(element != null && element.getValue() != null){
					
					rcList = (List<ResourceCaches>)element.getValue();
					
				}
				
				rcList.add(rc);
				element = new Element(url, rcList);
				
				resourceCache.put(element);
				
			}
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void deleteResource(String[] ids){
		
		List<String> idList = Arrays.asList(ids);
		
		List<String> keyList = resourceCache.getKeys();
		
		for(String key : keyList){
			
			Element element = resourceCache.get(key);
			
			if(element != null && element.getValue() != null){
				
				List<ResourceCaches> rcList = (List<ResourceCaches>)element.getValue();
				
				List<ResourceCaches> rcListCopy = new ArrayList<ResourceCaches>();
				rcListCopy.addAll(rcList);
				
				for(ResourceCaches rc : rcList){
					
					if(idList.contains(rc.getId())){
						
						rcListCopy.remove(rc);
						
					}
					
				}
				
				element = new Element(key, rcListCopy);
				
				resourceCache.put(element);
			}
			
		}
		
	}
	
}
