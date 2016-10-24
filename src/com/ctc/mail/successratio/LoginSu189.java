package com.ctc.mail.successratio;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import com.ctc.mail.common.CommonUtil;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

public class LoginSu189 {
	static String dateFormat = "hh:mm:ss.SSS";
	static SimpleDateFormat date = new SimpleDateFormat(dateFormat);
	// 工程根目录
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String appPackage = "com.corp21cn.mail189"; // 程序的package
	static String appActivity = "com.corp21cn.mailapp.activity.ClientIntroducePage"; // 程序的Activity
	static String fileName = "mail189.xls"; // 记录文档的文件名
	String screenshotName = "mail189_LoginSu"; // 截图的名字前缀

	public AndroidDriver driver;

	long time1 = 0;
	long time2 = 0;
	long loginTime = 0;

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

	private void loginSu(AndroidDriver driver) {
		for(int i=28;i<=50;i++){
			driver.resetApp();
			elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/mailsetselect_item_tv"), 60).click();
			CommonUtil.sleep(3000);
			if(elementManager.doesElementDisplay(driver, By.id("com.corp21cn.mail189:id/switch_account"))){
				elementManager.checkandClick(driver, By.id("com.corp21cn.mail189:id/switch_account"));
			}
			elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/mail_setuser"), 2).sendKeys("13631354095");
			elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/mail_setpwd"),2).sendKeys("as123456");
			WebElement login = driver.findElement(By.id("com.corp21cn.mail189:id/mail_setok"));
			time1 = System.currentTimeMillis();
			login.click();
			if (elementManager.waitForElement(driver, By.id("com.corp21cn.mail189:id/navigation_message"), 30) != null) {
				time2 = System.currentTimeMillis();
				loginTime = time2 - time1;
				if(loginTime<30000){
					inputToExcel( i, "首次登录成功", "耗时:" + loginTime);
					System.out.println(i + "首次登录成功 " + loginTime);
				}else{
					inputToExcel( i, "首次登录失败", "耗时:" + loginTime);
					System.out.println(i + " 首次登录失败 " + loginTime);
				}	
			} else {
				inputToExcel(i, "首次登录失败", "耗时:" + "");
				System.out.println(i + " 首次登录失败 ");
			}
		}	
	}

	// 数据输入到表格
	public  void inputToExcel(int caseNo, String caseName, String ifPass) {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "LoginSuCMCC",
					caseNo + "\t" + caseName + "\t" + ifPass + "\t" + loginTime + "\n");
			// StringBufferDemo(fileName ,"\t"+"\t"+"数据插入异常"+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
