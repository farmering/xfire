package com.sdjz.eshop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class SmsNofitier {
	private String userName = "dh21809"; // 用户名
	private String password = "21809.com"; // 密码
	public String url = "http://www.10690300.com/http/sms/Submit";

	public static void main(String[] args) {
		System.out.println();
		SmsNofitier nofitier = new SmsNofitier();
		try {
			nofitier.sendNotify("请按照要求于4月20日前填写统计表 （www.cnca.cn/ywzl/sysyjc/jyjctjzl）  省质监局",
								"15169064718");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public SmsNofitier() {
		super();
	}

	public SmsNofitier(String userName, String password, String url) {
		super();
		this.userName = userName;
		this.password = password;
		this.url = url;
	}

	public boolean sendNotify(String content, String phone) throws IOException{
		return send("" + System.currentTimeMillis(), phone, content, "", "", "");
	}

	// 发送短信
	/**
	 * 发送短信方法使用document 对象方法封装XML字符串
	 */
	private boolean send(String msgid, String phone, String content,
			String sign, String subcode, String sendtime) throws IOException{
		// String url = "http://3tong.net/http/sms/Submit";
		Map<String, String> params = new LinkedHashMap<String, String>();
		String message = DocXml(userName, MD5Encode(password), msgid, phone,
				content, sign, subcode, sendtime);
		System.out.println(message);
		params.put("message", message);
		String resp = doPost(url, params);
		System.out.println(resp);
		if (resp.indexOf("<result>0</result>") > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	private String doPost(String url, Map<String, String> params) throws IOException{
		HttpClient client = HttpClients.createDefault();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		
		// 设置Post数据
		if (!params.isEmpty()) {
			for (Entry<String, String> entry : params.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(entity);
		HttpResponse response = null;
		try {
			response = client.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		if(response !=null) {
			dump(response.getEntity());
			return response.getStatusLine().toString();
		} else {
			return "连接服务器失败！";
		}
	}

	/**
	 * 使用document 对象封装XML
	 * 
	 * @param userName
	 * @param pwd
	 * @param id
	 * @param phone
	 * @param contents
	 * @param sign
	 * @param subcode
	 * @param sendtime
	 * @return
	 */
	private String DocXml(String userName, String pwd, String msgid,
			String phone, String contents, String sign, String subcode,
			String sendtime) {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element message = doc.addElement("message");
		Element account = message.addElement("account");
		account.setText(userName);
		Element password = message.addElement("password");
		password.setText(pwd);
		Element msgid1 = message.addElement("msgid");
		msgid1.setText(msgid);
		Element phones = message.addElement("phones");
		phones.setText(phone);
		Element content = message.addElement("content");
		content.setText(contents);
		Element sign1 = message.addElement("sign");
		sign1.setText(sign);
		Element subcode1 = message.addElement("subcode");
		subcode1.setText(subcode);
		Element sendtime1 = message.addElement("sendtime");
		sendtime1.setText(sendtime);
		return message.asXML();

	}

	private String MD5Encode(String sourceString) {
		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byte2hexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	private final String byte2hexString(byte[] bytes) {
		StringBuffer bf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				bf.append("0");
			}
			bf.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return bf.toString();
	}
	
	/**
     * 打印页面
     * @param entity
     * @throws IOException
     */
    private static void dump(HttpEntity entity) throws IOException {
        BufferedReader br = new BufferedReader( new InputStreamReader(entity.getContent(), "UTF-8"));
        System.out.println(IOUtils.toString(br));
    }

}
