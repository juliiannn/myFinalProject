package co.edu.iub.myfinalproject.dto.request.coordinator

import co.edu.iub.myfinalproject.model.CoordinationArea
import co.edu.iub.myfinalproject.model.EducationLevel
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate

data class UpdateCoordinatorRequest(
    @field:NotBlank(message = "Employee code is required")
    val employeeCode: String,

    val area: CoordinationArea,

    @field:PastOrPresent(message = "Assignment date cannot be in the future")
    val assignmentDate: LocalDate,

    val educationLevel: EducationLevel
)