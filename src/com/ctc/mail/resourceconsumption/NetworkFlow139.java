package com.ctc.mail.resourceconsumption;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import pers.vinken.appiumUtil.elementManager;

import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.common.User;
import com.ctc.mail.dao.TestSqlHelper;
import com.ctc.mail.receivemail.Re139;
import com.ctc.mail.successratio.Components139;

/**
 * 资源消耗脚本
 * 机型  三星Note4
 * @author yang
 *
 */
public class NetworkFlow139 {
	public  SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public AndroidDriver driver;
	// 工程根目录
    String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String appPackage = "cn.cj.pe"; // 程序的package
	String appPackage360 = "com.qihoo360.mobilesafe"; // 程序的package
	String appActivity = "com.mail139.about.LaunchActivity"; // 程序的Activity
	String appActivity360 = "com.qihoo360.mobilesafe.ui.index.AppEnterActivity"; // 360程序的Activity
	String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	String screenshotName = "mail139_test139app"; // 截图的名字前缀

	@Before
	public void setUp() throws Exception {
		//清除缓存
		//CommonUtil.adbClearCache(appPackage);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("newCommandTimeout", "7200");//设置超时时间
		capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("resetKeyboard", true);
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	
	/**
	 * 首次登陆流量消耗 
	 * 运行此脚本需要将139邮箱APP图标放在手机第一页，即按Home键可以看到
	 * @throws Exception
	 */
	@Test
	public void testLoginFlow() throws Exception {
		try {
			
			boolean dataEnabled = driver.getNetworkConnection().dataEnabled();
			boolean wifiEnabled = driver.getNetworkConnection().wifiEnabled();
			String networkType = "";
			if((!wifiEnabled) && dataEnabled){
				networkType = "4G";
			}else {
				networkType = "CMCC";
			}
			
			System.out.println("---------------139邮箱客户端"+networkType+"网络资源消耗测试----------------");
			
			for(int i = 0; i < 15; i++){
			CommonUtil.sleep(3000);
			Components139 components = new Components139();
			User user = new User("18718879146","18879146");
			components.clearCache(driver,"139邮箱");
			components.clearFlow(driver);
			elementManager.waitForElement(driver, By.name("139邮箱"), 30).click();
			components.testFirstLoginNoCache(driver, networkType, user,i+1);
		   }
		} catch (Exception e) {
			//截图
			CommonUtil.screenShot(driver,fileDir,screenshotName,0);
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 空刷流量消耗 
	 * @throws Exception
	 */
	@Test
	public void testBrushFlow() throws Exception {
		try {
			String strflowtotal="";
			long waittime = 300000;
			boolean dataEnabled = driver.getNetworkConnection().dataEnabled();
			boolean wifiEnabled = driver.getNetworkConnection().wifiEnabled();
			String networkType = "";
			if((!wifiEnabled) && dataEnabled){
				networkType = "4G";
			}else {
				networkType = "CMCC";
			}
			
			Components139 components = new Components139();
			User user = new User("18718879146","18879146");
			components.testLoadMailNoCache(driver, networkType, user);	
			
			//等待5分钟
			CommonUtil.sleep(waittime);
			//清空流量
			components.clearFlow(driver);
			for(int i=0;i<30;i++){		  
			  strflowtotal=components.testBrushMail(driver,networkType);
			  String currentTime = CommonUtil.currentTime();
			  System.out.println("当前时间："+currentTime+" 第"+i+"次消耗流量:"+strflowtotal);
			  //空刷流量数据保存到数据库
			  String[] parameters = { strflowtotal.replace("K", "").replace("B", "").replace("M", ""), networkType,String.valueOf(i+1)};
		      TestSqlHelper.sqlUpdateBrushFlow(parameters);
			}
		} catch (Exception e) {
			//截图
			CommonUtil.screenShot(driver,fileDir,screenshotName,0);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 待机下无邮件接收 流量、电量消耗  
	 * @throws Exception
	 */
	@Test
	public void testNoReceiveStandby() throws Exception {
		try {
			String strFlowTotal = "";
			String strStartPowerTutor = "";
			String strEndPowerTutor = "";
			String strPowerTutor = "";
			String groupId = "1";
			List<String> strList = null;
			boolean dataEnabled = driver.getNetworkConnection().dataEnabled();
			boolean wifiEnabled = driver.getNetworkConnection().wifiEnabled();
			String networkType = "";
			if((!wifiEnabled) && dataEnabled){
				networkType = "4G";
			}else {
				networkType = "CMCC";
			}
			
			Components139 components = new Components139();
			User user = new User("18718879146","18879146");
			components.testLoadMailNoCache(driver, networkType, user);
			for(int i=0;i<1;i++)
			{
			  strList = components.testStandby(driver,"139邮箱",networkType);
			  strFlowTotal = strList.get(0);
			  strStartPowerTutor = strList.get(1);
			  strEndPowerTutor = components.recordPower(driver, "139邮箱");
			  strPowerTutor = components.getPowerTutor(strStartPowerTutor,strEndPowerTutor);
			  CommonUtil.adbClearCache("edu.umich.PowerTutor");
			  String currentTime = CommonUtil.currentTime();
			  System.out.println("当前时间："+currentTime+" 无邮件接收流量、电量消耗"+strFlowTotal+" "+strPowerTutor);
			 
			  String[] flowParams = { "139",networkType,f.format(new Date()),"无邮件流量消耗",strFlowTotal.replace("K", "").replace("B", "").replace("M", ""),groupId};
		    	 TestSqlHelper.sqlInsertStandby(flowParams);
		      String[] powerParams = { "139",networkType,f.format(new Date()),"无邮件电量消耗",strPowerTutor.replace(" J", ""),groupId};
		    	 TestSqlHelper.sqlInsertStandby(powerParams);
			}
		} catch (Exception e) {
			//截图
			CommonUtil.screenShot(driver,fileDir,"NoReceiveStandbyCMCC",0);
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 待机下邮箱接收流量、电量消耗 
	 * @throws Exception
	 */
	
	@Test
	public void testHasReceiveStandby() throws Exception {
		try {
			String strFlowTotal = "";
			String strStartPowerTutor = "";
			String strEndPowerTutor = "";
			String strPowerTutor = "";
			String groupId = "1";
			List<String> strList = null;
			boolean dataEnabled = driver.getNetworkConnection().dataEnabled();
			boolean wifiEnabled = driver.getNetworkConnection().wifiEnabled();
			String networkType = "";
			
			if((!wifiEnabled) && dataEnabled){
				networkType = "4G";
			}else {
				networkType = "CMCC";
			}
			
			Components139 components = new Components139();
			User user = new User("18718879146","18879146");
			components.testLoadMailNoCache(driver, networkType, user);
			for(int i=0;i< 1;i++){				
			  strList = components.testThreeMailStandby(driver,"139邮箱",networkType);	
			  strFlowTotal = strList.get(0);
			  strStartPowerTutor = strList.get(1);
			  strEndPowerTutor = components.recordPower(driver, "139邮箱");
			  strPowerTutor = components.getPowerTutor(strStartPowerTutor,strEndPowerTutor);
			  String currentTime = CommonUtil.currentTime();
			  
			  System.out.println("当前时间："+currentTime+" 接收流量、电量消耗"+strFlowTotal+" "+strPowerTutor);
			 
			  String[] flowParams = { "139",networkType,f.format(new Date()),"有邮件流量消耗",strFlowTotal.replace("K", "").replace("B", "").replace("M", ""),groupId};
		    	 TestSqlHelper.sqlInsertStandby(flowParams);
		      String[] powerParams = { "139",networkType,f.format(new Date()),"有邮件电量消耗",strPowerTutor.replace(" J", ""),groupId};
		    	 TestSqlHelper.sqlInsertStandby(powerParams);
			 // components.testDeleteMail(driver,appPackage,appActivity);
			}
		} catch (Exception e) {
			CommonUtil.screenShot(driver, fileDir, "HasReceiveStandbyCMCC",1);
			e.printStackTrace();
		}
	}
	
	

}
