import org.apache.spark.sql.SparkSession

object ProcessData extends App {
  val spark: SparkSession = SparkSession
    .builder()
    .appName("ProcessData")
    .master("local[*]")
    .getOrCreate()

  val kills = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/kills.csv")
  kills.printSchema()
  kills.show()
}
