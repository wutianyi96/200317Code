package com.atguigu.hbase.utils;

import com.atguigu.hbase.util.ConnectionUtil;
import com.atguigu.hbase.util.TableUtil;
import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by VULCAN on 2020/6/23
 */
public class TableUtilTest {

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
    public void createTable() throws IOException {

        System.out.println(TableUtil.createTable(conn,"t11","ns2","cf1","cf2"));

    }


    @Test
    public void ifTableExists() throws IOException {

        System.out.println(TableUtil.ifTableExists(conn,"t11","ns2"));

    }

    @Test
    public void dropTable() throws IOException {

        System.out.println(TableUtil.dropTable(conn,"t11","ns2"));

    }

    @Test
    public void test() throws IOException {

        String str="abc";

        char[] chars = str.toCharArray();

        String result="";

        for (char c : chars) {
            result+= Integer.toHexString(c);
        }

        System.out.println(result);

    }

    @Test
    public void createTableWithPreAssign() throws IOException {

        System.out.println(TableUtil.createTableWithPreAssign(conn,"t13","ns2","cf1"));

    }








}