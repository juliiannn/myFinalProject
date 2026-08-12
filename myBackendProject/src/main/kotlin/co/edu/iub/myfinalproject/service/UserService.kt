package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.user.CreateUserRequest
import co.edu.iub.myfinalproject.dto.request.user.UpdateUserRequest
import co.edu.iub.myfinalproject.dto.response.user.UserResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.DocumentType
import co.edu.iub.myfinalproject.model.Gender
import co.edu.iub.myfinalproject.model.User
import co.edu.iub.myfinalproject.model.UserRole
import co.edu.iub.myfinalproject.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun createInternalUser(
        documentType: DocumentType,
        document: String,
        email: String,
        fullName: String,
        phone: String,
        birthDate: LocalDate,
        gender: Gender,
        address: String,
        role: UserRole
    ): User {
        if (userRepository.existsByDocument(document)) {
            throw DuplicateResourceException("Document already exists")
        }
        if (userRepository.existsByEmail(email.lowercase())) {
            throw DuplicateResourceException("Email already exists")
        }

        val user = User(
            documentType = documentType,
            document = document,
            email = email.lowercase(),
            fullName = fullName,
            phone = phone,
            birthDate = birthDate,
            gender = gender,
            address = address,
            password = "",
            role = role
        )

        assignTemporaryPassword(user)

        return userRepository.save(user)
    }

    fun createUser(request: CreateUserRequest): UserResponse {
        val user = createInternalUser(
            documentType = request.documentType,
            document = request.document.trim(),
            email = request.email.trim(),
            fullName = request.fullName.trim(),
            phone = request.phone.trim(),
            birthDate = request.birthDate,
            gender = request.gender,
            address = request.address.trim(),
            role = UserRole.ADMIN
        )
        return user.toResponse()
    }

    fun getAllUsers(): List<UserResponse> {
        return userRepository
            .findAll()
            .map { it.toResponse() }
    }

    fun getUserById(id: Long): UserResponse {
        return findUser(id).toResponse()
    }

    fun updateUser(
        id: Long,
        request: UpdateUserRequest
    ): UserResponse {
        val user = findUser(id)
        val document = request.document.trim()
        if (userRepository.existsByDocumentAndIdNot(document, id)) {
            throw DuplicateResourceException("Document already exists")
        }
        val email = request.email.trim().lowercase()
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw DuplicateResourceException("Email already exists")
        }
        user.documentType = request.documentType
        user.document = document
        user.email = email
        user.fullName = request.fullName.trim()
        user.phone = request.phone.trim()
        user.birthDate = request.birthDate
        user.gender = request.gender
        user.address = request.address.trim()
        return userRepository
            .save(user)
            .toResponse()
    }

    fun activateUser(id: Long): UserResponse {
        val user = findUser(id)
        user.enabled = true
        return userRepository.save(user).toResponse()
    }

    fun deactivateUser(id: Long): UserResponse {
        val user = findUser(id)
        user.enabled = false
        return userRepository.save(user).toResponse()
    }

    fun resetPassword(id: Long): UserResponse {
        val user = findUser(id)
        assignTemporaryPassword(user)
        return userRepository.save(user).toResponse()
    }

    private fun findUser(id: Long): User {
        return userRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("User with id $id not found")
            }
    }

    private fun assignTemporaryPassword(user: User) {
        user.password = passwordEncoder.encode(user.document)!!
        user.mustChangePassword = true
    }

    private fun User.toResponse(): UserResponse {
        return UserResponse(
            id = requireNotNull(id),
            documentType = documentType,
            document = document,
            email = email,
            fullName = fullName,
            phone = phone,
            birthDate = birthDate,
            gender = gender,
            address = address,
            enabled = enabled,
            mustChangePassword = mustChangePassword,
            role = role,
            createdAt = createdAt
        )
    }
}