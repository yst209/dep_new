package dep.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.SCGLogInfo;


public class SCGLogValidator implements Validator
{
	public boolean supports(Class aClass)
	{
		return aClass.equals(SCGLogInfo.class);
	}

	public void validate(Object o, Errors errors)
	{
		SCGLogInfo info = (SCGLogInfo) o;	    
	    
		if(info.getReportMonth() != null && info.getReportMonth()==0L)
			errors.rejectValue("reportMonth", "error.invalid.month", "Please Select Month.");
		if(info.getEditMonth() != null && info.getEditMonth()==0L)
			errors.rejectValue("reportMonth", "error.invalid.month", "Please Select Month.");
	}
}
