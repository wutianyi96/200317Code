package com.atguigu.scala.day08;

/**
 * Created by VULCAN on 2020/7/8
 */
public class SwithTest {

    public static void main(String[] args) {

        int i=10;

        /*
            swith: 开关，切换，转换

            swith： 分支语句，swith在执行时，只能匹配到其中的一个分支！

                     分支穿透！ 需要加
         */
        switch (i){
            default:
                System.out.println("无");
                break;

            case 1 :
                System.out.println(1);
                break;
            case 2 :
                System.out.println(2);
                break;
            case 10 :
                System.out.println(10);
                break;


        }



    }
}
