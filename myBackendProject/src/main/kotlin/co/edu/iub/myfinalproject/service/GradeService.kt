package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.grade.CreateGradeRequest
import co.edu.iub.myfinalproject.dto.request.grade.UpdateGradeRequest
import co.edu.iub.myfinalproject.dto.response.grade.GradeResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ForbiddenOperationException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.model.Grade
import co.edu.iub.myfinalproject.repository.CourseSubjectRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import co.edu.iub.myfinalproject.repository.GradeRepository
import co.edu.iub.myfinalproject.repository.StudentRepository
import org.springframework.stereotype.Service

@Service
class GradeService(
    private val gradeRepository: GradeRepository,
    private val studentRepository: StudentRepository,
    private val courseSubjectRepository: CourseSubjectRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val currentUserService: CurrentUserService
) {
    fun createGrade(request: CreateGradeRequest): GradeResponse {
        val student = studentRepository
            .findById(request.studentId)
            .orElseThrow {
                ResourceNotFoundException("Student with id ${request.studentId} not found")
            }

        val courseSubject = courseSubjectRepository
            .findById(request.courseSubjectId)
            .orElseThrow {
                ResourceNotFoundException("CourseSubject with id ${request.courseSubjectId} not found")
            }

        if (courseSubject.teacher.id != currentUserService.getCurrentTeacherId()) {
            throw ForbiddenOperationException("You can only register grades for subjects assigned to you")
        }

        if (!courseSubject.active) {
            throw InvalidRequestException("Cannot register a grade for an inactive course-subject assignment")
        }

        val isEnrolled = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
            student.id!!,
            courseSubject.course.id!!,
            EnrollmentStatus.ACTIVE
        )
        if (!isEnrolled) {
            throw InvalidRequestException("Student is not actively enrolled in this course")
        }

        if (gradeRepository.existsByStudentIdAndCourseSubjectIdAndPeriod(
                student.id!!, courseSubject.id!!, request.period
            )) {
            throw DuplicateResourceException(
                "A grade already exists for this student, subject and period"
            )
        }

        val grade = Grade(
            student = student,
            courseSubject = courseSubject,
            period = request.period,
            value = request.value
        )
        return gradeRepository.save(grade).toResponse()
    }

    fun updateGrade(id: Long, request: UpdateGradeRequest): GradeResponse {
        val grade = findGrade(id)

        if (grade.courseSubject.teacher.id != currentUserService.getCurrentTeacherId()) {
            throw ForbiddenOperationException("You can only edit grades for subjects assigned to you")
        }

        grade.value = request.value
        return gradeRepository.save(grade).toResponse()
    }

    fun getMyGrades(): List<GradeResponse> {
        val studentId = currentUserService.getCurrentStudentId()
        return gradeRepository.findByStudentId(studentId).map { it.toResponse() }
    }

    fun getGradesByStudent(studentId: Long): List<GradeResponse> {
        if (currentUserService.hasRole("STUDENT") && studentId != currentUserService.getCurrentStudentId()) {
            throw ForbiddenOperationException("You can only view your own grades")
        }
        return gradeRepository.findByStudentId(studentId).map { it.toResponse() }
    }

    fun getGradesByCourseSubject(courseSubjectId: Long): List<GradeResponse> {
        return gradeRepository.findByCourseSubjectId(courseSubjectId).map { it.toResponse() }
    }

    fun getGradeById(id: Long): GradeResponse {
        return findGrade(id).toResponse()
    }

    private fun findGrade(id: Long): Grade {
        return gradeRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Grade with id $id not found")
            }
    }

    private fun Grade.toResponse(): GradeResponse {
        return GradeResponse(
            id = requireNotNull(id),
            studentId = requireNotNull(student.id),
            studentName = student.user.fullName,
            courseSubjectId = requireNotNull(courseSubject.id),
            subjectName = courseSubject.subject.name,
            courseName = courseSubject.course.name,
            teacherName = courseSubject.teacher.user.fullName,
            period = period,
            value = value
        )
    }
}