package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.response.statistics.CourseAttendanceRateResponse
import co.edu.iub.myfinalproject.dto.response.statistics.CourseStudentCountResponse
import co.edu.iub.myfinalproject.dto.response.statistics.GeneralStatisticsResponse
import co.edu.iub.myfinalproject.dto.response.statistics.SubjectAverageGradeResponse
import co.edu.iub.myfinalproject.model.AttendanceStatus
import co.edu.iub.myfinalproject.model.EnrollmentStatus
import co.edu.iub.myfinalproject.repository.AttendanceRepository
import co.edu.iub.myfinalproject.repository.CoordinatorRepository
import co.edu.iub.myfinalproject.repository.CourseRepository
import co.edu.iub.myfinalproject.repository.EnrollmentRepository
import co.edu.iub.myfinalproject.repository.GradeRepository
import co.edu.iub.myfinalproject.repository.StudentRepository
import co.edu.iub.myfinalproject.repository.SubjectRepository
import co.edu.iub.myfinalproject.repository.TeacherRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class StatisticsService(
    private val enrollmentRepository: EnrollmentRepository,
    private val gradeRepository: GradeRepository,
    private val attendanceRepository: AttendanceRepository,
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository,
    private val coordinatorRepository: CoordinatorRepository,
    private val subjectRepository: SubjectRepository
) {
    fun getStudentCountByCourse(): List<CourseStudentCountResponse> {
        return enrollmentRepository
            .findByStatus(EnrollmentStatus.ACTIVE)
            .groupBy { it.course }
            .map { (course, enrollments) ->
                CourseStudentCountResponse(
                    courseId = requireNotNull(course.id),
                    courseName = course.name,
                    studentCount = enrollments.size
                )
            }
    }

    fun getAverageGradesBySubject(): List<SubjectAverageGradeResponse> {
        return gradeRepository
            .findAll()
            .groupBy { it.courseSubject }
            .map { (courseSubject, grades) ->
                val average = grades
                    .map { it.value }
                    .fold(BigDecimal.ZERO) { acc, value -> acc + value }
                    .divide(BigDecimal(grades.size), 2, RoundingMode.HALF_UP)

                SubjectAverageGradeResponse(
                    courseId = requireNotNull(courseSubject.course.id),
                    courseName = courseSubject.course.name,
                    subjectId = requireNotNull(courseSubject.subject.id),
                    subjectName = courseSubject.subject.name,
                    averageGrade = average,
                    totalGrades = grades.size
                )
            }
    }

    fun getAttendanceRateByCourse(): List<CourseAttendanceRateResponse> {
        return attendanceRepository
            .findAll()
            .groupBy { it.courseSubject.course }
            .map { (course, records) ->
                val presentCount = records.count { it.status == AttendanceStatus.PRESENT }
                val rate = BigDecimal(presentCount)
                    .divide(BigDecimal(records.size), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP)

                CourseAttendanceRateResponse(
                    courseId = requireNotNull(course.id),
                    courseName = course.name,
                    attendanceRate = rate,
                    totalRecords = records.size
                )
            }
    }

    fun getGeneralStatistics(): GeneralStatisticsResponse {
        return GeneralStatisticsResponse(
            totalStudents = studentRepository.count(),
            totalTeachers = teacherRepository.count(),
            totalCoordinators = coordinatorRepository.count(),
            activeCourses = courseRepository.countByActiveTrue(),
            totalSubjects = subjectRepository.count()
        )
    }
}