package dep.web;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import ChartDirector.Chart;
import ChartDirector.LineLayer;
import ChartDirector.XYChart;
import dep.dao.DatabaseDao;
import dep.model.HistoricalTrendEntity;
import dep.model.MonthlyEntity;
import dep.model.ProjectBureauEntity;
import dep.model.TrendInfo;

@Controller
@RequestMapping("/trend/**") //**: Wildcard, /trend/xyz will be mapped to this controller
//@RequestMapping("/trend") //http://localhost:8080/dep/trend/trendChart.html
public class TrendController
{
	@Autowired
	private TrendValidator trendValidator;
	
	public String chart1URL;
	public String imageMap1;
	@Autowired
	DatabaseDao dao = new DatabaseDao();
	
	Map<String, String> projectToBureauMap;
	Map<String, HistoricalTrendEntity> map;
	Map<String, HistoricalTrendEntity> map2;
	Map<String, ArrayList<HistoricalTrendEntity>> map3;
	Map<String, MonthlyEntity> monthlyMap;
	Map<String, MonthlyEntity> comparedMonthlyMap;
	Map<String, MonthlyEntity> monthlyJSList;
	Map<String, MonthlyEntity> comparedMonthlyJSList;
	String bureau;
	String operatingBureau;

	DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	DateTimeFormatter dataPeriodFormatter = DateTimeFormat.forPattern("yyyyMM");
	
	Map<String,String> bureauSelectList;
	Map<String,String> opBureauSelectList;
	Map<Long,String> comparedMonthSelectList ;
	Map<Long,String> currentMonthSelectList;
	Map<Long,String> endPeriodSelectList;

	public void generateDropdown()
	{
		
		bureauSelectList = new LinkedHashMap<String,String>();
		bureauSelectList.put("BEDC", "BEDC");//Back end, Front end
		bureauSelectList.put("BWS", "BWS");
		
		opBureauSelectList = new LinkedHashMap<String,String>();
		opBureauSelectList.put("BWT", "BWT");
		opBureauSelectList.put("BWS", "BWS");
		opBureauSelectList.put("BWSO", "BWSO");
		opBureauSelectList.put("BEDC", "ALL");
		
		comparedMonthSelectList = new LinkedHashMap<Long,String>();
		
//		System.out.println(dateFormat.format(new DateTime(2011,1,1,0,0,0,0).toDate()));
        for(int i= 201103;i<=dao.getLatestDataDate().intValue()-1;i++)
        {
        	if(i%100>=1 && i%100<=12)
        	{
        		comparedMonthSelectList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
        	}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
        }	

		currentMonthSelectList = new LinkedHashMap<Long,String>();
		
//		System.out.println(dao.getLatestDataDate());
        for(int i= dao.getLatestDataDate().intValue()-1;i<=dao.getLatestDataDate().intValue();i++)
        {
        	if(i%100>=1 && i%100<=12)
        	{
        		currentMonthSelectList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
        	}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
        }	
        
        endPeriodSelectList = new LinkedHashMap<Long,String>();
        endPeriodSelectList.put(201412L, "2014");
        endPeriodSelectList.put(201512L, "2015");
        endPeriodSelectList.put(201612L, "2016");
        endPeriodSelectList.put(201712L, "2017");
        endPeriodSelectList.put(201812L, "2018");
        endPeriodSelectList.put(201912L, "2019");
        endPeriodSelectList.put(202012L, "2020");
        endPeriodSelectList.put(202112L, "2021");
        endPeriodSelectList.put(202212L, "2022");
        endPeriodSelectList.put(202312L, "2023");
        endPeriodSelectList.put(202412L, "2024");
        endPeriodSelectList.put(202512L, "2025");
	}
	
