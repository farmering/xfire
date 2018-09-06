package com.sdjz.eshop.servlet.recorder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sdjz.eshop.entity.InspecRecord;
import com.sdjz.eshop.service.enterprice.impl.InspecRecordServiceImpl;
import com.sdjz.eshop.util.SpringUtil;

public class RecorderServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8161114758495868045L;
	/**
	 * Constructor of the object.
	 */
	public RecorderServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Put your code here
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	/* 构建pdf 并回填数据  */
    public ByteArrayInputStream getWordFile(String id) throws Exception {
    	//InspecRecord inspecRecord=new InspecRecord();
    	InspecRecordServiceImpl inspecRecordService = (InspecRecordServiceImpl)SpringUtil.getBean("inspecRecordServiceImpl");
		//InspecRecordAction ins=new InspecRecordAction();
    	InspecRecord inspecRecord=inspecRecordService.get(id);
        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(doc, baos);
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H", false ); 
            doc.open();
            Font titleFont =  new  Font(bfChinese  ,  15 , Font.NORMAL);  
            Font contextFont =  new  Font(bfChinese  ,  12 , Font.NORMAL);  
            Font contextFont1 =  new  Font(bfChinese  ,  8 , Font.NORMAL);  
            doc.open();
            String titleString = "特种设备现场安全监督检查记录";
            Paragraph title = new Paragraph(titleString, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            //title.setLeading(10f);
            title.setSpacingBefore(80);
            doc.add(title);
            DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	DateFormat dateTimeformat1 = new SimpleDateFormat("yyyy-MM-dd");
            PdfPTable table = new PdfPTable(9);//设置列数为9的表格
            table.setSpacingBefore(30f);
            table.setWidthPercentage(80);
            String type="";
    		if("1".equals(inspecRecord.getInspectType())){
    			type+="全面检查";
    		}else if("2".equals(inspecRecord.getInspectType())){
    			type+="专项检查";
    		}
    		 PdfPCell cell = new PdfPCell(new Paragraph("检查类别", contextFont));
             cell.setColspan(2); 
             cell.setMinimumHeight(20);
             table.addCell(cell);
             PdfPCell cell1 = new PdfPCell(new Paragraph(type, contextFont));
             cell1.setColspan(7); 
             table.addCell(cell1);
             if(inspecRecord.getInspectDateBegan()!=null&&inspecRecord.getInspectDateEnd()!=null){
     			String strBeginDate = dateTimeformat.format(inspecRecord.getInspectDateBegan());
     			String strEndDate = dateTimeformat.format(inspecRecord.getInspectDateEnd());
                PdfPCell cell3 = new PdfPCell(new Paragraph("检查日期", contextFont));
                cell3.setColspan(2); 
                cell3.setMinimumHeight(20);
                table.addCell(cell3);
                PdfPCell cell4 = new PdfPCell(new Paragraph(strBeginDate+"   至       "+strEndDate, contextFont));
                cell4.setColspan(7); 
                cell4.setMinimumHeight(20);
                table.addCell(cell4);
     		}else if(inspecRecord.getInspectDateBegan()==null&&inspecRecord.getInspectDateEnd()!=null){
     			String strEndDate = dateTimeformat.format(inspecRecord.getInspectDateEnd());
                PdfPCell cell3 = new PdfPCell(new Paragraph("检查日期", contextFont));
                cell3.setColspan(2); 
                cell3.setMinimumHeight(20);
                table.addCell(cell3);
                PdfPCell cell4 = new PdfPCell(new Paragraph(""+"   至       "+strEndDate, contextFont));
                cell4.setColspan(7); 
                cell4.setMinimumHeight(20);
                table.addCell(cell4);	
     	    }else if(inspecRecord.getInspectDateBegan()!=null&&inspecRecord.getInspectDateEnd()==null){
     			String strBeginDate = dateTimeformat.format(inspecRecord.getInspectDateBegan());
                PdfPCell cell3 = new PdfPCell(new Paragraph("检查日期", contextFont));
                cell3.setColspan(2); 
                cell3.setMinimumHeight(20);
                table.addCell(cell3);
                PdfPCell cell4 = new PdfPCell(new Paragraph(strBeginDate+"   至       "+"", contextFont));
                cell4.setColspan(7); 
                cell4.setMinimumHeight(20);
                table.addCell(cell4);		
     	    }else if(inspecRecord.getInspectDateBegan()==null&&inspecRecord.getInspectDateEnd()==null){
                PdfPCell cell3 = new PdfPCell(new Paragraph("检查日期", contextFont));
                cell3.setColspan(2); 
                cell3.setMinimumHeight(20);
                table.addCell(cell3);
                PdfPCell cell4 = new PdfPCell(new Paragraph(""+"   至       "+"", contextFont));
                cell4.setColspan(7); 
                cell4.setMinimumHeight(20);
                table.addCell(cell4);
     	    }
            PdfPCell cell5 = new PdfPCell(new Paragraph("被检查单位情况", contextFont));
            cell5.setColspan(2); 
            cell5.setRowspan(4);
            cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Paragraph("名称:"+inspecRecord.getUseUnitsName(), contextFont));
            cell6.setColspan(4); 
            cell6.setMinimumHeight(20);
            table.addCell(cell6);
            PdfPCell cell7 = new PdfPCell(new Paragraph("法定代表人:"+inspecRecord.getManager(), contextFont));
            cell7.setColspan(3); 
            cell7.setMinimumHeight(20);
            table.addCell(cell7);
            PdfPCell cell8 = new PdfPCell(new Paragraph("地址："+inspecRecord.getUseUnitsAddress(), contextFont));
            cell8.setColspan(7); 
            cell8.setMinimumHeight(20);
            table.addCell(cell8);
            PdfPCell cell9 = new PdfPCell(new Paragraph("联系人:"+inspecRecord.getSinaName(), contextFont));
            cell9.setColspan(2); 
            cell9.setMinimumHeight(20);
            table.addCell(cell9);
            PdfPCell cell10 = new PdfPCell(new Paragraph("职务:"+inspecRecord.getInspectPosition(), contextFont));
            cell10.setColspan(2); 
            cell10.setMinimumHeight(20);
            table.addCell(cell10);
            PdfPCell cell11 = new PdfPCell(new Paragraph("联系电话:"+inspecRecord.getManagerTel(), contextFont));
            cell11.setColspan(3); 
            cell11.setMinimumHeight(20);
            table.addCell(cell11);
            PdfPCell cell12 = new PdfPCell(new Paragraph("类别："+inspecRecord.getInspectCategory(), contextFont));
            cell12.setColspan(7); 
            cell12.setMinimumHeight(20);
            table.addCell(cell12);
            PdfPCell cell13 = new PdfPCell(new Paragraph("        质量技术监督局特种设备安全监察员 "+inspecRecord.getInspectorName()+"依据《特种设备安全监察条例》的规定和有关安全技术规范的要求，对该单位进行安全监督检查，说明了来意，已出示了证件，该单位"+inspecRecord.getSinaName()+"陪同检查。", contextFont));
            cell13.setMinimumHeight(50);
            cell13.setColspan(9); 
            table.addCell(cell13);
            PdfPCell cell14 = new PdfPCell(new Paragraph("        检查主要内容："+inspecRecord.getTestContents()+"", contextFont));
            cell14.setColspan(9); 
            cell14.setMinimumHeight(100);
            table.addCell(cell14);
            PdfPCell cell15 = new PdfPCell(new Paragraph("设备类别", contextFont1));
            cell15.setColspan(1); 
            table.addCell(cell15);
            PdfPCell cell16 = new PdfPCell(new Paragraph("锅炉", contextFont1));
            cell16.setColspan(1); 
            table.addCell(cell16);
            PdfPCell cell17 = new PdfPCell(new Paragraph("压力容器", contextFont1));
            cell17.setColspan(1); 
            table.addCell(cell17);
            PdfPCell cell18 = new PdfPCell(new Paragraph("压力管道", contextFont1));
            cell18.setColspan(1); 
            table.addCell(cell18);
            PdfPCell cell19 = new PdfPCell(new Paragraph("电梯", contextFont1));
            cell19.setColspan(1); 
            table.addCell(cell19);
            PdfPCell cell20 = new PdfPCell(new Paragraph("起重机械", contextFont1));
            cell20.setColspan(1); 
            table.addCell(cell20);
            PdfPCell cell21 = new PdfPCell(new Paragraph("场内机动车", contextFont1));
            cell21.setColspan(1); 
            table.addCell(cell21);
            PdfPCell cell22 = new PdfPCell(new Paragraph("游乐设施", contextFont1));
            cell22.setColspan(1); 
            table.addCell(cell22);
            PdfPCell cell23 = new PdfPCell(new Paragraph("索道", contextFont1));
            cell23.setColspan(1); 
            table.addCell(cell23);
            PdfPCell cell24 = new PdfPCell(new Paragraph("抽查数量", contextFont1));
            cell24.setColspan(1); 
            table.addCell(cell24);
            if(inspecRecord.getGlnum()==null){
                PdfPCell cell25 = new PdfPCell(new Paragraph("", contextFont1));
                cell25.setColspan(1); 
                table.addCell(cell25);
	   		}else{
                PdfPCell cell25 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getGlnum()), contextFont1));
                cell25.setColspan(1); 
                table.addCell(cell25);
	   		}
	   		if(inspecRecord.getRqnum()==null){
                PdfPCell cell26 = new PdfPCell(new Paragraph("", contextFont1));
                cell26.setColspan(1); 
                table.addCell(cell26);
	   		}else{
                PdfPCell cell26 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getRqnum()), contextFont1));
                cell26.setColspan(1); 
                table.addCell(cell26);
	   		}
	   		if(inspecRecord.getGdnum()==null){
                PdfPCell cell27 = new PdfPCell(new Paragraph("", contextFont1));
                cell27.setColspan(1); 
                table.addCell(cell27);
	   		}else{
                PdfPCell cell27 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getGdnum()), contextFont1));
                cell27.setColspan(1); 
                table.addCell(cell27);
	   		}
	   		if(inspecRecord.getDtnum()==null){
                PdfPCell cell28 = new PdfPCell(new Paragraph("", contextFont1));
                cell28.setColspan(1); 
                table.addCell(cell28);
	   		}else{
                PdfPCell cell28 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getDtnum()), contextFont1));
                cell28.setColspan(1); 
                table.addCell(cell28);
	   		}	
	   		if(inspecRecord.getQznum()==null){
                PdfPCell cell29 = new PdfPCell(new Paragraph("", contextFont1));
                cell29.setColspan(1); 
                table.addCell(cell29);
	   		}else{
                PdfPCell cell29 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getQznum()), contextFont1));
                cell29.setColspan(1); 
                table.addCell(cell29);
	   		}	
	   		if(inspecRecord.getCcnum()==null){
                PdfPCell cell30 = new PdfPCell(new Paragraph("", contextFont1));
                cell30.setColspan(1); 
                table.addCell(cell30);
	   		}else{
                PdfPCell cell30 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getCcnum()), contextFont1));
                cell30.setColspan(1); 
                table.addCell(cell30);
	   		}	
	   		if(inspecRecord.getYlnum()==null){
                PdfPCell cell31 = new PdfPCell(new Paragraph("", contextFont1));
                cell31.setColspan(1); 
                table.addCell(cell31);
	   		}else{
                PdfPCell cell31 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getYlnum()), contextFont1));
                cell31.setColspan(1); 
                table.addCell(cell31);
	   		}
	   		if(inspecRecord.getSdnum()==null){
                PdfPCell cell32 = new PdfPCell(new Paragraph("", contextFont1));
                cell32.setColspan(1); 
                table.addCell(cell32);
	   		}else{
                PdfPCell cell32 = new PdfPCell(new Paragraph(String.valueOf(inspecRecord.getSdnum()), contextFont1));
                cell32.setColspan(1); 
                table.addCell(cell32);
	   		}
            PdfPCell cell33 = new PdfPCell(new Paragraph("安全监督检查中发现的主要问题（可另附续页）", contextFont));
            cell33.setColspan(9); 
            cell33.setMinimumHeight(20);
            table.addCell(cell33);
            PdfPCell cell34 = new PdfPCell(new Paragraph("          "+inspecRecord.getTestProblem(), contextFont));
            cell34.setMinimumHeight(100);
            cell34.setColspan(9); 
            table.addCell(cell34);
            PdfPCell cell35 = new PdfPCell(new Paragraph("          处理措施："+inspecRecord.getDisposalExplain(), contextFont));
            cell35.setMinimumHeight(100);
            cell35.setColspan(9); 
            table.addCell(cell35);
            
            PdfPCell cell36 = new PdfPCell(new Paragraph("      被检查单位对检查记录的意见 ：                                              "+inspecRecord.getQuestion(), contextFont));
            cell36.setColspan(5); 
            cell36.setFixedHeight(80);
            table.addCell(cell36);
            if(inspecRecord.getSinaDate()!=null){
    			String sinaDate = dateTimeformat1.format(inspecRecord.getSinaDate());
    			PdfPCell cell37 = new PdfPCell(new Paragraph("签名："+inspecRecord.getSinaName()+"         日期："+sinaDate, contextFont));
    			cell37.setColspan(4); 
                cell37.setFixedHeight(80);
                table.addCell(cell37);
    		}else{
    			PdfPCell cell37 = new PdfPCell(new Paragraph("签名："+inspecRecord.getSinaName()+"         日期："+"", contextFont));
    			cell37.setColspan(4); 
                cell37.setFixedHeight(80);
                table.addCell(cell37);
    		}
            if(inspecRecord.getSinaDate()!=null){
    			String inspectDate = dateTimeformat1.format(inspecRecord.getInspectDate());
    			PdfPCell cell38 = new PdfPCell(new Paragraph("特种设备安全监察员："+inspecRecord.getInspectorName() +" 记录员："+inspecRecord.getRecordName()+" 日期:"+ inspectDate, contextFont));
    			cell38.setColspan(9); 
    			cell38.setMinimumHeight(20);
    	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	        cell.setVerticalAlignment(Element.ALIGN_CENTER);
    			table.addCell(cell38);
            }else{
            	PdfPCell cell38 = new PdfPCell(new Paragraph("特种设备安全监察员："+inspecRecord.getInspectorName() +"         "+"  记录员："+inspecRecord.getRecordName()+"           "+"    日期:"+ "", contextFont));
    			cell38.setColspan(9); 
    			cell38.setMinimumHeight(20);
    	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    	        cell.setVerticalAlignment(Element.ALIGN_CENTER);
    			table.addCell(cell38);
            }
            doc.add(table);
            doc.close();
            
		} catch (Exception e) {
			// TODO: handle exception
		}

        // 得到输入流  
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        baos.close();
        return bais;
    }
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//头文件
			response.addHeader("Content-disposition","attachment; filename=123.pdf");
			response.setContentType("application/pdf;charset=utf-8");
			String id=request.getParameter("id");
			ByteArrayInputStream b= getWordFile(id);
			ServletOutputStream ou = response.getOutputStream();
	        PdfReader reader = new PdfReader(b);  
	        // 加完水印的文件  
	        PdfStamper stamper = new PdfStamper(reader, ou);  
	        Image upgif = Image.getInstance("C:/Users/XSB/Desktop/4592.png");
	        upgif.setAbsolutePosition(330,150); 
	        upgif.scalePercent(70);
	        PdfContentByte under = stamper.getUnderContent(1);  
	        under.addImage(upgif);
	        stamper.close();  
		    byte a[] = new byte[1024];
	    	int n = 0;
		    while (n != -1) {
		    	n = b.read(a);
			    if (n > 0) {
			      ou.write(a, 0, n);
			    }
		    }
		    b.close();
		    ou.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Put your code here
	}
	public String getServletInfo() {
		return "This is my default servlet created by Eclipse";
	}
	public void init() throws ServletException {
		// Put your code here
	}
}