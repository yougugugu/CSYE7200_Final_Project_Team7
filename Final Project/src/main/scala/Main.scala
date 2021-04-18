import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.feature.VectorAssembler

import java.util.Date
import scala.util.control.Breaks.{break, breakable}

object Main extends App {
  ProcessData.spark
  //  val predict = ProcessData.
  val filePath = "./bestModel"
  val file = scala.reflect.io.File(filePath)
  val bestModel: PipelineModel = {
    if (file.exists) {
      PipelineModel.load(filePath)
    } else {
      ModelSelection.loadModel
    }
  }
  breakable {
    while (true) {
      println("Press 1 to predict, else will close the app.")
      val condition = scala.io.StdIn.readInt()
      if (condition == 1) {
        println("Please input Team Gold Diff:")
        val tmd = scala.io.StdIn.readInt();
        println("Please input Top Gold Diff:")
        val tpd = scala.io.StdIn.readInt();
        println("Please input Jungle Gold Diff:")
        val jnd = scala.io.StdIn.readInt();
        println("Please input Mid Gold Diff:")
        val mdd = scala.io.StdIn.readInt();
        println("Please input ADC Gold Diff:")
        val acd = scala.io.StdIn.readInt();
        println("Please input Support Gold Diff:")
        val spd = scala.io.StdIn.readInt();
        println("Please input Dragon you got:")
        val dg = scala.io.StdIn.readInt();
        println("Please input Structures you got:")
        val stu = scala.io.StdIn.readInt();
        println("Please input kills you got:")
        val kil = scala.io.StdIn.readInt();



        val prev = new Date()

        val df = ProcessData.spark.createDataFrame(Seq(
          (tmd, tpd, jnd, mdd, acd, spd, dg, stu, kil),
        )).toDF("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills")
        val assembler = new VectorAssembler()
          .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
          .setOutputCol("features")

        val validData = assembler.transform(df)
        //  val evaluator_binary = new BinaryClassificationEvaluator()
        //    .setLabelCol("Result")
        //    .setRawPredictionCol("rawPrediction")
        //    .setMetricName("areaUnderROC")

        val predictions = bestModel.transform(validData)
        predictions.show(false)
        val now = new Date()
        println("Pridict Time: " + ((now.getTime - prev.getTime).toDouble / 1000))
        val a = predictions.select("prediction").rdd.first().getDouble(0)
        if (a == 1.0) {
          println("You will win!")
        } else {
          println("You will lose!")
        }
      } else {
        break;
      }
    }
  }

}
