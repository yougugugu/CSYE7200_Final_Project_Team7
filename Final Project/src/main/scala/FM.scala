import org.apache.spark.ml.classification.FMClassifier

object FM {

  val fm = new FMClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setStepSize(0.001)

  // Train model.
  val fmModel = fm.fit(ProcessData.assTrain)

  val validPred = fmModel.transform(ProcessData.assValid)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
