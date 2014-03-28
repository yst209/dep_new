package dep.model;

import java.util.List;
import java.util.Map;


public class StaffloadInfo
{
	private String projectId;

	private String stage;
	private String masterProgram;
	private String chartScale;
	private Long endFiscalYear;
	private String portfolioManager;
	private String accountableManager;
	private String managerType;
	private String managerName;
	private String projectStatus;
	private String queryString;
	private String arrayContent;
	
	private String chart1URL;
	private String imageMap;
	
	private Map<String, List<StaffloadEntity>> map1;
	private Map<String, List<StaffloadEntity>> map2;
	private Map<String, List<StaffloadEntity>> map3;
	private Map<String, List<StaffloadEntity>> mapAll;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getMasterProgram() {
		return masterProgram;
	}
	public void setMasterProgram(String masterProgram) {
		this.masterProgram = masterProgram;
	}
	public String getChartScale() {
		return chartScale;
	}
	public void setChartScale(String chartScale) {
		this.chartScale = chartScale;
	}
	public Long getEndFiscalYear() {
		return endFiscalYear;
	}
	public void setEndFiscalYear(Long endFiscalYear) {
		this.endFiscalYear = endFiscalYear;
	}
	public String getPortfolioManager() {
		return portfolioManager;
	}
	public void setPortfolioManager(String portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	public String getAccountableManager() {
		return accountableManager;
	}
	public void setAccountableManager(String accountableManager) {
		this.accountableManager = accountableManager;
	}
	public String getManagerType() {
		return managerType;
	}
	public void setManagerType(String managerType) {
		this.managerType = managerType;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String getArrayContent() {
		return arrayContent;
	}
	public void setArrayContent(String arrayContent) {
		this.arrayContent = arrayContent;
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
	public Map<String, List<StaffloadEntity>> getMap1() {
		return map1;
	}
	public void setMap1(Map<String, List<StaffloadEntity>> map1) {
		this.map1 = map1;
	}
	public Map<String, List<StaffloadEntity>> getMap2() {
		return map2;
	}
	public void setMap2(Map<String, List<StaffloadEntity>> map2) {
		this.map2 = map2;
	}
	public Map<String, List<StaffloadEntity>> getMap3() {
		return map3;
	}
	public void setMap3(Map<String, List<StaffloadEntity>> map3) {
		this.map3 = map3;
	}
	public Map<String, List<StaffloadEntity>> getMapAll() {
		return mapAll;
	}
	public void setMapAll(Map<String, List<StaffloadEntity>> mapAll) {
		this.mapAll = mapAll;
	}
	
}
