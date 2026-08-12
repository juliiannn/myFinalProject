package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Rector
import org.springframework.data.jpa.repository.JpaRepository

interface RectorRepository : JpaRepository<Rector, Long> {
    fun existsByEmployeeCode(employeeCode: String): Boolean
    fun existsByEmployeeCodeAndIdNot(employeeCode: String, id: Long): Boolean
    fun countByUserEnabledTrue(): Long
    fun findByUserEmail(email: String): Rector?
}