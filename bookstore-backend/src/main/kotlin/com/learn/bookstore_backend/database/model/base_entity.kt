package com.learn.bookstore_backend.database.model

import java.time.LocalDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) val id: Long? = null,
    val createdDate: LocalDateTime? = LocalDateTime.now(),
    val updatedDate: LocalDateTime? = LocalDateTime.now(),
    val createdBy: String? = "SYSTEM",
    val updatedBy: String? = "SYSTEM"
)