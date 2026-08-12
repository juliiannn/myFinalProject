package co.edu.iub.myfinalproject.dto.response.grade

import co.edu.iub.myfinalproject.model.Period
import java.math.BigDecimal

data class GradeResponse(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val courseSubjectId: Long,
    val subjectName: String,
    val courseName: String,
    val teacherName: String,
    val period: Period,
    val value: BigDecimal
)