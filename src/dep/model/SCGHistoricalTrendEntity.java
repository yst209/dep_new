package dep.model;

import java.util.Collections;
import java.util.Comparator;



public class SCGHistoricalTrendEntity
{
	private String id;
	private Long dataPeriod;
	private Long P6_Proj_id;
	private Long P6_Orig_Proj_id;//Baseline should point to the original project
	private String orig_Proj_ID;
	private String projectId;
	
	private String projectName;
	private String operatingBureau;
	private String currentStatus;
	private String currentStage;
	private String consent;
	private String DFD;//Delay for damage
	private String masterProgram;
	private String masterProgramOld;	

	private String BODR;	
	private String design30;	
	private String design60;	
	private String design90;	
	private String finalDesignCompleted;	

	private String baselineNTP; //For construction only, Current forecast NTP = Baseline NTP, so there is only one NTP.
	private String currentNTP;	//baselineNTP and currentNTP should match	
	private String baselineSC;//Baseline Substantial Completion
	private String currentSC;
	private String currentFC;

	private String dataDate;
	private Long slipGain;
	private Long SCCurrentVariance;

	private Long origConstructionContractAmount;
	private Long currentConstructionContractAmount;
	private String portfolioManager;
	private String designAccountableManager;
	private String constructionAccountableManager;
	
	//From SCGLogEntity
	private String SCGLead;
	private String SCGSupport;
	private String comments;
	private String claim;
	
	//From Project Supporting Roles
	private String projectControlsLead;
	private String permitsLead;
	private String sustainabilityManager;
	private String costEstimatingManager;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Long getP6_Orig_Proj_id() {
		return P6_Orig_Proj_id;
	}
	public void setP6_Orig_Proj_id(Long p6_Orig_Proj_id) {
		P6_Orig_Proj_id = p6_Orig_Proj_id;
	}
	public String getOrig_Proj_ID() {
		return orig_Proj_ID;
	}
	public void setOrig_Proj_ID(String orig_Proj_ID) {
		this.orig_Proj_ID = orig_Proj_ID;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getOperatingBureau() {
		return operatingBureau;
	}
	public void setOperatingBureau(String operatingBureau) {
		this.operatingBureau = operatingBureau;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}
	public String getConsent() {
		return consent;
	}
	public void setConsent(String consent) {
		this.consent = consent;
	}
	public String getDFD() {
		return DFD;
	}
	public void setDFD(String dFD) {
		DFD = dFD;
	}
	public String getMasterProgram() {
		return masterProgram;
	}
	public void setMasterProgram(String masterProgram) {
		this.masterProgram = masterProgram;
	}
	public String getMasterProgramOld() {
		return masterProgramOld;
	}
	public void setMasterProgramOld(String masterProgramOld) {
		this.masterProgramOld = masterProgramOld;
	}
	public String getBODR() {
		return BODR;
	}
	public void setBODR(String bODR) {
		BODR = bODR;
	}
	public String getDesign30() {
		return design30;
	}
	public void setDesign30(String design30) {
		this.design30 = design30;
	}
	public String getDesign60() {
		return design60;
	}
	public void setDesign60(String design60) {
		this.design60 = design60;
	}
	public String getDesign90() {
		return design90;
	}
	public void setDesign90(String design90) {
		this.design90 = design90;
	}
	public String getFinalDesignCompleted() {
		return finalDesignCompleted;
	}
	public void setFinalDesignCompleted(String finalDesignCompleted) {
		this.finalDesignCompleted = finalDesignCompleted;
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
	public Long getSlipGain() {
		return slipGain;
	}
	public void setSlipGain(Long slipGain) {
		this.slipGain = slipGain;
	}
	public Long getSCCurrentVariance() {
		return SCCurrentVariance;
	}
	public void setSCCurrentVariance(Long sCCurrentVariance) {
		SCCurrentVariance = sCCurrentVariance;
	}
	public Long getCurrentConstructionContractAmount() {
		return currentConstructionContractAmount;
	}
	public void setCurrentConstructionContractAmount(
			Long currentConstructionContractAmount) {
		this.currentConstructionContractAmount = currentConstructionContractAmount;
	}
	public String getPortfolioManager() {
		return portfolioManager;
	}
	public void setPortfolioManager(String portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	public String getDesignAccountableManager() {
		return designAccountableManager;
	}
	public void setDesignAccountableManager(String designAccountableManager) {
		this.designAccountableManager = designAccountableManager;
	}
	public Long getOrigConstructionContractAmount() {
		return origConstructionContractAmount;
	}
	public void setOrigConstructionContractAmount(
			Long origConstructionContractAmount) {
		this.origConstructionContractAmount = origConstructionContractAmount;
	}
	public String getConstructionAccountableManager() {
		return constructionAccountableManager;
	}
	public void setConstructionAccountableManager(
			String constructionAccountableManager) {
		this.constructionAccountableManager = constructionAccountableManager;
	}
	public String getSCGLead() {
		return SCGLead;
	}
	public void setSCGLead(String sCGLead) {
		SCGLead = sCGLead;
	}
	public String getSCGSupport() {
		return SCGSupport;
	}
	public void setSCGSupport(String sCGSupport) {
		SCGSupport = sCGSupport;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getClaim() {
		return claim;
	}
	public void setClaim(String claim) {
		this.claim = claim;
	}
	public String getCurrentFC() {
		return currentFC;
	}
	public void setCurrentFC(String currentFC) {
		this.currentFC = currentFC;
	}
	public String getProjectControlsLead() {
		return projectControlsLead;
	}
	public void setProjectControlsLead(String projectControlsLead) {
		this.projectControlsLead = projectControlsLead;
	}
	public String getPermitsLead() {
		return permitsLead;
	}
	public void setPermitsLead(String permitsLead) {
		this.permitsLead = permitsLead;
	}
	public String getSustainabilityManager() {
		return sustainabilityManager;
	}
	public void setSustainabilityManager(String sustainabilityManager) {
		this.sustainabilityManager = sustainabilityManager;
	}
	public String getCostEstimatingManager() {
		return costEstimatingManager;
	}
	public void setCostEstimatingManager(String costEstimatingManager) {
		this.costEstimatingManager = costEstimatingManager;
	}
	
}
