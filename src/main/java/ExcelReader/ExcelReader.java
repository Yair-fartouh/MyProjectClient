package ExcelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
    public Location getRandomLocation() {
        Location location = new Location();
        String path = System.getProperty("user.dir");
        String excelFilePath = path + "\\src\\main\\java\\ExcelReader\\report.xlsx";
        try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Random random = new Random();
            int rowIndex = random.nextInt(1412) + 2;
            Row row = sheet.getRow(rowIndex);
            double latitude = row.getCell(0).getNumericCellValue();
            double longitude = row.getCell(1).getNumericCellValue();
            //System.out.println("random: " + rowIndex + "Latitude: " + latitude + ", Horizon Line: " + longitude);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return location;
    }
}

