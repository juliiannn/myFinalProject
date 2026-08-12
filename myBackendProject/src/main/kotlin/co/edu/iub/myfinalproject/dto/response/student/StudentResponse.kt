package co.edu.iub.myfinalproject.dto.response.student

import java.time.LocalDate

data class StudentResponse(
    val id: Long,
    val userId: Long,
    val document: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val birthDate: LocalDate,
    val enabled: Boolean,
    val guardianName: String,
    val guardianPhone: String,
    val guardianEmail: String
)