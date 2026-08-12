package co.edu.iub.myfinalproject.dto.response.teacher

import co.edu.iub.myfinalproject.model.ContractType
import co.edu.iub.myfinalproject.model.EducationLevel
import java.time.LocalDate

data class TeacherResponse(
    val id: Long,
    val userId: Long,
    val document: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val birthDate: LocalDate,
    val enabled: Boolean,
    val employeeCode: String,
    val hireDate: LocalDate,
    val profession: String,
    val specialty: String,
    val educationLevel: EducationLevel,
    val contractType: ContractType
)