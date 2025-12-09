package br.com.unifaa.agendamento.service;

import org.springframework.stereotype.Service;

import br.com.unifaa.agendamento.model.Booking;
import br.com.unifaa.agendamento.model.Evaluation;
import br.com.unifaa.agendamento.repository.BookingRepository;
import br.com.unifaa.agendamento.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingReportPdfService {

    private final BookingRepository bookingRepository;
    private final EvaluationRepository evaluationRepository;

    public byte[] generatePdf(Long evaluationId) {

        log.info("Iniciando geração do PDF para evaluationId={}", evaluationId);

        Evaluation ev = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> {
                    log.error("Avaliação não encontrada: {}", evaluationId);
                    return new RuntimeException("Evaluation não encontrada: " + evaluationId);
                });

        List<Booking> bookings = bookingRepository.findByEvaluationIdOrderBySlotStartAsc(evaluationId);
        log.info("Total de bookings encontrados: {}", bookings.size());

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);

            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font subFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font bodyFont = new Font(Font.HELVETICA, 11);

            Paragraph title = new Paragraph("Lista de Presença - " + ev.getCampus().getName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);

            Paragraph subtitle = new Paragraph(ev.getCode(), titleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);

            document.add(title);
            document.add(subtitle);

            Paragraph info = new Paragraph(
                    "Período: " + ev.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                            " até " + ev.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    subFont);
            info.setAlignment(Element.ALIGN_LEFT);
            info.setSpacingAfter(15);
            document.add(info);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 4f, 3f, 4f, 3f });
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            String[] headers = { "Aluno", "Curso", "Horário", "Assinatura" };
            for (String h : headers) {
                PdfPCell hc = new PdfPCell(new Paragraph(h, headerFont));
                hc.setHorizontalAlignment(Element.ALIGN_CENTER);
                hc.setPadding(8);
                hc.setBackgroundColor(new Color(230, 230, 230));
                hc.setBorderWidth(1f);
                table.addCell(hc);
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Booking b : bookings) {
                table.addCell(new PdfPCell(new Paragraph(b.getUser().getFullName(), bodyFont)));
                table.addCell(new PdfPCell(new Paragraph(b.getUser().getCourse().getName(), bodyFont)));

                String horario = b.getSlotStart().format(fmt) + " - " + b.getSlotEnd().format(fmt);
                table.addCell(new PdfPCell(new Paragraph(horario, bodyFont)));

                PdfPCell assinatura = new PdfPCell();
                assinatura.setFixedHeight(28);
                table.addCell(assinatura);
            }

            document.add(table);

            document.close();

            byte[] pdfBytes = out.toByteArray();
            log.info("PDF gerado com sucesso! Tamanho: {} KB", pdfBytes.length / 1024);

            return pdfBytes;

        } catch (Exception e) {
            log.error("Erro ao gerar PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}
