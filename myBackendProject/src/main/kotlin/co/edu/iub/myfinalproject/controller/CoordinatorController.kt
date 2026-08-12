package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.coordinator.CreateCoordinatorRequest
import co.edu.iub.myfinalproject.dto.request.coordinator.UpdateCoordinatorRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.coordinator.CoordinatorResponse
import co.edu.iub.myfinalproject.service.CoordinatorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/coordinators")
class CoordinatorController(
    private val coordinatorService: CoordinatorService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createCoordinator(
        @Valid @RequestBody request: CreateCoordinatorRequest
    ): ResponseEntity<ApiResponse<CoordinatorResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Coordinator created successfully",
                    data = coordinatorService.createCoordinator(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateCoordinator(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateCoordinatorRequest
    ): ResponseEntity<ApiResponse<CoordinatorResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Coordinator updated successfully",
                data = coordinatorService.updateCoordinator(id, request)
            )
        )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR')")
    fun getAllCoordinators(): ResponseEntity<ApiResponse<List<CoordinatorResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Coordinators retrieved successfully",
                data = coordinatorService.getAllCoordinators()
            )
        )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('COORDINATOR')")
    fun getMyProfile(): ResponseEntity<ApiResponse<CoordinatorResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Profile retrieved successfully",
                data = coordinatorService.getMyProfile()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR')")
    fun getCoordinatorById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CoordinatorResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Coordinator retrieved successfully",
                data = coordinatorService.getCoordinatorById(id)
            )
        )
    }
}