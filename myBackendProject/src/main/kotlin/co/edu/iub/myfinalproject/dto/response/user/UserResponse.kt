package co.edu.iub.myfinalproject.dto.response.user

import co.edu.iub.myfinalproject.model.DocumentType
import co.edu.iub.myfinalproject.model.Gender
import co.edu.iub.myfinalproject.model.UserRole
import java.time.LocalDate
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val documentType: DocumentType,
    val document: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val address: String,
    val enabled: Boolean,
    val mustChangePassword: Boolean,
    val role: UserRole,
    val createdAt: LocalDateTime?
)