package com.example.serialization.utils

import java.time.format.DateTimeFormatter

object SystemConstants {
    final val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    final val baseUrl: String = "http://localhost:3000"
}
