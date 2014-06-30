package dep.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dep.model.CostTrendEntity;
import dep.model.CobraCostDataEntity;
import dep.model.EstimateEntity;
import dep.model.HistoricalTrendEntity;
import dep.model.HistoricalTrendRowMapper;
import dep.model.ProjectBureauEntity;
import dep.model.ProjectBureauRowMapper;
import dep.model.SCGHistoricalTrendEntity;
import dep.model.SCGLogEntity;
import dep.model.StaffBarChartEntity;
import dep.model.WorkloadEntity;

@Repository
@Resource
public class DatabaseDao implements IDao {
//	private static String strConnect =
//		"jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ=C:\\Project_Control.mdb;";

		private DataSource dataSource;

	    private JdbcTemplate jdbcTemplate;
	    private DecimalFormat formatter = new DecimalFormat("#,###.00");
		DateTimeFormatter dateHourMinuteFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	    

	    public void setDataSource() {
		 	BasicDataSource dataSource = new BasicDataSource();
		 	dataSource.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		 	dataSource.setUrl("jdbc:jtds:sqlserver://BEE-WASQA2;databaseName=ProjectControl");
		 	dataSource.setUsername("pmis_reader");
		 	dataSource.setPassword("pmis123");
		 	DatabaseDao dao = new DatabaseDao();
//		 	dao.setDataSource(dataSource);
	    	
	    	this.dataSource = dataSource;
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }
	    
	    @Autowired
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public List<HistoricalTrendEntity> getFilteredHistoricalTrendData() {
			//For baseline information, only use the most current reported baseline from Latest_Data_Period
			String SQL = "select Data_Period, P6_Proj_id, Project_ID, P6_Orig_Proj_id, NTP, Substantial_Completion, Current_Stage, Data_Date " +
						"from P6_Historical_Trend " +
						"where ((P6_Orig_Proj_id is null And Current_Status = 'Active') OR (P6_Orig_Proj_id is not null AND Data_Period = (SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend))) " +
						"And NTP <> '' And Data_Period >= 201001 " +
						"Order by P6_Orig_Proj_id, P6_Proj_id, Data_Period";

//			String SQL = "select Data_Period, P6_Proj_id, Project_ID, P6_Orig_Proj_id, NTP, Substantial_Completion, Current_Stage, Data_Date  from P6_Historical_Trend  where ((P6_Orig_Proj_id is null And Current_Status = 'Active') OR (P6_Orig_Proj_id is not null AND Data_Period = " + currentMonth + ")) And NTP <> '' And Data_Period >= 201001 Order by P6_Orig_Proj_id, P6_Proj_id, Data_Period";
			
			System.out.println("getFilteredHistoricalTrendData SQL: " + SQL);
			List list = jdbcTemplate.query(SQL, new HistoricalTrendRowMapper());
			System.out.println("list.size(): " + list.size());
			return list;
		}
		
		public List<ProjectBureauEntity> getProjectsByBureau(String bureau, String operatingBureau) {
			//Only check the most current data which is from Latest_Data_Period
			String SQL = "SELECT P6_Historical_Trend.Project_ID, P6_Historical_Trend.Bureau FROM P6_Historical_Trend WHERE P6_Historical_Trend.[P6_Orig_Proj_id] Is Null AND P6_Historical_Trend.[Current_Status]='Active' " +
					"AND P6_Historical_Trend.[Data_Period]= (SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend) " +
					"AND (P6_Historical_Trend.[Operating_Bureau] = '"+ bureau +"' AND ('BEDC' = '"+ operatingBureau +"' OR P6_Historical_Trend.[Bureau]='"+ operatingBureau +"')) " +
					"AND [Current_Stage] != 'PM' " +
					"ORDER BY P6_Historical_Trend.Project_ID";
			System.out.println("getProjectsByBureau SQL: " + SQL);
			List list = jdbcTemplate.query(SQL, new ProjectBureauRowMapper());
			System.out.println("list.size(): " + list.size());
			return list;
		}

		public Long getLatestDataDate() {
			String SQL = "SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend";
			return jdbcTemplate.queryForLong(SQL);
		}

		@SuppressWarnings("unchecked")
		public List<CobraCostDataEntity> getCobraCostData() {
			//Only check the most current data which is from Latest_Data_Period
			String SQL = "SELECT * FROM [ProjectControl].[dbo].[Cobra_CostData_View]";
			System.out.println("getCobraCostData SQL: " + SQL);

			List<CobraCostDataEntity> cobraCostDataList = new ArrayList<CobraCostDataEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			for (Map row : rows) {
				CobraCostDataEntity entity = new CobraCostDataEntity();
				entity.setProject((String)(row.get("PROJECT")));
				entity.setWBS((String)(row.get("WBS")));
				entity.setWP((String)(row.get("WP")));
				entity.setContractRegNumber((String)(row.get("ContractRegnumber")));
				entity.setContractID((String)(row.get("ContractID")));
				entity.setContractType((String)(row.get("ContractType")));
				entity.setCLASS((String)(row.get("CLASS")));
				entity.setVALUE(Long.parseLong(((BigDecimal)(row.get("VALUE"))).toString().split("\\.")[0]));
//				entity.setVALUE(Long.parseLong((String)(row.get("VALUE"))));
				entity.setStatusDate((String)(row.get("STATUSDATE")));
				entity.setMark("");
				entity.setValueInCSV("");
				cobraCostDataList.add(entity);
			}
			
			System.out.println("cobraCostDataList.size(): " + cobraCostDataList.size());
			return cobraCostDataList;
		}

