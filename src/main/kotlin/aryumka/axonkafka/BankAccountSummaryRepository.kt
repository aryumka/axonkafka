package aryumka.axonkafka

import aryumka.axonkafka.core.BankAccountSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BankAccountSummaryRepository: JpaRepository<BankAccountSummary, String>
