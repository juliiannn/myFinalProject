package co.edu.iub.myfinalproject.dto.request.coursesubject

import jakarta.validation.constraints.NotNull

data class CreateCourseSubjectRequest(
    @field:NotNull
    val courseId: Long,

    @field:NotNull
    val subjectId: Long,

    @field:NotNull
    val teacherId: Long
)