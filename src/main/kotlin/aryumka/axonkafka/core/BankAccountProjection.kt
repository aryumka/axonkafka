package aryumka.axonkafka.core

import aryumka.axonkafka.BankAccountSummaryRepository
import aryumka.axonkafka.api.BankAccountCreatedEvent
import aryumka.axonkafka.api.GetBankAccount
import aryumka.axonkafka.api.MoneyDepositedEvent
import aryumka.axonkafka.api.MoneyWithdrawnEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@ProcessingGroup("kafka-group")
class BankAccountProjection(
  private val bankAccountSummaryRepository: BankAccountSummaryRepository
) {

  @EventHandler
  @AllowReplay
  fun on(event: BankAccountCreatedEvent, @Timestamp timestamp: Instant) {
    this.bankAccountSummaryRepository.save(
      BankAccountSummary(
        id = event.id,
        overdraftLimit = event.overdraftLimit,
        balance = 0
      )
    )
  }

  @EventHandler
  @AllowReplay
  fun on(event: MoneyDepositedEvent, @Timestamp timestamp: Instant) {
    val account = this.bankAccountSummaryRepository.findById(event.bankAccountId).get()
    account.balance += event.amount
    this.bankAccountSummaryRepository.save(account)
  }

  @EventHandler
  @AllowReplay
  fun on(event: MoneyWithdrawnEvent, @Timestamp timestamp: Instant) {
    val account = this.bankAccountSummaryRepository.findById(event.bankAccountId).get()
    account.balance -= event.amount
    this.bankAccountSummaryRepository.save(account)
  }

  @QueryHandler
  fun on(query: GetBankAccount): BankAccountSummary {
    return this.bankAccountSummaryRepository.findById(query.bankAccountId).get()
  }

  @ResetHandler
  fun reset() {
    this.bankAccountSummaryRepository.deleteAll()
  }

}
