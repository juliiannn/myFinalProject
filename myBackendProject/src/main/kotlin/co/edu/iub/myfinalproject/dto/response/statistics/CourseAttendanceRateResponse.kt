package co.edu.iub.myfinalproject.dto.response.statistics

import java.math.BigDecimal

data class CourseAttendanceRateResponse(
    val courseId: Long,
    val courseName: String,
    val attendanceRate: BigDecimal,
    val totalRecords: Int
)