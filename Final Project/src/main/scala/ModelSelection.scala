import org.apache.spark.ml.classification.{ClassificationModel, DecisionTreeClassificationModel, FMClassificationModel, GBTClassificationModel, LinearSVCModel, LogisticRegressionModel, MultilayerPerceptronClassificationModel, RandomForestClassificationModel}
import org.apache.spark.ml.linalg
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.sql.DataFrame

object ModelSelection {
  val accuracyMap = Map("DT" -> DT.acc, "FM" -> FM.acc, "GBT" -> GBT.acc, "LR" -> LR.acc, "MLP" -> MLP.acc, "RF" -> RF.acc, "SVM" -> SVM.acc)

  val maxAccuracyKey: String = accuracyMap.maxBy(_._2) match {case (k,v) => k}

  def findBestModel(k: String) = k match {
    case "DT" => DT.dtModel
    case "FM" => FM.fmModel
    case "GBT" => GBT.gbtModel
    case "LR" => LR.lrModel
    case "MLP" => MLP.mlpModel
    case "RF" => RF.rfModel
    case "SVM" => SVM.svmModel
  }

  val bestModel = findBestModel(maxAccuracyKey)

  val testPred: DataFrame = bestModel.transform(ProcessData.assTest)

  val testAccuracy: Double = Evaluator.evaluator_binary.evaluate(testPred)

  //println(testAccuracy)
  //println(maxAccuracyKey)
}
