package db

import com.typesafe.config.ConfigFactory
import model.{FundsByPortfolio, Investment, TotalFunds}
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import java.util.UUID
import java.io._

trait Db {
  val depositMonthly = "Monthly"
  val depositOneTime = "OneTime"
  val highRiskPortfolio = "HighRisk"
  val retirementPortfolio = "Retirement"

  val config = ConfigFactory.load("application.conf")
  val dbPath = config.getString("db.file.path")

  def pushToDB(investment: Map[UUID, TotalFunds]): Unit = {
    val json = investment.asJson

    val fileObject = new File(dbPath)
    val printWriter = new PrintWriter(fileObject)
    printWriter.write(json.toString())
    printWriter.close()
  }

  def getFunds: Option[Map[UUID, TotalFunds]] = {

    val source = scala.io.Source.fromFile(dbPath)
    val filedata = source.mkString
    val resultFromDb = decode[Map[UUID, TotalFunds]](filedata) match {
      case Right(result) => Some(result)
      case Left(ex) => None
    }
    resultFromDb
  }
}
