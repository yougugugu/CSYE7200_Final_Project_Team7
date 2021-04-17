import org.apache.spark.ml.feature.StringIndexer

object ModelSelect extends App {
  val predict = ProcessData.predData

  val labelIndexer = new StringIndexer()
    .setInputCol("label")
    .setOutputCol("indexedLabel")
    .fit(predict)
}
