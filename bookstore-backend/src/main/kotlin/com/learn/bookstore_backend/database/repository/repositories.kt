package com.learn.bookstore_backend.database.repository

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findUserEntityByUsername(username: String): UserEntity?
}