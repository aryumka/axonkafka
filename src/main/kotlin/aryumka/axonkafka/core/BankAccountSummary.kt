package aryumka.axonkafka.core

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "bank_account_summary")
class BankAccountSummary {
  @Id var id: String
  var balance: Long
  var overdraftLimit: Long

  constructor(id: String, balance: Long, overdraftLimit: Long) {
    this.id = id
    this.balance = balance
    this.overdraftLimit = overdraftLimit
  }
}
