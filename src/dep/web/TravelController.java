package dep.web;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dep.model.TravelInfo;

@Controller
@RequestMapping("/travel/**")
public class TravelController
{
	private Logger logger = Logger.getLogger(TravelController.class);
	
	private List<String> cookies;
	private HttpURLConnection conn;
	String url = "https://www.evaair.com/en-us/index.html";
	String postUrl = "http://eservice.evaair.com/Evaweb/EVA/B2C/booking-online-blank.aspx?lang=en-us";
	String etravelUrl;
	String postParams2;
	String gmail = "https://mail.google.com/mail/";
	TravelInfo travelInfo;
	
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
	DateTimeFormatter yyyyMMddHHmmssFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	DateTimeFormatter yyyyMMddFormatter = DateTimeFormat.forPattern("yyyyMMdd");
	DateTimeFormatter MMddyyyyFormatter = DateTimeFormat.forPattern("MMddyyyy");
	DateTimeFormatter ddMMyyyyFormatter = DateTimeFormat.forPattern("ddMMyyyy");
	DateTimeFormatter MM_dd_yyyyFormatter = DateTimeFormat.forPattern("MM-dd-yyyy");
	DateTimeFormatter yyyy_MM_ddFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	 
	private final String USER_AGENT = "Mozilla/5.0";
	
	Map<String,String> flightTypeSelectList;
	Map<String,String> fromSelectList;
	Map<String,String> toSelectList;
	Map<String,String> departingDateSelectList;
	Map<String,String> returningDateSelectList;
	Map<String,String> classSelectList;
	Map<String,String> adultsSelectList;
	Map<String,String> childrenSelectList;
	
//	map.put("LANGUAGE", "US");
//	map.put("TRIP_TYPE", "R");//One Way: O
//	map.put("SITE", "CBHGCBHG");
//	map.put("COMMERCIAL_FARE_FAMILY_1", "NEWBIZ");//Class: Economy Class - NEWECO, Evergreen Deluxe Class/Elite Class - NEWDELOW, Business Class - NEWBIZ
//	map.put("IS_FLEXIBLE", "TRUE");
//	map.put("B_LOCATION_1", "JFK");
//	map.put("E_LOCATION_1", "TPE");
//	map.put("B_DATE_1", "201409050000");
//	map.put("B_DATE_2", "201409230000");
//	map.put("NB_ADT", "1");
//	map.put("NB_CHD", "0");
//	map.put("NB_INF", "0");
//	map.put("NB_STU", "0");
//	map.put("WDS_BR_PORTAL", "AIBS");

	public void generateDropdown()
	{
		flightTypeSelectList = new LinkedHashMap<String,String>();
		flightTypeSelectList.put("R", "Round Trip");//Back end, Front end
		flightTypeSelectList.put("O", "One Way");
		
		fromSelectList = new LinkedHashMap<String,String>();
		fromSelectList.put("JFK", "New York (John F Kennedy)");//Back end, Front end
		fromSelectList.put("LAX", "Los Angeles");
		fromSelectList.put("SEA", "Seattle");
		fromSelectList.put("SFO", "San Francisco");
		fromSelectList.put("YYZ", "Toronto");
		fromSelectList.put("YVR", "Vancouver");
		fromSelectList.put("TPE", "Taipei");
		fromSelectList.put("NRT", "Tokyo");
		fromSelectList.put("ICN", "Seoul");
		fromSelectList.put("HKG", "Hong Kong");
		fromSelectList.put("PVG", "Shanghai");

		
		toSelectList = new LinkedHashMap<String,String>();
		toSelectList.put("TPE", "Taipei");
		toSelectList.put("LAX", "Los Angeles");
		toSelectList.put("JFK", "New York (John F Kennedy)");
		toSelectList.put("SEA", "Seattle");
		toSelectList.put("SFO", "San Francisco");
		toSelectList.put("YYZ", "Toronto");
		toSelectList.put("YVR", "Vancouver");
		toSelectList.put("NRT", "Tokyo");
		toSelectList.put("ICN", "Seoul");
		toSelectList.put("HKG", "Hong Kong");
		toSelectList.put("PVG", "Shanghai");
		
		//Class: Economy Class - NEWECO, Evergreen Deluxe Class/Elite Class - NEWDELOW, Business Class - NEWBIZ
		classSelectList = new LinkedHashMap<String,String>();
		classSelectList.put("NEWECO", "Economy Class");//Back end, Front end
		classSelectList.put("NEWDELOW", "Evergreen Deluxe Class/Elite Class(Only for EvaAir)");
		classSelectList.put("NEWBIZ", "Business Class/Royal Laurel Class/Premium Laurel Class(Only for EvaAir");

		
		adultsSelectList = new LinkedHashMap<String,String>();
		adultsSelectList.put("1", "1");//Back end, Front end
		adultsSelectList.put("2", "2");
		adultsSelectList.put("3", "3");
		adultsSelectList.put("4", "4");

		childrenSelectList = new LinkedHashMap<String,String>();
		childrenSelectList.put("0", "0");
		childrenSelectList.put("1", "1");
		childrenSelectList.put("2", "2");
		childrenSelectList.put("3", "3");

	}
	
