package aryumka.axonkafka.api

data class BankAccountCreatedEvent(
  val id: String,
  val overdraftLimit: Long
)

sealed class MoneyAddedEvent(
  open val bankAccountId: String,
  open val amount: Long
)

data class MoneyDepositedEvent(override val bankAccountId: String, override val amount: Long):
  MoneyAddedEvent(bankAccountId, amount)

data class MoneyOfFailedBankTransferReturnedEvent(override val bankAccountId: String, override val amount: Long):
  MoneyAddedEvent(bankAccountId, amount)

data class DestinationBankAccountCreditedEvent(
  override val bankAccountId: String,
  override val amount: Long,
  val bankTransferId: String
): MoneyAddedEvent(bankAccountId, amount)

sealed class MoneySubtractedEvent(
  open val bankAccountId: String,
  open val amount: Long
)

data class MoneyWithdrawnEvent(override val bankAccountId: String, override val amount: Long):
  MoneySubtractedEvent(bankAccountId, amount)

data class SourceBankAccountDebitedEvent(
  override val bankAccountId: String,
  override val amount: Long,
  val bankTransferId: String
): MoneySubtractedEvent(bankAccountId, amount)

data class SourceBankAccountDebitRejectedEvent(val bankTransferId: String)
