package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.course.AssignDirectorTeacherRequest
import co.edu.iub.myfinalproject.dto.request.course.CreateCourseRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.course.CourseResponse
import co.edu.iub.myfinalproject.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createCourse(
        @Valid @RequestBody request: CreateCourseRequest
    ): ResponseEntity<ApiResponse<CourseResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Course created successfully",
                    data = courseService.createCourse(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateCourse(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateCourseRequest
    ): ResponseEntity<ApiResponse<CourseResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Course updated successfully",
                data = courseService.updateCourse(id, request)
            )
        )
    }

    @PutMapping("/{id}/director-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    fun assignDirectorTeacher(
        @PathVariable id: Long,
        @Valid @RequestBody request: AssignDirectorTeacherRequest
    ): ResponseEntity<ApiResponse<CourseResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Director teacher assigned successfully",
                data = courseService.assignDirectorTeacher(id, request.teacherId)
            )
        )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getAllCourses(): ResponseEntity<ApiResponse<List<CourseResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Courses retrieved successfully",
                data = courseService.getAllCourses()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getCourseById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CourseResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Course retrieved successfully",
                data = courseService.getCourseById(id)
            )
        )
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun deactivateCourse(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CourseResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Course deactivated successfully",
                data = courseService.deactivateCourse(id)
            )
        )
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    fun activateCourse(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<CourseResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Course activated successfully",
                data = courseService.activateCourse(id)
            )
        )
    }
}