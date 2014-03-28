package dep.model;

import java.util.List;


public class BatchInfo
{
	
	private Long datePeriod;
//	private String sourcePath;
//	private String targetPath;
	private String coordinator;
	
	private List<String> validatedProjectList;
	private List<String> errorProjectList;	
	private Boolean updateTotalsOnly;
	
	public Long getDatePeriod() {
		return datePeriod;
	}
	public void setDatePeriod(Long datePeriod) {
		this.datePeriod = datePeriod;
	}
//	public String getSourcePath() {
//		return sourcePath;
//	}
//	public void setSourcePath(String sourcePath) {
//		this.sourcePath = sourcePath;
//	}
//	public String getTargetPath() {
//		return targetPath;
//	}
//	public void setTargetPath(String targetPath) {
//		this.targetPath = targetPath;
//	}
	public List<String> getValidatedProjectList() {
		return validatedProjectList;
	}
	public void setValidatedProjectList(List<String> validatedProjectList) {
		this.validatedProjectList = validatedProjectList;
	}
	public List<String> getErrorProjectList() {
		return errorProjectList;
	}
	public void setErrorProjectList(List<String> errorProjectList) {
		this.errorProjectList = errorProjectList;
	}
	public Boolean getUpdateTotalsOnly() {
		return updateTotalsOnly;
	}
	public void setUpdateTotalsOnly(Boolean updateTotalsOnly) {
		this.updateTotalsOnly = updateTotalsOnly;
	}
	public String getCoordinator() {
		return coordinator;
	}
	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}

	
}
