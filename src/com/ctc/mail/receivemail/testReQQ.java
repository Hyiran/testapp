package com.ctc.mail.receivemail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

public class testReQQ {
	// 工程根目录
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String appPackage = "com.tencent.androidqqmail"; // 程序的package
	String appActivity = "com.tencent.qqmail.LaucherActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mailqq.xls"; // 记录文档的文件名
	String screenshotName = "mailqq_login"; // 截图的名字前缀
	
	private AndroidDriver driver;

	String userName = "18718879146@qq.com";
	String passWord = "Deng5375585";

	long endtime = 0;

	WebElement e = null;
	By by = null;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "9TGEQ8EM4DSSQ8GM");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void runTest() {
		int timeout = 30;
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.resetApp();
		sleep(3000);
		// 滑动
		driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
		driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
		driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
		driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
		driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
		driver.findElementByClassName("android.widget.Button").click();
		sleep(1000);
		driver.findElementById("e1").click();
		sleep(1000);
		// 设置用户名密码登录
		driver.findElementById("b9").sendKeys(userName);
		driver.findElementById("bi").sendKeys(passWord);
		e = driver.findElementById("a6");
		e.click();
		e = elementManager.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'完成')]"), timeout);
		e.click();
		elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"), timeout).click();
		System.out.println("开始测试：");
		for (int i = 1; i <= 15; i++) {
			while (!elementManager.doesElementDisplay(driver, By.id("com.tencent.androidqqmail:id/w"))) {
				elementManager.waitForElement(driver, By.id("com.tencent.androidqqmail:id/a3"), timeout).click();
				elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"), timeout).click();
			}
			endtime = System.currentTimeMillis();
			inputToExcel(i);
			System.out.println(endtime);
			sleep(2000);
			TouchAction action = new TouchAction(driver);
			List<WebElement> lst = driver.findElementsByClassName("android.widget.RelativeLayout");
			sleep(3000);
			action.longPress(lst.get(3)).perform();
			elementManager.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'删除')]"), timeout).click(); 
			sleep(1000);
			while (elementManager.doesElementDisplay(driver, By.id("com.tencent.androidqqmail:id/w"))) {
				driver.swipe(width /5 , height/ 5, width / 5, height* 4 / 5, 500);
			}
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
			ioManager.writeToExcelFile(fileDir + fileName, "testReQQ", caseNo + "\t" + endtime + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
