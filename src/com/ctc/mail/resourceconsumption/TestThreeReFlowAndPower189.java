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

public class TestThreeReFlowAndPower189 {
	static String dateFormat = "MM/dd  HH:mm:ss";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String appPackage = "com.corp21cn.mail189"; // 程序的package
	static String appActivity = "com.corp21cn.mailapp.activity.ClientIntroducePage"; // 程序的Activity
	static String fileName = "Rescource.xls"; // 记录文档的文件名

	public AndroidDriver driver;

	String UserName = "13631354095";
	String PassWord = "as123456";
	int network = 1;//输入1为2G/3G/4G,输入2为WiFi&CMCC

	long time1 = 0;
	long time2 = 0;
	String startime;

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
		testThreeReFlow(driver);
	}

	private void testThreeReFlow(AndroidDriver driver) throws Exception {
		System.out.println("待机1小时189资源消耗测试：");
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
		CommonUtil.sleep(300000);
		Safe360Record.Clear360FlowRecord(driver);
		PowerTutorRecord.startPowerTutor(driver);
		System.out.println("待机一小时");
		System.out.println("接收三封未读邮件");
		for (int i = 1; i <= 3; i++) {
			WebDriver wd = new ChromeDriver();
			wd.manage().window().maximize();
			testsend(wd);
			wd.close();
			wd.quit();
			CommonUtil.sleep(1200*1000);
		}
		String usepower = PowerTutorRecord.recordPower(driver, "189邮箱");
		LogManager.testTime("待机1小时接收三封新邮件电量流量:"+usepower);
		inputToExcel(1,usepower);//输入数据到表格，电量
		String useflow = Safe360Record.recordFlow(driver,"189邮箱");	
		LogManager.testTime("待机1小时接收三封新邮件消耗流量:"+useflow);
		inputToExcel(useflow);//输入数据到表格，流量
	}
	private void testsend(WebDriver driver) {
		driver.get("http://mail.189.cn/");
		WebElement iframeLogin =elementManager.waitForElement(driver,By.xpath("//*[@id='iframeLogin']"), 60);
		driver.switchTo().frame(iframeLogin);
		WebElement userField = elementManager.waitForElement(driver,By.xpath("//*[@name='userName']"), 60);
		userField.clear();
		userField.sendKeys(UserName);
		elementManager.waitForElement(driver, By.xpath("//*[@id='password']"), 60).sendKeys(PassWord);
		driver.findElement(By.xpath("//*[@id='b-logon']")).click();
		elementManager.waitForElement(driver,By.xpath("//*[@id='search-input']"), 60);
		elementManager.waitForElement(driver, By.xpath("//*[@id='j_mf_pannel_mail']/div[2]/div/div[1]/div/a"), 60).click();
		elementManager.waitForElement(driver, By.xpath("//*[@id='xhe0_iframe']"), 60);
		driver.findElement(By.xpath("//*[@id='J_wm_to_scrollbar']/div/input")).sendKeys(UserName+"@189.cn");
		driver.findElement(By.xpath("//*[@id='J_wm_subject']")).clear();
		driver.findElement(By.xpath("//*[@id='J_wm_subject']")).sendKeys("test");
		driver.findElement(By.xpath("//*[@id='J_wm_post_mail']")).click();
		elementManager.waitForElement(driver, By.xpath("//div[@class='secuess']"), 60);
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

