package co.edu.iub.myfinalproject.dto.request.enrollment

import jakarta.validation.constraints.NotNull

data class TransferEnrollmentRequest(
    @field:NotNull
    val studentId: Long,

    @field:NotNull
    val newCourseId: Long
)