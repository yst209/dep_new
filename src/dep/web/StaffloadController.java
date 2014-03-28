package dep.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dep.dao.DatabaseDao;
import dep.model.StaffloadEntity;
import dep.model.StaffloadInfo;

@Controller
public class StaffloadController
{
	@Autowired
	private StaffloadValidator staffloadValidator;
	
	public String chart1URL;
	public String imageMap1;
	
	DatabaseDao dao = new DatabaseDao();
	
	Map<String, List<StaffloadEntity>> staffloadLevelOneMap;
	Map<String, List<StaffloadEntity>> staffloadLevelTwoMap;
	Map<String, List<StaffloadEntity>> staffloadLevelThreeMap;
	Map<String, List<StaffloadEntity>> staffloadLevelAllMap;


	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyyMM");
	DateTimeFormatter dataPeriodFormatter = DateTimeFormat.forPattern("yyyyMM");
	
	Map<String,String> stageSelectList;
	Map<String,String> masterProgramSelectList;
	Map<String,String> chartScaleSelectList ;
	Map<Long,String> endFiscalYearSelectList;
	Map<String,String> PMSelectList ;
	Map<String,String> AMSelectList ;
	Map<String,String> projectStatusSelectList ;
	
	int startMonth;
	int endMonth;
	String stage;
	String masterProgram;
	String managedBy;
	String chartScale;
	Long endFiscalYear;
	String portfolioManager;
	String accountableManager;
	String projectStatus;
	String managerType;
	String managerName;

	public void generateDropdown()
	{
	 	dao.setDataSource();
		
//	 	stageSelectList = new LinkedHashMap<String,String>();
//	 	stageSelectList.put("Design", "Design");//Back end, Front end
//	 	stageSelectList.put("Construction", "Construction");
//	 	stageSelectList.put("Closeout", "Closeout");
//	 	
	 	masterProgramSelectList = new LinkedHashMap<String,String>();
	 	masterProgramSelectList.put("BEDC", "BEDC");
	 	masterProgramSelectList.put("WSC", "WSC");//Back end, Front end
	 	masterProgramSelectList.put("WWC", "WWC");
		
	 	chartScaleSelectList = new LinkedHashMap<String,String>();
	 	chartScaleSelectList.put("year", "In Years");
	 	chartScaleSelectList.put("quarter", "In Quarters");

	 	endFiscalYearSelectList = new LinkedHashMap<Long,String>();
	 	endFiscalYearSelectList.put(2014L, "2014");
	 	endFiscalYearSelectList.put(2015L, "2015");
	 	endFiscalYearSelectList.put(2016L, "2016");
	 	endFiscalYearSelectList.put(2017L, "2017");
	 	endFiscalYearSelectList.put(2018L, "2018");
	 	endFiscalYearSelectList.put(2019L, "2019");
	 	endFiscalYearSelectList.put(2020L, "2020");

	 	PMSelectList = new LinkedHashMap<String,String>();
	 	PMSelectList.put("All", "Compare All PMs in One Chart");
	 	PMSelectList.put("Daniel Solimando", "Daniel Solimando");
	 	PMSelectList.put("James Lauria", "James Lauria");
	 	PMSelectList.put("Kenneth Moriarty", "Kenneth Moriarty");
	 	PMSelectList.put("Kevin Clarke", "Kevin Clarke");
	 	PMSelectList.put("Matthew Osit", "Matthew Osit");
	 	PMSelectList.put("Roy Tysvaer", "Roy Tysvaer");

	 	AMSelectList = new LinkedHashMap<String,String>();
	 	for(String manager : dao.getActiveManagerList("AM", "WWC"))
	 		AMSelectList.put(manager, manager);

		projectStatusSelectList = new LinkedHashMap<String,String>();
		projectStatusSelectList.put("Active", "Current");//Back end, Front end
		projectStatusSelectList.put("Future", "Future");
	 			
	 			
	}
	
