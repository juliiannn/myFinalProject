package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.subject.CreateSubjectRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.subject.SubjectResponse
import co.edu.iub.myfinalproject.service.SubjectService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/subjects")
class SubjectController(
    private val subjectService: SubjectService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createSubject(
        @Valid @RequestBody request: CreateSubjectRequest
    ): ResponseEntity<ApiResponse<SubjectResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Subject created successfully",
                    data = subjectService.createSubject(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateSubject(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateSubjectRequest
    ): ResponseEntity<ApiResponse<SubjectResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Subject updated successfully",
                data = subjectService.updateSubject(id, request)
            )
        )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getAllSubjects(): ResponseEntity<ApiResponse<List<SubjectResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Subjects retrieved successfully",
                data = subjectService.getAllSubjects()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getSubjectById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<SubjectResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Subject retrieved successfully",
                data = subjectService.getSubjectById(id)
            )
        )
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun deactivateSubject(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<SubjectResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Subject deactivated successfully",
                data = subjectService.deactivateSubject(id)
            )
        )
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    fun activateSubject(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<SubjectResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Subject activated successfully",
                data = subjectService.activateSubject(id)
            )
        )
    }
}