		@SuppressWarnings("unchecked")
		public Map<String, String> getCobraStatusDate() {
			String SQL = "SELECT [PROJECT], max([STATUSDATE]) as STATUSDATE " +
					" FROM [ProjectControl].[dbo].[Cobra_CostData_View] " +
					" group by [PROJECT]";
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			Map<String, String> cobraStatusDateMap = new HashMap<String, String>();
			for (Map row : rows) {
				cobraStatusDateMap.put((String)(row.get("PROJECT")), (String)(row.get("STATUSDATE")));
			}
		
			return cobraStatusDateMap;
		}
		
		public List<SCGHistoricalTrendEntity> getFilteredSCGData(String dataPeriod) {
		 	
		 	List<SCGLogEntity> list;
		 	if(Long.parseLong(dataPeriod) > getLatestSCGLogDataPeriod())
		 		list = getSCGLog(getLatestSCGLogDataPeriod().toString());
		 	else
		 		list = getSCGLog(dataPeriod);
		 	
			String previousProjects = "";
			for(SCGLogEntity entity: list)
				previousProjects += "'" + entity.getProjectId() + "', ";
			previousProjects = previousProjects.substring(0, previousProjects.length()-2);
			System.out.println("previousProjects: " + previousProjects);
			//For baseline information, only use the most current reported baseline from Latest_Data_Period
			String SQL = "select * from P6_Historical_Trend " +
//						"where ((P6_Orig_Proj_id is null And Current_Status = 'Active' And Data_Period = '"+ dataPeriod +"') OR (P6_Orig_Proj_id is not null AND Data_Period = (SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend))) " +
						"where ((P6_Orig_Proj_id is null And Project_ID not in (select Project_ID from SCG_Exception_Project_List) And (Current_Status = 'Active' Or Project_ID in (" + previousProjects + ")) And Data_Period = '"+ dataPeriod +"') OR (P6_Orig_Proj_id is not null AND Data_Period = (SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend))) " +
						"And Data_Period >= 201001 " +
						"And Project_ID not like 'EE-DSGN%' " +
						"And Project_ID not like 'CTYWDCM%' " +
						"Order by P6_Orig_Proj_id, P6_Proj_id, Data_Period";
			
			System.out.println("getFilteredSCGData SQL: " + SQL);
			
			
			List<SCGHistoricalTrendEntity> SCGDataList = new ArrayList<SCGHistoricalTrendEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			System.out.println(rows.size());
			for (Map row : rows) {
				SCGHistoricalTrendEntity entity = new SCGHistoricalTrendEntity();

				entity.setDataPeriod(Long.parseLong(row.get("Data_Period").toString().split("\\.")[0]));
				entity.setP6_Proj_id(Long.parseLong(row.get("P6_Proj_id").toString().split("\\.")[0]));
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setProjectName((String)(row.get("Project_Name")));
				entity.setOperatingBureau((String)(row.get("Bureau")));
				entity.setCurrentStatus((String)(row.get("Current_Status")));
				entity.setCurrentStage((String)(row.get("Current_Stage")));
				entity.setConsent((String)(row.get("Consent")));
				entity.setDFD((String)(row.get("Damage_For_Delay")));
				entity.setMasterProgram((String)(row.get("Master_Program")));
				entity.setMasterProgramOld((String)(row.get("Master_Program_Old")));
				entity.setBODR((String)(row.get("BODR")));
				entity.setDesign30((String)(row.get("30%_Design")));
				entity.setDesign60((String)(row.get("60%_Design")));
				entity.setDesign90((String)(row.get("90%_Design")));
				entity.setFinalDesignCompleted((String)(row.get("Final_Design_Completed")));
				entity.setDataDate((String)(row.get("Data_Date")));
				if(row.get("SC_SlipGain") != null)
					entity.setSlipGain(Long.parseLong(row.get("SC_SlipGain").toString().split("\\.")[0]));
				
//				if(row.get("Current_Construction_Contract_Amount") != null)
//					entity.setCurrentConstructionContractAmount(Long.parseLong(row.get("Current_Construction_Contract_Amount").toString().split("\\.")[0]));
//				else
//					entity.setCurrentConstructionContractAmount(0L);
				entity.setPortfolioManager((String)(row.get("Portfolio_Manager")));
				entity.setDesignAccountableManager((String)(row.get("Accountable_Design_Manager")));
				entity.setConstructionAccountableManager((String)(row.get("Accountable_Construction_Manager")));
				
				
				if(row.get("P6_Orig_Proj_id")==null)//current project
				{
					entity.setCurrentNTP((String)(row.get("NTP")));
					entity.setCurrentSC((String)(row.get("Substantial_Completion")));
					entity.setCurrentStage((String)(row.get("Current_Stage")));
					entity.setCurrentFC((String)(row.get("Final_Completion")));
				}
				else//baseline
				{
					//find the right object
					entity.setOrig_Proj_ID((String)(row.get("Orig_Proj_ID")));
					entity.setP6_Orig_Proj_id(Long.parseLong(row.get("P6_Orig_Proj_id").toString()));
					entity.setBaselineNTP((String)(row.get("NTP")));
					entity.setBaselineSC((String)(row.get("Substantial_Completion")));
				}
				
				
				entity.setId(entity.getDataPeriod().toString()+entity.getProjectId().toString());
				
				

				SCGDataList.add(entity);
			}
			
			System.out.println("SCGDataList.size(): " + SCGDataList.size());
			return SCGDataList;
		}

