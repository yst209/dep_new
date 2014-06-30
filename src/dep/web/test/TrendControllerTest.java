package dep.web.test;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import dep.model.Account;
import dep.model.SubAccount;
import dep.web.TrendController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/test/test-servlet.xml")	//C:\tomcat\webapps\dep\src\
public class TrendControllerTest implements ApplicationContextAware
{

	private  AtomicInteger value = new AtomicInteger(0);
	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void getHome() throws Exception {
//        this.mockMvc.perform(get("/"))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(forwardedUrl("/jsp/index.jsp"));
    }
    
	@Test()
	public void testAccount() {
		System.out.println("testAccount");
//		Account account = new Account(5L);
		SubAccount sub1 = new SubAccount(5L, "hey");
		SubAccount sub2 = new SubAccount(6L, "hey");
		System.out.println("equal: " + sub1.equals(sub2));
	}
    
	@Test()
	public void testHelloController() {
		TrendController c= new TrendController();
		c.generateDropdown();
		
		Queue q = new LinkedList();
		PriorityQueue q2 = new PriorityQueue();
		AtomicInteger value = new AtomicInteger(0);
		
		
		//ArrayList -> CopyOnWriteArrayList
		Map<String,String> map = new ConcurrentHashMap<String,String>();//ConcurrentHashMap can solve this issue
		map.put("1", "a");
		System.out.println("before map size: " + map.size());
	    Iterator<Entry<String, String>> it = map.entrySet().iterator();
	    while (it.hasNext()) {//iterator is working on the initial list
			System.out.println("before adding c map size: " + map.size());
			map.put("3", "c"); //Ok, map size not changed
//			map.put("3", "c");//ConcurrentModificationException, map size changed when using iterator
	        Map.Entry<String, String> pairs = it.next();
	        System.out.println("after adding c map size: " + map.size());
	    }
	    
	    System.out.println("... map size: " + map.size());
	    
	    
        Map<String,String> myMap = new ConcurrentHashMap<String,String>();
        myMap.put("1", "1");
        myMap.put("2", "2");
        myMap.put("3", "3");
         
        Iterator<String> it1 = myMap.keySet().iterator();
        while(it1.hasNext()){
            String key = it1.next();
            System.out.println("Map Value:"+myMap.get(key));
            if(key.equals("3")){
                myMap.remove("2");
                myMap.put("4", "4");
                myMap.put("5", "5");
            }
        }
        
        it1 = myMap.keySet().iterator();
        while(it1.hasNext()){
            String key = it1.next();
            System.out.println("Map Value:"+myMap.get(key));

        }
         
        System.out.println("Map Size:"+myMap.size());
	    
	    
//		System.out.println(c.getBureauSelectList());
//	    Assert.isTrue(c.getBureauSelectList().size()==2);
	}
	
	 @Test()
	    public void testController() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // request init here

        MockHttpServletResponse response = new MockHttpServletResponse();
//        Object handler = handlerMapping.getHandler(request).getHandler();
        
        TrendController c= new TrendController();
        
//        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        ModelAndView modelAndView = c.index(request, response);
        System.out.println(modelAndView.getViewName());
        
//        Assert

        // modelAndView and/or response asserts here
    }
	

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}
	
}
