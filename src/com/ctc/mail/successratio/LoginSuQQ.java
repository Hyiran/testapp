package com.ctc.mail.successratio;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ctc.mail.common.CommonUtil;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

public class LoginSuQQ {

	static String dateFormat = "hh:mm:ss.SSS";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	// 工程根目录
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String apkDir = projectRootPath + "apk/"; // 安装包的位置
	String apkName = "qqmail_android_5.1.0.apk"; // 安装包名
	String appPackage = "com.tencent.androidqqmail"; // 程序的package
	String appActivity = "com.tencent.qqmail.LaucherActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mailqq.xls"; // 记录文档的文件名
	String screenshotName = "mailqq_login"; // 截图的名字前缀
	private AndroidDriver driver;
	// private static String ADB = "f:/android-sdk-windows/platform-tools/adb";

	String userName = "2725247461";
	String passWord = "xia1314520";

	long startTime = 0;
	long endTime = 0;
	long loginTime = 0;
	long noReadTime = 0;
	long downloadTime = 0;
	String nowtime = "";

	WebElement e = null;
	By by = null;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
															// // Emulator或者手机型号
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("unicodeKeyboard", true);
		capabilities.setCapability("resetKeyboard", true);
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		System.out.print("OK");
	}

	@After
		public void tearDown() throws Exception {
			driver.quit();
		}

	@Test
		public void runTest() {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			nowtime = df.format(new Date());
			for (int i = 0; i < 50; i++) {
			int timeout = 30;
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			// 开始滑动
			driver.resetApp();
			try {
				Thread.sleep(5000);
				System.out.print(" test ");
				// 滑动
				driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
				driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
				driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
				driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);
				driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, 500);

				driver.findElementByClassName("android.widget.Button").click();
				Thread.sleep(1000);
				driver.findElementById("e6").click();
				Thread.sleep(1000);
				// driver.findElementById("bl").click();
				// 设置用户名密码登录
				driver.findElementById("bd").sendKeys(userName);
				driver.findElementById("bn").sendKeys(passWord);

				e = driver.findElementById("a9");
				startTime = System.currentTimeMillis();
				e.click();
				elementManager.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'完成')]"), timeout)
						.click();
				long endTime = System.currentTimeMillis();
				loginTime = endTime - startTime;
				// inputToExcel(i, "自动登陆", "通过", "", date.format(new Date(time)));
				System.out.println(" 登陆成功 " + loginTime); 
			}catch(Exception e){	
	    } 
	 }
   }
}