		public List<SCGLogEntity> getSCGLog(String dataPeriod) {
			//For baseline information, only use the most current reported baseline from Latest_Data_Period
			String SQL = "select * from SCG_Log " +
						"where Data_Period = '"+ dataPeriod +"' " +
						"Order by Data_Period, Project_ID";
			
			System.out.println("getSCGLog SQL: " + SQL);
			
			
			List<SCGLogEntity> SCGLogList = new ArrayList<SCGLogEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			System.out.println(rows.size());
			for (Map row : rows) {
				SCGLogEntity entity = new SCGLogEntity();

				entity.setDataPeriod(Long.parseLong(row.get("Data_Period").toString().split("\\.")[0]));
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setSCGLead((String)(row.get("SCG_Lead")));
				entity.setSCGSupport((String)(row.get("SCG_Support")));
				entity.setComments((String)(row.get("Comments")));
				entity.setClaim((String)(row.get("Claim")));				

				SCGLogList.add(entity);
			}
			
			System.out.println("SCGLogList.size(): " + SCGLogList.size());
			return SCGLogList;
		}
		
		@SuppressWarnings("unchecked")
		public Map<String, Long> getCurrCont(String dataPeriod) {
			//Only check the most current data which is from Latest_Data_Period
			String SQL = "SELECT [Project], SUM(Value) as [Current_Construction_Contract_Amount] " +
						"FROM [ProjectControl].[dbo].[Cobra_Historical_Trend] " +
						"where [ContractType] = 'K' " + 
						"and (Class = 'ORIGCONT' OR Class = 'RCO' OR Class = 'PCO') " +
						"and [Data_Period] = '" + dataPeriod + "' " +
						"group by [Project]";
			System.out.println("getEAC SQL: " + SQL);

			Map<String, Long> map = new HashMap<String, Long>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			for (Map row : rows) {
				map.put((String)(row.get("PROJECT")), Long.parseLong(((BigDecimal)(row.get("Current_Construction_Contract_Amount"))).toString().split("\\.")[0]));
			}
			
			System.out.println("map.size(): " + map.size());
			return map;
		}
		
		@SuppressWarnings("unchecked")
		public Map<String, Long> getOrigCont(String dataPeriod) {
			//Only check the most current data which is from Latest_Data_Period
			String SQL = "SELECT [Project], SUM(Value) as [Orig_Construction_Contract_Amount] " +
						"FROM [ProjectControl].[dbo].[Cobra_Historical_Trend] " +
						"where [ContractType] = 'K' " + 
						"and Class = 'ORIGCONT' " +
						"and [Data_Period] = '" + dataPeriod + "' " +
						"group by [Project]";
			System.out.println("getOrigCont SQL: " + SQL);

			Map<String, Long> map = new HashMap<String, Long>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			for (Map row : rows) {
				map.put((String)(row.get("PROJECT")), Long.parseLong(((BigDecimal)(row.get("Orig_Construction_Contract_Amount"))).toString().split("\\.")[0]));
			}
			
			System.out.println("map.size(): " + map.size());
			return map;
		}
		
		public Long getLatestSCGLogDataPeriod() {
			String SQL = "SELECT max(Data_Period) as Latest_Data_Period FROM SCG_Log";
			return jdbcTemplate.queryForLong(SQL);
		}
		
