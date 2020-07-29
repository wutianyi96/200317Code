package com.atguigu.hbase.utils;

import com.atguigu.hbase.util.ConnectionUtil;
import com.atguigu.hbase.util.DataUtil;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by VULCAN on 2020/6/23
 */
public class DataUtilTest {

    private Connection conn;

    @Before
    public void init() throws IOException {
        conn= ConnectionUtil.getConn();
    }

    @After
    public void close() throws IOException {
        ConnectionUtil.close(conn);
    }


    @Test
    public void put() throws IOException {

       DataUtil.put(conn,"t1","ns2","r1","cf1","age","20");

    }

    @Test
    public void Get() throws IOException {

        Result result = DataUtil.get(conn, "t1", "ns2", "r1");

        DataUtil.parseResult(result);

    }

    @Test
    public void Scan() throws IOException {

        DataUtil.scan(conn, "t1", "ns2");



    }

    @Test
    public void Delete() throws IOException {

        // 测试删除一行
        //DataUtil.delete(conn, "t1", "ns2","r1",null,null);

        // 测试删除某一行的一个列族
       // DataUtil.delete(conn, "t1", "ns2","r2","cf1",null);


        // 测试删除某一列的最新版本
        //DataUtil.delete(conn, "t1", "ns2","r3","cf1","age");


        // 测试删除某一列的所有版本
        DataUtil.delete(conn, "t1", "ns2","r4","cf1","name");

    }








}