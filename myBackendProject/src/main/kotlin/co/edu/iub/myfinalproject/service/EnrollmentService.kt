package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.enrollment.CreateEnrollmentRequest
import co.edu.iub.myfinalproject.dto.request.enrollment.TransferEnrollmentRequest
import co.edu.iub.myfinalproject.dto.request.enrollment.UpdateEnrollmentStatusRequest
import co.edu.iub.myfinalproject.dto.response.enrollment.EnrollmentResponse
import co.edu.iub.myfinalproject.dto.response.student.StudentResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ForbiddenOperationException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.InvalidStatusTransitionException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Enrollment
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.model.Student
import co.edu.iub.myfinalproject.repository.CourseRepository
import co.edu.iub.myfinalproject.repository.CourseSubjectRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import co.edu.iub.myfinalproject.repository.StudentRepository
import org.springframework.stereotype.Service
import jakarta.transaction.Transactional
import java.time.LocalDate

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val courseRepository: CourseRepository,
    private val courseSubjectRepository: CourseSubjectRepository,
    private val currentUserService: CurrentUserService
) {
    @Transactional
    fun createEnrollment(
        request: CreateEnrollmentRequest
    ): EnrollmentResponse {
        val student = studentRepository
            .findById(request.studentId)
            .orElseThrow {
                ResourceNotFoundException("Student with id ${request.studentId} not found")
            }
        val course = courseRepository
            .findById(request.courseId)
            .orElseThrow {
                ResourceNotFoundException("Course with id ${request.courseId} not found")
            }

        if (!course.active) {
            throw InvalidRequestException("Cannot enroll a student in an inactive course")
        }

        val alreadyEnrolled = enrollmentRepository
            .existsByStudentIdAndCourseIdAndStatus(
                student.id!!,
                course.id!!,
                EnrollmentStatus.ACTIVE
            )
        if (alreadyEnrolled) {
            throw DuplicateResourceException("Student already enrolled in this course")
        }

        val enrollment = Enrollment(
            student = student,
            course = course,
            enrollmentDate = LocalDate.now(),
            status = EnrollmentStatus.ACTIVE
        )
        return enrollmentRepository
            .save(enrollment)
            .toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        return enrollmentRepository
            .findAll()
            .map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        return findEnrollment(id).toResponse()
    }

    fun cancelEnrollment(id: Long): EnrollmentResponse {
        val enrollment = findEnrollment(id)
        changeStatus(enrollment, EnrollmentStatus.CANCELLED)
        return enrollmentRepository.save(enrollment).toResponse()
    }

    fun updateStatus(
        id: Long,
        request: UpdateEnrollmentStatusRequest
    ): EnrollmentResponse {
        val enrollment = findEnrollment(id)
        changeStatus(enrollment, request.status)
        return enrollmentRepository.save(enrollment).toResponse()
    }

    @Transactional
    fun transferStudent(request: TransferEnrollmentRequest): EnrollmentResponse {
        val student = studentRepository
            .findById(request.studentId)
            .orElseThrow {
                ResourceNotFoundException("Student with id ${request.studentId} not found")
            }

        val currentEnrollment = enrollmentRepository
            .findByStudentIdAndStatus(student.id!!, EnrollmentStatus.ACTIVE)
            ?: throw ResourceNotFoundException("Student has no active enrollment to transfer from")

        val newCourse = courseRepository
            .findById(request.newCourseId)
            .orElseThrow {
                ResourceNotFoundException("Course with id ${request.newCourseId} not found")
            }

        if (!newCourse.active) {
            throw InvalidRequestException("Cannot enroll a student in an inactive course")
        }

        if (currentEnrollment.course.id == newCourse.id) {
            throw InvalidRequestException("Student is already enrolled in that course")
        }

        val alreadyEnrolledInTarget = enrollmentRepository
            .existsByStudentIdAndCourseIdAndStatus(student.id!!, newCourse.id!!, EnrollmentStatus.ACTIVE)
        if (alreadyEnrolledInTarget) {
            throw DuplicateResourceException("Student already enrolled in the target course")
        }

        changeStatus(currentEnrollment, EnrollmentStatus.CANCELLED)
        enrollmentRepository.save(currentEnrollment)

        val newEnrollment = Enrollment(
            student = student,
            course = newCourse,
            enrollmentDate = LocalDate.now(),
            status = EnrollmentStatus.ACTIVE
        )
        return enrollmentRepository.save(newEnrollment).toResponse()
    }

    fun getStudentsByCourse(courseId: Long): List<StudentResponse> {
        val course = courseRepository
            .findById(courseId)
            .orElseThrow { ResourceNotFoundException("Course with id $courseId not found") }

        if (currentUserService.hasRole("TEACHER")) {
            val teacherId = currentUserService.getCurrentTeacherId()
            val teachesThisCourse = courseSubjectRepository
                .findByCourseIdAndActiveTrue(requireNotNull(course.id))
                .any { it.teacher.id == teacherId }
            if (!teachesThisCourse) {
                throw ForbiddenOperationException("You can only view students from courses where you teach")
            }
        }

        return enrollmentRepository
            .findByCourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE)
            .map { it.student.toStudentResponse() }
    }

    private fun changeStatus(enrollment: Enrollment, newStatus: EnrollmentStatus) {
        val validTransitions = mapOf(
            EnrollmentStatus.ACTIVE to setOf(EnrollmentStatus.CANCELLED, EnrollmentStatus.COMPLETED),
            EnrollmentStatus.CANCELLED to emptySet(),
            EnrollmentStatus.COMPLETED to emptySet()
        )

        if (enrollment.status == newStatus) {
            return
        }

        val allowed = validTransitions[enrollment.status].orEmpty()
        if (newStatus !in allowed) {
            throw InvalidStatusTransitionException(
                "Cannot change enrollment status from ${enrollment.status} to $newStatus"
            )
        }

        enrollment.status = newStatus
    }

    private fun findEnrollment(id: Long): Enrollment {
        return enrollmentRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Enrollment with id $id not found")
            }
    }

    private fun Enrollment.toResponse(): EnrollmentResponse {
        return EnrollmentResponse(
            id = requireNotNull(id),
            studentId = requireNotNull(student.id),
            studentName = student.user.fullName,
            courseId = requireNotNull(course.id),
            courseName = course.name,
            enrollmentDate = enrollmentDate,
            status = status.name
        )
    }

    private fun Student.toStudentResponse(): StudentResponse {
        return StudentResponse(
            id = requireNotNull(id),
            userId = requireNotNull(user.id),
            document = user.document,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone,
            birthDate = user.birthDate,
            enabled = user.enabled,
            guardianName = guardianName,
            guardianPhone = guardianPhone,
            guardianEmail = guardianEmail
        )
    }
}