		public void updateSCGLog(SCGLogEntity project) {
			String SQL = "UPDATE [ProjectControl].[dbo].[SCG_Log] " +
						   "SET [SCG_Lead] = '" + (project.getSCGLead() == null ? "" : project.getSCGLead().replace("'", "''")) + "' " +
						   ", [Comments] = '" + (project.getComments() == null ? "" : project.getComments().replace("'", "''")) + "' " +
						   ", [Claim] = '" + (project.getClaim() == null ? "" : project.getClaim().replace("'", "''")) + "' " +
						 "WHERE [Data_Period] = '" + project.getDataPeriod().toString() + "' " + 
						 "and [Project_ID] = '" + project.getProjectId() + "'";
//			System.out.println("updateSCGLog SQL: " + SQL);
			jdbcTemplate.execute(SQL);
		}
		
		public void insertSCGLog(SCGLogEntity project) {
			String SQL = "INSERT INTO [ProjectControl].[dbo].[SCG_Log] " +
					     "      ([Data_Period] " +
					     "      ,[Project_ID] " +
					     "      ,[SCG_Lead] " +
					     "      ,[Comments] " +
					     "      ,[Claim]) " +
					     "VALUES " +
					     "      ('" + project.getDataPeriod().toString() + "' " + 
					     "      ,'" + project.getProjectId() + "' " + 
					     "      ,'" + (project.getSCGLead() == null ? "" : project.getSCGLead().replace("'", "''")) + "' " +
					     "      ,'" + (project.getComments() == null ? "" : project.getComments().replace("'", "''")) + "' " +
					     "      ,'" + (project.getClaim() == null ? "" : project.getClaim().replace("'", "''")) + "')";
//			System.out.println("insertSCGLog SQL: " + SQL);
			jdbcTemplate.execute(SQL);
		}
		
		public List<SCGLogEntity> getActiveDesignProjects() {
			String SQL = "select a.[Project_ID],[Project_Name],[P6_Status],[Status],[Plan_Year],[Budget],[Design_Stage],[Start_Date],[Achievable],[Approval_Status],[Reviewed_By],[DEP_Estimate],[Estimate],[Date_Received],[Date_Completed] " +
					" from [ProjectControl].[dbo].[Cost_Estimate] as a left join" +
					"  ( " +
					"	  select Data_Period, [Project_ID], [Project_Name]" +
					"	  from P6_Historical_Trend" +
					"	  where P6_Orig_Proj_id is null " +
					"	  And Current_Status = 'Active' " +
					"	  And (Current_Stage = 'P' Or Current_Stage like '%Des%')" +
					"	  AND Data_Period = (SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend)" +
					"  ) as b on a.[Project_ID] = b.[Project_ID]";
			
			System.out.println("getActiveDesignProjects SQL: " + SQL);
			
			
			List<SCGLogEntity> SCGLogList = new ArrayList<SCGLogEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			System.out.println(rows.size());
			for (Map row : rows) {
				SCGLogEntity entity = new SCGLogEntity();

				entity.setDataPeriod(Long.parseLong(row.get("Data_Period").toString().split("\\.")[0]));
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setSCGLead((String)(row.get("SCG_Lead")));
				entity.setSCGSupport((String)(row.get("SCG_Support")));
				entity.setComments((String)(row.get("Comments")));
				entity.setClaim((String)(row.get("Claim")));				

				SCGLogList.add(entity);
			}
			
			System.out.println("SCGLogList.size(): " + SCGLogList.size());
			return SCGLogList;
		}

		public List<HistoricalTrendEntity> getP6HistoricalTrendData() {
			String SQL="";
		
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(
					            "dep/dao/sql/P6MetaData.txt")));

			 
			String line;
			while( (line = br.readLine()) != null)
			{
				System.out.println(line);
				SQL+=line;
//				projectList.add(line);
			}
			
//			System.out.println(projectList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			System.out.println("getP6HistoricalTrendData SQL: " + SQL);
			
			
			List<HistoricalTrendEntity> p6List = new ArrayList<HistoricalTrendEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			System.out.println(rows.size());
			for (Map row : rows) {
				HistoricalTrendEntity entity = new HistoricalTrendEntity();
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setProjectName((String)(row.get("Project_Name")));
				entity.setOperatingBureau((String)(row.get("Operating_Bureau")));
				entity.setBureau((String)(row.get("Bureau")));
				entity.setPortfolioManager((String)(row.get("Portfolio_Manager")));
				entity.setAccountableDesignManager((String)(row.get("Accountable_Design_Manager")));
				entity.setAccountableConstructionManager((String)(row.get("Accountable_Construction_Manager")));
				entity.setMasterProgram((String)(row.get("Master_Program")));
				entity.setDataDate((String)(row.get("Data_Date")));
				entity.setStatus((String)(row.get("Current_Status")));
				entity.setStage((String)(row.get("Current_Stage")));
				p6List.add(entity);
			}
			
			System.out.println("p6List.size(): " + p6List.size());
			return p6List;
		}
		
		

		public List<WorkloadEntity> getWorkload(String stage, String masterProgram, String managedBy, String month) {
			String SQL="";
		
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(
					            "dep/dao/sql/workload" + stage + ".txt")));

			 
			String line;
			while( (line = br.readLine()) != null)
			{
//				System.out.println(line);
				SQL+=line;
			}
			
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
//			System.out.println("pre workload SQL: " + SQL);


			String managedBySQL = " ( SELECT Project " +
									" FROM [ProjectControl].[dbo].[Cobra_Historical_Trend]  " +
									" where [Data_Period] = (SELECT max(Data_Period) as Latest_Data_Period FROM P6_Historical_Trend)  " +
									" and (ContractRegNumber = 'IHD' or ContractRegNumber = 'ICM') " +
									" group by Project )";
			
			if(managedBy.equals("Inhouse"))
				managedBySQL = "  and Project in " + managedBySQL;
			else if(managedBy.equals("Consultant"))
				managedBySQL = "  and Project not in " + managedBySQL;
			else
				managedBySQL = "";
			
