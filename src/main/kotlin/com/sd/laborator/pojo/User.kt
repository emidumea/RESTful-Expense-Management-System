package com.sd.laborator.pojo

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, unique = true)
    var username: String = "",

    @Column(nullable = false)
    var passwordHash: String = "",

    @Column(nullable = false)
    var encryptedData: String = ""
)
