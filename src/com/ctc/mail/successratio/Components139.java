package com.ctc.mail.successratio;

import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import pers.vinken.appiumUtil.elementManager;

import com.ctc.mail.common.CommonUtil;
import com.ctc.mail.common.Location;
import com.ctc.mail.common.Log;
import com.ctc.mail.common.User;
import com.ctc.mail.dao.TestSqlHelper;
import com.ctc.mail.receivemail.Re139;

public class Components139 {
	public int timeout = 60;//设置超时时间
	public static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
	/**
	 * 首次登陆  可兼容不清除缓存
	 * @param driver 
	 * @param networkType 网络类型：4G、CMCC
	 * @param user 用户对象
	 * @param nocache 是否有缓存 ：true、false
	 */
	public void testFirstLogin(AndroidDriver driver,String netWorkType,User user,boolean noCache) {
		if(noCache){
		  driver.resetApp();
		}
	    if(!netWorkType.equals("4G")){
	    //首次登陆时，点击取消
	    elementManager.waitForElement(driver, By.name("取消"), 10).click();
	    //输入账号和密码
    	elementManager.waitForElement(driver, By.id("cn.cj.pe:id/login_name"), timeout).sendKeys(user.getUser());
    	elementManager.waitForElement(driver, By.id("cn.cj.pe:id/login_password"),timeout).click();
		driver.findElement(By.id("cn.cj.pe:id/login_password")).sendKeys(user.getPassword());
		driver.findElement(By.id("cn.cj.pe:id/login")).click();
	    }
	    
	    //去掉订阅的默认2个选项
	    elementManager.waitForElement(driver, By.id("cn.cj.pe:id/check"), timeout);
	    List<WebElement> list = driver.findElements(By.id("cn.cj.pe:id/check"));
	    list.get(0).click();
	    CommonUtil.sleep(1000);
	    list.get(1).click();
	   
	    //点击开始体验
	    elementManager.waitForElement(driver, By.id("cn.cj.pe:id/submit"), timeout).click();
	    
   }
  /**
   * 首次登陆    默认清除缓存
   * @param driver
   * @param networkType 网络类型：4G、CMCC
   * @param user 用户对象
   */
   public void testFirstLogin(AndroidDriver driver,String networkType,User user)
   {
	   testFirstLogin(driver,networkType,user,true);
   }
   
 
	/**
	 * 4G首次登陆
	 * @param driver
	 */
	public void testFirstLogin4G(AndroidDriver driver)
	{
		testFirstLogin(driver,"4G",null);
	}
	
	/**
	 * CMCC首次登陆
	 * @param driver
	 */
	public void testFirstLoginCMCC(AndroidDriver driver)
	{
		User user = new User("18718879146","18879146");
		testFirstLogin(driver,"CMCC",user);
	}
	
	
	/**
	 * 退出
	 * @param driver
	 */
	public void quit(AndroidDriver driver)
	{
		int	width = driver.manage().window().getSize().width;
		int	height = driver.manage().window().getSize().height;
		//点击【主菜单】
		elementManager.waitForElement(driver, By.id("cn.cj.pe:id/hjl_headicon"),5).click();
		CommonUtil.sleep(1000);
		driver.swipe(width/3, height*(4/5), width/3, height/5, 500);
		//点击【设置】
		elementManager.waitForElement(driver, By.name("设置"), 10).click();
		CommonUtil.sleep(200);
		driver.swipe(width / 3, height - 100, width / 3, height / 2, 200);// 上拉
		CommonUtil.sleep(200);
		driver.swipe(width / 3, height - 100, width / 3, height / 2, 200);// 上拉
		//点击【退出】。
		elementManager.waitForElement(driver,By.id("cn.cj.pe:id/log_off"),5).click();
		// 确认退出
		elementManager.waitForElement(driver,By.id("cn.cj.pe:id/right"),5).click();
		driver.closeApp();
	}
	
