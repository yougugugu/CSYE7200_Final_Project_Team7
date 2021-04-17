import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.sql.DataFrame

object DT {

  val dt: DecisionTreeClassifier = new DecisionTreeClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")

  val dtModel: DecisionTreeClassificationModel = dt.fit(ProcessData.assTrain)

  val validPred: DataFrame = dtModel.transform(ProcessData.assValid)

  val acc: Double = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
