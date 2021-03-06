package dep.web;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import ChartDirector.Chart;
import ChartDirector.LineLayer;
import ChartDirector.XYChart;
import dep.dao.DatabaseDao;
import dep.model.CostTrendEntity;
import dep.model.CostTrendInfo;
import dep.model.HistoricalTrendEntity;
import dep.model.MonthlyEntity;

@Controller
@RequestMapping("/cost_trend/**") //**: Wildcard, /trend/xyz will be mapped to this controller
//@RequestMapping("/trend") //http://localhost:8080/dep/trend/trendChart.html
public class CostTrendController
{
	
	private Logger logger = Logger.getLogger(CostTrendController.class);
	
	@Autowired
	private CostTrendValidator costTrendValidator;
	
	public String chart1URL;
	public String imageMap1;
	@Autowired
	DatabaseDao dao = new DatabaseDao();
	
	Map<String, String> projectToBureauMap;
	Map<String, HistoricalTrendEntity> map;
	Map<String, HistoricalTrendEntity> map2;
	Map<String, ArrayList<String>> PM_Map;
	Map<String, MonthlyEntity> monthlyMap;
	Map<String, MonthlyEntity> comparedMonthlyMap;
	Map<String, MonthlyEntity> monthlyJSList;
	Map<String, MonthlyEntity> comparedMonthlyJSList;
	String projectId;
	String contractType;

	DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	DateTimeFormatter dataPeriodFormatter = DateTimeFormat.forPattern("yyyyMM");
    DecimalFormat formatter = new DecimalFormat("#,###.00");
	
	Map<String,String> PMSelectList;
	Map<String,String> projectSelectList;
	Map<String,String> contractTypeSelectList ;
	Map<Long,String> currentMonthSelectList;
	Map<Long,String> endPeriodSelectList;

	public void generateDropdown()
	{
		
		PMSelectList = new LinkedHashMap<String,String>();
	 	PMSelectList.put("All", "All");//Back end, Front end
	 	PMSelectList.put("Daniel Solimando", "Daniel Solimando");
	 	PMSelectList.put("Gerard Cox", "Gerard Cox");
	 	PMSelectList.put("James Lauria", "James Lauria");
	 	PMSelectList.put("Kenneth Moriarty", "Kenneth Moriarty");
	 	PMSelectList.put("Kevin Clarke", "Kevin Clarke");
	 	PMSelectList.put("Louis Huang", "Louis Huang");
	 	PMSelectList.put("Maria Mandarino", "Maria Mandarino");
	 	PMSelectList.put("Matthew Osit", "Matthew Osit");
	 	PMSelectList.put("Paul Costa", "Paul Costa");
	 	PMSelectList.put("Paul Smith", "Paul Smith");
	 	PMSelectList.put("Roy Tysvaer", "Roy Tysvaer");
	 	PMSelectList.put("Sean McAndrew", "Sean McAndrew");
	 	PMSelectList.put("Wendy Sperduto", "Wendy Sperduto");
	 	
		
	 	projectSelectList = new LinkedHashMap<String,String>();
		
	 	contractTypeSelectList = new LinkedHashMap<String,String>();
	 	contractTypeSelectList.put("D", "Design");
	 	contractTypeSelectList.put("C", "CM");
	 	contractTypeSelectList.put("K", "Construction");
		
	 	PM_Map = new HashMap<String, ArrayList<String>>();
	 	
	 	map = new HashMap<String, HistoricalTrendEntity>();
	 	ArrayList<String> allProjectList = new ArrayList<String>();
	 	for(HistoricalTrendEntity entity : dao.getAllActiveProjects())
	 	{
	 		projectSelectList.put(entity.getProjectId(), entity.getProjectId());
	 		map.put(entity.getProjectId(), entity);
	 		ArrayList<String> list;
	 		if(PM_Map.get(entity.getPortfolioManager()) == null) {
	 			list = new ArrayList<String>();
	 			list.add(entity.getProjectId());
	 		} else {
	 			list = PM_Map.get(entity.getPortfolioManager());
	 			list.add(entity.getProjectId());
	 		}
	 		allProjectList.add(entity.getProjectId());
	 		PM_Map.put(entity.getPortfolioManager(), list);
	 	}
	 	PM_Map.put("All", allProjectList);
	}
	
