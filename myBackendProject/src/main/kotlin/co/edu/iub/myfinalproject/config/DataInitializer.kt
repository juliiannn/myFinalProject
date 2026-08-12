package co.edu.iub.myfinalproject.config

import co.edu.iub.myfinalproject.model.DocumentType
import co.edu.iub.myfinalproject.model.Gender
import co.edu.iub.myfinalproject.model.User
import co.edu.iub.myfinalproject.model.UserRole
import co.edu.iub.myfinalproject.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {

        if (!userRepository.existsByEmail("admin@colegio.com")) {

            val admin = User(
                documentType = DocumentType.CC,
                document = "1000000000",
                email = "admin@colegio.com",
                fullName = "Administrador del Sistema",
                phone = "3000000000",
                birthDate = LocalDate.of(1990, 1, 1),
                gender = Gender.MALE,
                address = "Institución Educativa",
                password = passwordEncoder.encode("Admin123.")!!,
                role = UserRole.ADMIN,
                mustChangePassword = false
            )

            userRepository.save(admin)

            println("==========================================")
            println("Administrador creado correctamente")
            println("Correo: admin@colegio.com")
            println("Contraseña: Admin123.")
            println("==========================================")
        }
    }
}