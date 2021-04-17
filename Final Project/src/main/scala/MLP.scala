import org.apache.spark.ml.classification.{MultilayerPerceptronClassificationModel, MultilayerPerceptronClassifier}
import org.apache.spark.sql.DataFrame

object MLP {

  val mlp: MultilayerPerceptronClassifier = new MultilayerPerceptronClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setLayers(Array(9, 7, 6, 2))
    .setBlockSize(128)
    .setSeed(4444L)
    .setMaxIter(100)

  val mlpModel: MultilayerPerceptronClassificationModel = mlp.fit(ProcessData.assTrain)

  val validPred: DataFrame = mlpModel.transform(ProcessData.assValid)

  val acc: Double = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
