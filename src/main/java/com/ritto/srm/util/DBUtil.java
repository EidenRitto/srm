package com.ritto.srm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/7/13
 * @Description:
 * @Modified By:
 */
public class DBUtil {
    /**
     * 获取连接
     *
     * @return
     */
    public static Connection getConn() {
        Connection conn = null;
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://192.168.241.111:3306/cashmanager";
            String user = "root";
            String password = "VONEchina20!&";
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    /**
     * 关闭连接
     *
     * @param conn
     */
    public static void closeConn(Connection conn, Statement stm) {
        try {
            if (stm != null) {
                stm.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
