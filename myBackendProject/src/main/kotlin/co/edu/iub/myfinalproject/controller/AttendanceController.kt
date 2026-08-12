package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.attendance.CreateAttendanceRequest
import co.edu.iub.myfinalproject.dto.request.attendance.CreateBulkAttendanceRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.attendance.AttendanceResponse
import co.edu.iub.myfinalproject.service.AttendanceService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/attendances")
class AttendanceController(
    private val attendanceService: AttendanceService
) {
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    fun createAttendance(
        @Valid @RequestBody request: CreateAttendanceRequest
    ): ResponseEntity<ApiResponse<AttendanceResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Attendance registered successfully",
                    data = attendanceService.createAttendance(request)
                )
            )
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('TEACHER')")
    fun createBulkAttendance(
        @Valid @RequestBody request: CreateBulkAttendanceRequest
    ): ResponseEntity<ApiResponse<List<AttendanceResponse>>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Attendance registered successfully for the class",
                    data = attendanceService.createBulkAttendance(request)
                )
            )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMyAttendance(): ResponseEntity<ApiResponse<List<AttendanceResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Attendance retrieved successfully",
                data = attendanceService.getMyAttendance()
            )
        )
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER','STUDENT')")
    fun getAttendanceByStudent(
        @PathVariable studentId: Long
    ): ResponseEntity<ApiResponse<List<AttendanceResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Attendance retrieved successfully",
                data = attendanceService.getAttendanceByStudent(studentId)
            )
        )
    }

    @GetMapping("/course-subject/{courseSubjectId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getAttendanceByCourseSubjectAndDate(
        @PathVariable courseSubjectId: Long,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<ApiResponse<List<AttendanceResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Attendance retrieved successfully",
                data = attendanceService.getAttendanceByCourseSubjectAndDate(courseSubjectId, date)
            )
        )
    }
}