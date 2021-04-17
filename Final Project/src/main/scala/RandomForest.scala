
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
object RandomForest {
  val train = ProcessData.train
  val valid = ProcessData.valid

  val assembler = new VectorAssembler()
    .setInputCols(Array("TeamDiff", "TopDiff", "JunDiff", "MidDiff", "ADCDiff", "SupDiff", "Dragons", "Structures", "Kills"))
    .setOutputCol("features")

  val ttrain = assembler.transform(train)

  val scaler = new StandardScaler()
    .setInputCol(assembler.getOutputCol)
    .setOutputCol("scaledFeatures")
    .setWithStd(true)
    .setWithMean(true)

  val scalerModel = scaler.fit(ttrain)

  val sttrain = scalerModel.transform(ttrain)

  val rf = new RandomForestClassifier()
    .setLabelCol("Result")
    .setFeaturesCol(scaler.getOutputCol)
    .setNumTrees(64)
    .setSeed(3333L)

  //val rfmodel = rf.fit(sttrain)
  val rfmodel =rf.fit(sttrain)
  val evaluator_binary = new BinaryClassificationEvaluator()
    .setLabelCol("Result")
    .setRawPredictionCol("rawPrediction")
    .setMetricName("areaUnderROC")

  val tvalid = assembler.transform(valid)
  val stvalid = scalerModel.transform(tvalid)
  val val_pred = rfmodel.transform(stvalid)
  val accur = evaluator_binary.evaluate(val_pred)
  println(accur)
}