	/**
	 * 登陆成功率
	 * @param driver
	 * @param user 用户对象
	 * @param networkType 网络类型：4G、CMCC 
	 * @param screenshotName 截图名称
	 * @param path 保存路径
	 */
	public void testloginsu(AndroidDriver driver,User user,String networkType,String screenshotName,String path) {
		System.out.println("开始进行"+networkType+"网络下登录成功率测试");
		for (int i = 0; i < 100; i++) {
			try {
				driver.launchApp();// 重新启动App
				//输入用户名
				elementManager.waitForElement(driver, By.id("cn.cj.pe:id/login_name"), timeout).sendKeys(user.getUser());
				//输入密码
				elementManager.waitForElement(driver,By.id("cn.cj.pe:id/login_password"),timeout).click();
				driver.findElement(By.id("cn.cj.pe:id/login_password")).sendKeys(user.getPassword());
				//开始计时
				double startTime = System.currentTimeMillis();
				driver.findElement(By.id("cn.cj.pe:id/login")).click();
				WebElement we = elementManager.waitForElement(driver, By.id("cn.cj.pe:id/actionbar_sub_title"), timeout);
				if(we !=null)
				{
				    double endTime = System.currentTimeMillis();
				    double time = endTime - startTime;
					String ipass = "";
					if (time < 30000) {
						ipass = "成功";
					} else {
						ipass = "失败";
					}
					//写入到数据库
					String[] parameters = { "139", networkType, f.format(new Date()), "登陆成功率", String.valueOf(time/1000), ipass,  String.valueOf(i)};
					TestSqlHelper.sqlInsertRate(parameters);
					System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次登陆： " + ipass +","+ time+"ms");
				}else{
					System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次登陆： 失败");
				}
				quit(driver);//退出
			} catch (Exception e) {
				System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次登陆： 登陆异常！");
				//截图
				CommonUtil.screenShot(driver,path,screenshotName,i);
				driver.closeApp();
				driver.launchApp();
			} 
		}
	}
	
	/**
	 * 发送成功率
	 * @param driver
	 * @param networkType 网络类型：4G、CMCC 
	 * @param recipient 收件人
	 * @param path 保存路径
	 * @param screenshotName 截图名称
	 */
	public void testsendsu ( AndroidDriver driver,String networkType,String recipient,String path,String screenshotName) {
		int	width = driver.manage().window().getSize().width;
		int timeout = 30;
		double sendStartTime = 0;
		double sendEndTime = 0;
		double receiveStartTime = 0;
		double receiveEndTime = 0;
		double sendTime = 0 ;
		double receiveTime = 0 ;
		boolean isReceived = false;
		String receiveStatus = "";
		String sendStatus = "";
		
		System.out.println("开始进行"+networkType+"网络下发送和接收邮件成功率测试");
		
		for (int i = 0; i < 100; i++) {
			try {
				//点击写信按钮
				elementManager.waitForElement(driver,By.id("cn.cj.pe:id/actionbar_right_view"),timeout).click();
				elementManager.waitForElement(driver, By.id("cn.cj.pe:id/to_wrapper"), 10).sendKeys(recipient);
				CommonUtil.sleep(3000);
				driver.findElement(By.id("cn.cj.pe:id/subject")).click();
				driver.findElement(By.id("cn.cj.pe:id/subject")).sendKeys("test" +i);
				
				//设定正文长度
				String textField = "123456789012345678901234567890";
				WebElement body = elementManager.waitForElement(driver, By.id("cn.cj.pe:id/editTextField"),timeout);
				body.click();
				body.sendKeys(textField);
				
				//点击附件
				elementManager.waitForElement(driver, By.id("cn.cj.pe:id/add_attachment"),timeout).click();
				elementManager.waitForElement(driver, By.id("cn.cj.pe:id/attach_display_file"), timeout).click();
				elementManager.waitForElement(driver, By.name("本地文件"), timeout).click();
				elementManager.waitForElement(driver, By.name("0"),timeout).click();
				elementManager.waitForElement(driver, By.name("0."), timeout).click();
				elementManager.waitForElement(driver, By.name("test2M.rar"), timeout).click();
				elementManager.checkandClick(driver,By.id("cn.cj.pe:id/check_button"));
				
				//开始计时
				sendStartTime =System.currentTimeMillis();
				//点击发送
				elementManager.waitForElement(driver, By.id("cn.cj.pe:id/txt_send"), 2)
						.click();
				WebElement we =	elementManager.waitForElement(driver, By.id("cn.cj.pe:id/gif_success_img"), 1000);
				
				receiveStartTime = System.currentTimeMillis();
				sendEndTime =System.currentTimeMillis();				
				sendTime = sendEndTime-sendStartTime;
				if(we !=null){	
					
					sendStatus = "成功";
					
					System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次发送邮件： 成功,"+ sendTime+"ms");
				    
					isReceived = waitUnReadMail(driver,By.name("testReceive"),30000);
				    receiveEndTime = System.currentTimeMillis();
				    
					if(isReceived){
						receiveTime = receiveEndTime - receiveStartTime;
					    receiveStatus = "成功";
					    //删除接收邮件
					    elementManager.waitForElement(driver, By.id("android:id/list"), 30);
					    CommonUtil.sleep(1000);
					    driver.swipe(width-10, 400,10 , 400, 500);
					    CommonUtil.sleep(1000);
					}else{
						receiveTime = 0;
						receiveStatus = "失败";
						System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次接收邮件： 失败");
						CommonUtil.screenShot(driver,path,screenshotName,i);
					}
					//接收成功率写入到数据库
					String[] receiveParameters = { "139", networkType, f.format(new Date()), "接收成功率", String.valueOf(receiveTime/1000), receiveStatus,  String.valueOf(i)};
					TestSqlHelper.sqlInsertRate(receiveParameters);
					
				}else{
					sendStatus = "失败";
					
					System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次发送邮件： 失败");
					CommonUtil.screenShot(driver,path,screenshotName,i);	
				}
				
				//发送成功率写入到数据库
				String[] parameters = { "139", networkType, f.format(new Date()), "发送成功率", String.valueOf(sendTime/1000), sendStatus,  String.valueOf(i)};
				TestSqlHelper.sqlInsertRate(parameters);
				
				//点击返回
				elementManager.waitForElement(driver,By.id("cn.cj.pe:id/hjl_headicon"),timeout).click();
				CommonUtil.sleep(1000);
				driver.closeApp();
				CommonUtil.sleep(10000);
				driver.launchApp();
				
			} catch (Exception ex) {
				System.out.println("当前时间："+CommonUtil.currentTime()+" "+"第"+i + "次发送邮件： 失败");
				//截图
				CommonUtil.screenShot(driver,path,screenshotName,i);
				driver.closeApp();
				driver.launchApp();
			}
		}
	}
	
