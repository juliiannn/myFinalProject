package co.edu.iub.myfinalproject.service

import co.edu.iub.myfinalproject.exception.ResourceNotFoundException
import co.edu.iub.myfinalproject.model.Period
import co.edu.iub.myfinalproject.repository.GradeRepository
import co.edu.iub.myfinalproject.repository.StudentRepository
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter

@Service
class ReportCardService(
    private val studentRepository: StudentRepository,
    private val gradeRepository: GradeRepository,
    private val currentUserService: CurrentUserService
) {
    private val periods = Period.entries.toList()

    fun generateMyReportCard(): ByteArray {
        return generateReportCard(currentUserService.getCurrentStudentId())
    }

    fun generateReportCard(studentId: Long): ByteArray {
        val student = studentRepository
            .findById(studentId)
            .orElseThrow { ResourceNotFoundException("Student with id $studentId not found") }

        val grades = gradeRepository.findByStudentId(studentId)

        // subjectName -> (period -> value)
        val bySubject = grades
            .groupBy { it.courseSubject.subject.name }
            .mapValues { (_, subjectGrades) ->
                subjectGrades.associate { it.period to it.value }
            }

        val document = PDDocument()
        val page = PDPage(PDRectangle.LETTER)
        document.addPage(page)

        val fontBold = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)
        val fontRegular = PDType1Font(Standard14Fonts.FontName.HELVETICA)

        PDPageContentStream(document, page).use { content ->
            var y = 740f
            val marginLeft = 50f

            content.beginText()
            content.setFont(fontBold, 16f)
            content.newLineAtOffset(marginLeft, y)
            content.showText("Boletin de Calificaciones")
            content.endText()
            y -= 30f

            content.beginText()
            content.setFont(fontRegular, 11f)
            content.newLineAtOffset(marginLeft, y)
            content.showText("Estudiante: ${student.user.fullName}")
            content.endText()
            y -= 16f

            content.beginText()
            content.setFont(fontRegular, 11f)
            content.newLineAtOffset(marginLeft, y)
            content.showText("Documento: ${student.user.document}")
            content.endText()
            y -= 16f

            content.beginText()
            content.setFont(fontRegular, 11f)
            content.newLineAtOffset(marginLeft, y)
            content.showText(
                "Fecha de generacion: ${
                    java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                }"
            )
            content.endText()
            y -= 30f

            // Encabezados de tabla
            val colSubject = marginLeft
            val colPeriod1 = 220f
            val colPeriod2 = 280f
            val colPeriod3 = 340f
            val colPeriod4 = 400f
            val colAverage = 460f

            content.beginText()
            content.setFont(fontBold, 10f)
            content.newLineAtOffset(colSubject, y)
            content.showText("Materia")
            content.endText()

            content.beginText()
            content.setFont(fontBold, 10f)
            content.newLineAtOffset(colPeriod1, y)
            content.showText("P1")
            content.endText()

            content.beginText()
            content.setFont(fontBold, 10f)
            content.newLineAtOffset(colPeriod2, y)
            content.showText("P2")
            content.endText()

            content.beginText()
            content.setFont(fontBold, 10f)
            content.newLineAtOffset(colPeriod3, y)
            content.showText("P3")
            content.endText()

            content.beginText()
            content.setFont(fontBold, 10f)
            content.newLineAtOffset(colPeriod4, y)
            content.showText("P4")
            content.endText()

            content.beginText()
            content.setFont(fontBold, 10f)
            content.newLineAtOffset(colAverage, y)
            content.showText("Promedio")
            content.endText()

            y -= 8f
            content.moveTo(marginLeft, y)
            content.lineTo(540f, y)
            content.stroke()
            y -= 14f

            var overallSum = BigDecimal.ZERO
            var overallCount = 0

            for ((subjectName, periodGrades) in bySubject) {
                content.beginText()
                content.setFont(fontRegular, 10f)
                content.newLineAtOffset(colSubject, y)
                content.showText(subjectName)
                content.endText()

                val colByPeriod = mapOf(
                    Period.PERIOD_1 to colPeriod1,
                    Period.PERIOD_2 to colPeriod2,
                    Period.PERIOD_3 to colPeriod3,
                    Period.PERIOD_4 to colPeriod4
                )

                for (period in periods) {
                    val value = periodGrades[period]
                    if (value != null) {
                        content.beginText()
                        content.setFont(fontRegular, 10f)
                        content.newLineAtOffset(requireNotNull(colByPeriod[period]), y)
                        content.showText(value.toPlainString())
                        content.endText()
                        overallSum = overallSum.add(value)
                        overallCount++
                    }
                }

                val subjectAverage = if (periodGrades.isNotEmpty()) {
                    periodGrades.values
                        .fold(BigDecimal.ZERO) { acc, v -> acc + v }
                        .divide(BigDecimal(periodGrades.size), 2, RoundingMode.HALF_UP)
                } else {
                    BigDecimal.ZERO
                }

                content.beginText()
                content.setFont(fontBold, 10f)
                content.newLineAtOffset(colAverage, y)
                content.showText(subjectAverage.toPlainString())
                content.endText()

                y -= 18f
            }

            y -= 10f
            content.moveTo(marginLeft, y)
            content.lineTo(540f, y)
            content.stroke()
            y -= 20f

            val overallAverage = if (overallCount > 0) {
                overallSum.divide(BigDecimal(overallCount), 2, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO
            }

            content.beginText()
            content.setFont(fontBold, 12f)
            content.newLineAtOffset(marginLeft, y)
            content.showText("Promedio general: ${overallAverage.toPlainString()}")
            content.endText()
        }

        val output = ByteArrayOutputStream()
        document.save(output)
        document.close()
        return output.toByteArray()
    }
}