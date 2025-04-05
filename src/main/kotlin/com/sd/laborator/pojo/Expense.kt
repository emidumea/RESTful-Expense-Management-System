package com.sd.laborator.pojo

import com.sd.laborator.models.ExpenseCategory
import javax.persistence.*
import java.util.*

@Entity
@Table(name = "expenses")
data class Expense(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false)
    var userId: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var category: ExpenseCategory = ExpenseCategory.PERSONALE,

    @Column(nullable = false)
    var amount: Double = 0.0,

    @Column(nullable = false)
    var date: Date = Date(),

    @Column(nullable = false)
    var description: String = "",
)