//			System.out.println("month: " + month);
//			System.out.println("masterProgram: " + masterProgram);
//			System.out.println("managedBy: " + managedBy);
			SQL = SQL.replaceAll("@month", month);
			SQL = SQL.replaceAll("@masterProgram", masterProgram);
			SQL = SQL.replaceAll("@managedBySQL", managedBySQL);

			
//			System.out.println("post workload SQL: " + SQL);

			
			List<WorkloadEntity> workloadList = new ArrayList<WorkloadEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
//			System.out.println(rows.size());
			for (Map row : rows) {
				WorkloadEntity entity = new WorkloadEntity();
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setStartDate((String)(row.get("start_date")));
				entity.setFinishDate((String)(row.get("finish_date")));
				if(row.get("EAC") != null)
					entity.setEAC(Long.parseLong(row.get("EAC").toString().split("\\.")[0]));
				if(row.get("level") != null)
					entity.setLevel(Long.parseLong(row.get("level").toString().split("\\.")[0]));
				workloadList.add(entity);
			}
			
//			System.out.println("workloadList.size(): " + workloadList.size());
			return workloadList;
		}
		
		public List<EstimateEntity> getCEReport(String month) {
//			String SQL = readFile("C://tomcat//webapps//dep//WEB-INF//resources//CE_Query.sql");
			String SQL="";
		
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(
					            "dep/dao/sql/CE_Query.txt")));

			 
			String line;
			while( (line = br.readLine()) != null)
			{
//				System.out.println(line);
				SQL+=line;
//				projectList.add(line);
			}
			
			SQL = SQL.replaceAll("@month", month);
			
//			System.out.println(projectList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
//			System.out.println("getCEReport SQL: " + SQL);
			
			
			List<EstimateEntity> estimateList = new ArrayList<EstimateEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
//			System.out.println(rows.size());
			for (Map row : rows) {
				EstimateEntity entity = new EstimateEntity();
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setProjectName((String)(row.get("Project_Name")));
				entity.setPlanYear((String)(row.get("Plan_Year")));
				entity.setStage((String)(row.get("Current_Stage")));
				
//				if(row.get("Budget") != null)
//					entity.setBudget(Long.parseLong(row.get("Budget").toString().split("\\.")[0]));
				if(row.get("Estimate") != null)
					entity.setEstimate(Long.parseLong(row.get("Estimate").toString().split("\\.")[0]));
				
				entity.setM_FP((String)(row.get("M_FP")));
				entity.setM_BODR((String)(row.get("M_BODR")));
				entity.setM_30((String)(row.get("M_30")));
				entity.setM_60((String)(row.get("M_60")));
				entity.setM_90((String)(row.get("M_90")));
				entity.setM_100((String)(row.get("M_100")));
				entity.setMilestone((String)(row.get("Milestone")));
				entity.setMilestoneDate((String)(row.get("Milestone_Date")));
				entity.setNextMilestone((String)(row.get("Next_Milestone")));
				entity.setNextMilestoneDate((String)(row.get("Next_Milestone_Date")));
				entity.setAccountableDesignManager((String)(row.get("Accountable_Design_Manager")));

				estimateList.add(entity);
			}
			
