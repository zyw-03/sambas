package com.zyw.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String userName;
    private static String password;
    private static String url;


    private static void init(){
        String config = "db.properties";
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream(config);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driver = properties.getProperty("driver");
        userName = properties.getProperty("username");
        url = properties.getProperty("url");
        password = properties.getProperty("password");

    }

    static {
        init();
    }

    public static Connection getConnection(){
        Connection connection = null;

        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return connection;
    }


    // 处理sql的查询
    public static ResultSet executeQuery(Connection connection, PreparedStatement ps,  String sql, Object[] params) throws SQLException {
        ResultSet rs = null;
        if(connection != null){
            ps = connection.prepareStatement(sql);
            for(int i = 0; i < params.length; ++ i )    ps.setObject(i + 1, params[i]);
            rs = ps.executeQuery();
        }

        return rs;
    }

    // 增删改
    public static int executeUpdate(Connection connection, PreparedStatement ps,  String sql, Object[] params) throws SQLException {
        int flag = 0;

        if(connection != null){
            ps = connection.prepareStatement(sql);
            for(int i = 0; i < params.length; ++ i )    ps.setObject(i + 1, params[i]);
            flag = ps.executeUpdate();
        }

        return flag;

    }


    public static Boolean closeAll(Connection connection, PreparedStatement ps, ResultSet rs){
        Boolean flag = true;

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if(ps != null){
            try {
                ps.close();
                ps = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if(connection != null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        return flag;
    }


}