  /**
    * 登陆时延
    * @param driver
    * @param networkType
    * @param user
    * @param i
    * @return
    */
	public double testLoginDelay(AndroidDriver driver,String networkType,User user,int i) {
		User firUser = new User("18718879146","18879146");
		int	width = driver.manage().window().getSize().width;
		int	height = driver.manage().window().getSize().height;
		//如果是4G网络则，首次登陆后退出，换另外一个账号登陆
		if(networkType.equals("4G")){
			// 修改网络设置；0 (什么都没有) 1 (飞行模式) 2 (只有Wifi) 4 (只有数据连接) 6 (开启所有网络)
			NetworkConnectionSetting connection = new NetworkConnectionSetting(1);
			driver.setNetworkConnection(connection);// 开启飞行模式，防止软件自动登录
			driver.resetApp();
			// 等待登陆按钮
			elementManager.waitForElement(driver, By.id("cn.cj.pe:id/login_name"), 10);
			connection = new NetworkConnectionSetting(4);
			driver.setNetworkConnection(connection);
    	}else{
    		driver.resetApp();
    		CommonUtil.sleep(10000);
    		//登陆时，点击取消
    	    watcherForCancel(driver,By.name("取消"));
    	}
		
		// 登录时延
		elementManager.waitForElement(driver, By.id("cn.cj.pe:id/login_name"), 10).sendKeys(user.getUser());
		driver.findElement(By.id("cn.cj.pe:id/login_password")).click();
		driver.findElement(By.id("cn.cj.pe:id/login_password")).sendKeys(user.getPassword());
		driver.findElement(By.id("cn.cj.pe:id/login")).click();
		// 记下登录时间
		double starttime = System.currentTimeMillis();
		double endTime = 0.0;
		double time = 0.0;
		String status = "";
		
		WebElement e = null ;

		 //去掉订阅的默认2个选项
		 e = elementManager.waitForElement(driver, By.id("cn.cj.pe:id/submit"), timeout);
		 // 记下登录结束时间
		 endTime = System.currentTimeMillis();
		 elementManager.waitForElement(driver, By.id("cn.cj.pe:id/check"), timeout);
		 List<WebElement> list = driver.findElements(By.id("cn.cj.pe:id/check"));
		 list.get(0).click();
		 CommonUtil.sleep(1000);
		 list.get(1).click();
		   
		 //点击开始体验
		 elementManager.waitForElement(driver, By.id("cn.cj.pe:id/submit"), timeout).click();
		
		if(e!=null){
			time = endTime - starttime;
			status = "成功";
		}else{
			time = 0;
			status = "失败";
		}
		Log.testTime("登录时延： " + String.valueOf(time));
	    
	    //点击【主菜单】
	  	elementManager.waitForElement(driver, By.id("cn.cj.pe:id/hjl_headicon"),5).click();
	  	CommonUtil.sleep(1000);
	  	driver.swipe(width/3, height*(4/5), width/3, height/5, 500);
	  	//点击【设置】
	  	elementManager.waitForElement(driver, By.name("设置"), 10).click();
	    //设置删除邮件同步到云端
	  	elementManager.waitForElement(driver, By.name("邮件删除不同步服务器"),timeout).click();
	    elementManager.waitForElement(driver, By.id("cn.cj.pe:id/hjl_headicon"),5).click();
	    elementManager.waitForElement(driver, By.name("收件箱"), timeout).click();
		return time;
	}

