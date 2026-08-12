package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "attendances",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["student_id", "course_subject_id", "date"])
    ]
)
class Attendance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    var student: Student,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_subject_id", nullable = false)
    var courseSubject: CourseSubject,

    @Column(nullable = false)
    var date: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: AttendanceStatus
)