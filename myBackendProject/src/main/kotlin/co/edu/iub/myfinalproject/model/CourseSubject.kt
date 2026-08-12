package co.edu.iub.myfinalproject.model

import jakarta.persistence.*

@Entity
@Table(name = "course_subjects")
class CourseSubject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    var course: Course,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    var subject: Subject,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    var teacher: Teacher,

    @Column(nullable = false)
    var active: Boolean = true
)