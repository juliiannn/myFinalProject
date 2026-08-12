package co.edu.iub.myfinalproject.dto.request.schedule

import co.edu.iub.myfinalproject.model.DayOfWeek
import jakarta.validation.constraints.NotNull
import java.time.LocalTime

data class CreateScheduleRequest(
    @field:NotNull
    val courseSubjectId: Long,

    @field:NotNull
    val dayOfWeek: DayOfWeek,

    @field:NotNull
    val startTime: LocalTime,

    @field:NotNull
    val endTime: LocalTime
)