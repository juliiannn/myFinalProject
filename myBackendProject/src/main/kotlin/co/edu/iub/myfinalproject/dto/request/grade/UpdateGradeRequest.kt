package co.edu.iub.myfinalproject.dto.request.grade

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class UpdateGradeRequest(
    @field:NotNull
    @field:DecimalMin(value = "0.0", message = "Grade must be at least 0.0")
    @field:DecimalMax(value = "10.0", message = "Grade must be at most 10.0")
    val value: BigDecimal
)