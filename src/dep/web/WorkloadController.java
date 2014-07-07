package dep.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import ChartDirector.Chart;
import ChartDirector.LineLayer;
import ChartDirector.XYChart;
import dep.dao.DatabaseDao;
import dep.model.WorkloadEntity;
import dep.model.WorkloadInfo;

@Controller
@RequestMapping("/workload/**")
public class WorkloadController
{
	@Autowired
	private WorkloadValidator workloadValidator;
	
	public String chart1URL;
	public String imageMap1;
	@Autowired
	DatabaseDao dao = new DatabaseDao();
	
	Map<String, List<WorkloadEntity>> workloadLevelOneMap;
	Map<String, List<WorkloadEntity>> workloadLevelTwoMap;
	Map<String, List<WorkloadEntity>> workloadLevelThreeMap;
	Map<String, List<WorkloadEntity>> workloadLevelAllMap;


	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyyMM");
	DateTimeFormatter dataPeriodFormatter = DateTimeFormat.forPattern("yyyyMM");
	
	Map<String,String> stageSelectList;
	Map<String,String> masterProgramSelectList;
	Map<String,String> managedBySelectList ;
	Map<Long,String> endPeriodSelectList;
	
	int startMonth;
	int endMonth;
	String stage;
	String masterProgram;
	String managedBy;
	Long endPeriod;

	public void generateDropdown()
	{
		
	 	stageSelectList = new LinkedHashMap<String,String>();
	 	stageSelectList.put("Design", "Design");//Back end, Front end
	 	stageSelectList.put("Construction", "Construction");
	 	stageSelectList.put("Closeout", "Closeout");
	 	
	 	masterProgramSelectList = new LinkedHashMap<String,String>();
	 	masterProgramSelectList.put("WSC", "WSC");//Back end, Front end
	 	masterProgramSelectList.put("WWC", "WWC");
	 	masterProgramSelectList.put("BEDC", "BEDC");
		
	 	managedBySelectList = new LinkedHashMap<String,String>();
	 	managedBySelectList.put("Inhouse", "Inhouse");
	 	managedBySelectList.put("Consultant", "Consultant");
	 	managedBySelectList.put("Both", "Inhouse or Consultant");
	 	
        endPeriodSelectList = new LinkedHashMap<Long,String>();
        endPeriodSelectList.put(201312L, "2013");
        endPeriodSelectList.put(201412L, "2014");
        endPeriodSelectList.put(201512L, "2015");
        endPeriodSelectList.put(201612L, "2016");
        endPeriodSelectList.put(201712L, "2017");
        endPeriodSelectList.put(201812L, "2018");
		
	}
	
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		System.out.println("workload index " + new DateTime());

