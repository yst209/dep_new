package dep.web;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import dep.model.EstimateEntity;
import dep.model.EstimateForm;
import dep.model.EstimateInfo;
import dep.model.MonthlyEntity;


@Controller
public class EstimateController
{
	@Autowired
	private EstimateValidator estimateValidator;
	
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
	List<EstimateEntity> estimateList;
	Map<String, String> projectToBureauMap;
	Map<String, EstimateEntity> mapx;
	Map<String, EstimateEntity> map2;
	Map<String, ArrayList<EstimateEntity>> map3;
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
		
        for(int i= 201207;i<=dao.getLatestDataDate().intValue();i++)
        {
        	if(i%100>=1 && i%100<=12)
        	{
        		dataPeriodSelectList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
        	}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
        }
	}

    @RequestMapping(value = "/estimate_index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		System.out.println("estimate_index " + new DateTime());
		
		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("estimate_index");
		modelAndView.addObject("EstimateInfo", new EstimateInfo());
		modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
		return modelAndView;
	}
    
    @RequestMapping(value = "/estimate_edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("EstimateInfo") EstimateInfo info, BindingResult result) {
		System.out.println("estimate_edit " + new DateTime());

    	
    	estimateValidator.validate(info, result);
		if (result.hasErrors()) {
			System.out.println("Valiadtion Errors.");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("estimate_index");
			modelAndView.addObject("EstimateInfo", info);
			modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;
		} else {

	    	runReport(info.getEditMonth().toString());
			EstimateForm form = new EstimateForm();
			
			form.setProjects(estimateList);
			form.sortProjectList();
			ModelAndView modelAndView = new ModelAndView("estimate_edit");
			modelAndView.addObject("estimateForm", form);
			
			return modelAndView;
		}
    }
 
    @RequestMapping(value = "/estimate_save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("estimateForm") EstimateForm estimateForm, BindingResult result) {
		System.out.println("estimate_save " + new DateTime());

        
    	estimateValidator.validate(estimateForm, result);
		if (result.hasErrors()) {
			System.out.println("Valiadtion Errors.");
			
			ModelAndView modelAndView = new ModelAndView("estimate_edit");
			modelAndView.addObject("EstimateForm", estimateForm);
			modelAndView.addObject("errors", result);
			return modelAndView;
		} else {

	        Long latestDataPeriod = dao.getLatestEstimateDataPeriod();
	        for(EstimateEntity project: estimateForm.getProjects())
	        {
	        	//if project info doesn't exist in Estimate_Log table, insert it;
	    	 	if(project.getDataPeriod() > latestDataPeriod)
	    	 		dao.insertEstimate(project);
	    	 	else
	    	 		dao.updateEstimate(project);
	        }
	 
	        return new ModelAndView("estimate_save", "estimateForm", estimateForm);
		}
		

    }
    

	
	
    @RequestMapping(value = "/estimate_submit", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("EstimateInfo") EstimateInfo info, BindingResult result) {
		System.out.println("estimate_submit " + new DateTime());
		
		estimateValidator.validate(info, result);
		if (result.hasErrors()) {
			System.out.println("Valiadtion Errors.");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("estimate_index");
			modelAndView.addObject("EstimateInfo", info);
			modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;
		} else {

		 	runReport(info.getReportMonth().toString());
		 	
			ModelAndView modelAndView = new ModelAndView("EstimateExcelBean");
			modelAndView.addObject("estimateList", estimateList);
			modelAndView.addObject("month", info.getReportMonth());
			return modelAndView;
		}
	}
    
    public void runReport(String selectedMonth) 
    {
//	 	selectedMonth = "201111";
	 

	 	
	 	
	 	String estimateDataPeriod;
	 	
	 	List<EstimateEntity> logList;
	 	if(Long.parseLong(selectedMonth) > dao.getLatestEstimateDataPeriod())
	 		estimateDataPeriod = dao.getLatestEstimateDataPeriod().toString();
	 	else
	 		estimateDataPeriod = selectedMonth;
	 	
	 	System.out.println("estimateDataPeriod: " + estimateDataPeriod);
	 	

	 	logList = dao.getEstimateLog(estimateDataPeriod);

	 	Map<String, EstimateEntity> logMap = new HashMap<String, EstimateEntity>();
		
		for(EstimateEntity entity : logList)				
			logMap.put(entity.getDataPeriod().toString()+entity.getProjectId(), entity);

	 	
	 	List<EstimateEntity> dataList = dao.getCEReport(selectedMonth);
	 	EstimateEntity tmp = new EstimateEntity();

	 	estimateList = new ArrayList<EstimateEntity>();
		
		for(EstimateEntity entity : dataList)
		{
			entity.setDataPeriod(Long.parseLong(selectedMonth));
			if(logMap.get(estimateDataPeriod+entity.getProjectId())!= null)
			{
		    	entity.setProjectId(logMap.get(estimateDataPeriod+entity.getProjectId()).getProjectId());
		    	entity.setStatus(logMap.get(estimateDataPeriod+entity.getProjectId()).getStatus());
		    	entity.setBudget(logMap.get(estimateDataPeriod+entity.getProjectId()).getBudget());
		    	entity.setLatestSubmittedEstimate(logMap.get(estimateDataPeriod+entity.getProjectId()).getLatestSubmittedEstimate());
		    	entity.setDateReceived(logMap.get(estimateDataPeriod+entity.getProjectId()).getDateReceived());
		    	entity.setDateCompleted(logMap.get(estimateDataPeriod+entity.getProjectId()).getDateCompleted());
		    	entity.setReconciled(logMap.get(estimateDataPeriod+entity.getProjectId()).getReconciled());
		    	entity.setNote(logMap.get(estimateDataPeriod+entity.getProjectId()).getNote());
			}						
//			map.put(selectedMonth+entity.getProjectId(), entity);
			estimateList.add(entity);
		} 
    
    }
}
