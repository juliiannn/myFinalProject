package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.coursesubject.CreateCourseSubjectRequest
import co.edu.iub.myfinalproject.dto.response.coursesubject.CourseSubjectResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ForbiddenOperationException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.CourseSubject
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.repository.CourseRepository
import co.edu.iub.myfinalproject.repository.CourseSubjectRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import co.edu.iub.myfinalproject.repository.SubjectRepository
import co.edu.iub.myfinalproject.repository.TeacherRepository
import org.springframework.stereotype.Service

@Service
class CourseSubjectService(
    private val courseSubjectRepository: CourseSubjectRepository,
    private val courseRepository: CourseRepository,
    private val subjectRepository: SubjectRepository,
    private val teacherRepository: TeacherRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val currentUserService: CurrentUserService
) {
    fun createCourseSubject(
        request: CreateCourseSubjectRequest
    ): CourseSubjectResponse {
        val course = courseRepository
            .findById(request.courseId)
            .orElseThrow {
                ResourceNotFoundException("Course with id ${request.courseId} not found")
            }
        if (!course.active) {
            throw InvalidRequestException("Cannot assign a subject to an inactive course")
        }

        val subject = subjectRepository
            .findById(request.subjectId)
            .orElseThrow {
                ResourceNotFoundException("Subject with id ${request.subjectId} not found")
            }
        if (!subject.active) {
            throw InvalidRequestException("Cannot assign an inactive subject")
        }

        val teacher = teacherRepository
            .findById(request.teacherId)
            .orElseThrow {
                ResourceNotFoundException("Teacher with id ${request.teacherId} not found")
            }

        if (courseSubjectRepository.existsByCourseIdAndSubjectIdAndActiveTrue(course.id!!, subject.id!!)) {
            throw DuplicateResourceException("This subject is already assigned to this course")
        }

        val courseSubject = CourseSubject(
            course = course,
            subject = subject,
            teacher = teacher
        )
        return courseSubjectRepository
            .save(courseSubject)
            .toResponse()
    }

    fun getAllCourseSubjects(): List<CourseSubjectResponse> {
        return courseSubjectRepository
            .findAll()
            .map { it.toResponse() }
    }

    fun getCourseSubjectById(id: Long): CourseSubjectResponse {
        return findCourseSubject(id).toResponse()
    }

    fun getCourseSubjectsByTeacher(teacherId: Long): List<CourseSubjectResponse> {
        if (currentUserService.hasRole("TEACHER") && teacherId != currentUserService.getCurrentTeacherId()) {
            throw ForbiddenOperationException("You can only view your own assigned subjects")
        }
        return courseSubjectRepository
            .findByTeacherIdAndActiveTrue(teacherId)
            .map { it.toResponse() }
    }

    fun getMySubjects(): List<CourseSubjectResponse> {
        val studentId = currentUserService.getCurrentStudentId()
        val enrollment = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE)
            ?: throw ResourceNotFoundException("You are not actively enrolled in any course")
        val courseId = requireNotNull(enrollment.course.id)
        return courseSubjectRepository
            .findByCourseIdAndActiveTrue(courseId)
            .map { it.toResponse() }
    }

    fun deactivateCourseSubject(id: Long): CourseSubjectResponse {
        val courseSubject = findCourseSubject(id)
        courseSubject.active = false
        return courseSubjectRepository.save(courseSubject).toResponse()
    }

    private fun findCourseSubject(id: Long): CourseSubject {
        return courseSubjectRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("CourseSubject with id $id not found")
            }
    }

    private fun CourseSubject.toResponse(): CourseSubjectResponse {
        return CourseSubjectResponse(
            id = requireNotNull(id),
            courseId = requireNotNull(course.id),
            courseName = course.name,
            subjectId = requireNotNull(subject.id),
            subjectName = subject.name,
            teacherId = requireNotNull(teacher.id),
            teacherName = teacher.user.fullName,
            active = active
        )
    }
}