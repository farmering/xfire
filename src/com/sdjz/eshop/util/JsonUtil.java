package com.sdjz.eshop.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.sdjz.eshop.entity.Sebaseinfo;
import com.sdjz.eshop.util.JsonColumn.PlatFormType;

/**
 * JSON字符串格式化工具，
 * 调用#fastjson#
 * @author Lee
 *
 */
public abstract class JsonUtil {

	private static SerializeConfig config;

	static {
	    config = new SerializeConfig();
	}

	private static final SerializerFeature[] features = {
		SerializerFeature.DisableCircularReferenceDetect,//打开循环引用检测，JSONField(serialize = false)不循环
		SerializerFeature.WriteDateUseDateFormat,//默认使用系统默认 格式日期格式化
		//SerializerFeature.WriteMapNullValue, //输出空置字段
        //SerializerFeature.WriteNullListAsEmpty,//list字段如果为null，输出为[]，而不是null
        //SerializerFeature.WriteNullNumberAsZero,// 数值字段如果为null，输出为0，而不是null
        //SerializerFeature.WriteNullBooleanAsFalse,//Boolean字段如果为null，输出为false，而不是null
        //SerializerFeature.WriteNullStringAsEmpty//字符类型字段如果为null，输出为""，而不是null
	};

	/**
	 * 序列化列表输出的字符串
	 * @param object
	 * @return
	 */
	public static final String toJSONString(Object object) {
		return JSON.toJSONString(object, config, features);
	}
	
	/**
	 * 序列化列表输出的字符串
	 * @param object
	 * @param clazz
	 * @param type  @JsonColumn.PlatFormType[PlatFormType.APP:app需要字段;PlatFormType.WEB:web需要字段]
	 * @return
	 */
	public static final String toJSONString(Object object, Class<?> clazz, PlatFormType type) {
		return JSON.toJSONString(object, config, getSerializeFilter(clazz, type), features);
	}
	
	/**
	 * 序列化列表(制定日期格式)输出的字符串
	 * @param object
	 * @param clazz
	 * @param type  @JsonColumn.PlatFormType[PlatFormType.APP:app需要字段;PlatFormType.WEB:web需要字段]
	 * @param format 日期格式
	 * @return
	 */
	public static final String toJSONString(Object object, Class<?> clazz, JsonColumn.PlatFormType type, String format) {
		config.put(Date.class, new SimpleDateFormatSerializer(format));
		return JSON.toJSONString(object, config, getSerializeFilter(clazz, type), features);
	}
	
	/**
	 * 序列化输出的字符串
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static final String toJSONString(Object object, Class<?> clazz) {
		return toJSONString(object, clazz, PlatFormType.ALL);
	}
	
	//序列化为和JSON-LIB兼容的字符串
	public static final String toJSONString(Object object, SerializerFeature... arg1) {
		return JSON.toJSONString(object, config, arg1);
	}
	
	/**
	 * 序列化列表(制定日期格式)输出的字符串
	 * @param object
	 * @param format 日期格式
	 * @return
	 */
	public static final String toJSONString(Object object, String format) {
		config.put(Date.class, new SimpleDateFormatSerializer(format));
		JSON.DEFFAULT_DATE_FORMAT=format;//设置日期格式
		return JSON.toJSONString(object, config, features);
	}
	
	/**
	 * 序列化为列表输出的字符串
	 * @param object
	 * @param sconfig
	 * @param arg1
	 * @return
	 */
	public static final String toJSONString(Object object, SerializeConfig sconfig, SerializerFeature... arg1) {
		return JSON.toJSONString(object, sconfig, arg1);
	}
	
	/**
	 * 序列化为列表输出的字符串
	 * @param object
	 * @param clazz
	 * @param type  @JsonColumn.PlatFormType[PlatFormType.APP:app需要字段;PlatFormType.WEB:web需要字段]
	 * @param sconfig
	 * @param arg1
	 * @return
	 */
	public static final String toJSONString(Object object, Class<?> clazz, JsonColumn.PlatFormType type, SerializerFeature... arg1) {
		return JSON.toJSONString(object, config, getSerializeFilter(clazz, type), arg1);
	}
	
