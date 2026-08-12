package co.edu.iub.myfinalproject.dto.response.coursesubject

data class CourseSubjectResponse(
    val id: Long,
    val courseId: Long,
    val courseName: String,
    val subjectId: Long,
    val subjectName: String,
    val teacherId: Long,
    val teacherName: String,
    val active: Boolean
)