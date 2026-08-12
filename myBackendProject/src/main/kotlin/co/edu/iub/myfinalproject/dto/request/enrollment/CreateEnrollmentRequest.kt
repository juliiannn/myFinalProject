package co.edu.iub.myfinalproject.dto.request.enrollment

import jakarta.validation.constraints.NotNull

data class CreateEnrollmentRequest(
    @field:NotNull
    val studentId: Long,

    @field:NotNull
    val courseId: Long
)