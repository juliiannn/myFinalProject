package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Subject
import org.springframework.data.jpa.repository.JpaRepository

interface SubjectRepository : JpaRepository<Subject, Long> {
    fun existsByNameIgnoreCase(name: String): Boolean
    fun existsByNameIgnoreCaseAndIdNot(name: String, id: Long): Boolean
}