		/**
		 * 打开未读邮件时延
		 * @param driver
		 * @return
		 */
	public double testUnReadMail(AndroidDriver driver) {
		  // 点击【收件箱】，选择【未读】邮件按钮
		  CommonUtil.sleep(10000);
		  WebElement list =elementManager.waitForElement(driver, By.id("android:id/list"), timeout);
		  List<WebElement> mailList = list.findElements(By.className("android.widget.LinearLayout"));
		  CommonUtil.sleep(3000);
		  // 打开第一封未读邮件
		  mailList.get(0).click();
		   
		  double starttime = System.currentTimeMillis();
		  double time = 0;
		   String status = "";
		   WebElement e= elementManager.waitForElement(driver, By.name("123456789012345678901234567890"),timeout);
		   if(e!=null){
				// 记下结束时间
			    double endTime = System.currentTimeMillis();
				time = endTime - starttime;
				status = "成功";
			}else{
				time = 0;
				status = "失败";
			}
		   Log.testTime("打开未读邮件时延： " + String.valueOf(time));
		   return time;
			
	}
		
	/**
	 * 下载附件
	 * @param driver
	 * @param path 路径
	 * @param filename 文件名 
	 * @return
	 */
	public double testDownLoad(AndroidDriver driver,String path,String fileName){
			int	width = driver.manage().window().getSize().width;
			int	height = driver.manage().window().getSize().height;
			if(CommonUtil.adbFindFile(path,fileName)){
				CommonUtil.adbDeleteFile(path);
			}
			CommonUtil.sleep(3000);
			Location location = CommonUtil.getLocation(1440, 2560, width,
					height, 120, 800);
			CommonUtil.sleep(1000);
			// 点击【下载】
			CommonUtil.adbClickElement(String.valueOf(location.getX()),
					String.valueOf(location.getY()));
			double starttime = System.currentTimeMillis();
			double time = 0;
			String status = "";
			//等待下载
			boolean isexists= CommonUtil.adbwaitforfile(path,fileName,120000);
			if(isexists){
				// 记下结束时间
			    long endTime = System.currentTimeMillis();
				time = endTime - starttime;
				status = "成功";
			}else{
				time = 0;
				status = "失败";
			}
			Log.testTime("下载附件时延： " + String.valueOf(time));
			CommonUtil.sleep(2000);
			elementManager.waitForElement(driver, By.id("cn.cj.pe:id/actionbar_back"),timeout).click();// 返回收件箱列表
		    
			return time;
	}

