import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler

object LogisticRegression {
  val train = ProcessData.train
  val valid = ProcessData.valid
  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")
  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)
    .setLabelCol("Result")

  val trainData = assembler.transform(train)
  val validData = assembler.transform(valid)
  val lrModel = lr.fit(trainData)

  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val predictions = lrModel.transform(validData)

  val accuracy = evaluator_binary.evaluate(predictions)
  print(accuracy)
}
