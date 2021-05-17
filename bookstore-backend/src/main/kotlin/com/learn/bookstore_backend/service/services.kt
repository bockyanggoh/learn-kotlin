package com.learn.bookstore_backend.service

import com.learn.bookstore_backend.controller.CreateUserRequest
import com.learn.bookstore_backend.controller.UserResponse
import com.learn.bookstore_backend.database.repository.UserEntity
import com.learn.bookstore_backend.database.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class UserService @Autowired constructor(
        private val repository: UserRepository
) {
    fun checkIfUserExists(username: String): Boolean? {

        val res = repository.findUserEntityByUsername(username)
        return res != null
    }


    fun getUser(username: String): UserEntity {
        val res = repository.findUserEntityByUsername(username)
        if (res != null)
            return res

        throw Exception("Id not found")
    }

    fun createUser(request: CreateUserRequest): UserResponse {
        try {
            val user = repository.save(UserEntity(
                    username = request.username
            ))

            return UserResponse(
                    userId = user.id,
                    username = user.username,
                    createdTs = user.createdTs.toString(),
                    activated = user.activated
            )

        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}