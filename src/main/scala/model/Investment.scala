package model

import java.util.UUID

case class Investment(customerId: UUID, deposits: List[Deposit])

case class Deposit(depositPlan: String, portfolio: String, amount: Double)
