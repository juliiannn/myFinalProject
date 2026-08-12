package co.edu.iub.myfinalproject.dto.response.assignment

import java.time.LocalDate
import java.time.LocalDateTime

data class AssignmentResponse(
    val id: Long,
    val courseSubjectId: Long,
    val courseId: Long,
    val courseName: String,
    val subjectId: Long,
    val subjectName: String,
    val teacherName: String,
    val title: String,
    val description: String,
    val dueDate: LocalDate,
    val createdAt: LocalDateTime?
)