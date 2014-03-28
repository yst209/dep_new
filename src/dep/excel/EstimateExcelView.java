package dep.excel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
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
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import dep.dao.DatabaseDao;
import dep.model.EstimateEntity;
import dep.model.SCGHistoricalTrendEntity;
import dep.model.SCGLogEntity;
 
public class EstimateExcelView extends AbstractExcelView{
	
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Cell cell;
    CreationHelper createHelper;
 
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=cost_estimate.xls");
 
		List<EstimateEntity> list = (List<EstimateEntity>) model.get("estimateList");


//		Long month = (Long) model.get("month");
//		Long previousMonth;
//		
//		if(month%100 == 1)
//			previousMonth = (month/100 - 1) * 100 + 12;
//		else 
//			previousMonth = month - 1;
//		
//		DatabaseDao dao = new DatabaseDao();
//		dao.setDataSource();
//		List<SCGLogEntity> previousList = dao.getSCGLog(previousMonth.toString());
//	 	previousMap = new HashMap<String, SCGLogEntity>();
//		
//		for(SCGLogEntity entity : previousList)				
//			previousMap.put(entity.getProjectId(), entity);
//			
//		
//		SCGComparator bvc =  new SCGComparator(map);
//        TreeMap<String,SCGHistoricalTrendEntity> sortedMap = new TreeMap(bvc);
//        
//        sortedMap.putAll(map);
        
		sortProjectList(list);
        
		//create a wordsheet
		HSSFSheet sheet = workbook.createSheet("Sheet 1");
 
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
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        style.setFillForegroundColor(HSSFColor.YELLOW.index);

        createHelper = workbook.getCreationHelper();

        
		HSSFRow header = sheet.createRow(0);
		header.setHeightInPoints(50);

