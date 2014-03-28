package dep.excel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import dep.dao.DatabaseDao;
import dep.model.EstimateEntity;
import dep.model.HistoricalTrendEntity;
import dep.model.SCGHistoricalTrendEntity;
import dep.model.SCGLogEntity;
 
public class P6ExcelView extends AbstractExcelView{
	
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
    Cell cell;
    CreationHelper createHelper;
 
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=p6_metadata.xls");
 
		List<HistoricalTrendEntity> list = (List<HistoricalTrendEntity>) model.get("P6List");
		
        
		//create a wordsheet
		HSSFSheet sheet = workbook.createSheet("SCG Sheet");
 
        // Create a row and put some cells in it. Rows are 0 based.
        Row row1 = sheet.createRow((short) 1);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//      font.setColor(HSSFColor.RED.index);
        headerStyle.setFont(font);
        
        CellStyle style = workbook.createCellStyle();
//        style.setFillForegroundColor(HSSFColor.RED.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.YELLOW.index);

        createHelper = workbook.getCreationHelper();

        
		HSSFRow header = sheet.createRow(0);
		header.setHeightInPoints(50);

		cell = header.createCell(0);	cell.setCellStyle(headerStyle);	cell.setCellValue("Project ID");
		cell = header.createCell(1);	cell.setCellStyle(headerStyle);	cell.setCellValue("Project Name");
		cell = header.createCell(2);	cell.setCellStyle(headerStyle);	cell.setCellValue("Operating Bureau");	
		cell = header.createCell(3);	cell.setCellStyle(headerStyle);	cell.setCellValue("Bureau");	
		cell = header.createCell(4);	cell.setCellStyle(headerStyle);	cell.setCellValue("Portfolio Manager");	
		cell = header.createCell(5);	cell.setCellStyle(headerStyle);	cell.setCellValue("Accountable Design Manager");	
		cell = header.createCell(6);	cell.setCellStyle(headerStyle);	cell.setCellValue("Accountable Construction Manager");	
		cell = header.createCell(7);	cell.setCellStyle(headerStyle);	cell.setCellValue("Master Program");	
		cell = header.createCell(8);	cell.setCellStyle(headerStyle);	cell.setCellValue("Data Date");		
		cell = header.createCell(9);	cell.setCellStyle(headerStyle);	cell.setCellValue("Status");	
		cell = header.createCell(10);	cell.setCellStyle(headerStyle);	cell.setCellValue("Stage");	
	
 
		int rowNum = 1;
		for (HistoricalTrendEntity entity : list) {
			//create the row data
			HSSFRow row = sheet.createRow(rowNum++);
			
			cell = row.createCell(0);	cell.setCellValue(entity.getProjectId());
			cell = row.createCell(1);	cell.setCellValue(entity.getProjectName());
			cell = row.createCell(2);	cell.setCellValue(entity.getOperatingBureau());
			cell = row.createCell(3);	cell.setCellValue(entity.getBureau());
			cell = row.createCell(4);	cell.setCellValue(entity.getPortfolioManager());
			cell = row.createCell(5);	if(entity.getAccountableDesignManager() != null) cell.setCellValue(entity.getAccountableDesignManager());
			cell = row.createCell(6);	if(entity.getAccountableConstructionManager() != null) cell.setCellValue(entity.getAccountableConstructionManager());
			cell = row.createCell(7);	cell.setCellValue(entity.getMasterProgram());
			cell = row.createCell(8);	cell.setCellValue(entity.getDataDate());
			cell = row.createCell(9);	cell.setCellValue(entity.getStatus());
			cell = row.createCell(10);	cell.setCellValue(entity.getStage());
         }
		
	}
	
}