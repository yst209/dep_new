package dep.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.TrendInfo;


public class TrendValidator implements Validator
{
	public boolean supports(Class aClass)
	{
		return aClass.equals(TrendInfo.class);
	}

	public void validate(Object o, Errors errors)
	{
		TrendInfo ti = (TrendInfo) o;
		

//		System.out.println(ti.getBureau());
//		System.out.println(ti.getOperatingBureau());
//		System.out.println(ti.getComparedMonth());
		if(ti.getBureau().equals("NONE"))
			errors.rejectValue("bureau", "error.invalid.bureau", "Please select bureau.");
		if(ti.getOperatingBureau().equals("NONE"))
			errors.rejectValue("bureau", "error.invalid.bureau", "Please select operating bureau.");
		if (ti.getBureau().equals("BWS") && !ti.getOperatingBureau().equals("BWS"))
			errors.rejectValue("bureau", "error.invalid.bureau", "Invalid bureau and operating bureau combination.");
		if(ti.getComparedMonth()==0L)
			errors.rejectValue("bureau", "error.invalid.bureau", "Please select compared month.");
		if(ti.getCurrentMonth()==0L)
			errors.rejectValue("bureau", "error.invalid.bureau", "Please select current month.");
		if(ti.getEndPeriod()==0L)
			errors.rejectValue("bureau", "error.invalid.bureau", "Please select trend chart end period.");
			

//		if (Double.compare(li.getPrincipal(), 0.0) <= 0)
//			errors.rejectValue("principal", "error.invalid.principal", "Principal invalid");
//		if (Double.compare(li.getApr(), 0.0) <= 0)
//			errors.rejectValue("apr", "error.invalid.apr", "APR invalid");
//		if (li.getYears() <= 0)
//			errors.rejectValue("years", "error.invalid.years", "Number of years invalid");
//		if (li.getPeriodPerYear() <= 0)
//			errors.rejectValue("periodPerYear", "error.invalid.periodPerYear", "Period invalid");
	}
}
