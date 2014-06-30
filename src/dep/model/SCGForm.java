package dep.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SCGForm
{
	public enum Stage {
		DesPro, P, Des, Des30, Des60, Des90, Des100, Con, ConPro, SC, FC, PM
	}
	
	private List<SCGLogEntity> projects;

	public List<SCGLogEntity> getProjects() {
		return projects;
	}

	public void setProjects(List<SCGLogEntity> projects) {
		this.projects = projects;
	}

	public void sortProjectList() {
		 Collections.sort(projects, new Comparator<SCGLogEntity>(){
									 		public int compare(SCGLogEntity a, SCGLogEntity b) {
									 			
									 			int result = 0;
									 			
									 			if((a.getStage()==null || a.getStage().equals("")) && (b.getStage()==null || b.getStage().equals("")))
									 				result = 0;
									 			else if(a.getStage()==null || a.getStage().equals(""))
									 				result = -1;
									 			else if(b.getStage()==null || b.getStage().equals(""))
									 				result = 1;
									 			else if(!a.getPortfolioManager().equals(b.getPortfolioManager()))
									 				result = a.getPortfolioManager().compareTo(b.getPortfolioManager());
									 			else if(!a.getStage().equals(b.getStage()))
									 				result = Stage.valueOf(a.getStage()).compareTo(Stage.valueOf(b.getStage()));
									 			else if(!a.getProjectId().equals(b.getProjectId()))
									 				result = a.getProjectId().compareTo(b.getProjectId());
									 			return result;
									 		}//End of compare
		 								}
		 );//End of Collections.sort();
	}
	
}
