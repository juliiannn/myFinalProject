package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.auth.ChangePasswordRequest
import co.edu.iub.myfinalproject.dto.request.auth.LoginRequest
import co.edu.iub.myfinalproject.dto.response.auth.TokenResponse
import co.edu.iub.myfinalproject.exception.InvalidCredentialsException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {
    fun login(request: LoginRequest): TokenResponse {
        val email = request.email.trim().lowercase()
        val user = userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException("Invalid credentials")

        if (!user.enabled || !passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialsException("Invalid credentials")
        }

        val token = jwtService.generateToken(user)
        return TokenResponse(
            accessToken = token,
            expiresIn = jwtService.expirationMinutes * 60,
            mustChangePassword = user.mustChangePassword,
            role = user.role
        )
    }

    fun changePassword(request: ChangePasswordRequest) {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw InvalidCredentialsException("User not authenticated")

        val email = authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("User not found")

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw InvalidCredentialsException("Current password is incorrect")
        }

        if (passwordEncoder.matches(request.newPassword, user.password)) {
            throw InvalidRequestException("New password must be different from the current password")
        }

        user.password = passwordEncoder.encode(request.newPassword)!!
        user.mustChangePassword = false
        userRepository.save(user)
    }
}