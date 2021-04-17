import org.apache.spark.ml.classification.{LinearSVC, LinearSVCModel}
import org.apache.spark.sql.DataFrame

object SVM {

  val svm: LinearSVC = new LinearSVC()
    .setMaxIter(100)
    .setRegParam(0.1)
    .setLabelCol("Result")
    .setFeaturesCol("features")

  val svmModel: LinearSVCModel = svm.fit(ProcessData.assTrain)

  val validPred: DataFrame = svmModel.transform(ProcessData.assValid)

  val acc: Double = Evaluator.evaluator_binary.evaluate(validPred)

  //println(acc)
}
