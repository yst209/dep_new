package dep.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.WorkloadInfo;


public class WorkloadValidator implements Validator
{
	public boolean supports(Class aClass)
	{
		return aClass.equals(WorkloadInfo.class);
	}

	public void validate(Object o, Errors errors)
	{
		WorkloadInfo wi = (WorkloadInfo) o;
		
		if(wi.getStage().equals("NONE"))
			errors.rejectValue("stage", "error.invalid.stage", "Please select project stage.");
		if(wi.getMasterProgram().equals("NONE"))
			errors.rejectValue("masterProgram", "error.invalid.masterProgram", "Please select Master Program.");
		if(wi.getManagedBy().equals("NONE"))
			errors.rejectValue("managedBy", "error.invalid.managedBy", "Please select who projects are managed by.");
		if(wi.getEndPeriod()==0L)
			errors.rejectValue("endPeriod", "error.invalid.endPeriod", "Please select workload chart end period.");


	}
}
