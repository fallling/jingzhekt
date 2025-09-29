package com.leng.jingzhekt.domain.usecase

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetBillsUseCase @Inject constructor(
    private val billRepository: BillRepository
) {
    operator fun invoke(): Flow<List<Bill>> = billRepository.getAllBills()
}

class GetBillsByDateRangeUseCase @Inject constructor(
    private val billRepository: BillRepository
) {
    operator fun invoke(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Bill>> =
        billRepository.getBillsByDateRange(startDate, endDate)
}

class InsertBillUseCase @Inject constructor(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(bill: Bill) = billRepository.insertBill(bill)
}

class DeleteBillUseCase @Inject constructor(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(bill: Bill) = billRepository.deleteBill(bill)
}

class UpdateBillUseCase @Inject constructor(
    private val billRepository: BillRepository
) {
    suspend operator fun invoke(bill: Bill) = billRepository.updateBill(bill)
}