	@RequestMapping(method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		logger.info("IP: " + request.getRemoteHost());
		
		generateDropdown();
		ModelAndView modelAndView = new ModelAndView("travel/travel");
		modelAndView.addObject("travelInfo", new TravelInfo());
		modelAndView.addObject("flightTypeSelectList", flightTypeSelectList);
		modelAndView.addObject("fromSelectList", fromSelectList);
		modelAndView.addObject("toSelectList", toSelectList);
		modelAndView.addObject("classSelectList", classSelectList);
		modelAndView.addObject("adultsSelectList", adultsSelectList);
		modelAndView.addObject("childrenSelectList", childrenSelectList);
		return modelAndView;
	}
	
//    @RequestMapping(value = "/allResults", method = RequestMethod.POST)
	@RequestMapping(method = RequestMethod.POST)
    public ModelAndView searchAllAgents(@ModelAttribute("travelInfo") TravelInfo ti, BindingResult result, HttpServletRequest request) throws Exception
	{
		logger.info("searchAllAgents: " + request.getRemoteHost());
		logger.info("ti.getFromDate(): " + ti.getFromDate());
		logger.info("ti.getToDate(): " + ti.getToDate());
//		logger.info("ti.getFlightType(): " + ti.getFlightType());
		logger.info("ti.getClassType(): " + ti.getClassType());
		logger.info("ti.getFrom(): " + ti.getFrom());
		logger.info("ti.getTo(): " + ti.getTo());
		logger.info("ti.getAdults(): " + ti.getAdults());
		logger.info("ti.getChildren(): " + ti.getChildren());
		travelInfo = ti;

		String pricelineLink = "http://www.priceline.com/qp.asp?productID=1&TripType=RT&DepCity=" + 
				ti.getFrom() + "&ArrCity=" + ti.getTo() + "&DepartureDate=" + ti.getFromDate() + 
				"&DepTime=&ReturnDate=" + ti.getToDate() + "&RetTime=&CabinClass=ECO&numAdults=" +  ti.getAdults() + 
				"&numChildren=" + ti.getChildren() + "&NumTickets=" + (Integer.parseInt(ti.getAdults()) + Integer.parseInt(ti.getChildren()));
		
		
		String orbitzLink = "http://www.orbitz.com/shop/home?type=air&ar.type=roundTrip&strm=true&ar.rt.leaveSlice.orig.key=" + 
				ti.getFrom() + "&_ar.rt.leaveSlice.originRadius=0&ar.rt.leaveSlice.dest.key=" + ti.getTo() + "&_ar.rt.leaveSlice.destinationRadius=0&ar.rt.leaveSlice.date=" + ti.getFromDate() + 
				"&ar.rt.leaveSlice.time=Anytime&ar.rt.returnSlice.date=" + ti.getToDate() + "&ar.rt.returnSlice.time=Anytime&ar.rt.flexAirSearch=true&_ar.rt.flexAirSearch=0&ar.rt.numAdult=" +  (Integer.parseInt(ti.getAdults()) + Integer.parseInt(ti.getChildren())) + 
				"&ar.rt.numSenior=0&ar.rt.numChild=0&_ar.rt.nonStop=1&_ar.rt.narrowSel=0&ar.rt.narrow=airlines&ar.rt.cabin=C&search=Search+Flights";		
		
		String cheapticketsLink = "http://www.cheaptickets.com/shop/airsearch?type=air&ar.type=roundTrip&&strm=true&ar.rt.leaveSlice.orig.key=" + 
				ti.getFrom() + "&_ar.rt.leaveSlice.originRadius=0&ar.rt.leaveSlice.dest.key=" + ti.getTo() + "&_ar.rt.leaveSlice.destinationRadius=0&ar.rt.leaveSlice.date=" + ti.getFromDate() + 
				"&ar.rt.leaveSlice.time=Anytime&ar.rt.returnSlice.date=" + ti.getToDate() + "&ar.rt.returnSlice.time=Anytime&ar.rt.flexAirSearch=true&_ar.rt.flexAirSearch=0&ar.rt.numAdult=" +  (Integer.parseInt(ti.getAdults()) + Integer.parseInt(ti.getChildren())) + 
				"&ar.rt.numSenior=0&ar.rt.numChild=0&_ar.rt.nonStop=1&_ar.rt.narrowSel=0&ar.rt.narrow=airlines&ar.rt.cabin=C&search=Search";		
		
		//Dates need to be one month behind.
		String vayamaLink = "http://www.vayama.com/home/searching.jsp?vayamaVisit=0&clickThrough=N&originArray=" + 
				ti.getFrom() + "," + ti.getTo() + "&destArray=" + ti.getTo() + "," + ti.getFrom() + "&dateArray=" + dateFormatter.parseDateTime(ti.getFromDate()).minusMonths(1).toString(ddMMyyyyFormatter) + "," + dateFormatter.parseDateTime(ti.getToDate()).minusMonths(1).toString(ddMMyyyyFormatter) + 
				"&cabinClass=Y&carrPreferance=&noAdults=" +  ti.getAdults() + "&noSenior=0&noChild=" + ti.getChildren() +
				"&noInfants=0&noStudents=0&nonStops=1&timeArray=0,0,0,0&couponCode=&tripType=RT";	


		String travelocityLink = "http://www.travelocity.com/Flights-Search?trip=roundtrip&leg1=from:" + 
				ti.getFrom() + ",to:" + ti.getTo() + ",departure:" + ti.getFromDate() + "TANYT&leg2=from:" + 
				ti.getTo() + ",to:" + ti.getFrom() + ",departure:" + ti.getToDate() + 		
				"TANYT&passengers=children:" + ti.getChildren() + ",adults:" + ti.getAdults() + 
				",seniors:0,infantinlap:Y&mode=search";		

		String expediaLink = "http://www.expedia.com/Flights-Search?trip=roundtrip&leg1=from:" + 
				ti.getFrom() + ",to:" + ti.getTo() + ",departure:" + ti.getFromDate() + "TANYT&leg2=from:" + 
				ti.getTo() + ",to:" + ti.getFrom() + ",departure:" + ti.getToDate() + 		
				"TANYT&passengers=children:" + ti.getChildren() + ",adults:" + ti.getAdults() + 
				",seniors:0,infantinlap:Y&options=cabinclass:economy,nopenalty:N,sortby:price&mode=search";		

		String fareboomLink = "http://www.fareboom.com/SearchFromBare?olc=" + 
				ti.getFrom() + "&dlc=" + ti.getTo() + "&dd=" + dateFormatter.parseDateTime(ti.getFromDate()).toString(MM_dd_yyyyFormatter) + "&rd=" + dateFormatter.parseDateTime(ti.getToDate()).toString(MM_dd_yyyyFormatter) + 
				"&na=1&nc=&trip=round&search=true&RefSource=IM";		

		String kayakLink = "http://www.kayak.com/flights/" + 
				ti.getFrom() + "-" + ti.getTo() + 
				"/" + dateFormatter.parseDateTime(ti.getFromDate()).toString(yyyy_MM_ddFormatter) + "/" + dateFormatter.parseDateTime(ti.getToDate()).toString(yyyy_MM_ddFormatter);


		//Can't find from/to codes 
		String lastminutetravelLink = "http://www.lastminutetravel.com/FlightSearchResults.aspx?st=1&fmode=1&frm=11032&afto=9933&frm1=&afto1=&frm2=&afto2=&frm3=&afto3=&dep=" + 
				ti.getFromDate() + "&ret=" + ti.getToDate() + 	
				"&dept=-1&rett=-1&dep1=&dep2=&dep3=&dept1=&dept2=&dept3=&afId=0&mod=2&scf=1&adt=" + ti.getAdults() + "&snn=0&cnn=" + ti.getChildren() + 
				"&inf=0&ca=&air=-1&cos=1&nonStop=true&Mode=HomePage&cwsn=LastMinuteTravel&cwsa=&cu=en-US&trcsb=true&bsp=1&showValResults=1&38=788";		

		//Will time out
		String onetravelLink = "http://www.onetravel.com/Default.aspx?tabid=1919&sid=1&oa=" + 
				ti.getFrom() + "&da=" + ti.getTo() + 
				"&adt=" + ti.getAdults() + "&chd=" + ti.getChildren() + 
				"&snr=0&infl=0&infs=0&dd=" + dateFormatter.parseDateTime(ti.getFromDate()).toString(MM_dd_yyyyFormatter) + "&rd=" + dateFormatter.parseDateTime(ti.getToDate()).toString(MM_dd_yyyyFormatter) + 
				"&tt=ROUNDTRIP";		

		
		logger.info("dateFormatter.parseDateTime(ti.getFromDate()): " + dateFormatter.parseDateTime(ti.getFromDate()).toString(MMddyyyyFormatter));
		logger.info("pricelineLink: " + pricelineLink);
		logger.info("orbitzLink: " + orbitzLink);
		logger.info("cheapticketsLink: " + cheapticketsLink);
		logger.info("vayamaLink: " + vayamaLink);
		logger.info("travelocityLink: " + travelocityLink);
		logger.info("expediaLink: " + expediaLink);
		logger.info("fareboomLink: " + fareboomLink);
		logger.info("kayakLink: " + kayakLink);
		logger.info("lastminutetravelLink: " + lastminutetravelLink);
		logger.info("onetravelLink: " + onetravelLink);

//		dateFormatter.parseDateTime(ti.getFromDate());
		//dateFormatter.parseDateTime(ti.getToDate()).toString(pricelineFormatter)
		
		ModelAndView modelAndView = new ModelAndView("travel/allResults");
		modelAndView.addObject("travelInfo", ti);
		modelAndView.addObject("pricelineLink", pricelineLink);
		modelAndView.addObject("orbitzLink", orbitzLink);
		modelAndView.addObject("cheapticketsLink", cheapticketsLink);
		modelAndView.addObject("vayamaLink", vayamaLink);
		modelAndView.addObject("travelocityLink", travelocityLink);
		modelAndView.addObject("expediaLink", expediaLink);
		modelAndView.addObject("fareboomLink", fareboomLink);
		modelAndView.addObject("kayakLink", kayakLink);
		modelAndView.addObject("lastminutetravelLink", lastminutetravelLink);
		modelAndView.addObject("onetravelLink", onetravelLink);
		return modelAndView;
	}
	
