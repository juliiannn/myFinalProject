package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.teacher.CreateTeacherRequest
import co.edu.iub.myfinalproject.dto.request.teacher.UpdateTeacherRequest
import co.edu.iub.myfinalproject.dto.response.teacher.TeacherResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Teacher
import co.edu.iub.myfinalproject.model.UserRole
import co.edu.iub.myfinalproject.repository.TeacherRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TeacherService(
    private val teacherRepository: TeacherRepository,
    private val userService: UserService,
    private val currentUserService: CurrentUserService
) {
    @Transactional
    fun createTeacher(
        request: CreateTeacherRequest
    ): TeacherResponse {
        val employeeCode = request.employeeCode.trim()
        if (teacherRepository.existsByEmployeeCode(employeeCode)) {
            throw DuplicateResourceException("Employee code already exists")
        }

        val user = userService.createInternalUser(
            documentType = request.documentType,
            document = request.document.trim(),
            email = request.email.trim(),
            fullName = request.fullName.trim(),
            phone = request.phone.trim(),
            birthDate = request.birthDate,
            gender = request.gender,
            address = request.address.trim(),
            role = UserRole.TEACHER
        )

        val teacher = Teacher(
            user = user,
            employeeCode = employeeCode,
            hireDate = request.hireDate,
            profession = request.profession.trim(),
            specialty = request.specialty.trim(),
            educationLevel = request.educationLevel,
            contractType = request.contractType
        )

        return teacherRepository
            .save(teacher)
            .toResponse()
    }

    fun getAllTeachers(): List<TeacherResponse> {
        return teacherRepository
            .findAll()
            .map { it.toResponse() }
    }

    fun getTeacherById(id: Long): TeacherResponse {
        return findTeacher(id).toResponse()
    }

    fun getMyProfile(): TeacherResponse {
        val email = currentUserService.getCurrentUserEmail()
        val teacher = teacherRepository.findByUserEmail(email)
            ?: throw ResourceNotFoundException("No teacher profile found for the authenticated user")
        return teacher.toResponse()
    }

    fun updateTeacher(
        id: Long,
        request: UpdateTeacherRequest
    ): TeacherResponse {
        val teacher = findTeacher(id)
        val employeeCode = request.employeeCode.trim()

        if (teacherRepository.existsByEmployeeCodeAndIdNot(employeeCode, id)) {
            throw DuplicateResourceException("Employee code already exists")
        }

        teacher.employeeCode = employeeCode
        teacher.hireDate = request.hireDate
        teacher.profession = request.profession.trim()
        teacher.specialty = request.specialty.trim()
        teacher.educationLevel = request.educationLevel
        teacher.contractType = request.contractType

        return teacherRepository
            .save(teacher)
            .toResponse()
    }

    private fun findTeacher(id: Long): Teacher {
        return teacherRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Teacher with id $id not found")
            }
    }

    private fun Teacher.toResponse(): TeacherResponse {
        return TeacherResponse(
            id = requireNotNull(id),
            userId = requireNotNull(user.id),
            document = user.document,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone,
            birthDate = user.birthDate,
            enabled = user.enabled,
            employeeCode = employeeCode,
            hireDate = hireDate,
            profession = profession,
            specialty = specialty,
            educationLevel = educationLevel,
            contractType = contractType
        )
    }
}