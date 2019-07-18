package com.nswt.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	String url = "jdbc:mysql://10.9.248.6:4406,10.9.248.7:4406,10.9.248.9:4406/db_ods_test?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;useSSL=false";
	String dbuser = "ods";
	String dbpwd = "ods";
	String driverClass = "com.mysql.jdbc.Driver";
//	String url = "jdbc:mysql://10.9.248.4:3306/db_ods_validata?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;useSSL=false";
//	String dbuser = "ods";
//	String dbpwd = "ods@soc";
//	String driverClass = "com.mysql.jdbc.Driver";
	
	/**
	 * 创建数据库连接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection createConnection() throws ClassNotFoundException, SQLException{
		Class.forName(driverClass);
		Connection conn = DriverManager.getConnection(url, dbuser, dbpwd);
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public void closeConnection(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
