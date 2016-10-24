package com.ctc.mail.resourceconsumption;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ctc.mail.common.CommonUtil;

import io.appium.java_client.android.AndroidDriver;

import pers.vinken.appiumUtil.LogManager;
import pers.vinken.appiumUtil.PowerTutorRecord;
import pers.vinken.appiumUtil.Safe360Record;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

/**
 * 待机中接收邮件推送（长时）流量监控
 * @author xiaoM
 *
 */

public class TestThreeReFlowAndPower163 {
	static String dateFormat = "MM/dd  HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");// 工程根目录
	String appPackage = "edu.umich.PowerTutor"; // 程序的package
	String appActivity = "edu.umich.PowerTutor.ui.UMLogger"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mail163.xls"; // 记录文档的文件名
	String screenshotName = "mail163_test"; // 截图的名字前缀

	static String UserName = "maomingtest5";
	static String PassWord = "as123456";

	public AndroidDriver driver;

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("newCommandTimeout", "6660");//设置Appium超时时间

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws Exception {
		testNoReFlow(driver);	
	}

	private void testNoReFlow(AndroidDriver driver)throws Exception {
		LogManager.testTime("待机1小时163邮箱资源消耗测试：");
		CommonUtil.adbClearCache("com.netease.mail");
		CommonUtil.adbStartAPP("com.netease.mail/com.netease.mobimail.activity.LaunchActivity");
		CommonUtil.sleep(5000);
		driver.findElement(By.id("com.netease.mail:id/editor_email")).sendKeys(UserName);
		driver.findElement(By.id("com.netease.mail:id/editor_password")).sendKeys(PassWord);
		driver.findElement(By.id("com.netease.mail:id/button_login")).click();
		elementManager.waitForElement(driver, By.id("com.netease.mail:id/btn_enter_mail"), 30);
		driver.findElement(By.id("com.netease.mail:id/btn_enter_mail")).click();
		elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"), 10);
		CommonUtil.sleep(20000);
		driver.sendKeyEvent(3);
		LogManager.testTime("后台静止5分钟，待软件稳定");
		CommonUtil.sleep(300000);
		Safe360Record.Clear360FlowRecord(driver);
		PowerTutorRecord.startPowerTutor(driver);
		LogManager.testTime("待机一小时");
		LogManager.testTime("每隔20分钟接收1封未读邮件：");
		for (int i = 1; i <= 3; i++) {
			LogManager.testTime("第"+i+"次发送邮件");
			WebDriver wd = new ChromeDriver();
			wd.manage().window().maximize();
			testsend(wd);	
			wd.close();
			wd.quit();	
			CommonUtil.sleep(1200*1000);
		}
		String usepower = PowerTutorRecord.recordPower(driver, "网易邮箱大师");
		LogManager.testTime("待机1小时接收三封新邮件电量流量:"+usepower);
		inputToExcel(1,usepower);//输入数据到表格，电量
		String useflow = Safe360Record.recordFlow(driver,"网易邮箱大师");	
		LogManager.testTime("待机1小时接收三封新邮件消耗流量:"+useflow);
		inputToExcel(useflow);//输入数据到表格，流量
	}

	public static void testsend(WebDriver driver) {
		driver.get("http://mail.163.com/");
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='x-URS-iframe']")));
		elementManager.waitForElement(driver, By.xpath("//input[@class='j-inputtext dlemail']"), 10).sendKeys("maomingtest");
		WebElement txtpass = driver.findElement(By.xpath("//input[@class='j-inputtext dlpwd']"));
		txtpass.clear();// 清空密码框
		txtpass.sendKeys(PassWord);// 输入密码
		elementManager.waitForElement(driver, By.id("dologin"), 20);
		driver.findElement(By.id("dologin")).click();// 点击登录按钮
		driver.switchTo().parentFrame();
		elementManager.waitForElement(driver, By.xpath("//li[@class='js-component-component ra0 mD0']"), 30);
		elementManager.waitForElement(driver, By.xpath("//li[@class='js-component-component ra0 mD0']"), 20).click();
		elementManager.waitForElement(driver, By.xpath("//section[@class='tH0']"), 20);
		// 输入主题
		elementManager.waitForElement(driver,By.xpath("//input[@class='nui-ipt-input' and @maxlength='256']"), 5).sendKeys("我是主题");
		// 输入收件人
		elementManager.waitForElement(driver,By.xpath("//input[@class='nui-editableAddr-ipt']"), 5).sendKeys(UserName+"@163.com");
		CommonUtil.sleep(3000);
		CommonUtil.waitForElement(driver,By.xpath("//div[@role='button' and @tabindex='2' and @class='js-component-button nui-mainBtn nui-btn nui-btn-hasIcon nui-mainBtn-hasIcon  ']"),10).click();
		CommonUtil.waitForElement(driver, By.cssSelector("b[class='nui-ico se0 pv1']"), 100);
	}

	// 输入文字到excel表格，不用修改
	public  void inputToExcel(String useflow) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "ThreeReFlow", "0" + "\t" +"待机1小时无接收新邮件流量消耗："+"\t"+useflow+ "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 输入文字到excel表格，不用修改
	public  void inputToExcel(int i,String usepower) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "ThreeRepower", i + "\t" +"待机1小时无接收新邮件电量消耗："+"\t"+usepower+ "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
