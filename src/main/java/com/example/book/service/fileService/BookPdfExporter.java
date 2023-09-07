package com.example.book.service.fileService;

import com.example.book.dto.book.BookDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class BookPdfExporter {

    private Document document;
    private List<BookDto> list;
    private PdfPTable table;
    private PdfPCell cell;

    public BookPdfExporter(List<BookDto> list, HttpServletResponse response) throws IOException {
        this.list = list;
        document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new int[] { 3, 3, 3, 3, 3, 3 });
        table.setSpacingBefore(5);

        cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);
    }

    private void writeHeaderLine(){
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);

        Paragraph paragraph = new Paragraph("List Of Books", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Author", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Library", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Size", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Theme", font));
        table.addCell(cell);
    }

    private void writeDataLines() {
        for (BookDto bookDto : list) {
            table.addCell(bookDto.getId().toString());
            table.addCell(bookDto.getName());
            table.addCell(bookDto.getAuthor().getFullName());
            table.addCell(bookDto.getLibrary().getName());
            table.addCell(bookDto.getSize().toString());
            table.addCell(bookDto.getTheme());
        }

        document.add(table);
        document.close();
    }

    public void export() {
        writeHeaderLine();
        writeDataLines();
    }

}
