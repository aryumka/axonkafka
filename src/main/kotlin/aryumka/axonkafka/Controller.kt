package aryumka.axonkafka

import aryumka.axonkafka.core.BankAccountSummary
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
  private val bankAccountQueryService: BankAccountQueryService
) {

  @RequestMapping("/{id}")
  fun get(@PathVariable id: String): BankAccountSummary {
    return this.bankAccountQueryService.getBankAccount(id)
  }

  @RequestMapping("/reset")
  fun reset() {
    this.bankAccountQueryService.reset()
  }

  @RequestMapping("/restore")
  fun restore() {
    this.bankAccountQueryService.restore()
  }


}
