package dep.model;



public class SCGLogEntity
{
	private Long dataPeriod;
	private String projectId;
	private String projectName;
	private String portfolioManager;
	private String stage;
	private String SCGLead;
	private String SCGSupport;
	private String comments;
	private String claim;
	
	public Long getDataPeriod() {
		return dataPeriod;
	}
	public void setDataPeriod(Long dataPeriod) {
		this.dataPeriod = dataPeriod;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
}
