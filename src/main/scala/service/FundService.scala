package service

import db.Db
import model.{Deposit, FundsByPortfolio, Investment, TotalFunds}

import java.util.UUID

class FundService() extends Db {

  val investment = Investment(UUID.randomUUID(), List(Deposit(depositMonthly, highRiskPortfolio, 1000), Deposit(depositOneTime, highRiskPortfolio, 10000)))


  def getFundsByCustId(customerID: UUID): Option[TotalFunds] = getFunds.flatMap(_.get(customerID))


  def investFunds(inverstment: Investment): Map[UUID, TotalFunds] = {

    val isSingleDepositPlan = inverstment.deposits.count(_.depositPlan == depositMonthly) < 2 && inverstment.deposits.count(_.depositPlan == depositOneTime) < 2

    if (inverstment.deposits.map(_.depositPlan).length <= 2 && isSingleDepositPlan) {
      val dbAllFunds = getFunds
      val highRiskAmount = inverstment.deposits.filter(_.portfolio == highRiskPortfolio).map(_.amount).sum
      val retirementAmount = inverstment.deposits.filter(_.portfolio == retirementPortfolio).map(_.amount).sum

      val newTotalFunds = dbAllFunds.flatMap(_.get(inverstment.customerId)) match {
        case Some(funds) => funds.copy(totalFunds = funds.totalFunds.map {
          case fundsByPortfolio if fundsByPortfolio.portfolio == highRiskPortfolio => fundsByPortfolio.copy(amount = fundsByPortfolio.amount + highRiskAmount)
          case fundsByPortfolio if fundsByPortfolio.portfolio == retirementPortfolio => fundsByPortfolio.copy(amount = fundsByPortfolio.amount + retirementAmount)
          case fundsByPortfolio => fundsByPortfolio
        })
        case None => TotalFunds(List(FundsByPortfolio(highRiskPortfolio, highRiskAmount), FundsByPortfolio(retirementPortfolio, retirementAmount)))
      }
      dbAllFunds.getOrElse(Map.empty) + (inverstment.customerId -> newTotalFunds)
    } else {
      throw new Exception("Max two different  deposit plans are allowed")
    }

  }
}

object FundService extends FundService
