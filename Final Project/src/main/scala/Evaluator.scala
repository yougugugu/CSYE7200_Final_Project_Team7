import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator

object Evaluator {
  val evaluator_binary: BinaryClassificationEvaluator = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")
}
