package com.atguigu.hbase.util;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by VULCAN on 2020/6/23
 */
public class ConnectionUtil {

    // 获取一个Connection对象
    public  static Connection getConn() throws IOException {



        /*
            ConnectionFactory.createConnection()会调用HBaseConfiguration.create()

            HBaseConfiguration.create()，会读取类路径下 hadoop相关的配置文件，以及
            hbase相关的配置文件
         */
        return  ConnectionFactory.createConnection();


    }

    // 关闭connection
    public  static  void close(Connection conn) throws IOException {

        if ( null != conn) conn.close();

    }
}
