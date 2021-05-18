package com.learn.bookstore_backend.controller
import com.learn.bookstore_backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
@RequestMapping("/u")
class UserController (
        private val userService: UserService
        ) {
    @GetMapping("/{username}")
    fun getUserName(@PathVariable username: String): ResponseEntity<UserResponse> {
        return try {
            val user = userService.getUser(username)
            ResponseEntity.ok(user)

        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/{username}/status")
    fun getUserActivation(@PathVariable username: String): ResponseEntity<UserActivationStatusResponse> {
        return try {
            val res = userService.getUser(username)
            ResponseEntity.ok(UserActivationStatusResponse(res.username, res.createdTs.toString(), res.activated))
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return try {
            val response = userService.createUser(request)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}