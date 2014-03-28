package dep.web;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import dep.model.BatchInfo;


public class BatchValidator implements Validator
{
	public boolean supports(Class aClass)
	{
		return aClass.equals(BatchInfo.class);
	}

	public void validate(Object o, Errors errors)
	{
		BatchInfo bi = (BatchInfo) o;
		
		String regEx = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9_-]+)+\\\\?";

//	    String path = "D:\\directoryname\\testing\\abc.txt";
//	    Pattern pattern = Pattern.compile(regEx);
//	    boolean isMatched = Pattern.matches(regEx,path);
//		System.out.println(pattern);
//		System.out.println(isMatched);
//		System.out.println(Pattern.matches(regEx,bi.getSourcePath()));	    
		if(bi.getDatePeriod()==0L)
			errors.rejectValue("datePeriod", "error.invalid.datePeriod", "Please Select Data Period.");

//		if(!bi.getUpdateTotalsOnly())
//		{
		if(bi.getCoordinator().equals("NONE"))
			errors.rejectValue("coordinator", "error.invalid.coordinator", "Please Select Project Control Coordinator.");

//			if(!Pattern.matches(regEx,bi.getSourcePath()))
//				errors.rejectValue("sourcePath", "error.invalid.sourcePath", "Invalid Source Location.");
//			if(!Pattern.matches(regEx,bi.getTargetPath()))
//				errors.rejectValue("targetPath", "error.invalid.targetPath", "Invalid Target Location.");
//		}
	}
}
