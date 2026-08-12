package co.edu.iub.myfinalproject.dto.request.rector

import co.edu.iub.myfinalproject.model.EducationLevel
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate

data class UpdateRectorRequest(
    @field:NotBlank(message = "Employee code is required")
    val employeeCode: String,

    @field:PastOrPresent(message = "Appointment date cannot be in the future")
    val appointmentDate: LocalDate,

    @field:NotBlank(message = "Administrative period is required")
    val administrativePeriod: String,

    val educationLevel: EducationLevel
)