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

import com.ctc.mail.common.CommonUtil;
import pers.vinken.appiumUtil.LogManager;
import pers.vinken.appiumUtil.Safe360Record;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;
import io.appium.java_client.android.AndroidDriver;

/**
 * 首次登录流量加载监控
 * @author xiaoM
 *
 */

public class TestLoginFlow163 {
	static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");// 工程根目录
	String appPackage = "com.netease.mail"; // 程序的package
	String appActivity = "com.netease.mobimail.activity.LaunchActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "Rescource.xls"; // 记录文档的文件名
	String screenshotName = "mail163_test"; // 截图的名字前缀

	String UserName = "maomingtest3";
	String PassWord = "as123456";
	
	public AndroidDriver driver;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("newCommandTimeout", "3610");//设置Appium超时时间

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
		
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws Exception {
		ClearFlow(driver);
		testLoginFlow(driver);	
	}

	private void ClearFlow(AndroidDriver driver) {
		CommonUtil.sleep(5000);
		Safe360Record.Clear360FlowRecord(driver);	
	}

	private void testLoginFlow(AndroidDriver driver) {
		String networkType  = CommonUtil.getNetworkType();//读取手机网络
		for(int i=1;i<=10;i++){
			driver.resetApp();
			CommonUtil.sleep(5000);
			driver.findElement(By.id("com.netease.mail:id/editor_email")).sendKeys(UserName);
			driver.findElement(By.id("com.netease.mail:id/editor_password")).sendKeys(PassWord);
			driver.findElement(By.id("com.netease.mail:id/button_login")).click();
			elementManager.waitForElement(driver, By.id("com.netease.mail:id/btn_enter_mail"), 30);
			driver.findElement(By.id("com.netease.mail:id/btn_enter_mail")).click();
			elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"), 10);
			elementManager.waitForElement(driver, By.name("20"), 60);//收件箱显示有20封未读邮件
			String useflow = Safe360Record.recordFlow(driver,"网易邮箱大师");
			LogManager.testTime("第"+i+"次无缓存登录消耗流量:"+useflow);
			inputToExcel(i,networkType, useflow);
			CommonUtil.sleep(20000);
		}
	}
	// 输入文字到excel表格，不用修改
	public  void inputToExcel(int caseNo,String networkType ,String useflow) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "163loginFlow", caseNo + "\t"+networkType + "\t" +"首次无缓存登录流量消耗："+"\t"+useflow+ "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}