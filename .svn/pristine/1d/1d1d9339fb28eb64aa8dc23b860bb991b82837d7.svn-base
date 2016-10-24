package com.ctc.mail.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

public class IOManagerUtil {
	/**
	 * 合并单元格
	 * @param strList 要合并的单元格列表如:$F$2:$H$2
	 */
	public static void addMergedRegion(HSSFSheet sheet,String[] regions){
		if(regions != null){
			for(int i = 0; i < regions.length; i++){
				//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列  
				//sheet.addMergedRegion(new CellRangeAddress(0,0,0,3)); 
				sheet.addMergedRegion(CellRangeAddress.valueOf(regions[i]));
			}	
		}
		
	}
	
	/**
	 * 创建表头
	 * @param book
	 * @param header
	 */
	public static void createHeader(HSSFWorkbook book,HSSFSheet sheet,String [] header){
		int lastRowNum = 0;
		if(sheet.getLastRowNum() != 0){
			lastRowNum = sheet.getLastRowNum()+1;
		}
		if(header != null){
			for(int i = 0 ; i < header.length; i++){
				HSSFRow row = sheet.createRow(lastRowNum+i);// 创建所需的行数
				String [] cols = header[i].split(",");
				for (int j = 0; j < cols.length; j++) {
				  HSSFCell cell = null; // 设置单元格的数据类型
				  cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
				  cell.setCellValue(cols[j].replace("null", "")); // 设置单元格的值
				  cell.setCellStyle(setCellStyle(book,"0.00"));
				}
			}
		}
	}
	
	/**
	 * 从其他工作薄获取数据保存为新的工作薄
	 * @param sheets
	 * @param header
	 * @param left
	 * @param num 获取行
	 * @throws IOException 
	 */
	public static void writeToExcelFile(String fileUrl,String[] sheets,String[] header,String[] left,int[] nums) throws IOException{
		int rowNum = 0;
        List<ReportCollection> reportCollection = new ArrayList<ReportCollection>();
		File file = new File(fileUrl);
		if(!file.exists()){
			System.out.println("文件不存在！！！");
			return;
		}
		//读取文件内容
		@SuppressWarnings("resource")
		FileInputStream Input = new FileInputStream(file);
		HSSFWorkbook book = new HSSFWorkbook(Input); // 新建一个工作簿
		HSSFSheet sheet = book.getSheet("汇总"); // 获取表格
		if(sheet == null){
			sheet = book.createSheet("汇总");
		}
		//写入表头
		if(header != null){
			createHeader(book, sheet, header);
		}
		rowNum = sheet.getLastRowNum()+1;
		for(int i = 0; i < sheets.length; i++){
			HSSFSheet oldsheet = book.getSheet(sheets[i]); // 获取表格
			
           for(int j = 0; j < nums.length; j++){
        	   HSSFRow row = oldsheet.getRow(nums[j]);
        	   String networkType = "";
        	   if(j == 0 ){
        		   networkType = "CMCC";   
        	   }else{
        		   networkType = "4G";   
        	   }
   			String productName = sheets[i];
   			String loginTime = row.getCell(1).toString();
   			String receiveTime = row.getCell(2).toString();
   			String unreadTime = row.getCell(3).toString();
   			String sendTime = row.getCell(4).toString();
   			String download = row.getCell(5).toString();
   			String loginRatio = row.getCell(6).toString().replace("%", "");
   			String sendRatio = row.getCell(7).toString().replace("%", "");
   			String receiveRatio = row.getCell(8).toString().replace("%", "");
   			String loadFlow = row.getCell(9).toString();
   			String burshFlow = row.getCell(10).toString();
   			String nomailStandyFlow = row.getCell(11).toString();
   			String hasmailStandyFlow = row.getCell(12).toString();
   			String nomailStandyPower = row.getCell(13).toString();
   			String hasmailStandyPower = row.getCell(14).toString();
   			ReportCollection report = new ReportCollection(productName,networkType,
   					   Double.parseDouble(loginTime),
   					   Double.parseDouble(receiveTime),
     			       Double.parseDouble(unreadTime),
     			       Double.parseDouble(sendTime),
					   Double.parseDouble(download),
					   Double.parseDouble(loginRatio),
					   Double.parseDouble(sendRatio),
					   Double.parseDouble(receiveRatio),
					   Double.parseDouble(loadFlow),
					   Double.parseDouble(burshFlow),
					   Double.parseDouble(nomailStandyFlow),
					   Double.parseDouble(hasmailStandyFlow),
					   Double.parseDouble(nomailStandyPower),
					   Double.parseDouble(hasmailStandyPower));
           }
			
				
				
			
		}
		
		FileOutputStream out = new FileOutputStream(file);
		book.write(out);
		out.close();
	}
	
	/**
	 * 从数据库导出为Excel
	 * @param fileUrl
	 * @param sheetName
	 * @param bodyResultSet
	 * @param header
	 * @param regions
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static int writeToExcelFile(String fileUrl, String sheetName,ResultSet bodyResultSet,String[] header,String[] regions) throws IOException, SQLException{
			int cols = bodyResultSet.getMetaData().getColumnCount();
			int rows = 0;
			File file = new File(fileUrl);
			
			//文件如果不存在，则创建；如果存在，则修改；
			if(!file.exists()){
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				
				file.getParentFile().mkdirs();
				@SuppressWarnings("resource")
				HSSFWorkbook newBook = new HSSFWorkbook(); // 新建一个工作簿
				FileOutputStream newOutput = new FileOutputStream(file);
				newBook.write(newOutput);
				newOutput.close();
			}
			//读取文件内容
			FileInputStream oldInput = new FileInputStream(file);
			
			@SuppressWarnings("resource")
			HSSFWorkbook oldBook = new HSSFWorkbook(oldInput); // 新建一个工作簿
			HSSFSheet sheet = oldBook.getSheet(sheetName); // 获取表格
			if (sheet == null) {
				sheet = oldBook.createSheet(sheetName); // 新建一个表格
			}
			if(regions != null){
				addMergedRegion(sheet,regions);
			}
			if(header != null){
			   createHeader(oldBook,sheet,header);
			}
			rows = sheet.getLastRowNum() + 1; // 获取已存在数据的行数
			while(bodyResultSet.next()){
				HSSFRow row = sheet.createRow(rows);// 创建所需的行数
				for (int j = 0; j < cols; j++) {
				  HSSFCell cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); // 设置单元格的数据类型
				  String value = bodyResultSet.getString(j+1);
				  if(value==null){
					  value = " ";
				  }
				  try {
					  cell.setCellValue(Double.parseDouble(value));
					} catch (NumberFormatException e) {
					  cell.setCellValue(value); 
					}   
				  // 设置单元格的值
				  String format = "0.00";
				  if(j==0){
					  format = "0";
				  }
				  cell.setCellStyle(setCellStyle(oldBook,format));//设置单元格格式
				}
				rows ++;
			}
			FileOutputStream oldOutput = new FileOutputStream(file);
			oldBook.write(oldOutput);
			oldOutput.close();
		    return rows;
	}
	
	/**
	 * 设置样式
	 * @param book
	 * @param format 0.00
	 * @return
	 */
	public static HSSFCellStyle setCellStyle(HSSFWorkbook book,String format){
		HSSFCellStyle style = book.createCellStyle();
		
		//style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); //设置单元格填充样式 SOLID_FOREGROUND纯色使用前景颜色填充
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 设置边界的类型单元格的左边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置单元格为水平对齐的类型
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //设置垂直居中
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
		return style;
	}
}
