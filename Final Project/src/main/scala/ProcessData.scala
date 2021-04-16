import org.apache.spark.sql.SparkSession

object ProcessData extends App {
  val spark: SparkSession = SparkSession
    .builder()
    .appName("ProcessData")
    .master("local[*]")
    .getOrCreate()

  val bans = spark.read.format("csv")
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

  val kills = spark.read.format("csv")
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

  val monsters = spark.read.format("csv")
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

  //structure number of each team in each match
  structures.createOrReplaceTempView("structures")
  val BlueStruc = spark.sql("SELECT Address, COUNT(Type) AS BlueStruc FROM structures WHERE Time <= 15 AND Team IN ('bTowers', 'bInhibs') GROUP BY Address")
  val RedStruc = spark.sql("SELECT Address, COUNT(Type) AS RedStruc FROM structures WHERE Time <= 15 AND Team IN ('rTowers', 'rInhibs') GROUP BY Address")
  BlueStruc.show()
  RedStruc.show()

  //result of each match
  matchinfo.createOrReplaceTempView("matchinfo")
  val MatchResult = spark.sql("SELECT Address, bResult, rResult FROM matchinfo")
  MatchResult.show()

  //kills number of each team in each match
  kills.createOrReplaceTempView("kills")
  val BlueKills = spark.sql("SELECT Address, COUNT(Killer) AS BlueKills FROM kills WHERE Time <= 15 AND Killer != 'TooEarly' AND Team = 'bKills' GROUP BY Address")
  val RedKills = spark.sql("SELECT Address, COUNT(Killer) AS RedKills FROM kills WHERE Time <= 15 AND Killer != 'TooEarly' AND Team = 'rKills' GROUP BY Address")
  RedKills.show()

}
