package com.example.java_kotlin

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SimpleController {
    @GetMapping
    fun getApi(): ResponseEntity<String> {
        return ResponseEntity.ok("Success!")
    }
}