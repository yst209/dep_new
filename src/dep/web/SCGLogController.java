package dep.web;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dep.dao.DatabaseDao;
import dep.model.MonthlyEntity;
import dep.model.SCGForm;
import dep.model.SCGHistoricalTrendEntity;
import dep.model.SCGLogEntity;
import dep.model.SCGLogInfo;

@Controller
@RequestMapping("/scg_log/**")
@Scope("session")
public class SCGLogController
{
	private Logger logger = Logger.getLogger(SCGLogController.class);
	
	@Autowired
	private SCGLogValidator SCGLogValidator;
	
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
	Long selectedMonth;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setAutoGrowCollectionLimit(1000);
	}
	
	public void generateDropdown()
	{
	 	
		Map<Long,String> tempList = new LinkedHashMap<Long,String>();
		dataPeriodSelectList = new LinkedHashMap<Long,String>();
		

//        for(int i= dao.getLatestDataDate().intValue();i>= 201209;i--)
        for(int i= 201109;i<=dao.getLatestDataDate().intValue();i++)        
        {
        	if(i%100>=1 && i%100<=12)
        	{
        		tempList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
        	}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
        }
        
        
        ListIterator<Map.Entry<Long, String>> iter = new ArrayList(tempList.entrySet()).listIterator(tempList.size());
        //Reverse order
    	while (iter.hasPrevious()) {
    	    Map.Entry<Long, String> entry = iter.previous();
    	    dataPeriodSelectList.put(entry.getKey(), entry.getValue());
    	}
	}

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		logger.info("IP: " + request.getRemoteHost());
		
		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("scg_log/scg_index");
		modelAndView.addObject("SCGLogInfo", new SCGLogInfo());
		modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
		return modelAndView;
	}
    
    @RequestMapping(value = "/scg_edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("SCGLogInfo") SCGLogInfo info, BindingResult result, HttpServletRequest request) {
		logger.info("IP: " + request.getRemoteHost());
    	
    	SCGLogValidator.validate(info, result);
		if (result.hasErrors()) {
			System.out.println("Valiadtion Errors.");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("scg_log/scg_index");
			modelAndView.addObject("SCGLogInfo", info);
			modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;
		} else {

			selectedMonth = info.getEditMonth();
	    	runReport(info.getEditMonth().toString());
			SCGForm form = new SCGForm();
			
			//Convert project data in map2 to SCGForm format in order to display on scg_edit page.
			List<SCGLogEntity> list = new ArrayList<SCGLogEntity>();
			Iterator<Entry<String, SCGHistoricalTrendEntity>> iterator = map2.entrySet().iterator();
		    while (iterator.hasNext()) {
		    	Map.Entry<String, SCGHistoricalTrendEntity> pairs = iterator.next();
		    	SCGLogEntity entity = new SCGLogEntity();
		    	entity.setDataPeriod(pairs.getValue().getDataPeriod());
		    	entity.setProjectId(pairs.getValue().getProjectId());
		    	entity.setProjectName(pairs.getValue().getProjectName());
		    	entity.setPortfolioManager(pairs.getValue().getPortfolioManager());
		    	entity.setStage(pairs.getValue().getCurrentStage());
		    	entity.setSCGLead(pairs.getValue().getSCGLead());
//		    	entity.setSCGSupport(pairs.getValue().getSCGSupport());
		    	entity.setClaim(pairs.getValue().getClaim());
		    	entity.setComments(pairs.getValue().getComments());
		    	list.add(entity);
		    }	    
			
			form.setProjects(list);
			form.sortProjectList();
			ModelAndView modelAndView = new ModelAndView("scg_log/scg_edit");
			modelAndView.addObject("scgForm", form);
			
			return modelAndView;
		}
    }
 
    @RequestMapping(value = "/scg_save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("scgForm") SCGForm scgForm, HttpServletRequest request) {
		logger.info("IP: " + request.getRemoteHost());

        Long latestDataPeriod = dao.getLatestSCGLogDataPeriod();
		List<SCGLogEntity> list = scgForm.getProjects();

        for(SCGLogEntity project: list)
        {
        	//if project info doesn't exist in SCG_Log table, insert it;
    	 	if(project.getDataPeriod() > latestDataPeriod)
    	 		dao.insertSCGLog(project);
    	 	else
    	 		dao.updateSCGLog(project);
        }
        
		
		Map<String, String> projectMap = new HashMap<String, String>();
        for(SCGHistoricalTrendEntity entity: dao.getProjectSupportingRoles("All"))
        {
        	projectMap.put(entity.getProjectId(), entity.getSCGLead());
        }
        for(SCGLogEntity entity: list)
        {
        	SCGHistoricalTrendEntity entity2 = new SCGHistoricalTrendEntity();
        	entity2.setProjectId(entity.getProjectId());
        	entity2.setSCGLead(entity.getSCGLead());
        	//Only update when latest month is selected 
        	//if latest month is selected and project info doesn't exist in Project_Supporting_Roles table, insert it;
        	if(selectedMonth.equals(latestDataPeriod))
        	{
	    	 	if(projectMap.get(entity.getProjectId())==null)
	    	 		dao.insertSupportingRole(entity2);
	    	 	else if(!projectMap.get(entity.getProjectId()).equals(entity2.getSCGLead()))//Update SCG Lead if it has been changed
	    	 		dao.updateSupportingRole(entity2, "SCG_Lead");
        	}
        }
 
        return new ModelAndView("scg_log/scg_save", "scgForm", scgForm);
    }	
	
    @RequestMapping(value = "/scg_submit", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("SCGLogInfo") SCGLogInfo info, BindingResult result, HttpServletRequest request) {
		logger.info("IP: " + request.getRemoteHost());

    	
    	SCGLogValidator.validate(info, result);
		if (result.hasErrors()) {
			System.out.println("Valiadtion Errors.");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("scg_log/scg_index");
			modelAndView.addObject("SCGLogInfo", info);
			modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;
		} else {

		 	runReport(info.getReportMonth().toString());
		 	
			ModelAndView modelAndView = new ModelAndView("SCGExcelBean");
			modelAndView.addObject("SCGMap", map2);
			modelAndView.addObject("dao", dao);
			modelAndView.addObject("month", info.getReportMonth());
			return modelAndView;
		}
	}
    
    public void runReport(String selectedMonth) 
    {
//	 	selectedMonth = "201111";
	 	
	 	String scgLogDataPeriod;
	 	
	 	List<SCGLogEntity> logList;
	 	if(Long.parseLong(selectedMonth) > dao.getLatestSCGLogDataPeriod())
	 		scgLogDataPeriod = dao.getLatestSCGLogDataPeriod().toString();
	 	else
	 		scgLogDataPeriod = selectedMonth;
	 	

	 	logList = dao.getSCGLog(scgLogDataPeriod);

	 	Map<String, SCGLogEntity> logMap = new HashMap<String, SCGLogEntity>();
		
		for(SCGLogEntity entity : logList)				
			logMap.put(entity.getDataPeriod().toString()+entity.getProjectId(), entity);

	 	
	 	List<SCGHistoricalTrendEntity> dataList = dao.getFilteredSCGData(selectedMonth);
	 	SCGHistoricalTrendEntity tmp = new SCGHistoricalTrendEntity();

		map = new HashMap<String, SCGHistoricalTrendEntity>();
		//STEP 1: Merge
		for(SCGHistoricalTrendEntity entity : dataList)
		{
			if(entity.getP6_Orig_Proj_id()==null)//current project
			{						
				map.put(entity.getId(), entity);
			}
			//Only merge latest baseline NTP and SC into the original project
			else
			{
		        //Special solution to iterate through all the historical months
		        for(int i= 201001;i<=Integer.parseInt(selectedMonth);i++)
		        {
		        	if(i%100>=1 && i%100<=12)
		        	{
						tmp = map.get(new Integer(i).toString()+entity.getOrig_Proj_ID().toString());

						if(tmp!=null)
						{
							tmp.setBaselineNTP(entity.getBaselineNTP());
							tmp.setBaselineSC(entity.getBaselineSC());
							map.put(tmp.getId(), tmp);
						}
		        	}
		        	else if(i%100 == 13)
		        		i=(i/100+1)*100;
		        }	
					

			}
		}
		
		map2 = new HashMap<String, SCGHistoricalTrendEntity>();
		
		Map<String, Long> currContMap = dao.getCurrCont(selectedMonth);
//		System.out.println("currContMap.size(): " + currContMap.size());
		
		Map<String, Long> origContMap = dao.getOrigCont(selectedMonth);
//		System.out.println("origContMap.size(): " + origContMap.size());
		
		//STEP 2: Filter out unpaired data
	    Iterator<Entry<String, SCGHistoricalTrendEntity>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, SCGHistoricalTrendEntity> pairs = it.next();
	        //Only add merged current project entry(with baseline NTP) into map2
	        if(pairs.getValue().getP6_Orig_Proj_id()==null)
	        {
	        	//new filter
	        	tmp = pairs.getValue();	        	
	        	
	        	//need to add if project not in exception list, skip this
	        	if(tmp.getCurrentSC() != null && !tmp.getCurrentSC().equals("") && dateFormatter.parseDateTime(tmp.getCurrentSC()).isBefore(new DateTime(2010, 12, 31, 0, 0, 0, 0))
	        		&& (logMap.get(scgLogDataPeriod+tmp.getProjectId())== null || logMap.get(scgLogDataPeriod+tmp.getProjectId())!= null && logMap.get(scgLogDataPeriod+tmp.getProjectId()).getClaim().equals("")  ))
	        	{
//	        		System.out.println("Don't show: " + tmp.getProjectId());
	        	}
	        	else
	        	{
		        	
//					System.out.println("tmp entity.getDataPeriod().toString()+entity.getProjectId(): " + tmp.getDataPeriod().toString()+tmp.getProjectId());
	
					if(logMap.get(scgLogDataPeriod+tmp.getProjectId()) != null)
					{
						tmp.setSCGLead(logMap.get(scgLogDataPeriod+tmp.getProjectId()).getSCGLead());
						tmp.setSCGSupport(logMap.get(scgLogDataPeriod+tmp.getProjectId()).getSCGSupport());
						tmp.setComments(logMap.get(scgLogDataPeriod+tmp.getProjectId()).getComments());
						tmp.setClaim(logMap.get(scgLogDataPeriod+tmp.getProjectId()).getClaim());
						tmp.setCurrentConstructionContractAmount(currContMap.get(tmp.getProjectId()));
						tmp.setOrigConstructionContractAmount(origContMap.get(tmp.getProjectId()));
					}
		        	map2.put(pairs.getKey(), tmp);	
	        	}
	        }
	    }
	    //Now all entries in map2 must have latest baseline NTP		
