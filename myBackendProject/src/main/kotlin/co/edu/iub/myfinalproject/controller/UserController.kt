package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.user.CreateUserRequest
import co.edu.iub.myfinalproject.dto.request.user.UpdateUserRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.user.UserResponse
import co.edu.iub.myfinalproject.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(
        @Valid @RequestBody request: CreateUserRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Admin user created successfully",
                    data = userService.createUser(request)
                )
            )
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): ResponseEntity<ApiResponse<List<UserResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Users retrieved successfully",
                data = userService.getAllUsers()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUserById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "User retrieved successfully",
                data = userService.getUserById(id)
            )
        )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "User updated successfully",
                data = userService.updateUser(id, request)
            )
        )
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    fun activateUser(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "User activated successfully",
                data = userService.activateUser(id)
            )
        )
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun deactivateUser(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "User deactivated successfully",
                data = userService.deactivateUser(id)
            )
        )
    }

    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    fun resetPassword(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Password reset successfully",
                data = userService.resetPassword(id)
            )
        )
    }
}