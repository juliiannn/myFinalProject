package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.student.CreateStudentRequest
import co.edu.iub.myfinalproject.dto.request.student.UpdateStudentRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.student.StudentResponse
import co.edu.iub.myfinalproject.service.StudentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/students")
class StudentController(
    private val studentService: StudentService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createStudent(
        @Valid @RequestBody request: CreateStudentRequest
    ): ResponseEntity<ApiResponse<StudentResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Student created successfully",
                    data = studentService.createStudent(request)
                )
            )
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateStudent(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateStudentRequest
    ): ResponseEntity<ApiResponse<StudentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Student updated successfully",
                data = studentService.updateStudent(
                    id,
                    request.guardianName,
                    request.guardianPhone,
                    request.guardianEmail
                )
            )
        )
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getAllStudents(): ResponseEntity<ApiResponse<List<StudentResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Students retrieved successfully",
                data = studentService.getAllStudents()
            )
        )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMyProfile(): ResponseEntity<ApiResponse<StudentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Profile retrieved successfully",
                data = studentService.getMyProfile()
            )
        )
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getStudentById(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<StudentResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Student retrieved successfully",
                data = studentService.getStudentById(id)
            )
        )
    }
}