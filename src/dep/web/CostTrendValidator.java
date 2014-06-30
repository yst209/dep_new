package dep.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.CostTrendInfo;


public class CostTrendValidator implements Validator
{
	public boolean supports(Class aClass)
	{
		return aClass.equals(CostTrendInfo.class);
	}

	public void validate(Object o, Errors errors)
	{
		CostTrendInfo ti = (CostTrendInfo) o;
		
//		System.out.println(ti.getPortfolioManager());
//		System.out.println(ti.getProjectId());
//		System.out.println(ti.getContractType());
//		
		if(ti.getProjectId().equals("NONE"))
			errors.rejectValue("projectId", "error.invalid.projectId", "Please Select Project ID.");
		if(ti.getContractType().equals("NONE"))
			errors.rejectValue("contractType", "error.invalid.contractType", "Please Select Contract Type.");
	}
}
