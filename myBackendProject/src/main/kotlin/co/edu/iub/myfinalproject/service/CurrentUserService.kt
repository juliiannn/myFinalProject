package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.exception.InvalidCredentialsException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.repository.StudentRepository
import co.edu.iub.myfinalproject.repository.TeacherRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CurrentUserService(
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository
) {
    fun getCurrentUserEmail(): String {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw InvalidCredentialsException("User not authenticated")
        return authentication.name
    }

    fun getCurrentTeacherId(): Long {
        val email = getCurrentUserEmail()
        val teacher = teacherRepository.findByUserEmail(email)
            ?: throw ResourceNotFoundException("No teacher profile found for the authenticated user")
        return requireNotNull(teacher.id)
    }

    fun getCurrentStudentId(): Long {
        val email = getCurrentUserEmail()
        val student = studentRepository.findByUserEmail(email)
            ?: throw ResourceNotFoundException("No student profile found for the authenticated user")
        return requireNotNull(student.id)
    }

    fun hasRole(role: String): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw InvalidCredentialsException("User not authenticated")
        return authentication.authorities.any { it.authority == "ROLE_$role" }
    }
}