		generateDropdown();
//		String SQL = "AND DS_Status = '(A)' AND convert(datetime, [Final_Design_Completed], 101) > ?month ) as schedule";
//		String month = "2012-06-01";
//		System.out.println("pre SQL: " + SQL);
//		System.out.println("month: " + month);
//		SQL = SQL.replaceAll("@month", month);
//
//		System.out.println("post SQL: " + SQL);
		
		
		ModelAndView modelAndView = new ModelAndView("workload/workload");
		modelAndView.addObject("workloadInfo", new WorkloadInfo());
		modelAndView.addObject("stageSelectList", stageSelectList);
		modelAndView.addObject("masterProgramSelectList", masterProgramSelectList);
		modelAndView.addObject("managedBySelectList", managedBySelectList);
		modelAndView.addObject("endPeriodSelectList", endPeriodSelectList);
		return modelAndView;
	}
	
    @RequestMapping(value = "/workloadResult", method = RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("workloadInfo") WorkloadInfo wi, BindingResult result) throws Exception
	{
		System.out.println("workload submit " + new DateTime());
		workloadValidator.validate(wi, result);
		if (result.hasErrors()) {
			System.out.println("errors");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("workload/workload");
			modelAndView.addObject("workloadInfo", wi);
			modelAndView.addObject("stageSelectList", stageSelectList);
			modelAndView.addObject("masterProgramSelectList", masterProgramSelectList);
			modelAndView.addObject("managedBySelectList", managedBySelectList);
			modelAndView.addObject("endPeriodSelectList", endPeriodSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;

		} else {
			

			stage = wi.getStage();
			masterProgram = wi.getMasterProgram();
			managedBy = wi.getManagedBy();
			endPeriod = wi.getEndPeriod();
			System.out.println("stage: " + stage);
			System.out.println("masterProgram: " + masterProgram);
			System.out.println("managedBy: " + managedBy);
			System.out.println("endPeriod: " + endPeriod);
			
		 	
			startMonth = dao.getLatestDataDate().intValue();
			endMonth = endPeriod.intValue();	

//			System.out.println("dao.getLatestDataDate(): " + dao.getLatestDataDate());
		 	workloadLevelOneMap = new HashMap<String, List<WorkloadEntity>>();
		 	workloadLevelTwoMap = new HashMap<String, List<WorkloadEntity>>();
		 	workloadLevelThreeMap = new HashMap<String, List<WorkloadEntity>>();
		 	workloadLevelAllMap = new HashMap<String, List<WorkloadEntity>>();
			
			

			
	        for(int i= startMonth;i<=endMonth;i++)
	        {
	        	if(i%100>=1 && i%100<=12)
	        	{
//	    			System.out.println("yyy: " + new Integer(i).toString());
//	    			System.out.println(dateFormat.format(dateFormatter.parseDateTime(new Integer(i).toString()).toDate()));
	    		 	List<WorkloadEntity> workloadList = dao.getWorkload(stage, masterProgram, managedBy, "'" + dateFormat.format(dateFormatter.parseDateTime(new Integer(i).toString()).toDate()) + "'");
	    		 	List<WorkloadEntity> levelOneList = new ArrayList<WorkloadEntity>();
	    		 	List<WorkloadEntity> levelTwoList = new ArrayList<WorkloadEntity>();
	    		 	List<WorkloadEntity> levelThreeList = new ArrayList<WorkloadEntity>();
	    		 	List<WorkloadEntity> levelAllList = new ArrayList<WorkloadEntity>();
	    		 	
	    		 	for(WorkloadEntity entity : workloadList)
	    		 	{
	    		 		if(entity.getLevel()==1)
	    		 		{
	    		 			levelOneList.add(entity);
	    		 			levelTwoList.add(entity);
	    		 			levelThreeList.add(entity);
	    		 			levelAllList.add(entity);
	    		 		}
	    	    		 else if(entity.getLevel()==2)
	    	    		{
	    		 			levelTwoList.add(entity);
	    		 			levelThreeList.add(entity);
	    		 			levelAllList.add(entity);
	    	    		}
	    		 		else if(entity.getLevel()==3)
	    		 		{
	    		 			levelThreeList.add(entity);
	    		 			levelAllList.add(entity);
	    		 		}
	    		 		else
	    		 			levelAllList.add(entity);
	    		 	}
	    		 	
	    		 	sortProjectList(levelOneList);
	    		 	sortProjectList(levelTwoList);
	    		 	sortProjectList(levelThreeList);
	    		 	sortProjectList(levelAllList);
	    		 	
	    		 	
	    		 	workloadLevelOneMap.put(new Integer(i).toString(), levelOneList);
	    		 	workloadLevelTwoMap.put(new Integer(i).toString(), levelTwoList);
	    		 	workloadLevelThreeMap.put(new Integer(i).toString(), levelThreeList);
	    		 	workloadLevelAllMap.put(new Integer(i).toString(), levelAllList);
	    		 	
	    		 	
	    		 	
//	    		 	workloadLevelAllMap.put(new Integer(i).toString(), dao.getWorkload(stage, "'" + dateFormat.format(dateFormatter.parseDateTime(new Integer(i).toString()).toDate()) + "'"));
//	    		 	System.out.println("workloadLevelOneMap.size(): " + workloadLevelOneMap.size());
//	    		 	System.out.println("workloadLevelTwoMap.size(): " + workloadLevelTwoMap.size());
//	    		 	System.out.println("workloadLevelThreeMap.size(): " + workloadLevelThreeMap.size());
//	    		 	System.out.println("workloadLevelAllMap.size(): " + workloadLevelAllMap.size());
//	        		System.out.println("xxx: " + dateFormatter.parseDateTime(new Integer(i).toString()).getYear()+ "");
//	        		dataPeriodSelectList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
	        	}
	        	else if(i%100 == 13)
	        		i=(i/100+1)*100;
	        }	
			
	        
	        renderChart(workloadLevelOneMap, workloadLevelTwoMap, workloadLevelThreeMap, workloadLevelAllMap);
			
			wi.setChart1URL("/dep/pages/getchart.jsp?" + chart1URL);
			wi.setImageMap(imageMap1);
			wi.setMap1(workloadLevelOneMap);
			wi.setMap2(workloadLevelTwoMap);
			wi.setMap3(workloadLevelThreeMap);
			wi.setMapAll(workloadLevelAllMap);

			ModelAndView modelAndView = new ModelAndView("workload/workloadResult");
			modelAndView.addObject("workloadInfo", wi);
			return modelAndView;
		}
	}
    
	public void sortProjectList(List<WorkloadEntity> projectList) {
		 Collections.sort(projectList, new Comparator<WorkloadEntity>(){
									 		public int compare(WorkloadEntity o1, WorkloadEntity o2) {
									 			WorkloadEntity we1 = o1;
									 			WorkloadEntity we2 = o2;
									 			return we1.getProjectId().compareTo(we2.getProjectId());
									 		}//End of compare
		 								}
		 );//End of Collections.sort();
	}
	
	public void renderChart(Map<String, List<WorkloadEntity>> map1, Map<String, List<WorkloadEntity>> map2, Map<String, List<WorkloadEntity>> map3, Map<String, List<WorkloadEntity>> mapAll)
	{
		double[] levelOneList = new double[map1.size()];
		double[] levelTwoList = new double[map2.size()];
		double[] levelThreeList = new double[map3.size()];
		double[] levelAllList = new double[mapAll.size()];
		String[] labelList = new String[mapAll.size()];
		
		int index=0, noValueCount=0, historicalDataCount=0;
		for(int i= startMonth;i<=endMonth;i++)
		{
        	if(i%100>=1 && i%100<=12)
        	{
//        		System.out.println("---------------------------");
//    			System.out.println(i);
//    			System.out.println(map1.get(new Integer(i).toString()).size());
//    			System.out.println(map2.get(new Integer(i).toString()).size());
//    			System.out.println(map3.get(new Integer(i).toString()).size());
//    			System.out.println(mapAll.get(new Integer(i).toString()).size());
    			levelOneList[index] = map1.get(new Integer(i).toString()).size();
    			levelTwoList[index] = map2.get(new Integer(i).toString()).size();
    			levelThreeList[index] = map3.get(new Integer(i).toString()).size();
    			levelAllList[index] = mapAll.get(new Integer(i).toString()).size();
//    			System.out.println(dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText());
        		labelList[index] = dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " +new Integer(i).toString().substring(2,4);
    			index++;
    		}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
		}

		
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
		String title = "Forecast " + masterProgram + " Workload";
		if(!managedBy.equals("Both"))
			title += " - " + managedBy;
		c.addTitle(title , "Times New Roman Bold Italic", 15
			    ).setBackground(0xccccff, 0x000000, Chart.glassEffect());


		// Add a title to the y axis
		c.yAxis().setTitle("Number of Projects in " + stage + " Phase");
		c.yAxis().setLabelStyle("Arial", 10, 0x101010);
//		c.yAxis().setLinearScale((lowest<3? lowest : lowest-3), highest+3);

		// Set the labels on the x axis.
		c.xAxis().setLabels(labelList);
		c.xAxis().setLabelStyle("Arial", 10, 0x000000).setFontAngle(45);

		// Display 1 out of 3 labels on the x-axis.
		c.xAxis().setLabelStep(3);

		// Add a line layer to the chart
		LineLayer layer = c.addLineLayer2();

		// Set the default line width to 2 pixels
		layer.setLineWidth(2);
		
//		for(int i = 0 ; i<=historicalDataCount-2;i++)
//			currentDashedForecastList[i] = ChartDirector.Chart.NoValue;
//		
//		for(int i = currentMonthlyMap.size()-1 ; i>=historicalDataCount;i--)
//			currentForecastList[i] = ChartDirector.Chart.NoValue;

		// Add the three data sets to the line layer. For demo purpose, we use a dash line
		// color for the last line
		layer.addDataSet(levelOneList, 0xff0000, "$0 - $20M");
		layer.addDataSet(levelTwoList, 0x008800, "$0 - $50");
		layer.addDataSet(levelThreeList, 0x3333ff, "$0 - $100");
		layer.addDataSet(levelAllList, 0xCC33CC, "All BEDC projects");
//		layer.addDataSet(currentForecastList, 0x008800, "Historical Data");
//		layer.addDataSet(currentDashedForecastList, c.dashLineColor(0x008800, Chart.DashLine), "Current Forecast");
//		layer.addDataSet(currentForecastList, c.dashLineColor(0x3333ff, Chart.DashLine), "varianceList");
//		layer.addDataSet(comparedBaselineList, c.dashLineColor(0x3333ff, Chart.DashLine), "comparedBaselineList");
//		layer.addDataSet(comparedForecastList, c.dashLineColor(0x3333ff, Chart.DashLine), "comparedForecastList");
//
//		for(int i =0 ; i<=noValueCount-1;i++)
//		{
//			comparedBaselineList[i] = ChartDirector.Chart.NoValue;
//			comparedForecastList[i] = ChartDirector.Chart.NoValue;
//		}
//		layer.addDataSet(comparedForecastList, c.dashLineColor(0x3333ff, Chart.DashLine), dataPeriodFormatter.parseDateTime(comparedMonth.toString()).monthOfYear().getAsShortText()+ " " +comparedMonth.toString().substring(0,4) + " Snapshot");
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
