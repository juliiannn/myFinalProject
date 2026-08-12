package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "enrollments")
class Enrollment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "student_id",
        nullable = false
    )
    var student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "course_id",
        nullable = false
    )
    var course: Course,

    @Column(nullable = false)
    var enrollmentDate: LocalDate = LocalDate.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: EnrollmentStatus = EnrollmentStatus.ACTIVE
)