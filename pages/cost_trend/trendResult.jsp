<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="ChartDirector.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
<body>

<input type="hidden" id="cursorX"/>
<input type="hidden" id="cursorY"/>
    <a href="/dep/cost_trend"></a><br/>
	<img src='${costTrendInfo.chart1URL}' usemap="#map1" style ="border:none;">
	<map name="map1">${costTrendInfo.imageMap}</map>
	<div id="tooltip_123" class="tooltip" />
	
 
 </body>
</html>