package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "rectors")
class Rector(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    var user: User,

    @Column(nullable = false, unique = true)
    var employeeCode: String,

    @Column(nullable = false)
    var appointmentDate: LocalDate,

    @Column(nullable = false)
    var administrativePeriod: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var educationLevel: EducationLevel
)