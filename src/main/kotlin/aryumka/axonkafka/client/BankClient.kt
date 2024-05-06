package aryumka.axonkafka.client

import aryumka.axonkafka.api.CreateBankAccountCommand
import aryumka.axonkafka.api.DepositMoneyCommand
import mu.KLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * 예약된 명령을 보내는 은행 클라이언트
 */
@Component
class BankClient(
  private val commandGateway: CommandGateway
) {

  companion object : KLogging()

  private val accountId = UUID.randomUUID().toString()
  private var amount = 100

  /**
   * 계좌는 한 번만 생성한다.
   */
  @Scheduled(initialDelay = 5_000, fixedDelay = 1000_000_000)
  fun createAccount() {
    logger.debug { "creating account $accountId" }
    commandGateway.send<CompletableFuture<String>>(
      CreateBankAccountCommand(
        bankAccountId = accountId,
        overdraftLimit = 1000
      )
    )
  }

  /**
   * 1초마다 한번씩 돈을 입금한다.
   */
  @Scheduled(initialDelay = 10_000, fixedDelay = 100)
  fun deposit() {
    logger.debug { "depositing $amount from account $accountId" }
    commandGateway.send<CompletableFuture<String>>(
      DepositMoneyCommand(
        bankAccountId = accountId,
        amountOfMoney = amount.toLong()
      )
    )
    amount = amount.inc()
  }
}
