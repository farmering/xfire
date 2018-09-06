package com.sdjz.eshop.util;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * Email 工具类 
 * @author Lee
 *
 */
public class CommonEmailUtil {
	
	private String email_host;  
	private String email_port;
	private String email_from_name;
	private String email_from_pwd;
	private String email_content_charset;
	private String email_from_username;	
	private String email_warn_attach_err;
	private String email_path;
	//单例子模式
	private static CommonEmailUtil instance;
	
	public CommonEmailUtil(){
		Properties props = new Properties();  
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("apache-common-email.properties"));
			email_host = props.getProperty("email.host");  
			email_port = props.getProperty("email.port");  
			email_from_name = props.getProperty("email.from.emailname");  
			email_from_pwd = props.getProperty("email.from.emailpwd"); 
			email_from_username = props.getProperty("email.from.username");
			email_content_charset =props.getProperty("email.content.charset");
			email_warn_attach_err = props.getProperty("email.warn.attach.err");
			this.setEmail_path(props.getProperty("email.path"));
		} catch (IOException e) {
			System.out.println("读取邮件服务器配置文件出错，请检查src下是否存在apache-common-email.properties文件");
			e.printStackTrace();
		} 
	}
	
	public static synchronized CommonEmailUtil getInstance() {
		if (instance == null) {
			instance = new CommonEmailUtil();
		}
		return instance;
	}
	
	/**
	 * 获取 Email对象
	 * @param to Email接收人
	 * @param subject Email 标题
	 * @return
	 * @throws Exception
	 */
	public HtmlEmail getHtmlEmail(String to, String subject) throws EmailException{

		HtmlEmail email = new HtmlEmail();
		this.emailCommonSetting(email);
		email.addTo(to);
		email.setSubject(subject);
		return email;
	}

	/**
	 * 获取文件名
	 * @param s 文件名，带路径兼容 win linux 路径
	 */
	public static String  getFileNameSend(String s) throws Exception{//这个改成void函数？
		int i = s.lastIndexOf("\\");
		if(i < 0 ||i >= s.length() - 1){
			i = s.lastIndexOf("/");
		if(i < 0 || i >= s.length() - 1)
			return s ;
		}
		return  s.substring(i + 1) ;		          
		
   }
	/**
	 * 发送简单邮件
	 * @param msg
	 * @param to 目标
	 * @param subject 主体内容
	 * @throws Exception
	 */
	public void sendSimpleEmail(String msg, String to, String subject) throws EmailException{
		Email email = new SimpleEmail();
		this.emailCommonSetting(email);
		email.setSubject(subject);
		email.setMsg(msg);
		email.addTo(to);
		email.send();
	}
	
	/**
	 * 发送本地或者局域网共享附件Email
	 * @param path 附件路径,eg: c:\data\1.txt ,\\192.168.1.123\share\data\1.txt 注意"\"必须在传入的时候进行转义为:"\\"
	 * @param msg 邮件正文
	 * @param to 收件人
	 * @param subject 邮件标题
	 */
	public void sendAttachmentLocal(List<String>urlList,String msg ,List<String>toList,String subject) throws UnsupportedEncodingException,EmailException{
		MultiPartEmail email = new MultiPartEmail(); 
		this.emailCommonSetting(email);
		email.setSubject(subject); 
		email.setMsg(msg); 
		for(String to : toList){
			email.addTo(to);
		}
		for(String url : urlList){
			EmailAttachment attachment = new EmailAttachment(); 
			attachment.setPath(url);
			attachment.setDisposition(EmailAttachment.ATTACHMENT); 
			//attachment.setDescription("描述：JS手册"); 
			attachment.setName(MimeUtility.encodeText(this.getFileName(url)));//解决附件名乱码问题
			// add the attachment 
			email.attach(attachment); 
		}
		email.send();
	}
	
		
	/**
	 * 群发短信提醒
	 * @param msgs  String []{memberId ,StandardNo,email ,telNum ,msg
	 * @param to
	 * @param subject
	 * @throws Exception
	 */
	public void sendWarnEmail(List<String []> msgsList ,String to,String subject) throws Exception{
		
		// Create the email message 		
		//MultiPartEmail email = new MultiPartEmail();
		HtmlEmail email = new HtmlEmail();
		this.emailCommonSetting(email);
		email.addTo(to);
		email.setSubject(subject); 
		String htmlMsg ="标准状态变化提醒:<br>";		
		for (String [] tmp :msgsList) {
			htmlMsg += tmp[4]+"<hr>" ;			 
		}
		email.setHtmlMsg(htmlMsg);
		email.send();				
	  }
	/**
	 * 群发短信提醒	
	 * @param to
	 * @param subject
	 * @throws Exception
	 */
	public void send15WarnEmail(String msg ,String to,String subject) throws Exception{
		
		// Create the email message 		
		//MultiPartEmail email = new MultiPartEmail();
		HtmlEmail email = new HtmlEmail();
		this.emailCommonSetting(email);
		email.addTo(to);
		email.setSubject(subject); 
		String htmlMsg ="您以下托管标准将在15天后到期:<br>";		
		htmlMsg += msg;
		email.setHtmlMsg(htmlMsg);
		email.send();				
	  }
	/**
	 * 发送远程网络上的附件Email
	 * @param url 远程附件URL
	 * @param to 收件人
	 * @param subject 邮件标题
	 * @param msg 邮件内容
	 */
	public void sendAttachmentRemote(String url,String msg,String to,String subject) throws Exception{
		EmailAttachment attachment = new EmailAttachment(); 
		attachment.setURL(new URL(url)); 
		attachment.setDisposition(EmailAttachment.ATTACHMENT); 
		//attachment.setDescription("描述：JS手册"); 
		attachment.setName(MimeUtility.encodeText(this.getFileName(url)));//解决附件名乱码问题 		
		// Create the email message 
		MultiPartEmail email = new MultiPartEmail(); 
		this.emailCommonSetting(email);
		email.addTo(to);
		email.setSubject(subject); 
		email.setMsg(msg); 
		// add the attachment 
		email.attach(attachment);  
		// send the email
		email.send();
	}	
	
	/**
	 * 发送多个远程网络上的附件Email
	 * @param urlList 邮件URl列表
	 * @param to 收件人
	 * @param subject 邮件标题
	 * @param msg 邮件内容
	 */
	public void sendAttachmentListRemote(List<String> urlList,String msg,String to,String subject) throws Exception{		
		// Create the email message 		
		MultiPartEmail email = new MultiPartEmail(); 
		this.emailCommonSetting(email);
		email.addTo(to);
		email.setSubject(subject); 
		email.setMsg(msg); 
		for(String url : urlList){
			EmailAttachment attachment = new EmailAttachment(); 
			attachment.setURL(new URL(url)); 
			attachment.setDisposition(EmailAttachment.ATTACHMENT); 
			//attachment.setDescription("描述：JS手册"); 
			attachment.setName(MimeUtility.encodeText(this.getFileName(url)));//解决附件名乱码问题
			// add the attachment 
			email.attach(attachment); 
		}
		// send the email
		email.send();
	}

	/**
	 * Email 通用配置
	 * 
	 * @param email
	 * @throws Exception
	 */
	private void emailCommonSetting(Email email) throws EmailException {
		email.setHostName(email_host);
		email.setSmtpPort(Integer.parseInt(email_port));
		email.setAuthenticator(new DefaultAuthenticator(email_from_name, email_from_pwd));
		// email.setSSLOnConnect(true);
		email.setFrom(email_from_name, email_from_username);
		email.setCharset(email_content_charset);
	}
	
	/**
	 * 获取文件名
	 * 
	 * @param s 文件名，带路径兼容 win linux 路径
	 */
	private String getFileName(String s) {// 这个改成void函数？
		int i = s.lastIndexOf("\\");
		if (i < 0 || i >= s.length() - 1) {
			i = s.lastIndexOf("/");
			if (i < 0 || i >= s.length() - 1)
				return s;
		}
		return s.substring(i + 1);

	}

	/**
	 * 转化 \\192.168.1.1\share\data\1.txt
	 * 路径为：\\\\192.168.1.1\\share\\data\\\1.txt
	 * 
	 * @param path
	 * @return
	 */
	public String parseWinSharepath(String path) {
		path = path.trim();
		path = "\\" + path;
		return path.replaceAll("\\\\", "\\\\\\\\");
	}

	public String getEmail_path() {
		return email_path;
	}

	public void setEmail_path(String email_path) {
		this.email_path = email_path;
	}

}