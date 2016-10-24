package com.ctc.mail.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户端执行状态
 * @author yang
 *
 */
public class Status {
	private String pcName;
	private String groupId;
	private String speed139;
	private String data139;
	private String speed163;
	private String data163;
	private String speedqq;
	private String dataqq;
	private String speed189;
	private String data189;
	private boolean isSuccess;
	
	/**
	 * 构造函数
	 */
	public Status(){
		pcName  = "";
		groupId  = "";
		speed139  = "";
		data139  = "";
		speed163  = "";
		data163  = "";
		speedqq  = "";
		dataqq  = "";
		speed189  = "";
		data189  = "";
		isSuccess  = false;
	}
	
	public boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getPcName() {
		return pcName;
	}
	public void setPcName(String pcName) {
		this.pcName = pcName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getSpeed139() {
		return speed139;
	}
	public void setSpeed139(String speed139) {
		this.speed139 = speed139;
	}
	public String getData139() {
		return data139;
	}
	public void setData139(String data139) {
		this.data139 = data139;
	}
	public String getSpeed163() {
		return speed163;
	}
	public void setSpeed163(String speed163) {
		this.speed163 = speed163;
	}
	public String getData163() {
		return data163;
	}
	public void setData163(String data163) {
		this.data163 = data163;
	}
	public String getSpeedqq() {
		return speedqq;
	}
	public void setSpeedqq(String speedqq) {
		this.speedqq = speedqq;
	}
	public String getDataqq() {
		return dataqq;
	}
	public void setDataqq(String dataqq) {
		this.dataqq = dataqq;
	}
	public String getSpeed189() {
		return speed189;
	}
	public void setSpeed189(String speed189) {
		this.speed189 = speed189;
	}
	public String getData189() {
		return data189;
	}
	public void setData189(String data189) {
		this.data189 = data189;
	}
	
	/**
	 * 从哪里开始执行
	 */
	public void showInfo(){
		List<String> strList = new ArrayList<String>();
		String index = "";
		String Name = "";
		strList.add(this.getSpeed139());
		strList.add(this.getData139());
		strList.add(this.getSpeed163());
		strList.add(this.getData163());
		strList.add(this.getSpeedqq());
		strList.add(this.getDataqq());
		strList.add(this.getSpeed189());
		strList.add(this.getData189());
		index = String.valueOf(strList.indexOf("N"));
		switch(index){
		case "0":Name="139邮箱测速";
		         break;
		case "1":Name="139邮箱时延";
		         break;
		case "2":Name="163邮箱测速";
                 break;
		case "3":Name="163邮箱时延";
		         break;
		case "4":Name="QQ邮箱测速";
		         break;
		case "5":Name="QQ邮箱时延";
		         break;
		case "6":Name="189邮箱测速";
		         break;
		case "7":Name="189邮箱时延";
		         break;
	    default : break;
		}
		System.out.println("---------------本次测试从"+Name+"开始!!!----------------");
	}

}
