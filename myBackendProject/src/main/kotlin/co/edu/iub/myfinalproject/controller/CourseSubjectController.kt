package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.coursesubject.CreateCourseSubjectRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.coursesubject.CourseSubjectResponse
import co.edu.iub.myfinalproject.service.CourseSubjectService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course-subjects")
class CourseSubjectController(
    private val courseSubjectService: CourseSubjectService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createCourseSubject(
        @Valid @RequestBody request: CreateCourseSubjectRequest
    ): ResponseEntity<ApiResponse<CourseSubjectResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Subject assigned to course successfully",
                    data = courseSubjectService.createCourseSubject(request)
                )
            )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getAllCourseSubjects(): ResponseEntity<ApiResponse<List<CourseSubjectResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Course-subject assignments retrieved successfully",
                data = courseSubjectService.getAllCourseSubjects()
            )
        )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMySubjects(): ResponseEntity<ApiResponse<List<CourseSubjectResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Subjects retrieved successfully",
                data = courseSubjectService.getMySubjects()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getCourseSubjectById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CourseSubjectResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Course-subject assignment retrieved successfully",
                data = courseSubjectService.getCourseSubjectById(id)
            )
        )
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getCourseSubjectsByTeacher(
        @PathVariable teacherId: Long
    ): ResponseEntity<ApiResponse<List<CourseSubjectResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Teacher's subjects retrieved successfully",
                data = courseSubjectService.getCourseSubjectsByTeacher(teacherId)
            )
        )
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun deactivateCourseSubject(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CourseSubjectResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Assignment deactivated successfully",
                data = courseSubjectService.deactivateCourseSubject(id)
            )
        )
    }
}