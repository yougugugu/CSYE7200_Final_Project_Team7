import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.DataFrame

object ModelSelection {
  val accuracyMap = Map("DT" -> DT.acc, "FM" -> FM.acc, "GBT" -> GBT.acc, "LR" -> LR.acc, "MLP" -> MLP.acc, "RF" -> RF.acc, "SVM" -> SVM.acc)

  val maxAccuracyKey: String = accuracyMap.maxBy(_._2) match {
    case (k, v) => k
  }

  def findBestModel(k: String) = k match {
    case "DT" => DT.dt
    case "FM" => FM.fm
    case "GBT" => GBT.gbt
    case "LR" => LR.lr
    case "MLP" => MLP.mlp
    case "RF" => RF.rf
    case "SVM" => SVM.svm
  }

  val pipeline: Pipeline = new Pipeline().setStages(Array(findBestModel(maxAccuracyKey)))
  val bestModel: PipelineModel = pipeline.fit(ProcessData.assTrain)
  bestModel.save("./bestModel")
  //  val bestModel = findBestModel(maxAccuracyKey)
  //  pipeline.write.overwrite.save("./bestModel")

  val loadModel: PipelineModel = PipelineModel.load("./bestModel")

  val testPred: DataFrame = bestModel.transform(ProcessData.assTest)

  val testAccuracy: Double = Evaluator.evaluator_binary.evaluate(testPred)

  //println(testAccuracy)
  //println(maxAccuracyKey)
}
