package com.atguigu.hbase.util;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VULCAN on 2020/6/24
 */
public class TableUtil {

    private static Logger logger= LoggerFactory.getLogger(TableUtil.class);

    // 检查表名是否合法
    public  static TableName checkTableName(String tableName,String nsName){

        //判断表名是否为null
        if (StringUtils.isBlank(tableName)){
            logger.error("请输入表名！");
            return null;
        }

        return TableName.valueOf(nsName,tableName);

    }

    // 判断表是否存在
    public static  boolean ifTableExists(Connection connection,String tableName,String nsName) throws IOException {

        //验证表名是否合法，如果合法，返回表名对应的TableName对象
        TableName tn = checkTableName(tableName, nsName);

        if (tn == null){
            return false;
        }

        //创建Admin
        Admin admin = connection.getAdmin();

        boolean exists = admin.tableExists(tn);

        admin.close();

        return exists;

    }

    // 创建表
    public static  boolean createTable(Connection connection,String tableName,String nsName,String...cfs ) throws IOException {

        //验证表名是否合法，如果合法，返回表名对应的TableName对象
        TableName tn = checkTableName(tableName, nsName);

        if (tn == null){
            return false;
        }
        //是否至少传入一个列族
        if (cfs.length < 1){
            logger.error("请至少指定一个列族！");
            return  false;
        }

        //创建Admin
        Admin admin = connection.getAdmin();

        //需要创建表的TableName对象

        //创建TableDescriptor
        //TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tn).build();

        //TableDescriptorBuilder.ModifyableTableDescriptor tableDescriptor = new TableDescriptorBuilder.ModifyableTableDescriptor(tn);

        // 创建一个存放列族描述的集合
        List<ColumnFamilyDescriptor> cfdess=new ArrayList<>();

        // 描述表，将列族的属性进行声明和添加
        for (String cf : cfs) {

            //列族描述 只读
           ColumnFamilyDescriptor familyDescriptor = ColumnFamilyDescriptorBuilder
                   .newBuilder(Bytes.toBytes(cf))
                   .setMaxVersions(3).setMinVersions(1).build();
           // ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor familyDescriptor = new ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor(Bytes.toBytes(cf));

            //设置最多保留三个版本
            //familyDescriptor.setMaxVersions(3);

            //添加列族的描述
            cfdess.add(familyDescriptor);

        }

        // 基于列族的描述的集合，创建对应的表的描述
        TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tn).setColumnFamilies(cfdess).build();

        // 调用Admin的方法创建表
        admin.createTable(tableDescriptor);

        // 关闭Admin
        admin.close();

        return true;
    }

    // 删除表
    public static  boolean dropTable(Connection connection,String tableName,String nsName) throws IOException {

        //验证表名是否合法，如果合法，返回表名对应的TableName对象
        TableName tn = checkTableName(tableName, nsName);

        if (tn == null){
            return false;
        }

        //创建Admin
        Admin admin = connection.getAdmin();

       //先禁用表
        admin.disableTable(tn);
        //删除表
        admin.deleteTable(tn);

        admin.close();

        return true;


    }


    // 建表+预分区
    // 创建表
    public static  boolean createTableWithPreAssign(Connection connection,String tableName,String nsName,String...cfs ) throws IOException {

        //验证表名是否合法，如果合法，返回表名对应的TableName对象
        TableName tn = checkTableName(tableName, nsName);

        if (tn == null){
            return false;
        }
        //是否至少传入一个列族
        if (cfs.length < 1){
            logger.error("请至少指定一个列族！");
            return  false;
        }

        //创建Admin
        Admin admin = connection.getAdmin();


        // 创建一个存放列族描述的集合
        List<ColumnFamilyDescriptor> cfdess=new ArrayList<>();

        // 描述表，将列族的属性进行声明和添加
        for (String cf : cfs) {

            //列族描述 只读
            ColumnFamilyDescriptor familyDescriptor = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes(cf))
                    .setMaxVersions(3).setMinVersions(1).build();


            //添加列族的描述
            cfdess.add(familyDescriptor);

        }

        // 基于列族的描述的集合，创建对应的表的描述
        TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tn).setColumnFamilies(cfdess).build();

        // 明确startKey和endKey，以及分区个数，让hbase自动帮我们生成中间的分区临界值
        //admin.createTable(tableDescriptor,Bytes.toBytes("aaa"),Bytes.toBytes("zzz"),5);
        byte [][] splitKeys = new byte[4][] ;

        splitKeys[0]=Bytes.toBytes("aaa");
        splitKeys[1]=Bytes.toBytes("bbb");
        splitKeys[2]=Bytes.toBytes("ccc");
        splitKeys[3]=Bytes.toBytes("ddd");

        admin.createTable(tableDescriptor,splitKeys);

        // 关闭Admin
        admin.close();

        return true;
    }
}
