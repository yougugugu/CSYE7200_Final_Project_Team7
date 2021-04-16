import org.apache.spark.sql.SparkSession

object ProcessData extends App {
  val spark: SparkSession = SparkSession
    .builder()
    .appName("ProcessData")
    .master("local[*]")
    .getOrCreate()

  //read all cvs as dataframe
  val bans = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/bans.csv")
//  bans.printSchema()
//  bans.show()

  val gold = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/gold.csv")
//  gold.printSchema()
//  gold.show()

  val kills = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/kills.csv")
//  kills.printSchema()
//  kills.show()

  val matchinfo = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/matchinfo.csv")
//  matchinfo.printSchema()
//  matchinfo.show()

  val monsters = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/monsters.csv")
//  monsters.printSchema()
//  monsters.show()

  val structures = spark.read.format("csv")
    .option("header", "true")
    .option("inferSchema", "true")
    .load("src/main/resources/structures.csv")
//  structures.printSchema()
//  structures.show()

  
  //Team gold difference
  gold.createOrReplaceTempView("gold")

  val teamDiff = spark.sql("SELECT Address, min_15 AS teamGoldDiff_15 FROM gold WHERE Type = 'golddiff'")
  teamDiff.createOrReplaceTempView("teamDiff")

  //Top gold difference
  val topBlue = spark.sql("SELECT Address, min_15 AS topBlueGold_15 FROM gold WHERE Type = 'goldblueTop'")
  topBlue.createOrReplaceTempView("topBlue")

  val topRed = spark.sql("SELECT Address, min_15 AS topRedGold_15 FROM gold WHERE Type = 'goldredTop'")
  topRed.createOrReplaceTempView("topRed")

  val topDiff = spark.sql("SELECT b.Address, b.topBlueGold_15 - r.topRedGold_15 AS topGoldDiff_15 FROM topBlue b JOIN topRed r ON b.Address = r.Address")
  topDiff.createOrReplaceTempView("topDiff")

  // Jungle gold difference
  val junBlue = spark.sql("SELECT Address, min_15 AS jungleBlueGold_15 FROM gold WHERE Type = 'goldblueJungle'")
  junBlue.createOrReplaceTempView("junBlue")

  val junRed = spark.sql("SELECT Address, min_15 AS jungleRedGold_15 FROM gold WHERE Type = 'goldredJungle'")
  junRed.createOrReplaceTempView("junRed")

  val junDiff = spark.sql("SELECT b.Address, b.jungleBlueGold_15 - r.jungleRedGold_15 AS jungleGoldDiff_15 FROM junBlue b JOIN junRed r ON b.Address = r.Address")
  junDiff.createOrReplaceTempView("junDiff")

  // Middle gold difference
  val midBlue = spark.sql("SELECT Address, min_15 AS middleBlueGold_15 FROM gold WHERE Type = 'goldblueMiddle'")
  midBlue.createOrReplaceTempView("midBlue")

  val midRed = spark.sql("SELECT Address, min_15 AS middleRedGold_15 FROM gold WHERE Type = 'goldredMiddle'")
  midRed.createOrReplaceTempView("midRed")

  val midDiff = spark.sql("SELECT b.Address, b.middleBlueGold_15 - r.middleRedGold_15 AS middleGoldDiff_15 FROM midBlue b JOIN midRed r ON b.Address = r.Address")
  midDiff.createOrReplaceTempView("midDiff")

  // ADC gold difference
  val adcBlue = spark.sql("SELECT Address, min_15 AS adcBlueGold_15 FROM gold WHERE Type = 'goldblueADC'")
  adcBlue.createOrReplaceTempView("adcBlue")

  val adcRed = spark.sql("SELECT Address, min_15 AS adcRedGold_15 FROM gold WHERE Type = 'goldredADC'")
  adcRed.createOrReplaceTempView("adcRed")

  val adcDiff = spark.sql("SELECT b.Address, b.adcBlueGold_15 - r.adcRedGold_15 AS adcGoldDiff_15 FROM adcBlue b JOIN adcRed r ON b.Address = r.Address")
  adcDiff.createOrReplaceTempView("adcDiff")

  //Support gold difference
  val supBlue = spark.sql("SELECT Address, min_15 AS supportBlueGold_15 FROM gold WHERE Type = 'goldblueSupport'")
  supBlue.createOrReplaceTempView("supBlue")

  val supRed = spark.sql("SELECT Address, min_15 AS supportRedGold_15 FROM gold WHERE Type = 'goldredSupport'")
  supRed.createOrReplaceTempView("supRed")

  val supDiff = spark.sql("SELECT b.Address, b.supportBlueGold_15 - r.supportRedGold_15 AS supportGoldDiff_15 FROM supBlue b JOIN supRed r ON b.Address = r.Address")
  supDiff.createOrReplaceTempView("supDiff")

  //Combine dataframe
  val goldDiff = spark.sql("SELECT team.Address AS Address, team.teamGoldDiff_15 AS team_15, t.topGoldDiff_15 AS top_15, j.jungleGoldDiff_15 AS jun_15, m.middleGoldDiff_15 AS mid_15, a.adcGoldDiff_15 AS adc_15, s.supportGoldDiff_15 AS sup_15 " +
                                    "FROM teamDiff team JOIN topDiff t ON team.Address = t.Address" +
                                                      " JOIN midDiff m ON team.Address = m.Address" +
                                                      " JOIN junDiff j ON team.Address = j.Address" +
                                                      " JOIN adcDiff a ON team.Address = a.Address" +
                                                      " JOIN supDiff s ON team.Address = s.Address")
  goldDiff.createOrReplaceTempView("goldDiff")
//  goldDiff.show()

  
  //Blue Monsters
  monsters.createOrReplaceTempView("bmonsters")

  val blueDragon = spark.sql("select Address, Count(Type) AS BlueDragon " +
    "from bmonsters where Time <= 15 AND Team in ('bDragons','bHeralds')" +
    "group by Address")
//  blueDragon.show()

  //Red Monsters
  monsters.createOrReplaceTempView("rmonsters")

  val redDragon = spark.sql("select Address,  Count(Type) AS RedDragon " +
    "from rmonsters where Time <= 15 AND Team in ('rDragons','rHeralds')" +
    "group by Address")
//  redDragon.show()
  
  
  //structure number of each team in each match
  structures.createOrReplaceTempView("structures")
  
  val BlueStruc = spark.sql("SELECT Address, COUNT(Type) AS BlueStruc FROM structures WHERE Time <= 15 AND Team IN ('bTowers', 'bInhibs') GROUP BY Address")
  val RedStruc = spark.sql("SELECT Address, COUNT(Type) AS RedStruc FROM structures WHERE Time <= 15 AND Team IN ('rTowers', 'rInhibs') GROUP BY Address")
//  BlueStruc.show()
//  RedStruc.show()

  
  //result of each match
  matchinfo.createOrReplaceTempView("matchinfo")
  
  val MatchResult = spark.sql("SELECT Address, bResult, rResult FROM matchinfo")
//  MatchResult.show()

  
  //kills number of each team in each match
  kills.createOrReplaceTempView("kills")
  
  val BlueKills = spark.sql("SELECT Address, COUNT(Killer) AS BlueKills FROM kills WHERE Time <= 15 AND Killer != 'TooEarly' AND Team = 'bKills' GROUP BY Address")
  val RedKills = spark.sql("SELECT Address, COUNT(Killer) AS RedKills FROM kills WHERE Time <= 15 AND Killer != 'TooEarly' AND Team = 'rKills' GROUP BY Address")
//  RedKills.show()


}
