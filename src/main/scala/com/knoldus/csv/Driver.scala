package com.knoldus.csv

object Driver extends App {
  private val urlReader = CsvReader.fetchFromUrl(
    "https://www.stats.govt.nz/assets/Uploads/Business-operations-survey/Business-operations-survey-2022/Download-data/business-operations-survey-2022-information-and-communications-technology.csv"
  )

  private val fileReader = CsvReader.fetchFromFile(
    "/home/knoldus/Downloads/business-operations-survey-2022-business-finance.csv"
  )

  urlReader.map(_.eachRow { row =>
    val description = row.getOptionalValue("description")
    val industry = row.getOptionalValue("industry")
    val level = row.getOptionalValue("level")

    println(s"$description \t $industry \t $level")

  })
}
