package bank.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bank.common.User;

public class LogsIO {
	// Declare Variables
	private int rowNum;
	@SuppressWarnings("unused")
	private DateTimeFormatter dtf;
	@SuppressWarnings("unused")
	private LocalDateTime now;
	
	private File logFile;
	
	private Workbook workbook;
	private Sheet sheet;
	
	// Default constructor
	public LogsIO() {
		rowNum = 0;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM YYYY - HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("logs");
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue("DATE");
        cell = row.createCell(1);
        cell.setCellValue("ROLE");
        cell = row.createCell(2);
        cell.setCellValue("FULL NAME");
        cell = row.createCell(3);
        cell.setCellValue("DESCRIPTION");
		rowNum++;
		
		String home = System.getProperty("user.home"); 
		logFile = new File("//" + home + "//Documents/" + dtf.format(now)  + "-BankLogs.xlsx");
		logFile.getParentFile().mkdirs(); 
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(logFile)) {
            workbook.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean insertRow(String description, String error, User usr) {
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("mm/dd/yyyy hh:mm:ss"));
		
		sheet.createRow(rowNum).createCell(0).setCellValue(new Date(System.currentTimeMillis()));
		Cell cell = sheet.getRow(rowNum).getCell(0);
		cell.setCellStyle(cellStyle);
		
		if(usr.getIsAdmin())
			sheet.getRow(rowNum).createCell(1).setCellValue("Admin");
		else
			sheet.getRow(rowNum).createCell(1).setCellValue("Admin");
		
		sheet.getRow(rowNum).createCell(2).setCellValue(usr.getFullName());
		sheet.getRow(rowNum++).createCell(3).setCellValue(description);
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
			
		try (FileOutputStream outputStream = new FileOutputStream(logFile)) {
			workbook.write(outputStream);
	        outputStream.close();
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
