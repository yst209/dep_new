package dep.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.Months;
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
import dep.model.BatchInfo;
import dep.model.CobraCostDataEntity;

@Controller
@RequestMapping("/batch/**")
public class BatchController
{
	@Autowired
	private BatchValidator batchValidator;
	
	public String dataPeriod;
	public String sourceFolder;
	public String reportFolder;
	public String tempFolder;
	public String backupFolder;
	public String coordinator;
	private FileWriter writer;
	public List<String> projectList;
	public List<String> validatedProjectList;
	public List<String> errorProjectList;
	public List<String> CSVFileTypes = new ArrayList<String>(Arrays.asList("ACWP", "RES Fore", "ACT Fore", "RES 12MTH", "ACT 12MTH", "RES RCO", "ACT RCO", "RES PCO", "ACT PCO", "Schedule"));
	DateTime dataPeriodDate;
	String dataPeriodDateToString;
	ArrayList<String> storeValues;
	
	DatabaseDao dao = new DatabaseDao();
	
	public String chart1URL;
	public String imageMap1;
//	@Resource
//    private DatabaseDao databaseDao;
	private @Autowired
	HttpServletRequest request;
	Map<String, String> cobraStatusDateMap;
	Boolean updateTotalsOnly;

	DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
//	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	DateTimeFormatter dataPeriodFormatter = DateTimeFormat.forPattern("yyyyMM");
	DateTimeFormatter yearMonthFormatter = DateTimeFormat.forPattern("yyyy-MM");
	DateTimeFormatter dateHourMinuteFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HHmm");

	Map<Long,String> dataPeriodSelectList;
	Map<String,String> coordinatorSelectList;
	
	public static String newLine = System.getProperty("line.separator");
	
