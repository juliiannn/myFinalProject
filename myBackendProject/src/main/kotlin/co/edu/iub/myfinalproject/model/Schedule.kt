package co.edu.iub.myfinalproject.model

import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "schedules")
class Schedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_subject_id", nullable = false)
    var courseSubject: CourseSubject,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var dayOfWeek: DayOfWeek,

    @Column(nullable = false)
    var startTime: LocalTime,

    @Column(nullable = false)
    var endTime: LocalTime
)