//			System.out.println("estimateList.size(): " + estimateList.size());
			return estimateList;
		}
		
		public List<EstimateEntity> getEstimateLog(String dataPeriod) {
			//For baseline information, only use the most current reported baseline from Latest_Data_Period
			String SQL = "select * from Estimate_Log " +
						"where Data_Period = '"+ dataPeriod +"' " +
						"Order by Data_Period, Project_ID";
			
//			System.out.println("getEstimateLog SQL: " + SQL);
			
			
			List<EstimateEntity> EstimateList = new ArrayList<EstimateEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
//			System.out.println(rows.size());
			for (Map row : rows) {
				EstimateEntity entity = new EstimateEntity();

				entity.setDataPeriod(Long.parseLong(row.get("Data_Period").toString().split("\\.")[0]));
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setStatus((String)(row.get("Status")));
//				entity.setBudget((String)row.get("Budget"));
				entity.setBudget((row.get("Budget").toString().equals("")? "" : formatter.format(Double.parseDouble((String)row.get("Budget")))));
				entity.setLatestSubmittedEstimate((row.get("Latest_Submitted_Estimate").toString().equals("")? "" : formatter.format(Double.parseDouble((String)row.get("Latest_Submitted_Estimate")))));
				entity.setDateReceived((String)(row.get("Date_Received")));
				entity.setDateCompleted((String)(row.get("Date_Completed")));
				entity.setReconciled((String)(row.get("Reconciled")));
				entity.setNote((String)(row.get("Note")));				

				EstimateList.add(entity);
			}
			
//			System.out.println("EstimateList.size(): " + EstimateList.size());
			return EstimateList;
		}

		public Long getLatestEstimateDataPeriod() {
			String SQL = "SELECT max(Data_Period) as Latest_Data_Period FROM Estimate_Log";
			return jdbcTemplate.queryForLong(SQL);
		}

		public void updateEstimate(EstimateEntity project) {
			String SQL = "UPDATE [ProjectControl].[dbo].[Estimate_Log] " +
					   "SET [Status] = '" + (project.getStatus() == null ? "" : project.getStatus().replace("'", "''")) + "' " +
					   ", [Budget] = '" + (project.getBudget() == null ? "" : project.getBudget().replace(",", "").replace("'", "''")) + "' " +
					   ", [Latest_Submitted_Estimate] = '" + (project.getLatestSubmittedEstimate() == null ? "" : project.getLatestSubmittedEstimate().replace(",", "").replace("'", "''")) + "' " +
					   ", [Date_Received] = '" + (project.getDateReceived() == null ? "" : project.getDateReceived().replace("'", "''")) + "' " +
					   ", [Date_Completed] = '" + (project.getDateCompleted() == null ? "" : project.getDateCompleted().replace("'", "''")) + "' " +
					   ", [Reconciled] = '" + (project.getReconciled() == null ? "" : project.getReconciled().replace("'", "''")) + "' " +
					   ", [Note] = '" + (project.getNote() == null ? "" : project.getNote().replace("'", "''")) + "' " +
					 "WHERE [Data_Period] = '" + project.getDataPeriod().toString() + "' " + 
					 "and [Project_ID] = '" + project.getProjectId() + "'";
//			System.out.println("updateEstimate SQL: " + SQL);
			jdbcTemplate.execute(SQL);
		}
		
		public void insertEstimate(EstimateEntity project) {
			String SQL = "INSERT INTO [ProjectControl].[dbo].[Estimate_Log] " +
				     "      ([Data_Period] " +
				     "      ,[Project_ID] " +
				     "      ,[Status] " +
				     "      ,[Budget] " +
				     "      ,[Latest_Submitted_Estimate] " +
				     "      ,[Date_Received] " +
				     "      ,[Date_Completed] " +
				     "      ,[Reconciled] " +
				     "      ,[Note]) " +
				     "VALUES " +
				     "      ('" + project.getDataPeriod().toString() + "' " + 
				     "      ,'" + project.getProjectId() + "' " + 
				     "      ,'" + (project.getStatus() == null ? "" : project.getStatus().replace("'", "''")) + "' " +
				     "      ,'" + (project.getBudget() == null ? "" : project.getBudget().replace(",", "").replace("'", "''")) + "' " +
				     "      ,'" + (project.getLatestSubmittedEstimate() == null ? "" : project.getLatestSubmittedEstimate().replace(",", "").replace("'", "''")) + "' " +
				     "      ,'" + (project.getDateReceived() == null ? "" : project.getDateReceived().replace("'", "''")) + "' " +
				     "      ,'" + (project.getDateCompleted() == null ? "" : project.getDateCompleted().replace("'", "''")) + "' " +
				     "      ,'" + (project.getReconciled() == null ? "" : project.getReconciled().replace("'", "''")) + "' " +
				     "      ,'" + (project.getNote() == null ? "" : project.getNote().replace("'", "''")) + "')";
//			System.out.println("insertEstimate SQL: " + SQL);
			jdbcTemplate.execute(SQL);
		}
		
		public String getStaffload(String query, String chartScale, DateTime startDate, Long endFiscalYear, String masterProgram, String projectStatus, String managerType, String managerName) {
			DateTime endDate;
			String quarterString="", union="";
			System.out.println("projectStatus: " + projectStatus);
			System.out.println("managerType: " + managerType);
			System.out.println("managerName: " + managerName);
		 	
		 	for(int i=0;startDate.plusMonths(9).getYear()<=endFiscalYear;i++)// 04/01 + 9 -> 13/01
		 	{
			 	startDate = startDate.plusMonths(3);
		 		endDate = startDate.plusMonths(3).minusMinutes(1);

//		 		System.out.println("startDate: " + startDate.toString(dateHourMinuteFormatter));
//			 	System.out.println("endDate: " + endDate.toString(dateHourMinuteFormatter));
//			 	System.out.println("FY: " + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3);
			 	
			 	quarterString+="      ,	case " +
				"when convert(datetime, [Final_Completion], 101) < '" + endDate.toString(dateHourMinuteFormatter) + "' then 'SC'  " +
				"when convert(datetime, [Substantial_Completion], 101) < '" + endDate.toString(dateHourMinuteFormatter) + "' AND convert(datetime, [Final_Completion], 101) > '" + startDate.toString(dateHourMinuteFormatter) + "' then 'SC'  " +
				"when convert(datetime, [NTP], 101) < '" + endDate.toString(dateHourMinuteFormatter) + "' AND convert(datetime, [Substantial_Completion], 101) > '" + startDate.toString(dateHourMinuteFormatter) + "' then 'CON'  " +
				"when convert(datetime, [Final_Design_Completed], 101) < '" + endDate.toString(dateHourMinuteFormatter) + "' AND convert(datetime, [NTP], 101) > '" + startDate.toString(dateHourMinuteFormatter) + "' then 'ConPro'  " +
				"when convert(datetime, [Design_Start], 101) < '" + endDate.toString(dateHourMinuteFormatter) + "' AND convert(datetime, [Final_Design_Completed], 101) > '" + startDate.toString(dateHourMinuteFormatter) + "' then 'DES'  " +
				"when convert(datetime, [FP_Start], 101) < '" + endDate.toString(dateHourMinuteFormatter) + "' AND convert(datetime, [Design_Start], 101) > '" + startDate.toString(dateHourMinuteFormatter) + "' then 'P'  " +
				"end as '" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "' "  +  System.getProperty("line.separator");
			 	
			 	if(i==0)
				 	union = " select [Project_ID], '" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + " - ' + P6_info.[" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "] as Fiscal_Year_Status, '" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "' as Fiscal_Year, P6_info.[" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "] as Quarterly_Status from P6_info "+  System.getProperty("line.separator");
			 	else
				 	union += "union " +  System.getProperty("line.separator") +
				 			" select [Project_ID], '" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + " - ' + P6_info.[" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "], '" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "' as Fiscal_Year, P6_info.[" + endDate.plusMonths(6).getYear() + "_Q" + endDate.plusMonths(6).getMonthOfYear()/3 + "] as Quarterly_Status from P6_info "+  System.getProperty("line.separator");

		 	}
		 	

		 	
//		 	System.out.println("quarterString: " + quarterString);
//		 	System.out.println("union: " + union);
		 	String filename = "";
		 	if(managerName.equals("All"))
		 		filename = "dep/dao/sql/staffing/staffchart_All_PMs_in_one_chart_" + chartScale + ".txt";
		 	else 
		 		filename = "dep/dao/sql/staffing/staffchart_" + chartScale + ".txt";
		 	
		 	String SQL="";
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(filename)));
			 
			String line;
			while( (line = br.readLine()) != null)
				SQL+=line + System.getProperty("line.separator");
			
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			SQL = SQL.replaceAll("@quarterString", quarterString);
			SQL = SQL.replaceAll("@union", union);
			SQL = SQL.replaceAll("@masterProgram", "WWC");//TODO hardcoded WWC for now
			SQL = SQL.replaceAll("@currentStatus", "Current_Status = '" + projectStatus + "'");
			if(projectStatus.equals("Future"))
				SQL = SQL.replaceAll("@managerType", " ");
			else if(managerType.equals("PM"))
				SQL = SQL.replaceAll("@managerType", "and [Portfolio_Manager] = '" + managerName + "'");
			else if(managerType.equals("AM"))
				SQL = SQL.replaceAll("@managerType", "and [Active_Accountable_Manager] = '" + managerName + "'");

			
			System.out.println("Staff SQL: " + SQL);

			
			List<StaffBarChartEntity> staffBarChartList = new ArrayList<StaffBarChartEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);		
			
			
			for (Map row : rows) {
				StaffBarChartEntity entity = new StaffBarChartEntity();
				entity.setFiscalYear((String)(row.get("Fiscal_Year")));
				entity.setStaff((String)(row.get("Staff")));
				entity.setTotal(Double.parseDouble(formatter.format(row.get("Total"))));
				staffBarChartList.add(entity);
			}
			
			String arrayContent = "";
			int numOfBar=8;
			
			//Array Header
			if(managerName.equals("All"))
			{
				arrayContent = "['Month', ";

				List<String> PMList = getActiveManagerList("PM", "WWC");
				numOfBar = PMList.size();
				for(String PM : PMList)
				{
					arrayContent += "'" + PM + "', ";
				}
				
                StringBuilder bc = new StringBuilder(arrayContent);
                bc.replace(arrayContent.lastIndexOf(","), arrayContent.lastIndexOf(",") + 1, "], " + System.getProperty("line.separator") );
                arrayContent = bc.toString();
			
			}
			else
			{
				arrayContent = "['Month', 'Administative Tech', 'Project Manager for Design', 'Project Manager for Construction', 'Associate Project Manager Level 1', 'Associate Project Manager Level 2', 'Associate Project Manager Level 3', 'Manager M Title', 'Portfolio Manager'], " + System.getProperty("line.separator");
			}
			
			//Array Content
			for(int i=0; i+numOfBar<=staffBarChartList.size();i=i+numOfBar)
			{
				if(!(chartScale.equals("year")&&staffBarChartList.get(i).getFiscalYear().equals("2013")))
				{
					arrayContent += "['FY " + staffBarChartList.get(i).getFiscalYear() + "'";
					for(int j=i;j<=i+numOfBar-1;j++)
						arrayContent += ", " + staffBarChartList.get(j).getTotal();
					arrayContent += "], " + System.getProperty("line.separator");
				}
			}
			
            if(arrayContent.length()>0)
            {
                 StringBuilder b = new StringBuilder(arrayContent);
                 b.replace(arrayContent.lastIndexOf(","), arrayContent.lastIndexOf(",") + 1, "" );
                 arrayContent = b.toString();
            }
            else arrayContent = "['', 0, 0, 0, 0, 0, 0, 0, 0]";

            System.out.println("arrayContent: " + arrayContent);
           
            return arrayContent;
		}
		

		@SuppressWarnings("unchecked")
		public List<String> getActiveManagerList(String managerType, String masterProgram) {
			String SQL="";
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(
					            "dep/dao/sql/" + managerType + "List.txt")));
			 
			String line;
			while( (line = br.readLine()) != null)
				SQL+=line + System.getProperty("line.separator");
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			System.out.println("getActiveManagerList SQL: " + SQL);

			List<String> list = new ArrayList<String>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			for (Map row : rows) {
				if(managerType.equals("AM"))
					list.add((String)(row.get("Active_Accountable_Manager")));
				else if(managerType.equals("PM"))
					list.add((String)(row.get("Portfolio_Manager")));
			}
			
