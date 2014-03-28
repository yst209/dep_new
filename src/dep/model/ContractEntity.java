package dep.model;

import java.util.Date;

public class ContractEntity {
	private Long id;
	private Long month;
	private Long year;
	private String projectId;
	private Date noticeToProceed; //For construction only, Current forecast NTP = Baseline NTP, so there is only one NTP.
	private Date substantialCompletionOfBaseline;//Baseline Substantial Completion
	private Date substantialCompletion;
	private Long baselineDuration;
	private Long currDuration;
	
	
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Date getNoticeToProceed() {
		return noticeToProceed;
	}
	public void setNoticeToProceed(Date noticeToProceed) {
		this.noticeToProceed = noticeToProceed;
	}
	public Date getSubstantialCompletionOfBaseline() {
		return substantialCompletionOfBaseline;
	}
	public void setSubstantialCompletionOfBaseline(Date substantialCompletionOfBaseline) {
		this.substantialCompletionOfBaseline = substantialCompletionOfBaseline;
	}
	public Date getSubstantialCompletion() {
		return substantialCompletion;
	}
	public void setSubstantialCompletion(Date substantialCompletion) {
		this.substantialCompletion = substantialCompletion;
	}
	public Long getBaselineDuration() {
		return baselineDuration;
	}
	public void setBaselineDuration(Long baselineDuration) {
		this.baselineDuration = baselineDuration;
	}
	public Long getCurrDuration() {
		return currDuration;
	}
	public void setCurrDuration(Long currDuration) {
		this.currDuration = currDuration;
	}


}