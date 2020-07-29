package day04

import scala.io.StdIn

/**
 * Created by VULCAN on 2020/7/1
 */
object ForTest {


  // 统计三个班成绩情况，每个班有3名同学，求出各个班的平均分和所有班级的平均分和及格人数[学生的成绩从键盘输入
  def main(args: Array[String]): Unit = {

    //希望计算的参数
    // 每个班的平均分 = 班级总分 / 班级的人数
    var avgScorePerClass = 0.0
    // 所有人平均分 = 所有人的总分 / 总人数    所有人的总分 = 所有班级分数累加(使用)  或  所有人的分数累加
    var avgScore = 0.0
    // 及格人数：  当每个人的成绩及格时，+1
    var nums = 0

    var totalScore = 0.0

    //班级数
    val classNum = 3
    val stuNum = 3

    //循环接受每个班每个学生的成绩   外层循环，每个班循环一次统计成绩  内层循环： 统计这个班所有学生的成绩
    for ( i <- 1 to classNum ){

      //当前班级的总分
      var totalClassScore = 0.0

      for( j <- 1 to stuNum){
        //提示：
        printf("请输入 %d 班 %d 号同学的成绩:",i,j)
        //接收成绩
        val score: Double = StdIn.readDouble()

        if (score > 60){
          nums += 1
        }
        //累加到班级总分
        totalClassScore += score
      }

      // 输出当前班的平均分
      printf(" %d 班的平均分是： %f",i,totalClassScore / stuNum)

      totalScore += totalClassScore
    }

    //所有学生全部统计完成
    printf(" 所有学生的平均分是： %f",totalScore / (classNum * stuNum))
    printf(" 所有学生的及格人数是： %d",nums)



  }


}
