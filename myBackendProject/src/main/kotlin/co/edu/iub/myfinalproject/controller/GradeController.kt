package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.grade.CreateGradeRequest
import co.edu.iub.myfinalproject.dto.request.grade.UpdateGradeRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.grade.GradeResponse
import co.edu.iub.myfinalproject.service.GradeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/grades")
class GradeController(
    private val gradeService: GradeService
) {
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    fun createGrade(
        @Valid @RequestBody request: CreateGradeRequest
    ): ResponseEntity<ApiResponse<GradeResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Grade registered successfully",
                    data = gradeService.createGrade(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    fun updateGrade(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateGradeRequest
    ): ResponseEntity<ApiResponse<GradeResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Grade updated successfully",
                data = gradeService.updateGrade(id, request)
            )
        )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMyGrades(): ResponseEntity<ApiResponse<List<GradeResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Grades retrieved successfully",
                data = gradeService.getMyGrades()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER','STUDENT')")
    fun getGradeById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<GradeResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Grade retrieved successfully",
                data = gradeService.getGradeById(id)
            )
        )
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER','STUDENT')")
    fun getGradesByStudent(
        @PathVariable studentId: Long
    ): ResponseEntity<ApiResponse<List<GradeResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Grades retrieved successfully",
                data = gradeService.getGradesByStudent(studentId)
            )
        )
    }

    @GetMapping("/course-subject/{courseSubjectId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getGradesByCourseSubject(
        @PathVariable courseSubjectId: Long
    ): ResponseEntity<ApiResponse<List<GradeResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Grades retrieved successfully",
                data = gradeService.getGradesByCourseSubject(courseSubjectId)
            )
        )
    }
}