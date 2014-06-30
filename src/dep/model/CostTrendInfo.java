package dep.model;



public class CostTrendInfo
{	
	private String projectId;
	private String portfolioManager;
	private String contractType;
	
	private String chart1URL;
	private String imageMap;

	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getPortfolioManager() {
		return portfolioManager;
	}
	public void setPortfolioManager(String portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	
	public String getChart1URL() {
		return chart1URL;
	}
	public void setChart1URL(String chart1url) {
		chart1URL = chart1url;
	}
	public String getImageMap() {
		return imageMap;
	}
	public void setImageMap(String imageMap) {
		this.imageMap = imageMap;
	}

}
