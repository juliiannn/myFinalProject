package co.edu.iub.myfinalproject.dto.request.teacher

import co.edu.iub.myfinalproject.model.ContractType
import co.edu.iub.myfinalproject.model.EducationLevel
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate

data class UpdateTeacherRequest(
    @field:NotBlank(message = "Employee code is required")
    val employeeCode: String,

    @field:PastOrPresent(message = "Hire date cannot be in the future")
    val hireDate: LocalDate,

    @field:NotBlank(message = "Profession is required")
    val profession: String,

    @field:NotBlank(message = "Specialty is required")
    val specialty: String,

    val educationLevel: EducationLevel,

    val contractType: ContractType
)