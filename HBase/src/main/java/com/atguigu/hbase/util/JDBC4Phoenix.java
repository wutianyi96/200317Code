package com.atguigu.hbase.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by VULCAN on 2020/6/28
 */
public class JDBC4Phoenix {

    public static void main(String[] args) throws Exception {

        // 注册驱动
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");

        //获取Connection
        Connection connection = DriverManager.getConnection("jdbc:phoenix:hadoop102,hadoop103,hadoop104:2181", "atguigu", "");

        // 获取ps
        String sql="select * from US_POPULATION";

        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet resultSet = ps.executeQuery();

        //遍历结果集
        while (resultSet.next()){

            System.out.println(resultSet.getString("STATE")+resultSet.getString("CITY")+resultSet.getLong("POPULATION"));

        }

        //关闭结果集和连接和ps
        resultSet.close();

        ps.close();

        connection.close();

    }
}
