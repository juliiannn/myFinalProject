package co.edu.iub.myfinalproject.dto.request.attendance

import co.edu.iub.myfinalproject.model.AttendanceStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate

data class BulkAttendanceRecord(
    @field:NotNull
    val studentId: Long,

    @field:NotNull
    val status: AttendanceStatus
)

data class CreateBulkAttendanceRequest(
    @field:NotNull
    val courseSubjectId: Long,

    @field:NotNull
    @field:PastOrPresent(message = "Attendance date cannot be in the future")
    val date: LocalDate,

    @field:NotEmpty(message = "At least one attendance record is required")
    @field:Valid
    val records: List<BulkAttendanceRecord>
)