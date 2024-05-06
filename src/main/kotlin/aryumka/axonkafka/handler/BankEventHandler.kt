package aryumka.axonkafka.handler

import mu.KLogging
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.EventMessage
import org.springframework.stereotype.Component

@Component
@ProcessingGroup("kafka-group")
class BankEventHandler {

  companion object : KLogging()

  @EventHandler
  fun <T : EventMessage<Any>> on(event: T) {
    logger.info { "received event ${event.payload}" }
  }
}
