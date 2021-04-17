import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}
import org.apache.spark.sql.DataFrame

object GBT {

  val gbt: GBTClassifier = new GBTClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setMaxIter(10)
    .setFeatureSubsetStrategy("auto")

  val gbtModel: GBTClassificationModel = gbt.fit(ProcessData.assTrain)

  val validPred: DataFrame = gbtModel.transform(ProcessData.assValid)

  val acc: Double = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
