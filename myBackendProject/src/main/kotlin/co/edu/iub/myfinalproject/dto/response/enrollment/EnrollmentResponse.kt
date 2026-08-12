package co.edu.iub.myfinalproject.dto.response.enrollment

import java.time.LocalDate

data class EnrollmentResponse(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val courseId: Long,
    val courseName: String,
    val enrollmentDate: LocalDate,
    val status: String
)