	@RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		System.out.println("trendchart index from IP: " + request.getRemoteHost()+ " at " + new DateTime());

		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("trend/trendChart"); // /jsp/ trend/trendChart .jsp
		modelAndView.addObject("trendInfo", new TrendInfo());
		modelAndView.addObject("bureauSelectList", bureauSelectList);
		modelAndView.addObject("opBureauSelectList", opBureauSelectList);
		modelAndView.addObject("comparedMonthSelectList", comparedMonthSelectList);
		modelAndView.addObject("currentMonthSelectList", currentMonthSelectList);
		modelAndView.addObject("endPeriodSelectList", endPeriodSelectList);
		return modelAndView;
	}
	
    @RequestMapping(value = "/trendResult", method=RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("trendInfo") TrendInfo ti, BindingResult result) throws Exception
	{
		System.out.println("trendchart submit " + new DateTime());
		trendValidator.validate(ti, result);
		if (result.hasErrors()) {
			System.out.println("errors");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("trend/trendChart");
			modelAndView.addObject("trendInfo", ti);
			modelAndView.addObject("bureauSelectList", bureauSelectList);
			modelAndView.addObject("opBureauSelectList", opBureauSelectList);
			modelAndView.addObject("comparedMonthSelectList", comparedMonthSelectList);
			modelAndView.addObject("currentMonthSelectList", currentMonthSelectList);
			modelAndView.addObject("endPeriodSelectList", endPeriodSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;

		} else {

		
	//		DBManager manager = new DBManager();
	//		ModelAndView modelAndView = new ModelAndView("trend");
			
			
			Long currentMonth = ti.getCurrentMonth();
			Long comparedMonth = ti.getComparedMonth();
			System.out.println(comparedMonth);
			Long startMonth = 201001L;
//			Long endMonth = 201212L;
			Long endMonth = ti.getEndPeriod();
			System.out.println("endMonth" + endMonth);
			bureau = ti.getBureau();
			operatingBureau = ti.getOperatingBureau();
			
			if(bureau.equals("BWS") && operatingBureau.equals("BWS"))
			{
				if(startMonth<201105L)
					startMonth = 201105L;
				if(comparedMonth<201105L)
					comparedMonth = 201105L;
			}
	
			monthlyMap = drawTrendLine(currentMonth, startMonth, endMonth);
	
		    
			comparedMonthlyMap = drawTrendLine(comparedMonth, startMonth, endMonth);
	
			renderChart(monthlyMap, comparedMonthlyMap, startMonth, endMonth, currentMonth, comparedMonth);
			
			//If there are no projects for the months
			//Should be removed from the list
			//Otherwise it will kill front end project list js popup
			if(bureau.equals("BWS") && operatingBureau.equals("BWS"))
			{
				monthlyJSList = new HashMap<String, MonthlyEntity>();
			    Iterator<Entry<String, MonthlyEntity>> it = monthlyMap.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<String, MonthlyEntity> pairs = it.next();
			        if(pairs.getValue().getTotalProjectCount()>0)
				        monthlyJSList.put(pairs.getKey(), pairs.getValue());
			    }
			    
			    comparedMonthlyJSList = new HashMap<String, MonthlyEntity>();
			    it = comparedMonthlyMap.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry<String, MonthlyEntity> pairs = it.next();
			        if(pairs.getValue().getTotalProjectCount()>0)
				        comparedMonthlyJSList.put(pairs.getKey(), pairs.getValue());
			    }
			}
			else
			{
				monthlyJSList = monthlyMap;
				comparedMonthlyJSList = comparedMonthlyMap;
			}
			
			ti.setChart1URL("/dep/pages/getchart.jsp?" + chart1URL);
			ti.setImageMap(imageMap1);
			ti.setMonthlyMap(monthlyJSList);
			ti.setComparedMonthlyMap(comparedMonthlyJSList);

			ModelAndView modelAndView = new ModelAndView("trend/trendResult");
			modelAndView.addObject("trendInfo", ti);
			return modelAndView;
		}
	}

	public Map<String, MonthlyEntity> drawTrendLine(Long currentMonth, Long startMonth, Long endMonth){
		try {
//		 	BasicDataSource dataSource = new BasicDataSource();
//		 	dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
//		 	dataSource.setUrl("jdbc:jtds:sqlserver://BEE-WASQA2;databaseName=ProjectControl");
//		 	dataSource.setUsername("pmis_reader");
//		 	dataSource.setPassword("pmis123");
		 	
		 	
		 	List<ProjectBureauEntity> projectMappingList = dao.getProjectsByBureau(bureau, operatingBureau);
		 	projectToBureauMap = new HashMap<String, String>();
		 	for(ProjectBureauEntity entity : projectMappingList)
		 	{
		 		if(operatingBureau.equals("BEDC"))
		 			projectToBureauMap.put(entity.getProjectId(), "BEDC");
		 		else
			 		projectToBureauMap.put(entity.getProjectId(), entity.getOperatingBureau());
		 	}
		 	List<HistoricalTrendEntity> dataList = dao.getFilteredHistoricalTrendData();
			HistoricalTrendEntity tmp = new HistoricalTrendEntity();

			map = new HashMap<String, HistoricalTrendEntity>();
			//STEP 1: Merge
			for(HistoricalTrendEntity entity : dataList)
			{
				boolean isInBureau = true;
//				if(!bureau.equals("BEDC"))
//				{
					if(projectToBureauMap.get(entity.getProjectId())!=null)
						isInBureau = projectToBureauMap.get(entity.getProjectId()).equals(operatingBureau);
					else 
						isInBureau = false;
//				}
//				else
//					isInBureau = true; //BEDC: count all projects
				
				if(entity.getP6_Orig_Proj_id()==null)//current project
				{						
					if(isInBureau)
						map.put(entity.getId(), entity);
				}
				//Only merge latest baseline NTP and SC into the original project
				else
				{
			        //Special solution to iterate through all the historical months
			        for(int i= startMonth.intValue();i<=currentMonth.intValue();i++)
			        {
			        	if(i%100>=1 && i%100<=12)
			        	{
							tmp = map.get(new Integer(i).toString()+entity.getP6_Orig_Proj_id().toString());

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
			map2 = new HashMap<String, HistoricalTrendEntity>();

			//STEP 2: Filter out unpaired data
		    Iterator<Entry<String, HistoricalTrendEntity>> it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, HistoricalTrendEntity> pairs = it.next();
		        //Only add merged current project entry(with baseline NTP) into map2
		        if(pairs.getValue().getP6_Orig_Proj_id()==null && pairs.getValue().getBaselineNTP()!=null)
		        {
		        	map2.put(pairs.getKey(), pairs.getValue());			        	
		        }
		    }
		    //Now all entries in map2 must have latest baseline NTP
			
			map3 = new HashMap<String, ArrayList<HistoricalTrendEntity>>();
			//STEP 3: Categorized entries by data period
			it = map2.entrySet().iterator();
		    while (it.hasNext()) {
		    	Map.Entry<String, HistoricalTrendEntity> pairs = it.next();
		        ArrayList<HistoricalTrendEntity> tmpList;
		        //Dump all the projects into corresponding data period buckets and stored as an array list
		        //First project for current data period bucket
		        if(map3.get(pairs.getValue().getDataPeriod().toString())==null)
		        	tmpList = new ArrayList<HistoricalTrendEntity>();
		        //Attach to the existing list
		        else
		        	tmpList = map3.get(pairs.getValue().getDataPeriod().toString());
	        	tmpList.add(pairs.getValue());
	        	map3.put(pairs.getValue().getDataPeriod().toString(), tmpList);
		    }

		    
     		Long totalBaselineDuration = 0L;
     		Long totalCurrentDuration = 0L;
     		Integer totalProjectCount = 0;
     		Map<String, MonthlyEntity> outputMonthlyMap = new HashMap<String, MonthlyEntity>();
			//STEP 4: Check which projects should be considered and add up numbers for each month
			//HISTORICAL PART
			Iterator<Entry<String, ArrayList<HistoricalTrendEntity>>> iter = map3.entrySet().iterator();
		    while (iter.hasNext()) {
		    	Map.Entry<String, ArrayList<HistoricalTrendEntity>> pairs = iter.next();
		    	MonthlyEntity monthEntity = new MonthlyEntity();
        		totalBaselineDuration = 0L;
        		totalCurrentDuration = 0L;
        		totalProjectCount = 0;
		    	//Calculate current and baseline duration for historical data.
		        for(HistoricalTrendEntity entity : pairs.getValue())
		        {		        	
		        	//TODO catch the inconsistency between current stage and NTP/SC date
//			        	if(entity.getCurrentStage().equals("Con")!=isInConstruction(entity.getDataPeriod().toString(), entity.getCurrentNTP(), entity.getCurrentSC()))
//			        		System.out.println("Test: "+ entity.getProjectId()+", date: " + entity.getDataPeriod() +" Con: " + entity.getCurrentStage().equals("Con") +  ", isInConstruction: "+ isInConstruction(entity.getDataPeriod().toString(), entity.getCurrentNTP(), entity.getCurrentSC()) + " " + entity.getDataPeriod().toString()+", " +  entity.getCurrentNTP()+", " +  entity.getCurrentSC());
		    		
		        	//Only use NTP/SC date to check if the project is in construction because current stage might be inaccurate
//			    		if(entity.getCurrentStage().equals("Con") && isInConstruction(entity.getDataPeriod().toString(), entity.getCurrentNTP(), entity.getCurrentSC()))
		    		if(isInConstruction(entity.getDataPeriod().toString(), entity.getCurrentNTP(), entity.getCurrentSC()))
		    		{
		    			entity.setBaselineDuration(Math.round(getDuration(entity.getBaselineNTP(), entity.getBaselineSC())/30.0));
		    			totalBaselineDuration+=getDuration(entity.getBaselineNTP(), entity.getBaselineSC());
		    			entity.setCurrentDuration(Math.round(getDuration(entity.getCurrentNTP(), entity.getCurrentSC())/30.0));
		    			totalCurrentDuration+=getDuration(entity.getCurrentNTP(), entity.getCurrentSC());
		    			totalProjectCount++;
		    			List<HistoricalTrendEntity> projectList = monthEntity.getProjectList();
		    			projectList.add(entity);
		    			monthEntity.setProjectList(projectList);
		    			monthEntity.sortProjectList();
		    		}
		        }
 			monthEntity.setId(Long.parseLong(pairs.getKey()));
 			monthEntity.setMonth(Long.parseLong(pairs.getKey().substring(4, 6)));
 			monthEntity.setYear(Long.parseLong(pairs.getKey().substring(0, 4)));
 			monthEntity.setTotalProjectCount(totalProjectCount);
 			monthEntity.setBaselineDuration(Math.round(totalBaselineDuration/30.0/totalProjectCount));
 			monthEntity.setCurrentDuration(Math.round(totalCurrentDuration/30.0/totalProjectCount));
 			outputMonthlyMap.put(pairs.getKey(), monthEntity);
 					        			    
		    }

		    
		    
	        //FORECAST PART
		    //Forecast future numbers based on current month data
	        ArrayList<HistoricalTrendEntity> currentMonthData = map3.get(currentMonth.toString());

	        //Special solution to iterate through all the months
	        for(int i= currentMonth.intValue()+1;i<=endMonth.intValue();i++)
	        {
	        	if(i%100>=1 && i%100<=12)
	        	{
	        		MonthlyEntity monthEntity = new MonthlyEntity();
	        		totalBaselineDuration = 0L;
	        		totalCurrentDuration = 0L;
	        		totalProjectCount = 0;
	        		for(HistoricalTrendEntity entity : currentMonthData)
	        		{
	        			if(isInConstruction(new Integer(i).toString(), entity.getCurrentNTP(), entity.getCurrentSC()))
	        			{
			    			entity.setBaselineDuration(Math.round(getDuration(entity.getBaselineNTP(), entity.getBaselineSC())/30.0));
			    			totalBaselineDuration+=getDuration(entity.getBaselineNTP(), entity.getBaselineSC());
			    			entity.setCurrentDuration(Math.round(getDuration(entity.getCurrentNTP(), entity.getCurrentSC())/30.0));
			    			totalCurrentDuration+=getDuration(entity.getCurrentNTP(), entity.getCurrentSC());
			    			totalProjectCount++;
			    			List<HistoricalTrendEntity> projectList = monthEntity.getProjectList();
			    			projectList.add(entity);
			    			monthEntity.setProjectList(projectList);
			    			monthEntity.sortProjectList();
	        			}
	        		}
        			monthEntity.setId(new Long(i));
        			monthEntity.setMonth(Long.parseLong(new Long(i).toString().substring(4, 6)));
        			monthEntity.setYear(Long.parseLong(new Long(i).toString().substring(0, 4)));
        			monthEntity.setTotalProjectCount(totalProjectCount);

        			if(totalProjectCount!=0)
        			{
	        			monthEntity.setBaselineDuration(Math.round(totalBaselineDuration/30.0/totalProjectCount));
	        			monthEntity.setCurrentDuration(Math.round(totalCurrentDuration/30.0/totalProjectCount));
        			}
        			else //Avoid divide by zero exception for future month that doesn't have any active projects.
        			{
	        			monthEntity.setBaselineDuration(0L);
	        			monthEntity.setCurrentDuration(0L);
        			}
        			outputMonthlyMap.put(new Integer(i).toString(), monthEntity);
      			
	        	}
	        	else if(i%100 == 13)
	        		i=(i/100+1)*100;
	        }
		    		    
		    return outputMonthlyMap;
    
		} catch(Exception e) 
		{
			 System.out.println(e.getMessage());
		}		
		return null;
	}
	
	
	public Boolean isInConstruction(String dataPeriod, String startDate, String endDate){
		//TODO Assume check point for each data period is the end of each month
		DateTime dataPeriodDate = dataPeriodFormatter.parseDateTime(dataPeriod).plusMonths(1).minusHours(1);
		DateTime currentNTPDate = dateFormatter.parseDateTime(startDate);
		DateTime currentSCDate = dateFormatter.parseDateTime(endDate);
		if(dataPeriodDate.isAfter(currentNTPDate) && dataPeriodDate.isBefore(currentSCDate))
			return true;
		else
			return false;
	}
	
	public Long getDuration(String startDate, String endDate){
		DateTime NTPDate = dateFormatter.parseDateTime(startDate);
		DateTime SCDate = dateFormatter.parseDateTime(endDate);
		return new Integer(Days.daysBetween(NTPDate, SCDate).getDays()).longValue();
	}
	
	public void renderChart(Map<String, MonthlyEntity> currentMonthlyMap, Map<String, MonthlyEntity> comparedMonthlyMap, Long startMonth, Long endMonth, Long currentMonth, Long comparedMonth){
		double[] currentBaselineList = new double[currentMonthlyMap.size()];
		double[] currentBaselinePlus15List = new double[currentMonthlyMap.size()];//Baseline + 15% Delay
		double[] currentForecastList = new double[currentMonthlyMap.size()];
		double[] currentDashedForecastList = new double[currentMonthlyMap.size()];
		String[] labelList = new String[currentMonthlyMap.size()];

		double[] comparedForecastList = new double[comparedMonthlyMap.size()];
		double lowest=1000.0, highest=0.0;

		int index=0, noValueCount=0, historicalDataCount=0;
    	for(int i= startMonth.intValue();i<=endMonth.intValue();i++)
        {
    		if(i%100>=1 && i%100<=12)
        	{
        		currentBaselineList[index] = currentMonthlyMap.get(new Integer(i).toString()).getBaselineDuration().doubleValue();
        		currentBaselinePlus15List[index] = new BigDecimal(currentBaselineList[index]*1.15).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        		currentForecastList[index] = currentMonthlyMap.get(new Integer(i).toString()).getCurrentDuration().doubleValue();
        		currentDashedForecastList[index] = currentMonthlyMap.get(new Integer(i).toString()).getCurrentDuration().doubleValue();
        		labelList[index] = dataPeriodFormatter.parseDateTime(currentMonthlyMap.get(new Integer(i).toString()).getId().toString()).monthOfYear().getAsShortText()+ " " +currentMonthlyMap.get(new Integer(i).toString()).getId().toString().substring(2,4);
        		if(i>=comparedMonth.intValue())
        		{
	        		comparedForecastList[index] = comparedMonthlyMap.get(new Integer(i).toString()).getCurrentDuration().doubleValue();
	        		if(comparedForecastList[index]<lowest)
	        			lowest = comparedForecastList[index];
	        		if(comparedForecastList[index]>highest)
	        			highest = comparedForecastList[index];
        		}
        		else
        			noValueCount++;
        		
        		if(i<=currentMonth.intValue())
        			historicalDataCount++;
        		
        		if(currentBaselineList[index]<lowest)
        			lowest = currentBaselineList[index];

        		if(currentForecastList[index]<lowest)
        			lowest = currentForecastList[index];

        		if(currentForecastList[index]>highest)
        			highest = currentForecastList[index];
        		
        		index++;
        	}
    		else if(i%100 == 13)
        		i=(i/100+1)*100;
        }
					    
		// The data for the line chart
		double[] data0 = {42, 49, 33, 38, 51, 46, 29, 41, 44, 57, 59, 52, 37, 34, 51, 56, 56,
		    60, 70, 76, 63, 67, 75, 64, 51};
		double[] data1 = {50, 55, 47, 34, 42, 49, 63, 62, 73, 59, 56, 50, 64, 60, 67, 67, 58,
		    59, 73, 77, 84, 82, 80, 84, 98};
		double[] data2 = {36, 28, 25, 33, 38, 20, 22, 30, 25, 33, 30, 24, 28, 15, 21, 26, 46,
		    42, 48, 45, 43, 52, 64, 60, 70};

		// The labels for the line chart
		String[] labels = {"0", "1", "2", "Feb 11", "4", "5", "6", "7", "8", "9", "10", "11",
		    "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

		// Create an XYChart object of size 600 x 300 pixels, with a light blue (EEEEFF)
		// background, black border, 1 pxiel 3D border effect and rounded corners
		XYChart c = new XYChart(900, 550, 0xeeeeff, 0x000000, 1);
		c.setRoundedFrame();

		// Set the plotarea at (55, 58) and of size 520 x 195 pixels, with white background.
		// Turn on both horizontal and vertical grid lines with light grey color (0xcccccc)
		c.setPlotArea(70, 80, 800, 400, 0xffffff, -1, -1, 0xcccccc, 0xcccccc);

		// Add a legend box at (50, 30) (top of the chart) with horizontal layout. Use 9 pts
		// Arial Bold font. Set the background and border color to Transparent.
		c.addLegend(60, 40, false, "Arial Bold", 9).setBackground(Chart.Transparent);

		// Add a title box to the chart using 15 pts Times Bold Italic font, on a light blue
		// (CCCCFF) background with glass effect. white (0xffffff) on a dark red (0x800000)
		// background, with a 1 pixel 3D border.
		if(bureau.equals("BWS"))
			c.addTitle("Construction Duration Trend and Projection For BWS Managed Projects", "Times New Roman Bold Italic", 15
					).setBackground(0xccccff, 0x000000, Chart.glassEffect());
		else
			c.addTitle(operatingBureau + " Capital Program Construction Duration Trend and Projection", "Times New Roman Bold Italic", 15
				    ).setBackground(0xccccff, 0x000000, Chart.glassEffect());


		// Add a title to the y axis
		c.yAxis().setTitle("Average Construction Duration (months)");
		c.yAxis().setLabelStyle("Arial", 10, 0x101010);
		c.yAxis().setLinearScale((lowest<3? lowest : lowest-3), highest+3);

		// Set the labels on the x axis.
		c.xAxis().setLabels(labelList);
		c.xAxis().setLabelStyle("Arial", 10, 0x000000).setFontAngle(45);

		// Display 1 out of 3 labels on the x-axis.
		c.xAxis().setLabelStep(3);

		// Add a line layer to the chart
		LineLayer layer = c.addLineLayer2();

		// Set the default line width to 2 pixels
		layer.setLineWidth(2);
		
		for(int i = 0 ; i<=historicalDataCount-2;i++)
			currentDashedForecastList[i] = ChartDirector.Chart.NoValue;
		
		for(int i = currentMonthlyMap.size()-1 ; i>=historicalDataCount;i--)
			currentForecastList[i] = ChartDirector.Chart.NoValue;

		// Add the three data sets to the line layer. For demo purpose, we use a dash line
		// color for the last line
		layer.addDataSet(currentBaselineList, 0xff0000, "Baseline");
		layer.addDataSet(currentBaselinePlus15List, c.dashLineColor(0xff00ff, Chart.DashLine), "Baseline + 15% Delay");
		layer.addDataSet(currentForecastList, 0x008800, "Historical Data").setDataSymbol(Chart.TriangleShape, 5,
				0x008800, 0x008800);
		layer.addDataSet(currentDashedForecastList, c.dashLineColor(0x008800, Chart.DashLine), "Current Forecast").setDataSymbol(Chart.SquareShape, 4,
				0x008800, 0x008800);
//		layer.addDataSet(currentForecastList, c.dashLineColor(0x3333ff, Chart.DashLine), "varianceList");
//		layer.addDataSet(comparedBaselineList, c.dashLineColor(0x3333ff, Chart.DashLine), "comparedBaselineList");
//		layer.addDataSet(comparedForecastList, c.dashLineColor(0x3333ff, Chart.DashLine), "comparedForecastList");

		for(int i =0 ; i<=noValueCount-1;i++)
		{
//			comparedBaselineList[i] = ChartDirector.Chart.NoValue;
			comparedForecastList[i] = ChartDirector.Chart.NoValue;
		}
		layer.addDataSet(comparedForecastList, c.dashLineColor(0x3333ff, Chart.DashLine), dataPeriodFormatter.parseDateTime(comparedMonth.toString()).monthOfYear().getAsShortText()+ " " +comparedMonth.toString().substring(0,4) + " Snapshot");
//		layer2.addDataSet(comparedBaselineList, c.dashLineColor(0x3333ff, Chart.DashLine));	
		
		HttpServletRequest request = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		// Output the chart
		 chart1URL = c.makeSession(request, "chart1");

		 String id = "id='{value}{dataSet}{x}' ";
		 String date = "date='{xLabel}' ";
		 String data_set = "data_set='{dataSet}' ";
		 // Client side Javascript to show detail information "onmouseover"
		 String showText = "onmouseover=\"tooltip_show(\'tooltip_123\', \'{value}{dataSet}{x}\');\" ";
		 //'tooltip_show(\"'tooltip_123', {x}{value}{dataSet}, 50, 50 \");' ";

		 // Client side Javascript to hide detail information "onmouseout"
		 String hideText = "onmouseout=\"tooltip_hide(\'tooltip_123\');\" ";

		 // "title" attribute to show tool tip
		 String toolTip = "tooltip='Period: {xLabel}<br/> Value: {value}'";
		 
			// Include tool tip for the chart
		 imageMap1 = c.getHTMLImageMap("", "", date + data_set + id + showText + hideText + toolTip);

	}
}
