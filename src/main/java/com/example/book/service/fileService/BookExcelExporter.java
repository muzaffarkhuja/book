package com.example.book.service.fileService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.book.dto.book.BookDto;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class BookExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<BookDto> list;
    @Autowired
    private AmazonS3 s3Client;
    @Value("${application.bucket.name}")
    private String bucketName;

    public BookExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    public void setData(List<BookDto> list){
        this.list = list;
    }

    private void  writeHeaderLine() {
        sheet = workbook.createSheet("Books");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Id", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Author", style);
        createCell(row, 3, "Library", style);
        createCell(row, 4, "Size", style);
        createCell(row, 5, "Theme", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(){
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (BookDto bookDto : list) {
            Row row = sheet.createRow(rowCount++);

            createCell(row, 0, bookDto.getId(), style);
            createCell(row, 1, bookDto.getName(), style);
            createCell(row, 2, bookDto.getAuthor().getFullName(), style);
            createCell(row, 3, bookDto.getLibrary().getName(), style);
            createCell(row, 4, bookDto.getSize(), style);
            createCell(row, 5, bookDto.getTheme(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public String export(String fileName) throws IOException {
        writeHeaderLine();
        writeDataLines();

        File path = new File(FileSyncJob.localDirectory);
        if(!path.exists()) path.mkdirs();
        File file = File.createTempFile("file", ".xlsx", path);
        FileOutputStream outputStream = new FileOutputStream(file);

        workbook.write(outputStream);
        outputStream.close();
        file.deleteOnExit();

        s3Client.putObject(new PutObjectRequest(bucketName, fileName + ".xlsx", file));

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName + ".xlsx")
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

}
