package com.ctc.mail.successratio;

import io.appium.java_client.android.AndroidDriver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.ctc.mail.common.User;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * 登陆成功脚本 
 * 机型：三星Note4
 * @author yang
 *
 */
public class LoginSu139 {
	public AndroidDriver driver;
	// 工程根目录
	String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	String appPackage = "cn.cj.pe"; // 程序的package
	String appActivity = "com.mail139.about.LaunchActivity"; // 程序的Activity
	String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	String screenshotName = "mail139_login"; // 截图的名字前缀


	@Before
	public void setUp() throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");// 你要测试的手机操作系统
		capabilities.setCapability("deviceName", "Android");// 使用的手机类型或模拟器类型，真机时输入Android
		capabilities.setCapability("platformVersion", "4.4.4");// 手机操作系统版本，可不更改
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
	/**
	 * 登陆成功率
	 * @throws Exception
	 */
	public void test() throws Exception {
		try {
			boolean dataEnabled = driver.getNetworkConnection().dataEnabled();
			boolean wifiEnabled = driver.getNetworkConnection().wifiEnabled();
			String networkType = "";
			if((!wifiEnabled) && dataEnabled){
				networkType = "4G";
			}else {
				networkType = "CMCC";
			}
			Components139 components = new Components139();
			if(networkType.equals("4G")){
				components.testFirstLogin4G(driver);
			}else{
				components.testFirstLoginCMCC(driver);
			}
			
			components.quit(driver);
			User user = new User("18718848534","yscs135");
			components.testloginsu(driver,user,networkType,screenshotName,fileDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
