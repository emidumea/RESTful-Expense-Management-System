package com.sd.laborator.repositories

import com.sd.laborator.pojo.Expense
import com.sd.laborator.pojo.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository : JpaRepository<Expense, Int> {
    fun findByUserId(userId: Int): List<Expense>
}
