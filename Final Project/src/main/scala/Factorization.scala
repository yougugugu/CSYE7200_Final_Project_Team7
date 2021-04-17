import org.apache.spark.ml.classification.FMClassifier
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler

object Factorization {
  val train = ProcessData.train
  val valid = ProcessData.valid
  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")
  val trainData = assembler.transform(train)
  val validData = assembler.transform(valid)

  //  val labelIndexer = new StringIndexer()
  //    .setInputCol("Result")
  //    .setOutputCol("indexedLabel")
  //    .fit(trainData)
  //  // Scale features.
  //
  //  val featureScaler = new MinMaxScaler()
  //    .setInputCol("features")
  //    .setOutputCol("scaledFeatures")
  //    .fit(train)

  val fm = new FMClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("features")
    .setStepSize(0.001)

  //  val labelConverter = new IndexToString()
  //    .setInputCol("prediction")
  //    .setOutputCol("predictedLabel")
  //    .setLabels(labelIndexer.labelsArray(0))

  //  // Create a Pipeline.
  //  val pipeline = new Pipeline()
  //    .setStages(Array(labelIndexer, featureScaler, fm, labelConverter))

  // Train model.
  val model = fm.fit(trainData)

  val predictions = model.transform(validData)

  // Select example rows to display.
  //  predictions.select("predictedLabel", "label", "features").show(5)

  // Select (prediction, true label) and compute test accuracy.
  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")
  val accuracy = evaluator_binary.evaluate(predictions)
  println(s"Test set accuracy = $accuracy")

  //  val fmModel = model.stages(2).asInstanceOf[FMClassificationModel]
  //  println(s"Factors: ${fmModel.factors} Linear: ${fmModel.linear} " +
  //    s"Intercept: ${fmModel.intercept}")
}
