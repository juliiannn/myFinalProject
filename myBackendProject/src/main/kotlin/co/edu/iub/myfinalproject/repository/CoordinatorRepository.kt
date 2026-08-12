package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Coordinator
import org.springframework.data.jpa.repository.JpaRepository

interface CoordinatorRepository : JpaRepository<Coordinator, Long> {
    fun existsByEmployeeCode(employeeCode: String): Boolean
    fun existsByEmployeeCodeAndIdNot(employeeCode: String, id: Long): Boolean
    fun findByUserEmail(email: String): Coordinator?
}