//			System.out.println("AMList.size(): " + AMList.size());
			return list;
		}
		
		

		public List<CostTrendEntity> getBudgetContractEAC(String projectId, String contractType) {
			String SQL="";
		
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(
					            "dep/dao/sql/BudgetContractEAC.txt")));

			 
			String line;
			while( (line = br.readLine()) != null)
			{
				SQL+=line;
//				projectList.add(line);
			}
			
//			System.out.println(projectList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			SQL = SQL.replaceAll("@projectId", projectId);
			SQL = SQL.replaceAll("@contractType", contractType);
			
//			System.out.println("getBudgetContractEAC SQL: " + SQL);
			
			
			List<CostTrendEntity> list = new ArrayList<CostTrendEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			for (Map row : rows) {
				CostTrendEntity entity = new CostTrendEntity();
				
				entity.setDataPeriod(Long.parseLong(row.get("Data_Period").toString().split("\\.")[0]));
				if(row.get("EAC") != null)
					entity.setEAC(Long.parseLong(row.get("EAC").toString().split("\\.")[0]));
				if(row.get("Budget") != null)
					entity.setBudget(Long.parseLong(row.get("Budget").toString().split("\\.")[0]));
				if(row.get("Current_Contract_Forecast") != null)
					entity.setCurrentContractForecast(Long.parseLong(row.get("Current_Contract_Forecast").toString().split("\\.")[0]));
				if(row.get("Adjusted_Contract_Price") != null)
					entity.setAdjustedContractPrice(Long.parseLong(row.get("Adjusted_Contract_Price").toString().split("\\.")[0]));

				list.add(entity);
			}
			
			return list;
		}
		

		public List<HistoricalTrendEntity> getAllActiveProjects() {
			String SQL="";
		
			
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
					    getClass().getClassLoader().getResourceAsStream(
					            "dep/dao/sql/ActiveProjects.txt")));

			 
			String line;
			while( (line = br.readLine()) != null)
			{
				SQL+=line;
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			List<HistoricalTrendEntity> list = new ArrayList<HistoricalTrendEntity>();
			
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
			
			for (Map row : rows) {
				HistoricalTrendEntity entity = new HistoricalTrendEntity();
				
				entity.setProjectId((String)(row.get("Project_ID")));
				entity.setProjectName((String)(row.get("Project_Name")));
				entity.setPortfolioManager((String)(row.get("Portfolio_Manager")));
				
				list.add(entity);
			}
			
			return list;
		}
		
		
}
