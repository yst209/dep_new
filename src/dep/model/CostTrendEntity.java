package dep.model;



public class CostTrendEntity {
	private Long dataPeriod;
	private Long budget;
	private Long amountSpent;
	private Long EAC;
	private Long adjustedContractPrice;
	private Long currentContractForecast;
	private Double spentPercentage;
	private Double physicalPercentage;
	
	public Long getDataPeriod() {
		return dataPeriod;
	}
	public void setDataPeriod(Long dataPeriod) {
		this.dataPeriod = dataPeriod;
	}
	public Long getBudget() {
		return budget;
	}
	public void setBudget(Long budget) {
		this.budget = budget;
	}
	public Long getAmountSpent() {
		return amountSpent;
	}
	public void setAmountSpent(Long amountSpent) {
		this.amountSpent = amountSpent;
	}
	public Long getEAC() {
		return EAC;
	}
	public void setEAC(Long eAC) {
		EAC = eAC;
	}
	public Long getAdjustedContractPrice() {
		return adjustedContractPrice;
	}
	public void setAdjustedContractPrice(Long adjustedContractPrice) {
		this.adjustedContractPrice = adjustedContractPrice;
	}
	public Long getCurrentContractForecast() {
		return currentContractForecast;
	}
	public void setCurrentContractForecast(Long currentContractForecast) {
		this.currentContractForecast = currentContractForecast;
	}
	public Double getSpentPercentage() {
		return spentPercentage;
	}
	public void setSpentPercentage(Double spentPercentage) {
		this.spentPercentage = spentPercentage;
	}
	public Double getPhysicalPercentage() {
		return physicalPercentage;
	}
	public void setPhysicalPercentage(Double physicalPercentage) {
		this.physicalPercentage = physicalPercentage;
	}
}