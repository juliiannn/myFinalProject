package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.dto.request.subject.CreateSubjectRequest
import co.edu.iub.myfinalproject.dto.response.subject.SubjectResponse
import co.edu.iub.myfinalproject.exception.DuplicateResourceException
import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Subject
import co.edu.iub.myfinalproject.repository.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository
) {
    fun createSubject(request: CreateSubjectRequest): SubjectResponse {
        val name = request.name.trim()
        if (subjectRepository.existsByNameIgnoreCase(name)) {
            throw DuplicateResourceException("Subject already exists")
        }
        val subject = Subject(name = name)
        return subjectRepository.save(subject).toResponse()
    }

    fun updateSubject(id: Long, request: CreateSubjectRequest): SubjectResponse {
        val subject = findSubject(id)
        val name = request.name.trim()
        if (subjectRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw DuplicateResourceException("Subject already exists")
        }
        subject.name = name
        return subjectRepository.save(subject).toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        return findSubject(id).toResponse()
    }

    fun deactivateSubject(id: Long): SubjectResponse {
        val subject = findSubject(id)
        subject.active = false
        return subjectRepository.save(subject).toResponse()
    }

    fun activateSubject(id: Long): SubjectResponse {
        val subject = findSubject(id)
        subject.active = true
        return subjectRepository.save(subject).toResponse()
    }

    private fun findSubject(id: Long): Subject {
        return subjectRepository
            .findById(id)
            .orElseThrow {
                ResourceNotFoundException("Subject with id $id not found")
            }
    }

    private fun Subject.toResponse(): SubjectResponse {
        return SubjectResponse(
            id = requireNotNull(id),
            name = name,
            active = active
        )
    }
}