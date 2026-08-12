package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Course
import org.springframework.data.jpa.repository.JpaRepository

interface CourseRepository : JpaRepository<Course, Long> {
    fun existsByNameAndClassroomIgnoreCaseAndSchoolYear(
        name: String,
        classroom: String,
        schoolYear: Int
    ): Boolean

    fun existsByNameAndClassroomIgnoreCaseAndSchoolYearAndIdNot(
        name: String,
        classroom: String,
        schoolYear: Int,
        id: Long
    ): Boolean

    fun countByActiveTrue(): Long
}