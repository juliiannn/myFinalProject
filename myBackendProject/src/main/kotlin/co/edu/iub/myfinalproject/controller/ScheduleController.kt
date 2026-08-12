package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.request.schedule.CreateScheduleRequest
import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.schedule.ScheduleResponse
import co.edu.iub.myfinalproject.service.ScheduleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/schedules")
class ScheduleController(
    private val scheduleService: ScheduleService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createSchedule(
        @Valid @RequestBody request: CreateScheduleRequest
    ): ResponseEntity<ApiResponse<ScheduleResponse>> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                ApiResponse(
                    success = true,
                    message = "Schedule created successfully",
                    data = scheduleService.createSchedule(request)
                )
            )
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMySchedule(): ResponseEntity<ApiResponse<List<ScheduleResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Schedule retrieved successfully",
                data = scheduleService.getMySchedule()
            )
        )
    }

    @GetMapping("/teacher/me")
    @PreAuthorize("hasRole('TEACHER')")
    fun getMyTeachingSchedule(): ResponseEntity<ApiResponse<List<ScheduleResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Teaching schedule retrieved successfully",
                data = scheduleService.getMyTeachingSchedule()
            )
        )
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR','TEACHER')")
    fun getSchedulesByCourse(
        @PathVariable courseId: Long
    ): ResponseEntity<ApiResponse<List<ScheduleResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Schedule retrieved successfully",
                data = scheduleService.getSchedulesByCourse(courseId)
            )
        )
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteSchedule(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Void>> {
        scheduleService.deleteSchedule(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Schedule deleted successfully",
                data = null
            )
        )
    }
}