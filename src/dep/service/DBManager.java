package dep.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import dep.model.ContractEntity;





public class DBManager {
	 //
	private static List<ContractEntity> trendList = new ArrayList<ContractEntity>();
//	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//	DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
//	DateTime dateTime;
	static {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		ContractEntity entity1 = new ContractEntity();
		entity1.setMonth(4L);
		entity1.setYear(2011L);
		entity1.setProjectId("CAT-177");		
		entity1.setNoticeToProceed(formatter.parseDateTime("2/11/2008").toDate());
		entity1.setSubstantialCompletionOfBaseline(formatter.parseDateTime("8/12/2008").toDate());
		entity1.setSubstantialCompletion(formatter.parseDateTime("6/1/2011").toDate());

		ContractEntity entity2 = new ContractEntity();
		entity2.setMonth(4L);
		entity2.setYear(2011L);
		entity2.setProjectId("CAT-191");		
		entity2.setNoticeToProceed(formatter.parseDateTime("11/1/2005").toDate());
		entity2.setSubstantialCompletionOfBaseline(formatter.parseDateTime("10/26/2006").toDate());
		entity2.setSubstantialCompletion(formatter.parseDateTime("6/9/2011").toDate());

		
		trendList = new ArrayList<ContractEntity>();
		trendList.add(entity1);
		trendList.add(entity2);

		
//		Brand brand1 = new Brand();
//		brand1.setId((long)1);
//		brand1.setName("Mercedes");
//		brand1.setCountry("Germany");		
// 
//		Brand brand2 = new Brand();
//		brand2.setId((long)2);
//		brand2.setName("Peugeot");
//		brand2.setCountry("France");		
// 
//		Car car1 = new Car();
//		car1.setId((long)1);
//		car1.setBrand(brand1);
//		car1.setModel("SL 500");
//		car1.setPrice(new BigDecimal(40000));
// 
//		Car car2 = new Car();
//		car2.setId((long)2);
//		car2.setBrand(brand2);
//		car2.setModel("607");
//		car2.setPrice(new BigDecimal(35000));
// 
//		carList = new LinkedList<Car>();
//		carList.add(car1);
//		carList.add(car2);		
	}
	
	public List<ContractEntity> getTrendList() {
		// parse the string
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		DateTime date1 = formatter.parseDateTime("4/11/2008");
		DateTime date2 = formatter.parseDateTime("2/11/2011");
		Days days = Days.daysBetween(date1, date2);
		
//		DateTime date = new DateTime(dateFormat.parse("02/11/2008")); 
		System.out.println(days.getDays());
		return trendList;
	}
 
}