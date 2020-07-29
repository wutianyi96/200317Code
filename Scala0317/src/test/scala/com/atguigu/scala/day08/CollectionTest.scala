package com.atguigu.scala.day08

/**
  * Created by VULCAN on 2020/7/7
  */

import  org.junit._

class CollectionTest {

  @Test
  def test1() : Unit ={

    val data = List(
      ("zhangsan", "河北", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "鞋"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河南", "衣服"),
      ("wangwu", "河南", "鞋"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "鞋"),
      ("zhangsan", "河北", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "帽子"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河南", "衣服"),
      ("wangwu", "河南", "帽子"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "帽子"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "电脑"),
      ("zhangsan", "河南", "鞋"),
      ("lisi", "河南", "衣服"),
      ("wangwu", "河南", "电脑"),
      ("zhangsan", "河南", "电脑"),
      ("lisi", "河北", "衣服"),
      ("wangwu", "河北", "帽子")
    )

    /*
        格式：(用户名 username，地区 area，品类 category)    sales

        统计： 不同地区，不同品类的购买量排序

        粒度：   一个用户 购买的一条记录

           Map{
                ("河北",List[("鞋子",10 ),("电脑",8) ... ]),
                ("河南",List[("衣服",10 ),("帽子",8) ... ])
              }

            select
                area,category,count(*) salecount
            from sales
            group by area,category
            order by salecount desc

        Scala集合：
            ① ("lisi", "河北", "衣服") -> map  ("河北", "衣服")
            ②   ("河北", "衣服")...  -> groupby  ("河北", "衣服")
                    Map{ (("河北", "衣服"), List( ("河北", "衣服"),("河北", "衣服"),("河北", "衣服") ) ) ,
                    (("河南", "衣服"), List( ("河南", "衣服"),("河南", "衣服"),("河南", "衣服") ) )
                    }

            ③ 将第二步结果 Map  => Map{  (("河北", "衣服"),3)   }

            ④ (("河北", "衣服"),3)  =>  ("河北", ("衣服",3) )

            ⑤ 再次按照地区分组：  Map{  (("河北"),  List[ ("河北", ("衣服",3),("河北", ("帽子",5),("河北", ("电脑",1) ]   )

               }

            ⑥  => Map{  (("河北"),  List[  ("帽子",5), ("衣服",3), ("电脑",1) ]   )


     */
    //   ① ("lisi", "河北", "衣服") -> map  ("河北", "衣服")
    val data1: List[(String, String)] = data.map(v => (v._2, v._3))

    // ②按照省份和品类分组
    val data2: Map[(String, String), List[(String, String)]] = data1.groupBy(v => v)


    //  ③ 将第二步结果 Map  => Map{  (("河北", "衣服"),3)   }
    val data3: Map[(String, String), Int] = data2.map(v => (v._1, v._2.length))

    //  ④ (("河北", "衣服"),3)  =>  ("河北", ("衣服",3) )

    val data4: List[((String, String), Int)] = data3.toList


    val data5: List[(String, (String, Int))] = data4.map(kv => (kv._1._1, (kv._1._2, kv._2)))

    // 按照省份聚合
    val data6: Map[String, List[(String, (String, Int))]] = data5.groupBy(x => x._1)

    val result: Map[String, List[(String, Int)]] = data6.mapValues(value => {

      // (品类，购买数量)
      val l1: List[(String, Int)] = value.map(x => x._2)

      // 排序
      l1.sortBy(x => x._2)(Ordering.Int.reverse)
    })

    println(result)



  }


}
