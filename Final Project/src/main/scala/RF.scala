import org.apache.spark.ml.classification.{RandomForestClassificationModel, RandomForestClassifier}
import org.apache.spark.sql.DataFrame

object RF {

  val rf: RandomForestClassifier = new RandomForestClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setNumTrees(64)
    .setSeed(3333L)

  val rfModel: RandomForestClassificationModel = rf.fit(ProcessData.assTrain)

  val validPred: DataFrame = rfModel.transform(ProcessData.assValid)

  val acc: Double = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
