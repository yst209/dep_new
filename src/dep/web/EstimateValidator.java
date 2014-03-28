package dep.web;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.EstimateForm;
import dep.model.EstimateInfo;


public class EstimateValidator implements Validator
{
	DecimalFormat formatter = new DecimalFormat("#,###.00");

	public boolean supports(Class aClass)
	{
		return aClass.equals(EstimateInfo.class);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {}
	
	public void validate(EstimateInfo info, Errors errors)
	{   
	    
		if(info.getReportMonth() != null && info.getReportMonth()==0L)
			errors.rejectValue("reportMonth", "error.invalid.month", "Please Select Month.");
		if(info.getEditMonth() != null && info.getEditMonth()==0L)
			errors.rejectValue("reportMonth", "error.invalid.month", "Please Select Month.");
	}
	
	public void validate(EstimateForm form, Errors errors)
	{
		String dateRegex = "^(0[1-9]|1[012])[/]?(0[1-9]|[12][0-9]|3[01])[/]?(18|19|20|21)\\d{2}$";
//		String dollarRegex = "(,\\d\\d\\d)*";
		String dollarRegex = "[\\d]+\\.?[\\d]+";
		
		for(int i =0; i<=form.getProjects().size()-1;i++)
		{
//			System.out.println(formatter.format(amount));
			
			if(form.getProjects().get(i).getBudget() != null && form.getProjects().get(i).getBudget().length()>0 && !Pattern.matches(dollarRegex,form.getProjects().get(i).getBudget().replaceAll(",", "")))
				errors.rejectValue("budgetArray[" + i + "]", "error.invalid.date", "Invalid Budget Format. ");		
			if(form.getProjects().get(i).getLatestSubmittedEstimate() != null && form.getProjects().get(i).getLatestSubmittedEstimate().length()>0 && !Pattern.matches(dollarRegex,form.getProjects().get(i).getLatestSubmittedEstimate().replaceAll(",", "")))
				errors.rejectValue("latestSubmittedEstimateArray[" + i + "]", "error.invalid.date", "Invalid Latest Submitted Estimate Format. ");		
			
			if(form.getProjects().get(i).getDateReceived() != null && form.getProjects().get(i).getDateReceived().length()>0 && !Pattern.matches(dateRegex,form.getProjects().get(i).getDateReceived()))
				errors.rejectValue("dateReceivedArray[" + i + "]", "error.invalid.date", "Please enter received date in MM/dd/yyyy format. ");		
			if(form.getProjects().get(i).getDateCompleted() != null && form.getProjects().get(i).getDateCompleted().length()>0 && !Pattern.matches(dateRegex,form.getProjects().get(i).getDateCompleted()))
				errors.rejectValue("dateCompletedArray[" + i + "]", "error.invalid.date", "Please enter completed date in MM/dd/yyyy format. ");		
		}

	}
}
