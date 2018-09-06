package com.sdjz.eshop.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sdjz.eshop.entity.Persons;

/**
 * 利用开源组件POI动态导出EXCEL文档 转载时请保留以下信息，注明出处！
 * @author leno
 * @version v1.0
 * @param <T>
 * 应用泛型，代表任意一个符合javabean风格的类
 * 注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 * byte[]表jpg格式的图片数据
 */
public class ExcelExport<T> {

	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	}
	
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel(title, headers, dataset, out, "yyyy-MM-dd");
	}
	
	public void exportExcel(String title, String[] headers, String[] attrs, Collection<T> dataset, OutputStream out) {
		exportExcel(title, headers, attrs, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern);
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * @param title 表格标题名
	 * @param headers 表格属性列名数组
	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。
	 * 此方法支持的 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out  与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
	 */
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		Workbook workbook = new XSSFWorkbook();
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		//sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式(加粗标题)
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		// 生成并设置另一个样式(普通样式)
		CellStyle style2 = workbook.createCellStyle();
		Font font2 = workbook.createFont();
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		style2.setFont(font2);

		CreationHelper helper = workbook.getCreationHelper();
		// 声明一个画图的顶级管理器
		Drawing drawing = sheet.createDrawingPatriarch();
		// 产生表格标题行
		Row row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			RichTextString text = new XSSFRichTextString(headers[i]);//xlsx
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			int columnIndex=0;
			for (short i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class<? extends Object> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (35 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
					    int pictureIdx = workbook.addPicture(bsValue, Workbook.PICTURE_TYPE_JPEG);
					    //add a picture shape
					    ClientAnchor anchor = helper.createClientAnchor();
					    //set top-left corner of the picture,
					    anchor.setCol1(3);
					    anchor.setRow1(2);
					    Picture pict = drawing.createPicture(anchor, pictureIdx);
					  //auto-size picture relative to its top-left corner
					    pict.resize();
					} else {
						// 其它数据类型都当作字符串简单处理
						if(value != null)
						textValue = value.toString();
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Cell cell = row.createCell(columnIndex);
						cell.setCellStyle(style2);
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							RichTextString richString = new XSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
						columnIndex++;
						System.out.println(cell.getRowIndex()+"+"+cell.getColumnIndex()+"="+fieldName+":"+textValue);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * @param title 表格标题名
	 * @param headers 表格属性列名数组
	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。
	 * 此方法支持的 javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out  与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
	 */
	public void exportExcel(String title, String[] headers, String[] attrs, Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		Workbook workbook = new XSSFWorkbook();
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		//sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式(加粗标题)
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		// 生成并设置另一个样式(普通样式)
		CellStyle style2 = workbook.createCellStyle();
		Font font2 = workbook.createFont();
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		style2.setFont(font2);

		CreationHelper helper = workbook.getCreationHelper();
		// 声明一个画图的顶级管理器
		Drawing drawing = sheet.createDrawingPatriarch();
		// 产生表格标题行
		Row row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			RichTextString text = new XSSFRichTextString(headers[i]);//xlsx
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			//Field[] fields = t.getClass().getDeclaredFields();
			int columnIndex=0;
			for (short i = 0; i < attrs.length; i++) {
				String fieldName = attrs[i];
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class<? extends Object> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}
					} else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (35 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
					    int pictureIdx = workbook.addPicture(bsValue, Workbook.PICTURE_TYPE_JPEG);
					    //add a picture shape
					    ClientAnchor anchor = helper.createClientAnchor();
					    //set top-left corner of the picture,
					    anchor.setCol1(3);
					    anchor.setRow1(2);
					    Picture pict = drawing.createPicture(anchor, pictureIdx);
					  //auto-size picture relative to its top-left corner
					    pict.resize();
					} else {
						// 其它数据类型都当作字符串简单处理
						if(value != null)
							textValue = value.toString();
						else
							textValue = "";
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					Cell cell = row.createCell(columnIndex);
					cell.setCellStyle(style2);
					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(textValue);
					if (matcher.matches()) {
						// 是数字当作double处理
						cell.setCellValue(Double.parseDouble(textValue));
					} else {
						RichTextString richString = new XSSFRichTextString(textValue);
						cell.setCellValue(richString);
					}
					columnIndex++;
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @param title sheet标题
	 * @param headers 列名的汉字明细
	 * @param attrs   数据集合中的key
	 * @param dataset 数据集合
	 * @param output
	 */
	public void exportExcel(String title, String[] headers, String[] attrs,List<Map<Object, Object>> dataset, ByteArrayOutputStream output) {
		// 声明一个工作薄
		//Workbook workbook = new XSSFWorkbook();
		Workbook workbook = new SXSSFWorkbook(100);
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 生成一个样式(加粗标题)
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		// 生成并设置另一个样式(普通样式)
		CellStyle style2 = workbook.createCellStyle();
		Font font2 = workbook.createFont();
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		style2.setFont(font2);
		//产生表格标题行
		Row row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			sheet.setColumnWidth(i, 4400);//调整宽度
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			RichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		//遍历集合数据，产生数据行
		Iterator<Map<Object, Object>> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Map<Object, Object> map = it.next();
			int cellIndex = 0;
			for (String str : attrs) {
				String key = str.trim().toUpperCase();
				Object value = map.get(key);
				Cell cell = row.createCell(cellIndex);
				cell.setCellStyle(style2);
				if (value != null) {
					String val = value.toString();
					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(val);
					if (matcher.matches()) {
						cell.setCellValue(Double.parseDouble(val));//是数字当作double处理
					} else {
						cell.setCellValue(new XSSFRichTextString(val));
					}
				} else {
					cell.setCellValue("");//为null是存入空数据
				}
				cellIndex++;
			}
		}
		try {
			workbook.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// 测试学生
		ExcelExport<Persons> ex = new ExcelExport<Persons>();
		String[] headers = { "姓名", "设份证号", "性别", "出生日期", "手机号", "联系电话" ,"邮箱" };
		List<Persons> dataset = new ArrayList<Persons>();
		dataset.add(new Persons("张三", "1234455344543344543", "男", new Date(),"13123433456","123","zs@163.com"));
		dataset.add(new Persons("李四", "234567654345676543", "女", new Date(),"13123433456","123","zs@163.com"));
		dataset.add(new Persons("王五", "234565432345675432", "男", new Date(),"13123433456","345","zs@163.com"));
		try {
			OutputStream out = new FileOutputStream("E://"+new Date().getTime()+".xlsx");
			ex.exportExcel("测试",headers, dataset, out);
			out.close();
			//JOptionPane.showMessageDialog(null, "导出成功!");
			System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}