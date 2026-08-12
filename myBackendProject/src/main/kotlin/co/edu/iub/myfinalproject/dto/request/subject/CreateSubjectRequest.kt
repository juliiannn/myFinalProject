package co.edu.iub.myfinalproject.dto.request.subject

import jakarta.validation.constraints.NotBlank

data class CreateSubjectRequest(
    @field:NotBlank(message = "Subject name is required")
    val name: String
)