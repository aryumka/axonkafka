package aryumka.axonkafka.core

import aryumka.axonkafka.api.BankAccountCreatedEvent
import aryumka.axonkafka.api.CreateBankAccountCommand
import aryumka.axonkafka.api.DepositMoneyCommand
import aryumka.axonkafka.api.MoneyAddedEvent
import aryumka.axonkafka.api.MoneyDepositedEvent
import aryumka.axonkafka.api.MoneyOfFailedBankTransferReturnedEvent
import aryumka.axonkafka.api.MoneySubtractedEvent
import aryumka.axonkafka.api.MoneyWithdrawnEvent
import aryumka.axonkafka.api.ReturnMoneyOfFailedBankTransferCommand
import aryumka.axonkafka.api.WithdrawMoneyCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class BankAccount() {

  @AggregateIdentifier
  lateinit var id: String
  var overdraftLimit: Long = 0
  var balanceInCents: Long = 0

  @CommandHandler
  constructor(command: CreateBankAccountCommand) : this() {
    apply(BankAccountCreatedEvent(command.bankAccountId, command.overdraftLimit))
  }

  @CommandHandler
  fun deposit(command: DepositMoneyCommand) {
    apply(MoneyDepositedEvent(id, command.amountOfMoney))
  }

  @CommandHandler
  fun withdraw(command: WithdrawMoneyCommand) {
    if (command.amountOfMoney <= balanceInCents + overdraftLimit) {
      apply(MoneyWithdrawnEvent(id, command.amountOfMoney))
    }
  }

  @CommandHandler
  fun returnMoney(command: ReturnMoneyOfFailedBankTransferCommand) {
    apply(MoneyOfFailedBankTransferReturnedEvent(id, command.amount))
  }

  @EventSourcingHandler
  fun on(event: BankAccountCreatedEvent) {
    id = event.id
    overdraftLimit = event.overdraftLimit
    balanceInCents = 0
  }

  @EventSourcingHandler
  fun on(event: MoneyAddedEvent) {
    balanceInCents += event.amount
  }

  @EventSourcingHandler
  fun on(event: MoneySubtractedEvent) {
    balanceInCents -= event.amount
  }
}
