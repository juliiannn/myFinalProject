package co.edu.iub.myfinalproject.dto.response.auth

import co.edu.iub.myfinalproject.model.UserRole

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val mustChangePassword: Boolean,
    val role: UserRole
)