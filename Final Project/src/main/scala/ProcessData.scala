import org.apache.spark.sql.SparkSession

object ProcessData extends App{
  val spark: SparkSession = SparkSession
    .builder()
    .appName("ProcessData")
    .master("local[*]")
    .getOrCreate()

  val bans= spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/bans.csv")
  bans.printSchema()
  bans.show()
  
  val gold = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/gold.csv")
  gold.printSchema()
  gold.show()
  
  val kills= spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/kills.csv")
  kills.printSchema()
  kills.show()

  val matchinfo = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/matchinfo.csv")
  matchinfo.printSchema()
  matchinfo.show()

  val monsters= spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/monsters.csv")
  monsters.printSchema()
  monsters.show()
  
  val structures = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/structures.csv")
  structures.printSchema()
  structures.show()

}
