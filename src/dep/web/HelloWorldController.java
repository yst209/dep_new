package dep.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import ChartDirector.XYChart;

public class HelloWorldController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

remove("e", "ab", "abbcabbcedb");

String s ="aaabb cccccdfsfsd dsfsd       ";
char[] array = s.toCharArray();
//char[] array2 = new char[](10000);
StringBuilder sb = new StringBuilder();
for(char c : array)
{
	if(c!=' ')
		sb.append(c);
	else
		sb.append("%20");
}
sb.toString();
System.out.println("sb: " + sb);

StringBuilder sb2 = new StringBuilder();
int count = 0;
for(int i = 0;i<=s.length()-1;i++)
{
	int j=i;
	while(j<=s.length()-1 && s.charAt(i)==s.charAt(j))
	{
		count++;
		j++;
	}

	sb2.append(s.charAt(i));
	sb2.append(count);
	
	i+=count-1;
	count=0;
	
		
}
System.out.println("sb2x: " + sb2.toString());

int[][] matrix = {{1,2,3}, {4,5,0}, {7,8,9}};
int temp;

for(int i = 0;i<3;i++)
	for(int j = 0;j<3;j++)
		System.out.println("before ["+i+"][" + j + "]: " + matrix[i][j]);

//for(int i = 0;i<3;i++)
//	for(int j = i;j<3;j++)
//	{
//		if(i!=j)
//		{
//			temp = matrix[i][j];
//			matrix[i][j] = matrix[j][i];
//			matrix[j][i] = temp;
//		}
//	}

for(int i = 0;i<3;i++)
	for(int j = 0;j<3;j++)
	{
		if(matrix[i][j]==0)
		{
			for(int m = 0;m<3;m++)
				matrix[m][j]=0;
			for(int n = 0;n<3;n++)
				matrix[i][n]=0;
		}
	}

for(int i = 0;i<3;i++)
	for(int j = 0;j<3;j++)
		System.out.println("after ["+i+"][" + j + "]: " + matrix[i][j]);





		String aMessage = "Hello World MVC!xxx";
		
		ModelAndView modelAndView = new ModelAndView("hello_world");
		modelAndView.addObject("message", aMessage);
		
			// The data for the line chart
			double[] data = {30, 28, 40, 55, 75, 68, 54, 60, 50, 62, 75, 65, 75, 91, 60, 55, 53,
			    35, 50, 66, 56, 48, 52, 65, 62};

			// The labels for the line chart
			String[] labels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
			    "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

			// Create a XYChart object of size 250 x 250 pixels
			XYChart c = new XYChart(250, 250);

			// Set the plotarea at (30, 20) and of size 200 x 200 pixels
			c.setPlotArea(30, 20, 200, 200);

			// Add a line chart layer using the given data
			c.addLineLayer(data);

			// Set the labels on the x axis.
			c.xAxis().setLabels(labels);

			// Display 1 out of 3 labels on the x-axis.
			c.xAxis().setLabelStep(3);

			
			// Output the chart
			String chart1URL = c.makeSession(request, "chart1");

			// Include tool tip for the chart
			String imageMap1 = c.getHTMLImageMap("", "",
			    "title='Hour {xLabel}: Traffic {value} GBytes'");
			
			
				modelAndView.addObject("chart1URL", response.encodeURL("jsp/getchart.jsp?"+chart1URL));
//				System.out.println(response.encodeURL("jsp/getchart.jsp?"+chart1URL));
				modelAndView.addObject("imageMap1", imageMap1);
				modelAndView.addObject("getServletPath", request.getServletPath());
//				System.out.println(imageMap1);
//				System.out.println(request.getServletPath());
		
		return modelAndView;
	}
	
	public String remove(String regex1, String regex2, String s)
	{
		System.out.println(s);
		String output="";
		char[] charArray = s.toCharArray();
		
		for(int i =0; i<=s.length()-1;i++)
		{
			if(s.charAt(i)==regex1.charAt(0) && i + regex1.length() <= s.length() && s.substring(i, i + regex1.length()).equals(regex1))
			{ 
				i = i + regex1.length() - 1;
			}
			else if(s.charAt(i)==regex2.charAt(0) && i + regex2.length() <= s.length() && s.substring(i, i + regex2.length()).equals(regex2))
			{
				i = i + regex2.length() - 1;

			}
			else
			{
				output = output + s.charAt(i);
			}
		}
		
		System.out.println(output);
		return output;
	}
}
