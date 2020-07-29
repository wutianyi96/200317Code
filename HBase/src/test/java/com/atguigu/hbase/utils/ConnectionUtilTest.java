package com.atguigu.hbase.utils;

import com.atguigu.hbase.util.ConnectionUtil;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by VULCAN on 2020/6/23
 *
 * Error:java: 读取D:\dev\apache-maven-3.5.3\repository\org\apache\HBase\thirdparty\hbase-shaded-netty\2.2.1\hbase-shaded-netty-2.2.1.jar时出错; invalid LOC header (bad signature)
 */
public class ConnectionUtilTest {

    @org.junit.Test
    public void getConn() throws IOException {

        System.out.println(ConnectionUtil.getConn());

    }
}