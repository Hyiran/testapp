package com.ctc.mail.resourceconsumption;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.common.Log;

import pers.vinken.appiumUtil.Safe360Record;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

/**
 * 空刷邮件（无新邮件）流量监控
 * @author xiaoM
 *
 */

public class TestBrushFlow189 {
	static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	static String fileDir = projectRootPath + "log/"; 
	static String appPackage = "com.corp21cn.mail189";
	static String appActivity = "com.corp21cn.mailapp.activity.ClientIntroducePage"; 
	static String fileName = "Rescource.xls";

	public AndroidDriver driver;

	String UserName = " 18718848936";
	String PassWord = "as123456";
	long time1 = 0;
	long time2 = 0;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "6.0");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("newCommandTimeout", "3660");//设置Appium超时时间

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	// 在此处进行测试代码编写
	public void test() throws Exception {
		testBrushFlow(driver);
	}

	private void testBrushFlow(AndroidDriver driver) {
		driver.resetApp();
		elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/mailsetselect_item_tv"), 60).click();
		if(elementManager.doesElementDisplay(driver, By.id("com.corp21cn.mail189:id/switch_account"))){
			elementManager.checkandClick(driver, By.id("com.corp21cn.mail189:id/switch_account"));
		}
		elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/mail_setuser"), 2).sendKeys(UserName);
		elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/mail_setpwd"),2).sendKeys(PassWord);
		driver.findElement(By.id("com.corp21cn.mail189:id/mail_setok")).click();
		elementManager.waitForElement(driver, By.name("收件箱"), 30);
		CommonUtil.sleep(10000);
		driver.sendKeyEvent(3);
		System.out.println("后台静止5分钟，待软件稳定");
		CommonUtil.sleep(60000);	
		Safe360Record.Clear360FlowRecord(driver);
		int	width = driver.manage().window().getSize().width;
		int	height = driver.manage().window().getSize().height;
		String networkType  = CommonUtil.getNetworkType();//读取手机网络
		for(int i=1;i<=10;i++){
			driver.findElement(By.name("189邮箱")).click();
			elementManager.waitForElement(driver, By.name("收件箱"), 120);
			driver.swipe(width/2, height*1/5, width/2, height*4/5, 500);
			CommonUtil.sleep(30000);
			driver.sendKeyEvent(3);
			String useflow = Safe360Record.recordFlow(driver,"189邮箱");
			Log.testTime("第"+i+"次空刷消耗流量:"+useflow);
			inputToExcel(i, networkType,useflow);
		}	
	}
	// 输入文字到excel表格，不用修改
	public  void inputToExcel(int caseNo,String networkType ,String useflow) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "189BrushFlow", caseNo + "\t"+networkType + "\t"+"空刷流量消耗："+"\t"+useflow+ "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
