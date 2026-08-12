package co.edu.iub.myfinalproject.dto.request.course

import jakarta.validation.constraints.NotNull

data class AssignDirectorTeacherRequest(
    @field:NotNull
    val teacherId: Long
)