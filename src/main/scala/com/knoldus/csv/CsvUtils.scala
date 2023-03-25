package com.knoldus.csv

import org.apache.commons.io.IOUtils

import java.io.{File, FileInputStream, FileOutputStream, InputStream}
import java.net.{URL, URLConnection}
import scala.util.{Failure, Success, Try}

object CsvUtils {

  private val KnoldusCsvPrefix = "com.knoldus.csv.tmp"
  private val KnoldusCsvSuffix = ".csv"

  private[this] def establishConnection(url: String): Either[String, URLConnection] = {
    Try(new URL(url).openConnection()) match {
      case Failure(exception) =>
        Left(s"Error connecting to $url with error: ${exception.getMessage}")
      case Success(conn) =>
        conn.addRequestProperty("User-Agent", "Mozilla/4.76")
        Right(conn)
    }
  }

  private[this] def createTempFile(): Either[String, File] = {
    Try(File.createTempFile(KnoldusCsvPrefix, KnoldusCsvSuffix)) match {
      case Failure(exception) =>
        Left(s"Error in creating temporary file: ${exception.getMessage}")
      case Success(tempFile) =>
        tempFile.deleteOnExit()
        Right(tempFile)
    }
  }

  private[this] def writeToTempFile(is: InputStream, tempFile: File): Either[String, File] = {
    Try(new FileOutputStream(tempFile)) match {
      case Failure(exception) =>
        Left(s"Error writing to temp file: ${exception.getMessage}")
      case Success(os) =>
        IOUtils.copy(is, os)
        os.close()
        Right(tempFile)
    }
  }
  def downloadToTempFile(url: String): Either[String, File] = {
    for {
      conn <- establishConnection(url)
      tempFile <- createTempFile()
      updatedFile <- writeToTempFile(conn.getInputStream, tempFile)
    } yield updatedFile

  }

  def createFromFile(path: String): Either[String, File] = {
    val is = new FileInputStream(path)
    for {
      tempFile <- createTempFile()
      updatedFile <- writeToTempFile(is, tempFile)
    } yield {
      is.close()
      updatedFile
    }
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
