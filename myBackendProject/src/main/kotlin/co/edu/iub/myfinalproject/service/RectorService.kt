package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.rector.CreateRectorRequest
import co.edu.iub.myfinalproject.dto.request.rector.UpdateRectorRequest
import co.edu.iub.myfinalproject.dto.response.rector.RectorResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.InvalidRequestException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Rector
import co.edu.iub.myfinalproject.model.UserRole
import co.edu.iub.myfinalproject.repository.RectorRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RectorService(
    private val rectorRepository: RectorRepository,
    private val userService: UserService,
    private val currentUserService: CurrentUserService
) {
    @Transactional
    fun createRector(request: CreateRectorRequest): RectorResponse {
        if (rectorRepository.countByUserEnabledTrue() > 0) {
            throw InvalidRequestException(
                "There is already an active rector; deactivate the current one before assigning a new one"
            )
        }

        val employeeCode = request.employeeCode.trim()
        if (rectorRepository.existsByEmployeeCode(employeeCode)) {
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
            role = UserRole.RECTOR
        )

        val rector = Rector(
            user = user,
            employeeCode = employeeCode,
            appointmentDate = request.appointmentDate,
            administrativePeriod = request.administrativePeriod.trim(),
            educationLevel = request.educationLevel
        )

        return rectorRepository.save(rector).toResponse()
    }

    fun getAllRectors(): List<RectorResponse> {
        return rectorRepository.findAll().map { it.toResponse() }
    }

    fun getRectorById(id: Long): RectorResponse {
        return findRector(id).toResponse()
    }

    fun getMyProfile(): RectorResponse {
        val email = currentUserService.getCurrentUserEmail()
        val rector = rectorRepository.findByUserEmail(email)
            ?: throw ResourceNotFoundException("No rector profile found for the authenticated user")
        return rector.toResponse()
    }

    fun updateRector(id: Long, request: UpdateRectorRequest): RectorResponse {
        val rector = findRector(id)
        val employeeCode = request.employeeCode.trim()

        if (rectorRepository.existsByEmployeeCodeAndIdNot(employeeCode, id)) {
            throw DuplicateResourceException("Employee code already exists")
        }

        rector.employeeCode = employeeCode
        rector.appointmentDate = request.appointmentDate
        rector.administrativePeriod = request.administrativePeriod.trim()
        rector.educationLevel = request.educationLevel

        return rectorRepository.save(rector).toResponse()
    }

    private fun findRector(id: Long): Rector {
        return rectorRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Rector with id $id not found") }
    }

    private fun Rector.toResponse(): RectorResponse {
        return RectorResponse(
            id = requireNotNull(id),
            userId = requireNotNull(user.id),
            document = user.document,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone,
            birthDate = user.birthDate,
            enabled = user.enabled,
            employeeCode = employeeCode,
            appointmentDate = appointmentDate,
            administrativePeriod = administrativePeriod,
            educationLevel = educationLevel
        )
    }
}