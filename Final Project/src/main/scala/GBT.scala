import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.classification. GBTClassifier
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator

object GBT {
  val train = ProcessData.train
  val valid = ProcessData.valid

  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")

  val gbt = new GBTClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setMaxIter(10)
    .setFeatureSubsetStrategy("auto")

  val atrain = assembler.transform(train)
  val gbtModel = gbt.fit(atrain)

  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val avalid = assembler.transform(valid)
  val val_pred = gbtModel.transform(avalid)
  val accu = evaluator_binary.evaluate(val_pred)
  println(accu)

}
