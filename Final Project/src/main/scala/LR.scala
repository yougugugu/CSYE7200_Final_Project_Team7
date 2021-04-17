import org.apache.spark.ml.classification.LogisticRegression


object LR {

  val lr = new LogisticRegression()
    .setMaxIter(10)
    .setRegParam(0.3)
    .setElasticNetParam(0.8)
    .setLabelCol("Result")

  val lrModel = lr.fit(ProcessData.assTrain)

  val validPred = lrModel.transform(ProcessData.assValid)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
