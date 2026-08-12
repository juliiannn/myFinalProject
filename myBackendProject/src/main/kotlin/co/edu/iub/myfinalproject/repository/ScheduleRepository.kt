package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.DayOfWeek
import co.edu.iub.myfinalproject.model.Schedule
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository : JpaRepository<Schedule, Long> {
    fun findByCourseSubjectCourseIdAndDayOfWeek(courseId: Long, dayOfWeek: DayOfWeek): List<Schedule>
    fun findByCourseSubjectTeacherIdAndDayOfWeek(teacherId: Long, dayOfWeek: DayOfWeek): List<Schedule>
    fun findByCourseSubjectCourseId(courseId: Long): List<Schedule>
    fun findByCourseSubjectTeacherId(teacherId: Long): List<Schedule>
}