	public void generateDropdown()
	{
	 	dao.setDataSource();
	 	
		dataPeriodSelectList = new LinkedHashMap<Long,String>();
		
        for(int i= new Integer(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).minusMonths(1).toDate()));i<=new Integer(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).toDate()));i++)
        {
        	if(i%100>=1 && i%100<=12)
        	{
        		dataPeriodSelectList.put(new Integer(i).longValue(), dataPeriodFormatter.parseDateTime(new Integer(i).toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(new Integer(i).toString()).getYear());
        	}
        	else if(i%100 == 13)
        		i=(i/100+1)*100;
        }	

//		dataPeriodSelectList.put(new Long(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).minusMonths(1).toDate())), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).minusMonths(1).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).minusMonths(1).getYear());
//		dataPeriodSelectList.put(dao.getLatestDataDate(), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).getYear());
//		dataPeriodSelectList.put(new Long(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(1).toDate())), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(1).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(1).getYear());

//		dataPeriodSelectList.put(new Long(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(2).toDate())), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(2).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(2).getYear());
//		dataPeriodSelectList.put(new Long(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(3).toDate())), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(3).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(3).getYear());
//		dataPeriodSelectList.put(new Long(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(4).toDate())), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(4).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(4).getYear());
//		dataPeriodSelectList.put(new Long(dateFormat.format(dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(5).toDate())), dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(5).monthOfYear().getAsShortText()+ " " + dataPeriodFormatter.parseDateTime(dao.getLatestDataDate().toString()).plusMonths(5).getYear());
		 
        
        coordinatorSelectList = new LinkedHashMap<String,String>();
        coordinatorSelectList.put("Alyson", "Alyson");//Back end, Front end
        coordinatorSelectList.put("Byron", "Byron");
        coordinatorSelectList.put("Daniel", "Daniel");
        coordinatorSelectList.put("Frank", "Frank");
        coordinatorSelectList.put("Rosalina", "Rosalina");
        coordinatorSelectList.put("Vijaya", "Vijaya");
        coordinatorSelectList.put("Vincent", "Vincent");
	}
	
	@RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		System.out.println("batch index from IP: " + request.getRemoteHost()+ " at " + new DateTime());
		
		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("batch/batch");
		modelAndView.addObject("batchInfo", new BatchInfo());
		modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
		modelAndView.addObject("coordinatorSelectList", coordinatorSelectList);
		return modelAndView;
	}
	
    @RequestMapping(value = "/batchResult", method = RequestMethod.POST)
    synchronized public ModelAndView submit(@ModelAttribute("batchInfo") BatchInfo bi, BindingResult result) throws Exception
	{
    	System.out.println("submit at " + new DateTime());
		batchValidator.validate(bi, result);
		if (result.hasErrors()) {
			System.out.println("errors");
			
			generateDropdown();
			ModelAndView modelAndView = new ModelAndView("batch/batch");
			modelAndView.addObject("batchInfo", bi);
			modelAndView.addObject("dataPeriodSelectList", dataPeriodSelectList);
			modelAndView.addObject("coordinatorSelectList", coordinatorSelectList);
			modelAndView.addObject("errors", result);
			return modelAndView;
		} else {
			System.out.println("no errors");
			
			projectList = new ArrayList<String>();
			validatedProjectList = new ArrayList<String>();
			errorProjectList = new ArrayList<String>();
			
	
			dataPeriod = bi.getDatePeriod().toString();
			coordinator = bi.getCoordinator();
//			dataPeriod = "201207";
//			coordinator = "Vincent";
			
	//		if(bi.getSourcePath().endsWith("\\"))
	//			sourceFolder = bi.getSourcePath().substring(0, bi.getSourcePath().length()-1).replace("\\", "\\\\");
	//		else
	//			sourceFolder = bi.getSourcePath().replace("\\", "\\\\");
	//		
	//		if(bi.getTargetPath().endsWith("\\"))
	//			backupFolder = bi.getTargetPath().substring(0, bi.getTargetPath().length()-1).replace("\\", "\\\\");
	//		else
	//			backupFolder = bi.getTargetPath().replace("\\", "\\\\");
			
			System.out.println("-----------------");
	
			
		
	
//			System.out.println("new DateTime(): " + new DateTime().toLocalTime()); //10:46:23.804
//			System.out.println("backupFolder: " + new DateTime().toLocalDate()); //2011-11-29
//			System.out.println("backupFolder: " + new DateTime().toGregorianCalendar());		
			
			
			//Hard coding for now
	//		dataPeriod = "201110";
	//		sourceFolder = "\\\\BEE-WASQA2\\C$\\CobraCSVFiles";
	//		backupFolder = "\\\\BEE-WASQA2\\C$\\SavedFolder";
			
			sourceFolder = "C:\\ProjectControl\\" + coordinator + "\\CobraCSVFiles";
			reportFolder = "C:\\ProjectControl\\" + coordinator + "\\Reports";
			tempFolder = "C:\\ProjectControl\\temp_" + coordinator;
			backupFolder = "C:\\ProjectControl\\BackupFolder";
			
			System.out.println("sourceFolder: " + sourceFolder);
			System.out.println("backupFolder: " + backupFolder);	
			System.out.println("tempFolder: " + tempFolder);
			
			dataPeriodDate = dataPeriodFormatter.parseDateTime(dataPeriod).plusMonths(1).minusHours(1);
			
			//This is to take care of the month format when month is in single digit (Jan - Sept)
			//CSV file name will be 2012-1-31, not 2012-01-31.
			dataPeriodDateToString = dataPeriodDate.getYear()+"-"+dataPeriodDate.monthOfYear().get()+"-"+dataPeriodDate.dayOfMonth().get();
			System.out.println("dataPeriodDateToString: " + dataPeriodDateToString); 

			createFolder(reportFolder);
			createFolder(tempFolder);
			createFolder(backupFolder);
	
			
			
			//Get project list to be processed.
			getProjectList("C:\\ProjectControl\\" + coordinator + "\\project_list.csv");
			
			updateTotalsOnly = bi.getUpdateTotalsOnly();
			dao.setDataSource();
		 	cobraStatusDateMap = dao.getCobraStatusDate();
			if(updateTotalsOnly)
				runUpdate("update_totals");//advance_calendar
			else
			{
				runBatchProcess();
				generateReport();
			}
			
			deleteDir(new File(tempFolder));
			
			System.out.println("Done at " + new DateTime());
	
			bi.setValidatedProjectList(validatedProjectList);
			bi.setErrorProjectList(errorProjectList);
			
			ModelAndView modelAndView = new ModelAndView("batch/batchResult");
			modelAndView.addObject("batchInfo", bi);
			return modelAndView;
		}
	}

    private void getProjectList(String filePath)
	{
		System.out.println("getProjectList");

		BufferedReader br = null;
		try 
		{
			String line;
			
			br = new BufferedReader( new FileReader(filePath));
 
			while( (line = br.readLine()) != null)
			{
				System.out.println(line);
				projectList.add(line);
			}
			br.close();
			
			System.out.println(projectList);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(br != null){
                try {
                	br.close();
                } catch (IOException e) {
                    //do something clever with the exception
                }
            }
			
		}
	}	
	
	public boolean validateProjectFiles(String projectID)
	{
		BufferedReader br = null;
		try {
			Map<String, String> tempMap = new HashMap<String, String>();
//			String fileName="C:\\CobraCSVFiles\\26W-11\\2011-09\\26W-11 ACT 12MTH 2011-10-31.csv";
			String fileName;
			for(String fileType : CSVFileTypes)
			{
				fileName=sourceFolder + "\\" + projectID + " " + fileType + " " + dataPeriodDateToString + ".csv";
				System.out.println(fileName);
				String line;
				 
				ArrayList<String> storeValues = new ArrayList<String>();
				//storeValues.clear();//just in case this is the second call of the ReadFile Method./
				br = new BufferedReader( new FileReader(fileName));
	 
				while( (line = br.readLine()) != null)
				{
					if(line.contains("ERROR") || line.contains("FOUND"))
					{
						errorProjectList.add(projectID + ": Error occurred in " + projectID + " " + fileType);
						System.out.println("Error occurred in " + projectID + " " + fileType);
						return false;
					}
					else if(fileType.equals("ACWP"))
					{
						if(tempMap.get(fileType + " - " + line.split(",")[0]+line.split(",")[1]+line.split(",")[2]+line.split(",")[3])==null)
							tempMap.put(fileType + " - " + line.split(",")[0]+line.split(",")[1]+line.split(",")[2]+line.split(",")[3], "");
						else
						{
							errorProjectList.add(projectID + ": Found duplicate records in " + projectID + " " + fileType);
							System.out.println("Found duplicate records in " + projectID + " " + fileType);
							return false;
						}
					}
					else if(fileType.subSequence(0, 3).equals("RES"))
					{
						if(tempMap.get(fileType + " - " + line.split(",")[0]+line.split(",")[1]+line.split(",")[2])==null)
							tempMap.put(fileType + " - " + line.split(",")[0]+line.split(",")[1]+line.split(",")[2], "");
						else
						{
							errorProjectList.add(projectID + ": Found duplicate records in " + projectID + " " + fileType);
							System.out.println("Found duplicate records in " + projectID + " " + fileType);
							return false;
						}
					}
	 
				}
				br.close();
			
			}

		} 
		catch (FileNotFoundException e) {
			errorProjectList.add(projectID + ": File not found");
			System.out.println(projectID + ": File not found");
			return false;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			errorProjectList.add(projectID + ": Error occured");
			e.printStackTrace();
			return false;
		}
		finally
		{
			if(br != null){
                try {
                	br.close();
                } catch (IOException e) {
                    //do something clever with the exception
                }
            }
			
		}
 
		return true;
	}	
	
	
	public boolean validateLogFile(File file)
	{
		BufferedReader br = null;
		String projectID = file.getName().split("Batch")[0]; //BB-57BatchAPILog_03-27-2014 Thu - 1124.txt

		try {
			String line;
			br = new BufferedReader( new FileReader(file));
 
			while( (line = br.readLine()) != null)
			{
				if(line.contains("[Error] Unable to login"))
				{
					validatedProjectList.remove(projectID);
					errorProjectList.add(projectID + ": Unable to update. No Cobra license available at this time. Please run again later.");
					System.out.println("[Error] Unable to login.");
					return false;
				}
			}
			br.close();
			

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			errorProjectList.add(projectID + ": Error occured");
			e.printStackTrace();
			return false;
		}
		finally
		{
			if(br != null){
                try {
                	br.close();
                } catch (IOException e) {
                    //do something clever with the exception
                }
            }
			
		}
 
		return true;
	}	
	
	
	public void createFile(String ProjectID, String type)
	{
		try
		{
		    if(type.equals("advance_calendar"))
		    {
			    writer = new FileWriter(tempFolder + "\\monthlyStatus_" + ProjectID + ".txt");
			    
			    //Code for advance calendar only
				//Cobra Status date vs data period
				//To auto advance calendar to the right month
				for(int i = 0; i<Months.monthsBetween(dateFormatter.parseDateTime(cobraStatusDateMap.get(ProjectID)).plusDays(1).minusMonths(1), dataPeriodDate).getMonths();i++)
				{
					System.out.println("run");
				
				    writer.append("[Process001]\n");
				    writer.append("ProcessID=AdvanceCalendar\n");
				    writer.append("Project=" + ProjectID + "\n");
				    writer.append("\n");
				}
		    }
		    else if(type.equals("update_totals"))
		    {
			    writer = new FileWriter(tempFolder + "\\monthlyStatus_" + ProjectID + ".txt");
			    
			    writer.append("[Process008]\n");
			    writer.append("ProcessID=UpdateTotals\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("Total=S,P,A,E\n");
			    writer.append("\n\n\n");
		    }
		    else if(type.equals("txt"))
		    {
			    writer = new FileWriter(tempFolder + "\\monthlyStatus_" + ProjectID + ".txt");
			    
				//Cobra Status date vs data period
				//To auto advance calendar to the right month
				for(int i = 0; i<Months.monthsBetween(dateFormatter.parseDateTime(cobraStatusDateMap.get(ProjectID)).plusDays(1).minusMonths(1), dataPeriodDate).getMonths();i++)
				{
					System.out.println("run");
				
				    writer.append("[Process001]\n");
				    writer.append("ProcessID=AdvanceCalendar\n");
				    writer.append("Project=" + ProjectID + "\n");
				    writer.append("\n");
				}

			    writer.append("[Process002]\n");
			    writer.append("ProcessID=IntegrateProjectData\n");
			    writer.append("ConfigurationName=Schedule Update\n");				
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("StatusFile=" + tempFolder + "\\" + ProjectID + " Schedule " + dataPeriodDateToString + ".csv" + "\n");
				
//				writer.append("[Process002]\n");
//			    writer.append("ProcessID=IntegrateProjectData\n");
//			    writer.append("ConfigurationName=P6 Update - " + ProjectID + "\n");
			    writer.append("\n");
			    writer.append("[Process003]\n");
			    writer.append("ProcessID=IntegrateActualCosts\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("ConfigurationName= NYC DEP Load Actuals\n");
			    writer.append("TransactionFile=" + tempFolder + "\\" + ProjectID + " ACWP " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("\n");
			    writer.append("[Process004]\n");
			    writer.append("ProcessID=CalculateEV\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("\n");
			    writer.append("[Process005]\n");
			    writer.append("ProcessID=CalculateForecast\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("ClassList=Forecast,CALCETC,12MTH_FCAST\n");
			    writer.append("\n");
			    writer.append("[Process006]\n");
			    writer.append("ProcessID=IntegrateProjectData\n");
			    writer.append("ConfigurationName=RCO\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("ActivityFile=" + tempFolder + "\\" + ProjectID + " ACT RCO " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("ResourceAssignmentFile=" + tempFolder + "\\" + ProjectID + " RES RCO " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("\n");
			    writer.append("[Process007]\n");
			    writer.append("ProcessID=IntegrateProjectData\n");
			    writer.append("ConfigurationName=PCO\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("ActivityFile=" + tempFolder + "\\" + ProjectID + " ACT PCO " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("ResourceAssignmentFile=" + tempFolder + "\\" + ProjectID + " RES PCO " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("\n");
			    writer.append("[Process008]\n");
			    writer.append("ProcessID=IntegrateProjectData\n");
			    writer.append("ConfigurationName=Forecast 2.FORECAST CLASS\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("ActivityFile=" + tempFolder + "\\" + ProjectID + " ACT Fore " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("ResourceAssignmentFile=" + tempFolder + "\\" + ProjectID + " RES Fore " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("\n");
			    writer.append("[Process009]\n");
			    writer.append("ProcessID=IntegrateProjectData\n");
			    writer.append("ConfigurationName=Forecast 1.12MTH_FCAST\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("ActivityFile=" + tempFolder + "\\" + ProjectID + " ACT 12MTH " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("ResourceAssignmentFile=" + tempFolder + "\\" + ProjectID + " RES 12MTH " + dataPeriodDateToString + ".csv" + "\n");
			    writer.append("\n");
			    writer.append("[Process010]\n");
			    writer.append("ProcessID=UpdateTotals\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("Total=S,P,A,E\n");
			    writer.append("\n");
			    writer.append("[Process011]\n");
			    writer.append("ProcessID=BackupProject\n");
			    writer.append("Project=" + ProjectID + "\n");
			    writer.append("Destination=" + tempFolder + "\n");
			    writer.append("\n\n\n");
		    }
		    else if(type.equals("cmd"))
		    {
			    writer = new FileWriter(tempFolder + "\\BatchProcessCSV_" + ProjectID + ".cmd");
			    
			    writer.append("@echo off\n");
			    writer.append(" cls\n");
			    writer.append("\n");
			    writer.append("rem Setup the Project\n");
			    writer.append(" set CobraEXEPath=%programfiles%\\Deltek\\Cobra 5\n");
			    writer.append(" set CobraLogPath=%userprofile%\\documents\\deltek\\Cobra\\Logs\\Batch.Api.log\n");
			    writer.append(" set CSVrootPath=" + tempFolder + "\n");
			    writer.append("\n");
			    writer.append(" set projectID=" + ProjectID + "\n");
			    writer.append("\n");
			    writer.append(" set CobraScriptFile=%CSVrootPath%\\monthlyStatus_%projectID%.txt \n");
			    writer.append(" set CobraUser=byron\n");
			    writer.append(" set CobraPWD=stellar\n");
			    writer.append("\n");
			    writer.append("@echo on \n");
			    writer.append("\n");
			    writer.append("\"%CobraEXEPath%\\Cobra.Api.exe\" script:%CobraScriptFile% user:%CobraUser%/%CobraPWD%\n");
			    writer.append("\n");
			    writer.append("@echo off\n");
			    writer.append("\n");
			    writer.append("rem Parse the date (e.g., Mon 10/31/2011)\n");
			    writer.append(" set cur_yyyy=%date:~10,4%\n");
			    writer.append(" set cur_mm=%date:~4,2%\n");
			    writer.append(" set cur_dd=%date:~7,2%\n");
			    writer.append("\n");
			    writer.append("rem Parse the time (e.g., 11:17:13.49)\n");
			    writer.append(" set cur_hh=%time:~0,2%\n");
			    writer.append(" if %cur_hh% lss 10 (set cur_hh=0%time:~1,1%)\n");
			    writer.append(" set cur_nn=%time:~3,2%\n");
			    writer.append(" set cur_ss=%time:~6,2%\n");
			    writer.append(" set cur_ms=%time:~9,2%\n");
			    writer.append("\n");
			    writer.append("rem check for the Deltek Batch api log, move it to project CSV folder\n");
//			    writer.append(" if not exist \"%CSVrootPath%\\%projectID%\" md \"%CSVrootPath%\\%projectID%\"\n");
			    writer.append("\n");
			    writer.append(" if exist %CobraLogPath% move \"%CobraLogPath%\" \"%CSVrootPath%\\%projectID%BatchAPILog_%date:/=-% - %cur_hh%%cur_nn%.txt\"\n");
			    writer.append("\n");
			    writer.append("rem Clear the environment variables\n");
			    writer.append(" set cur_yyyy=\n");
			    writer.append(" set cur_mm=\n");
			    writer.append(" set cur_dd=\n");
			    writer.append(" set cur_hh=\n");
			    writer.append(" set cur_nn=\n");
			    writer.append(" set cur_ss=\n");
			    writer.append(" set cur_ms=\n");
			    writer.append(" set timestamp=\n");
			    writer.append(" set CobraEXEPath= \n");
			    writer.append(" set CobraLogPath=\n");
			    writer.append(" set CSVrootPath=\n");
			    writer.append(" set CobraScriptFile=\n");
			    writer.append(" set CobraUser=\n");
			    writer.append(" set CobraPWD= \n");
			    writer.append(" set projectID=");
		    }
	 
	 
		    writer.flush();
		    writer.close();
	    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void runBatchFile(String filePath)
	{
		Runtime runtime = Runtime.getRuntime();
		try
		{
			Process p1 = runtime.exec("cmd /c "+ filePath);
			InputStream is = p1.getInputStream();
			int i = 0;
			while( (i = is.read() ) != -1)
			{
				System.out.print((char)i);
			}

		}
		catch(IOException ioException)
		{
			System.out.println(ioException.getMessage() );
		}
	}
	
	private void runBatchProcess()
	{
		
		try
		{
			//Looping through the list
			for(String project: projectList)
			{
				//If the file is ok, copy it to temp folder.
				if(validateProjectFiles(project))
				{
					validatedProjectList.add(project);
					for(String fileType : CSVFileTypes)
					{
						moveFile(sourceFolder + "\\" + project + " " + fileType + " " + dataPeriodDateToString + ".csv", tempFolder + "\\" + project + " " + fileType + " " + dataPeriodDateToString + ".csv");
					}
	
					createFile(project, "txt");
					createFile(project, "cmd");
					
					runBatchFile(tempFolder + "\\BatchProcessCSV_" + project + ".cmd");
				}
				
			}
			
			//Move files to saved project folder
			for(String project: validatedProjectList)
			{
				createFolder(backupFolder + "\\" + project);
				if(new File(backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter)).exists())
					deleteDir(new File(backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter)));
				createFolder(backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter));
				for(String fileType : CSVFileTypes)
				{
					
					moveFile(tempFolder + "\\" + project + " " + fileType + " " + dataPeriodDateToString + ".csv", backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter) + "\\" + project + " " + fileType + " " + dataPeriodDateToString + ".csv");
				}
				moveFile(tempFolder + "\\" + project + ".CMP", backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter) + "\\" + project + ".CMP");
				moveFile(tempFolder + "\\" + "BatchProcessCSV_" + project + ".cmd", backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter) + "\\" + "BatchProcessCSV_" + project + ".cmd");
				moveFile(tempFolder + "\\" + "monthlyStatus_" + project + ".txt", backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter) + "\\" + "monthlyStatus_" + project + ".txt");

			}

			File directory = new File(tempFolder);
			File[] files = directory.listFiles();
					
			for (File file : files)
			{
				validateLogFile(file);
				copyFile(file.toString(), reportFolder + "\\"  + "Log_" + new DateTime().toString(dateHourMinuteFormatter) + ".txt", true);
				
				// Delete each file
				System.out.println("file: " + file);
				if (!file.delete())
				{
					// Failed to delete file
					System.out.println("Failed to delete "+file);
				}
			}			
//			Thread.sleep(10000);
		
		}
		catch(Exception exception)
		{
			System.out.println(exception.getMessage() );
		}
	}

	
	public void runUpdate(String option)
	{
		try
		{
			//Looping through the list
			for(String project: projectList)
			{
					createFile(project, option);
					createFile(project, "cmd");
					runBatchFile(tempFolder + "\\BatchProcessCSV_" + project + ".cmd");
			}
		
		}
		catch(Exception exception)
		{
			System.out.println(exception.getMessage() );
		}
	}
	
	public void createFolder(String folderPath)
	{
		System.out.println("Folder: " + folderPath + " created." );
		new File(folderPath).mkdir();
	}
	
	public void deleteFile(String filePath)
	{
		File file = new File(filePath);
		if (!file.delete())
		{
			// Failed to delete file
			System.out.println("Failed to delete "+ file);
		}
	}
	
	//If destinationFile exists, the source file will not be removed.
	public void moveFile(String sourceFilePath, String destinationFilePath)
	{
//		System.out.println("moveFile, sourceFilePath: " + sourceFilePath);
//		System.out.println("moveFile, destinationFilePath: " + destinationFilePath);
		if(new File(destinationFilePath).exists())
			deleteFile(destinationFilePath);
		
		new File(sourceFilePath).renameTo(new File(destinationFilePath));
	}
	
	public void copyFile(String srFile, String dtFile, boolean append)
	{
		try
		{
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);
		  
			//For Append the file.
			//OutputStream out = new FileOutputStream(f2,true);

			//For Overwrite the file.
			OutputStream out = new FileOutputStream(f2, append);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File Copied.");
		}
		catch(FileNotFoundException ex)
		{
			System.out.println(ex.getMessage() + " in the specified directory.");
		}
		catch(IOException e){
			System.out.println(e.getMessage());  
		}
	}

	// Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }

	public void appendReport(String project, String fileType)
	{
		BufferedReader br = null;
		try
		{
			
			String fileName;
	
			fileName=backupFolder + "\\" + project + "\\" + dataPeriodDate.toString(yearMonthFormatter) + "\\" + project + " " + fileType + " " + dataPeriodDateToString + ".csv";
			System.out.println(fileName);
			String line;
	
			br = new BufferedReader( new FileReader(fileName));
	
			StringTokenizer st = null;
	
			while( (line = br.readLine()) != null)
			{
				//Replace empty cell with NULL.
				line = line.replace(",,", ",NULL,");
				
				if(!line.contains("Resource"))// if not header row
				{
					st = new StringTokenizer(line, ",");
					String newLine="";
					if(fileType.equals("RES Fore"))
					{
						String[] array = st.nextToken().split("\\.");
						newLine += array[0];//WBS
						newLine += "," + array[0] + "." + array[1];//WBS2
						newLine += "," + array[0] + "." + array[1] + "." + array[2];//WBS3
						
						newLine += "," + st.nextToken();//Resource
						newLine += ",Forecast";//CostSet
						newLine += "," + st.nextToken().split("\\.")[0] + "\n"; //Value 100.00 -> 100
					}
					else if(fileType.equals("ACWP"))
					{
						String[] array = st.nextToken().split("\\.");
						newLine += array[0];
						newLine += "," + array[0] + "." + array[1];
						newLine += "," + array[0] + "." + array[1] + "." + array[2];
	
						st.nextToken();
						newLine += "," + st.nextToken();
						newLine += "," + st.nextToken();
						st.nextToken();
						newLine += "," + st.nextToken().split("\\.")[0] + "\n";
					}
					else if(fileType.equals("RES 12MTH"))
					{
						String[] array = st.nextToken().split("\\.");
						newLine += array[0];
						newLine += "," + array[0] + "." + array[1];
						newLine += "," + array[0] + "." + array[1] + "." + array[2];
						
						newLine += "," + st.nextToken();
						newLine += ",12MTH_FCAST";
						newLine += "," + st.nextToken().split("\\.")[0] + "\n";
					}
					else if(fileType.equals("RES RCO"))
					{
						String[] array = st.nextToken().split("\\.");
						newLine += array[0];
						newLine += "," + array[0] + "." + array[1];
						newLine += "," + array[0] + "." + array[1] + "." + array[2];
						
						newLine += "," + st.nextToken();
						newLine += ",RCO";
						newLine += "," + st.nextToken().split("\\.")[0] + "\n";
					}
					else if(fileType.equals("RES PCO"))
					{
						String[] array = st.nextToken().split("\\.");
						newLine += array[0];
						newLine += "," + array[0] + "." + array[1];
						newLine += "," + array[0] + "." + array[1] + "." + array[2];
						
						newLine += "," + st.nextToken();
						newLine += ",PCO";
						newLine += "," + st.nextToken().split("\\.")[0] + "\n";
					}
	
					storeValues.add(newLine.replaceAll("\"", ""));
//					System.out.println("newLine: " + newLine);
	
				}
			}
			br.close();
		
		}
		catch(Exception e){
			System.out.println("Exception: " + e.getMessage());  
		}
		finally
		{
			if(br != null){
                try {
                	br.close();
                } catch (IOException e) {
                    //do something clever with the exception
                }
            }
			
		}
	}
	public void generateReport()
	{
		try
		{
			//26W-11 RES Fore 2011-10-31.csv
			//26W-11 ACWP 2011-10-31.csv
			//26W-11 RES 12MTH 2011-10-31.csv
			
//			getProjectList();
			
		    writer = new FileWriter(reportFolder + "\\"  + "Report_" + new DateTime().toString(dateHourMinuteFormatter) + ".csv");
		    
		    storeValues = new ArrayList<String>();
//		    storeValues.add("WBS, WBS2, WBS3, Resource, Cost Set, Value, Mark, Cobra value\n");
		
			for(String project: validatedProjectList)
			{
				appendReport(project, "ACWP");
				appendReport(project, "RES Fore");
				appendReport(project, "RES 12MTH");
				appendReport(project, "RES RCO");
				appendReport(project, "RES PCO");
			}
			

		    
		    
		 	dao.setDataSource();
		 	
		 	List<CobraCostDataEntity> list = dao.getCobraCostData();
		 	Map<String, CobraCostDataEntity> map = new HashMap<String, CobraCostDataEntity>();
		 	for(CobraCostDataEntity entity : list)
		 	{
		 		map.put(entity.getWBS() + entity.getContractRegNumber() + entity.getCLASS(), entity);
//		 		System.out.println(c.getProject());
//		 		System.out.println("x.getVALUE().toString()" + c.getVALUE().toString().split("\\.")[0]);
//		 		System.out.println("entity.getVALUE().toString()" + Integer.valueOf(entity.getVALUE().intValue()).toString());
//		 		System.out.println("z.getVALUE().toString()" + c.getVALUE().intValueExact());
//		 		System.out.println("xxx.getVALUE().toString()" + c.getVALUE().toBigIntegerExact().toString());
		 	}
		 	
		 	CobraCostDataEntity tmp;
		 	
		 	List<String> updatedValues = new ArrayList<String>();
		 	updatedValues.add("WBS,WBS2,WBS3,Resource,CostSet,Value,Mark,Cobra Value\n");
		 	
			for(String line: storeValues)
			{
//				System.out.println("now line: " + line);
				String[] array = line.split(",");				                                           //Value in Cobra != Value in CSV
				if(map.get(array[2]+array[3]+array[4]) != null && map.get(array[2]+array[3]+array[4]).getVALUE().longValue()!=Long.parseLong(array[5].trim()))
				{
					//Should not go here
					//If it hits here, there's something wrong.
					
					updatedValues.add(line.replace("\n", ",Not Matched," + map.get(array[2]+array[3]+array[4]).getVALUE() + "\n"));

					tmp = map.get(array[2]+array[3]+array[4]);
					tmp.setMark("Not Matched");
					tmp.setValueInCSV(array[5]);
					map.put(array[2]+array[3]+array[4], tmp);
					
//					System.out.println("line: " + line);
				}
				else if(map.get(array[2]+array[3]+array[4]) != null)//Value in Cobra == Value in CSV
				{

					updatedValues.add(line.replace("\n", ",Updated," + map.get(array[2]+array[3]+array[4]).getVALUE() + "\n"));
					
					tmp = map.get(array[2]+array[3]+array[4]);
					tmp.setMark("Updated");
					map.put(array[2]+array[3]+array[4], tmp);
//					System.out.println("line: " + line);

				}
				else
				{
//					System.out.println("before replace line: " + line);
					updatedValues.add(line.replace("\n", ",Not Found in Cobra\n"));
//					System.out.println("after replace line: " + line);
				}
			}

			//Print Report_YYYY-MM-DD file
			for(String line: updatedValues)
				writer.append(line);
			
		    writer.flush();
		    writer.close();
		    
		
		    //Print Cobra Cost Data Dump
		    writer = new FileWriter(reportFolder + "\\"  + "Cobra_Cost_Data_Dump_" + new DateTime().toString(dateHourMinuteFormatter) + ".csv");

		    writer.append("PROJECT, WBS, WP, REG NUMBER, Contract ID, Contract Type, Class, Value, Status Date, Mark, Value In CSV\n");
		 	

		 	Iterator<Entry<String, CobraCostDataEntity>> it = map.entrySet().iterator();
		    while (it.hasNext()) {
		    	Map.Entry<String, CobraCostDataEntity> pairs = it.next();

	        	writer.append(pairs.getValue().getProject() + ",\"" + pairs.getValue().getWBS() + "\",\"" + pairs.getValue().getWP() + "\",\"" + pairs.getValue().getContractRegNumber() + "\",\"" + pairs.getValue().getContractID() + "\"," + pairs.getValue().getContractType() + "," + pairs.getValue().getCLASS() + "," + pairs.getValue().getVALUE() + "," + pairs.getValue().getStatusDate() + "," + pairs.getValue().getMark() + "," + pairs.getValue().getValueInCSV() + "\n");
		    }
	
			
		    writer.flush();
		    writer.close();

		    
		    //Print processed and error project list
		    writer = new FileWriter(reportFolder + "\\"  + "Batch_Status_" + new DateTime().toString(dateHourMinuteFormatter) + ".txt");

		    writer.append("Validated Project List: ");
			writer.append(newLine);

			for(String project: validatedProjectList)
			{
				writer.append(project);
				writer.append(newLine);
			}

			writer.append(newLine);
			writer.append(newLine);

			writer.append("Error Project List: ");
			writer.append(newLine);

			for(String project: errorProjectList)
			{
				writer.append(project);
				writer.append(newLine);

			}
			
		    writer.flush();
		    writer.close();
		    
		}
		catch(FileNotFoundException ex)
		{
			System.out.println(ex.getMessage() + " in the specified directory.");
		}
		catch(IOException e){
			System.out.println(e.getMessage());  
		}
	}
}
