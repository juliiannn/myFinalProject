package co.edu.iub.myfinalproject.dto.request.course

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class CreateCourseRequest(
    @field:NotBlank(message = "Course name is required")
    val name: String,

    @field:NotBlank(message = "Grade is required")
    val grade: String,

    @field:NotBlank(message = "Classroom is required")
    val classroom: String,

    @field:Min(value = 2000, message = "School year must be a valid year")
    val schoolYear: Int,

    val directorTeacherId: Long? = null
)