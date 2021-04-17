import org.apache.spark.ml.classification. GBTClassifier

object GBT {

  val gbt = new GBTClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setMaxIter(10)
    .setFeatureSubsetStrategy("auto")

  val gbtModel = gbt.fit(ProcessData.assTrain)

  val validPred = gbtModel.transform(ProcessData.assValid)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
