package aryumka.axonkafka

import aryumka.axonkafka.api.GetBankAccount
import aryumka.axonkafka.core.BankAccountSummary
import org.axonframework.config.Configuration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Service

@Service
class BankAccountQueryService(
  private val queryGateway: QueryGateway,
  private val configuration: Configuration
) {
  fun getBankAccount(id: String): BankAccountSummary {
    return queryGateway.query(GetBankAccount(id), BankAccountSummary::class.java).join()
  }

  fun reset() {
    this.configuration
      .eventProcessingConfiguration()
      .eventProcessorByProcessingGroup("kafka-group", TrackingEventProcessor::class.java)
      .ifPresent {
        it.shutDown()
        it.resetTokens()
      }
  }

  fun restore() {
    this.configuration
      .eventProcessingConfiguration()
      .eventProcessorByProcessingGroup("kafka-group", TrackingEventProcessor::class.java)
      .ifPresent {
        it.start()
      }
  }
}
