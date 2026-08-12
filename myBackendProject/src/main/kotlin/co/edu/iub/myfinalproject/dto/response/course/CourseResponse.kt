package co.edu.iub.myfinalproject.dto.response.course

data class CourseResponse(
    val id: Long,
    val name: String,
    val grade: String,
    val classroom: String,
    val schoolYear: Int,
    val directorTeacherId: Long?,
    val directorTeacherName: String?,
    val active: Boolean
)