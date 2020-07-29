package com.atguigu.hbase.utils;

import com.atguigu.hbase.util.ConnectionUtil;
import com.atguigu.hbase.util.NameSpaceUtil;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by VULCAN on 2020/6/23
 */
public class NameSpaceUtilTest {

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
    public void listNameSpaces() throws IOException {

        System.out.println(NameSpaceUtil.listNameSpaces(conn));

    }

    @Test
    public void ifNSExists() throws IOException {

        System.out.println(NameSpaceUtil.ifNSExists(conn,"  "));

    }

    @Test
    public void createNameSpace() throws IOException {

        System.out.println(NameSpaceUtil.createNameSpace(conn,"ns1"));

    }

    @Test
    public void dropNameSpace() throws IOException {

        System.out.println(NameSpaceUtil.dropNameSpace(conn,"ns2"));

    }

}