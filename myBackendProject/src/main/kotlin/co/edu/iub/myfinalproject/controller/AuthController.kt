package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.auth.ChangePasswordRequest
import co.edu.iub.myfinalproject.dto.request.auth.LoginRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.auth.TokenResponse
import co.edu.iub.myfinalproject.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(

    private val authService: AuthService

) {

    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<TokenResponse>> {

        val token = authService.login(request)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Login successful",
                data = token
            )
        )
    }

    @PutMapping("/change-password")
    fun changePassword(
        @Valid
        @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<ApiResponse<Void>> {

        authService.changePassword(request)

        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Password changed successfully",
                data = null
            )
        )
    }

}