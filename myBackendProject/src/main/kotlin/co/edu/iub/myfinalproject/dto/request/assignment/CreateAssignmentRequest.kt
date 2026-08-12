package co.edu.iub.myfinalproject.dto.request.assignment

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateAssignmentRequest(
    @field:NotNull
    val courseSubjectId: Long,

    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:NotBlank(message = "Description is required")
    val description: String,

    @field:NotNull
    @field:FutureOrPresent(message = "Due date cannot be in the past")
    val dueDate: LocalDate
)