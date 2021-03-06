package com.ctc.mail.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.common.IOManagerUtil;
import com.ctc.mail.dao.TestSqlHelper;

import pers.vinken.appiumUtil.ioManager;


/**
 * 用于从数据库导出网速表
 * @author yang
 *
 */
public class SpeedExport {
	public static SimpleDateFormat tf = new SimpleDateFormat("yyyyMM");
	public static SimpleDateFormat f = new SimpleDateFormat("M");
	// 工程根目录
	public static Date date = new Date();
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	
	
	public static void main(String[] args) throws IOException, SQLException {
		speedExport();
		 

	}
	
	/**
	 * 导出网速数据到Excel
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void speedExport() throws IOException, SQLException{
		 String[] networkType = {"CMCC","4G"};
		 
		 for(int i = 0 ; i < networkType.length; i++){
		 String fileName = f.format(new Date())+"月份"+networkType[i]+"网络测速情况表.xls"; // 记录文档的文件名
		 File file = new File(fileDir+fileName);
		 if(file.exists()){
			 file.delete();
		 }
		 String tbSpeed = "tb_app_net_"+tf.format(new Date());
		 String tbFilterSpeed = "tb_app_net_filter_"+tf.format(new Date());
	   	 String[] parameters = { networkType[i],networkType[i],networkType[i],networkType[i],networkType[i]};
	   	 String[] header = {"轮次,测试时间,139邮箱,,QQ邮箱,,网易邮箱,,189邮箱,,,",
	   			 ",,下行带宽,上行带宽,下行带宽,上行带宽,下行带宽,上行带宽,下行带宽,上行带宽,"
	   			 + "下行标准差,上行标准差,平均标准差"};
	   	 String[] regions = {"$C$1:$D$1","$E$1:$F$1","$G$1:$H$1","$I$1:$J$1"};
	   	 //所有网速
	   	 ResultSet allResultSet = TestSqlHelper.sqlQuerySpeed(parameters,tbSpeed);
	   	 
	   	 //筛选后的网速
	   	 ResultSet filterResultSet = TestSqlHelper.sqlQuerySpeed(parameters,tbFilterSpeed);
	   	 IOManagerUtil.writeToExcelFile(fileDir+fileName,"CMCC网络测速汇总表",allResultSet,header,regions);
	   	 IOManagerUtil.writeToExcelFile(fileDir+fileName,"有效数据网络测速表",filterResultSet,header,regions);
	    }
		 
	     System.out.println("保存网速到Excel成功！！！");
	}
	
	
	
	
		
    
		
	

}
