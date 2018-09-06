package com.sdjz.eshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader2 {

	private Workbook wb;
	private Sheet sheet;
	private Row row;
	private boolean isXssf = false;

	public ExcelReader2(InputStream is) {
		try {
			wb = WorkbookFactory.create(is);
			if (wb instanceof XSSFWorkbook) {
				isXssf = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	public Object getCellFormatValue(Cell cell) {
		Object cellvalue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: {
				cellvalue = cell.getNumericCellValue();

				String type = (String) getCellFormatValue(cell.getRow().getCell(cell.getColumnIndex() + 1));
				if ("Date".equals(type)) {
					cellvalue = cell.getDateCellValue();
				} else if ("String".equals(type)) {
					Double value = Double.parseDouble(cellvalue.toString());
					cellvalue = value.longValue() + "";

				} else if ("Long".equals(type)) {
					cellvalue = cell.getNumericCellValue() + "";

				} else if ("Double".equals(type)) {
					cellvalue = cell.getNumericCellValue() + "";

				}
				break;
			}
			case Cell.CELL_TYPE_FORMULA: {
				FormulaEvaluator evaluator = null;
				if (isXssf) {
					evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) cell.getRow().getSheet().getWorkbook());
				} else {
					evaluator = new HSSFFormulaEvaluator((HSSFWorkbook) cell.getRow().getSheet().getWorkbook());
				}
				cell.setCellFormula(cell.getCellFormula());
				Cell cell1 = evaluator.evaluateInCell(cell);
				cellvalue = getCellFormatValue(cell1);
				break;
			}
			case Cell.CELL_TYPE_STRING: {
				cellvalue = cell.getRichStringCellValue().getString();
				// Object obj =
				// getCellFormatValue(cell.getRow().getCell(cell.getColumnIndex()+1));
				// String type = (String)obj;
				// if( "0".equals(cellvalue) && "String".equals(type)){
				// cellvalue = "";
				// }
				break;
			}
			default:
				return null;
			}
		}
		return cellvalue;
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	public String getCellValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: {
				cellvalue = cell.getNumericCellValue() + "";
				break;
			}

			case Cell.CELL_TYPE_STRING: {
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			}

			}
		}
		return cellvalue;
	}

	/**
	 * 
	 * @param is
	 * @param index
	 * @param X
	 *            行
	 * @param Y
	 *            列
	 * @return
	 */
	/*
	 * @SuppressWarnings("deprecation") public Map<String, Object>
	 * readBaseExcel(InputStream is, int index, int X,int Y) { try { fs = new
	 * POIFSFileSystem(is); wb = new HSSFWorkbook(fs); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * Map<String, Object> map=new HashMap<String, Object>(); sheet =
	 * wb.getSheetAt(index); int rowNum = sheet.getLastRowNum(); for (int i = X;
	 * i <= rowNum; i++) { row = sheet.getRow(i); String key =
	 * (String)getCellValue(row.getCell(Y)); if(key==null || key == ""){ break;
	 * } Object value1 = getCellFormatValue(row.getCell((Y + 1))); String type =
	 * (String)getCellValue(row.getCell((Y + 2))); if("Double".equals(type)){
	 * Double value = Double.valueOf(value1==null?"":value1.toString());
	 * map.put(key, value); }else if("Long".equals(type)){ Long
	 * value=Long.parseLong(value1==null?"":value1.toString()); map.put(key,
	 * value); }else if("Date".equals(type)){ //SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy-MM-dd"); map.put(key, value1); }else
	 * if("String".equals(type)){ map.put(key, value1); } } return map; }
	 */

	public Map<String, Object> readBaseExcel(int index, int X, int Y) {
		Map<String, Object> map = new HashMap<String, Object>();
		sheet = wb.getSheetAt(index);
		int rowNum = sheet.getLastRowNum();

		for (int i = X; i <= rowNum; i++) {
			row = sheet.getRow(i);
			String key = (String) getCellValue(row.getCell(Y));
			if (key == null || key == "") {
				break;
			}
			Object value1 = getCellFormatValue(row.getCell((Y + 1)));
			String type = (String) getCellValue(row.getCell((Y + 2))).trim();
			try {
				if(key.equals("prUnitsPost")) {
					if(Integer.parseInt(value1.toString())!=0) {
						if(value1.toString().length()!=6) {
							map.put("errorString", value1);
						}
					}
				}
				if(key.equals("vehicleFuelEXP")) {
						if(value1.toString()==null||value1.toString().equals("0.0")) {
							map.put("errorString", "燃料种类");
						}
				}
				if ("Double".equals(type)) {
					Double value = Double.valueOf(value1 == null ? "" : String.valueOf(value1));
					if (value != 0.0) {
						//map.put(key, null);
					//} else {
						map.put(key, value);
					}
				} else if ("Long".equals(type)) {
					Double value = Double.parseDouble(value1 == null ? "" : value1.toString());
					if (value.longValue() != 0) {
						//map.put(key, null);
					//} else {
						map.put(key, value.longValue());
					}
				} else if ("Date".equals(type)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String dat = sdf.format(value1);
					if (!(dat).equals("1899-12-31")) {
						//map.put(key, null);
					//} else {
						map.put(key, value1);
					}
				} else if ("String".equals(type)) {
					String value = String.valueOf(value1 == null ? "" : value1.toString());
					if (!value.equals("0")) {
					//	map.put(key, null);
					//} else {
						map.put(key, value.trim());
					}
				}
			} catch (Exception e) {
				map.put("errorString", value1);
				map.put("formatString", type);
				return map;
			}
		}
		return map;
	}

	/**
	 * 生成实体类
	 * 
	 * @param clzss
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public <T> T object(Class<T> clzss, Map<String, Object> map) throws Exception {
		T t = clzss.newInstance();
		Field[] fields = clzss.getDeclaredFields();
		for (int i = 0, length = fields.length; i < length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			Set<String> keys = map.keySet();
			Iterator<String> keyI = keys.iterator();
			while (keyI.hasNext()) {
				String key = keyI.next();
				if (fieldName.equalsIgnoreCase(key)) {
					String firstFieldNameLetter = fieldName.substring(0, 1).toUpperCase();
					String setMethodName = "set" + firstFieldNameLetter + fieldName.substring(1);
					Method setMethod = clzss.getMethod(setMethodName, field.getType());
					setMethod.invoke(t, map.get(key));
				}
			}
		}
		return t;
	}

	/**
	 * 辅机附件获取数据
	 * 
	 * @param sheetIndex
	 * @return
	 */
	public Map<Integer, List<Object>> readExcelContent(int sheetIndex) {// 压力管道数据读取
		Map<Integer, List<Object>> content = new HashMap<Integer, List<Object>>();
		sheet = wb.getSheetAt(sheetIndex);
		row = sheet.getRow(1);
		int colNum = row.getPhysicalNumberOfCells();
		List<String> type = new ArrayList<String>();
		for (int j = 0; j < colNum; j++) {
			if (row.getCell(j) == null) {
				break;
			}
			String t = row.getCell(j).getStringCellValue();
			type.add(t);
		}
		// 正文内容应该从第三行开始,第一行为表头的标题
		for (int i = 3;; i++) {
			Row row = sheet.getRow(i);
			if (row.getCell(0) == null || JZStringUtils.isNullOrBlank(row.getCell(0).getStringCellValue())) {
				break;
			}
			List<Object> li = new ArrayList<Object>();
			for (int j = 0; j < colNum; j++) {
				// if(row.getCell(j)==null){
				// break;
				// }
				Object obj = row.getCell(j);
				try {
					if ("Double".equals(type.get(j))) {

						if (obj.toString() == null || obj.toString() == "") {
							li.add(obj);
						} else {
							Double value = Double.valueOf(obj.toString());
							li.add(value);
						}
					} else if ("Long".equals(type.get(j))) {
						if (obj.toString() == null || obj.toString() == "") {
							li.add(obj);
						} else {
							Double value = Double.valueOf(obj.toString());
							li.add(value.longValue());
						}
					} else if ("Date".equals(type.get(j))) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						if (row.getCell(j).getDateCellValue() == null) {
							li.add(null);
						} else {
							Date date = row.getCell(j).getDateCellValue();
							sdf.format(date);
							li.add(sdf.format(date));
						}

					} else if ("String".equals(type.get(j))) {
						String value = String.valueOf(obj == null ? "" : obj.toString());
						li.add(value.trim());
					}
				} catch (Exception e) {
					li.clear();
					li.add("错误的是：" + obj);
					content.put(i, li);
					return content;
				}
			}
			content.put(i, li);
		}
		return content;
	}

	/**
	 * 资质获取数据
	 * 
	 * @param sheetIndex
	 * @return
	 */
	public Map<Integer, List<Object>> readExcelLicense() {
		Map<Integer, List<Object>> content = new HashMap<Integer, List<Object>>();
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(1);
		int colNum = row.getPhysicalNumberOfCells();
		List<String> type = new ArrayList<String>();
		for (int j = 0; j < colNum; j++) {
			if (row.getCell(j) == null) {
				break;
			}
			String t = row.getCell(j).getStringCellValue();
			type.add(t);
		}
		// 正文内容应该从第三行开始,第一行为表头的标题
		for (int i = 3;; i++) {
			Row row = sheet.getRow(i);
			if (row.getCell(0) == null || JZStringUtils.isNullOrBlank(row.getCell(0).getStringCellValue())) {
				break;
			}
			List<Object> li = new ArrayList<Object>();
			for (int j = 0; j < colNum; j++) {
				// if(row.getCell(j)==null){
				// break;
				// }
				Object obj = row.getCell(j);
				try {
					if ("Double".equals(type.get(j))) {

						if (obj.toString() == null || obj.toString() == "") {
							li.add(obj);
						} else {
							Double value = Double.valueOf(obj.toString());
							li.add(value);
						}
					} else if ("Long".equals(type.get(j))) {
						if (obj.toString() == null || obj.toString() == "") {
							li.add(obj);
						} else {
							Double value = Double.valueOf(obj.toString());
							li.add(value.longValue());
						}

					} else if ("Date".equals(type.get(j))) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						if (row.getCell(j).getDateCellValue() == null) {
							li.add(null);
						} else {
							Date date = row.getCell(j).getDateCellValue();
							sdf.format(date);
							li.add(sdf.format(date));
						}

					} else if ("String".equals(type.get(j))) {
						String value = String.valueOf(obj == null ? "" : obj.toString());
						li.add(value.trim());
					}
				} catch (Exception e) {
					li.clear();
					li.add("错误的是：" + obj);
					content.put(i, li);
					return content;
				}
			}
			content.put(i, li);
		}
		return content;
	}

	/**
	 * 压力管道获取数据
	 * 
	 * @param sheetIndex
	 * @return
	 */
	public Map<Integer, List<Object>> readYLGDExcelContent(int sheetIndex) {// 压力管道数据读取
		Map<Integer, List<Object>> content = new HashMap<Integer, List<Object>>();
		sheet = wb.getSheetAt(sheetIndex);
		row = sheet.getRow(2);
		List<String> type = new ArrayList<String>();
		for (int j = 0; j < 20; j++) {
			String t = row.getCell(j).getStringCellValue();
			type.add(t);
		}
		// 正文内容应该从第三行开始,第一行为表头的标题
		for (int i = 5;; i++) {
			Row row = sheet.getRow(i);
			if (row.getCell(0) == null || JZStringUtils.isNullOrBlank(row.getCell(0).getStringCellValue())) {
				break;
			}
			List<Object> li = new ArrayList<Object>();
			for (int j = 0; j < 20; j++) {
				Object obj = row.getCell(j);
				try {
					if ("Double".equals(type.get(j))) {
						if (obj.toString() == null || obj.toString() == "") {
							li.add(obj);
						} else {
							Double value = Double.valueOf(obj.toString());
							li.add(value);
						}
					} else if ("Long".equals(type.get(j))) {
						if (obj.toString() == null || obj.toString() == "") {
							li.add(obj);
						} else {
							Double value = Double.valueOf(obj.toString());
							li.add(value.longValue());
						}
					} else if ("Date".equals(type.get(j))) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						if (row.getCell(j).getDateCellValue() == null) {
							li.add(null);
						} else {
							Date date = row.getCell(j).getDateCellValue();
							sdf.format(date);
							li.add(sdf.format(date));
						}

					} else if ("String".equals(type.get(j))) {
						String value = String.valueOf(obj == null ? "" : obj.toString());
						li.add(value.trim());
					}
				} catch (Exception e) {
					li.clear();
					li.add("错误的是：" + obj);
					content.put(i, li);
					return content;
				}
			}
			content.put(i, li);
		}
		return content;
	}

	// 获得表名
	public String getTitle() {
		String title = null;
		sheet = wb.getSheetAt(0);
		title = sheet.getRow(0).getCell(1).toString().trim();
		return title;
	}
}