package com.ctc.mail.common;

import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import pers.vinken.appiumUtil.formatUtil;
import pers.vinken.appiumUtil.ioManager;

public class CommonUtil {
//	public static String ADB = "E:/adt-bundle-windows-x64/android-sdk-windows/platform-tools/adb";

	/*
	 * 判断文本是否出现
	 */
	public static boolean findText(AndroidDriver driver, String text) throws InterruptedException {
		boolean flag = false;
		try {
			String xpt = "//*[contains(.,'" + text + "')]";
			WebElement element = driver.findElement(By.xpath(xpt));
			flag = null != element;
		} catch (NoSuchElementException e) {
			Thread.sleep(1);
		}
		return flag;
	}

	/**
	 * @String 设置等待超时时间
	 */
	public static boolean readTimeOut(long timeoutMillis) {
		boolean isTimeOut = true;
		long timeout = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < timeout) {
			try {
				while (System.in.available() > 0) {
					System.in.read();
					isTimeOut = false;
				}
				if (!isTimeOut) {
					break;
				}
			} catch (IOException e) {

			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}
		return isTimeOut;
	}

	/**
	 * 等待元素出现
	 * @param driver
	 * @param by
	 * @param timeout
	 * @return
	 */
	public static WebElement waitForElement(WebDriver driver, By by, long timeout) {
		WebElement e = null;
		WebDriverWait wait = new WebDriverWait(driver, timeout, (long) 0.1);
		try {
			e = wait.until(new ExpectedCondition<WebElement>() {

				@Override
				public WebElement apply(WebDriver arg0) {
					return arg0.findElement(by);
				}

			});
		} catch (TimeoutException es) {

		} catch (NoSuchElementException ew) {

		}
		return e;
	}

	
	/**
	 * 强行点击相对坐标
	 * 
	 * @param e
	 */
	public static void adbClickElement(String x, String y) {
		ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "input", "touchscreen", "tap", x, y);
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * adb命令返回键
	 */
	public static void adbBack() {
		ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "input", "keyevent", "4");
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 写入excel
	 * @param fileUrl
	 * @param sheetName
	 * @param timeList 元素列表
	 * @return
	 * @throws IOException
	 */
	public static boolean writeToExcelFile(String fileUrl, String sheetName, List<RecordTime> timeList)
			throws IOException {
		if (timeList != null) {
			File file = new File(fileUrl);

			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				@SuppressWarnings("resource")
				HSSFWorkbook newWb = new HSSFWorkbook(); // 新建一个工作簿
				FileOutputStream fos = new FileOutputStream(file);
				newWb.write(fos);
				fos.close();
			}

			FileInputStream fis = new FileInputStream(file);

			@SuppressWarnings("resource")
			HSSFWorkbook wb = new HSSFWorkbook(fis); // 新建一个工作簿

			HSSFSheet sheet = wb.getSheet(sheetName); // 获取表格
			// System.out.println("表格：" + sheet);
			if (sheet == null) {
				sheet = wb.createSheet(sheetName); // 新建一个表格
			}

			int numberOfRows = sheet.getLastRowNum() + 1; // 获取已存在数据的行数

			// 将查询出的数据设置到sheet对应的单元格中
			int j = 0;
			int i = 0;
			for (RecordTime t : timeList) {
				HSSFRow row = sheet.createRow(i++ + numberOfRows);// 创建所需的行数
				j = 0;
				HSSFCell cell = null; // 设置单元格的数据类型
				cell = row.createCell(j++, HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(t.getTag()); // 设置单元格的值
				cell = row.createCell(j++, HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(t.getName()); // 设置单元格的值
				cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(t.getTime()); // 设置单元格的值
			}

			try {
				FileOutputStream fos = new FileOutputStream(file);
				wb.write(fos);
				fos.close();
			} catch (IOException e) {
				System.out.println("文件“" + file.getPath() + "”创建失败！");
				e.printStackTrace();
			}
		}
		return true;
	}

	
	/**
	 * 输出手机文件到电脑
	 * 
	 * @String x 需要输出的文件在手机内存卡的位置+需要输出的文件名
	 * 
	 * @String y 输出的文件保存到电脑的位置
	 * 
	 * 
	 */
	public static void adboutput(String x, String y) {
		ProcessBuilder pb1 = new ProcessBuilder("adb", "pull", x, y);
		try {
			System.out.print("正在提取文件... ");
			pb1.start();
			System.out.println("OK!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 *  卸载app
	 * @param x
	 */
	public static void uninstall(String x) {
		try {
			ProcessBuilder pb2 = new ProcessBuilder("adb", "uninstall", x);
			pb2.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	/**
	 * @String x apkDir + apkName 安装包的位置和安装包名
	 */
	public static void install(String x) {
		ProcessBuilder pb = new ProcessBuilder("adb", "install", x);
		try {
			Process p = pb.start();
			String line = null;
			BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf-8"));
			while ((line = bf.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 广播
	 */
	public static void adbentry() {
		ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "am", "broadcast", "-a", "mybroadcast");
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	
	/**
	 * 截图
	 * @param driver
	 * @param path
	 * @param screenshotName
	 * @param i
	 */
	public static void screenShot(AndroidDriver driver, String path,String screenshotName, int i) {
			File screenshot = driver.getScreenshotAs(OutputType.FILE);// 截取当前图片
			try {
				FileUtils.copyFile(
						screenshot,
						new File(path + screenshotName + " - No." + i
								+ formatUtil.getSystemTime(" - MM.dd-HH.mm.ss")
								+ ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

    }
		
	/**
	 * 输入文字到excel表格
	 * @param caseNo
	 * @param path
	 * @param fileName
	 * @param params
	 */
    public static void inputToExcel(int caseNo, String path, String fileName,
				ArrayList<Object> params) {
			try {
				String content =caseNo+ "\t";
				for (int i = 0; i < params.size(); i++) {
					if (i == params.size() - 1) {
						content += params.get(i) + "\n";
					} else {
						content += params.get(i) + "\t";
					}
				}
				ioManager.writeToExcelFile(path, fileName, content);

			} catch (IOException e) {
				e.printStackTrace();
			}
    }
		
	/**
	 * 输入文字到excel表格，不用修改
	 * @param caseNo
	 * @param caseName
	 * @param ifPass
	 * @param starttime
	 * @param endtime
	 * @param time
	 * @param path
	 * @param excleName
	 */
    public static void inputToExcel(int caseNo, String caseName,String ifPass, String starttime,String endtime,long time,String path,String excleName) {
			String content = caseNo + "\t" + caseName +  "\t" + ifPass + "\t"+ starttime + "\t"+ endtime + "\t" + time +"\n";
			try {
				ioManager.writeToExcelFile(path, excleName,content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
    /**
     *  输入文字到excel表格，不用修改
     * @param caseNo
     * @param caseName
     * @param starttime
     * @param logintime
     * @param readtime
     * @param downloadtime
     * @param sendtime
     * @param receivetime
     * @param path
     * @param excleName
     */
    public static void inputToExcel(int caseNo, String caseName,  String starttime,long logintime,long readtime,
				     long downloadtime,long sendtime,long receivetime,String path,String excleName) {
			String content = caseNo + "\t" + caseName + "\t"+ starttime + "\t"+ logintime + "\t"+ readtime + "\t" + downloadtime + "\t" + sendtime+"\t"+receivetime+"\n";
			try {
				  ioManager.writeToExcelFile(path, excleName,content);
				} catch (IOException e) {
				  e.printStackTrace();
				}
    }
		
	/**
	 * 输出当前时间
	 * @return
	 */
    public static String currentTime(){
			Date date =new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return   sdf.format(date);
    }
		
	/**
	 * 获取相对坐标
	 * @param width1
	 * @param height1
	 * @param width2
	 * @param height2
	 * @param x1
	 * @param y1
	 * @return
	 */
    public static Location getLocation(int width1,int height1,int width2,int height2,int x1, int y1){
			double a = (double)width1/(double)width2;
			double b = (double)height1/(double)height2;
			double x2 = ((double)x1)/a;
			double y2 = ((double)y1)/b;
			
			Location Location = new Location((int)x2,(int)y2);
			
			return Location;
    }


	/**
	 * 用于设置等待时间，不用修改
	 * @param millis
	 */
    public static void sleep(long millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    }

	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
    public static boolean adbFindFile(String path) {
			ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "ls", path);
			StringBuffer result = new StringBuffer();
			boolean isexists = false;;
				try {
					Process process =pb1.start();
				    Scanner scanner = new Scanner(process.getInputStream());
					while (scanner.hasNextLine()) {
					 result.append(scanner.nextLine());
					}
					scanner.close();
					if((!result.toString().contains("No such file or directory")) && result.toString().contains(path))
					{
						isexists=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return isexists;
    }
	/**
	 * 文件是否存在
	 * @param path
	 * @param filename
	 * @return
	 */
    public static boolean adbFindFile(String path,String filename) {
			ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "ls", path);
			StringBuffer result = new StringBuffer();
			boolean isexists = false;;
				try {
					Process process =pb1.start();
				    Scanner scanner = new Scanner(process.getInputStream());
					while (scanner.hasNextLine()) {
					 result.append(scanner.nextLine());
					}
					scanner.close();
					if((!result.toString().contains("No such file or directory")) && result.toString().contains(filename))
					{
						isexists=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return isexists;
    }
	
    /**
     * 删除手机中文件
     * @param path
     * @return
     */
    public static boolean adbDeleteFile(String path) {
			ProcessBuilder pb1 = new ProcessBuilder("adb","shell","rm",path);
			StringBuffer result = new StringBuffer();
			boolean isexists = false;;
				try {
					Process process = pb1.start();
				    Scanner scanner = new Scanner(process.getInputStream());
					while (scanner.hasNextLine()) {
					 result.append(scanner.nextLine());
					}
					scanner.close();
					if(!result.toString().contains("No such file or directory"))
					{
						isexists=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return isexists;
    }
		
    /**
     * 判断电脑中文件是否存在
     * @param path
     * @return
     */
    public static boolean isexists(String path){
		boolean issuccess = true;
		try {
			File file = new File(path);
			if(file.exists())
			{
				file.delete();
			}
		} catch (Exception e) {
			
			return false;
		}
		return issuccess;
    }
	
	/**
	 * 等待电脑上本地文件出现timeoutMillis为毫秒
	 * @param path
	 * @param timeoutMillis
	 * @return
	 */
	public static boolean waitforfile(String path, long timeoutMillis){
		boolean isTimeOut = false;
		//超时时间
		long timeout = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < timeout) {
			
				try {
					File file = new File(path);
					if(file.exists()) {
						return isTimeOut = true;
					}
				} catch (Exception e) {
					System.out.println("wait for \" " + path + " \" error!");
					return false;
				}
		}
		return isTimeOut;
    }
	
	/**
	 * 等待下载完成
	 * @param path
	 * @param timeoutMillis
	 * @return
	 */
    public static boolean adbwaitforfile(String path, long timeoutMillis){
		boolean isTimeOut = false;
		//超时时间
		long timeout = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < timeout) {
				try {
					if(adbFindFile(path)) {
						return isTimeOut = true;
					}
				} catch (Exception e) {
					System.out.println("wait for \" " + path + " \" error!");
					return false;
				}
		}
		if(!isTimeOut){
		   System.out.println("wait for \" " + path + " \" error!");
		}
		return isTimeOut;
    }
	
    /**
     * 等待下载完成
     * @param path
     * @param filename
     * @param timeoutMillis
     * @return
     */
    public static boolean adbwaitforfile(String path,String filename, long timeoutMillis)
		{
			boolean isTimeOut = false;
			//超时时间
			long timeout = System.currentTimeMillis() + timeoutMillis;
			while (System.currentTimeMillis() < timeout) {
				
					try {
						if(adbFindFile(path,filename)) {
							return isTimeOut = true;
						}
					} catch (Exception e) {
						System.out.println("wait for \" " + path + " \" error!");
						return false;
					}
			}
			if(!isTimeOut){
			   System.out.println("wait for \" " + path + " \" error!");
			}
			return isTimeOut;
    }
	
    /**
     * 清除缓存
     * @param appPackage
     * @return
     */
    public static boolean adbClearCache(String appPackage) {
			ProcessBuilder pb1 = new ProcessBuilder("adb","shell","pm","clear",appPackage);
			StringBuffer result = new StringBuffer();
			boolean isexists = false;;
				try {
					Process process = pb1.start();
				    Scanner scanner = new Scanner(process.getInputStream());
					while (scanner.hasNextLine()) {
					 result.append(scanner.nextLine());
					}
					scanner.close();
					if(result.toString().contains("Success")){
						isexists=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return isexists;
    }
		
	/**
	 * 获取文件的大小
	 * @param path
	 * @return
	 */
    public static double adbGetFileSize(String path) {
			ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "du", "-k", path);
			StringBuffer result = new StringBuffer();
			String strResult = null;
			Double size = 0.0;
				try {
					Process process =pb1.start();
				    Scanner scanner = new Scanner(process.getInputStream());
					while (scanner.hasNextLine()) {
					 result.append(scanner.nextLine());
					}
					scanner.close();
					int endIndex = result.toString().indexOf("\t");
					strResult = result.toString().substring(0, endIndex);
					size = Double.parseDouble(strResult);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				return size;
    }
		
	/**
	 * 启动应用程序
	 * @param appPackageActivity
	 */
    public static void adbStartAPP(String appPackageActivity){
			ProcessBuilder pb1 = new ProcessBuilder("adb", "shell", "am", "start", "-n",appPackageActivity);
			try {
				pb1.start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    }
    
 
	/**
	 * 获取电脑名
	 * @return
	 */
    public static String getName(){
    	Map<String,String> map = System.getenv(); 
    	String pcName = map.get("COMPUTERNAME");
    	return pcName;
    }
    /**
	 * 判断WiFi是否开启
	 * @return
	 */
	public static boolean adbGetWifi_on() {
		ProcessBuilder pb1 = new ProcessBuilder("adb","shell","settings","get","global","wifi_on");
		StringBuffer result = new StringBuffer();
		boolean wifi_on = false;
		try {
			Process process =pb1.start();
			Scanner scanner = new Scanner(process.getInputStream());
			while (scanner.hasNextLine()) {
				result.append(scanner.nextLine());
			}
			scanner.close();
			if(!result.toString().contains("0")){//0 代表关闭
				wifi_on = true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return wifi_on;
	}
	/**
	 * 读取手机网络
	 * @return
	 */
	public static String getNetworkType(){
		boolean result = adbGetWifi_on();
		String type = "CMCC";
		if(!result){
			type = "4G";
		}
		return type;
	}
			
}
