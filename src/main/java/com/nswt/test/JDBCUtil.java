package com.nswt.test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Statement;

/**
 * 数据库连接
 * @author Administrator
 *
 */
public class JDBCUtil {
	private static final transient Logger log = Logger.getLogger(JDBCUtil.class);
	static org.slf4j.Logger logger = LoggerFactory.getLogger(JDBCUtil.class);
	public static Connection getConn() {
		InputStream inStream  = ClassLoader.getSystemResourceAsStream("application.properties");
		Properties prop = new Properties();  
		Connection conn = null;
        try {
        	prop.load(inStream);
        	String driver = prop.getProperty("driver");
        	String url = prop.getProperty("url");  
        	String username = prop.getProperty("username");  
        	String password = prop.getProperty("password");
        	logger.info("dbConnection:driver:{}, url:{}, username:{}", driver, url, username);
        	Class.forName(driver); // classLoader,加载对应驱动
        	conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			log.error("connect to db failed:", e);
		}
        return conn;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public static void closeConnection(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭statement
	 */
	public static void closeStatement(Statement stmt){
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭statement
	 */
	public static void closeStatement(PreparedStatement stmt){
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
