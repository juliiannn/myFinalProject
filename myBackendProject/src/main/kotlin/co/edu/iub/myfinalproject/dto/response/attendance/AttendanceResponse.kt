package co.edu.iub.myfinalproject.dto.response.attendance

import co.edu.iub.myfinalproject.model.AttendanceStatus
import java.time.LocalDate

data class AttendanceResponse(
    val id: Long,
    val studentId: Long,
    val studentName: String,
    val courseSubjectId: Long,
    val subjectName: String,
    val courseName: String,
    val date: LocalDate,
    val status: AttendanceStatus
)