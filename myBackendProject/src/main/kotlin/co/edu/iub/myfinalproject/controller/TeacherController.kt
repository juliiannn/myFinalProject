package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.teacher.CreateTeacherRequest
import co.edu.iub.myfinalproject.dto.request.teacher.UpdateTeacherRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.teacher.TeacherResponse
import co.edu.iub.myfinalproject.service.TeacherService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/teachers")
class TeacherController(
    private val teacherService: TeacherService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createTeacher(
        @Valid @RequestBody request: CreateTeacherRequest
    ): ResponseEntity<ApiResponse<TeacherResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Teacher created successfully",
                    data = teacherService.createTeacher(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateTeacher(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateTeacherRequest
    ): ResponseEntity<ApiResponse<TeacherResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Teacher updated successfully",
                data = teacherService.updateTeacher(id, request)
            )
        )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getAllTeachers(): ResponseEntity<ApiResponse<List<TeacherResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Teachers retrieved successfully",
                data = teacherService.getAllTeachers()
            )
        )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    fun getMyProfile(): ResponseEntity<ApiResponse<TeacherResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Profile retrieved successfully",
                data = teacherService.getMyProfile()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getTeacherById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<TeacherResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Teacher retrieved successfully",
                data = teacherService.getTeacherById(id)
            )
        )
    }
}