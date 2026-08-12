package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AttendanceRepository : JpaRepository<Attendance, Long> {
    fun existsByStudentIdAndCourseSubjectIdAndDate(
        studentId: Long,
        courseSubjectId: Long,
        date: LocalDate
    ): Boolean

    fun findByStudentId(studentId: Long): List<Attendance>
    fun findByCourseSubjectIdAndDate(courseSubjectId: Long, date: LocalDate): List<Attendance>
}