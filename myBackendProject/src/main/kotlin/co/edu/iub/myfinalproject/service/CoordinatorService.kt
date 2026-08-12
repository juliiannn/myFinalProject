package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.coordinator.CreateCoordinatorRequest
import co.edu.iub.myfinalproject.dto.request.coordinator.UpdateCoordinatorRequest
import co.edu.iub.myfinalproject.dto.response.coordinator.CoordinatorResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Coordinator
import co.edu.iub.myfinalproject.model.UserRole
import co.edu.iub.myfinalproject.repository.CoordinatorRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CoordinatorService(
    private val coordinatorRepository: CoordinatorRepository,
    private val userService: UserService,
    private val currentUserService: CurrentUserService
) {
    @Transactional
    fun createCoordinator(request: CreateCoordinatorRequest): CoordinatorResponse {
        val employeeCode = request.employeeCode.trim()
        if (coordinatorRepository.existsByEmployeeCode(employeeCode)) {
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
            role = UserRole.COORDINATOR
        )

        val coordinator = Coordinator(
            user = user,
            employeeCode = employeeCode,
            area = request.area,
            assignmentDate = request.assignmentDate,
            educationLevel = request.educationLevel
        )

        return coordinatorRepository.save(coordinator).toResponse()
    }

    fun getAllCoordinators(): List<CoordinatorResponse> {
        return coordinatorRepository.findAll().map { it.toResponse() }
    }

    fun getCoordinatorById(id: Long): CoordinatorResponse {
        return findCoordinator(id).toResponse()
    }

    fun getMyProfile(): CoordinatorResponse {
        val email = currentUserService.getCurrentUserEmail()
        val coordinator = coordinatorRepository.findByUserEmail(email)
            ?: throw ResourceNotFoundException("No coordinator profile found for the authenticated user")
        return coordinator.toResponse()
    }

    fun updateCoordinator(id: Long, request: UpdateCoordinatorRequest): CoordinatorResponse {
        val coordinator = findCoordinator(id)
        val employeeCode = request.employeeCode.trim()

        if (coordinatorRepository.existsByEmployeeCodeAndIdNot(employeeCode, id)) {
            throw DuplicateResourceException("Employee code already exists")
        }

        coordinator.employeeCode = employeeCode
        coordinator.area = request.area
        coordinator.assignmentDate = request.assignmentDate
        coordinator.educationLevel = request.educationLevel

        return coordinatorRepository.save(coordinator).toResponse()
    }

    private fun findCoordinator(id: Long): Coordinator {
        return coordinatorRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Coordinator with id $id not found") }
    }

    private fun Coordinator.toResponse(): CoordinatorResponse {
        return CoordinatorResponse(
            id = requireNotNull(id),
            userId = requireNotNull(user.id),
            document = user.document,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone,
            birthDate = user.birthDate,
            enabled = user.enabled,
            employeeCode = employeeCode,
            area = area,
            assignmentDate = assignmentDate,
            educationLevel = educationLevel
        )
    }
}