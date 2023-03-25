package com.knoldus.csv

import com.github.tototoshi.csv.CSVReader

import java.io.File

object CsvReader {
  def fetchFromUrl(
    url: String
  ): CsvReader = {
    CsvReader(
      file = CsvUtils.downloadToTempFile(url)
    )
  }

  def fetchFromFile(
                   path: String
                   ): CsvReader = {
    CsvReader(
      file = CsvUtils.createFromFile(path)
    )
  }
}

case class CsvReader(file: File) {

  private[this] var haveHeaders: Boolean = false
  private[this] var headers: Seq[String] = Nil
  private[this] var thisRow: Seq[String] = Nil

  def eachRow(f: CsvMap => Unit): Unit = {

    val reader: CSVReader = CSVReader.open(file)
    try {
      reader.foreach { parts =>
        parts
          .filter(_.trim.nonEmpty)
          .foreach { _ =>
            this.thisRow = parts
            if (haveHeaders)
              f(CsvMap(CsvUtils.toMap(headers, parts)))
            else {
              val tmp = CsvUtils.formatHeaders(parts)
              headers = tmp.map(_.toLowerCase)
              haveHeaders = true
            }
          }
      }

      /*reader.foreach { parts: Seq[String] =>
        if (parts.exists(_.trim.nonEmpty)) {
          this.thisRow = parts
          if (haveHeaders)
            f(CsvMap(CsvUtils.toMap(headers, parts)))
          else {
            val tmp = CsvUtils.formatHeaders(parts)
            headers = tmp.map(_.toLowerCase)
          }
        } else ()
      }*/
    } finally {
      reader.close()
    }
  }
}