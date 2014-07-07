package dep.web;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
import dep.model.EstimateInfo;
import dep.model.HistoricalTrendEntity;
import dep.model.MonthlyEntity;
import dep.model.SCGHistoricalTrendEntity;

@Controller
public class P6Controller
{
	public String dataPeriod;
	public String sourceLocation;
	public String targetLocation;
	public String tempFolder;
	private FileWriter writer;
	public List<String> projectList;
	public List<String> validatedProjectList;
	public List<String> errorProjectList;
	public List<String> CSVFileTypes = new ArrayList<String>(Arrays.asList("ACT 12MTH", "ACT Fore", "ACWP", "RES 12MTH", "RES Fore"));
	DateTime dataPeriodDate;
	ArrayList<String> storeValues;
	@Autowired
	DatabaseDao dao = new DatabaseDao();
	
	public String chart1URL;
	public String imageMap1;
//	private @Autowired
//	HttpServletResponse response;
	List<HistoricalTrendEntity> P6List;
	Map<String, String> projectToBureauMap;
	Map<String, SCGHistoricalTrendEntity> map;
	Map<String, SCGHistoricalTrendEntity> map2;
	Map<String, ArrayList<SCGHistoricalTrendEntity>> map3;
	Map<String, MonthlyEntity> monthlyMap;
	Map<String, MonthlyEntity> comparedMonthlyMap;
	Map<String, MonthlyEntity> monthlyJSList;
	Map<String, MonthlyEntity> comparedMonthlyJSList;
	String bureau;
	String operatingBureau;

//	DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
//	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	DateTimeFormatter dataPeriodFormatter = DateTimeFormat.forPattern("yyyyMM");
	DateTimeFormatter yearMonthFormatter = DateTimeFormat.forPattern("yyyy-MM");
	DateTimeFormatter dateHourMinuteFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HHmm");
	
	Map<Long,String> dataPeriodSelectList;
	
	public void generateDropdown()
	{
	 	
		dataPeriodSelectList = new LinkedHashMap<Long,String>();
		
        for(int i= 201109;i<=dao.getLatestDataDate().intValue();i++)
        {
        	if(i%100>=1 && i%100<=12)
        	{
        		dataPeriodSelectList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
        	}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
        }
	}

    @RequestMapping(value = "/p6_index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		System.out.println("p6_index");
		
	 	P6List = dao.getP6HistoricalTrendData();
		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("p6_index");
//		modelAndView.addObject("EstimateInfo", new EstimateInfo());
		modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
		return modelAndView;
	}
	
	
    @RequestMapping(value = "/p6_submit", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("EstimateInfo") EstimateInfo info, BindingResult result) {
		System.out.println("submit");

    	
		ModelAndView modelAndView = new ModelAndView("P6ExcelBean");
		modelAndView.addObject("P6List", P6List);
//		modelAndView.addObject("month", info.getReportMonth());
		return modelAndView;
		
//		estimateValidator.validate(info, result);
//		if (result.hasErrors()) {
//			System.out.println("Valiadtion Errors.");
//			
//			generateDropdown();
//			ModelAndView modelAndView = new ModelAndView("estimate_index");
//			modelAndView.addObject("EstimateInfo", info);
//			modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
//			modelAndView.addObject("errors", result);
//			return modelAndView;
//		} else {
//
//		 	runReport(info.getReportMonth().toString());
//		 	
//			ModelAndView modelAndView = new ModelAndView("SCGExcelBean");
//			modelAndView.addObject("SCGMap", map2);
//			modelAndView.addObject("month", info.getReportMonth());
//			return modelAndView;
//		}
	}
    
}
