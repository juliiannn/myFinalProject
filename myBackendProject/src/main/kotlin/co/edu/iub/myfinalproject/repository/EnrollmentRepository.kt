package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Enrollment
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import org.springframework.data.jpa.repository.JpaRepository

interface EnrollmentRepository : JpaRepository<Enrollment, Long> {
    fun existsByStudentIdAndCourseIdAndStatus(
        studentId: Long,
        courseId: Long,
        status: EnrollmentStatus
    ): Boolean

    fun findByStudentIdAndStatus(studentId: Long, status: EnrollmentStatus): Enrollment?
    fun findByCourseIdAndStatus(courseId: Long, status: EnrollmentStatus): List<Enrollment>
    fun findByStatus(status: EnrollmentStatus): List<Enrollment>
}