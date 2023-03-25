package com.knoldus.csv

import scala.util.{Failure, Success, Try}

case class CsvMap(
  original: Map[String, String]
) {
  private[this] val data = original.map {
    case (k, v) => k.toLowerCase().trim -> v
  }

  def get(key: String): Either[String, String] =
    Try(data(key)) match {
      case Failure(exception) => Left(s"Error fetching value for key: $key.\nException: $exception ")
      case Success(value) => Right(value)
    }

  def getOptionalValue(key: String): Option[String] = {
    data.get(key.toLowerCase().trim).map(_.trim)
  }
}
