package dep.excel;

import java.util.Comparator;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dep.model.SCGHistoricalTrendEntity;
 
public class SCGComparator implements Comparator {

		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		
		public enum Stage {
			DesPro, P, Des, Des30, Des60, Des90, Des100, Con, ConPro, SC, FC, PM
		}
	
	  Map<String,SCGHistoricalTrendEntity> base;
	  public SCGComparator(Map<String,SCGHistoricalTrendEntity> base) {
	      this.base = base;
	  }

	  public int compare(Object a, Object b) {
//		System.out.println("compare");
		//a > b => 1
		//a < b => -1
		int result = 0;

//		System.out.println("base.get(a).getProjectId(): " + base.get(a).getProjectId());
//    	System.out.println("base.get(a).getCurrentStage(): " + base.get(a).getCurrentStage());
//		System.out.println("base.get(b).getProjectId(): " + base.get(b).getProjectId());
//    	System.out.println("base.get(b).getCurrentStage(): " + base.get(b).getCurrentStage());
		if(!base.get(a).getPortfolioManager().equals(base.get(b).getPortfolioManager()))
			result = base.get(a).getPortfolioManager().compareTo(base.get(b).getPortfolioManager());
		else
		{
	    	if((base.get(a).getCurrentStage() == null || base.get(a).getCurrentStage().equals("")) && (base.get(b).getCurrentStage() == null || base.get(b).getCurrentStage().equals("")))
	    		result = base.get(a).getProjectId().compareTo(base.get(b).getProjectId());
	    	else if(base.get(a).getCurrentStage() == null || base.get(a).getCurrentStage().equals(""))
	    		result = -1;
	    	else if(base.get(b).getCurrentStage() == null || base.get(b).getCurrentStage().equals(""))
	    		result = 1;
	    	else if(Stage.valueOf(base.get(a).getCurrentStage()).compareTo(Stage.valueOf(base.get(b).getCurrentStage())) < 0)
		    	result = -1;
		    else if(Stage.valueOf(base.get(a).getCurrentStage()).compareTo(Stage.valueOf(base.get(b).getCurrentStage())) > 0)
		    	result = 1;
		    else 
		    {
	//	    	System.out.println("base.get(a).getFinalDesignCompleted(): " + base.get(a).getFinalDesignCompleted());
	//	    	System.out.println("base.get(b).getFinalDesignCompleted(): " + base.get(b).getFinalDesignCompleted());
		    	if(base.get(a).getCurrentStage().equals("P") || base.get(a).getCurrentStage().startsWith("Des"))
		    	{
		    		if((base.get(a).getFinalDesignCompleted() == null || base.get(a).getFinalDesignCompleted().equals("")) && (base.get(b).getFinalDesignCompleted() == null || base.get(b).getFinalDesignCompleted().equals("")))
		    			result = base.get(a).getProjectId().compareTo(base.get(b).getProjectId());
		    		else if(base.get(a).getFinalDesignCompleted() == null || base.get(a).getFinalDesignCompleted().equals(""))
		    			result = -1;
		    		else if(base.get(b).getFinalDesignCompleted() == null || base.get(b).getFinalDesignCompleted().equals(""))
		    			result = 1;
		    		else if(dateFormatter.parseDateTime(base.get(a).getFinalDesignCompleted()).isBefore(dateFormatter.parseDateTime(base.get(b).getFinalDesignCompleted())))
		    			result = -1;
		    		else if(dateFormatter.parseDateTime(base.get(a).getFinalDesignCompleted()).isAfter(dateFormatter.parseDateTime(base.get(b).getFinalDesignCompleted())))
		    			result = 1;
		    		else
		    			result = base.get(a).getProjectId().compareTo(base.get(b).getProjectId());
		    	}
		    	else if(base.get(a).getCurrentStage().equals("Con"))
		    	{
		    		if((base.get(a).getCurrentNTP() == null || base.get(a).getCurrentNTP().equals("")) && (base.get(b).getCurrentNTP() == null || base.get(b).getCurrentNTP().equals("")))
		    			result = base.get(a).getProjectId().compareTo(base.get(b).getProjectId());
		    		else if(base.get(a).getCurrentNTP() == null || base.get(a).getCurrentNTP().equals(""))
		    			result = -1;
		    		else if(base.get(b).getCurrentNTP() == null || base.get(b).getCurrentNTP().equals(""))
		    			result = 1;
		    		else if(dateFormatter.parseDateTime(base.get(a).getCurrentNTP()).isBefore(dateFormatter.parseDateTime(base.get(b).getCurrentNTP())))
		    			result = -1;
		    		else if(dateFormatter.parseDateTime(base.get(a).getCurrentNTP()).isAfter(dateFormatter.parseDateTime(base.get(b).getCurrentNTP())))
		    			result = 1;
		    		else
		    			result = base.get(a).getProjectId().compareTo(base.get(b).getProjectId());
		    	}
		    	else
		    		result = base.get(a).getProjectId().compareTo(base.get(b).getProjectId());
	
		    }
		}
	    
//	    System.out.println("base.get(a).getProjectId().compareTo(base.get(b).getProjectId()): " + base.get(a).getProjectId().compareTo(base.get(b).getProjectId()));
	    return result;
	  }
	}