    @RequestMapping(value = "/evaair", method = RequestMethod.POST)
    public ModelAndView searchEvaAir(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		logger.info("searchEvaAirFlights" + request.getRemoteHost());
		
		generateDropdown();
		
		logger.info("searchAllAgents: " + request.getRemoteHost());
		logger.info("travelInfo.getFromDate(): " + dateFormatter.parseDateTime(travelInfo.getFromDate()).toString(yyyyMMddFormatter)+"0000");
		logger.info("travelInfo.getToDate(): " + travelInfo.getToDate());
//		logger.info("ti.getFlightType(): " + ti.getFlightType());
		logger.info("travelInfo.getClassType(): " + travelInfo.getClassType());
		logger.info("travelInfo.getFrom(): " + travelInfo.getFrom());
		logger.info("travelInfo.getTo(): " + travelInfo.getTo());
		logger.info("travelInfo.getAdults(): " + travelInfo.getAdults());
		logger.info("travelInfo.getChildren(): " + travelInfo.getChildren());
//		String url = "https://accounts.google.com/ServiceLoginAuth";

	 

	 
		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());
	 
		// 1. Send a "GET" request, so that you can extract the form's data.
//		String page = getPageContent(url);
//		String postParams = getFormParams(page, "form1");
//	 
//		// 2. Construct above post's content and then send a POST request for
//		// authentication
//		String processingPageContent = sendPost(postUrl, postParams);
//		System.out.println("here: " + processingPageContent);		
////		processingPageContent = processingPageContent.replaceAll("<input type=\"hidden\" name=\"B_DATE_1\" value='201408120000'>", "<input type=\"hidden\" name=\"B_DATE_1\" value='201408220000'><input type=\"hidden\" name=\"B_DATE_2\" value='201408280000'>");
//		postParams2 = getFormParams(processingPageContent, "PostForm");
//		System.out.println("etravelUrl: " + etravelUrl);
//		
		
		
		etravelUrl = "https://wftc3.e-travel.com/EVAOnline/dyn/air/booking/availability?ENCT=1&ENC=4C62052AD3ED2EF8D6514E4F338B8D5351D82D299F6C76FB636925484B6B086545F442A2D800272DB72DCE38B7B54D10&ENC_TIME=";
		etravelUrl += new DateTime().toDateTime(DateTimeZone.forID("Asia/Taipei")).toString(yyyyMMddHHmmssFormatter);
		System.out.println("etravelUrl: " + etravelUrl);
		
		
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("LANGUAGE", "US");
		map.put("TRIP_TYPE", "R");//One Way: O
		map.put("SITE", "CBHGCBHG");
		map.put("COMMERCIAL_FARE_FAMILY_1", travelInfo.getClassType());//Class: Economy Class - NEWECO, Evergreen Deluxe Class/Elite Class - NEWDELOW, Business Class - NEWBIZ
		map.put("IS_FLEXIBLE", "TRUE");
		map.put("B_LOCATION_1", travelInfo.getFrom());
		map.put("E_LOCATION_1", travelInfo.getTo());
		map.put("B_DATE_1", dateFormatter.parseDateTime(travelInfo.getFromDate()).toString(yyyyMMddFormatter)+"0000");
		map.put("B_DATE_2", dateFormatter.parseDateTime(travelInfo.getToDate()).toString(yyyyMMddFormatter)+"0000");
		map.put("NB_ADT", travelInfo.getAdults());
		map.put("NB_CHD", travelInfo.getChildren());
		map.put("NB_INF", "0");
		map.put("NB_STU", "0");
		map.put("WDS_BR_PORTAL", "AIBS");
		
//		201409050000
		StringBuilder params = new StringBuilder();
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = it.next();
	  		if (params.length() == 0) {
	  			params.append(pairs.getKey() + "=" + pairs.getValue());
	  		} else {
	  			params.append("&" + pairs.getKey() + "=" + pairs.getValue());
	  		}
	    }
		
	    System.out.println("params: " + params);
	    
		//etravelUrl: https://wftc3.e-travel.com/EVAOnline/dyn/air/booking/availability?ENCT=1&ENC=4C62052AD3ED2EF8D6514E4F338B8D5351D82D299F6C76FB636925484B6B086545F442A2D800272DB72DCE38B7B54D10&ENC_TIME=20140805042344
		//postParams2: LANGUAGE=US&TRIP_TYPE=R&SITE=CBHGCBHG&COMMERCIAL_FARE_FAMILY_1=NEWECO&IS_FLEXIBLE=TRUE&B_LOCATION_1=LAX&E_LOCATION_1=TPE&B_DATE_1=201408220000&B_DATE_2=201408280000&NB_ADT=1&NB_CHD=0&NB_INF=0&NB_STU=0&WDS_BR_PORTAL=AIBS&LIST_CORPORATE_NUMBER_1_1=&WDS_CORPORATE_NAME=&WDS_ACCOUNT_NUMBER=
