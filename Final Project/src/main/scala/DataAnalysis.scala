import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.stat.Statistics

object DataAnalysis extends App {
  val preData = ProcessData.predData

  //calculate the correlation
  val rddpreD = preData.rdd.map{row =>
    val first = row.getAs[Integer]("Result")
    val second = row.getAs[Integer]("TeamDiff")
    val third = row.getAs[Integer]("TopDiff")
    val fourth = row.getAs[Integer]("JunDiff")
    val fifth= row.getAs[Integer]("MidDiff")
    val sixth= row.getAs[Integer]("ADCDiff")
    val seventh= row.getAs[Integer]("SupDiff")
    val eighth= row.getAs[Integer]("Dragons")
    val ninth= row.getAs[Integer]("Structures")
    val tenth= row.getAs[Integer]("Kills")
    Vectors.dense(first.toDouble,second.toDouble,third.toDouble,fourth.toDouble,fifth.toDouble,sixth.toDouble,seventh.toDouble,eighth.toDouble,ninth.toDouble,tenth.toDouble)
  }

  val correlMatrix = Statistics.corr(rddpreD)

  import ProcessData.spark.implicits._
  val cols = (0 until correlMatrix.numCols)

  val df = correlMatrix.transpose
    .colIter.toSeq
    .map(_.toArray)
    .toDF("arr")

  val cor = cols.foldLeft(df)((df, i) => df.withColumn("_" + (i+1), $"arr"(i)))
    .drop("arr")
    .withColumnRenamed("_1","Result")
    .withColumnRenamed("_2","TeamDiff")
    .withColumnRenamed("_3","TopDiff")
    .withColumnRenamed("_4","JunDiff")
    .withColumnRenamed("_5","MidDiff")
    .withColumnRenamed("_6","ADCDiff")
    .withColumnRenamed("_7","SupDiff")
    .withColumnRenamed("_8","Dragons")
    .withColumnRenamed("_9","Structures")
    .withColumnRenamed("_10","Kills")

  cor.show(false)

}
