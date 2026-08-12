package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.attendance.CreateAttendanceRequest
import co.edu.iub.myfinalproject.dto.request.attendance.CreateBulkAttendanceRequest
import co.edu.iub.myfinalproject.dto.response.attendance.AttendanceResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ForbiddenOperationException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Attendance
import co.edu.iub.myfinalproject.model.CourseSubject
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.model.Student
import co.edu.iub.myfinalproject.repository.AttendanceRepository
import co.edu.iub.myfinalproject.repository.CourseSubjectRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import co.edu.iub.myfinalproject.repository.StudentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val studentRepository: StudentRepository,
    private val courseSubjectRepository: CourseSubjectRepository,
    private val enrollmentRepository: EnrollmentRepository,
    private val currentUserService: CurrentUserService
) {
    fun createAttendance(request: CreateAttendanceRequest): AttendanceResponse {
        val student = findStudent(request.studentId)
        val courseSubject = findCourseSubject(request.courseSubjectId)

        checkOwnership(courseSubject)
        validateAssignable(student, courseSubject, request.date)

        val attendance = Attendance(
            student = student,
            courseSubject = courseSubject,
            date = request.date,
            status = request.status
        )
        return attendanceRepository.save(attendance).toResponse()
    }

    @Transactional
    fun createBulkAttendance(request: CreateBulkAttendanceRequest): List<AttendanceResponse> {
        val courseSubject = findCourseSubject(request.courseSubjectId)
        checkOwnership(courseSubject)

        return request.records.map { record ->
            val student = findStudent(record.studentId)
            validateAssignable(student, courseSubject, request.date)

            val attendance = Attendance(
                student = student,
                courseSubject = courseSubject,
                date = request.date,
                status = record.status
            )
            attendanceRepository.save(attendance).toResponse()
        }
    }

    fun getMyAttendance(): List<AttendanceResponse> {
        val studentId = currentUserService.getCurrentStudentId()
        return attendanceRepository.findByStudentId(studentId).map { it.toResponse() }
    }

    fun getAttendanceByStudent(studentId: Long): List<AttendanceResponse> {
        if (currentUserService.hasRole("STUDENT") && studentId != currentUserService.getCurrentStudentId()) {
            throw ForbiddenOperationException("You can only view your own attendance")
        }
        return attendanceRepository.findByStudentId(studentId).map { it.toResponse() }
    }

    fun getAttendanceByCourseSubjectAndDate(
        courseSubjectId: Long,
        date: LocalDate
    ): List<AttendanceResponse> {
        return attendanceRepository
            .findByCourseSubjectIdAndDate(courseSubjectId, date)
            .map { it.toResponse() }
    }

    private fun checkOwnership(courseSubject: CourseSubject) {
        if (courseSubject.teacher.id != currentUserService.getCurrentTeacherId()) {
            throw ForbiddenOperationException("You can only register attendance for subjects assigned to you")
        }
    }

    private fun validateAssignable(student: Student, courseSubject: CourseSubject, date: LocalDate) {
        if (!courseSubject.active) {
            throw InvalidRequestException("Cannot register attendance for an inactive course-subject assignment")
        }

        val isEnrolled = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
            student.id!!,
            courseSubject.course.id!!,
            EnrollmentStatus.ACTIVE
        )
        if (!isEnrolled) {
            throw InvalidRequestException("Student is not actively enrolled in this course")
        }

        if (attendanceRepository.existsByStudentIdAndCourseSubjectIdAndDate(
                student.id!!, courseSubject.id!!, date
            )) {
            throw DuplicateResourceException(
                "Attendance already registered for this student, subject and date"
            )
        }
    }

    private fun findStudent(id: Long): Student {
        return studentRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Student with id $id not found") }
    }

    private fun findCourseSubject(id: Long): CourseSubject {
        return courseSubjectRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("CourseSubject with id $id not found") }
    }

    private fun Attendance.toResponse(): AttendanceResponse {
        return AttendanceResponse(
            id = requireNotNull(id),
            studentId = requireNotNull(student.id),
            studentName = student.user.fullName,
            courseSubjectId = requireNotNull(courseSubject.id),
            subjectName = courseSubject.subject.name,
            courseName = courseSubject.course.name,
            date = date,
            status = status
        )
    }
}