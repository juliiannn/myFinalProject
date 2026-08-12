package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Teacher
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository : JpaRepository<Teacher, Long> {
    fun existsByEmployeeCode(employeeCode: String): Boolean
    fun existsByEmployeeCodeAndIdNot(employeeCode: String, id: Long): Boolean
    fun findByUserEmail(email: String): Teacher?
}