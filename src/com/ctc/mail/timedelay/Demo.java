package com.ctc.mail.timedelay;

import com.ctc.mail.dao.SqlHelper;

public class Demo {
	public static void main(String[]args){
		String[]GX = {"2.02",
				"4.886",
				"2.939",
				"4.576",
				"2.242",
				"3.783",
				"3.6",
				"4.693",
				"6.463",
				"2.275",
				"3.482",
				"3.67",
				"3.474",
				"2.755",
				"3.607"};
		for(int i=0;i<GX.length;i++){
			String sql = "UPDATE tb_app_data_201610 SET sendtime = '"+ GX[i] +"' WHERE networkType = 'CMCC' AND productName = '189' AND groupId =  "+(i+1);
			SqlHelper.executeUpdate(sql, null);
		}
	}
}
