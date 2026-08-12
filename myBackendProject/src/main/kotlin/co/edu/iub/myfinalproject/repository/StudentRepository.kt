package co.edu.iub.myfinalproject.repository

import co.edu.iub.myfinalproject.model.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long> {
    fun findByUserEmail(email: String): Student?
}