package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.course.CreateCourseRequest
import co.edu.iub.myfinalproject.dto.response.course.CourseResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Course
import co.edu.iub.myfinalproject.repository.CourseRepository
import co.edu.iub.myfinalproject.repository.TeacherRepository
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository
) {
    fun createCourse(
        request: CreateCourseRequest
    ): CourseResponse {
        val name = request.name.trim()
        val classroom = request.classroom.trim()

        if (courseRepository.existsByNameAndClassroomIgnoreCaseAndSchoolYear(name, classroom, request.schoolYear)) {
            throw DuplicateResourceException("Course already exists for that school year")
        }

        val directorTeacher = request.directorTeacherId?.let { teacherId ->
            teacherRepository
                .findById(teacherId)
                .orElseThrow {
                    ResourceNotFoundException("Teacher with id $teacherId not found")
                }
        }

        val course = Course(
            name = name,
            grade = request.grade.trim(),
            classroom = classroom,
            schoolYear = request.schoolYear,
            directorTeacher = directorTeacher
        )
        return courseRepository
            .save(course)
            .toResponse()
    }

    fun updateCourse(
        id: Long,
        request: CreateCourseRequest
    ): CourseResponse {
        val course = findCourse(id)
        val name = request.name.trim()
        val classroom = request.classroom.trim()

        if (courseRepository.existsByNameAndClassroomIgnoreCaseAndSchoolYearAndIdNot(
                name, classroom, request.schoolYear, id
            )) {
            throw DuplicateResourceException("Course already exists for that school year")
        }

        val directorTeacher = request.directorTeacherId?.let { teacherId ->
            teacherRepository
                .findById(teacherId)
                .orElseThrow {
                    ResourceNotFoundException("Teacher with id $teacherId not found")
                }
        }

        course.name = name
        course.grade = request.grade.trim()
        course.classroom = classroom
        course.schoolYear = request.schoolYear
        course.directorTeacher = directorTeacher
        return courseRepository
            .save(course)
            .toResponse()
    }

    fun assignDirectorTeacher(id: Long, teacherId: Long): CourseResponse {
        val course = findCourse(id)
        val teacher = teacherRepository
            .findById(teacherId)
            .orElseThrow {
                ResourceNotFoundException("Teacher with id $teacherId not found")
            }
        course.directorTeacher = teacher
        return courseRepository.save(course).toResponse()
    }

    fun getAllCourses(): List<CourseResponse> {
        return courseRepository
            .findAll()
            .map { it.toResponse() }
    }

    fun getCourseById(id: Long): CourseResponse {
        return findCourse(id).toResponse()
    }

    fun deactivateCourse(id: Long): CourseResponse {
        val course = findCourse(id)
        course.active = false
        return courseRepository.save(course).toResponse()
    }

    fun activateCourse(id: Long): CourseResponse {
        val course = findCourse(id)
        course.active = true
        return courseRepository.save(course).toResponse()
    }

    private fun findCourse(id: Long): Course {
        return courseRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Course with id $id not found")
            }
    }

    private fun Course.toResponse(): CourseResponse {
        return CourseResponse(
            id = requireNotNull(id),
            name = name,
            grade = grade,
            classroom = classroom,
            schoolYear = schoolYear,
            directorTeacherId = directorTeacher?.id,
            directorTeacherName = directorTeacher?.user?.fullName,
            active = active
        )
    }
}