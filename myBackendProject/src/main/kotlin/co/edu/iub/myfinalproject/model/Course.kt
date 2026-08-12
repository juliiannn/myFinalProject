package co.edu.iub.myfinalproject.model

import jakarta.persistence.*

@Entity
@Table(name = "courses")
class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var grade: String,

    @Column(nullable = false)
    var classroom: String,

    @Column(nullable = false)
    var schoolYear: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_teacher_id")
    var directorTeacher: Teacher?,

    @Column(nullable = false)
    var active: Boolean = true
)