package com.atguigu.hbase.util;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by VULCAN on 2020/6/24
 */
public class DataUtil {

    //根据表名返回对应的Table对象
    public  static Table getTable(Connection connection,String tableName,String nsName) throws IOException {

        //验证表名，如果合法返回表名
        TableName tn = TableUtil.checkTableName(tableName, nsName);

        //判断是否合法
        if (tn == null){
            return null;
        }

        //返回Table对象
        return  connection.getTable(tn);

    }

    //put
    // put '表名','rowkey','列族:列名', 值
    public static  void put(Connection connection,String tableName,String nsName,String rowkey,String cf,String cn,String value) throws IOException {

        //获取表对象
        Table table = getTable(connection, tableName, nsName);

        //判断是否合法
        if (table == null){
            return ;
        }

        // 创建一个向指定行插入的Put对象
        Put put = new Put(Bytes.toBytes(rowkey));



        // 添加一列
        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));
            //    .addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn),Bytes.toBytes(value));

        //执行put操作
        table.put(put);




        table.close();

    }

    //get  查询当行
    public static Result  get(Connection connection,String tableName,String nsName,String rowkey) throws IOException {

        //获取表对象
        Table table = getTable(connection, tableName, nsName);

        //判断是否合法
        if (table == null){
            return null;
        }

        Get get = new Get(Bytes.toBytes(rowkey));

        Result result = table.get(get);

        table.close();

        return  result;

    }

    // 遍历Result结果集
    public static  void parseResult(Result result){

        if (result !=null){

            Cell[] cells = result.rawCells();

            // Cell:  'rowkey','列族:列名', 值
            for (Cell cell : cells) {
                System.out.println("Row:"+ Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.println("ColumnFamily:"+ Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("ColumnName:"+ Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("Value:"+ Bytes.toString(CellUtil.cloneValue(cell)));

                System.out.println("----------------------------------------");
            }

        }


    }

    //scan
    //get  查询当行
    public static void   scan(Connection connection,String tableName,String nsName) throws IOException {

        //获取表对象
        Table table = getTable(connection, tableName, nsName);

        //判断是否合法
        if (table == null){
            return ;
        }

        //获取扫描器
        Scan scan = new Scan();


        //添加扫描器的属性
        scan.setRaw(true);
        scan.readVersions(10);


        ResultScanner scanner = table.getScanner(scan);

        // 使用扫描器 的 next()方法，去服务端查询一行的结果
        for (Result result : scanner) {
            parseResult(result);
        }

        table.close();

    }

    //delete
    public static  void delete(Connection connection,String tableName,String nsName,String rowkey,String cf,String cn) throws IOException {

        //获取表对象
        Table table = getTable(connection, tableName, nsName);

        //判断是否合法
        if (table == null){
            return ;
        }

        //构建Delete对象
        // 默认是删除整行，为所有列族添加一条 column=列族名:, timestamp=1592969248338, type=DeleteFamily
        Delete delete = new Delete(Bytes.toBytes(rowkey));

        //只删除某个列族
        // 为指定列族添加 cf1:, timestamp=1592969432112, type=DeleteFamily
       // delete.addFamily(Bytes.toBytes(cf));

        // 删除指定列的最新版本
        //  为指定列添加：column=cf1:age, timestamp=1592968506383, type=Delete
        //delete.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cn));

        // 删除指定列的所有版本
        // column=cf1:name, timestamp=1592969787092, type=DeleteColumn
        delete.addColumns(Bytes.toBytes(cf),Bytes.toBytes(cn));

        table.delete(delete);

        table.close();
    }
}
