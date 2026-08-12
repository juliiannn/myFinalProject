package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByEmailAndIdNot(email: String, id: Long): Boolean
    fun existsByDocument(document: String): Boolean
    fun existsByDocumentAndIdNot(document: String, id: Long): Boolean
}