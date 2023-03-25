package com.knoldus.csv

import org.apache.commons.io.IOUtils

import java.io.{File, FileInputStream, FileOutputStream}
import java.net.URL

object CsvUtils {
  def downloadToTempFile(url: String): File = {
    val tempFile = File.createTempFile("com.knoldus.csv.tmp", ".csv")
    tempFile.deleteOnExit()

    val connection = new URL(url).openConnection()

    connection.addRequestProperty("User-Agent", "Mozilla/4.76")
    val out = new FileOutputStream(tempFile)
    IOUtils.copy(connection.getInputStream, out)
    out.close()
    tempFile
  }

  def createFromFile(path: String): File = {
    val tempFile = File.createTempFile("com.knoldus.csv.tmp", ".csv")
    tempFile.deleteOnExit()
    val is = new FileInputStream(path)
    val out = new FileOutputStream(tempFile)
    IOUtils.copy(is, out)
    out.close()
    tempFile
  }

  def toMap(headers: Seq[String], data: Seq[String]): Map[String, String] = {
    headers.zipWithIndex.map {
      case (headerValue, index) if index < data.length => headerValue -> data(index)
      case (headerValue, _) => headerValue -> ""
    }.toMap
  }

  def formatHeaders(values: Seq[String]): Seq[String] = {
    values.map { v =>
      v.trim.replace("\\s+", "_")
    }
  }
}
