package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.rector.CreateRectorRequest
import co.edu.iub.myfinalproject.dto.request.rector.UpdateRectorRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.rector.RectorResponse
import co.edu.iub.myfinalproject.service.RectorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rectors")
class RectorController(
    private val rectorService: RectorService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createRector(
        @Valid @RequestBody request: CreateRectorRequest
    ): ResponseEntity<ApiResponse<RectorResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Rector created successfully",
                    data = rectorService.createRector(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateRector(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateRectorRequest
    ): ResponseEntity<ApiResponse<RectorResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Rector updated successfully",
                data = rectorService.updateRector(id, request)
            )
        )
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllRectors(): ResponseEntity<ApiResponse<List<RectorResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Rectors retrieved successfully",
                data = rectorService.getAllRectors()
            )
        )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('RECTOR')")
    fun getMyProfile(): ResponseEntity<ApiResponse<RectorResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Profile retrieved successfully",
                data = rectorService.getMyProfile()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getRectorById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<RectorResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Rector retrieved successfully",
                data = rectorService.getRectorById(id)
            )
        )
    }
}