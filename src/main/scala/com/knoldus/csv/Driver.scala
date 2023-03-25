package com.knoldus.csv

object Driver extends App {
  val reader = CsvReader.fetchFromUrl(
    "https://www.stats.govt.nz/assets/Uploads/Business-operations-survey/Business-operations-survey-2022/Download-data/business-operations-survey-2022-information-and-communications-technology.csv"
  )

  reader.eachRow { row =>
    val description = row.getOptionalValue("description")
    val industry = row.getOptionalValue("industry")
    val level = row.getOptionalValue("level")

    println(s"$description \t $industry \t $level")

  }
}
