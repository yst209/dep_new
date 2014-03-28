package dep.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MonthlyEntity {
	private Long id;
	private Long month;
	private Long year;
	private Long baselineDuration;
	private Long currentDuration;
	private Integer totalProjectCount;
	
	private List<HistoricalTrendEntity> projectList = new ArrayList<HistoricalTrendEntity>();
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Integer getTotalProjectCount() {
		return totalProjectCount;
	}
	public void setTotalProjectCount(Integer totalProjectCount) {
		this.totalProjectCount = totalProjectCount;
	}
	public List<HistoricalTrendEntity> getProjectList() {
		return projectList;
	}
	public void setProjectList(List<HistoricalTrendEntity> projectList) {
		this.projectList = projectList;
	}

	public void sortProjectList() {
		 Collections.sort(projectList, new Comparator<HistoricalTrendEntity>(){
									 		public int compare(HistoricalTrendEntity o1, HistoricalTrendEntity o2) {
									 			HistoricalTrendEntity ht1 = o1;
									 			HistoricalTrendEntity ht2 = o2;
									 			return ht1.getProjectId().compareTo(ht2.getProjectId());
									 		}//End of compare
		 								}
		 );//End of Collections.sort();
	}
}