package aryumka.axonkafka

import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling


fun main(args: Array<String>) {
  runApplication<KafkaAxonApplication>(*args)
}

const val KAFKA_GROUP = "kafka-group"

@SpringBootApplication
@EnableScheduling
class KafkaAxonApplication {
  @Bean
  fun tokenStore() = InMemoryTokenStore()
}

@Configuration
@ConditionalOnProperty(value = ["axon.kafka.consumer.event-processor-mode"], havingValue = "tracking")
class TrackingConfiguration {

  @Autowired
  fun configureStreamableKafkaSource(
    configurer: EventProcessingConfigurer,
    streamableKafkaMessageSource: StreamableKafkaMessageSource<String, ByteArray>
  ) {
    // Tracking Event Processor의 이름을 KAFKA_GROUP으로 설정하고 StreamableKafkaMessageSource를 사용하여 등록
    configurer.registerTrackingEventProcessor(KAFKA_GROUP) { streamableKafkaMessageSource }
  }
}
