package model

case class TotalFunds(totalFunds: List[FundsByPortfolio])

case class FundsByPortfolio(portfolio: String, amount: Double)
