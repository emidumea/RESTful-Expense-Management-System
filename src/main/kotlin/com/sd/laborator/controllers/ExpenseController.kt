package com.sd.laborator.controllers

import com.sd.laborator.models.ExpenseCategory
import com.sd.laborator.pojo.Expense
import com.sd.laborator.services.ExpenseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ExpenseController {

    @Autowired
    private lateinit var expenseService: ExpenseService

    @RequestMapping(value = ["expenses/add"], method = [RequestMethod.POST])
    fun addExpense(
        @RequestParam userId: Int,
        @RequestParam amount: Double,
        @RequestParam category: ExpenseCategory,
        @RequestParam description: String
        ): ResponseEntity<Unit>
    {
        val expense = Expense(userId = userId, amount = amount, category = category, description = description)
        expenseService.addExpense(expense)
        return ResponseEntity(Unit, HttpStatus.CREATED)

    }

    @RequestMapping(value = ["expenses/{id}"], method = [RequestMethod.GET])
    fun getExpenses(@PathVariable id: Int): ResponseEntity<List<Expense>>
    {
        val expenses: List<Expense>? = expenseService.getExpenses(id)
        val status = if (expenses == null)
        {
            HttpStatus.NOT_FOUND
        }
        else
        {
            HttpStatus.OK
        }
        return ResponseEntity(expenses, status)
    }

    @RequestMapping(value = ["expenses/delete/{id}"], method = [RequestMethod.DELETE])
    fun deleteExpense(@PathVariable id: Int): ResponseEntity<Unit>
    {
        expenseService.deleteExpense(id)
        return ResponseEntity(Unit, HttpStatus.NO_CONTENT)
    }

//    @RequestMapping(value = ["expenses/update/{id}"], method = [RequestMethod.PUT])
//    fun updateExpense(
//        @PathVariable id: Int,
//        @RequestParam amount: Double,
//        @RequestParam category: String,
//        @RequestParam description: String
//    ): ResponseEntity<String> {
//        val categoryEnum = try {
//            ExpenseCategory.valueOf(category)
//        } catch (e: IllegalArgumentException) {
//            return ResponseEntity("Invalid category", HttpStatus.BAD_REQUEST)
//        }
//
//        return if (expenseService.updateExpense(id, amount, categoryEnum, description)) {
//            ResponseEntity("Expense updated successfully", HttpStatus.OK)
//        } else {
//            ResponseEntity("Expense not found", HttpStatus.NOT_FOUND)
//        }
//    }

    @RequestMapping(value = ["expenses/update/{id}"], method = [RequestMethod.PUT])
    fun updateExpense(
        @PathVariable id: Int,
        @RequestBody expenseUpdate: Map<String, Any>
    ): ResponseEntity<String> {
        val amount = (expenseUpdate["amount"] as? Double) ?: return ResponseEntity("Invalid amount", HttpStatus.BAD_REQUEST)
        val categoryStr = (expenseUpdate["category"] as? String) ?: return ResponseEntity("Invalid category", HttpStatus.BAD_REQUEST)
        val description = (expenseUpdate["description"] as? String) ?: return ResponseEntity("Invalid description", HttpStatus.BAD_REQUEST)

        val category = try {
            ExpenseCategory.valueOf(categoryStr)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity("Invalid category", HttpStatus.BAD_REQUEST)
        }

        return if (expenseService.updateExpense(id, amount, category, description)) {
            ResponseEntity("Expense updated successfully", HttpStatus.OK)
        } else {
            ResponseEntity("Expense not found", HttpStatus.NOT_FOUND)
        }
    }

}
