package co.edu.iub.myfinalproject.dto.response.statistics

import java.math.BigDecimal

data class SubjectAverageGradeResponse(
    val courseId: Long,
    val courseName: String,
    val subjectId: Long,
    val subjectName: String,
    val averageGrade: BigDecimal,
    val totalGrades: Int
)