<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="ChartDirector.*" %>
<%@page import="dep.web.WorkloadController" %>
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
cloud 
{
     display:block;
     padding:40px;
     background:blue;
     color:#171728;
     font-weight:bold;
     text-align:center;
         width: 2px;
    height: 2px;
}
area 
{
     display:block;
     border: solid 1px #2B67AF;
     padding:40px;
     background:gold;
     color:#171717;
     font-weight:bold;
     text-align:center;
}

map
{
     display:block;
     border: solid 1px #2B67AF;
     padding:40px;
     background:gold;
     color:#171717;
     font-weight:bold;
     text-align:center;
}
#container {
    width: 100px;
    height: 100px;
    position: relative;
}

#navi
{
	padding-left:100px;
	padding-left:100px;
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
} 
#infoi {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
}

#infoi {
    z-index: 10;
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
    
  	var map1 = {
  		    <c:forEach items="${workloadInfo.map1}" var="entry" varStatus="loop">
  		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value}" varStatus="inside_loop">${project.projectId}  ${inside_loop.index== (fn:length(entry.value) < 25 ? 25 :  (fn:length(entry.value) % 2 == 0 ? fn:length(entry.value)/2-1 : (fn:length(entry.value)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : ''}</c:forEach>${!loop.last ? '<br/></div>",' : '<br/></div>"'}
  		    </c:forEach>};

  	var map2 = {
  		    <c:forEach items="${workloadInfo.map2}" var="entry" varStatus="loop">
		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value}" varStatus="inside_loop">${project.projectId} ${inside_loop.index== (fn:length(entry.value) < 25 ? 25 :  (fn:length(entry.value) % 2 == 0 ? fn:length(entry.value)/2-1 : (fn:length(entry.value)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : ''}</c:forEach>${!loop.last ? '<br/></div>",' : '<br/></div>"'}
  		    </c:forEach>};
  	
  	var map3 = {
  		    <c:forEach items="${workloadInfo.map3}" var="entry" varStatus="loop">
		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value}" varStatus="inside_loop">${project.projectId}  ${inside_loop.index== (fn:length(entry.value) < 25 ? 25 :  (fn:length(entry.value) % 2 == 0 ? fn:length(entry.value)/2-1 : (fn:length(entry.value)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : ''}</c:forEach>${!loop.last ? '<br/></div>",' : '<br/></div>"'}
  		    </c:forEach>};
  	
  	var mapAll = {
  		    <c:forEach items="${workloadInfo.mapAll}" var="entry" varStatus="loop">
		        "${entry.key}": "<div style='float:left;margin-right:10px;margin-top:15px;'><c:forEach var="project" items="${entry.value}" varStatus="inside_loop">${project.projectId}  ${inside_loop.index== (fn:length(entry.value) < 25 ? 25 :  (fn:length(entry.value) % 2 == 0 ? fn:length(entry.value)/2-1 : (fn:length(entry.value)-1)/2)   )    ? '</div><div style=\'float:left;\'>' : ''} ${!inside_loop.last ? '<br/>' : ''}</c:forEach>${!loop.last ? '<br/></div>",' : '<br/></div>"'}
  		    </c:forEach>};
  	

  	if(data_set=="0" && map1[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=map1[20+date.substring(4) + monthNumber(date.substring(0,3))];
  	else if(data_set=="1" && map2[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=map2[20+date.substring(4) + monthNumber(date.substring(0,3))];
  	else if(data_set=="2" && map3[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=map3[20+date.substring(4) + monthNumber(date.substring(0,3))];
  	else if(data_set=="3" && mapAll[20+date.substring(4) + monthNumber(date.substring(0,3))] != null)
  		it.innerHTML+=mapAll[20+date.substring(4) + monthNumber(date.substring(0,3))];
    

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
	document.createElement('cloud');
	document.createElement('map');
	document.createElement('area');
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

<div id="container">
	<div id="navi">
	<cloud>
	     <p> Text </p>
	</cloud>
	
	</div>
	<divid="infoi" style="floatx:left;">
	<input type="hidden" id="cursorX"/>
	<input type="hidden" id="cursorY"/>
	    <a href="/dep/workload.html"></a><br/>
		<img src='${workloadInfo.chart1URL}' usemap="#map1" style ="border:none;">
		<map name="map1">${workloadInfo.imageMap}</map>
		<div id="tooltip_123" class="tooltip" />	
	</div>
</div>
 </body>
</html>