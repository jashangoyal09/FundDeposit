package service

import db.Db
import model.{Deposit, FundsByPortfolio, Investment, TotalFunds}
import org.mockito.Mockito.when
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.mockito.MockitoSugar

import java.util.UUID

class FundServiceTest extends FunSuite with Matchers with MockitoSugar {

  val mockedDb = mock[Db]
  val service: FundService = new FundService {
    override def getFunds: Option[Map[UUID, TotalFunds]] = mockedDb.getFunds
  }
  val depositMonthly = "Monthly"
  val depositOneTime = "OneTime"
  val highRiskPortfolio = "HighRisk"
  val retirementPortfolio = "Retirement"


  test("Investing First user with both Portfolio") {
    when(mockedDb.getFunds).thenReturn(None)
    val investment = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1"), List(Deposit(depositMonthly, highRiskPortfolio, 1000),
      Deposit(depositOneTime, retirementPortfolio, 1000)))
    val result = service.investFunds(investment)
    val response = Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 1000.0), FundsByPortfolio("Retirement", 1000.0))))

    assert(result == response)
  }

  test("Investing First user with one Portfolio") {
    when(mockedDb.getFunds).thenReturn(None)
    val investment = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1"), List(Deposit(depositMonthly, highRiskPortfolio, 1000)))
    val result = service.investFunds(investment)
    val response = Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 1000.0), FundsByPortfolio("Retirement", 0.0))))

    assert(result == response)
  }

  test("Investing again with one Portfolio") {
    when(mockedDb.getFunds).thenReturn(Some(Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 1000.0), FundsByPortfolio("Retirement", 1000.0))))))
    val investment = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1"), List(Deposit(depositMonthly, highRiskPortfolio, 10000)))
    val result = service.investFunds(investment)
    val response = Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 11000.0), FundsByPortfolio("Retirement", 1000.0))))

    assert(result == response)
  }

  test("Investing again with two Portfolio") {
    when(mockedDb.getFunds).thenReturn(Some(Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 1000.0), FundsByPortfolio("Retirement", 1000.0))))))
    val investment = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1"), List(Deposit(depositMonthly, highRiskPortfolio, 10000), Deposit(depositOneTime, retirementPortfolio, 10000)))
    val result = service.investFunds(investment)
    val response = Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 11000.0), FundsByPortfolio("Retirement", 11000.0))))

    assert(result == response)
  }

  test("throw exception while investing again with more than two deposit plan") {
    when(mockedDb.getFunds).thenReturn(Some(Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 1000.0), FundsByPortfolio("Retirement", 1000.0))))))
    val investment = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1"), List(Deposit(depositMonthly, highRiskPortfolio, 10000), Deposit(depositOneTime, retirementPortfolio, 10000), Deposit(depositOneTime, retirementPortfolio, 1000)))
    val thrown = intercept[Exception] {
      service.investFunds(investment)
    }

    assert(thrown.getMessage == "Max two different  deposit plans are allowed")
  }

  test("throw exception while investing again with two same deposit plan") {
    when(mockedDb.getFunds).thenReturn(Some(Map(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1") -> TotalFunds(List(FundsByPortfolio("HighRisk", 1000.0), FundsByPortfolio("Retirement", 1000.0))))))
    val investment = Investment(UUID.fromString("9cde7c19-a577-45ee-a2a9-fa86c72a90d1"), List(Deposit(depositMonthly, highRiskPortfolio, 10000), Deposit(depositMonthly, retirementPortfolio, 1000)))
    val thrown = intercept[Exception] {
      service.investFunds(investment)
    }

    assert(thrown.getMessage == "Max two different  deposit plans are allowed")
  }

}
