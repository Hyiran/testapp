package com.ctc.mail.timedelay;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.common.Status;
import com.ctc.mail.dao.TestSqlHelper;
import com.ctc.mail.speed.SpeedTestApp;

/**
 * 运行四个产品的脚本
 * @author yang
 *
 */
public class RunAllApp {
	public static SimpleDateFormat f = new SimpleDateFormat("YYYYMM");
	public static void main(String[] args) throws Exception {
		String groupId = "";
		try {
			String networkType  = CommonUtil.getNetworkType();//读取手机网络
			//获取上一次执行状态
			Status status = TestSqlHelper.noSuccess(CommonUtil.getName(),networkType);
			if(!status.getIsSuccess()){
				groupId = status.getGroupId();
				System.out.println("---------------上次执行失败！继续执行第"+groupId+"组！！！----------------");
				status.showInfo();
			}else{
				groupId = String.valueOf(getGroupId(networkType));
				status = TestSqlHelper.noSuccess(CommonUtil.getName(),networkType);
			}
			
			if(!status.getSpeed139().equals("Y")){
				startSpeed("139",groupId);
				
			}
			if(!status.getData139().equals("Y")){
				start139App(groupId);
			}
			
			if(!status.getSpeed163().equals("Y")){
			startSpeed("163",groupId);
			}
			if(!status.getData163().equals("Y")){
			start163App(groupId);
			}
			
			if(!status.getSpeedqq().equals("Y")){
			startSpeed("QQ",groupId);
			}
			if(!status.getDataqq().equals("Y")){
			startQQApp(groupId);
			}
			if(!status.getSpeed189().equals("Y")){
			startSpeed("189",groupId);
			}
			if(!status.getData189().equals("Y")){
			start189App(groupId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 启动测速软件
	 * @throws Exception
	 */
	public static void startSpeed(String productName,String groupId) throws Exception{
		SpeedTestApp speed = new SpeedTestApp();
		speed.setProductName(productName);
		speed.setGroupId(groupId);
		speed.setUp();
		speed.test();
		speed.tearDown();
	}
	
	/**
	 * 启动139APP 4G网络
	 * @throws Exception
	 */
	public static void start139App(String groupId) throws Exception{
		Test139 test139 = new Test139();
		test139.setGroupId(groupId);
		test139.setUp();
		test139.test();
		test139.tearDown();
	}
	
	

    /**
     * 启动163APP
     * @throws Exception
     */
    public static void start163App(String groupId) throws Exception{
    	Test163 test163 = new Test163();
    	test163.setGroupId(groupId);
    	test163.setUp();
    	test163.test();
    	test163.tearDown();
	}

    /**
     * 启动189APP
     * @throws Exception
     */
    public static void start189App(String groupId) throws Exception{
    	Test189 test189 = new Test189();
    	test189.setGroupId(groupId);
    	test189.setUp();
    	test189.test();
    	test189.tearDown();
    }

    /**
     * 启动QQAPP
     * @throws Exception
     */
    public static void startQQApp(String groupId) throws Exception{
    	TestQQ testQQ = new TestQQ();
    	testQQ.setGroupId(groupId);
    	testQQ.setUp();
    	testQQ.runTest();
    	testQQ.tearDown();
    }
	/**
	 * 获取组数
	 * @param netwokType
	 * @return
	 * @throws SQLException
	 */
    public static int getGroupId(String netwokType) throws SQLException{
    	int groupId = 0;
    	String month = f.format(new Date());

    	//是否存在组数,如果不存在则创建
		if(!TestSqlHelper.isExistOrder(month,netwokType)){
    		TestSqlHelper.insertOrder(month,netwokType);
    	}
		//获取组数
    	groupId = TestSqlHelper.getOrder(month,netwokType);
    	//写入组数
    	TestSqlHelper.updateOrder(groupId, month,netwokType);
    	TestSqlHelper.insertLog(CommonUtil.getName(), groupId,netwokType);
    	return groupId;
    }
    


}