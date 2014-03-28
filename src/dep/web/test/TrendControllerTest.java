package dep.web.test;

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
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dep.web.TrendController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/test-config.xml")
public class TrendControllerTest implements ApplicationContextAware
{

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
	public void testHelloController() {
		TrendController c= new TrendController();
		c.generateDropdown();
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
