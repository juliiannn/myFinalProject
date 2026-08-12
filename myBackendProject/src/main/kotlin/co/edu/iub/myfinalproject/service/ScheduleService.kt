package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.schedule.CreateScheduleRequest
import co.edu.iub.myfinalproject.dto.response.schedule.ScheduleResponse
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.model.Schedule
import co.edu.iub.myfinalproject.repository.CourseSubjectRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import co.edu.iub.myfinalproject.repository.ScheduleRepository
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val courseSubjectRepository: CourseSubjectRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val currentUserService: CurrentUserService
) {
    fun createSchedule(request: CreateScheduleRequest): ScheduleResponse {
        if (!request.startTime.isBefore(request.endTime)) {
            throw InvalidRequestException("Start time must be before end time")
        }

        val courseSubject = courseSubjectRepository
            .findById(request.courseSubjectId)
            .orElseThrow {
                ResourceNotFoundException("CourseSubject with id ${request.courseSubjectId} not found")
            }

        if (!courseSubject.active) {
            throw InvalidRequestException("Cannot schedule an inactive course-subject assignment")
        }

        val courseId = requireNotNull(courseSubject.course.id)
        val teacherId = requireNotNull(courseSubject.teacher.id)

        val courseConflict = scheduleRepository
            .findByCourseSubjectCourseIdAndDayOfWeek(courseId, request.dayOfWeek)
            .any { overlaps(it.startTime, it.endTime, request.startTime, request.endTime) }
        if (courseConflict) {
            throw InvalidRequestException(
                "This course already has a class scheduled at that time on ${request.dayOfWeek}"
            )
        }

        val teacherConflict = scheduleRepository
            .findByCourseSubjectTeacherIdAndDayOfWeek(teacherId, request.dayOfWeek)
            .any { overlaps(it.startTime, it.endTime, request.startTime, request.endTime) }
        if (teacherConflict) {
            throw InvalidRequestException(
                "This teacher already has another class scheduled at that time on ${request.dayOfWeek}"
            )
        }

        val schedule = Schedule(
            courseSubject = courseSubject,
            dayOfWeek = request.dayOfWeek,
            startTime = request.startTime,
            endTime = request.endTime
        )
        return scheduleRepository.save(schedule).toResponse()
    }

    fun getSchedulesByCourse(courseId: Long): List<ScheduleResponse> {
        return scheduleRepository
            .findByCourseSubjectCourseId(courseId)
            .map { it.toResponse() }
    }

    fun getMySchedule(): List<ScheduleResponse> {
        val studentId = currentUserService.getCurrentStudentId()
        val enrollment = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE)
            ?: throw ResourceNotFoundException("You are not actively enrolled in any course")
        val courseId = requireNotNull(enrollment.course.id)
        return scheduleRepository
            .findByCourseSubjectCourseId(courseId)
            .map { it.toResponse() }
    }

    fun getMyTeachingSchedule(): List<ScheduleResponse> {
        val teacherId = currentUserService.getCurrentTeacherId()
        return scheduleRepository
            .findByCourseSubjectTeacherId(teacherId)
            .map { it.toResponse() }
    }

    fun deleteSchedule(id: Long) {
        val schedule = scheduleRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Schedule with id $id not found") }
        scheduleRepository.delete(schedule)
    }

    private fun overlaps(
        existingStart: java.time.LocalTime,
        existingEnd: java.time.LocalTime,
        newStart: java.time.LocalTime,
        newEnd: java.time.LocalTime
    ): Boolean {
        return newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd)
    }

    private fun Schedule.toResponse(): ScheduleResponse {
        return ScheduleResponse(
            id = requireNotNull(id),
            courseSubjectId = requireNotNull(courseSubject.id),
            courseId = requireNotNull(courseSubject.course.id),
            courseName = courseSubject.course.name,
            subjectId = requireNotNull(courseSubject.subject.id),
            subjectName = courseSubject.subject.name,
            teacherId = requireNotNull(courseSubject.teacher.id),
            teacherName = courseSubject.teacher.user.fullName,
            dayOfWeek = dayOfWeek,
            startTime = startTime,
            endTime = endTime
        )
    }
}