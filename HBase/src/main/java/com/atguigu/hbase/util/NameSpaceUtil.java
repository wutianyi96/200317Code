package com.atguigu.hbase.util;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VULCAN on 2020/6/23
 *
 *  操作库的工具类
 */
public class NameSpaceUtil {

    private static Logger logger= LoggerFactory.getLogger(NameSpaceUtil.class);

    //创建库
        public static  boolean createNameSpace  (Connection connection,String nsName) throws IOException {

            //检查入参
            if (StringUtils.isBlank(nsName)){
                logger.warn("库名非法！");
                return false;
            }

            //获取一个Admin
            Admin admin = connection.getAdmin();

            try {
                //根据库的描述创建库
                NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(nsName).build();

                //添加库的属性
                //namespaceDescriptor.setConfiguration("属性名","属性值");

                admin.createNamespace(namespaceDescriptor);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {

                //关闭admin
                admin.close();
            }


        }

    //删除库
    //判断库是否存在
        public static  boolean dropNameSpace (Connection connection,String nsName) throws IOException {

            //检查入参
            if (StringUtils.isBlank(nsName)){
                logger.error("库名非法！");
                return false;
            }

            //获取一个Admin
            Admin admin = connection.getAdmin();

            //删除之前，先判断库是否存在，如果存在，就删除
            if (! ifNSExists(connection,nsName)){
                logger.error("当前库不存在！");
                admin.close();
                return false;
            }

            //库中有表，无法删除
            List<String> result = getTableInNameSpace(connection, nsName);

            if (result == null || result.size() != 0){
                admin.close();
                logger.error("当前库为非空库，无法删除！");
                return false;
            }

            admin.deleteNamespace(nsName);

            //关闭admin
            admin.close();

            return  true;

        }

    //查询库
    public static List<String> listNameSpaces(Connection conn) throws IOException {

        ArrayList<String> result = new ArrayList<>();

        //获取一个Admin
        Admin admin = conn.getAdmin();

        //查询所有库
        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();

        for (NamespaceDescriptor namespaceDescriptor : namespaceDescriptors) {

            //将库名加入到集合
            result.add(namespaceDescriptor.getName());

        }

        admin.close();

        return result;

    }

    //判断库是否存在
    public static  boolean ifNSExists(Connection connection,String nsName) throws IOException {

        //检查入参
        if (StringUtils.isBlank(nsName)){
            logger.warn("库名非法！");
            return false;
        }

        //获取一个Admin
        Admin admin = connection.getAdmin();

        //判断库名是否存在
        try {

            admin.getNamespaceDescriptor(nsName);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {

            //关闭admin
            admin.close();
        }


    }


    //查询库中的表
    //判断库是否存在
        public static  List<String> getTableInNameSpace  (Connection connection,String nsName) throws IOException {

            ArrayList<String> result = new ArrayList<>();
            //检查入参
            if (StringUtils.isBlank(nsName)){
                logger.warn("库名非法！");
                return null;
            }

            //获取一个Admin
            Admin admin = connection.getAdmin();

            HTableDescriptor[] hTableDescriptors = admin.listTableDescriptorsByNamespace(nsName);

            for (HTableDescriptor hTableDescriptor : hTableDescriptors) {

                result.add(hTableDescriptor.getTableName().getNameAsString());

            }

            //关闭admin
            admin.close();

            return result;

        }
}
