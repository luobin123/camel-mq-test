package com.nswt.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

/**
 * 从空表向航班信息表中插入数据
 * @author zhangfengyang
 *
 */
public class InsertFlightInfo {
	private Logger logger = LoggerFactory.getLogger(InsertFlightInfo.class);

	public void insertData(){
		new Thread(new InsertFlightInfoThread("f_op_flt_flight_info")).start();
		new Thread(new InsertFlightInfoThread("f_op_flt_flight_info_sub")).start();
		new Thread(new InsertFlightInfoThread("f_op_flt_flight_info_sub_change")).start();
		new Thread(new InsertFlightInfoThread("f_op_flt_flight_info_change")).start();
		new Thread(new InsertFlightInfoThread("f_cm_flight_id_relationships")).start();
	}
	public static void main(String[] args) {
		new InsertFlightInfo().insertData();
	}
	
	class InsertFlightInfoThread implements Runnable{
		private String tableName;
		
		public InsertFlightInfoThread(String tableName) {
			this.tableName = tableName;
		}

		@Override
		public void run() {
			Stopwatch stopwatch = Stopwatch.createStarted();
			logger.info("{}表数据插入开始", tableName);
			String tmpTableName = "tmp_" + tableName;
			String sql = "insert into "	+ tableName
						+ " select * from " + tmpTableName;
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = JDBCUtil.getConn();
				stmt = conn.prepareStatement(sql);
				int rowCount = stmt.executeUpdate();
				logger.info("{}表数据插入完成，总条数：{}，执行时间: {}ms",
						tableName, rowCount, stopwatch.elapsed(TimeUnit.MILLISECONDS));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.closeConnection(conn);
				JDBCUtil.closeStatement(stmt);
			}
		}
		
	}
}
