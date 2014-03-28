package dep.model;

import java.math.BigDecimal;


public class CobraCostDataEntity {
	private String project;
	private String WBS;
	private String WP;
	private String contractRegNumber;
	private String contractID;
	private String contractType;
	private String CLASS;
	private Long VALUE;
	private String statusDate;
	private String mark;
	private String valueInCSV;
	

	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getWBS() {
		return WBS;
	}
	public void setWBS(String wBS) {
		WBS = wBS;
	}
	public String getWP() {
		return WP;
	}
	public void setWP(String wP) {
		WP = wP;
	}
	public String getContractRegNumber() {
		return contractRegNumber;
	}
	public void setContractRegNumber(String contractRegNumber) {
		this.contractRegNumber = contractRegNumber;
	}
	public String getContractID() {
		return contractID;
	}
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getCLASS() {
		return CLASS;
	}
	public void setCLASS(String cLASS) {
		CLASS = cLASS;
	}
	public Long getVALUE() {
		return VALUE;
	}
	public void setVALUE(Long vALUE) {
		VALUE = vALUE;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getValueInCSV() {
		return valueInCSV;
	}
	public void setValueInCSV(String valueInCSV) {
		this.valueInCSV = valueInCSV;
	}
}