package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.student.CreateStudentRequest
import co.edu.iub.myfinalproject.dto.response.student.StudentResponse
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Student
import co.edu.iub.myfinalproject.model.UserRole
import co.edu.iub.myfinalproject.repository.StudentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository,
    private val userService: UserService,
    private val currentUserService: CurrentUserService
) {
    @Transactional
    fun createStudent(
        request: CreateStudentRequest
    ): StudentResponse {
        val user = userService.createInternalUser(
            documentType = request.documentType,
            document = request.document.trim(),
            email = request.email.trim(),
            fullName = request.fullName.trim(),
            phone = request.phone.trim(),
            birthDate = request.birthDate,
            gender = request.gender,
            address = request.address.trim(),
            role = UserRole.STUDENT
        )
        val student = Student(
            user = user,
            guardianName = request.guardianName.trim(),
            guardianPhone = request.guardianPhone.trim(),
            guardianEmail = request.guardianEmail.trim()
        )
        return studentRepository
            .save(student)
            .toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        return studentRepository
            .findAll()
            .map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        return findStudent(id).toResponse()
    }

    fun getMyProfile(): StudentResponse {
        val email = currentUserService.getCurrentUserEmail()
        val student = studentRepository.findByUserEmail(email)
            ?: throw ResourceNotFoundException("No student profile found for the authenticated user")
        return student.toResponse()
    }

    fun updateStudent(
        id: Long,
        guardianName: String,
        guardianPhone: String,
        guardianEmail: String
    ): StudentResponse {
        val student = findStudent(id)
        student.guardianName = guardianName.trim()
        student.guardianPhone = guardianPhone.trim()
        student.guardianEmail = guardianEmail.trim()
        return studentRepository
            .save(student)
            .toResponse()
    }

    private fun findStudent(id: Long): Student {
        return studentRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Student with id $id not found")
            }
    }

    private fun Student.toResponse(): StudentResponse {
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