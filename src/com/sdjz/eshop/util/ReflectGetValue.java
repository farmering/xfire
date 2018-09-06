package com.sdjz.eshop.util;

import java.lang.reflect.Field;  
import java.lang.reflect.Method;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.Locale;  
import java.util.Map;  

public class ReflectGetValue {
	
	/**  
     * 取出bean 属性和值  
     * @param obj  
     * @return  
     * @throws Exception  
     */  
	public static Map<Object, Object> getFieldValue(Object obj) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		Class<?> cls = obj.getClass();

		for (; cls != Object.class; cls = cls.getSuperclass()) {
			try {
				Field fields[] = cls.getDeclaredFields();
				Method methods[] = cls.getMethods();
				for (Field field : fields) {
					//String fldtype = field.getType().getSimpleName();
					String getMetName = pareGetName(field.getName());

					if (!checkMethod(methods, getMetName)) {
						continue;
					}
					Method method = cls.getMethod(getMetName);
					Object object = method.invoke(obj, new Object[] {});

					map.put(field.getName(), object);
				}

			} catch (Exception e) {
				// 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
				// 如果这里的异常打印或者往外抛，则就不会执行clazz =
				// clazz.getSuperclass(),最后就不会进入到父类中了
			}
		}

		return map;
	}

    /**  
     * 设置bean 属性值  
     * @param map  
     * @param bean  
     * @throws Exception  
     */  
	public static void setFieldValue(Map<Object, Object> map, Object bean) throws Exception {
		Class<?> cls = bean.getClass();
		Method methods[] = cls.getDeclaredMethods();
		Field fields[] = cls.getDeclaredFields();

		for (Field field : fields) {
			String fldtype = field.getType().getSimpleName();
			String fldSetName = field.getName();
			String setMethod = pareSetName(fldSetName);
			if (!checkMethod(methods, setMethod)) {
				continue;
			}
			Object value = map.get(fldSetName);
			System.out.println(value.toString());
			Method method = cls.getMethod(setMethod, field.getType());
			System.out.println(method.getName());
			if (null != value) {
				if ("String".equals(fldtype)) {
					method.invoke(bean, (String) value);
				} else if ("Double".equals(fldtype)) {
					method.invoke(bean, (Double) value);
				} else if ("int".equals(fldtype)) {
					int val = Integer.valueOf((String) value);
					method.invoke(bean, val);
				}
			}

		}
	}
 
    /**  
     * 拼接某属性get 方法  
     * @param fldname  
     * @return  
     */  
	public static String pareGetName(String fldname) {
		if (null == fldname || "".equals(fldname)) {
			return null;
		}
		String pro = "get" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
		return pro;
	}

    /**  
     * 拼接某属性set 方法  
     * @param fldname  
     * @return  
     */  
	public static String pareSetName(String fldname) {
		if (null == fldname || "".equals(fldname)) {
			return null;
		}
		String pro = "set" + fldname.substring(0, 1).toUpperCase() + fldname.substring(1);
		return pro;
	}  
    /**  
     * 判断该方法是否存在  
     * @param methods  
     * @param met  
     * @return  
     */  
    public static boolean checkMethod(Method methods[],String met){  
		if (null != methods) {
			for (Method method : methods) {
				if (met.equals(method.getName())) {
					return true;
				}
			}
		}
        return false;  
    }  
    /**  
     * 把date 类转换成string  
     * @param date  
     * @return  
     */  
    public static String fmlDate(Date date){  
        if(null != date){  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);  
            return sdf.format(date);  
        }  
        return null;  
    }  

}
