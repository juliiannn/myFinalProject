package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.CourseSubject
import org.springframework.data.jpa.repository.JpaRepository

interface CourseSubjectRepository : JpaRepository<CourseSubject, Long> {
    fun existsByCourseIdAndSubjectIdAndActiveTrue(courseId: Long, subjectId: Long): Boolean
    fun findByTeacherIdAndActiveTrue(teacherId: Long): List<CourseSubject>
    fun findByCourseIdAndActiveTrue(courseId: Long): List<CourseSubject>
}