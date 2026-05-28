package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.ReportLecturerDto;
import com.lowagie.text.pdf.BaseFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервіс для експорту звітів про підвищення кваліфікації у різних форматах.
 * Підтримує генерацію звітів у форматах PDF (використовуючи Thymeleaf та Flying Saucer iText) 
 * та Excel (XLSX, використовуючи Apache POI).
 */
@Service
public class ExportService {

    /**
     * Рушій шаблонів Thymeleaf для рендерингу HTML, який потім перетворюється на PDF.
     */
    private final TemplateEngine templateEngine;

    /**
     * Конструктор для ініціалізації залежностей сервісу експорту.
     * 
     * @param templateEngine рушій шаблонів Thymeleaf
     */
    public ExportService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Генерує PDF-документ зі звітом про викладачів на основі HTML-шаблону.
     * Дані групуються за факультетом та кафедрою.
     * 
     * @param data список DTO об'єктів {@link ReportLecturerDto} з даними для звіту
     * @param level рівень деталізації (наприклад, університет, факультет, кафедра)
     * @param yearFrom початковий рік періоду (включно, може бути null)
     * @param yearTo кінцевий рік періоду (включно, може бути null)
     * @param detailedMode чи включений детальний режим (з розбивкою за дисциплінами)
     * @return масив байтів (byte[]), що представляє згенерований PDF-файл
     * @throws Exception якщо виникає помилка при генерації PDF
     */
    public byte[] generatePdfReport(List<ReportLecturerDto> data, String level, Integer yearFrom, Integer yearTo, boolean detailedMode) throws Exception {
        Context context = new Context();
        
        Map<String, List<ReportLecturerDto>> groupedData = data.stream()
                .collect(Collectors.groupingBy(
                        r -> (r.getFacultyName() != null ? r.getFacultyName() : "") + " / " + (r.getDepartmentName() != null ? r.getDepartmentName() : ""),
                        java.util.LinkedHashMap::new, 
                        Collectors.toList()
                ));
        
        context.setVariable("groupedData", groupedData);
        context.setVariable("level", level);
        context.setVariable("yearFrom", yearFrom);
        context.setVariable("yearTo", yearTo);
        context.setVariable("detailedMode", detailedMode);

        String htmlContent = templateEngine.process("reportPdfTemplate", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            
            // Register custom font to support Cyrillic characters
            String fontPath = getClass().getClassLoader().getResource("fonts/Roboto-Regular.ttf").toExternalForm();
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Генерує Excel-документ (XLSX) зі звітом про підвищення кваліфікації викладачів.
     * Дані групуються за факультетом та кафедрою з відповідним виділенням рядків-заголовків.
     * 
     * @param data список DTO об'єктів {@link ReportLecturerDto} з даними для звіту
     * @param level рівень деталізації (наприклад, університет, факультет, кафедра)
     * @param yearFrom початковий рік періоду (включно, може бути null)
     * @param yearTo кінцевий рік періоду (включно, може бути null)
     * @param detailedMode чи включений детальний режим (впливає на набір колонок у звіті)
     * @return масив байтів (byte[]), що представляє згенерований XLSX-файл
     * @throws Exception якщо виникає помилка при генерації Excel
     */
    public byte[] generateXlsxReport(List<ReportLecturerDto> data, String level, Integer yearFrom, Integer yearTo, boolean detailedMode) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Звіт");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Data Style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setWrapText(true);
            dataStyle.setVerticalAlignment(VerticalAlignment.TOP);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Create Headers
            Row headerRow = sheet.createRow(0);
            String[] headers;
            if (detailedMode) {
                headers = new String[]{"ПІБ", "Посада", "Освіта", "Академічна інформація", "Дисципліна", "Підвищення кваліфікації"};
            } else {
                headers = new String[]{"ПІБ", "Посада", "Освіта", "Академічна інформація", "Підвищення кваліфікації"};
            }
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 256 * 20); // Base width
            }
            sheet.setColumnWidth(2, 256 * 40); // Education
            if (detailedMode) {
                sheet.setColumnWidth(4, 256 * 30); // Discipline
                sheet.setColumnWidth(5, 256 * 50); // Upskilling
            } else {
                sheet.setColumnWidth(4, 256 * 50); // Upskilling
            }

            int rowIdx = 1;
            
            String currentDivision = "";

            for (ReportLecturerDto dto : data) {
                String div = (dto.getFacultyName() != null ? dto.getFacultyName() : "") + " / " + 
                             (dto.getDepartmentName() != null ? dto.getDepartmentName() : "");
                             
                if (!div.equals(currentDivision)) {
                    currentDivision = div;
                    Row divRow = sheet.createRow(rowIdx++);
                    Cell divCell = divRow.createCell(0);
                    divCell.setCellValue("Підрозділ: " + div);
                    
                    CellStyle divStyle = workbook.createCellStyle();
                    Font divFont = workbook.createFont();
                    divFont.setBold(true);
                    divStyle.setFont(divFont);
                    divStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                    divStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    divCell.setCellStyle(divStyle);
                }

                Row row = sheet.createRow(rowIdx++);
                Cell c0 = row.createCell(0); c0.setCellValue(dto.getFullName()); c0.setCellStyle(dataStyle);
                Cell c1 = row.createCell(1); c1.setCellValue(dto.getPosition()); c1.setCellStyle(dataStyle);
                Cell c2 = row.createCell(2); c2.setCellValue(dto.getEducationDetails()); c2.setCellStyle(dataStyle);
                Cell c3 = row.createCell(3); c3.setCellValue(dto.getAcademicInfo()); c3.setCellStyle(dataStyle);
                
                if (detailedMode) {
                    Cell c4 = row.createCell(4); c4.setCellValue(dto.getDisciplineDetails()); c4.setCellStyle(dataStyle);
                    Cell c5 = row.createCell(5); c5.setCellValue(dto.getUpskillingDetails()); c5.setCellStyle(dataStyle);
                } else {
                    Cell c4 = row.createCell(4); c4.setCellValue(dto.getUpskillingDetails()); c4.setCellStyle(dataStyle);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
