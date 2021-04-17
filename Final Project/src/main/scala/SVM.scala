import org.apache.spark.ml.classification.LinearSVC
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler

object SVM {
  val train = ProcessData.train
  val valid = ProcessData.valid
  val test = ProcessData.test

  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")

  val svm = new LinearSVC()
    .setMaxIter(10)
    .setRegParam(0.1)
    .setLabelCol("Result")
    .setFeaturesCol("features")

  val atrain = assembler.transform(train)
  val svmModel = svm.fit(atrain)

  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val avalid = assembler.transform(valid)
  val val_pred = svmModel.transform(avalid)
  val accu = evaluator_binary.evaluate(val_pred)
  println(accu)


}
