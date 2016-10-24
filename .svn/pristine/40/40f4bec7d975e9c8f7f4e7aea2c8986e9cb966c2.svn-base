package com.ctc.mail.receivemail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import pers.vinken.appiumUtil.elementManager;
import pers.vinken.appiumUtil.ioManager;

import java.io.File;
import java.io.IOException;

public class Re139 {
	static String projectRootPath = new File(System.getProperty("user.dir")).getPath().concat("/");
	static String userName = "13427665067";// 139用户名
	static String userPwd = "as525100";// 邮箱密码
	static String userName2 = "18588872082@139.com";
	static String title = "testReceive"; // 邮件主题
	static String fileDir = projectRootPath + "log/"; // 记录文档的储存路径
	static String fileName = "mail139.xls"; // 记录文档的文件名

	static long startime = 0;

	public static void main(String[] args) {
		for (int i = 1; i <= 1; i++) {
			Re139 re139 = new Re139();
			WebDriver driver = new ChromeDriver();
			driver.manage().window().maximize();
			re139.testRe139(driver);
			driver.close();
			driver.quit();
			
		}
	}

	public  long testRe139(WebDriver driver) {
		
		driver.get("http://mail.139.com/");
		elementManager.waitForElement(driver, By.name("UserName"), 20).sendKeys(userName);
		elementManager.waitForElement(driver, By.id("txtPass"), 20).sendKeys(userPwd);
		driver.findElement(By.id("loginBtn")).click();
		while (!elementManager.doesElementDisplay(driver, By.xpath("//input[@id='tb_mailSearch']"))) {
		}
		driver.findElement(By.xpath("//a[@id='btn_compose']")).click();
		sleep(3000);
		driver.switchTo().frame(driver.findElement(By.id("compose_preload")));
		
		elementManager.waitForElement(driver, By.xpath("//input[@id='txtSubject']"), 30).sendKeys(title);
		sleep(3000);
		// 输入收件人
		elementManager.waitForElement(driver,
				By.xpath("//*[@id='toContainer']/div/div[2]/div[2]/input"), 30).sendKeys(userName2);;
		sleep(1000);
		// 邮件主题
		
		driver.findElement(By.id("bottomSend")).click();
		elementManager.waitForElement(driver, By.xpath("//h1[@id='snedStatus']"), 15);
		return startime = System.currentTimeMillis();
	}

	// 用于设置等待时间，不用修改
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void inputToExcel() {
		try {
			ioManager.writeToExcelFile(fileDir + fileName, "testRe139", startime + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
