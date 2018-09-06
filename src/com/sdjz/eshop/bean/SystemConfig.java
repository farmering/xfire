package com.sdjz.eshop.bean;



/**
 * Bean类 - 系统配置
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

public class SystemConfig {
	

	public static final String EXTENSION_SEPARATOR = ",";// 文件扩展名分隔符
	public static final String UPLOAD_IMAGE_DIRHTML = "/integral_content/images/";// 图片文件上传目录

	private String systemName;// 系统名称
	private String systemVersion;// 系统版本
	private String systemDescription;// 系统描述
	private Boolean isInstalled;// 是否已安装

	private Boolean isLoginFailureLock; // 是否开启登录失败锁定账号功能
	private Integer loginFailureLockCount;// 同一账号允许连续登录失败的最大次数，超出次数后将锁定其账号
	private Integer loginFailureLockTime;// 账号锁定时间(单位：分钟,0表示永久锁定)
	private Boolean isRegister;// 是否开放注册

	private Integer uploadLimit;
	private String allowedUploadImageExtension;// 允许上传的图片文件扩展名（为空表示不允许上传图片文件）
	private String allowedUploadMediaExtension;// 允许上传的媒体文件扩展名（为空表示不允许上传媒体文件）
	private String allowedUploadFileExtension;// 允许上传的文件扩展名（为空表示不允许上传文件）
	
	private Integer bigProductImageWidth;// 商品图片（大）宽度
	private Integer bigProductImageHeight;// 商品图片（大）高度
	private Integer smallProductImageWidth;// 商品图片（小）宽度
	private Integer smallProductImageHeight;// 商品图片（小）高度
	private Integer thumbnailProductImageWidth;// 商品缩略图宽度
	private Integer thumbnailProductImageHeight;// 商品缩略图高度
	
	private String key; // 软件授权码
	
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getSystemDescription() {
		return systemDescription;
	}

	public void setSystemDescription(String systemDescription) {
		this.systemDescription = systemDescription;
	}

	public Boolean getIsInstalled() {
		return isInstalled;
	}

	public void setIsInstalled(Boolean isInstalled) {
		this.isInstalled = isInstalled;
	}


	public Boolean getIsLoginFailureLock() {
		return isLoginFailureLock;
	}

	public void setIsLoginFailureLock(Boolean isLoginFailureLock) {
		this.isLoginFailureLock = isLoginFailureLock;
	}

	public Integer getLoginFailureLockCount() {
		return loginFailureLockCount;
	}

	public void setLoginFailureLockCount(Integer loginFailureLockCount) {
		this.loginFailureLockCount = loginFailureLockCount;
	}

	public Integer getLoginFailureLockTime() {
		return loginFailureLockTime;
	}

	public void setLoginFailureLockTime(Integer loginFailureLockTime) {
		this.loginFailureLockTime = loginFailureLockTime;
	}

	public Boolean getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(Boolean isRegister) {
		this.isRegister = isRegister;
	}


	public String getAllowedUploadImageExtension() {
		return allowedUploadImageExtension;
	}

	public void setAllowedUploadImageExtension(String allowedUploadImageExtension) {
		this.allowedUploadImageExtension = allowedUploadImageExtension;
	}

	public String getAllowedUploadMediaExtension() {
		return allowedUploadMediaExtension;
	}

	public void setAllowedUploadMediaExtension(String allowedUploadMediaExtension) {
		this.allowedUploadMediaExtension = allowedUploadMediaExtension;
	}

	public String getAllowedUploadFileExtension() {
		return allowedUploadFileExtension;
	}

	public void setAllowedUploadFileExtension(String allowedUploadFileExtension) {
		this.allowedUploadFileExtension = allowedUploadFileExtension;
	}

	public Integer getUploadLimit() {
		return uploadLimit;
	}

	public void setUploadLimit(Integer uploadLimit) {
		this.uploadLimit = uploadLimit;
	}

	public Integer getBigProductImageWidth() {
		return bigProductImageWidth;
	}

	public void setBigProductImageWidth(Integer bigProductImageWidth) {
		this.bigProductImageWidth = bigProductImageWidth;
	}

	public Integer getBigProductImageHeight() {
		return bigProductImageHeight;
	}

	public void setBigProductImageHeight(Integer bigProductImageHeight) {
		this.bigProductImageHeight = bigProductImageHeight;
	}

	public Integer getSmallProductImageWidth() {
		return smallProductImageWidth;
	}

	public void setSmallProductImageWidth(Integer smallProductImageWidth) {
		this.smallProductImageWidth = smallProductImageWidth;
	}

	public Integer getSmallProductImageHeight() {
		return smallProductImageHeight;
	}

	public void setSmallProductImageHeight(Integer smallProductImageHeight) {
		this.smallProductImageHeight = smallProductImageHeight;
	}

	public Integer getThumbnailProductImageWidth() {
		return thumbnailProductImageWidth;
	}

	public void setThumbnailProductImageWidth(Integer thumbnailProductImageWidth) {
		this.thumbnailProductImageWidth = thumbnailProductImageWidth;
	}

	public Integer getThumbnailProductImageHeight() {
		return thumbnailProductImageHeight;
	}

	public void setThumbnailProductImageHeight(Integer thumbnailProductImageHeight) {
		this.thumbnailProductImageHeight = thumbnailProductImageHeight;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}