		cell = header.createCell(0);	cell.setCellStyle(headerStyle);	cell.setCellValue("Project ID");
		cell = header.createCell(1);	cell.setCellStyle(headerStyle);	cell.setCellValue("Project Name");
		cell = header.createCell(2);	cell.setCellStyle(headerStyle);	cell.setCellValue("Status");	
		cell = header.createCell(3);	cell.setCellStyle(headerStyle);	cell.setCellValue("Plan Year");	
		cell = header.createCell(4);	cell.setCellStyle(headerStyle);	cell.setCellValue("Budget");	
		cell = header.createCell(5);	cell.setCellStyle(headerStyle);	cell.setCellValue("Stage");	
		cell = header.createCell(6);	cell.setCellStyle(headerStyle);	cell.setCellValue("FP");	
		cell = header.createCell(7);	cell.setCellStyle(headerStyle);	cell.setCellValue("BODR");	
		cell = header.createCell(8);	cell.setCellStyle(headerStyle);	cell.setCellValue("30%");	
		cell = header.createCell(9);	cell.setCellStyle(headerStyle);	cell.setCellValue("60%");	
		cell = header.createCell(10);	cell.setCellStyle(headerStyle);	cell.setCellValue("90%");	
		cell = header.createCell(11);	cell.setCellStyle(headerStyle);	cell.setCellValue("100%");	
		cell = header.createCell(12);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Milestone");	
		cell = header.createCell(13);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Milestone Date");	
		cell = header.createCell(14);	cell.setCellStyle(headerStyle);	cell.setCellValue("Date Received by CE Team");	
		cell = header.createCell(15);	cell.setCellStyle(headerStyle);	cell.setCellValue("Date Completed");	
		cell = header.createCell(16);	cell.setCellStyle(headerStyle);	cell.setCellValue("Current Estimate");	
		cell = header.createCell(17);	cell.setCellStyle(headerStyle);	cell.setCellValue("Lastest Submitted Estimate");	
		cell = header.createCell(18);	cell.setCellStyle(headerStyle);	cell.setCellValue("Reconciled");	
		cell = header.createCell(19);	cell.setCellStyle(headerStyle);	cell.setCellValue("Next Milestone");	
		cell = header.createCell(20);	cell.setCellStyle(headerStyle);	cell.setCellValue("Due Date");	
		cell = header.createCell(21);	cell.setCellStyle(headerStyle);	cell.setCellValue("Notes");	
		cell = header.createCell(22);	cell.setCellStyle(headerStyle);	cell.setCellValue("Accountable Design Manager");
	
 
		int rowNum = 1;
		for (EstimateEntity entity : list) {
			//create the row data
			HSSFRow row = sheet.createRow(rowNum++);
			
			cell = row.createCell(0);	setCellValues(entity.getProjectId(), style, "");
			cell = row.createCell(1);	setCellValues(entity.getProjectName(), style, "");
			cell = row.createCell(2);	setCellValues(entity.getStatus(), style, "");
			cell = row.createCell(3);	if(entity.getPlanYear() != null) setCellValues(entity.getPlanYear().toString(), style, "");
			cell = row.createCell(4);	setCellValues(entity.getBudget().replaceAll(",", "").replaceAll("\\.00", ""), workbook.createCellStyle(), "currency");
			cell = row.createCell(5);	setCellValues(entity.getStage(), style, "");
			cell = row.createCell(6);	setCellValues(entity.getM_FP(), style, "");
			cell = row.createCell(7);	setCellValues(entity.getM_BODR(), style, "");
			cell = row.createCell(8);	setCellValues(entity.getM_30(), style, "");
			cell = row.createCell(9);	setCellValues(entity.getM_60(), style, "");
			cell = row.createCell(10);	setCellValues(entity.getM_90(), style, "");
			cell = row.createCell(11);	setCellValues(entity.getM_100(), style, "");
			cell = row.createCell(12);	setCellValues(entity.getMilestone(), style, "");
			cell = row.createCell(13);	
			if(!entity.getProjectId().equals("CAT-178")) 
				setCellValues(entity.getMilestoneDate(), style, "date");
			else
				setCellValues("07/01/2012", style, "date");//TODO special case for CAT-178 despro start date
			
			cell = row.createCell(14);	if(entity.getDateReceived() != null) setCellValues(entity.getDateReceived(), style, "date");
			
			cell = row.createCell(15);	if(entity.getDateCompleted() != null) setCellValues(entity.getDateCompleted(), style, "date");
			cell = row.createCell(16);	setCellValues(entity.getEstimate(), workbook.createCellStyle(), "currency");
			cell = row.createCell(17);	setCellValues(entity.getLatestSubmittedEstimate().replaceAll(",", "").replaceAll("\\.00", ""), workbook.createCellStyle(), "currency");
			cell = row.createCell(18);	setCellValues(entity.getReconciled(), style, "");

			
			cell = row.createCell(19);	
			if(!entity.getProjectId().equals("EE-PR-TRC")) 
				setCellValues(entity.getNextMilestone(), style, "");
			else
				setCellValues("NTP", style, "");			//TODO special case for EE-PR-TRC
			cell = row.createCell(20);	
			if(!entity.getProjectId().equals("EE-PR-TRC")) 
				setCellValues(entity.getNextMilestoneDate(), style, "date");
			else
				setCellValues("12/31/2012", style, "date");	//TODO special case for EE-PR-TRC
			cell = row.createCell(21);	setCellValues(entity.getNote(), style, "");
			cell = row.createCell(22);	setCellValues(entity.getAccountableDesignManager(), style, "");
         }
		
	}
	
	
	void setCellValues(Object value, CellStyle style, String type)
	{
		//date cell
		if(type.equals("date"))
		{
			if(value != null && !value.equals("")) 
				cell.setCellValue(dateFormatter.parseDateTime((String)value).toDate());
			
			//Format this cell as a date.
	        style.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));
			cell.setCellStyle(style);
		}
		//currency cell
		else if(type.equals("currency"))
		{
			if(value != null && !value.equals("")) 
				cell.setCellValue(Long.parseLong(value.toString()));
			
			//Format this cell as currency.
	        style.setDataFormat((short)8); //8 = "($#,##0.00_);[Red]($#,##0.00)"
			cell.setCellStyle(style);
		}	
		else //text cell
			cell.setCellValue((String)value);
	}
	
	public void sortProjectList(List<EstimateEntity> projects) {
		 Collections.sort(projects, new Comparator<EstimateEntity>(){
									 		public int compare(EstimateEntity o1, EstimateEntity o2) {
									 			EstimateEntity e1 = o1;
									 			EstimateEntity e2 = o2;
									 			return e1.getProjectId().compareTo(e2.getProjectId());
									 		}//End of compare
		 								}
		 );//End of Collections.sort();
	}
}