import org.apache.spark.ml.classification.{FMClassificationModel, FMClassifier}
import org.apache.spark.sql.DataFrame

object FM {

  val fm: FMClassifier = new FMClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setStepSize(0.001)

  // Train model.
  val fmModel: FMClassificationModel = fm.fit(ProcessData.assTrain)

  val validPred: DataFrame = fmModel.transform(ProcessData.assValid)

  val acc: Double = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
