package app

import model.{Deposit, Investment}
import service.FundService

import java.util.UUID

object Start extends FundService with App {
  println("Starting...")

//  val investment = Investment(UUID.randomUUID(), List(Deposit(depositMonthly, highRiskPortfolio, 1000)))
  val investment1 = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d3"), List(Deposit(depositMonthly, highRiskPortfolio, 1000), Deposit(depositOneTime, retirementPortfolio, 1000)))

  val calulatedFunds = investFunds(investment1)
  pushToDB(calulatedFunds)
  println(getFundsByCustId(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d3")))

}