	@RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request) throws Exception {
		logger.info("IP: " + request.getRemoteHost());
//	    logger.debug("This is a debug message.");
//	    logger.error("This is a error message.");
		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("cost_trend/trendChart"); // /jsp/ trend/trendChart .jsp
		modelAndView.addObject("costTrendInfo", new CostTrendInfo());
		modelAndView.addObject("PMSelectList", PMSelectList);
		modelAndView.addObject("projectSelectList", projectSelectList);
		modelAndView.addObject("contractTypeSelectList", contractTypeSelectList);
		modelAndView.addObject("PM_Map", PM_Map);
		return modelAndView;
	}
	
    @RequestMapping(value = "/trendResult", method=RequestMethod.POST)
    public ModelAndView submit(@ModelAttribute("costTrendInfo") CostTrendInfo ti, BindingResult result, HttpServletRequest request) throws Exception
	{
		logger.info("IP: " + request.getRemoteHost());
		costTrendValidator.validate(ti, result);
		if (result.hasErrors()) {
			System.out.println("errors");
			
//			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("cost_trend/trendChart");
			modelAndView.addObject("costTrendInfo", ti);
			modelAndView.addObject("PMSelectList", PMSelectList);
			modelAndView.addObject("projectSelectList", projectSelectList);
			modelAndView.addObject("contractTypeSelectList", contractTypeSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;

		} else {
			generateDropdown();
//			projectId = "CAT-213B";
//			contractType = "D";
			projectId = ti.getProjectId();
			contractType = ti.getContractType();
	
//			System.out.println("Selected projectId: " + projectId + ", contractType: " + contractType + ", map.get(projectId): " + map.get(projectId).getCurrentStage());
		 	
		 	List<CostTrendEntity> list = dao.getBudgetContractEAC(map.get(projectId), contractType);
		 	
//		 	for(CostTrendEntity entity : list)
//		 	{
//		 		System.out.println(entity.getDataPeriod() + ", " + entity.getBudget()  + ", " + entity.getEAC()  + ", " + entity.getAdjustedContractPrice()  + ", " + entity.getCurrentContractForecast() );
//		 	}
			renderChart(list);
			
			
			ti.setChart1URL("/dep/pages/getchart.jsp?" + chart1URL);
			ti.setImageMap(imageMap1);

			ModelAndView modelAndView = new ModelAndView("cost_trend/trendResult");
			modelAndView.addObject("costTrendInfo", ti);
			return modelAndView;
		}
	}

	
	public void renderChart(List<CostTrendEntity> list){
		double[] budgetList = new double[list.size()];
		double[] EACList = new double[list.size()];//Baseline + 15% Delay
		double[] adjustedContractPriceList = new double[list.size()];
		double[] currentContractForecastList = new double[list.size()];
	
		double[] spentPercentageList = new double[list.size()];
		double[] physicalPercentageList = new double[list.size()];

		
		String[] labelList = new String[list.size()];
		double lowest=100000000000.0, highest=0.0;

    	for(int i= 0;i<list.size();i++)
        {
    			budgetList[i] = list.get(i).getBudget().doubleValue();
    			EACList[i] = list.get(i).getEAC().doubleValue();
    			adjustedContractPriceList[i] = list.get(i).getAdjustedContractPrice().doubleValue();
    			currentContractForecastList[i] = list.get(i).getCurrentContractForecast().doubleValue();
    			spentPercentageList[i] = list.get(i).getSpentPercentage().doubleValue() * 100;
    			physicalPercentageList[i] = list.get(i).getPhysicalPercentage().doubleValue() * 100;
        		labelList[i] = dataPeriodFormatter.parseDateTime(list.get(i).getDataPeriod().toString()).monthOfYear().getAsShortText()+ " " +list.get(i).getDataPeriod().toString().substring(2,4);

        		if(budgetList[i]<lowest)
        			lowest = budgetList[i];
        		if(EACList[i]<lowest)
        			lowest = EACList[i];
        		if(currentContractForecastList[i]<lowest)
        			lowest = currentContractForecastList[i];
        		if(adjustedContractPriceList[i]<lowest)
        			lowest = adjustedContractPriceList[i];
        		
        		if(budgetList[i]>highest)
        			highest = budgetList[i];
        		if(EACList[i]>highest)
        			highest = EACList[i];
        		if(currentContractForecastList[i]>highest)
        			highest = currentContractForecastList[i];
        		if(adjustedContractPriceList[i]>highest)
        			highest = adjustedContractPriceList[i];

        }
    	
		// Create an XYChart object of size 600 x 300 pixels, with a light blue (EEEEFF)
		// background, black border, 1 pxiel 3D border effect and rounded corners
		XYChart c = new XYChart(950, 550, 0xeeeeff, 0x000000, 1);
		c.setRoundedFrame();

		// Set the plotarea at (55, 58) and of size 520 x 195 pixels, with white background.
		// Turn on both horizontal and vertical grid lines with light grey color (0xcccccc)
		c.setPlotArea(100, 90, 780, 400, 0xffffff, -1, -1, 0xcccccc, 0xcccccc);

		// Add a legend box at (50, 30) (top of the chart) with horizontal layout. Use 9 pts
		// Arial Bold font. Set the background and border color to Transparent.
		c.addLegend(60, 40, false, "Arial Bold", 9).setBackground(Chart.Transparent);

		// Add a title box to the chart using 15 pts Times Bold Italic font, on a light blue
		// (CCCCFF) background with glass effect. white (0xffffff) on a dark red (0x800000)
		// background, with a 1 pixel 3D border.
			c.addTitle("Cost Trend Chart - " + projectId + " " + contractTypeSelectList.get(contractType) + " Contract", "Times New Roman Bold Italic", 15
					).setBackground(0xccccff, 0x000000, Chart.glassEffect());

		// Add a title to the y axis
		if(lowest<highest) {
			c.yAxis().setTitle("Amount");
			c.yAxis().setLabelStyle("Arial", 10, 0x101010);
			c.yAxis().setLinearScale(lowest*0.8, highest*1.1);
			c.yAxis().setLabelFormat("${value|,.}");
		}
		else {
			c.yAxis().setTitle("NO DATA");
		}

		// Set the labels on the x axis.
		c.xAxis().setLabels(labelList);
		c.xAxis().setLabelStyle("Arial", 10, 0x000000).setFontAngle(45);

		// Display 1 out of 3 labels on the x-axis.
		c.xAxis().setLabelStep(3);

		// Add a line layer to the chart
		LineLayer layer = c.addLineLayer2();

		// Set the default line width to 2 pixels
		layer.setLineWidth(2);
		
		// Add the three data sets to the line layer. For demo purpose, we use a dash line
		// color for the last line
		layer.addDataSet(budgetList, 0x1F77B4, "Budget");
		layer.addDataSet(EACList, 0xFF7F0E, "EAC").setDataSymbol(Chart.CircleShape, 4,
				0xFF7F0E, 0xFF7F0E);
		layer.addDataSet(adjustedContractPriceList, 0x2CA02C, "Adjusted Contract Price(Orig + RCO)").setDataSymbol(Chart.TriangleShape, 5,
				0x2CA02C, 0x2CA02C);
		layer.addDataSet(currentContractForecastList, 0xD62728, "Current Contract Forecast(Orig + RCO + PCO)").setDataSymbol(Chart.SquareShape, 6,
				0xD62728, 0xD62728);
		
//		System.out.println("contractType: " + contractType);
		if(!contractType.equals("K"))
		{
			// Add a line layer to the chart
			LineLayer layer2 = c.addLineLayer2();
			layer2.setLineWidth(3);
			layer2.setUseYAxis2();
			// Add the pareto line using deep blue (0000ff) as the color, with circle symbols
			layer2.addDataSet(spentPercentageList, c.dashLineColor(0xAD8BC9, Chart.DashLine), "Lead " + contractTypeSelectList.get(contractType) + " Contract Current Phase Spent %").setDataSymbol(Chart.CircleShape, 5,
					0xAD8BC9, 0xAD8BC9);//66ff66 light green
			layer2.addDataSet(physicalPercentageList, c.dashLineColor(0xA8786E, Chart.DashLine), "Lead " + contractTypeSelectList.get(contractType) + " Contract Current Phase Physical % Complete").setDataSymbol(Chart.SquareShape, 6,
					0xA8786E, 0xA8786E);//66ff66 light green
	
			// Tool tip for the line layer
			layer2.setHTMLImageMap("", "", "title='{xLabel}: {value}%'");
			c.yAxis2().setTitle("Percentage");
			c.yAxis2().setLabelFormat("{value}%");
			c.yAxis2().setLinearScale(0, 100, 20);
		}
		
		HttpServletRequest request = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		// Output the chart
		 chart1URL = c.makeSession(request, "chart1");

		 // "title" attribute to show tool tip
		 String toolTip = "title='{xLabel}: ${value|,.}'";
		 
			// Include tool tip for the chart
		 imageMap1 = c.getHTMLImageMap("", "", toolTip);

	}
	
    @RequestMapping(value = "/getURL", method = RequestMethod.GET)
	public @ResponseBody String getChart1URL() {
    	System.out.println("getChart1URL");
		return "index";
	}

	public void setChart1URL(String chart1url) {
		chart1URL = chart1url;
	}
	
    @RequestMapping(value = "/getProjectsByPM", method = RequestMethod.POST)
	public @ResponseBody List<String> getProjectsByPM(@RequestBody String PM) {
    	System.out.println("getProjectsByPM: " + PM);
    	ArrayList<String> list;
    	if(PM.equals("NONE") || PM.equals("All"))
    		list = PM_Map.get("All");
    	else
    		list = PM_Map.get(PM);
    	
		return list;
	}
}
