package com.ctc.mail.timedelay;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.dao.TestSqlHelper;
import com.ctc.mail.receivemail.ReQQ;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

public class TestQQ {
	static String dateFormat = "hh:mm:ss.SSS";
	public  SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DecimalFormat df = new DecimalFormat("0.00");//格式化数值，保留两位小数
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
	String groupId = "1";

	long time = 0;
	long startTime = 0;
	long endTime = 0;
	long loginTime = 0;
	long noReadTime = 0;
	long downloadTime = 0;
	long receiveMailTime =0;
	String nowtime = "";
	

	WebElement e = null;
	By by = null;
	/**
	 * 设置组数属性
	 * @param groupId
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

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
		capabilities.setCapability("newCommandTimeout", "3610");//设置超时时间
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
		int timeout = 30;
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		boolean dataEnabled = driver.getNetworkConnection().dataEnabled();
		boolean wifiEnabled = driver.getNetworkConnection().wifiEnabled();
        String networkType = "";
        String productName = "QQ";
		
		if((!wifiEnabled) && dataEnabled){
			networkType = "4G";
		}else {
			networkType = "CMCC";
		}
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
			//driver.findElementById("e6").click();
			driver.findElementById("ea").click();
			Thread.sleep(1000);
			// 设置用户名密码登录
			//driver.findElementById("bd").sendKeys(userName);
			//driver.findElementById("bn").sendKeys(passWord);
			driver.findElementById("bi").sendKeys(userName);
			driver.findElementById("bs").sendKeys(passWord);

			//e = driver.findElementById("a9");
			e = driver.findElementByXPath("//android.widget.Button[contains(@text,'登录')]");
			startTime = System.currentTimeMillis();
			e.click();
			elementManager.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'完成')]"), timeout)
					.click();
			long endTime = System.currentTimeMillis();
			loginTime = endTime - startTime;
			// inputToExcel(i, "自动登陆", "通过", "", date.format(new Date(time)));
			System.out.println(" 登陆成功 " + loginTime); 
													
			// 发送邮件，
			Thread.sleep(3000);
			elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"),
					timeout);
			List<WebElement> list1 = driver.findElementsByClassName("android.widget.RelativeLayout");
			list1.get(3).click();
			/*driver.findElementById("a_").click();
			driver.findElementById("o7").sendKeys("740662426@qq.com");
			driver.findElementById("pk").sendKeys("TestMail");
			driver.findElementById("pe").sendKeys("123456789012345678901234567890");
			driver.findElementById("oq").click();
			driver.findElementById("gb").click();*/
			driver.findElementById("aa").click();
			driver.findElementById("nf").sendKeys("740662426@qq.com");
			driver.findElementById("px").sendKeys("TestMail");
			driver.findElementById("pr").sendKeys("123456789012345678901234567890");
			driver.findElementById("p3").click();
			driver.findElementById("gh").click();
			//elementManager.waitForElement(driver, By.name("qqmail"), 30).click();
			// 查找文件名为Test.part01.rar的附件上传
			elementManager.checkandClick(driver, By.name("Test.part01.rar"));
			elementManager
					.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'添加到邮件(1)')]"), timeout)
					.click();
			//driver.findElementById("a9").click();
			driver.findElementById("a_").click();
			adbClickElement();
			System.out.println("发送完成");
			Thread.sleep(5000);
			List<WebElement> lst = driver.findElementsByClassName("android.widget.RelativeLayout");
			startTime = System.currentTimeMillis();
			lst.get(3).click();
			elementManager.waitForElement(driver, By.name("123456789012345678901234567890"), timeout);
			endTime = System.currentTimeMillis() - startTime;
			noReadTime = endTime;
			System.out.println("打开未读时长" + " " + noReadTime);
			// 判断是否文件已存在
			String path = "sdcard/Download/Test.part01.rar";
			String fileName = "Test.part01";
			if (CommonUtil.adbFindFile(path, fileName)) {
				CommonUtil.adbDeleteFile(path);
			}
			// 开始附件下载
			driver.swipe(width / 2, height / 2, width / 2, height / 4, 500);
			driver.swipe(width / 2, height / 2, width / 2, height / 4, 500);
			driver.swipe(width / 2, height / 2, width / 2, height / 4, 500);
			sleep(1000);
			driver.swipe(600, 2100, 600, 2100, 1000);
			// CommonUtil.adbClickElement("600","2100");
			elementManager.waitForElement(driver, By.name("保存文件"), 30).click();
			//返回download目录
			elementManager.waitForElement(driver, By.id("um"), 30).click();
			
			long time1 = System.currentTimeMillis();
			//.waitForElement(driver, By.id("a9"), 30).click();
			elementManager.waitForElement(driver, By.id("a_"), timeout).click();
			CommonUtil.adbwaitforfile(path, fileName, 60000);
			long time2 = System.currentTimeMillis();
			downloadTime = time2 - time1;
			System.out.println("附件下载时延：" + downloadTime);

			// 返回删除第一封邮件
			elementManager.waitForElement(driver, By.id("a6"), 30).click();
			TouchAction action = new TouchAction(driver);
			// 获取要长按的元素,然后删除
			/*action.longPress(lst.get(3)).perform();
			elementManager.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'删除')]"), timeout)
					.click();*/
			sleep(3000);
			/*
			 * 接收本域邮件
			 * 
			 */
			
			ReQQ r=new ReQQ();
			WebDriver dir=new ChromeDriver();
			dir.manage().window().maximize();
		    time=r.case1(dir);
		    //客户端进行刷新
		    boolean b=false;
		    b=waitUnReadMail(driver, By.name("收件箱(1)​"), 30000);
		    if(b)
		    {
		    	receiveMailTime =System.currentTimeMillis()-time;
		    	System.out.println(receiveMailTime);
				// 获取要长按的元素,然后删除
		    	action.longPress(lst.get(3)).perform();
		    	elementManager.waitForElement(driver, By.xpath("//android.widget.Button[contains(@text,'删除')]"), timeout)
				.click();
		    }
		    sleep(3000);
		    String[] parameters = { productName, networkType, f.format(new Date()), String.valueOf(((double)loginTime/1000.0)), String.valueOf(((double)receiveMailTime/1000.0)), String.valueOf(((double)noReadTime/1000.0)),null, String.valueOf(((double)downloadTime/1000.0)), groupId};
			TestSqlHelper.sqlInsertDelay(parameters);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	

	
	/**
	 * 等待未读邮件
	 * @param driver
	 * @param by
	 * @param timeoutMillis 超时时间
	 * @return
	 */
	public boolean waitUnReadMail(AndroidDriver driver,By by,long timeoutMillis)
	{
		int	width = driver.manage().window().getSize().width;
		int	height = driver.manage().window().getSize().height;
		boolean isTimeOut = false;
		//超时时间
		long timeout = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < timeout) {
				try {
					if(driver.findElement(by).isDisplayed()){
						return true;
					}
				} catch (Exception e) {
					driver.swipe(width / 5, height / 5, width / 5, height * 4 / 5, 500);
				}
		}
		if(!isTimeOut)
		{
		   System.out.println("wait for \" " + by + " \" error!");
		}
		return isTimeOut;
		
	}

	
	public static void adbClickElement() {
		ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "am", "broadcast ", "-a", "mybroadcast");
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 数据输出

	public void inputToExcel(String nowtime) {
		try {
			System.out.println("QQ邮箱测试完成");
			ioManager.writeToExcelFile(fileDir + fileName, "qq",
					"\t" + nowtime + "\t" + loginTime + "\t" + noReadTime + "\t" + downloadTime + "\t"+receiveMailTime +"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
