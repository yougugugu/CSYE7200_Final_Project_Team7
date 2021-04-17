import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
object MLP {
  val train = ProcessData.train
  val valid = ProcessData.valid

  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")

  val ttrain = assembler.transform(train)

  val scaler = new StandardScaler()
    .setInputCol(assembler.getOutputCol)
    .setOutputCol("scaledFeatures")
    .setWithStd(true)
    .setWithMean(true)

  val scalerModel = scaler.fit(ttrain)

  val sttrain = scalerModel.transform(ttrain)

  val trainer = new MultilayerPerceptronClassifier()
    .setLabelCol("Result")
    .setFeaturesCol("scaledFeatures")
    .setLayers(Array(8, 7, 6, 5, 4, 3, 2))
    .setBlockSize(128)
    .setSeed(1234L)
    .setMaxIter(100)

  val model = trainer.fit(sttrain)

  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val tvalid = assembler.transform(valid)
  val stvalid = scalerModel.transform(tvalid)
  val val_pred = model.transform(stvalid)
  val accur = evaluator_binary.evaluate(val_pred)
  println(accur)
}
