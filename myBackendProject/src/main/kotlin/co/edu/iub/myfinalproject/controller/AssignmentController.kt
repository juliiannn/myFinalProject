package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.assignment.CreateAssignmentRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.assignment.AssignmentResponse
import co.edu.iub.myfinalproject.service.AssignmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/assignments")
class AssignmentController(
    private val assignmentService: AssignmentService
) {
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    fun createAssignment(
        @Valid @RequestBody request: CreateAssignmentRequest
    ): ResponseEntity<ApiResponse<AssignmentResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Assignment published successfully",
                    data = assignmentService.createAssignment(request)
                )
            )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMyAssignments(): ResponseEntity<ApiResponse<List<AssignmentResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Assignments retrieved successfully",
                data = assignmentService.getMyAssignments()
            )
        )
    }

    @GetMapping("/course-subject/{courseSubjectId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getAssignmentsByCourseSubject(
        @PathVariable courseSubjectId: Long
    ): ResponseEntity<ApiResponse<List<AssignmentResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Assignments retrieved successfully",
                data = assignmentService.getAssignmentsByCourseSubject(courseSubjectId)
            )
        )
    }
}