package dep.model;


public class HistoricalTrendEntity {
	private String id;
	private Long month;
	private Long year;
	private Long dataPeriod;
	private Long P6_Proj_id;
	private String projectId;
	private Long P6_Orig_Proj_id;//Baseline should point to the original project
	private String baselineNTP; //For construction only, Current forecast NTP = Baseline NTP, so there is only one NTP.
	private String currentNTP;	//baselineNTP and currentNTP should match
	
	private String baselineSC;//Baseline Substantial Completion
	private String currentSC;
	private String dataDate;
	private Long baselineDuration;
	private Long currentDuration;
	private String currentStage;
	
	private String operatingBureau;
	private String bureau;
	private String projectName;
	private String portfolioManager;
	private String accountableDesignManager;
	private String accountableConstructionManager;
	private String masterProgram;
	private String status;
	private String stage;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getMonth() {
		return month;
	}
	public void setMonth(Long month) {
		this.month = month;
	}
	public Long getYear() {
		return year;
	}
	public void setYear(Long year) {
		this.year = year;
	}
	public Long getDataPeriod() {
		return dataPeriod;
	}
	public void setDataPeriod(Long dataPeriod) {
		this.dataPeriod = dataPeriod;
	}
	public Long getP6_Proj_id() {
		return P6_Proj_id;
	}
	public void setP6_Proj_id(Long p6_Proj_id) {
		P6_Proj_id = p6_Proj_id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Long getP6_Orig_Proj_id() {
		return P6_Orig_Proj_id;
	}
	public void setP6_Orig_Proj_id(Long p6_Orig_Proj_id) {
		P6_Orig_Proj_id = p6_Orig_Proj_id;
	}
	public String getBaselineNTP() {
		return baselineNTP;
	}
	public void setBaselineNTP(String baselineNTP) {
		this.baselineNTP = baselineNTP;
	}
	public String getCurrentNTP() {
		return currentNTP;
	}
	public void setCurrentNTP(String currentNTP) {
		this.currentNTP = currentNTP;
	}
	public String getBaselineSC() {
		return baselineSC;
	}
	public void setBaselineSC(String baselineSC) {
		this.baselineSC = baselineSC;
	}
	public String getCurrentSC() {
		return currentSC;
	}
	public void setCurrentSC(String currentSC) {
		this.currentSC = currentSC;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public Long getBaselineDuration() {
		return baselineDuration;
	}
	public void setBaselineDuration(Long baselineDuration) {
		this.baselineDuration = baselineDuration;
	}
	public Long getCurrentDuration() {
		return currentDuration;
	}
	public void setCurrentDuration(Long currentDuration) {
		this.currentDuration = currentDuration;
	}
	public String getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	public String getOperatingBureau() {
		return operatingBureau;
	}
	public void setOperatingBureau(String operatingBureau) {
		this.operatingBureau = operatingBureau;
	}
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getPortfolioManager() {
		return portfolioManager;
	}
	public void setPortfolioManager(String portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	public String getAccountableDesignManager() {
		return accountableDesignManager;
	}
	public void setAccountableDesignManager(String accountableDesignManager) {
		this.accountableDesignManager = accountableDesignManager;
	}
	public String getAccountableConstructionManager() {
		return accountableConstructionManager;
	}
	public void setAccountableConstructionManager(
			String accountableConstructionManager) {
		this.accountableConstructionManager = accountableConstructionManager;
	}
	public String getMasterProgram() {
		return masterProgram;
	}
	public void setMasterProgram(String masterProgram) {
		this.masterProgram = masterProgram;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	
}