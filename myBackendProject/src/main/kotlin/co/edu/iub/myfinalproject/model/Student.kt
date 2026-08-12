package co.edu.iub.myfinalproject.model

import jakarta.persistence.*

@Entity
@Table(name = "students")
class Student(

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

    @Column(nullable = false)
    var guardianName: String,

    @Column(nullable = false)
    var guardianPhone: String,

    @Column(nullable = false)
    var guardianEmail: String
)