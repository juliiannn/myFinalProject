package co.edu.iub.myfinalproject.dto.request.student

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UpdateStudentRequest(
    @field:NotBlank
    val guardianName: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^[0-9]+$",
        message = "Guardian phone must contain only numbers"
    )
    val guardianPhone: String,

    @field:Email
    @field:NotBlank
    val guardianEmail: String
)