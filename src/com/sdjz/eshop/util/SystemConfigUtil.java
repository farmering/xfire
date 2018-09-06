package com.sdjz.eshop.util;

import java.io.File;
import java.io.FileOutputStream;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sdjz.eshop.bean.SystemConfig;

/**
 * 工具类 - 系统配置
 * 
 */

public class SystemConfigUtil {
	
	public static final String CONFIG_FILE_NAME = "shopxx.xml";// 系统配置文件名称
	public static final String SYSTEM_CONFIG_CACHE_KEY = "systemConfig";// systemConfig缓存Key

	/**
	 * 获取系统配置信息
	 * 
	 * @return SystemConfig对象
	 */
	public static SystemConfig getSystemConfig() {
		SystemConfig systemConfig = (SystemConfig) OsCacheConfigUtil.getFromCache(SYSTEM_CONFIG_CACHE_KEY);
		if (systemConfig != null) {
			return systemConfig;
		}
		File configFile = null;
		Document document = null;
		try {
			String configFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + CONFIG_FILE_NAME;
			configFile = new File(configFilePath);
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Node systemNameNode = document.selectSingleNode("/shopxx/systemConfig/systemName");
		Node systemVersionNode = document.selectSingleNode("/shopxx/systemConfig/systemVersion");
		Node systemDescriptionNode = document.selectSingleNode("/shopxx/systemConfig/systemDescription");
		Node isInstalledNode = document.selectSingleNode("/shopxx/systemConfig/isInstalled");
		
		Node isLoginFailureLockNode = document.selectSingleNode("/shopxx/systemConfig/isLoginFailureLock");
		Node loginFailureLockCountNode = document.selectSingleNode("/shopxx/systemConfig/loginFailureLockCount");
		Node loginFailureLockTimeNode = document.selectSingleNode("/shopxx/systemConfig/loginFailureLockTime");
		Node isRegisterNode = document.selectSingleNode("/shopxx/systemConfig/isRegister");
		
		Node uploadLimitNode = document.selectSingleNode("/shopxx/systemConfig/uploadLimit");
		Node allowedUploadImageExtensionNode = document.selectSingleNode("/shopxx/systemConfig/allowedUploadImageExtension");
		Node allowedUploadMediaExtensionNode = document.selectSingleNode("/shopxx/systemConfig/allowedUploadMediaExtension");
		Node allowedUploadFileExtensionNode = document.selectSingleNode("/shopxx/systemConfig/allowedUploadFileExtension");
		
		Node bigProductImageWidthNode = document.selectSingleNode("/shopxx/systemConfig/bigProductImageWidth");
		Node bigProductImageHeightNode = document.selectSingleNode("/shopxx/systemConfig/bigProductImageHeight");
		Node smallProductImageWidthNode = document.selectSingleNode("/shopxx/systemConfig/smallProductImageWidth");
		Node smallProductImageHeightNode = document.selectSingleNode("/shopxx/systemConfig/smallProductImageHeight");
		Node thumbnailProductImageWidthNode = document.selectSingleNode("/shopxx/systemConfig/thumbnailProductImageWidth");
		Node thumbnailProductImageHeightNode = document.selectSingleNode("/shopxx/systemConfig/thumbnailProductImageHeight");
		
		Node keyNode = document.selectSingleNode("/shopxx/systemConfig/key");
		
		systemConfig = new SystemConfig();
		
		systemConfig.setSystemName(systemNameNode.getText());
		systemConfig.setSystemVersion(systemVersionNode.getText());
		systemConfig.setSystemDescription(systemDescriptionNode.getText());
		systemConfig.setIsInstalled(Boolean.valueOf(isInstalledNode.getText()));
		
		systemConfig.setIsLoginFailureLock(Boolean.valueOf(isLoginFailureLockNode.getText()));
		systemConfig.setLoginFailureLockCount(Integer.valueOf(loginFailureLockCountNode.getText()));
		systemConfig.setLoginFailureLockTime(Integer.valueOf(loginFailureLockTimeNode.getText()));
		systemConfig.setIsRegister(Boolean.valueOf(isRegisterNode.getText()));
		
		systemConfig.setUploadLimit(Integer.valueOf(uploadLimitNode.getText()));
		systemConfig.setAllowedUploadImageExtension(allowedUploadImageExtensionNode.getText());
		systemConfig.setAllowedUploadMediaExtension(allowedUploadMediaExtensionNode.getText());
		systemConfig.setAllowedUploadFileExtension(allowedUploadFileExtensionNode.getText());
		
		systemConfig.setBigProductImageWidth(Integer.valueOf(bigProductImageWidthNode.getText()));
		systemConfig.setBigProductImageHeight(Integer.valueOf(bigProductImageHeightNode.getText()));
		systemConfig.setSmallProductImageWidth(Integer.valueOf(smallProductImageWidthNode.getText()));
		systemConfig.setSmallProductImageHeight(Integer.valueOf(smallProductImageHeightNode.getText()));
		systemConfig.setThumbnailProductImageWidth(Integer.valueOf(thumbnailProductImageWidthNode.getText()));
		systemConfig.setThumbnailProductImageHeight(Integer.valueOf(thumbnailProductImageHeightNode.getText()));
		
		systemConfig.setKey(keyNode.getText());
		
		OsCacheConfigUtil.putInCache(SYSTEM_CONFIG_CACHE_KEY, systemConfig);
		return systemConfig;
	}
	
	/**
	 * 更新系统配置信息
	 * 
	 * @param systemConfig
	 *          SystemConfig对象
	 */
	public static void update(SystemConfig systemConfig) {
		File configFile = null;
		Document document = null;
		try {
			String configFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + CONFIG_FILE_NAME;
			configFile = new File(configFilePath);
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element rootElement = document.getRootElement();
		Element systemConfigElement = rootElement.element("systemConfig");
		Node systemNameNode = document.selectSingleNode("/shopxx/systemConfig/systemName");
		Node systemVersionNode = document.selectSingleNode("/shopxx/systemConfig/systemVersion");
		Node systemDescriptionNode = document.selectSingleNode("/shopxx/systemConfig/systemDescription");
		Node isInstalledNode = document.selectSingleNode("/shopxx/systemConfig/isInstalled");
		
		Node isLoginFailureLockNode = document.selectSingleNode("/shopxx/systemConfig/isLoginFailureLock");
		Node loginFailureLockCountNode = document.selectSingleNode("/shopxx/systemConfig/loginFailureLockCount");
		Node loginFailureLockTimeNode = document.selectSingleNode("/shopxx/systemConfig/loginFailureLockTime");
		Node isRegisterNode = document.selectSingleNode("/shopxx/systemConfig/isRegister");
		
		Node uploadLimitNode = document.selectSingleNode("/shopxx/systemConfig/uploadLimit");
		Node allowedUploadImageExtensionNode = document.selectSingleNode("/shopxx/systemConfig/allowedUploadImageExtension");
		Node allowedUploadMediaExtensionNode = document.selectSingleNode("/shopxx/systemConfig/allowedUploadMediaExtension");
		Node allowedUploadFileExtensionNode = document.selectSingleNode("/shopxx/systemConfig/allowedUploadFileExtension");
		
		Node bigProductImageWidthNode = document.selectSingleNode("/shopxx/systemConfig/bigProductImageWidth");
		Node bigProductImageHeightNode = document.selectSingleNode("/shopxx/systemConfig/bigProductImageHeight");
		Node smallProductImageWidthNode = document.selectSingleNode("/shopxx/systemConfig/smallProductImageWidth");
		Node smallProductImageHeightNode = document.selectSingleNode("/shopxx/systemConfig/smallProductImageHeight");
		Node thumbnailProductImageWidthNode = document.selectSingleNode("/shopxx/systemConfig/thumbnailProductImageWidth");
		Node thumbnailProductImageHeightNode = document.selectSingleNode("/shopxx/systemConfig/thumbnailProductImageHeight");
		
		Node keyNode = document.selectSingleNode("/shopxx/systemConfig/key");
		
		if(systemNameNode == null){
			systemNameNode = systemConfigElement.addElement("systemName");
		}
		if(systemVersionNode == null){
			systemVersionNode = systemConfigElement.addElement("systemVersion");
		}
		if(systemDescriptionNode == null){
			systemDescriptionNode = systemConfigElement.addElement("systemDescription");
		}
		if(isInstalledNode == null){
			isInstalledNode = systemConfigElement.addElement("isInstalled");
		}
		if(isLoginFailureLockNode == null){
			isLoginFailureLockNode = systemConfigElement.addElement("isLoginFailureLock");
		}
		if(loginFailureLockCountNode == null){
			loginFailureLockCountNode = systemConfigElement.addElement("loginFailureLockCount");
		}
		if(loginFailureLockTimeNode == null){
			loginFailureLockTimeNode = systemConfigElement.addElement("loginFailureLockTime");
		}
		if(isRegisterNode == null){
			isRegisterNode = systemConfigElement.addElement("isRegister");
		}
		if(uploadLimitNode == null ){
			uploadLimitNode = systemConfigElement.addElement("uploadLimit");
		}
		if(allowedUploadImageExtensionNode == null){
			allowedUploadImageExtensionNode = systemConfigElement.addElement("allowedUploadImageExtension");
		}
		if(allowedUploadMediaExtensionNode == null){
			allowedUploadMediaExtensionNode = systemConfigElement.addElement("allowedUploadMediaExtension");
		}
		if(allowedUploadFileExtensionNode == null){
			allowedUploadFileExtensionNode = systemConfigElement.addElement("allowedUploadFileExtension");
		}
		if(bigProductImageWidthNode == null){
			bigProductImageWidthNode = systemConfigElement.addElement("bigProductImageWidth");
		}
		if(bigProductImageHeightNode == null){
			bigProductImageHeightNode = systemConfigElement.addElement("bigProductImageHeight");
		}
		if(smallProductImageWidthNode == null){
			smallProductImageWidthNode = systemConfigElement.addElement("smallProductImageWidth");
		}
		if(smallProductImageHeightNode == null){
			smallProductImageHeightNode = systemConfigElement.addElement("smallProductImageHeight");
		}
		if(thumbnailProductImageWidthNode == null){
			thumbnailProductImageWidthNode = systemConfigElement.addElement("thumbnailProductImageWidth");
		}
		if(thumbnailProductImageHeightNode == null){
			thumbnailProductImageHeightNode = systemConfigElement.addElement("thumbnailProductImageHeight");
		}
		
		if(keyNode == null ){
			
			keyNode = systemConfigElement.addElement("key");
		}
		
		systemNameNode.setText(systemConfig.getSystemName());
		systemVersionNode.setText(systemConfig.getSystemVersion());
		systemDescriptionNode.setText(systemConfig.getSystemDescription());
		isInstalledNode.setText(systemConfig.getIsInstalled().toString());
		isLoginFailureLockNode.setText(String.valueOf(systemConfig.getIsLoginFailureLock()));
		loginFailureLockCountNode.setText(String.valueOf(systemConfig.getLoginFailureLockCount()));
		loginFailureLockTimeNode.setText(String.valueOf(systemConfig.getLoginFailureLockTime()));
		isRegisterNode.setText(String.valueOf(systemConfig.getIsRegister()));
		uploadLimitNode.setText(String.valueOf(systemConfig.getUploadLimit()));
		allowedUploadImageExtensionNode.setText(systemConfig.getAllowedUploadImageExtension());
		allowedUploadMediaExtensionNode.setText(systemConfig.getAllowedUploadMediaExtension());
		allowedUploadFileExtensionNode.setText(systemConfig.getAllowedUploadFileExtension());
		bigProductImageWidthNode.setText(String.valueOf(systemConfig.getBigProductImageWidth()));
		bigProductImageHeightNode.setText(String.valueOf(systemConfig.getBigProductImageHeight()));
		smallProductImageWidthNode.setText(String.valueOf(systemConfig.getSmallProductImageWidth()));
		smallProductImageHeightNode.setText(String.valueOf(systemConfig.getSmallProductImageHeight()));
		thumbnailProductImageWidthNode.setText(String.valueOf(systemConfig.getThumbnailProductImageWidth()));
		thumbnailProductImageHeightNode.setText(String.valueOf(systemConfig.getThumbnailProductImageHeight()));
		
		keyNode.setText(systemConfig.getKey());
		
		try {
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();// 设置XML文档输出格式
			outputFormat.setEncoding("UTF-8");// 设置XML文档的编码类型
			outputFormat.setIndent(true);// 设置是否缩进
			outputFormat.setIndent("	");// 以TAB方式实现缩进
			outputFormat.setNewlines(true);// 设置是否换行
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(configFile), outputFormat);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		OsCacheConfigUtil.flushEntry(SYSTEM_CONFIG_CACHE_KEY);
	}
	
	/**
	 * 刷新系统配置信息
	 * 
	 */
	public void flush() {
		OsCacheConfigUtil.flushEntry(SYSTEM_CONFIG_CACHE_KEY);
	}
	
}