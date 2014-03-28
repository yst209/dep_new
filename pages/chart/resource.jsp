<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
          ['Month', 'Administative Tech', 'Project Manager for Design', 'Project Manager for Construction', 'Associate Project Manager Level 1', 'Associate Project Manager Level 2', 'Associate Project Manager Level 3', 'Manager M Title', 'Portfolio Manager'],
          ['FY 2013 Q3',  7.55,      11.7,         10,             10,           10,      10,      10,      10],
          ['FY 2013 Q4',  8.05,      10.2,        10,             10,          10,      10,      10,      10],
          ['FY 2014 Q1',  8.8,      8.55,        10,             10,           10,      10,      10,      10],
          ['FY 2014 Q2',  9.1,      9.55,        10,             10,           10,      10,      10,      10],
          ['FY 2014 Q3',  9.6,      10.55,         10,             10,          10,      10,      10,      10],
          ['FY 2014 Q4',  9.6,      10.55,         10,             10,          10,      10,      10,      10],
          ['FY 2015 Q1',  8.8,      8.55,        10,             10,           10,      10,      10,      10],
          ['FY 2015 Q2',  9.1,      9.55,        10,             10,           10,      10,      10,      10],
          ['FY 2015 Q3',  9.6,      10.55,         10,             10,          10,      10,      10,      10],
          ['FY 2015 Q4',  9.6,      10.55,         10,             10,          10,      10,      10,      10]
         ]);

        var options = {
		  titleTextStyle: {color: '#000000', fontSize:20},
          title : 'Resource Allocation Charttt',
		  vAxis : {title: "Staff Load"},//
          hAxis: {title: "Fiscal Year"},
          seriesType: "bars"
        };

        var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
      google.setOnLoadCallback(drawVisualization);
    </script>

  </head>
  <body>
    <div id="chart_div" style="border:1px;width: 1200px; height: 600px;"></div>
    <div id="chart_div2" style="width: 1200px; height: 800px;"></div>
  </body>
</html>