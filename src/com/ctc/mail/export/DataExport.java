package com.ctc.mail.export;


import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ctc.mail.common.IOManagerUtil;
import com.ctc.mail.dao.TestSqlHelper;

public class DataExport {
	public static SimpleDateFormat f = new SimpleDateFormat("M");
	public static SimpleDateFormat tf = new SimpleDateFormat("yyyyMM");
	// 工程根目录
	public static Date date = new Date();
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	
	public static void main(String[] args) throws IOException, SQLException {
		dataExport();
		/*String[] sheets = {"139邮箱","QQ邮箱","网易邮箱","189邮箱"};
		String[] header = {"指标,,产品,,,,",",网络,139邮箱,QQ邮箱,网易邮箱,189邮箱,平均值"};
		String[] left = {"首次登录","接收本域邮件时延","打开未读邮件时延","发送邮件时延","附件下载时延",
				"登录成功率（首次）","发送邮件成功率","接收邮件成功率","首次加载流量","空刷无新邮件流量",
				"待机无新邮件流量","待机接收新邮件流量","待机中无接收邮件电量","待机中无接收邮件电量"};
		int[] nums = {19,41};
		String fileName = "139邮箱客户端Android"+f.format(new Date())+"月份考核原始数据.xls"; // 记录文档的文件名
		IOManagerUtil.writeToExcelFile(fileDir+fileName,sheets,header,left,nums);*/

	}
	
	/**
	 * 导出Android测试数据
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void dataExport() throws IOException, SQLException{
		 String[] networkType = {"CMCC","4G"};
		 String[] table = {"tb_app_data_"+tf.format(new Date())+",原始数据","tb_app_data_filter_"+tf.format(new Date())+",筛选数据","tb_app_data_result_"+tf.format(new Date())+",数据"};
		 String[] productName = {"139","QQ","163","189"};
		 int groupCount = 15;//获取组数
		 int rows = 0;
		 for(int k = 0; k < table.length; k++){
			 if(k!=0){
			  groupCount = 10;
			 }
			 String fileName = "139邮箱客户端Android"+f.format(new Date())+"月份考核"+table[k].split(",")[1]+".xls"; // 记录文档的文件名
		     File file = new File(fileDir+fileName);
			 if(file.exists()){
				 file.delete();
			 }
			 for(int j = 0; j < productName.length;j++){
		     for(int i = 0 ; i < networkType.length; i++){
			 
		   	 String[] header = getHeader(networkType[i]);
		   	
		   	 //所有数据
		   	 ResultSet allResultSet = TestSqlHelper.sqlQueryData(table[k].split(",")[0],productName[j],networkType[i]);
		   	 rows = IOManagerUtil.writeToExcelFile(fileDir+fileName,productName[j].replace("163", "网易")+"邮箱",allResultSet,header,getMergedRegion(groupCount,rows,networkType[i]));
	       }
		  }
		 }
	     System.out.println("保存数据到Excel成功！！！");
	}
	
	/**
	 * 获取合并单元格列表
	 * @param i 15条还是10条
	 * @param rows
	 * @param networkType
	 * @return
	 */
	public static String[] getMergedRegion(int i,int rows,String networkType){
		 String[] regionsCMCC = {"$A$1:$O$1","$A$2:$A$3","$B$2:$F$2","$G$2:$I$2","$J$2:$O$2",
	   			 "$G$4:$G$"+(i+3)+"","$H$4:$H$"+(i+3)+"","$I$4:$I$"+(i+3)+"",
	   			 "$L$4:$L$6","$L$7:$L$9","$L$10:$L$13",
	   			 "$M$4:$M$6","$M$7:$M$9","$M$10:$M$13",
	   			 "$N$4:$N$6","$N$7:$N$9","$N$10:$N$13",
	   			 "$O$4:$O$6","$O$7:$O$9","$O$10:$O$13"};
		 String[] regions4G = {"$A$"+(rows+4)+":$O$"+(rows+4)+"","$A$"+(rows+5)+":$A$"+(rows+6)+"","$B$"+(rows+5)+":$F$"+(rows+5)+"","$G$"+(rows+5)+":$I$"+(rows+5)+"","$J$"+(rows+5)+":$O$"+(rows+5)+"",
	   			 "$G$"+(rows+7)+":$G$"+(rows+(i+6))+"","$H$"+(rows+7)+":$H$"+(rows+(i+6))+"","$I$"+(rows+7)+":$I$"+(rows+(i+6))+"","$L$"+(rows+7)+":$L$"+(rows+9)+"","$L$"+(rows+10)+":$L$"+(rows+12)+"","$L$"+(rows+13)+":$L$"+(rows+16)+"",
	   			 "$M$"+(rows+7)+":$M$"+(rows+9)+"","$M$"+(rows+10)+":$M$"+(rows+12)+"","$M$"+(rows+13)+":$M$"+(rows+16)+"",
	   			 "$N$"+(rows+7)+":$N$"+(rows+9)+"","$N$"+(rows+10)+":$N$"+(rows+12)+"","$N$"+(rows+13)+":$N$"+(rows+16)+"",
	   			 "$O$"+(rows+7)+":$O$"+(rows+9)+"","$O$"+(rows+10)+":$O$"+(rows+12)+"","$O$"+(rows+13)+":$O$"+(rows+16)+""};
		if(networkType.equals("CMCC")){
			return regionsCMCC;
		}else{
			return regions4G;
		}
	}
	/**
	 * 获取头部
	 * @param networkType
	 * @return
	 */
	public static String[] getHeader(String networkType){
		 String[] header = {networkType+",null,null,null,null,null,null,null,null,null,null,null,null,null,null",
				 "次数,即时性,null,null,null,null,成功率,null,null,资源消耗,null,null,null,null,null",
	   			 ",首次登录,接收本域邮件时延,打开未读邮件时延,发送邮件时延,附件下载时延,登录成功率（首次）,"
	   			 + "发送邮件成功率,接收邮件成功率,首次加载流量,空刷无新邮件流量,待机无新邮件流量,待机接收新邮件流量,"
	   			 + "待机中无接收邮件电量,待机中接收邮件电量"};
	   	 String[]  bottom =  {",,,,,,,,,,,,,,",
	   			 ",S,S,S,S,S,,,,Kb,Kb,Kb,Kb,焦,焦,",",,,,,,,,,,,,,,"};
	   	 if(networkType.equals("4G")){
	   	 bottom= Arrays.copyOf(bottom,bottom.length+header.length);
	   	 System.arraycopy(header, 0, bottom, 3,3 );
	   	 return bottom;
	   	 }
	   	 return header;
	}
	
}
