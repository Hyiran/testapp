package com.ctc.mail.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ctc.mail.common.CommonUtil;

import io.appium.java_client.android.AndroidDriver;
import pers.vinken.appiumUtil.elementManager;
/**
 * 测试资源消耗--电量
 * @author xiaoM
 * @version PowerTutor 版本：1.4
 */
public class PowerTutorRecord {
	static int timeout = 60;

	/**
	 * 开启PowerTutor电量监控
	 * @param driver
	 */
	public static void startPowerTutor(AndroidDriver driver){
		CommonUtil.adbClearCache("edu.umich.PowerTutor");
		CommonUtil.adbStartAPP("edu.umich.PowerTutor/edu.umich.PowerTutor.ui.UMLogger");
		elementManager.waitForElement(driver, By.name("Ok"), timeout).click();
		elementManager.waitForElement(driver, By.name("Agree"), timeout).click();
		elementManager.waitForElement(driver, By.id("edu.umich.PowerTutor:id/servicestartbutton"), timeout).click();
		driver.sendKeyEvent(3);
	}

	/**
	 * 查看被监控的应用的电量消耗
	 * @param driver
	 * @param findText	被监控应用名称
	 * @return
	 * @throws Exception
	 */
	public static String recordPower(AndroidDriver driver,String findText)throws Exception{
		String  strpowertotal="";
		String result = "";
		driver.sendKeyEvent(3);
		CommonUtil.adbStartAPP("edu.umich.PowerTutor/edu.umich.PowerTutor.ui.UMLogger");
		elementManager.waitForElement(driver, By.id("edu.umich.PowerTutor:id/appviewerbutton"), timeout).click();
		WebElement e = elementManager.waitForElement(driver, By.xpath("//android.widget.TextView[contains(@text,'"+findText+"')]"), timeout);
		if(e==null){
			driver.scrollTo(findText);
			e=driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'"+findText+"')]"));
		}
		result = e.getAttribute("text");
		int index = result.indexOf("\n");
		strpowertotal = result.substring(index+1, result.length());
		driver.sendKeyEvent(4);
		elementManager.waitForElement(driver, By.id("edu.umich.PowerTutor:id/servicestartbutton"), timeout).click();
		return strpowertotal;
	}
}
