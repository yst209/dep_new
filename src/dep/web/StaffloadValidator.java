package dep.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.StaffloadInfo;


public class StaffloadValidator implements Validator
{
	public boolean supports(Class aClass)
	{
		return aClass.equals(StaffloadInfo.class);
	}

	public void validate(Object o, Errors errors)
	{
		StaffloadInfo si = (StaffloadInfo) o;
		
		if(si.getProjectStatus().equals("NONE"))
			errors.rejectValue("projectStatus", "error.invalid.projectStatus", "Please select project status.");
//		if(si.getMasterProgram().equals("NONE"))
//			errors.rejectValue("masterProgram", "error.invalid.masterProgram", "Please select Master Program.");
		if(!si.getProjectStatus().equals("Future") && (!si.getPortfolioManager().equals("NONE") && !si.getAccountableManager().equals("NONE")))
			errors.rejectValue("managerType", "error.invalid.managerType", "You can't select both PM and AM. Please select either PM or AM.");
		else if(!si.getProjectStatus().equals("Future") && (si.getPortfolioManager().equals("NONE") && si.getAccountableManager().equals("NONE")))
			errors.rejectValue("managerType", "error.invalid.managerType", "Please select either PM or AM.");
		if(si.getChartScale().equals("NONE"))
			errors.rejectValue("chartScale", "error.invalid.chartScale", "Please select chart scale.");
		if(si.getEndFiscalYear()==0L)
			errors.rejectValue("endFiscalYear", "error.invalid.endFiscalYear", "Please select end fiscal year.");


	}
}
