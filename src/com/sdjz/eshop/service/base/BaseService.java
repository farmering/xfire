package com.sdjz.eshop.service.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;

import com.sdjz.eshop.bean.Corresponding;
import com.sdjz.eshop.bean.Pager;

/**
 * Service接口 - Service接口基类
 * 
 */

public interface BaseService<T, PK extends Serializable> {

	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id
	 *            记录ID
	 * @return 实体对象
	 */
	public T get(PK id);

	/**
	 * 根据ID获取实体对象.
	 * 
	 * @param id
	 *            记录ID
	 * @return 实体对象
	 */
	public T load(PK id);
	
	/**
	 * 获取所有实体对象集合.
	 * 根据只有组合DetachedCriteria对象
	 * @return 实体对象集合
	 */
	public List<T> getListByCriteria(DetachedCriteria detachedCriteria);
	
	/**
	 * 获取所有实体对象集合.
	 * 根据只有组合DetachedCriteria对象
	 * @return 实体对象集合
	 */
	public List<Object> getObjectListByCriteria(DetachedCriteria detachedCriteria);
	
	/**
	 * 根据ID数组获取实体对象集合.
	 * 
	 * @param ids
	 *            ID对象数组
	 * 
	 * @return 实体对象集合
	 */
	public List<T> get(PK[] ids);
	
	/**
	 * 根据属性名和属性值获取实体对象.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 实体对象
	 */
	public T get(String propertyName, Object value);
	public Map<Object, Object> getBySql(String sql);
	/**
	 * 根据属性名和属性值获取实体对象集合.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 实体对象集合
	 */
	public List<T> getList(String propertyName, Object value);
	public List<Map<Object, Object>> getListBySql(String sql);
	/**
	 * 获取所有实体对象集合.
	 * 
	 * @return 实体对象集合
	 */
	public List<T> getAll();
	
	/**
	 * 获取所有实体对象总数.
	 * 
	 * @return 实体对象总数
	 */
	public Long getTotalCount();
	public Integer getRowCount(DetachedCriteria detachedCriteria);
	/**
	 * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param oldValue
	 *            修改前的属性值
	 * @param oldValue
	 *            修改后的属性值
	 * @return boolean
	 */
	public boolean isUnique(String propertyName, Object oldValue, Object newValue);
	
	/**
	 * 根据属性名判断数据是否已存在.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * @return boolean
	 */
	public boolean isExist(String propertyName, Object value);
	
	/**
	 * 根据属性名判断数据是否已存在.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * @return boolean
	 */
	public boolean isExist(String propertyName, Object value,String id);

	/**
	 * 保存实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @return ID
	 */
	public PK save(T entity);

	/**
	 * 更新实体对象.
	 * 
	 * @param entity
	 *            对象
	 */
	public void update(T entity);

	/**
	 * 删除实体对象.
	 * 
	 * @param entity
	 *            对象
	 * @return
	 */
	public void delete(T entity);

	/**
	 * 根据ID删除实体对象.
	 * 
	 * @param id
	 *            记录ID
	 */
	public void delete(PK id);

	/**
	 * 根据ID数组删除实体对象.
	 * 
	 * @param ids
	 *            ID数组
	 */
	public void delete(PK[] ids);
	
	/**
	 * 根据属性名删除数据.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * @return boolean
	 */
	public void delete(String propertyName, Object... values);
	
	/**
	 * 根据属性名删除数据.
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            值
	 * @return boolean
	 */
	public void delete(String propertyName, String... values);
	public void deleteAll();
	/**
	 * 刷新session.
	 * 
	 */
	public void flush();

	/**
	 * 清除Session.
	 * 
	 */
	public void clear();
	
	/**
	 * 清除某一对象.
	 * 
	 * @param object
	 *            需要清除的对象
	 */
	public void evict(Object object);

	/**
	 * 根据Page对象进行查询(提供分页、查找、排序功能).
	 * 
	 * @param page
	 *            Page对象
	 * @return Page对象
	 */
	public Pager findByPager(Pager pager);
	
	/**
	 * 根据Pager和DetachedCriteria对象进行查询(提供分页、查找、排序功能).
	 * 
	 * @param pager
	 *            Pager对象
	 * @return Pager对象
	 */
	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria);
	
	/**
	 * 自定义关联查询
	 * 
	 * @param pager
	 * @param detachedCriteria
	 * @param correspondings
	 * @return
	 */
	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria,List<Corresponding> correspondings);
	
	/***
	 * 根据ids批量修改某个字段
	 * 
	 * @param propertyName
	 * @param value
	 * @param ids
	 */
	public void updateAll(String propertyName, Object value,PK[] ids);
	
	/***
	 * sql语句执行
	 * 
	 * @param sql
	 * @param list
	 */
	public void update(String sql , List<Object> list);
	
	/***
	 * 根据自由组合DetachedCriteria 对象和 统计方式Projection 来统计数据
	 * 
	 * @param detachedCriteria
	 * @param projection
	 * @return
	 */
	public Integer staticByCriteria(DetachedCriteria detachedCriteria , Projection projection);
	
	/***
	 * 根据属性名和属性值的集合获取实体对象.
	 * 
	 * @param map
	 * @return
	 */
	public T get(Map<String, Object> map);
	
	/**
	 * 根据属性名和属性值的集合获取实体对象集合.
	 * 
	 * @param map
	 * @return
	 */
	public List<T> getList(Map<String, Object> map);
}
