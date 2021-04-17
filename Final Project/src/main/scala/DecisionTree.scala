import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator

object DecisionTree extends App{
  val train = ProcessData.train
  val valid = ProcessData.valid
  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")

  val dt = new DecisionTreeClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")


  val ttrain = assembler.transform(train)
  val dtModel = dt.fit(ttrain)

  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val tvalid = assembler.transform(valid)
  val val_pred = dtModel.transform(tvalid)
  val accur = evaluator_binary.evaluate(val_pred)
  println(accur)
}
