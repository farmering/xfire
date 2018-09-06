package com.sdjz.eshop.servlet.image;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.sdjz.eshop.util.JZStringUtils;

public class AntiFakeCode extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 禁止图像缓存。
//		response.setHeader("Pragma", "no-cache");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setDateHeader("Expires", 0);
		response.setContentType("image/bmp");

		ServletOutputStream sos = response.getOutputStream();
		String antiFakeCode = request.getParameter("antiFakeCode");
		antiFakeCode=URLDecoder.decode(antiFakeCode,"utf-8");
		String width = request.getParameter("width");
		String height = request.getParameter("height");

		if (width == null || width.isEmpty()) {
			width = "100";
		}
		if (height == null || height.isEmpty()) {
			height = "100";
		}

		try {
			ZxingFactory.encodeQRToStream(antiFakeCode, BarcodeFormat.QR_CODE,
							Integer.parseInt(width), Integer.parseInt(height),
							sos, "bmp");
					
		} catch (WriterException e) {
			e.printStackTrace();
		}

		sos.close();
	}

}
