package com.sdjz.eshop.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.sdjz.eshop.bean.SystemConfig;

public class ImgFileUpload {
	
	/***
	 * 
	 * 
	 * @param myfile 源文件
	 * @param filename 源文件名
	 * @param name 上传后文件名称
	 * @param rootpath 上传文件根目录
	 * @param path 访问路径
	 * @return
	 * @throws IOException
	 */
	public static String[] saveImg( File myfile,String filename,String name,String rootpath,String path) throws IOException{
		
		String[] ss = new String[2];
		
		String allowedUploadImageExtension = SystemConfigUtil.getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
		String fileExtension =  StringUtils.substringAfterLast(filename, ".").toLowerCase();
		String[] fileExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
		if (!ArrayUtils.contains(fileExtensionArray, fileExtension)) {
			ss[0] = "0";
			ss[1] = "只允许上传文件类型: " + allowedUploadImageExtension + "!";
			return ss;
		}
		
		File file = new File(rootpath + path);
		
		if(!file.exists()){
			
			file.mkdirs();
		}
		
		File img = new File(rootpath + path+name+"."+fileExtension);
		
		FileUtils.copyFile(myfile,img);
		
		String s = path+name+"."+fileExtension;
		
		ss[0] = "1";
		ss[1] = s.replace("\\", "/");
		
		return ss;
		
	}
	
	/***
	 * 
	 * 
	 * @param myfile 源文件
	 * @param filename 源文件名
	 * @param name 上传后文件名称
	 * @param rootpath 上传文件根目录
	 * @param path 访问路径
	 * @return
	 * @throws IOException
	 */
	public static String[] saveImgZoom( File myfile,String filename,String name,String rootpath,String path,int height,int width) throws IOException{
		
		String[] ss = new String[2];
		
		String allowedUploadImageExtension = SystemConfigUtil.getSystemConfig().getAllowedUploadImageExtension().toLowerCase();
		String fileExtension =  StringUtils.substringAfterLast(filename, ".").toLowerCase();
		String[] fileExtensionArray = allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
		if (!ArrayUtils.contains(fileExtensionArray, fileExtension)) {
			ss[0] = "0";
			ss[1] = "只允许上传文件类型: " + allowedUploadImageExtension + "!";
			return ss;
		}
		
		File file = new File(rootpath + path);
		
		if(!file.exists()){
			
			file.mkdirs();
		}
		
		
		File img = new File(rootpath + path+name+"."+fileExtension);
		
		BufferedImage srcBufferedImage = ImageIO.read(myfile);
		
		ImageUtil.zoom(srcBufferedImage, img, height, width);
			
		
		String s = path+name+"."+fileExtension;
		
		ss[0] = "1";
		ss[1] = s.replace("\\", "/");
		
		return ss;
		
	}
	
	/***
	 * 
	 * 
	 * @param myfile 源文件
	 * @param filename 源文件名
	 * @param name 上传后文件名称
	 * @param rootpath 上传文件根目录
	 * @param path 访问路径
	 * @return
	 * @throws IOException
	 */
	public static String[] saveFile( File myfile,String filename,String name,String rootpath,String path) throws IOException{
		
		String[] ss = new String[2];
		
		String allowedUploadImageExtension = SystemConfigUtil.getSystemConfig().getAllowedUploadFileExtension().toLowerCase();
		String fileExtension =  StringUtils.substringAfterLast(filename, ".").toLowerCase();
		allowedUploadImageExtension.split(SystemConfig.EXTENSION_SEPARATOR);
		
		File file = new File(rootpath + path);
		
		if(!file.exists()){
			
			file.mkdirs();
		}
		
		File img = new File(rootpath + path+name+"."+fileExtension);
		
		FileUtils.copyFile(myfile,img);
		
		String s =rootpath+ path+name+"."+fileExtension;
		
		ss[0] = "1";
		ss[1] = s.replace("\\", "/");
		
		return ss;
		
	}

}
