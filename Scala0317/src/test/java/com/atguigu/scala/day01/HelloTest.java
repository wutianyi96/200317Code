package com.atguigu.scala.day01;



import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by VULCAN on 2020/6/28
 */
public class HelloTest {



    @org.junit.Test
    public void hello() throws NoSuchFieldException, IllegalAccessException {


        /*
            String 是不可变的 : String中
                    private final char value[] 由final修饰，一旦初始化值(内存地址)不可变

                    但是内容可以变！

         */
        String s ="abcdefg" ;

        s.substring(2);

        System.out.println(s+","+s.hashCode());

        //substring 返回 new String()
       /* s=s.substring(2);

        System.out.println(s+","+s.hashCode());*/

        // 修改 s中 char value[] 的内容

        // 获取String类型中的 value 属性的 Field类型
        Field value = s.getClass().getDeclaredField("value");

        //设置可以访问
        value.setAccessible(true);

        // 获取 String s 变量的 value字段的值
        char [] o = (char [])value.get(s);

        o[2]='z';

        System.out.println(s);

    }
}