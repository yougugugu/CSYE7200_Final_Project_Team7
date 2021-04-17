import org.apache.spark.ml.classification.{LogisticRegression, OneVsRest}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler

object OVR {
  val train = ProcessData.train
  val valid = ProcessData.valid

  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")

  val classifier = new LogisticRegression()
    .setMaxIter(10)
    .setTol(1E-6)
    .setFitIntercept(true)

  val ovr = new OneVsRest()
    .setClassifier(classifier)
    .setLabelCol("Result")
    .setFeaturesCol("features")

  val atrain = assembler.transform(train)
  val gbtModel = ovr.fit(atrain)

  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val avalid = assembler.transform(valid)
  val val_pred = gbtModel.transform(avalid)
  val accu = evaluator_binary.evaluate(val_pred)
  println(accu)

}
