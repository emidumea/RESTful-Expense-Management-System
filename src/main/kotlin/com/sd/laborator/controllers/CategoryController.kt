package com.sd.laborator.controllers

import com.sd.laborator.models.ExpenseCategory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController {
    @GetMapping
    fun getCategories(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(ExpenseCategory.values().map { it.name })
    }
}