package co.edu.iub.myfinalproject.dto.response.coordinator

import co.edu.iub.myfinalproject.model.CoordinationArea
import co.edu.iub.myfinalproject.model.EducationLevel
import java.time.LocalDate

data class CoordinatorResponse(
    val id: Long,
    val userId: Long,
    val document: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val birthDate: LocalDate,
    val enabled: Boolean,
    val employeeCode: String,
    val area: CoordinationArea,
    val assignmentDate: LocalDate,
    val educationLevel: EducationLevel
)