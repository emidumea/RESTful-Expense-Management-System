package com.sd.laborator.services

import com.sd.laborator.pojo.User
import com.sd.laborator.repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val userRepository: UserRepository)
{
    private val passwordEncoder = BCryptPasswordEncoder()

    fun registerUser(username: String, password: String, encryptedData: String): User
    {
        val hashedPassword = passwordEncoder.encode(username + password)
        val user = User(username = username, passwordHash = hashedPassword, encryptedData = encryptedData)
        return userRepository.save(user)
    }

    fun authenticateUser(username: String, password: String): Boolean
    {
        val user = userRepository.findByUsername(username)
        return user != null && passwordEncoder.matches(username + password, user.passwordHash)
    }
}