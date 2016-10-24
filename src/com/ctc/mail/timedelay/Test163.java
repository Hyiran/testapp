package com.ctc.mail.timedelay;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;



import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.dao.TestSqlHelper;

import pers.vinken.appiumUtil.LogManager;
import pers.vinken.appiumUtil.NetworkManager;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.formatUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 163app，测试机型：三星note4
 * @author xiaoM
 *
 */

public class Test163 {
	static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DecimalFormat df = new DecimalFormat("0.00");//格式化数值，保留两位小数
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");// 工程根目录
	String appPackage = "com.netease.mail"; // 程序的package
	String appActivity = "com.netease.mobimail.activity.LaunchActivity"; // 程序的Activity
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	String screenshotName = "mail163_test"; // 截图的名字前缀
	String productName = "163";
	String groupId = "1";

	public AndroidDriver driver;

	String UserName = "maomingtest4";
	String PassWord = "as123456";
	String UserName2 = "maomingtest";
	String PassWord2 = "as123456";
	
	String logintime,readtime,downloadtime,receivetime;

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
		capabilities.setCapability("platformVersion", "6.0");// 手机操作系统版本，可不更改
		capabilities.setCapability("appPackage", appPackage);// 你想运行的Android应用的包名
		capabilities.setCapability("appActivity", appActivity);// 你要启动的Android
		capabilities.setCapability("newCommandTimeout", "120");//设置Appium超时时间
		capabilities.setCapability("unicodeKeyboard", true);// 隐藏键盘，以便输入中文
		capabilities.setCapability("resetKeyboard", true);

		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);// 若测试多台手机需为开启多个appium，且修改4725（一台对应一个）
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test() throws Exception {
		String networkType  = CommonUtil.getNetworkType();//读取手机网络
		try {
			testLogin(driver);
			testReadAndDownload(driver);
			testRemail(driver);
			testSendmail(driver);
			String[] parameters = { productName, networkType, date.format(new Date()), logintime, receivetime, readtime,null, downloadtime, groupId};
			TestSqlHelper.sqlInsertDelay(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testLogin(AndroidDriver driver){
		driver.resetApp();
		CommonUtil.sleep(5000);
		driver.findElement(By.id("com.netease.mail:id/editor_email")).sendKeys(UserName);
		driver.findElement(By.id("com.netease.mail:id/editor_password")).sendKeys(PassWord);
		WebElement login = driver.findElement(By.id("com.netease.mail:id/button_login"));
		long logintime1 = System.currentTimeMillis();
		login.click();
		elementManager.waitForElement(driver, By.id("com.netease.mail:id/btn_enter_mail"), 30);
		long logintime2 = System.currentTimeMillis();
		logintime = df.format((double)(logintime2 - logintime1)/1000.0);
		LogManager.testTime("登录时延： " + logintime+"s");
		driver.findElement(By.id("com.netease.mail:id/btn_enter_mail")).click();
		elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'收件箱')]"), 10);
		CommonUtil.sleep(10000);// 等待10s邮件载入
	}

	public void testReadAndDownload(AndroidDriver driver) {		
		List<WebElement> read = driver.findElements(By.id("com.netease.mail:id/mail_list_item_content"));
		long readtime1 = System.currentTimeMillis();
		read.get(0).click();
		CommonUtil.waitForElement(driver, By.id("com.netease.mail:id/fragment_layout"), 60);
		long readtime2 = System.currentTimeMillis();
		readtime = df.format((double)(readtime2 - readtime1)/1000.0);
		LogManager.testTime("打开未读邮件时延： " + readtime+"s");
		CommonUtil.waitForElement(driver, By.id("com.netease.mail:id/tv_count"),20).click();
		CommonUtil.sleep(1000);
		CommonUtil.adbClickElement("660", "1800");
		CommonUtil.waitForElement(driver,By.id("com.netease.mail:id/save"),5).click();
		String path="sdcard/Download/test2M*.rar";
		String fileName = "test2M";
		if(	CommonUtil.adbFindFile(path, fileName)){
			CommonUtil.adbDeleteFile(path);
		}
		CommonUtil.sleep(1000);
		WebElement e1 = CommonUtil.waitForElement(driver,By.id("com.netease.mail:id/tv_done"),5);
		long time1 = System.currentTimeMillis();
		e1.click();
		CommonUtil.adbwaitforfile(path, fileName, 100000);
		long time2 = System.currentTimeMillis();
		downloadtime = df.format((double)(time2 - time1)/1000.0);
		LogManager.testTime("附件下载时延："+downloadtime+"s" );
		elementManager.checkandClick(driver, By.id("com.netease.mail:id/iv_mail_read_back"));// 返回收件箱列表
	}

	public void testRemail(AndroidDriver driver) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		WebDriver wd = new ChromeDriver();
		wd.manage().window().maximize();
		long retime1 = testsend(wd);	
		while (!elementManager.doesElementDisplay(driver, By.id("com.netease.mail:id/mail_list_item_state"))) {
			driver.swipe(width /5 , height/ 5, width / 5, height* 4 / 5, 500);
		}
		long retime2 = System.currentTimeMillis();
		receivetime = df.format((double)(retime2 - retime1)/1000.0);
		LogManager.testTime("接收本域邮件时延：" +receivetime+"s");
		List<WebElement> read = driver.findElements(By.id("com.netease.mail:id/mail_list_item_content"));
		TouchAction action = new TouchAction(driver);
		WebElement el = read.get(0);
		action.longPress(el).perform();
		driver.findElement(By.id("com.netease.mail:id/op_delete")).click();
		wd.close();
		wd.quit();
	}

	public void testSendmail(AndroidDriver driver) {
		driver.findElement(By.id("com.netease.mail:id/iv_mail_list_plus")).click();
		elementManager.checkandClick(driver, By.id("com.netease.mail:id/write_layout"));
		driver.findElement(By.id("com.netease.mail:id/mailcompose_address_input"))
		.sendKeys(UserName2+"@163.com");
		driver.findElement(By.id("com.netease.mail:id/mailcompose_subject_textedit")).sendKeys("test");
		// 设定正文长度
		String textField = "";
		for (int x = 0; x < 3; x++) {
			textField +="1234567890";
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
		driver.findElement(By.id("com.netease.mail:id/tv_done")).click();
		CommonUtil.adbentry();// 发送一个广播给辅助工具接收记录时间
		LogManager.testTime("发送时延,辅助工具有记录");
		CommonUtil.sleep(5000);	
	}		

	public long testsend(WebDriver driver) {
		driver.get("http://mail.163.com/");
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='x-URS-iframe']")));
		elementManager.waitForElement(driver, By.xpath("//input[@class='j-inputtext dlemail']"), 10).sendKeys(UserName2);
		WebElement txtpass = driver.findElement(By.xpath("//input[@class='j-inputtext dlpwd']"));
		txtpass.clear();// 清空密码框
		txtpass.sendKeys(PassWord2);// 输入密码
		elementManager.waitForElement(driver, By.id("dologin"), 20);
		driver.findElement(By.id("dologin")).click();// 点击登录按钮
		driver.switchTo().parentFrame();
		elementManager.waitForElement(driver, By.xpath("//li[@class='js-component-component ra0 mD0']"), 30);
		elementManager.waitForElement(driver, By.xpath("//li[@class='js-component-component ra0 mD0']"), 20).click();
		elementManager.waitForElement(driver, By.xpath("//section[@class='tH0']"), 20);
		// 输入主题
		elementManager.waitForElement(driver,By.xpath("//input[@class='nui-ipt-input' and @maxlength='256']"), 5).sendKeys("竞品测试");
		// 输入收件人
		elementManager.waitForElement(driver,By.xpath("//input[@class='nui-editableAddr-ipt']"), 5).sendKeys(UserName+"@163.com");
		CommonUtil.sleep(1000);
		CommonUtil.waitForElement(driver,By.xpath("//div[@role='button' and @tabindex='2' and @class='js-component-button nui-mainBtn nui-btn nui-btn-hasIcon nui-mainBtn-hasIcon  ']"),10).click();
//		elementManager.waitForElement(driver, By.xpath("//b[@class='nui-ico se0 pv1']"), 100);
		long time = System.currentTimeMillis();
		return time;
	}
	// 屏幕截图
	public  void screenShot() {
		try {
			File screenshot = driver.getScreenshotAs(OutputType.FILE); // 截取当前图片
			FileUtils.copyFile(screenshot, new File(fileDir + screenshotName 
					+ formatUtil.getSystemTime(" - MM.dd-HH.mm.ss") + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
