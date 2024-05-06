package aryumka.axonkafka.api

import jakarta.validation.constraints.Min
import org.axonframework.modelling.command.TargetAggregateIdentifier

/**
 *  계좌를 생성한다.
 */
data class CreateBankAccountCommand(
  @TargetAggregateIdentifier
  val bankAccountId: String,
  @Min(value = 0, message = "Overdraft limit must not be less than zero")
  val overdraftLimit: Long
)

/**
 * 입금
 */
data class DepositMoneyCommand(
  @TargetAggregateIdentifier
  val bankAccountId: String,
  val amountOfMoney: Long
)

/**
 * 출금
 */
data class WithdrawMoneyCommand(
  @TargetAggregateIdentifier
  val bankAccountId: String,
  val amountOfMoney: Long
)

/**
 * 이체가 불가능할 경우 돈을 반환한다.
 */
data class ReturnMoneyOfFailedBankTransferCommand(
  @TargetAggregateIdentifier
  val bankAccountId: String,
  val amount: Long
)
