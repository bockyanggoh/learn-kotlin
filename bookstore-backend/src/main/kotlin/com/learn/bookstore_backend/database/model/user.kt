package com.learn.bookstore_backend.database.repository

import lombok.Data
import org.springframework.data.annotation.CreatedBy
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Data
class UserEntity(
        @Id @GeneratedValue var id: Long? = null,
        @Column(unique = true)
        var username: String,
        var activated: Boolean = true,
        var createdTs: LocalDateTime = LocalDateTime.now(),
        var createdBy: String = "SYSTEM",
        var updatedTs: LocalDateTime = LocalDateTime.now(),
        var updatedBy: String = "SYSTEM"
)

