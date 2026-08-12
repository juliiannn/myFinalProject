package co.edu.iub.myfinalproject.dto.response.statistics

data class CourseStudentCountResponse(
    val courseId: Long,
    val courseName: String,
    val studentCount: Int
)