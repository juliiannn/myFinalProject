package co.edu.iub.myfinalproject.dto.response.schedule

import co.edu.iub.myfinalproject.model.DayOfWeek
import java.time.LocalTime

data class ScheduleResponse(
    val id: Long,
    val courseSubjectId: Long,
    val courseId: Long,
    val courseName: String,
    val subjectId: Long,
    val subjectName: String,
    val teacherId: Long,
    val teacherName: String,
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
)