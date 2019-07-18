package com.nswt.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateFlightInfo {
	private Logger logger = LoggerFactory.getLogger(CreateFlightInfo.class);
	
	/**
	 * 查询创建航班
	 */
	public void createFltInfo(){
		logger.info("start");
		DbUtil dbUtil = new DbUtil();
		Connection conn = null;
		PreparedStatement stmt= null;
		ResultSet rs = null;
		try{
			conn = dbUtil.createConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			logger.info("connected.");
			conn.setAutoCommit(false);
			String sql = "select * from tms_bussiness_asm_new where source = 'ods_20180211-13:46:22.asm'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(); 
			logger.info("query tms finish.");
//			List<Map<String, Object>> tmsDataList = new ArrayList<>();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			BlockingQueue<Map<String, Object>> tmsMapQueue = new ArrayBlockingQueue<>(1000);
			Thread insertThread1 = new Thread(new InsertDataThread(tmsMapQueue));
			Thread insertThread2 = new Thread(new InsertDataThread(tmsMapQueue));
			Thread insertThread3 = new Thread(new InsertDataThread(tmsMapQueue));
			Thread insertThread4 = new Thread(new InsertDataThread(tmsMapQueue));
			insertThread1.setName("InsertThread-1");
			insertThread1.start();
			insertThread2.setName("InsertThread-2");
			insertThread2.start();
			insertThread3.setName("InsertThread-3");
			insertThread3.start();
			insertThread4.setName("InsertThread-4");
			insertThread4.start();
			while(rs.next()){
				Map<String, Object> tmsMap = new HashMap<>();
				for(int i = 1; i <= rsMetaData.getColumnCount(); i++){
					tmsMap.put(rsMetaData.getColumnName(i), rs.getObject(i));
				}
				tmsMapQueue.put(tmsMap);
//				tmsDataList.add(tmsMap);
			}
			tmsMapQueue.put(new HashMap<String, Object>());
			tmsMapQueue.put(new HashMap<String, Object>());
			tmsMapQueue.put(new HashMap<String, Object>());
			tmsMapQueue.put(new HashMap<String, Object>());
			logger.info("load tms data.");
		} catch (Exception e) {
			logger.error("error:", e);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		new CreateFlightInfo().createFltInfo();
	}
}


class InsertDataThread implements Runnable{
	private Logger logger = LoggerFactory.getLogger(InsertDataThread.class);
	private BlockingQueue<Map<String, Object>> queue;
	Connection conn = null;
	DbUtil dbUtil = new DbUtil();

	PreparedStatement insertInfoStmt;
	PreparedStatement insertInfoChangeStmt;
	PreparedStatement insertSubStmt;
	PreparedStatement insertSubChangeStmt;
	int counter = 0;
	String insertInfoSql = "  insert into f_op_flt_flight_info ( "
			+ " FLT_ID_PK,  STD,  COMPANY_CODE3,  DEAL_TIME,  COMPANY_CODE2,  FLT_ID,  "
			+ "FLT_DATE,  DEP_APT,  ARR_APT,  FLT_ID_FIX,  GLOBAL_PK,  FLT_DATE_PEK) values("
			+ "?, ?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?, ?, ?)";
	
	String insertInfoChangeSql = "  insert into f_op_flt_flight_info_change ( "
			+ " FLT_ID_PK  ,COMPANY_CODE2  ,FLT_ID  ,FLT_DATE  ,DEP_APT  ,ARR_APT  ,DEAL_TIME, ID )"
			+ " values (?,?,?,?,?,?,?,?)";
	
	String insertSubSql = "insert into f_op_flt_flight_info_sub ( FLT_ID_PK, AC_ID, DEAL_TIME) values(?, ?, ?)";
	
	String insertSubChangeSql = "insert into f_op_flt_flight_info_sub_change (id,  FLT_ID_PK, AC_ID, DEAL_TIME) values(?, ?, ?, ?)";
	
	public InsertDataThread(BlockingQueue<Map<String, Object>> queue) throws ClassNotFoundException, SQLException {
		this.queue = queue;
		conn = dbUtil.createConnection();
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		insertInfoStmt = conn.prepareStatement(insertInfoSql);
		insertInfoChangeStmt = conn.prepareStatement(insertInfoChangeSql);
		insertSubStmt = conn.prepareStatement(insertSubSql);
		insertSubChangeStmt = conn.prepareStatement(insertSubChangeSql);
		conn.setAutoCommit(false);
	}
	@Override
	public void run() {
		long sleepTime = (long) (Math.random()*1000);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try{
			while(true){
				if(queue.size()< 500){
					logger.info("queue size :" + queue.size());
				}
				Map<String, Object> tmsMap = queue.take();
				if(tmsMap.isEmpty()){
					logger.info("commit start:" + counter);
					insertInfoStmt.executeBatch();
					logger.info("info committed.");
					insertInfoChangeStmt.executeBatch();
					logger.info("infochange committed.");
					insertSubStmt.executeBatch();
					logger.info("sub committed.");
					insertSubChangeStmt.executeBatch();
					logger.info("subchange committed.");
					conn.commit();
					logger.info("commit finish:" + counter);
					break;
				}
				
			
			
//			for(Map<String, Object> tmsMap : tmsDataList){
//				String fltMainId = (String) rs.get("flt_main_id");
//				String actionCode = (String) rs.get("action_code");
//				String source = rs.get("source");
//				Date date = rs.getDate("flt_dt");
				
//				String tmsSql = "select * from TMS_BUSSINESS_ASM_"+ actionCode
//						+ " where flt_main_id = ? ";
//				PreparedStatement tmsStmt = conn.prepareStatement(tmsSql);
//				tmsStmt.setString(1, fltMainId);
//				ResultSet tmsRs = tmsStmt.executeQuery();
				
//				while(tmsRs.next()){
//					String ID= tmsMap.get("ID");
//					logger.info(counter + ": query tms id:" + ID);
//					String MID= tmsRs.getString("MID");
//					String RECEIVE_TIME= tmsRs.getString("RECEIVE_TIME");
//					String ACTION_CODE= tmsRs.getString("ACTION_CODE");
//					String MODIFY_REASON= tmsRs.getString("MODIFY_REASON");
//					String IATA_C= tmsRs.getString("IATA_C");
//					String FLT_ID= tmsRs.getString("FLT_ID");
//					String T_ZONE= tmsRs.getString("T_ZONE");
//					String FLT_DATE= tmsRs.getString("FLT_DATE");
//					String DEP_APT= tmsRs.getString("DEP_APT");
//					String ARR_APT= tmsRs.getString("ARR_APT");
//					String OFF_TIME= tmsRs.getString("OFF_TIME");
//					String ON_TIME= tmsRs.getString("ON_TIME");
//					String SERVICE_TYPE= tmsRs.getString("SERVICE_TYPE");
//					String AC_TYPE_MARKET= tmsRs.getString("AC_TYPE_MARKET");
//					String CAB_LAY= tmsRs.getString("CAB_LAY");
//					String AC_ID= tmsRs.getString("AC_ID");
//					String CON_DEP_APT= tmsRs.getString("CON_DEP_APT");
//					String CON_ARR_APT= tmsRs.getString("CON_ARR_APT");
//					String CON_OFF_TIME= tmsRs.getString("CON_OFF_TIME");
//					String CON_ON_TIME= tmsRs.getString("CON_ON_TIME");
//					String CON_FLTNR= tmsRs.getString("CON_FLTNR");
//					String CON_SCH_DEP_DT= tmsRs.getString("CON_SCH_DEP_DT");
//					String CO_SEQ= tmsRs.getString("CO_SEQ");
//					String START_ROW= tmsRs.getString("START_ROW");
//					String MESSAGE_INDEX= tmsRs.getString("MESSAGE_INDEX");
//					String MSG_TYPE= tmsRs.getString("MSG_TYPE");
//					String BASE_VERSION= tmsRs.getString("BASE_VERSION");
//					String VERSION_NO= tmsRs.getString("VERSION_NO");
					
				String companyCode2 = "CA";
				String companyCode3 = "---";
				Timestamp fltDate = new Timestamp(new Date().getTime());
				Timestamp std = new Timestamp(new Date().getTime());
				Timestamp sta = new Timestamp(new Date().getTime());
				String depApt = "PEK";
				String arrApt = "SHA";
				String fltId = "100";
				
				String fltIdPk = UUID.randomUUID().toString().replaceAll("-", "");
//					insertInfoSql = "  insert into f_op_flt_flight_info ( "
//							+ " FLT_ID_PK,  STD,  COMPANY_CODE3,  DEAL_TIME,  COMPANY_CODE2,  FLT_ID,  "
//							+ "FLT_DATE,  DEP_APT,  ARR_APT,  FLT_ID_FIX,  GLOBAL_PK,  FLT_DATE_PEK) values("
//							+ "?, ?, ?, ?, ?, ?, "
//							+ "?, ?, ?, ?, ?, ?)";
				insertInfoStmt.setString(1, fltIdPk);
				insertInfoStmt.setTimestamp(2, std);
				insertInfoStmt.setString(3, companyCode3);
				insertInfoStmt.setTimestamp(4, new Timestamp(new Date().getTime()));
				insertInfoStmt.setTimestamp(4, new Timestamp(new Date().getTime()));
				insertInfoStmt.setString(5, companyCode2);
				insertInfoStmt.setString(6, fltId);
				insertInfoStmt.setTimestamp(7, fltDate);
				insertInfoStmt.setString(8, depApt);
				insertInfoStmt.setString(9, arrApt);
				insertInfoStmt.setString(10, "");
				insertInfoStmt.setString(11, "1234567890");
				insertInfoStmt.setTimestamp(12, fltDate);
				insertInfoStmt.addBatch();
				
//					String insertInfoChangeSql = "  insert into f_op_flt_flight_info_change ( "
//							+ " FLT_ID_PK  ,COMPANY_CODE2  ,FLT_ID  ,FLT_DATE  ,DEP_APT  ,ARR_APT  ,DEAL_TIME, ID )"
//							+ " values (?,?,?,?,?,?,?,?)";
				insertInfoChangeStmt.setString(1, fltIdPk);
				insertInfoChangeStmt.setString(2, companyCode2);
				insertInfoChangeStmt.setString(3, fltId);
				insertInfoChangeStmt.setTimestamp(4, fltDate);
				insertInfoChangeStmt.setString(5, depApt);
				insertInfoChangeStmt.setString(6, arrApt);
				insertInfoChangeStmt.setTimestamp(7, new Timestamp(new Date().getTime()));
				insertInfoChangeStmt.setString(8, UUID.randomUUID().toString().replaceAll("-", ""));
				insertInfoChangeStmt.addBatch();
				
//					String insertSubSql = "insert into f_op_flt_flight_info (FLT_ID_PK, AC_ID, DEAL_TIME) values(?, ?)";
				insertSubStmt.setString(1, fltIdPk);
				insertSubStmt.setString(2, "B8765");
				insertSubStmt.setTimestamp(3, new Timestamp(new Date().getTime()));
				insertSubStmt.addBatch();
				
//					String insertSubChangeSql = "insert into f_op_flt_flight_info_sub_change 
//					(id,  FLT_ID_PK, AC_ID, DEAL_TIME) values(?, ?, ?, ?)";
				insertSubChangeStmt.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
				insertSubChangeStmt.setString(2, fltIdPk);
				insertSubChangeStmt.setString(3, "B8765");
				insertSubChangeStmt.setTimestamp(4, new Timestamp(new Date().getTime()));
				insertSubChangeStmt.addBatch();
				counter++;
				if(counter % 1000 == 0 ){
					logger.info("commit start:" + counter);
					insertInfoStmt.executeBatch();
					logger.info("info committed.");
					insertInfoChangeStmt.executeBatch();
					logger.info("infochange committed.");
					insertSubStmt.executeBatch();
					logger.info("sub committed.");
					insertSubChangeStmt.executeBatch();
					logger.info("subchange committed.");
					conn.commit();
					logger.info("commit finish:" + counter);
				}
			}
			logger.info("finish all");
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			try {
				insertInfoStmt.close();
				insertInfoChangeStmt.close();
				insertSubStmt.close();
				insertSubChangeStmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
