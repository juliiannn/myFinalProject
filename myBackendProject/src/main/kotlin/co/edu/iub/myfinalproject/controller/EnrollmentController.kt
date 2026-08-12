package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.enrollment.CreateEnrollmentRequest
import co.edu.iub.myfinalproject.dto.request.enrollment.TransferEnrollmentRequest
import co.edu.iub.myfinalproject.dto.request.enrollment.UpdateEnrollmentStatusRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.enrollment.EnrollmentResponse
import co.edu.iub.myfinalproject.dto.response.student.StudentResponse
import co.edu.iub.myfinalproject.service.EnrollmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','COORDINATOR')")
    fun createEnrollment(
        @Valid @RequestBody request: CreateEnrollmentRequest
    ): ResponseEntity<ApiResponse<EnrollmentResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Enrollment created successfully",
                    data = enrollmentService.createEnrollment(request)
                )
            )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getAllEnrollments(): ResponseEntity<ApiResponse<List<EnrollmentResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Enrollments retrieved successfully",
                data = enrollmentService.getAllEnrollments()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getEnrollmentById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<EnrollmentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Enrollment retrieved successfully",
                data = enrollmentService.getEnrollmentById(id)
            )
        )
    }

    @PutMapping("/transfer")
    @PreAuthorize("hasAnyRole('ADMIN','COORDINATOR')")
    fun transferStudent(
        @Valid @RequestBody request: TransferEnrollmentRequest
    ): ResponseEntity<ApiResponse<EnrollmentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Student transferred successfully",
                data = enrollmentService.transferStudent(request)
            )
        )
    }

    @GetMapping("/course/{courseId}/students")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getStudentsByCourse(
        @PathVariable courseId: Long
    ): ResponseEntity<ApiResponse<List<StudentResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Students retrieved successfully",
                data = enrollmentService.getStudentsByCourse(courseId)
            )
        )
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','COORDINATOR')")
    fun cancelEnrollment(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<EnrollmentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Enrollment cancelled successfully",
                data = enrollmentService.cancelEnrollment(id)
            )
        )
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','COORDINATOR')")
    fun updateStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateEnrollmentStatusRequest
    ): ResponseEntity<ApiResponse<EnrollmentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Enrollment status updated successfully",
                data = enrollmentService.updateStatus(id, request)
            )
        )
    }
}