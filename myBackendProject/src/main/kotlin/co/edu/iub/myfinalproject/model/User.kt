package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var documentType: DocumentType,

    @Column(nullable = false, unique = true)
    var document: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var fullName: String,

    @Column(nullable = false)
    var phone: String,

    @Column(nullable = false)
    var birthDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: Gender,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var enabled: Boolean = true,

    @Column(nullable = false)
    var mustChangePassword: Boolean = true,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
)