package dep.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import dep.dao.DatabaseDao;
import dep.model.SCGHistoricalTrendEntity;
import dep.model.SCGLogEntity;
 
public class SCGExcelView extends AbstractExcelView{
	
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	Map<String, SCGLogEntity> previousMap;
    Cell cell;
    CreationHelper createHelper;
	DatabaseDao dao;
 
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=SCG-Log.xls");
 
		Map<String,SCGHistoricalTrendEntity> map = (Map<String,SCGHistoricalTrendEntity>) model.get("SCGMap");
		dao = (DatabaseDao)model.get("dao");
		Long month = (Long) model.get("month");
		Long previousMonth;
		
		if(month%100 == 1)
			previousMonth = (month/100 - 1) * 100 + 12;
		else 
			previousMonth = month - 1;
		
		List<SCGLogEntity> previousList = dao.getSCGLog(previousMonth.toString());
	 	previousMap = new HashMap<String, SCGLogEntity>();
		
		for(SCGLogEntity entity : previousList)				
			previousMap.put(entity.getProjectId(), entity);
			
		
		SCGComparator bvc =  new SCGComparator(map);
        TreeMap<String,SCGHistoricalTrendEntity> sortedMap = new TreeMap<String,SCGHistoricalTrendEntity>(bvc);
        
        sortedMap.putAll(map);
        
        
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
		cell = header.createCell(2);	cell.setCellStyle(headerStyle);	cell.setCellValue("Assistant Commissioner");	
		cell = header.createCell(3);	cell.setCellStyle(headerStyle);	cell.setCellValue("Portfolio Manager");	
		cell = header.createCell(4);	cell.setCellStyle(headerStyle);	cell.setCellValue("Accountable Design Manager");	
		cell = header.createCell(5);	cell.setCellStyle(headerStyle);	cell.setCellValue("Accountable Construction Manager");	
		cell = header.createCell(6);	cell.setCellStyle(headerStyle);	cell.setCellValue("SCG Lead");
		cell = header.createCell(7);	cell.setCellStyle(headerStyle);	cell.setCellValue("Project Controls Lead");	
		cell = header.createCell(8);	cell.setCellStyle(headerStyle);	cell.setCellValue("Permits Lead");	
		cell = header.createCell(9);	cell.setCellStyle(headerStyle);	cell.setCellValue("Sustainability Manager");	
		cell = header.createCell(10);	cell.setCellStyle(headerStyle);	cell.setCellValue("Cost Estimating Manager");	

		
		cell = header.createCell(11);	cell.setCellStyle(headerStyle);	cell.setCellValue("Operating Bureau");	
		cell = header.createCell(12);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Status");	
		cell = header.createCell(13);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Stage");	
		cell = header.createCell(14);	cell.setCellStyle(headerStyle);	cell.setCellValue("Consent");	
		cell = header.createCell(15);	cell.setCellStyle(headerStyle);	cell.setCellValue("DFD");	
		cell = header.createCell(16);	cell.setCellStyle(headerStyle);	cell.setCellValue("Program");	
		cell = header.createCell(17);	cell.setCellStyle(headerStyle);	cell.setCellValue("BODR");	
		cell = header.createCell(18);	cell.setCellStyle(headerStyle);	cell.setCellValue("30%");	
		cell = header.createCell(19);	cell.setCellStyle(headerStyle);	cell.setCellValue("60%");	
		cell = header.createCell(20);	cell.setCellStyle(headerStyle);	cell.setCellValue("90%");	
		cell = header.createCell(21);	cell.setCellStyle(headerStyle);	cell.setCellValue("100%");	
		cell = header.createCell(22);	cell.setCellStyle(headerStyle);	cell.setCellValue("NTP");	
		cell = header.createCell(23);	cell.setCellStyle(headerStyle);	cell.setCellValue("Data Date");	
		cell = header.createCell(24);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Baseline Date");	
		cell = header.createCell(25);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Substantial Completion");	
		cell = header.createCell(26);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Final Completion");	
		cell = header.createCell(27);	cell.setCellStyle(headerStyle);	cell.setCellValue("SC Current Variance");	
		cell = header.createCell(28);	cell.setCellStyle(headerStyle);	cell.setCellValue("Period Gain/Loss");	
		cell = header.createCell(29);	cell.setCellStyle(headerStyle);	cell.setCellValue("Original Construction Contract Amount");	
		cell = header.createCell(30);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Construction Contract Amount");		
//		cell = header.createCell(31);	cell.setCellStyle(headerStyle);	cell.setCellValue("SCG Support");	
		cell = header.createCell(31);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Comments/ Issue");	
		cell = header.createCell(32);	cell.setCellStyle(headerStyle);	cell.setCellValue("Comptr. Claim");	
 
