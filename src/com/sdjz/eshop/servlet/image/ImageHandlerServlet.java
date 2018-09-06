package com.sdjz.eshop.servlet.image;

import java.awt.image.BufferedImage;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class ImageHandlerServlet extends HttpServlet{	
	private static final long serialVersionUID = -6694452931894369831L;
	public void service(HttpServletRequest request, HttpServletResponse response){
        String srcImgFileName = request.getParameter("filePath");
        try {
	        if(srcImgFileName==null || "".equals(srcImgFileName)) {
				System.out.println("图像参数错误");
	        }
	        response.setContentType("image/jpeg");
	        ServletOutputStream sos = response.getOutputStream();
	        //调用PicZoom类的静态方法zoom对原始图像进行缩放
	        BufferedImage buffImg = PicZoom.zoom(srcImgFileName);
	        //创建JPEG图像编码器，用于编码内存中的图像数据到JPEG数据输出流
	        JPEGImageEncoder jpgEncoder = JPEGCodec.createJPEGEncoder(sos);
	        //编码BufferedImage对象到JPEG数据输出流
	        jpgEncoder.encode(buffImg);
	        sos.close();
        }catch (Exception e) {
			e.printStackTrace();
		}
	}
}