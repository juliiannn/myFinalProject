package co.edu.iub.myfinalproject.dto.request.user

import co.edu.iub.myfinalproject.model.DocumentType
import co.edu.iub.myfinalproject.model.Gender
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class UpdateUserRequest(
    val documentType: DocumentType,

    @field:NotBlank(message = "Document is required")
    @field:Pattern(
        regexp = "^[0-9]+$",
        message = "Document must contain only numbers"
    )
    @field:Size(
        min = 6,
        max = 15,
        message = "Document must contain between 6 and 15 digits"
    )
    val document: String,

    @field:Email(message = "Email must be valid")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:NotBlank(message = "Full name is required")
    val fullName: String,

    @field:NotBlank(message = "Phone is required")
    @field:Pattern(
        regexp = "^[0-9]+$",
        message = "Phone must contain only numbers"
    )
    val phone: String,

    @field:Past(message = "Birth date must be in the past")
    val birthDate: LocalDate,

    val gender: Gender,

    @field:NotBlank(message = "Address is required")
    val address: String
)