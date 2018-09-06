package com.sdjz.eshop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class SendSMS {
	
	private String url;
	private String account;
	private String password;
	private String content;
	private Properties properties;
	
	private static SendSMS instance;
	
	private SendSMS() {
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader()
					.getResourceAsStream("sms.properties"));
			url = properties.getProperty("url");
			account = properties.getProperty("account");
			password = properties.getProperty("password");
			content = properties.getProperty("content");
		} catch (IOException e) {
			System.out
					.println("读取webservices文件出错，请检查src下是否存在sms.properties文件");
			url = "http://sms.106jiekou.com/utf8/sms.aspx";
			account = "wangfumao";
			password = "521007";
			content = "您的验证码是：【变量】。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
			e.printStackTrace();
		}
	}
	
	public static SendSMS getInstance() {
		if (instance == null) {
			instance = new SendSMS();
		}
		return instance;
	}

    public String SMS(String code,String mobile) {
        try {
            //发送POST请求
            URL urlpath = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlpath.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            String postData = "account="+account+
            				"&password="+password+
            				"&mobile="+mobile+
            				"&content="+java.net.URLEncoder.encode(content.replace("【变量】", code),"utf-8");
            
            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
    
    
}