	/**
	 * 发送邮件时延
	 * @param driver
	 * @param recipient 收件人
	 * @return
	 */
	public double testWriterMail(AndroidDriver driver,String recipient) {
			// 写邮件，点击【写邮件】按钮
			CommonUtil.sleep(1000);
			driver.findElement(By.id("cn.cj.pe:id/actionbar_right_view")).click();
			elementManager.waitForElement(driver, By.id("cn.cj.pe:id/to_wrapper"), 10).sendKeys(recipient);
			CommonUtil.sleep(3000);
			driver.findElement(By.id("cn.cj.pe:id/subject")).click();
			driver.findElement(By.id("cn.cj.pe:id/subject")).sendKeys("test");
			// 设定正文长度
			String textField = "123456789012345678901234567890";
			WebElement body = driver.findElement(By.id("cn.cj.pe:id/editTextField"));
			body.sendKeys(textField);
			// 添加附件
			driver.findElement(By.id("cn.cj.pe:id/add_attachment")).click();
			elementManager.waitForElement(driver, By.id("cn.cj.pe:id/attach_display_file"), 5).click();
			elementManager.waitForElement(driver, By.name("本地文件"), 5).click();
			elementManager.waitForElement(driver, By.name("0"), 5).click();
			elementManager.waitForElement(driver, By.name("0."), 5).click();
			elementManager.waitForElement(driver, By.name("test2M.rar"), 5).click();
			elementManager.checkandClick(driver, By.id("cn.cj.pe:id/check_button"));
			double starttime = System.currentTimeMillis();
			elementManager.waitForElement(driver, By.id("cn.cj.pe:id/txt_send"), 2).click();

			WebElement e = elementManager.waitForElement(driver, By.name("已完成"),1000);
			double time = 0;
			String status = "";
			if(e!=null){
				// 记下结束时间
				double endTime = System.currentTimeMillis();
				time = endTime - starttime;
				status = "成功";
			}else{
				time = 0;
				status = "失败";
			}
			//返回邮件列表
			driver.findElement(By.id("cn.cj.pe:id/hjl_headicon")).click();
			CommonUtil.sleep(5000);
			Log.testTime("发送邮件时延： " + String.valueOf(time));
			return time;
	}
	
