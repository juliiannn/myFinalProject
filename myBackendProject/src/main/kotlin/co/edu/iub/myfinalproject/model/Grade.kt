package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(
    name = "grades",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["student_id", "course_subject_id", "period"])
    ]
)
class Grade(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    var student: Student,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_subject_id", nullable = false)
    var courseSubject: CourseSubject,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var period: Period,

    @Column(nullable = false, precision = 3, scale = 1)
    var value: BigDecimal
)