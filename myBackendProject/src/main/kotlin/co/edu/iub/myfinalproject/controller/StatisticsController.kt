package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.dto.response.ApiResponse
import co.edu.iub.myfinalproject.dto.response.statistics.CourseAttendanceRateResponse
import co.edu.iub.myfinalproject.dto.response.statistics.CourseStudentCountResponse
import co.edu.iub.myfinalproject.dto.response.statistics.GeneralStatisticsResponse
import co.edu.iub.myfinalproject.dto.response.statistics.SubjectAverageGradeResponse
import co.edu.iub.myfinalproject.service.StatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/statistics")
@PreAuthorize("hasAnyRole('ADMIN','RECTOR')")
class StatisticsController(
    private val statisticsService: StatisticsService
) {
    @GetMapping("/students-by-course")
    fun getStudentCountByCourse(): ResponseEntity<ApiResponse<List<CourseStudentCountResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Student count by course retrieved successfully",
                data = statisticsService.getStudentCountByCourse()
            )
        )
    }

    @GetMapping("/average-grades")
    fun getAverageGradesBySubject(): ResponseEntity<ApiResponse<List<SubjectAverageGradeResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Average grades retrieved successfully",
                data = statisticsService.getAverageGradesBySubject()
            )
        )
    }

    @GetMapping("/attendance-rate")
    fun getAttendanceRateByCourse(): ResponseEntity<ApiResponse<List<CourseAttendanceRateResponse>>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "Attendance rate retrieved successfully",
                data = statisticsService.getAttendanceRateByCourse()
            )
        )
    }

    @GetMapping("/general")
    fun getGeneralStatistics(): ResponseEntity<ApiResponse<GeneralStatisticsResponse>> {
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                message = "General statistics retrieved successfully",
                data = statisticsService.getGeneralStatistics()
            )
        )
    }
}