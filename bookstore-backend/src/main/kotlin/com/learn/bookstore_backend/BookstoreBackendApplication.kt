package com.learn.bookstore_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class BookstoreBackendApplication

fun main(args: Array<String>) {
    runApplication<BookstoreBackendApplication>(*args)
}