    @RequestMapping(value = "/staffload", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		System.out.println("staffload index " + new DateTime());

		generateDropdown();
		
		
		ModelAndView modelAndView = new ModelAndView("staffload");
		modelAndView.addObject("staffloadInfo", new StaffloadInfo());
//		modelAndView.addObject("stageSelectList", stageSelectList);
		modelAndView.addObject("masterProgramSelectList", masterProgramSelectList);
		modelAndView.addObject("chartScaleSelectList", chartScaleSelectList);
		modelAndView.addObject("endFiscalYearSelectList", endFiscalYearSelectList);
		modelAndView.addObject("PMSelectList", PMSelectList);
		modelAndView.addObject("AMSelectList", AMSelectList);
		modelAndView.addObject("projectStatusSelectList", projectStatusSelectList);
		return modelAndView;
	}
	
    @RequestMapping(value = "/staffloadResult", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("staffloadInfo") StaffloadInfo si, BindingResult result) throws Exception
	{
		System.out.println("staffload submit " + new DateTime());
		staffloadValidator.validate(si, result);
		if (result.hasErrors()) {
			System.out.println("errors");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("staffload");
			modelAndView.addObject("staffloadInfo", si);
//			modelAndView.addObject("stageSelectList", stageSelectList);
			modelAndView.addObject("masterProgramSelectList", masterProgramSelectList);
			modelAndView.addObject("chartScaleSelectList", chartScaleSelectList);
			modelAndView.addObject("endFiscalYearSelectList", endFiscalYearSelectList);
			modelAndView.addObject("PMSelectList", PMSelectList);
			modelAndView.addObject("AMSelectList", AMSelectList);
			modelAndView.addObject("projectStatusSelectList", projectStatusSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;

		} else {
			

//			stage = si.getStage();
			masterProgram = si.getMasterProgram();
			chartScale = si.getChartScale();
			endFiscalYear = si.getEndFiscalYear();
			portfolioManager = si.getPortfolioManager();
			accountableManager = si.getAccountableManager();
			projectStatus = si.getProjectStatus();
			if(!si.getPortfolioManager().equals("NONE"))
			{
				managerType = "PM";
				managerName = si.getPortfolioManager();
				si.setManagerType(managerType);
				si.setManagerName(managerName);
			}
			else if(!si.getAccountableManager().equals("NONE"))
			{
				managerType = "AM";
				managerName = si.getAccountableManager();
				si.setManagerType(managerType);
				si.setManagerName(managerName);
			}
//			System.out.println("stage: " + stage);
//			System.out.println("masterProgram: " + masterProgram);
//			System.out.println("managedBy: " + managedBy);
			System.out.println("endFiscalYear: " + endFiscalYear);
			
		 	DatabaseDao dao = new DatabaseDao();
		 	dao.setDataSource();
		 	
		 	
		 	String quarterString="", union="";
		 	
		 	DateTime startDate = new DateTime(2013, 1, 1, 0, 0, 0, 0);
		 	DateTime endDate = startDate.plusMonths(3).minusMinutes(1);
//		 	System.out.println("quarterStartDate: " + quarterStartDate);
//		 	System.out.println("quarterEndDate: " + quarterEndDate);
//		 	dao.getStaffload("resource", quarterStartDate);
		 	si.setArrayContent(dao.getStaffload("staffchart_year", chartScale, startDate, endFiscalYear, masterProgram, projectStatus, managerType, managerName));

//			System.out.println("getArrayContent: " + si.getArrayContent());
//			for(StaffBarChartEntity entity : staffBarChartList)
//			{
//				System.out.println("entity: " + entity.getFiscalYear());
//				System.out.println("entity: " + entity.getStaff());
//				System.out.println("entity: " + entity.getTotal());
//				System.out.println("----------------------------------");
//			}

			
			startMonth = dao.getLatestDataDate().intValue();
			endMonth = endFiscalYear.intValue();	
			

			ModelAndView modelAndView = new ModelAndView("staffloadResult");
			modelAndView.addObject("staffloadInfo", si);
			return modelAndView;
		}
	}
    
	
}
