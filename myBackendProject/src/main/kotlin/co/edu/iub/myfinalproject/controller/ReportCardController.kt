package co.edu.iub.myfinalproject.controller

import co.edu.iub.myfinalproject.service.ReportCardService
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/report-cards")
class ReportCardController(
    private val reportCardService: ReportCardService
) {
    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    fun getMyReportCard(): ResponseEntity<ByteArray> {
        val pdf = reportCardService.generateMyReportCard()
        return buildPdfResponse(pdf, "boletin.pdf")
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECTOR','COORDINATOR')")
    fun getReportCard(
        @PathVariable studentId: Long
    ): ResponseEntity<ByteArray> {
        val pdf = reportCardService.generateReportCard(studentId)
        return buildPdfResponse(pdf, "boletin_$studentId.pdf")
    }

    private fun buildPdfResponse(pdf: ByteArray, filename: String): ResponseEntity<ByteArray> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_PDF
        headers.contentDisposition = ContentDisposition
            .attachment()
            .filename(filename)
            .build()
        return ResponseEntity.ok().headers(headers).body(pdf)
    }
}