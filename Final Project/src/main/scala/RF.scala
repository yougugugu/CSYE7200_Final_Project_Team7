import org.apache.spark.ml.classification.RandomForestClassifier

object RF {

  val rf = new RandomForestClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setNumTrees(64)
    .setSeed(3333L)

  val rfModel =rf.fit(ProcessData.assTrain)

  val validPred = rfModel.transform(ProcessData.assTrain)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
