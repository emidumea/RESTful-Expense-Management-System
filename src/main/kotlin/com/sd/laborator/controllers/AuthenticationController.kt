package com.sd.laborator.controllers

import com.sd.laborator.repositories.UserRepository
import com.sd.laborator.services.AuthenticationService
import com.sd.laborator.services.EncryptionService
import com.sd.laborator.services.ExpenseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class AuthenticationController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @Autowired
    private lateinit var encryptionService: EncryptionService

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestParam username: String, @RequestParam password: String, @RequestParam name: String): ResponseEntity<Unit>
    {
        println("Received: username=$username, password=$password, name=$name")
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null)
        {
            return ResponseEntity(Unit, HttpStatus.BAD_REQUEST)
        }
        val encryptedName = encryptionService.encrypt(name)
        authenticationService.registerUser(username, password, encryptedName)
        return ResponseEntity(Unit, HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestParam username: String, @RequestParam password: String): ResponseEntity<Map<String, Any>> {
        val user = userRepository.findByUsername(username)

        if (user != null && authenticationService.authenticateUser(username, password)) {
            val responseBody = mapOf("message" to "Login successful", "user_id" to user.id)
            return ResponseEntity(responseBody, HttpStatus.OK)
        }

        return ResponseEntity(mapOf("message" to "Invalid credentials"), HttpStatus.UNAUTHORIZED)
    }


}