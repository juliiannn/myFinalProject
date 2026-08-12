package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Grade
import co.edu.iub.myfinalproject.model.Period
import org.springframework.data.jpa.repository.JpaRepository

interface GradeRepository : JpaRepository<Grade, Long> {
    fun existsByStudentIdAndCourseSubjectIdAndPeriod(
        studentId: Long,
        courseSubjectId: Long,
        period: Period
    ): Boolean

    fun findByStudentId(studentId: Long): List<Grade>
    fun findByCourseSubjectId(courseSubjectId: Long): List<Grade>
}