	/**
	 * 序列化为列表输出的字符串
	 * @param object
	 * @param clazz
	 * @param type  @JsonColumn.PlatFormType[PlatFormType.APP:app需要字段;PlatFormType.WEB:web需要字段]
	 * @param sconfig
	 * @param arg1
	 * @return
	 */
	public static final String toJSONString(Object object, Class<?> clazz, JsonColumn.PlatFormType type ,SerializeConfig sconfig, SerializerFeature... arg1) {
		return JSON.toJSONString(object, sconfig, getSerializeFilter(clazz, type), arg1);
	}
	
	/**
	 * 根据实体类注释获取过滤的展示字段的过滤器
	 * @param clazz
	 * @param type  @JsonColumn.PlatFormType
	 * @return
	 */
	private static final SerializeFilter getSerializeFilter(Class<?> clazz, JsonColumn.PlatFormType type){
		List<Field> flist = null;
		if(PlatFormType.ALL.equals(type))
			flist = getFields(clazz);
		else
			flist = getFields(clazz, type);
		String[] fields = new String[flist.size()];
		for (int i = 0; i < flist.size(); i++) {
			fields[i] = flist.get(i).getName();
		}
		SerializeFilter filter = new SimplePropertyPreFilter(clazz, fields);
		return filter;
	}
	
	/**
	 * 根据实体类注释类别不同 获取过滤的展示字段
	 * @param clazz
	 * @param type @JsonColumn.PlatFormType
	 * @return
	 */
	private static final List<Field> getFields(Class<?> clazz, JsonColumn.PlatFormType type){
		List<Field> list = new ArrayList<Field>();
		
		Field[] fszz = clazz.getDeclaredFields();//获取字段
		Class<?> claxx = clazz.getSuperclass();//获取父类
		Field[] fsxx = claxx.getDeclaredFields();//获取父类字段
		try {
			for (Field field : fszz) {
				JsonColumn column = field.getAnnotation(JsonColumn.class);
				if (column != null) {//展示字段
					if(PlatFormType.ALL.equals(column.value()) || type.equals(column.value()))
					list.add(field);
				}
			}
			for (Field field : fsxx) {//父类BaseEntity数据库三个字段
				JsonColumn column = field.getAnnotation(JsonColumn.class);
				if (column != null) {//展示字段
					if(PlatFormType.ALL.equals(column.value()) || type.equals(column.value()))
					list.add(field);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 根据实体类注释 获取过滤的展示字段
	 * @param clazz
	 * @return
	 */
	private static final List<Field> getFields(Class<?> clazz){
		List<Field> list = new ArrayList<Field>();
		
		Field[] fszz = clazz.getDeclaredFields();//获取字段
		Class<?> claxx = clazz.getSuperclass();//获取父类
		Field[] fsxx = claxx.getDeclaredFields();//获取父类字段
		try {
			for (Field field : fszz) {
				JsonColumn column = field.getAnnotation(JsonColumn.class);
				if (column != null) {//展示字段
					list.add(field);
				}
			}
			for (Field field : fsxx) {//父类BaseEntity数据库三个字段
				JsonColumn column = field.getAnnotation(JsonColumn.class);
				if (column != null) {//展示字段
					list.add(field);
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		Sebaseinfo eb = new Sebaseinfo();
		eb.setId("asadfasd123");
		eb.setSeName("测试一下");
		//eb.setCardId("37142536453274532");
		eb.setSeModel("email@163.com");
		
		System.out.println("------------------------------------");
		//System.out.println(JsonUtil.toJSONString(eb));
		//System.out.println("---过滤---\t"+JsonUtil.toJSONString(eb, EnterpriceBaseinfo.class));
		//System.out.println("---web过滤---\t"+JsonUtil.toJSONString(eb, EnterpriceBaseinfo.class, PlatFormType.WEB));
		//System.out.println("---app过滤 ---\t"+JsonUtil.toJSONString(eb, EnterpriceBaseinfo.class, PlatFormType.APP));
		
		List<Field> pList= JsonUtil.getFields(Sebaseinfo.class, PlatFormType.WEB);
		for (int i = 0; i < pList.size(); i++) {
			Field p = pList.get(i);
			System.out.println(p.getName());
		}
		//JsonUtil.toApp3(eb);
		//System.out.println(JSON.toJSONString(map));
		//System.out.println(JsonUtil.toJSONString(map,true));
		//System.out.println(JsonUtil.toJSONString(map,false));
	}
}
