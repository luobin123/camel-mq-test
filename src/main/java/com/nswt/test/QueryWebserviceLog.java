package com.nswt.test;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangfengyang
 * @date 2019-06-06.
 */
public class QueryWebserviceLog {
    private static Logger logger = LoggerFactory.getLogger(QueryWebserviceLog.class);
    public static void main(String[] args) throws Exception {
        while(true){
            queryData();
            Thread.sleep(1000);
        }
    }

    public static void queryData() throws ClassNotFoundException, SQLException {
        DbUtil dbUtil = new DbUtil();
        String sql = "select * from db_ods.f_mg_serviceloginfo a " +
                "where user_id = 'flifo001' and PROCESS_START_TIME > ?" +
                "order by PROCESS_START_TIME desc";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            conn = dbUtil.createConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis() - 1000*60*3));
            resultSet = stmt.executeQuery();
            for(Map data : convertList(resultSet)){
                logger.info(""+data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static List<Map> convertList(ResultSet rs) throws SQLException {

        List list = new ArrayList();

        ResultSetMetaData md = rs.getMetaData();

        int columnCount = md.getColumnCount();

        while (rs.next()) {

            Map rowData = new LinkedHashMap();

            for (int i = 1; i <= columnCount; i++) {

                rowData.put(md.getColumnName(i), rs.getObject(i));

            }

            list.add(rowData);

        }

        return list;

    }
}
