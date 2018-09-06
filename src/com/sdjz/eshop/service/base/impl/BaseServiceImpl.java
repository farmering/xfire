package com.sdjz.eshop.service.base.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;

import com.sdjz.eshop.bean.Corresponding;
import com.sdjz.eshop.bean.Pager;
import com.sdjz.eshop.dao.BaseDao;
import com.sdjz.eshop.service.base.BaseService;


/**
 * Service实现类 - Service实现类基类
 * 
 * 
 */
public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

	private BaseDao<T, PK> baseDao;

	public BaseDao<T, PK> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao<T, PK> baseDao) {
		this.baseDao = baseDao;
	}

	public T get(PK id) {
		return baseDao.get(id);
	}

	public T load(PK id) {
		return baseDao.load(id);
	}
	
	public List<T> get(PK[] ids) {
		return baseDao.get(ids);
	}
	
	public T get(String propertyName, Object value) {
		return baseDao.get(propertyName, value);
	}
	public Map<Object, Object> getBySql(String sql) {
		return baseDao.getBySql(sql);
	}
	public List<T> getList(String propertyName, Object value) {
		return baseDao.getList(propertyName, value);
	}
	public List<Map<Object, Object>> getListBySql(String sql) {
		return baseDao.getListBySql(sql);
	}
	public List<T> getAll() {
		return baseDao.getAll();
	}
	
	public Long getTotalCount() {
		return baseDao.getTotalCount();
	}
	public Integer getRowCount(DetachedCriteria detachedCriteria){
		return baseDao.getRowCount(detachedCriteria);
	}

	public boolean isUnique(String propertyName, Object oldValue, Object newValue) {
		return baseDao.isUnique(propertyName, oldValue, newValue);
	}
	
	public boolean isExist(String propertyName, Object value) {
		return baseDao.isExist(propertyName, value);
	}
	
	public boolean isExist(String propertyName, Object value , String id) {
		return baseDao.isExist(propertyName, value,id);
	}

	public PK save(T entity) {
		return baseDao.save(entity);
	}

	public void update(T entity) {
		baseDao.update(entity);
	}

	public void delete(T entity) {
		baseDao.delete(entity);
	}

	public void delete(PK id) {
		baseDao.delete(id);
	}

	public void delete(PK[] ids) {
		baseDao.delete(ids);
	}
	
	public void delete(String propertyName, Object... values) {
		baseDao.delete(propertyName, values);
	}
	
	public void delete(String propertyName, String... values) {
		baseDao.delete(propertyName, values);
	}
	public void deleteAll() {
		baseDao.deleteAll();
	}
	public void flush() {
		baseDao.flush();
	}

	public void clear() {
		baseDao.clear();
	}
	
	public void evict(Object object) {
		baseDao.evict(object);
	}

	public Pager findByPager(Pager pager) {
		return baseDao.findByPager(pager);
	}
	
	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria) {
		return baseDao.findByPager(pager, detachedCriteria);
	}
	
	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria, List<Corresponding> correspondings) {
		return baseDao.findByPager(pager, detachedCriteria, correspondings);
	}

	public List<T> getListByCriteria(DetachedCriteria detachedCriteria) {
		return baseDao.getListByCriteria(detachedCriteria);
	}
	
	public List<Object> getObjectListByCriteria(DetachedCriteria detachedCriteria) {
		return baseDao.getObjectListByCriteria(detachedCriteria);
	}

	public void updateAll(String propertyName, Object value, PK[] ids) {
		baseDao.updateAll(propertyName, value, ids);
	}

	public void update(String sql, List<Object> list) {
		baseDao.update(sql, list);
	}

	public Integer staticByCriteria(DetachedCriteria detachedCriteria, Projection projection) {
		return baseDao.staticByCriteria(detachedCriteria, projection);
	}
	
	public T get(Map<String, Object> map) {
		return baseDao.get(map);
	}

	public List<T> getList(Map<String, Object> map) {
		return baseDao.getList(map);
	}
}
