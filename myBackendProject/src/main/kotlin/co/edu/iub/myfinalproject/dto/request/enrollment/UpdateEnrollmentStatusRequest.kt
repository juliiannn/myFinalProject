package co.edu.iub.myfinalproject.dto.request.enrollment

import co.edu.iub.myfinalproject.model.EnrollmentStatus
import jakarta.validation.constraints.NotNull

data class UpdateEnrollmentStatusRequest(
    @field:NotNull
    val status: EnrollmentStatus
)