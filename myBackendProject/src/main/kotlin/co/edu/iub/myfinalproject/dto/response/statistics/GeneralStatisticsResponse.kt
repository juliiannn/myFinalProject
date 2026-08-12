package co.edu.iub.myfinalproject.dto.response.statistics

data class GeneralStatisticsResponse(
    val totalStudents: Long,
    val totalTeachers: Long,
    val totalCoordinators: Long,
    val activeCourses: Long,
    val totalSubjects: Long
)