//	    System.out.println(map2.size());
	    	    
//		Iterator<Entry<String, SCGHistoricalTrendEntity>> iterator = map2.entrySet().iterator();
//	    while (iterator.hasNext()) {
//	    	Map.Entry<String, SCGHistoricalTrendEntity> pairs = iterator.next();
//		    System.out.println("map2 - "+ pairs.getKey() + ": "+map2.get(pairs.getKey()).getProjectId()+ ", getCurrentNTP: " + map2.get(pairs.getKey()).getCurrentNTP()+ " - getBaselineNTP: " + map2.get(pairs.getKey()).getBaselineNTP()+" ");
//	    }	    
    
    }
    
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public ModelAndView editRoles(HttpServletRequest request) {
		logger.info("IP: " + request.getRemoteHost());

		SCGForm form = new SCGForm();
		
		form.setSupportingRoleProjects(dao.getProjectSupportingRoles("Current"));
		ModelAndView modelAndView = new ModelAndView("scg_log/roles");
		modelAndView.addObject("scgForm", form);
		
		return modelAndView;
    }
    
    @RequestMapping(value = "/roles_save", method = RequestMethod.POST)
    public ModelAndView saveRoles(@ModelAttribute("scgForm") SCGForm scgForm, HttpServletRequest request) {
		logger.info("IP: " + request.getRemoteHost());

		List<SCGHistoricalTrendEntity> list = scgForm.getSupportingRoleProjects();
		
		Map<String, Boolean> projectMap = new HashMap<String, Boolean>();
        for(SCGHistoricalTrendEntity entity: dao.getProjectSupportingRoles("All"))
        {
        	projectMap.put(entity.getProjectId(), true);
        }
        for(SCGHistoricalTrendEntity entity: list)
        {
        	//if project info doesn't exist in Project_Supporting_Roles table, insert it;
    	 	if(projectMap.get(entity.getProjectId())==null)
    	 		dao.insertSupportingRole(entity);
    	 	else
    	 		dao.updateSupportingRole(entity, "Roles");
        }
 
        return new ModelAndView("scg_log/roles_save", "scgForm", scgForm);
    }

}
