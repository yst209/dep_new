package dep.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class StaffloadEntity
{
	private String projectId;
	private String startDate;
	private String finishDate;
	private Long EAC;
	private Long level;
	
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public Long getEAC() {
		return EAC;
	}
	public void setEAC(Long eAC) {
		EAC = eAC;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	
	
}
