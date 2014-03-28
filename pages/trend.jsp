<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="ChartDirector.*" %>
<%@page import="dep.web.TrendController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core">
      
<style>
.tooltip 
{
    visibility: hidden; 
    position: absolute; 
    top: 50px;  
    left: 920px; 
    z-index: 2; 
    background-color: #689CDA;
    opacity:0.9;filter:alpha(opacity=90);
	border: solid 1px #2B67AF;
    color:#FFFFFF;
    font: normal 8pt arial; 
    padding: 10px; 
    text-align: left;
}
</style>
<script>
function tooltip_hide(id)
{
    it = document.getElementById(id); 
    it.style.visibility = 'hidden'; 
}

function tooltip_show(tooltipId, parentId)
{

    it = document.getElementById(tooltipId);
    var date=document.getElementById(parentId).getAttribute("date");
    var data_set=document.getElementById(parentId).getAttribute("data_set");
    var tootip=document.getElementById(parentId).getAttribute("tooltip");

    it.innerHTML = "<b><font size='2'>" + tootip + "<br/><br/>Project List:</font></b><br/>";
    
  	var monthlyMap = {
  		    <c:forEach items="${monthlyMap}" var="entry" varStatus="loop">
  		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value.projectList}" varStatus="inside_loop">${project.projectId} - ${project.currentDuration} ${inside_loop.index== (fn:length(entry.value.projectList) < 25 ? 25 :  (fn:length(entry.value.projectList) % 2 == 0 ? fn:length(entry.value.projectList)/2-1 : (fn:length(entry.value.projectList)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : '<br/></div>"'}</c:forEach>${!loop.last ? ',' : ''}
  		    </c:forEach>};

  	var comparedMonthlyMap = {
  		    <c:forEach items="${comparedMonthlyMap}" var="entry" varStatus="loop">
		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value.projectList}" varStatus="inside_loop">${project.projectId} - ${project.currentDuration} ${inside_loop.index== (fn:length(entry.value.projectList) < 25 ? 25 :  (fn:length(entry.value.projectList) % 2 == 0 ? fn:length(entry.value.projectList)/2-1 : (fn:length(entry.value.projectList)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : '<br/></div>"'}</c:forEach>${!loop.last ? ',' : ''}
  		    </c:forEach>};
  	
  	var monthlyBaselineMap = {
  		    <c:forEach items="${monthlyMap}" var="entry" varStatus="loop">
		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value.projectList}" varStatus="inside_loop">${project.projectId} - ${project.baselineDuration} ${inside_loop.index== (fn:length(entry.value.projectList) < 25 ? 25 :  (fn:length(entry.value.projectList) % 2 == 0 ? fn:length(entry.value.projectList)/2-1 : (fn:length(entry.value.projectList)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : '<br/></div>"'}</c:forEach>${!loop.last ? ',' : ''}
  		    </c:forEach>};
  	

  	if(data_set=="3" && comparedMonthlyMap[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=comparedMonthlyMap[20+date.substring(4) + monthNumber(date.substring(0,3))];
  	else if(data_set=="0" && monthlyBaselineMap[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=monthlyBaselineMap[20+date.substring(4) + monthNumber(date.substring(0,3))];
  	else if((data_set=="1" || data_set=="2") && monthlyMap[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=monthlyMap[20+date.substring(4) + monthNumber(date.substring(0,3))]; 

    

	it.style.top = parseInt(document.getElementById('cursorY').value) -120 +'';
	it.style.left = parseInt(document.getElementById('cursorX').value) + 20 +'';

    
    it.style.visibility = 'visible'; 
}

function monthNumber(month)
{
var displayMonth;
if (month == "Jan")
	displayMonth = "01";
else
if (month == "Feb")
	displayMonth = "02";
else
if (month == "Mar")
	displayMonth = "03";
else
if (month == "Apr")
	displayMonth = "04";
else
if (month == "May")
	displayMonth = "05";
else
if (month == "Jun")
	displayMonth = "06";
else
if (month == "Jul")
	displayMonth = "07";
else
if (month == "Aug")
	displayMonth = "08";
else
if (month == "Sep")
	displayMonth = "09";
else
if (month == "Oct")
	displayMonth = "10";
else
if (month == "Nov")
	displayMonth = "11";
else
if (month == "Dec")
	displayMonth = "12";

return displayMonth;
}	

function init() {
	if (window.Event) {
	document.captureEvents(Event.MOUSEMOVE);
	}
	document.onmousemove = getCursorXY;
}

function getCursorXY(e) {
	document.getElementById('cursorX').value = (window.Event) ? e.pageX : event.clientX + (document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft);
	document.getElementById('cursorY').value = (window.Event) ? e.pageY : event.clientY + (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop);
}
window.onload = init;
</script>
<body>

<input type="hidden" id="cursorX"/>
<input type="hidden" id="cursorY"/>

    
	<img src='${chart1URL}' usemap="#map1" style ="border:none;">
	<map name="map1">${imageMap}</map>
	${trend.chart1URL}
	<div id="tooltip_123" class="tooltip" />
	
 
 </body>
</html>