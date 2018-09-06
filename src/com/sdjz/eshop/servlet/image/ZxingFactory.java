package com.sdjz.eshop.servlet.image;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成工具
 * @author Administrator
 */
public class ZxingFactory {

	/**
	 * 生成二维码
	 * @param content
	 *            内容
	 * @param codeType
	 *            二位码的编码格式
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param outputStream
	 *            输出流
	 * @param imgType
	 *            输出流图片格式
	 * @throws WriterException
	 *             写错误异常
	 * @throws IOException
	 *             输入输出异常
	 */
	public static void encodeQRToStream(String content, BarcodeFormat codeType,
			int width, int height, OutputStream outputStream, String imgType)
			throws WriterException, IOException {

		// 读取源图像
		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();

		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		// 生成二维码
		BitMatrix matrix = new MultiFormatWriter().encode(content, codeType,
				width, height, hint);

		MatrixToImageWriter.writeToStream(matrix, imgType, outputStream);
	}
}
