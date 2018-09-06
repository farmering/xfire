package com.sdjz.eshop.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


public class CommonFile {
	Properties properties = new Properties();
	/** 
	  * 删除某个文件夹下的所有文件夹和文件 
	  * @param delpath String 
	  * @throws FileNotFoundException 
	  * @throws IOException 
	  * @return boolean 
	  */  
	 public  boolean deletefile(String delpath) throws Exception {
		 try {  
		   File file = new File(delpath);  
		   // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true  
		   if (!file.isDirectory()) {  
			   file.delete(); 
		   } /*else if (file.isDirectory()) {  
			   String[] filelist = file.list();  
			   for (int i = 0; i < filelist.length; i++) {  
				   File delfile = new File(delpath + File.separator + filelist[i]);  
				   if (!delfile.isDirectory()) {  
					   delfile.delete();  
				   } else if (delfile.isDirectory()) {  
					   deletefile(delpath + File.separator + filelist[i]);  
				   }  
			   }  
			   file.delete();  
		   }  */
		  }catch (Exception e) {  
			  System.out.println("deletefile() Exception:" + e.getMessage());  
		  }  
		  return true;  
	 } 
	/**   
    * WORD转HTML   
    * @param docfile WORD文件全路径   
    * @param htmlfile 转换后HTML存放路径   
	 1:Microsoft Word 97 - 2003 模板 (.dot)
	 2:文本文档 (.txt)
	 3:文本文档 (.txt)
	 4:文本文档 (.txt)
	 5:文本文档 (.txt)
	 6:RTF 格式 (.rtf)
	 7:文本文档 (.txt)
	 8:HTML 文档 (.htm)(带文件夹)
	 9:MHTML 文档 (.mht)(单文件)
	 10:MHTML 文档 (.mht)(单文件)
	 11:XML 文档 (.xml)
	 12:Microsoft Word 文档 (.docx)
	 13:Microsoft Word 启用宏的文档 (.docm)
	 14:Microsoft Word 模板 (.dotx)
	 15:Microsoft Word 启用宏的模板 (.dotm)
	 16:Microsoft Word 文档 (.docx)
	 17:PDF 文件 (.pdf)
	 18:XPS 文档 (.xps)
	 19:XML 文档 (.xml)
	 20:XML 文档 (.xml)
	 21:XML 文档 (.xml)
	 22:XML 文档 (.xml)
	 23:OpenDocument 文本 (.odt)
	 24:WTF 文件 (.wtf)
    */    
	public void saveAsFromWord(String sourceURL, String destPath, int docType) {
		// 新建路径
		ActiveXComponent word = new ActiveXComponent("Word.Application");
		Dispatch doc = null;
		// 核心代码
		try {
			// 初始化com的线程
			ComThread.InitSTA();
			// 设置word属性
			word.setProperty("Visible", new Variant(false));
			word.setProperty("DisplayAlerts", new Variant(false));
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
			Dispatch docs = word.getProperty("Documents").toDispatch();
			// 打开word文档
			doc = Dispatch.invoke(docs, "Open", Dispatch.Method,
					new Object[] { sourceURL, new Variant(false), new Variant(true) }, new int[1]).toDispatch();
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] { destPath, new Variant(docType) }, new int[1]);
			Variant file = new Variant(false);
			// 关闭文档
			Dispatch.call(doc, "Close", file);
			try {
				if (word != null) {
					word.invoke("Quit", new Variant[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("关闭word进程失败");
			}
			ComThread.Release();
			word = null;
			System.out.println("转换完毕");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				deletefile(sourceURL);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("文档转换失败");
		}
	}
	/**
	 * pdf转换成swf
	 */
	public void pdf2swf(String sourceURL, String destPath,String docType) throws Exception {
		File pdfFile=new File(sourceURL);
		File swfFile=new File(destPath);
		Runtime r = Runtime.getRuntime();
		String SWFTools_Windows="";// 这里根据SWFTools安装路径需要进行相应更改
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				try {
					properties.load(this.getClass().getClassLoader().getResourceAsStream("filePath.properties"));
					SWFTools_Windows = properties.getProperty("SWFTools_Windows");
					Process p = r.exec(SWFTools_Windows+" "+ sourceURL+ " -o " + destPath+ " -T 9");
					final InputStream is1 = p.getInputStream();
		        	new Thread(new Runnable() {
		        	    public void run() {
		        	        BufferedReader br = new BufferedReader(new InputStreamReader(is1)); 
		        	        try {
								while(br.readLine() != null) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
		        	    }
		        	}).start();// 启动单独的线程来清空process.getInputStream()的缓冲区
		        	InputStream is2 = p.getErrorStream();
		        	BufferedReader br2 = new BufferedReader(new InputStreamReader(is2)); 
		        	StringBuilder buf = new StringBuilder(); // 保存输出结果流
		        	String line = null;
		        	while((line = br2.readLine()) != null){
		        		buf.append(line); // 循环等待ffmpeg进程结束
		        	} 
		        	System.out.println("输出结果为：" + buf);
		        	try {       
	               	 	p.waitFor();
		        	}catch (InterruptedException e) {       
	                   
		        	}  
		           	System.out.println("###--Msg: swf 转换成功");
					if(!".pdf".equalsIgnoreCase(docType)){
						if (pdfFile.exists()) {
							pdfFile.delete();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("****pdf不存在，无法转换****");
			}
		} else {
			System.out.println("****swf已存在不需要转换****");
		}
	}
	@SuppressWarnings("unused")
	private String loadStream(InputStream in) throws IOException {
		int ptr = 0;
		//把InputStream字节流 替换为BufferedReader字符流 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder buffer = new StringBuilder();
		while ((ptr = reader.read()) != -1) {
			buffer.append((char) ptr);
		}
		return buffer.toString();
	}
	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	public String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 依据原始文件名生成新文件名
	 * @return
	 */
	public String getFileName(String fileName) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssms");
		return UUID.randomUUID() + this.getFileExt(fileName);
	}
	
	/**
	 * 根据字符串设置目录字符串， 并追加年份子目录
	 * @param path
	 * @return
	 */
	public String createPath(String pathName){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return pathName + "/" + sdf.format(new Date())+"/";
	}
	
	/**
	 * 根据字符串设置业务目录字符串， +年份子目录，+ 文件名
	 * @param path 业务目录
	 * @param fileName 文件名
	 * @return
	 */
	public String createFileName(String pathName,String fileName){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return pathName + "/" + sdf.format(new Date()) + "/" + fileName;
	}

	/**
	 * 根据字符串创建本地目录 并按照年份建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	public String getFolder(String pathName) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy");
		String path = "";
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("filePath.properties"));
			path = properties.getProperty("rootPath") + properties.getProperty(pathName);
		} catch (IOException e) {
			System.out.println("读取webservices文件出错，请检查src下是否存在webservice.properties文件");
		}
		path = path + formater.format(new Date()) + File.separator;
		File dir = new File(path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return path;
	}
	public String check(String pathName) {
		String path = "";
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("filePath.properties"));
			path = properties.getProperty(pathName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File fileToChange = new File(path);
	    //读取文件的最后修改时间
	    Date filetime = new Date(fileToChange.lastModified());
	    //读取最近更新时间
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	    return sdf.format(filetime);
	}
	/**
	 * 获取根目录
	 * 
	 * @param path
	 * @return
	 */
	public String getFileRootPath() {
		String rootPath = "";
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("filePath.properties"));
			rootPath = properties.getProperty("rootPath");
		} catch (IOException e) {
			System.out.println("读取webservices文件出错，请检查src下是否存在webservice.properties文件");
		}
		return rootPath;
	}
}