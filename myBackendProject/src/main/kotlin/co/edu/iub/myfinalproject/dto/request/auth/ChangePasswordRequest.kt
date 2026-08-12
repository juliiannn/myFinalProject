package co.edu.iub.myfinalproject.dto.request.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    val newPassword: String
)