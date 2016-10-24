package com.ctc.mail.successratio;

import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;//导入截图包

import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.formatUtil;
import pers.vinken.appiumUtil.ioManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class SendSu163 {
	static String dateFormat = "MM/dd  HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String appPackage = "com.netease.mail"; // 程序的package
	String appActivity = "com.netease.mobimail.activity.LaunchActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mail163.xls"; // 记录文档的文件名
	String screenshotName = "mail163_SendSu"; // 截图的名字前缀
	
	public AndroidDriver driver;
	
	String userName = "maomingtest2";
	String password = "as123456";

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "6.0");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("unicodeKeyboard", true);
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
		SendSu(driver);
	}

	public void SendSu(AndroidDriver driver) throws Exception {
			driver.resetApp();// 重置应用
			Thread.sleep(3000);
			driver.findElement(By.id("com.netease.mail:id/editor_email")).sendKeys(userName);
			driver.findElement(By.id("com.netease.mail:id/editor_password")).sendKeys(password);
			driver.findElement(By.id("com.netease.mail:id/button_login")).click();
			elementManager.waitForElement(driver, By.id("com.netease.mail:id/btn_enter_mail"), 10).click();
			elementManager.waitForElement(driver, By.id("com.netease.mail:id/tv_mail_list_title"), 10);
			// 设置发送次数
			for (int i = 1; i <= 50; i++) {
				sleep(3000);
				driver.findElement(By.id("com.netease.mail:id/iv_mail_list_plus")).click();
				elementManager.checkandClick(driver, By.id("com.netease.mail:id/write_layout"));
				driver.findElement(By.id("com.netease.mail:id/mailcompose_address_input")).sendKeys("maomingtest@163.com");
				driver.findElement(By.id("com.netease.mail:id/mailcompose_subject_textedit")).sendKeys("test"+i);
				// 设定正文长度
				String textField = "";
				for (int x = 0; x < 3; x++) {
					textField += "1234567890";
				}
				WebElement body = driver.findElement(By.id("com.netease.mail:id/mailcompose_content"));
				body.clear();
				body.sendKeys(textField);
				elementManager.checkandClick(driver, By.id("com.netease.mail:id/mailcompose_btn_add_attachment"));
				elementManager.waitForElement(driver, By.id("com.netease.mail:id/vertical_file"), 5).click();
				if (elementManager.waitForElement(driver, By.name("0."), 2) != null) {
					elementManager.checkandClick(driver, By.name("0."));
					elementManager.checkandClick(driver, By.name("test2M.rar"));
				} else {
					elementManager.checkandClick(driver, By.name("test2M.rar"));
				}
				WebElement send = driver.findElement(By.id("com.netease.mail:id/tv_done"));
				send.click();// 发送
				if (waitForElement(driver, By.name("邮件发送失败"), 10) == null) {
					System.out.println("第" + i + "次" + "发送邮件" + " 成功");
					inputToExcel(i, "发送邮件成功");
				} else {
					screenShot(i);
					elementManager.checkandClick(driver, By.id("com.netease.mail:id/alert_dialog_btnOK"));
					System.out.println("第" + i + "次" + " 发送邮件失败 ");
					inputToExcel(i, "发送邮件失败");
					driver.closeApp();
					driver.launchApp();
					continue;
				}
			}
	}

	// 等待元素超时
	public static WebElement waitForElement(AndroidDriver driver, By by, int timeOut) {
		try {
			return new WebDriverWait(driver, timeOut).until(new ExpectedCondition<WebElement>() {
				@Override
				public WebElement apply(WebDriver wd) {
					return wd.findElement(by);
				}
			});
		} catch (Exception e) {
			return null;
		}
	}

	// 数据输入到表格
	public static void inputToExcel(int caseNo, String caseName) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "SendSu163CMCC", caseNo + "\t" + caseName + "\t");
		} catch (IOException e) {
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
	//截图
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