//		postParams2 = "LANGUAGE=US&TRIP_TYPE=R&SITE=CBHGCBHG&COMMERCIAL_FARE_FAMILY_1=NEWECO&IS_FLEXIBLE=TRUE&B_LOCATION_1=LAX&E_LOCATION_1=TPE&B_DATE_1=201408220000&B_DATE_2=201408280000&NB_ADT=1&NB_CHD=0&NB_INF=0&NB_STU=0&WDS_BR_PORTAL=AIBS&LIST_CORPORATE_NUMBER_1_1=&WDS_CORPORATE_NAME=&WDS_ACCOUNT_NUMBER=";
//		etravelUrl = "https://wftc3.e-travel.com/EVAOnline/dyn/air/booking/availability?ENCT=1&ENC=4C62052AD3ED2EF8D6514E4F338B8D5351D82D299F6C76FB636925484B6B086545F442A2D800272DB72DCE38B7B54D10&ENC_TIME=20140805042344";
		String pricePageHtml = sendPost(etravelUrl, params.toString());
		pricePageHtml = pricePageHtml.replaceAll("/EVAOnline", "https://wftc3.e-travel.com/EVAOnline");
		// 3. success then go to gmail.
//		String result = getPageContent(gmail);
		System.out.println("pricePageHtml: " + pricePageHtml);		
		
		
		
		ModelAndView modelAndView = new ModelAndView("travel/evaair");
		modelAndView.addObject("pricePageHtml", pricePageHtml);
		return modelAndView;
	}

    private String sendPost(String url, String postParams) throws Exception {
   
  	URL obj = new URL(url);
  	conn = (HttpURLConnection) obj.openConnection();
   
  	// Acts like a browser
  	conn.setUseCaches(false);
  	conn.setRequestMethod("POST");
  	conn.setRequestProperty("Host", "accounts.google.com");
  	conn.setRequestProperty("User-Agent", USER_AGENT);
  	conn.setRequestProperty("Accept",
  		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
  	conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
  	conn.addRequestProperty("Cookie", "");

  	conn.setRequestProperty("Connection", "keep-alive");
  	conn.setRequestProperty("Referer", "https://accounts.google.com/ServiceLoginAuth");
  	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
  	conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));
   
  	conn.setDoOutput(true);
  	conn.setDoInput(true);
   
  	// Send post request
  	DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
  	wr.writeBytes(postParams);
  	wr.flush();
  	wr.close();
   
  	int responseCode = conn.getResponseCode();
  	System.out.println("\nSending 'POST' request to URL : " + url);
  	System.out.println("Post parameters : " + postParams);
  	System.out.println("Response Code : " + responseCode);
   
  	BufferedReader in = 
               new BufferedReader(new InputStreamReader(conn.getInputStream()));
  	String inputLine;
  	StringBuffer response = new StringBuffer();
   
  	while ((inputLine = in.readLine()) != null) {
  		response.append(inputLine);
  	}
  	in.close();
  	 System.out.println(response.toString());
   
  	 return response.toString();
    }
   
    private String getPageContent(String url) throws Exception {
   
  	URL obj = new URL(url);
  	conn = (HttpURLConnection) obj.openConnection();
   
  	// default is GET
  	conn.setRequestMethod("GET");
   
  	conn.setUseCaches(false);
   
  	// act like a browser
  	conn.setRequestProperty("User-Agent", USER_AGENT);
  	conn.setRequestProperty("Accept",
  		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
  	conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
  	if (cookies != null) {
  		for (String cookie : this.cookies) {
  			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
  		}
  	}
  	int responseCode = conn.getResponseCode();
  	System.out.println("\nSending 'GET' request to URL : " + url);
  	System.out.println("Response Code : " + responseCode);
   
  	BufferedReader in = 
              new BufferedReader(new InputStreamReader(conn.getInputStream()));
  	String inputLine;
  	StringBuffer response = new StringBuffer();
   
  	while ((inputLine = in.readLine()) != null) {
  		response.append(inputLine);
  	}
  	in.close();
   
  	// Get the response cookies
  	setCookies(conn.getHeaderFields().get("Set-Cookie"));
   
  	return response.toString();
   
    }
   
    public String getFormParams(String html, String formId)
  		throws UnsupportedEncodingException {
   
  	System.out.println("Extracting form's data...");
   
  	Document doc = Jsoup.parse(html);
   

  		
  	// Google form id
  	Element loginform = doc.getElementById(formId);
  	
  	
//  	if(formId.equals("PostForm")){
//  		Elements formElements = loginform.getElementsByTag("form");
//  		for (Element form : formElements) {
//  			etravelUrl = form.attr("action");
//  			System.out.println("form: " + form.attr("action"));
//  			
//  			etravelUrl = "https://wftc3.e-travel.com/EVAOnline/dyn/air/booking/availability?ENCT=1&ENC=4C62052AD3ED2EF8D6514E4F338B8D5351D82D299F6C76FB636925484B6B086545F442A2D800272DB72DCE38B7B54D10&ENC_TIME=";
//  			etravelUrl += new DateTime().toDateTime(DateTimeZone.forID("Asia/Taipei")).toString(dateHourMinuteFormatter);
//  			System.out.println("etravelUrl: " + etravelUrl);
//  			//https://wftc3.e-travel.com/EVAOnline/dyn/air/booking/availability?ENCT=1&ENC=4C62052AD3ED2EF8D6514E4F338B8D5351D82D299F6C76FB636925484B6B086545F442A2D800272DB72DCE38B7B54D10&ENC_TIME=20140805221931
//  			//ENC_TIME=20140805221931
//  		}
//  	}
  	Elements inputElements = loginform.getElementsByTag("input");
  	List<String> paramList = new ArrayList<String>();
  	for (Element inputElement : inputElements) {

  		String key = inputElement.attr("name");
  		String value = inputElement.attr("value");
  		
  		if (key.equals("HID_DEPAREA"))
  			value = "02";
  		else if (key.equals("HID_DEPCITY"))
  			value = "LAX";
  		else if (key.equals("HID_ARRAREA"))
  			value = "01";
  		else if (key.equals("HID_ARRCITY"))
  			value = "TPE";
  		
  		else if (key.equals("TRIP_TYPE"))
  			value = "R";
  		else if (key.equals("IS_FLEXIBLE"))
  			value = "TRUE";
  		else if (key.equals("COMMERCIAL_FARE_FAMILY_1"))
  			value = "NEWECO";
  		
  		System.out.println("input key: " + key + ", Value: " + value);
 		paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
  	}
//  	if(formId.equals("PostForm"))
//  		paramList.add("B_DATE_2" + "=" + URLEncoder.encode("201408190000", "UTF-8"));
  	Elements selectElements = loginform.getElementsByTag("select");
  	for (Element selectElement : selectElements) {

  		String key = selectElement.attr("name");
  		String value = selectElement.attr("value");
   
  		
  		
  		if (key.equals("gDepArea"))
  			value = "02";
  		else if (key.equals("gGoDep"))
  			value = "LAX";
  		else if (key.equals("gArrArea"))
  			value = "01";
  		else if (key.equals("gGoArr"))
  			value = "TPE";
  		else if (key.equals("gGoYYYYMM"))
  			value = "2014-8";
  		else if (key.equals("gGoDD"))
  			value = "12";
  		else if (key.equals("gBackYYYYMM"))
  			value = "2014-8";
  		else if (key.equals("gBackDD"))
  			value = "19";
  		else if (key.equals("gCabinClass"))
  			value = "EY";
  		else if (key.equals("gAdult"))
  			value = "1";
  		else if (key.equals("gChild"))
  			value = "0";
   		
  		System.out.println("select key: " + key + ", Value: " + value);
 		paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
  	}

  	// build parameters list
  	StringBuilder result = new StringBuilder();
  	for (String param : paramList) {
  		if (result.length() == 0) {
  			result.append(param);
  		} else {
  			result.append("&" + param);
  		}
  	}
  	System.out.println("result: " + result);
  	return result.toString();
    }
   
    public List<String> getCookies() {
  	return cookies;
    }
   
    public void setCookies(List<String> cookies) {
  	this.cookies = cookies;
    }
   

	
}
