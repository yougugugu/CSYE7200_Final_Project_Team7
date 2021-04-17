import org.apache.spark.ml.classification.LinearSVC

object SVM extends App{

  val svm = new LinearSVC()
    .setMaxIter(100)
    .setRegParam(0.1)
    .setLabelCol("Result")
    .setFeaturesCol("features")

  val svmModel = svm.fit(ProcessData.assTrain)

  val validPred = svmModel.transform(ProcessData.assValid)

  val acc = Evaluator.evaluator_binary.evaluate(validPred)

  println(acc)
}
