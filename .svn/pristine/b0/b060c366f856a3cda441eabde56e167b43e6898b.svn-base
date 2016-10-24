package com.ctc.mail.dao;

import java.sql.*;

public class JDBCTest {
	public static void main(String[] args) {
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		//String driver = "com.mysql.cj.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://192.168.0.109:3306/139mailapp";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "root12345";
		String name = "";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			String sql = "select * from tb_app_data_201609";
			// 结果集
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				// 选择sname这列数据
				name = rs.getString("productName");
				// 输出结果
				System.out.println(rs.getString("id") + "\t" + name);
			}
			rs.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}