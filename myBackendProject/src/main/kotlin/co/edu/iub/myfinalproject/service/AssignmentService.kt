package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.assignment.CreateAssignmentRequest
import co.edu.iub.myfinalproject.dto.response.assignment.AssignmentResponse
import co.edu.iub.myfinalproject.exception.ForbiddenOperationException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Assignment
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.repository.AssignmentRepository
import co.edu.iub.myfinalproject.repository.CourseSubjectRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import org.springframework.stereotype.Service

@Service
class AssignmentService(
    private val assignmentRepository: AssignmentRepository,
    private val courseSubjectRepository: CourseSubjectRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val currentUserService: CurrentUserService
) {
    fun createAssignment(request: CreateAssignmentRequest): AssignmentResponse {
        val courseSubject = courseSubjectRepository
            .findById(request.courseSubjectId)
            .orElseThrow {
                ResourceNotFoundException("CourseSubject with id ${request.courseSubjectId} not found")
            }

        if (courseSubject.teacher.id != currentUserService.getCurrentTeacherId()) {
            throw ForbiddenOperationException("You can only publish assignments for subjects assigned to you")
        }

        if (!courseSubject.active) {
            throw InvalidRequestException("Cannot publish an assignment for an inactive course-subject assignment")
        }

        val assignment = Assignment(
            courseSubject = courseSubject,
            title = request.title.trim(),
            description = request.description.trim(),
            dueDate = request.dueDate
        )
        return assignmentRepository.save(assignment).toResponse()
    }

    fun getAssignmentsByCourseSubject(courseSubjectId: Long): List<AssignmentResponse> {
        return assignmentRepository
            .findByCourseSubjectId(courseSubjectId)
            .map { it.toResponse() }
    }

    fun getMyAssignments(): List<AssignmentResponse> {
        val studentId = currentUserService.getCurrentStudentId()
        val enrollment = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE)
            ?: throw ResourceNotFoundException("You are not actively enrolled in any course")
        val courseId = requireNotNull(enrollment.course.id)
        return assignmentRepository
            .findByCourseSubjectCourseId(courseId)
            .map { it.toResponse() }
    }

    private fun Assignment.toResponse(): AssignmentResponse {
        return AssignmentResponse(
            id = requireNotNull(id),
            courseSubjectId = requireNotNull(courseSubject.id),
            courseId = requireNotNull(courseSubject.course.id),
            courseName = courseSubject.course.name,
            subjectId = requireNotNull(courseSubject.subject.id),
            subjectName = courseSubject.subject.name,
            teacherName = courseSubject.teacher.user.fullName,
            title = title,
            description = description,
            dueDate = dueDate,
            createdAt = createdAt
        )
    }
}