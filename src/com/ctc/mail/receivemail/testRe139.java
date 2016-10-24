package com.ctc.mail.receivemail;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ctc.mail.successratio.Components139;

import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class testRe139 {
	// 工程根目录
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String appPackage = "cn.cj.pe"; // 程序的package
	String appActivity = "com.mail139.about.LaunchActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mail139.xls"; // 记录文档的文件名
	
	public AndroidDriver driver;
	
	long endtime = 0;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws Exception {
		testLogin(driver);
	}

	private void testLogin(AndroidDriver driver) {
		try {
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			Components139 components = new Components139();
			components.testFirstLogin4G(driver);
			sleep(10000);
			System.out.println("开始测试：");
			for (int i = 1; i <= 15; i++) {
				while (!elementManager.doesElementDisplay(driver, By.id("cn.cj.pe:id/red_hint"))) {
					driver.swipe(width / 5, height / 5, width / 5, height * 4 / 5, 500);
				}
				endtime = System.currentTimeMillis();
				System.out.println("第"+i+"次："+endtime);
				inputToExcel(i);
				List<WebElement> read = driver.findElements(By.id("cn.cj.pe:id/front"));
				TouchAction action = new TouchAction(driver);
				
				WebElement el = read.get(0);
				action.longPress(el).perform();
				sleep(1000);
				elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'删除')]"), 5).click();
				sleep(1000);
				
			}
			components.quit(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 用于设置等待时间，不用修改
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 输入文字到excel表格，不用修改
	public void inputToExcel(int caseNo) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "testRe139", caseNo + "\t" + endtime + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
