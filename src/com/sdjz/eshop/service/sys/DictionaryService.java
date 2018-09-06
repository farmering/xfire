package com.sdjz.eshop.service.sys;

import java.util.List;

import com.sdjz.eshop.entity.Dictionary;
import com.sdjz.eshop.service.base.BaseService;

public interface DictionaryService extends BaseService<Dictionary, String> {
	
	public void saveAll(Dictionary dictionary , List<Dictionary> list);
	
	public void updateAllA(Dictionary dictionary , List<Dictionary> list);
	
	public void updateAllC(Dictionary dictionary , List<Dictionary> list,Integer dsort);
	
	public void deleteAll(String[] ids);

}
