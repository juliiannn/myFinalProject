package co.edu.iub.myfinalproject.model

import jakarta.persistence.*

@Entity
@Table(name = "subjects")
class Subject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false)
    var active: Boolean = true
)