package com.learn.bookstore_backend.controller

data class UserActivationStatusResponse(
        val username: String,
        val createdTs: String,
        val activated: Boolean
)

data class UserResponse(
        val userId: Long?,
        val username: String,
        val createdTs: String,
        val activated: Boolean
)