package Api.Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * Verilen JSON listesini bir Excel dosyasına yazar.
     * @param filePath Oluşturulacak Excel dosyasının yolu.
     * @param sheetName Excel sayfasının adı.
     * @param dataList JSON objelerinden oluşan liste.
     */
    public static void writeDataToExcel(String filePath, String sheetName, List<Map<String, Object>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("Yazılacak veri bulunamadı: " + sheetName);
            return;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Başlık satırını oluştur
            Map<String, Object> firstItem = dataList.get(0);
            Row headerRow = sheet.createRow(0);
            int cellNum = 0;
            for (String key : firstItem.keySet()) {
                Cell cell = headerRow.createCell(cellNum++);
                cell.setCellValue(key);
            }

            // Veri satırlarını oluştur
            int rowNum = 1;
            for (Map<String, Object> item : dataList) {
                Row row = sheet.createRow(rowNum++);
                cellNum = 0;
                for (String key : firstItem.keySet()) {
                    Cell cell = row.createCell(cellNum++);
                    Object value = item.get(key);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }

            // Dosyayı yaz
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            System.out.println("✅ Excel dosyası başarıyla oluşturuldu/güncellendi: " + filePath);

        } catch (IOException e) {
            System.err.println("Excel dosyası yazılırken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}