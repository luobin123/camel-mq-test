package com.nswt.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangfengyang
 * @date 2019-06-06.
 */
public class QueryProcessList {
    private static Logger logger = LoggerFactory.getLogger(QueryProcessList.class);
    public static void main(String[] args) throws Exception {
        while(true){
            queryData();
            Thread.sleep(1000);
        }
    }

    public static void queryData() throws ClassNotFoundException, SQLException {
        DbUtil dbUtil = new DbUtil();
        String getid = getid();
        String sql = "insert into db_ods_test.information_schema(mid, name, create_date, create_open) values (" + getid + ", '111', '222' , '111');";
//        String sql = "insert into db_ods_test.information_schema(mid, name, create_date, create_open) values (" + getid + ", '111', '222' , '111');";
//        String sql2 = "select * FROM db_ods_test.information_schema where mid = "+ getid +";";
        String sql2 = "select * from information_schema.`PROCESSLIST` WHERE INFO !='';";
        logger.info(""+ sql);
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet resultSet = null;
        try {
            conn = dbUtil.createConnection();
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();//执行sql语句

            stmt1 = conn.prepareStatement(sql2);
            resultSet = stmt1.executeQuery();
            for(Map data : convertList(resultSet)){
                logger.info(""+data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt1 != null) {
                stmt1.close();
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
                String content = "";
                if (rs.getObject(i) != null) {
                    content = rs.getObject(i).toString().replace("\n", "\\n").replace("\r", "\\r");
                }
                rowData.put(md.getColumnName(i), content);

            }

            list.add(rowData);

        }

        return list;

    }

    private static String getid () {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        return sdf.format(date);
    }
}
