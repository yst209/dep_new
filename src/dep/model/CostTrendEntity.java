package dep.model;



public class CostTrendEntity {
	private Long dataPeriod;
	private Long budget;
	private Long EAC;
	private Long adjustedContractPrice;
	private Long currentContractForecast;
	
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
	

}