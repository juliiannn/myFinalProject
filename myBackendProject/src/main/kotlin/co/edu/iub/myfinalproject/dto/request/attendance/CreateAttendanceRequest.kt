package co.edu.iub.myfinalproject.dto.request.attendance

import co.edu.iub.myfinalproject.model.AttendanceStatus
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate

data class CreateAttendanceRequest(
    @field:NotNull
    val studentId: Long,

    @field:NotNull
    val courseSubjectId: Long,

    @field:NotNull
    @field:PastOrPresent(message = "Attendance date cannot be in the future")
    val date: LocalDate,

    @field:NotNull
    val status: AttendanceStatus
)