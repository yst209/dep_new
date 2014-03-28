<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ChartDirector.*" %>
<%@ page import="dep.web.WorkloadController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>
      Google Visualization API
    </title>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load('visualization', '1', {packages: ['corechart']});
    </script>

    <script type="text/javascript">
      function drawVisualization() {
        // Some raw data (not necessarily accurate)
        var data = google.visualization.arrayToDataTable([
          ${staffloadInfo.arrayContent}

         ]);

        var options = {
		  tooltip: {isHtml: true},
		  vAxis : {title: "Number of Staff Needed", titleTextStyle: {italic: false, color:'#000000'}, viewWindow: { min: 0}},//
          hAxis: {title: "Fiscal Year", titleTextStyle: {italic: false, color:'#000000'}},
          seriesType: "bars"
        };

		
        var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
      google.setOnLoadCallback(drawVisualization);

    </script>

  </head>
  <body>
  	<c:set var="currentString" value="Current Projects, ${staffloadInfo.managerType} - ${staffloadInfo.managerName}" />
    <div style="width: 1200px; height: 50px;font-weight:bold;font-size:30px;margin-left:220px;">Staffload Chart - WWC, ${staffloadInfo.projectStatus=='Future' ? 'ALL Future Projects' : currentString} </div>
    <div id="chart_div" style="width: 1200px; height: 600px;margin-top:-15px;"></div>
        
    
  </body>
</html>