	/**
	 * 接收本域邮件
	 * @param driver
	 * @return
	 */
	public double testReceiveMail(AndroidDriver driver){
		int	width = driver.manage().window().getSize().width;
		String status = "";
		Re139 re139 = new Re139();
		WebDriver wd = new ChromeDriver();
		wd.manage().window().maximize();
		//获取开始时间
		double startTime = re139.testRe139(wd);
	    long timeout = 30000;
	    boolean isReceived = waitUnReadMail(driver,By.name("testReceive"),timeout);
	    double endTime = System.currentTimeMillis();
	    double time = 0;
		if(isReceived){
			time = endTime - startTime;
		    status = "成功";
		    //删除接收邮件
		    elementManager.waitForElement(driver, By.id("android:id/list"), 30);
		    CommonUtil.sleep(1000);
		    driver.swipe(width-10, 400,10 , 400, 500);
		    CommonUtil.sleep(1000);
		    elementManager.waitForElement(driver, By.id("cn.cj.pe:id/back_delete"), 30).click();
		    CommonUtil.sleep(1000);
		}else{
			time = 0;
			status = "失败";
		}
		Log.testTime("接收本域邮件时延： " + String.valueOf(time));
		wd.close();
		wd.quit();
	    return time;
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
		
	/**
	 * 点击取消  监听弹出的确认框，点击取消
	 * @param driver
	 * @param by
	 * @return
	 */
	public static WebElement watcherForCancel(AndroidDriver driver,By by){
		 WebElement e =null;
		 try {
			e = driver.findElement(by);
			e.click();
		} catch (Exception e1) {
		}
		 return e;
	}
		
     /**
      * 无缓存登陆
      * @param driver
      * @param networkType 网络类型：4G、CMCC 
      * @param user 用户对象
      * @param i 
      */
	public void testFirstLoginNoCache(AndroidDriver driver,String networkType,User user,int i ) {
			 int noreademailcount=0;
			 String strflowtotal="";	
			testFirstLogin(driver,networkType,user,false);
			 
			 //等待未读文本出现
			 WebElement e=  elementManager.waitForElement(driver, By.id("cn.cj.pe:id/mailAlert"), 120);

			 if(e!=null){
				 noreademailcount = Integer.parseInt(e.getAttribute("text"));
				 //如果有20封未读邮件，则进入360卫士获取流量消耗
				 if(noreademailcount == 20){
					 strflowtotal = recordFlow(driver,"139邮箱",networkType);
					 String currentTime = CommonUtil.currentTime();
					 System.out.println("当前时间："+currentTime+" 消耗流量"+strflowtotal);
					 
					 String[] parameters = { "139", networkType, f.format(new Date()),  strflowtotal.replace("K", "").replace("B", "").replace("M", ""), String.valueOf(i)};
					 TestSqlHelper.sqlInsertLoadFlow(parameters);
				 }else{
					 System.out.println("未读邮件数量不对："+noreademailcount);
				 }
			 }else{
				 System.out.println("没有找到未读邮件文本");
			 }
	}
	
	
   /**
    * 记录360卫士的流量
    * @param driver
    * @param findText 查找文本
    * @param networkType 网络类型：CMCC 4G
    * @return
    */
	public String recordFlow(AndroidDriver driver,String findText,String networkType){
		   String  strflowtotal="";
		   CommonUtil.adbStartAPP("com.qihoo360.mobilesafe/com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
		   CommonUtil.sleep(3000);
		   elementManager.waitForElement(driver, By.name("话费•流量"), timeout).click();
		   elementManager.waitForElement(driver, By.name("软件流量管理"), timeout).click();
		   WebElement e = elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_popbtns"), timeout);
		   List<WebElement> textViews = e.findElements(By.className("android.widget.TextView"));
		   textViews.get(1).click();
		   CommonUtil.sleep(1000);
		   List<WebElement> rowTitle = driver.findElements(By.className("android.widget.TextView"));
		   if(networkType.equals("4G")){
			   rowTitle.get(0).click();
		   } else{
			   rowTitle.get(1).click();
		   }
		   WebElement item139 = elementManager.waitForElement(driver, By.name(findText), 5);
		   if(item139 ==null){
		     driver.scrollTo(findText);
		   }
		   WebElement flowtotal = driver.findElementByXPath("//android.widget.TextView[contains(@text,'"+findText+"')]/following-sibling::android.widget.TextView[1]");
		   strflowtotal = flowtotal.getAttribute("text");
		   elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_back"), timeout).click();
		   elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_setting"), timeout).click();
		   elementManager.waitForElement(driver, By.name("清空流量统计数据"), timeout).click();
		   elementManager.waitForElement(driver, By.name("确定"), timeout).click();
		   //返回到360主界面
		   elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_back"), timeout).click();
		   elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_back"), timeout).click();
		   driver.sendKeyEvent(3);
		   return strflowtotal;
    }
		   
    /**
     * 清除360卫士的流量
     * @param driver
     */
    public void clearFlow(AndroidDriver driver){
	      //点击Home按键
		  driver.sendKeyEvent(3);
		  CommonUtil.adbStartAPP("com.qihoo360.mobilesafe/com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
		  elementManager.waitForElement(driver, By.name("话费•流量"), timeout).click();
		 
		  elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_setting"), timeout).click();
		  elementManager.waitForElement(driver, By.name("清空流量统计数据"), timeout).click();
		  elementManager.waitForElement(driver, By.name("确定"), timeout).click();
		  //返回到360主界面
		  elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_back"), timeout).click();
		  elementManager.waitForElement(driver, By.id("com.qihoo360.mobilesafe:id/common_img_back"), timeout).click();
		  driver.sendKeyEvent(3);
    }
		   
    /**
     * 预置100封邮件，无缓存登陆
     * @param driver
     * @param networkType 网络类型：4G、CMCC
     * @param user 用户对象
     */
    public void testLoadMailNoCache(AndroidDriver driver,String networkType,User user){
		   testFirstLogin(driver,networkType,user,true);
			  //等待收件箱出现
		   elementManager.waitForElement(driver, By.name("收件箱"), 120);
		   CommonUtil.sleep(30000);
    }
    /**
     * 加载一百分邮件
     * @param driver
     * @param networkType 网络类型：4G、CMCC
     * @param user 用户对象
     */
    public void testLoadMail(AndroidDriver driver,String networkType,User user){
        int	width = driver.manage().window().getSize().width;
		   int	height = driver.manage().window().getSize().height;
		   testFirstLogin(driver,networkType,user,false);
			  //等待收件箱出现
		   elementManager.waitForElement(driver, By.name("收件箱"), 120);
		   CommonUtil.sleep(30000);
		   //循环查出邮件直到找到为止
		   while(!elementManager.doesWebElementExist(driver, By.name("已没有更多邮件"))){
			   boolean isDisplay = false;
			   while(!elementManager.doesWebElementExist(driver, By.name("加载更多邮件"))){
				   driver.swipe(width/2, height-100, width/2, 350, 500);
				   if(elementManager.doesWebElementExist(driver, By.name("已没有更多邮件"))){
					   isDisplay = true;
					   break;
				   }
				}
			   if(!isDisplay){
			      elementManager.waitForElement(driver, By.name("加载更多邮件"), 30).click();
			   }else{
				   break;
			   }
		   }
 }
		   
   /**
    * 空刷邮件
    * @param driver
    * @param networkType 网络类型：CMCC 4G
    * @return
    */
    public String testBrushMail(AndroidDriver driver,String networkType){
           int	width = driver.manage().window().getSize().width;
		   int	height = driver.manage().window().getSize().height;
		   String strflowtotal = "";
		   driver.sendKeyEvent(3);
		   elementManager.waitForElement(driver, By.name("139邮箱"), 30).click();
		   elementManager.waitForElement(driver, By.name("收件箱"), 120);
		   //空刷一次
		   driver.swipe(width/2, 350, width/2, height-100, 500);
		   //点击Home按键
		   driver.sendKeyEvent(3);
		   CommonUtil.sleep(10000);
		   strflowtotal = recordFlow(driver,"139邮箱",networkType);
		   return  strflowtotal;
    }
		   
	 /**
	  * 待机1小时流量、电量消耗
	  * @param driver
	  * @param appName 应用程序名称
	  * @param networkType 网络类型：CMCC 4G
	  * @return
	  */
    public List<String> testStandby(AndroidDriver driver,String appName,String networkType){
           int waittime = 1*60*60;
           List<String> strList = new ArrayList<String>();
           CommonUtil.sleep(3000);
		   //点击Home按键
		   driver.sendKeyEvent(3);
		   startPowerTutor(driver);
		   //等待5分钟
		   System.out.println("等待5分钟");
		   CommonUtil.sleep(300000);
		   //清空流量
		   clearFlow(driver);
		   String startPower = recordPower(driver,appName);
		   System.out.println("开始电量"+startPower);
		   System.out.println("等待1小时");
		    //等待1小时
		   CommonUtil.sleep(waittime*1000);
		   String strflowtotal = recordFlow(driver,appName,networkType);
		   strList.add(strflowtotal);
		   strList.add(startPower);
		   return  strList;
    }
    
    /**
     * 获取计算的电量
     * @param start 开始电量
     * @param end   结束电量
     * @return
     */
    public String getPowerTutor(String start,String end){
    	   Double strPowerTutor = 0.0;
	       Double startNumber = getNumber(start);
	       Double endNumber = getNumber(end);
	       strPowerTutor = ((endNumber - startNumber)/1000);
	       return strPowerTutor.toString()+" J"; 
    }
    
    /**
     * 截取字符串中的数值
     * @param str 输入字符串
     * @return
     */
    public Double getNumber (String str){
    	String unit = str.substring(str.indexOf(" ")+1, str.length());
    	Double number = 0.0;
    	switch(unit){
    	case "mJ":
    		number = Double.parseDouble(str.substring(0, str.indexOf(" ")));
    		break;
    	case "J" :
    		number = Double.parseDouble(str.substring(0, str.indexOf(" ")))*1000;
    		break;
    	case "kJ":
    		number = Double.parseDouble(str.substring(0, str.indexOf(" ")))*1000*1000;
    		break;
    	default  :
    		break;
    	}
    	return number;
    }
		   
     /**
      * 待机1小时发送三封邮件流量消耗
      * @param driver
      * @param appName 应用程序名称
      * @param networkType 网络类型：CMCC 4G
      * @return
      */
    public List<String> testThreeMailStandby(AndroidDriver driver,String appName,String networkType){
           int waittime = 1*20*60;
		   CommonUtil.sleep(3000);
		   //点击Home按键
		   driver.sendKeyEvent(3);
		   List<String> strList = new ArrayList<String>();
		   //开启电量软件
		   startPowerTutor(driver);
		   //等待5分钟
		   System.out.println("等待5分钟");
		   CommonUtil.sleep(300000);
		   //清空流量
		   clearFlow(driver);
		   String strPowerTutor = recordPower(driver, appName);
		   //等待1小时
		   System.out.println("等待1小时");
		   for(int i = 0; i< 3; i++){
			   CommonUtil.sleep(waittime*1000);
			   sendMail();  
		   }
		   CommonUtil.sleep(5000);
		   String strflowtotal = recordFlow(driver,appName,networkType);
		   strList.add(strflowtotal);
		   strList.add(strPowerTutor);
		   return  strList;
    } 
    
    /**
     * 发送一封邮件
     */
    public void sendMail()
    {
    	Re139 re139 = new Re139();
		WebDriver wd = new ChromeDriver();
		wd.manage().window().maximize();
		re139.testRe139(wd);	
		wd.close();
		wd.quit();	
    }
		   
	/**
	 * 删除3封待机中发送的未读邮件
	 * @param driver
	 * @param appPackage 包名
	 * @param appActivity 启动路径
	 */
    public void testDeleteMail(AndroidDriver driver,String appPackage,String appActivity){
    	   int	width = driver.manage().window().getSize().width;
		   int	height = driver.manage().window().getSize().height;
		   driver.startActivity(appPackage, appActivity);
		   
		   //点击【主菜单】
		   elementManager.waitForElement(driver, By.id("cn.cj.pe:id/hjl_headicon"),5).click();
		   CommonUtil.sleep(1000);
		   driver.swipe(width/3, height*(4/5), width/3, height/5, 500);
		   //点击【设置】
		   elementManager.waitForElement(driver, By.name("设置"), 10).click();
		   //设置删除邮件同步到云端
		   elementManager.waitForElement(driver, By.name("邮件删除不同步服务器"),timeout).click();
		   elementManager.waitForElement(driver, By.id("cn.cj.pe:id/hjl_headicon"),5).click();
		   elementManager.waitForElement(driver, By.name("收件箱"), timeout).click();
		   
		   CommonUtil.sleep(10000);
		   elementManager.waitForElement(driver, By.id("android:id/list"), timeout);
		   List<WebElement> unReadList = driver.findElements(By.id("cn.cj.pe:id/red_hint"));
		   System.out.println("未读邮件数量："+unReadList.size());
		   for(int i=0;i<unReadList.size();i++){
		    driver.swipe(width-10, 400,10 , 400, 500);
		    CommonUtil.sleep(1000);
		    elementManager.waitForElement(driver, By.id("cn.cj.pe:id/back_delete"), timeout).click();
		    CommonUtil.sleep(1000);
		   }
    }
    
		   
	/**
	 * 记录电量
	 * @param driver
	 * @param findText 查找文本
	 * @return
	 */
    public String recordPower(AndroidDriver driver,String findText){
           String  strpowertotal="";
		   String result = "";
		   //点击Home按键
		   driver.sendKeyEvent(3);
		   //启动PowerTutor
		   CommonUtil.adbStartAPP("edu.umich.PowerTutor/edu.umich.PowerTutor.ui.UMLogger");
		   elementManager.waitForElement(driver, By.id("edu.umich.PowerTutor:id/appviewerbutton"), timeout).click();
		   WebElement e = elementManager.waitForElement(driver,By.xpath("//android.widget.TextView[contains(@text,'"+findText+"')]"),timeout);
		   if(e==null){
			  driver.scrollTo(findText);
		   }
		   result = e.getAttribute("text");
		   int index = result.indexOf("\n");
		   strpowertotal = result.substring(index+1, result.length());
		   //点击返回键
		   driver.sendKeyEvent(AndroidKeyCode.BACK);
		   driver.sendKeyEvent(3);
		   return strpowertotal;
    }
		   
	/**
	 * 启动PowerTutor
	 * @param driver
	 */
    public void startPowerTutor(AndroidDriver driver){
           //点击Home按键
		   driver.sendKeyEvent(3);
		   //启动PowerTutor
		   CommonUtil.adbStartAPP("edu.umich.PowerTutor/edu.umich.PowerTutor.ui.UMLogger");
		   elementManager.waitForElement(driver, By.name("Ok"), 5).click();
		   elementManager.waitForElement(driver, By.name("Agree"), 5).click();
		   elementManager.waitForElement(driver, By.id("edu.umich.PowerTutor:id/servicestartbutton"), 5).click();
		   //点击Home按键
		   driver.sendKeyEvent(3);
    }
    
    /**
     * 
     * @param driver
     * @param findText 元素名称
     */
    public void clearCache(AndroidDriver driver,String findText){
    	//按Home键
		driver.sendKeyEvent(3);
		driver.openNotifications();
		elementManager.waitForElement(driver, By.id("com.android.systemui:id/basic_settings_button"), 30).click();
		driver.scrollTo("应用程序管理器");
		elementManager.waitForElement(driver, By.name("应用程序管理器"), 30).click();
		WebElement e = elementManager.waitForElement(driver, By.name(findText), 2);
		if(e == null){
			driver.scrollTo(findText);
			e = driver.findElement(By.name(findText));
		}
		e.click();
		elementManager.waitForElement(driver, By.name("强制停止"), 30).click();
		elementManager.waitForElement(driver, By.name("确定"), 30).click();
		CommonUtil.sleep(5000);
		elementManager.waitForElement(driver, By.name("清除数据"), 30).click();
		elementManager.waitForElement(driver, By.name("确定"), 30).click();
		driver.sendKeyEvent(3);
    }
		   
	
}