		int rowNum = 1;
		for (Map.Entry<String, SCGHistoricalTrendEntity> entry : sortedMap.entrySet()) {
			//create the row data
			HSSFRow row = sheet.createRow(rowNum++);
			
			cell = row.createCell(0);	setCellValues(entry.getValue(), entry.getValue().getProjectId(), style, ""); 
			cell = row.createCell(1);	setCellValues(entry.getValue(), entry.getValue().getProjectName(), style, "");
			cell = row.createCell(2);	
			if(entry.getValue().getOperatingBureau().equals("BWT"))
				setCellValues(entry.getValue(), "Donnelly", style, "");
			else if(entry.getValue().getOperatingBureau().equals("BWS") || entry.getValue().getOperatingBureau().equals("BWSO"))
				setCellValues(entry.getValue(), "Borsykowsky", style, "");
			else
				setCellValues(entry.getValue(), "", style, "");

			cell = row.createCell(3);	setCellValues(entry.getValue(), entry.getValue().getPortfolioManager(), style, "");
			cell = row.createCell(4);	setCellValues(entry.getValue(), entry.getValue().getDesignAccountableManager(), style, "");
			cell = row.createCell(5);	setCellValues(entry.getValue(), entry.getValue().getConstructionAccountableManager(), style, "");
			cell = row.createCell(6);	setCellValues(entry.getValue(), entry.getValue().getSCGLead(), style, "");
			cell = row.createCell(7);	setCellValues(entry.getValue(), entry.getValue().getProjectControlsLead(), style, "");
			cell = row.createCell(8);	setCellValues(entry.getValue(), entry.getValue().getPermitsLead(), style, "");
			cell = row.createCell(9);	setCellValues(entry.getValue(), entry.getValue().getSustainabilityManager(), style, "");
			cell = row.createCell(10);	setCellValues(entry.getValue(), entry.getValue().getCostEstimatingManager(), style, "");
			
			cell = row.createCell(11);	setCellValues(entry.getValue(), entry.getValue().getOperatingBureau(), style, "");
			cell = row.createCell(12);	setCellValues(entry.getValue(), entry.getValue().getCurrentStatus(), style, "");
			cell = row.createCell(13);	setCellValues(entry.getValue(), entry.getValue().getCurrentStage(), style, "");
			cell = row.createCell(14);	setCellValues(entry.getValue(), entry.getValue().getConsent(), style, "");
			cell = row.createCell(15);	setCellValues(entry.getValue(), entry.getValue().getDFD(), style, "");
			cell = row.createCell(16);	setCellValues(entry.getValue(), entry.getValue().getMasterProgramOld(), style, "");

			cell = row.createCell(17);	setCellValues(entry.getValue(), entry.getValue().getBODR(), workbook.createCellStyle(), "date");
			cell = row.createCell(18);	setCellValues(entry.getValue(), entry.getValue().getDesign30(), workbook.createCellStyle(), "date");
			cell = row.createCell(19);	setCellValues(entry.getValue(), entry.getValue().getDesign60(), workbook.createCellStyle(), "date");
			cell = row.createCell(20);	setCellValues(entry.getValue(), entry.getValue().getDesign90(), workbook.createCellStyle(), "date");
			cell = row.createCell(21);	setCellValues(entry.getValue(), entry.getValue().getFinalDesignCompleted(), workbook.createCellStyle(), "date");
			cell = row.createCell(22);	setCellValues(entry.getValue(), entry.getValue().getCurrentNTP(), workbook.createCellStyle(), "date");
			cell = row.createCell(23);	setCellValues(entry.getValue(), entry.getValue().getDataDate(), workbook.createCellStyle(), "date");
			cell = row.createCell(24);	setCellValues(entry.getValue(), entry.getValue().getBaselineSC(), workbook.createCellStyle(), "date");
			cell = row.createCell(25);	setCellValues(entry.getValue(), entry.getValue().getCurrentSC(), workbook.createCellStyle(), "date");
			cell = row.createCell(26);	setCellValues(entry.getValue(), entry.getValue().getCurrentFC(), workbook.createCellStyle(), "date");

			cell = row.createCell(27);	if(previousMap.get(entry.getValue().getProjectId()) == null)	cell.setCellStyle(style);
			if(entry.getValue().getBaselineSC() != null && !entry.getValue().getBaselineSC().equals("") && entry.getValue().getCurrentSC() != null && !entry.getValue().getCurrentSC().equals(""))
				cell.setCellValue(Days.daysBetween(dateFormatter.parseDateTime(entry.getValue().getCurrentSC()), dateFormatter.parseDateTime(entry.getValue().getBaselineSC())).getDays());
			cell = row.createCell(28);	if(previousMap.get(entry.getValue().getProjectId()) == null)	cell.setCellStyle(style);
			if(entry.getValue().getSlipGain() != null)
				cell.setCellValue(entry.getValue().getSlipGain());
			cell = row.createCell(29);	setCellValues(entry.getValue(), entry.getValue().getOrigConstructionContractAmount(), workbook.createCellStyle(), "currency");
			cell = row.createCell(30);	setCellValues(entry.getValue(), entry.getValue().getCurrentConstructionContractAmount(), workbook.createCellStyle(), "currency");			

//			cell = row.createCell(31);	setCellValues(entry.getValue(), entry.getValue().getSCGSupport(), style, "");
			cell = row.createCell(31);	setCellValues(entry.getValue(), entry.getValue().getComments(), style, "");
			cell = row.createCell(32);	setCellValues(entry.getValue(), entry.getValue().getClaim(), style, "");
         }
		
	}
	
	void setCellValues(SCGHistoricalTrendEntity entity, Object value, CellStyle style, String type)
	{
		//date cell
		if(type.equals("date"))
		{
			if(value != null && !value.equals("")) 
				cell.setCellValue(dateFormatter.parseDateTime((String)value).toDate());
			
			//If this is a date cell and is in a new added project row.
			if(previousMap.get(entity.getProjectId()) == null)	
			{
		        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        	style.setFillForegroundColor(HSSFColor.YELLOW.index);
			}
			
			//Format this cell as a date.
	        style.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
			cell.setCellStyle(style);
		}
		//currency cell
		else if(type.equals("currency"))
		{
			if(value != null && !value.equals("")) 
				cell.setCellValue((Long)value);
			
			//If this is a cell in a new added project row.
			if(previousMap.get(entity.getProjectId()) == null)	
			{
		        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        	style.setFillForegroundColor(HSSFColor.YELLOW.index);
			}
			
			//Format this cell as currency.
	        style.setDataFormat((short)8); //8 = "($#,##0.00_);[Red]($#,##0.00)"
			cell.setCellStyle(style);
		}		
		else //text cell
			cell.setCellValue((String)value);
			
		//If this cell is in a new added project row.
		if(previousMap.get(entity.getProjectId()) == null)	
			cell.setCellStyle(style);
	}
}