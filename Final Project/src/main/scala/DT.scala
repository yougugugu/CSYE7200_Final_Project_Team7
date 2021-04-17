import org.apache.spark.ml.classification.DecisionTreeClassifier

object DT {

  val dt = new DecisionTreeClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")

  val dtModel = dt.fit(ProcessData.assTrain)

  val validPred = dtModel.transform(ProcessData.assValid)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
