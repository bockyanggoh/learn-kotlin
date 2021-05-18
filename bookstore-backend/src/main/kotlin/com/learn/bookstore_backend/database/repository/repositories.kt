package com.learn.bookstore_backend.database.repository

import com.learn.bookstore_backend.database.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findUserEntityByUsername(username: String): UserEntity?
}