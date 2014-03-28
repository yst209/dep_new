package dep.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class EstimateForm
{
	private List<EstimateEntity> projects;
	private String[] dateReceivedArray = new String[200];
	private String[] dateCompletedArray = new String[200];
	private String[] budgetArray = new String[200];
	private String[] latestSubmittedEstimateArray = new String[200];


	public List<EstimateEntity> getProjects() {
		return projects;
	}

	public void setProjects(List<EstimateEntity> projects) {
		this.projects = projects;
	}

	public void sortProjectList() {
		 Collections.sort(projects, new Comparator<EstimateEntity>(){
									 		public int compare(EstimateEntity o1, EstimateEntity o2) {
									 			EstimateEntity e1 = o1;
									 			EstimateEntity e2 = o2;
									 			return e1.getProjectId().compareTo(e2.getProjectId());
									 		}//End of compare
		 								}
		 );//End of Collections.sort();
	}

	public String[] getDateReceivedArray() {
		return dateReceivedArray;
	}

	public void setDateReceivedArray(String[] dateReceivedArray) {
		this.dateReceivedArray = dateReceivedArray;
	}

	public String[] getDateCompletedArray() {
		return dateCompletedArray;
	}

	public void setDateCompletedArray(String[] dateCompletedArray) {
		this.dateCompletedArray = dateCompletedArray;
	}

	public String[] getBudgetArray() {
		return budgetArray;
	}

	public void setBudgetArray(String[] budgetArray) {
		this.budgetArray = budgetArray;
	}

	public String[] getLatestSubmittedEstimateArray() {
		return latestSubmittedEstimateArray;
	}

	public void setLatestSubmittedEstimateArray(
			String[] latestSubmittedEstimateArray) {
		this.latestSubmittedEstimateArray = latestSubmittedEstimateArray;
	}
	
}
