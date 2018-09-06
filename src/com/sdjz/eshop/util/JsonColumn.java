package com.sdjz.eshop.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标注不同平台展示字段,默认设置多平台都展示
 * @author Lee
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonColumn {
	/** 使用枚举类型 */  
    public enum PlatFormType{
       APP,WEB,ALL
    };
    PlatFormType value() default PlatFormType.ALL;
}
