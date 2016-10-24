package com.ctc.mail.successratio;

import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.formatUtil;
import pers.vinken.appiumUtil.ioManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class LoginSu163 {
	static String dateFormat = "MM/dd  HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	// 工程根目录
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String appPackage = "com.netease.mail"; // 程序的package
	String appActivity = "com.netease.mobimail.activity.LaunchActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mail163.xls"; // 记录文档的文件名
	String screenshotName = "mail163_LoginSu"; // 截图的名字前缀

	public AndroidDriver driver;
	
	String UserName = "maomingtest2@163.com";
	String PassWord = "as123456";

	long nowtime = 0;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "6.0");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("unicodeKeyboard", true);// 隐藏键盘，以便输入中文
		capabilities.setCapability("resetKeyboard", true);

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	// 在此处进行测试代码编写
	public void test() throws Exception {
		loginSu(driver);
	}

	public void loginSu(AndroidDriver driver) throws Exception {
		for (int i = 1; i <= 50; i++) {
			driver.resetApp();// 重置app
			elementManager.waitForElement(driver, By.id("com.netease.mail:id/editor_email"), 5).sendKeys(UserName);
			driver.findElement(By.id("com.netease.mail:id/editor_password")).sendKeys(PassWord);
			WebElement login = driver.findElement(By.id("com.netease.mail:id/button_login"));
			long startime = System.currentTimeMillis();
			login.click();
			// 判断是否登录成功
			if (elementManager.waitForElement(driver, By.id("com.netease.mail:id/btn_enter_mail"), 60) != null) {
				long endtime = System.currentTimeMillis();
				nowtime = endtime - startime;
				String ifPass = "";
				if (nowtime > 30000) {
					ifPass = "登录失败";
				} else {
					ifPass = "登录成功";
				}
				inputToExcel(i, "无缓存登录", ifPass);
				System.out.println(i + " " + ifPass + " " +  nowtime);
			} else {
				screenShot(i);
				System.out.println(i + " " + " 登录失败 ");
				inputToExcel(i, "无缓存登录", "失败");
			}
		}
	}

	// 数据输入到表格
	public  void inputToExcel(int caseNo, String caseName, String ifPass) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "LoginSu163CMCC",
					caseNo + "\t" + caseName + "\t" + ifPass + "\t" + nowtime + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 屏幕截图
	public  void screenShot(int i) {
		try {
			File screenshot = driver.getScreenshotAs(OutputType.FILE); // 截取当前图片
			FileUtils.copyFile(screenshot, new File(fileDir + screenshotName + " - No." + i
					+ formatUtil.getSystemTime(" - MM.dd-HH.mm.ss") + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
