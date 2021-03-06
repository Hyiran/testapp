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
import pers.vinken.appiumUtil.LogManager;
import pers.vinken.appiumUtil.Safe360Record;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

/**
 * 空刷邮件（无新邮件）流量监控
 * @author xiaoM
 *
 */

public class TestBrushFlow163 {
	static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");// 工程根目录
	String appPackage = "com.netease.mail"; // 程序的package
	String appActivity = "com.netease.mobimail.activity.LaunchActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "Rescource.xls"; // 记录文档的文件名
	String screenshotName = "mail163_test"; // 截图的名字前缀

	String UserName = "maomingtest5";
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
		capabilities.setCapability("newCommandTimeout", "6600");//设置Appium超时时间,单位：s

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws Exception {
		testBrushFlow(driver);	
	}

	private void testBrushFlow(AndroidDriver driver) {
		driver.resetApp();
		CommonUtil.sleep(5000);
		driver.findElement(By.id("com.netease.mail:id/editor_email")).sendKeys(UserName);
		driver.findElement(By.id("com.netease.mail:id/editor_password")).sendKeys(PassWord);
		driver.findElement(By.id("com.netease.mail:id/button_login")).click();
		elementManager.waitForElement(driver, By.id("com.netease.mail:id/btn_enter_mail"), 30);
		driver.findElement(By.id("com.netease.mail:id/btn_enter_mail")).click();
		elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"), 10);
		CommonUtil.sleep(10000);
		driver.sendKeyEvent(3);
		LogManager.testTime("后台静止5分钟，待软件稳定");
		CommonUtil.sleep(60000);
		driver.findElement(By.name("网易邮箱大师")).click();
		Safe360Record.Clear360FlowRecord(driver);
		int	width = driver.manage().window().getSize().width;
		int	height = driver.manage().window().getSize().height;
		String networkType  = CommonUtil.getNetworkType();//读取手机网络
		for(int i=1;i<=10;i++){
			driver.findElement(By.name("网易邮箱大师")).click();
			CommonUtil.sleep(3000);
			if(elementManager.doesElementDisplay(driver, By.id("com.netease.mail:id/bind_skip"))){
				driver.findElement(By.id("com.netease.mail:id/bind_skip")).click();
			}
			elementManager.waitForElement(driver, By.name("收件箱"), 120);
			driver.swipe(width/2, height*1/5, width/2, height*4/5, 500);
			CommonUtil.sleep(30000);
			driver.sendKeyEvent(3);
			String useflow = Safe360Record.recordFlow(driver,"网易邮箱大师");
			LogManager.testTime("第"+i+"次空刷消耗流量:"+useflow);
			inputToExcel(i,networkType, useflow);
		}	
	}

	// 输入文字到excel表格，不用修改
	public  void inputToExcel(int caseNo,String networkType ,String useflow) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "163BrushFlow", caseNo + "\t"+networkType + "\t"+"空刷流量消耗："+"\t"+useflow+ "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}