package com.sdjz.eshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelReader {
	
	 	private POIFSFileSystem fs;
	    private HSSFWorkbook wb;
	    private HSSFSheet sheet;
	    private HSSFRow row;

	    /**
	     * 读取Excel表格表头的内容
	     * @param InputStream
	     * @return String 表头内容的数组
	     */
	    @SuppressWarnings("deprecation")
		public String[] readExcelTitle(InputStream is) {
	        try {
	            fs = new POIFSFileSystem(is);
	            wb = new HSSFWorkbook(fs);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        sheet = wb.getSheetAt(0);
	        row = sheet.getRow(1);
	        // 标题总列数
	        int colNum = row.getPhysicalNumberOfCells();
	        
	        String[] title = new String[colNum];
	        for (int i = 0; i < colNum; i++) {
	            //title[i] = getStringCellValue(row.getCell((short) i));
	            title[i] = getCellFormatValue(row.getCell((short) i));
	        }
	        return title;
	    }

	    /**
	     * 读取Excel数据内容
	     * @param InputStream
	     * @return Map 包含单元格数据内容的Map对象
	     */
	    @SuppressWarnings("deprecation")
		public Map<Integer, List<Object>> readExcelContent(InputStream is) {
	        Map<Integer, List<Object>> content = new HashMap<Integer, List<Object>>();
	        try {
	            fs = new POIFSFileSystem(is);
	            wb = new HSSFWorkbook(fs);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        sheet = wb.getSheetAt(0);
	        // 得到总行数
	        int rowNum = sheet.getLastRowNum();
	        row = sheet.getRow(1);
	        int colNum = row.getPhysicalNumberOfCells();
	        // 正文内容应该从第三行开始,第一行为表头的标题
	        for (int i =2; i <rowNum; i++) {
	        	List<Object> li = new ArrayList<Object>();
	            row = sheet.getRow(i);
	            int j = 0;
	            while (j < colNum) {
	                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
	                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
	                // str += getStringCellValue(row.getCell((short) j)).trim() +
	                // "-";
	            	li.add(getCellFormatValue(row.getCell((short) j)).trim());
	                
	                j++;
	            }
	            content.put(i, li);
	           
	        }
	        return content;
	    }
	    
	    /**
	     * 读取Excel数据内容
	     * @param InputStream
	     * @return Map 包含单元格数据内容的Map对象
	     */
	    @SuppressWarnings("deprecation")
		public List<List<String>> readExcelContentList(InputStream is) {
	    	List<List<String>> list = new ArrayList<List<String>>();
	        try {
	            fs = new POIFSFileSystem(is);
	            wb = new HSSFWorkbook(fs);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        sheet = wb.getSheetAt(0);
	        // 得到总行数
	        int rowNum = sheet.getLastRowNum();
	        row = sheet.getRow(0);
	        int colNum = row.getPhysicalNumberOfCells();
	        // 正文内容应该从第三行开始,第一行为表头的标题
	        for (int i = 1; i <= rowNum; i++) {
	        	List<String> li = new ArrayList<String>();
	            row = sheet.getRow(i);
	            int j = 0;
	            while (j < colNum) {
	                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
	                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
	                // str += getStringCellValue(row.getCell((short) j)).trim() +
	                // "-";
	            	li.add(getCellFormatValue(row.getCell((short) j)).trim());
	                
	                j++;
	            }
	            list.add(li);
	           
	        }
	        return list;
	    }

	    /**
	     * 根据HSSFCell类型设置数据
	     * @param cell
	     * @return
	     */
	    private String getCellFormatValue(HSSFCell cell) {
	        String cellvalue = "";
	        if (cell != null) {
	            // 判断当前Cell的Type
	            switch (cell.getCellType()) {
	            // 如果当前Cell的Type为NUMERIC
	            case HSSFCell.CELL_TYPE_NUMERIC:
	            case HSSFCell.CELL_TYPE_FORMULA: {
	                // 判断当前的cell是否为Date
	                if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                    // 如果是Date类型则，转化为Data格式
	                    
	                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
	                    //cellvalue = cell.getDateCellValue().toLocaleString();
	                    
	                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
	                    Date date = cell.getDateCellValue();
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                    cellvalue = sdf.format(date);
	                    
	                }
	                // 如果是纯数字
	                else {
	                    // 取得当前Cell的数值
	                    cellvalue = String.valueOf(cell.getNumericCellValue());
	                }
	                break;
	            }
	            // 如果当前Cell的Type为STRIN
	            case HSSFCell.CELL_TYPE_STRING:
	                // 取得当前的Cell字符串
	                cellvalue = cell.getRichStringCellValue().getString();
	                break;
	            // 默认的Cell值
	            default:
	                cellvalue = "";
	            }
	        } else {
	            cellvalue = "";
	        }
	        return cellvalue;

	    }

}
