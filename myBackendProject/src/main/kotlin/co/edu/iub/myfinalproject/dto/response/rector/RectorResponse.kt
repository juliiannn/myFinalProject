package co.edu.iub.myfinalproject.dto.response.rector

import co.edu.iub.myfinalproject.model.EducationLevel
import java.time.LocalDate

data class RectorResponse(
    val id: Long,
    val userId: Long,
    val document: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val birthDate: LocalDate,
    val enabled: Boolean,
    val employeeCode: String,
    val appointmentDate: LocalDate,
    val administrativePeriod: String,
    val educationLevel: EducationLevel
)