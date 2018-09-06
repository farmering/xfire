package com.sdjz.eshop.service.sys.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.sdjz.eshop.dao.DictionaryDao;
import com.sdjz.eshop.entity.Dictionary;
import com.sdjz.eshop.service.base.impl.BaseServiceImpl;
import com.sdjz.eshop.service.sys.DictionaryService;
import com.sdjz.eshop.util.JZStringUtils;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary, String> implements DictionaryService {

	@Resource
	private DictionaryDao dictionaryDao;

	@Resource
	public void setBaseDao(DictionaryDao dictionaryDao) {
		super.setBaseDao(dictionaryDao);
	}

	public void saveAll(Dictionary dictionary, List<Dictionary> list) {
		
		dictionaryDao.save(dictionary);
		
		for(Dictionary dictionary2 : list){
			
			dictionary2.setSort(dictionary2.getSort()+1);
			
			dictionaryDao.update(dictionary2);
		}
		
	}

	public void updateAllA(Dictionary dictionary, List<Dictionary> list) {
		
		dictionaryDao.update(dictionary);
		
		for(Dictionary dictionary2 : list){
			
			dictionary2.setSort(dictionary2.getSort()+1);
			
			dictionaryDao.update(dictionary2);
		}
		
	}

	public void updateAllC(Dictionary dictionary, List<Dictionary> list,Integer dsort) {
		
		for(Dictionary dictionary2 : list){
			dsort = dictionary2.getSort();
			dictionary2.setSort(dictionary2.getSort()-1);
			
			dictionaryDao.update(dictionary2);
		}
		
		dictionary.setSort(dsort);
		
		dictionaryDao.update(dictionary);
		
	}

	public void deleteAll(String[] ids) {
		
		Dictionary dictionary = dictionaryDao.get(ids[0]);
		String pid = dictionary.getParent()!=null ? dictionary.getParent().getId() : "";
		DetachedCriteria criteria  =DetachedCriteria.forClass(Dictionary.class);
		criteria.add(Restrictions.eq("isdelete", false));
		criteria.addOrder(Order.asc("sort"));
		if(!JZStringUtils.isNullOrBlankFull(pid)){
			
			criteria.add(Restrictions.eq("parent.id", pid));
		}else{
			
			criteria.add(Restrictions.isNull("parent"));
		}
		
		dictionaryDao.updateAll("isdelete", true, ids);
		
		List<Dictionary> dictionaryList = dictionaryDao.getListByCriteria(criteria);
		
		for(int i = 0; i< dictionaryList.size() ; i++){
			
			Dictionary dictionary2 = dictionaryList.get(i);
			dictionary2.setSort(i);
			dictionaryDao.update(dictionary2);
		}
		
		
		
	}

	@Override
	public Dictionary get(Map<String, Object> map) {
		
		return super.get(map);
	}

}
