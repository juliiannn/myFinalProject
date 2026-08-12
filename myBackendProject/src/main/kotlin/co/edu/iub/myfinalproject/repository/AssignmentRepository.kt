package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Assignment
import org.springframework.data.jpa.repository.JpaRepository

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun findByCourseSubjectId(courseSubjectId: Long): List<Assignment>
    fun findByCourseSubjectCourseId(courseId: Long): List<Assignment>
}