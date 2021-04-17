import org.apache.spark.ml.classification.MultilayerPerceptronClassifier

object MLP {

  val mlp = new MultilayerPerceptronClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setLayers(Array(9, 7, 6, 2))
    .setBlockSize(128)
    .setSeed(4444L)
    .setMaxIter(100)

  val mlpModel = mlp.fit(ProcessData.assTrain)

  val validPred = mlpModel.transform(ProcessData.assValid)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
