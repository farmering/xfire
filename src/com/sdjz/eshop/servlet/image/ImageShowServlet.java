package com.sdjz.eshop.servlet.image;

import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
public class ImageShowServlet extends HttpServlet {  
	private static final long serialVersionUID = -7811827376583622456L;
	@Override  
    protected void service(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        OutputStream os = response.getOutputStream(); 
        String filePath=request.getParameter("filePath");
        File file = new File(filePath);  
        FileInputStream fips = new FileInputStream(file);  
        byte[] btImg = readStream(fips);  
        os.write(btImg);  
        os.flush(); 
        fips.close();
        os.close();
    }  
      
    /** 
     * 读取管道中的流数据 
     */  
    public byte[] readStream(InputStream inStream) {  
        ByteArrayOutputStream bops = new ByteArrayOutputStream();  
        int data = -1;  
        try {  
            while((data = inStream.read()) != -1){  
                bops.write(data);  
            } 
            bops.close();
            return bops.toByteArray();  
        }catch(Exception e){  
            return null;  
        }  
    }  
}  