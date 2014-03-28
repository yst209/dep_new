package dep.model;

import java.util.Map;


public class TrendInfo
{
	
	private String bureau;
	private String operatingBureau;
	private Long comparedMonth;
	private Long currentMonth;
	private Long endPeriod;
	
	private String chart1URL;
	private String imageMap;
	private Map<String, MonthlyEntity> monthlyMap;
	private Map<String, MonthlyEntity> comparedMonthlyMap;
	
	
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getOperatingBureau() {
		return operatingBureau;
	}
	public void setOperatingBureau(String operatingBureau) {
		this.operatingBureau = operatingBureau;
	}
	public Long getComparedMonth() {
		return comparedMonth;
	}
	public void setComparedMonth(Long comparedMonth) {
		this.comparedMonth = comparedMonth;
	}
	public Long getCurrentMonth() {
		return currentMonth;
	}
	public void setCurrentMonth(Long currentMonth) {
		this.currentMonth = currentMonth;
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
	public Map<String, MonthlyEntity> getMonthlyMap() {
		return monthlyMap;
	}
	public void setMonthlyMap(Map<String, MonthlyEntity> monthlyMap) {
		this.monthlyMap = monthlyMap;
	}
	public Map<String, MonthlyEntity> getComparedMonthlyMap() {
		return comparedMonthlyMap;
	}
	public void setComparedMonthlyMap(Map<String, MonthlyEntity> comparedMonthlyMap) {
		this.comparedMonthlyMap = comparedMonthlyMap;
	}

}
