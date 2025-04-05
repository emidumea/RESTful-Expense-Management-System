package com.sd.laborator.services

import com.sd.laborator.models.ExpenseCategory
import com.sd.laborator.pojo.Expense
import com.sd.laborator.pojo.User
import com.sd.laborator.repositories.ExpenseRepository
import com.sd.laborator.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository, private val userRepository: UserRepository ) {

    fun addExpense(expense: Expense) {
        expenseRepository.save(expense)
    }


    fun getExpenses(id: Int): List<Expense> {
        return expenseRepository.findByUserId(id)
    }

    //    fun getExpense(expenseId: Int): Expense?{
//        return expenseRepository.findById(expenseId)
//    }
    fun deleteExpense(id: Int) {
        expenseRepository.deleteById(id)
    }

    fun updateExpense(expenseId: Int, amount: Double, category: ExpenseCategory, description: String): Boolean {
        val expense = expenseRepository.findById(expenseId).orElse(null) ?: return false

        val updatedExpense = expense.copy(amount = amount, category = category, description = description)
        expenseRepository.save(updatedExpense)

        return true
    }

}

