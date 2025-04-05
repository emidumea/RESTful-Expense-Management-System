package com.sd.laborator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.sd.laborator.repositories"])
class ExpensesManagementApplication

fun main(args: Array<String>) {
    runApplication<ExpensesManagementApplication>(*args)
}