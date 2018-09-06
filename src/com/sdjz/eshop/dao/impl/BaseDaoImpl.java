package com.sdjz.eshop.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sdjz.eshop.bean.Corresponding;
import com.sdjz.eshop.bean.Pager;
import com.sdjz.eshop.bean.Pager.OrderType;
import com.sdjz.eshop.dao.BaseDao;
import com.sdjz.eshop.util.JZStringUtils;
import com.sdjz.eshop.util.ReflectGetValue;

/**
 * Dao实现类 - Dao实现类基类
 * 
 */
@Repository
public class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {

	private Class<T> entityClass;
	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.entityClass = null;
		Class c = getClass();
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<T>) parameterizedType[0];
        }
	}

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T get(PK id) {
		Assert.notNull(id, "id is required");
		return (T) getSession().get(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public T load(PK id) {
		Assert.notNull(id, "id is required");
		return (T) getSession().load(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> get(PK[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		String hql = "from " + entityClass.getName() + " as model where model.id in(:ids)";
		return getSession().createQuery(hql).setParameterList("ids", ids).list();
	}
	
	@SuppressWarnings("unchecked")
	public T get(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		return (T) getSession().createQuery(hql).setParameter(0, value).uniqueResult();
	}
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getBySql(String sql) {
		return (Map)getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
	}
	public void updateAll(String propertyName, Object value ,PK[] ids) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		Assert.notEmpty(ids, "ids must not be empty");
		String hql = "update " + entityClass.getName() + " set " + propertyName + " = ? where id in(:ids)";
		getSession().createQuery(hql).setParameter(0, value).setParameterList("ids", ids).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getList(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		return getSession().createQuery(hql).setParameter(0, value).list();
	}
	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> getListBySql(String sql) {
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	@SuppressWarnings("unchecked")
	public List<T> getListByCriteria(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getObjectListByCriteria(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public T get(Map<String, Object> map) {
		
		StringBuffer sb = new StringBuffer("from ");
		sb.append(entityClass.getName());
		sb.append(" as model ");
		int i = 0;
		
		for(String key : map.keySet()){
			Assert.hasText(key, "propertyName must not be empty");
			Assert.notNull(map.get(key), "value is required");
			if(i == 0){
				sb.append(" where ");
			}else{
				sb.append(" and ");
			}
			sb.append(" model.").append(key).append(" = ?");
			i++;
		}
		Query query = getSession().createQuery(sb.toString());
		
		int j = 0;
		for(String key : map.keySet()){
			query.setParameter(j, map.get(key));
			j++;
		}
		
		return (T) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getList(Map<String, Object> map) {
		StringBuffer sb = new StringBuffer("from ");
		sb.append(entityClass.getName());
		sb.append(" as model ");
		int i = 0;
		
		for(String key : map.keySet()){
			Assert.hasText(key, "propertyName must not be empty");
			Assert.notNull(map.get(key), "value is required");
			if(i == 0){
				sb.append(" where ");
			}else{
				sb.append(" and ");
			}
			sb.append(" model.").append(key).append(" = ?");
			i++;
		}
		Query query = getSession().createQuery(sb.toString());
		
		int j = 0;
		for(String key : map.keySet()){
			query.setParameter(j, map.get(key));
			j++;
		}
		
		return query.list();
	}


	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		String hql = "from " + entityClass.getName();
		return getSession().createQuery(hql).list();
	}
	
	public Long getTotalCount() {
		String hql = "select count(*) from " + entityClass.getName();
		return (Long) getSession().createQuery(hql).uniqueResult();
	}
	
	public Integer getRowCount(DetachedCriteria detachedCriteria){	
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());	
		Long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return totalCount.intValue();
	}
	public boolean isUnique(String propertyName, Object oldValue, Object newValue) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(newValue, "newValue is required");
		if (newValue == oldValue || newValue.equals(oldValue)) {
			return true;
		}
		if (newValue instanceof String) {
			if (oldValue != null && StringUtils.equalsIgnoreCase((String) oldValue, (String) newValue)) {
				return true;
			}
		}
		T object = get(propertyName, newValue);
		return (object == null);
	}
	
	public boolean isExist(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		List<T> list = getList(propertyName, value);
		return (list != null && list.size()>0);
	}
	
	@SuppressWarnings("unchecked")
	public boolean isExist(String propertyName, Object value, String id) {
		Assert.notNull(id, "id is required");
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(value, "value is required");
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ? and model.id != ? ";
		List<T> list = (List<T>) getSession().createQuery(hql).setParameter(0, value).setParameter(1, id).list();//uniqueResult()
		return (list != null && list.size()>0);
	}

	@SuppressWarnings("unchecked")
	public PK save(T entity) {
		Assert.notNull(entity, "entity is required");
		return (PK) getSession().save(entity);
	}

	public void update(T entity) {
		Assert.notNull(entity, "entity is required");
		getSession().update(entity);
	}

	public void delete(T entity) {
		Assert.notNull(entity, "entity is required");
		getSession().delete(entity);
	}

	public void delete(PK id) {
		Assert.notNull(id, "id is required");
		T entity = load(id);
		getSession().delete(entity);
	}

	public void delete(PK[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		for (PK id : ids) {
			T entity = load(id);
			getSession().delete(entity);
		}
	}
	
	public void delete(String propertyName, Object... values){
		/*
		T entity = get(propertyName, value);
		getSession().delete(entity);
		 */
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(values, "value is required");
		for (Object value : values) {
			Query query = getSession().createQuery("delete "+entityClass.getName()+" where "+propertyName+"=?");
			query.setParameter(0, value);
			query.executeUpdate();
		}
		
	}
	
	public void delete(String propertyName, String... values){
		Assert.hasText(propertyName, "propertyName must not be empty");
		Assert.notNull(values, "value is required");
		for (String value : values) {
			Query query = getSession().createQuery("delete "+entityClass.getName()+" where "+propertyName+"=?");
			query.setParameter(0, value);
			query.executeUpdate();
		}
		
	}
	public void deleteAll(){
		Query query = getSession().createQuery("delete "+entityClass.getName());
		query.executeUpdate();
	}
	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	public void evict(Object object) {
		Assert.notNull(object, "object is required");
		getSession().evict(object);
	}
	
	public Pager findByPager(Pager pager) {
		if (pager == null) {
			pager = new Pager();
		}
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
		return findByPager(pager, detachedCriteria);
	}

	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria) {
		if (pager == null) {
			pager = new Pager();
		}
		Integer pageNumber = pager.getPageNo();
		Integer pageSize = pager.getPageSize();
		String orderBy = pager.getOrderBy();
		OrderType orderType = pager.getOrderType();
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		Long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		
		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		//ROOT_ENTITY
		criteria.setFirstResult((pageNumber - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		/**
		 * 添加功能：运行多个字段分别升序或降序排序 2013-04-07	
		usage:
				......
			LinkedHashMap<String, OrderType> orderFields = new LinkedHashMap<String, OrderType>();
			orderFields.put("isRead", OrderType.asc);
			orderFields.put("createDate", OrderType.desc);
			pager.setOrderByList(orderFields);
			pager = standardWarnMessageService.findByPager(pager, detachedCriteria);
				.....
		 */
		//单字段排序
		if(pager.getOrderByList()==null || pager.getOrderByList().isEmpty()){
			if (StringUtils.isNotEmpty(orderBy) && orderType != null) {
				if (orderType == OrderType.asc) {
					criteria.addOrder(Order.asc(orderBy));
					criteria.addOrder(Order.asc("id"));
				} else {
					criteria.addOrder(Order.desc(orderBy));
					criteria.addOrder(Order.desc("id"));
				}
		   }
		}else{//多字段 分别排序
			LinkedHashMap<String, OrderType> maps = pager.getOrderByList() ;
			for (Iterator<String> it =  maps.keySet().iterator();it.hasNext();)
			   {
			    String key_2 = it.next();
			    OrderType ot = maps.get(key_2);
			    if (ot == OrderType.asc) {
					criteria.addOrder(Order.asc(key_2));
				} else {
					criteria.addOrder(Order.desc(key_2));
				}
			 }
		}
		pager.setTotalRows(totalCount.intValue());
		pager.setList(criteria.list());
		return pager;
	}
	
	@SuppressWarnings("unchecked")
	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria, List<Corresponding> correspondings) {
		if (pager == null) {
			pager = new Pager();
		}
		Integer pageNumber = pager.getPageNo();
		Integer pageSize = pager.getPageSize();
		String orderBy = pager.getOrderBy();
		OrderType orderType = pager.getOrderType();
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		
		Long totalCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		
		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		//ROOT_ENTITY
		criteria.setFirstResult((pageNumber - 1) * pageSize);
		criteria.setMaxResults(pageSize);
	
		//单字段排序
		if(pager.getOrderByList()==null || pager.getOrderByList().isEmpty()){
			if (StringUtils.isNotEmpty(orderBy) && orderType != null) {
				if (orderType == OrderType.asc) {
					criteria.addOrder(Order.asc(orderBy));
					criteria.addOrder(Order.asc("id"));
				} else {
					criteria.addOrder(Order.desc(orderBy));
					criteria.addOrder(Order.desc("id"));
				}
		   }
		}else{//多字段 分别排序
			LinkedHashMap<String, OrderType> maps = pager.getOrderByList() ;
			for (Iterator<String> it =  maps.keySet().iterator();it.hasNext();)
			   {
			    String key_2 = it.next();
			    OrderType ot = maps.get(key_2);
			    if (ot == OrderType.asc) {
					criteria.addOrder(Order.asc(key_2));
				} else {
					criteria.addOrder(Order.desc(key_2));
				}
			 }
		}
		pager.setTotalRows(totalCount.intValue());
		List<T> list = criteria.list();
		List<Map<Object, Object>> resoutList = new ArrayList<Map<Object,Object>>();
		Map<String,Map<Object,Object> > cache  = new HashMap<String,Map<Object,Object>>();
		for(T t : list){
			try {
				Map<Object,Object> rsmap = ReflectGetValue.getFieldValue(t);
				if(correspondings!=null)
				for(Corresponding corresponding : correspondings){
					StringBuffer hql = new StringBuffer();
					hql.append("from ").append(corresponding.getClazz().getName()).append(" where ").append(corresponding.getReferencedColumnName()).append(" = ? ");
					if(!JZStringUtils.isNullOrBlankFull(corresponding.getWhere())){
						hql.append(" and ").append(corresponding.getWhere());
					}
					Map<Object,Object> rsmap1 = null;
					if(cache.containsKey(hql.toString()+rsmap.get(corresponding.getName()))){
						rsmap1 = cache.get(hql.toString()+rsmap.get(corresponding.getName()));
					}else{
						Object object = getSession().createQuery(hql.toString()).setParameter(0, rsmap.get(corresponding.getName())).uniqueResult();
						if(object != null){
							rsmap1 = ReflectGetValue.getFieldValue(object);
						}else{
							rsmap1 = new HashMap<Object,Object>(0);
						}
						cache.put(hql.toString()+rsmap.get(corresponding.getName()), rsmap1);
					}
					rsmap.put(corresponding.getAttribute(), rsmap1);
				}
				resoutList.add(rsmap);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cache = null;
		pager.setList(resoutList);
		return pager;
	}
	
	public void update(String sql , List<Object> list){
		
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		
		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size(); i++ ){
				Object obj = list.get(i);
				sqlQuery.setParameter(i, obj);
			}
		}
		
		sqlQuery.executeUpdate();
	}

	public Integer staticByCriteria(DetachedCriteria detachedCriteria, Projection projection) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		Long number = (Long)criteria.setProjection(projection).uniqueResult();
		return number.intValue();
	}
}