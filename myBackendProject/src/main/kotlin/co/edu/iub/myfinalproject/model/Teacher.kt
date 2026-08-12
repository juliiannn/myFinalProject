package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "teachers")
class Teacher(

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
    var hireDate: LocalDate,

    @Column(nullable = false)
    var profession: String,

    @Column(nullable = false)
    var specialty: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var educationLevel: EducationLevel,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var contractType: ContractType
)