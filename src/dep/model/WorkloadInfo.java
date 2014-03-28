package dep.model;

import java.util.List;
import java.util.Map;


public class WorkloadInfo
{
	private String projectId;

	private String stage;
	private String masterProgram;
	private String managedBy;
	private Long endPeriod;
	
	private String chart1URL;
	private String imageMap;
	
	private Map<String, List<WorkloadEntity>> map1;
	private Map<String, List<WorkloadEntity>> map2;
	private Map<String, List<WorkloadEntity>> map3;
	private Map<String, List<WorkloadEntity>> mapAll;
	
	
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
	public String getManagedBy() {
		return managedBy;
	}
	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}
	public Long getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(Long endPeriod) {
		this.endPeriod = endPeriod;
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
	public Map<String, List<WorkloadEntity>> getMap1() {
		return map1;
	}
	public void setMap1(Map<String, List<WorkloadEntity>> map1) {
		this.map1 = map1;
	}
	public Map<String, List<WorkloadEntity>> getMap2() {
		return map2;
	}
	public void setMap2(Map<String, List<WorkloadEntity>> map2) {
		this.map2 = map2;
	}
	public Map<String, List<WorkloadEntity>> getMap3() {
		return map3;
	}
	public void setMap3(Map<String, List<WorkloadEntity>> map3) {
		this.map3 = map3;
	}
	public Map<String, List<WorkloadEntity>> getMapAll() {
		return mapAll;
	}
	public void setMapAll(Map<String, List<WorkloadEntity>> mapAll) {
		this.mapAll = mapAll;
	}

}
