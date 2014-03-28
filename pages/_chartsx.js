    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load('visualization', '1', {packages: ['corechart']});
    </script>


    <script type="text/javascript">
      function drawVisualization1() {
        // Some raw data (not necessarily accurate)

    	  var a = [
[1369, 20, 1, 0, 0, 0, 0, 0],
[23333, 1321, 783, 68, 0, 0, 0, 0],
[658, 61, 0, 0, 0, 0, 0, 0],
[22277, 2501, 1433, 84, 0, 0, 0, 0],
[9919, 1389, 807, 146, 0, 0, 0, 0],
[57556, 5292, 3024, 298, 0, 0, 0, 0]
];
//alert (a[0][1]-a[0][2]-a[0][3]);

var data = new google.visualization.DataTable();
data.addColumn({ type: 'string', label: 'Labels' });
data.addColumn({ type: 'number', label: 'Assessment Completed' });
data.addColumn({ type: 'number', label: 'Assessment Scheduled' });
data.addColumn({ type: 'number', label: 'Outstanding Assessment Requests' });
data.addColumn({ type: 'number', label: 'Line Series 1' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series 2' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series 3' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series Total' });
data.addColumn({ type: 'string', role: 'annotation' });

data.addRows(
[

['Bronx', a[0][3]+10, a[0][2], a[0][1]-a[0][2]-a[0][3], a[0][3]/2, null, a[0][3]+(a[0][2]/2), null, a[0][3]+a[0][2]+(a[0][1]-a[0][2]-a[0][3])/2, null, a[0][1]+50, a[0][1].toString()],
['Brooklyn', a[1][3], a[1][2], a[1][1]-a[1][2]-a[1][3], a[1][3]/2, a[1][3].toString(), a[1][3]+(a[1][2]/2), a[1][2].toString(), a[1][3]+a[1][2]+(a[1][1]-a[1][2]-a[1][3])/2-100, (a[1][1]-a[1][2]-a[1][3]).toString(), a[1][1]+50, a[1][1].toString()],
['Manhattan', a[2][3], a[2][2], a[2][1]-a[2][2]-a[2][3], a[2][3]/2, null, a[2][3]+(a[2][2]/2), null, a[2][3]+a[2][2]+(a[2][1]-a[2][2]-a[2][3])/2, null, a[2][1]+50, a[2][1].toString()],
['Queens', a[3][3]+15, a[3][2], a[3][1]-a[3][2]-a[3][3], a[3][3]/2, a[3][3].toString(), a[3][3]+(a[3][2]/2), a[3][2].toString(), a[3][3]+a[3][2]+(a[3][1]-a[3][2]-a[3][3])/2, (a[3][1]-a[3][2]-a[3][3]).toString(), a[3][1]+50, a[3][1].toString()],
['Staten Island', a[4][3], a[4][2], a[4][1]-a[4][2]-a[4][3], a[4][3]/2, a[4][3].toString(), a[4][3]+(a[4][2]/2), a[4][2].toString(), a[4][3]+a[4][2]+(a[4][1]-a[4][2]-a[4][3])/2-100, (a[4][1]-a[4][2]-a[4][3]).toString(), a[4][1]+50, a[4][1].toString()]
]);

  /*
  ['Bronx', a[0][3], a[0][2], a[0][1]-a[0][2]-a[0][3], a[0][3]/2, a[0][3].toString(), a[0][3]+(a[0][2]/2), a[0][2].toString(), a[0][3]+a[0][2]+(a[0][1]-a[0][2]-a[0][3])/2, (a[0][1]-a[0][2]-a[0][3]).toString(), a[0][1]+20, a[0][1].toString()],
  ['Brooklyn', a[1][3], a[1][2], a[1][1]-a[1][2]-a[1][3], a[1][3]/2, a[1][3].toString(), a[1][3]+(a[1][2]/2), a[1][2].toString(), a[1][3]+a[1][2]+(a[1][1]-a[1][2]-a[1][3])/2, (a[1][1]-a[1][2]-a[1][3]).toString(), a[1][1]+20, a[1][1].toString()],
  ['Manhattan', a[2][3], a[2][2], a[2][1]-a[2][2]-a[2][3], a[2][3]/2, a[2][3].toString(), a[2][3]+(a[2][2]/2), a[2][2].toString(), a[2][3]+a[2][2]+(a[2][1]-a[2][2]-a[2][3])/2, (a[2][1]-a[2][2]-a[2][3]).toString(), a[2][1]+20, a[2][1].toString()],
  ['Queens', a[3][3], a[3][2], a[3][1]-a[3][2]-a[3][3], a[3][3]/2, a[3][3].toString(), a[3][3]+(a[3][2]/2), a[3][2].toString(), a[3][3]+a[3][2]+(a[3][1]-a[3][2]-a[3][3])/2, (a[3][1]-a[3][2]-a[3][3]).toString(), a[3][1]+20, a[3][1].toString()],
  ['Staten Island', a[4][3], a[4][2], a[4][1]-a[4][2]-a[4][3], a[4][3]/2, a[4][3].toString(), a[4][3]+(a[4][2]/2), a[4][2].toString(), a[4][3]+a[4][2]+(a[4][1]-a[4][2]-a[4][3])/2, (a[4][1]-a[4][2]-a[4][3]).toString(), a[4][1]+20, a[4][1].toString()]
  ]);
  */


  var options = {//title:"Assessment Progress Tracking",
  			   titleTextStyle: {color: '#000000', fontSize:20},
  			   vAxis : {textColor: '#ffffff', viewWindow: { min: 0, max: 3000}},
  			   tooltip: {trigger:'none'},
  			   annotations: {style: 'letter', color: '#9999ff'},//annotation: {'column_id': {style: 'line'}}
  			   seriesType: "bars",
  			   isStacked:true,
  			   series: {
  						0: { type: 'bars', color: '#3E70D7' },
  						1: { type: 'bars', color: '#ACC5FF'  },
  						2: { type: 'bars', areaOpacity:0.0, color: '#D0E8FF' },
  						3: { type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color: '#000000'},
  						4: { type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color: '#000000'},
  						5: { type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color: '#000000',textColor: '#9999ff'},
  						6: { type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color: '#000000',textColor: '#9999ff'}
  					   }
  				};

          var chart = new google.visualization.ComboChart(document.getElementById('chart_div1'));
          chart.draw(data, options);
        }
        google.setOnLoadCallback(drawVisualization1);
    </script>

    <script type="text/javascript">
      function drawVisualization3() {
        // Some raw data (not necessarily accurate)

var a = [
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0]
];
//alert (a[0][1]-a[0][2]-a[0][3]);

var data = new google.visualization.DataTable();
data.addColumn({ type: 'string', label: 'Labels' });
data.addColumn({ type: 'number', label: 'Work Order Completed' });
data.addColumn({ type: 'number', label: 'Work Order In Progress' });
data.addColumn({ type: 'number', label: 'Outstanding Work Order' });

data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });

data.addRows(
[
['Bronx', a[0][7], a[0][6], a[0][5]-a[0][6]-a[0][7], 0, null, a[0][7], null, a[0][7]+a[0][6], null, a[0][5], null, a[0][5]+a[0][1]-a[0][2]-a[0][3]+16, null],
['Brooklyn', a[1][7], a[1][6], a[1][5]-a[1][6]-a[1][7], 0, null, a[1][7], null, a[1][7]+a[1][6], null, a[1][5], null, a[1][5]+a[1][1]-a[1][2]-a[1][3]+16, null],
['Manhattan', a[2][7], a[2][6], a[2][5]-a[2][6]-a[2][7], 0, null, a[2][7], null, a[2][7]+a[2][6], null, a[2][5], null, a[2][5]+a[2][1]-a[2][2]-a[2][3]+16, null],
['Queens', a[3][7], a[3][6], a[3][5]-a[3][6]-a[3][7], 0, null, a[3][7], null, a[3][7]+a[3][6], null, a[3][5], null, a[3][5]+a[3][1]-a[3][2]-a[3][3]+16, null],
['Staten Island', a[4][7], a[4][6], a[4][5]-a[4][6]-a[4][7], 0, null, a[4][7], null, a[4][7]+a[4][6], null, a[4][5], null, a[4][5]+a[4][1]-a[4][2]-a[4][3]+16, null]


]);

var options = {//title:"Work Order Progress Tracking",
			   titleTextStyle: {color: '#000000', fontSize:20},
  			   vAxis : {textColor: '#ffffff'},//, viewWindow: { min: 0, max: 2000}
			   tooltip: {trigger:'none'},
			   series: [
						//{ type: 'bars',color: '#003366'  },
						{ type: 'bars',color: '#3E70D7'  },
						{ type: 'bars',color: '#ACC5FF'  },
						{ type: 'bars',color: '#D0E8FF' },
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'}],
				isStacked:true
						
				};

        var chart = new google.visualization.ComboChart(document.getElementById('chart_div3'));
        chart.draw(data, options);
      }
      google.setOnLoadCallback(drawVisualization3);
    </script>



    <script type="text/javascript">
      function drawVisualization2() {
        // Some raw data (not necessarily accurate)

var a = [274, 100, 169, 118, 0, 0];
//alert (a[0][1]-a[0][2]-a[0][3]);

var data = new google.visualization.DataTable();
data.addColumn({ type: 'string', label: 'Labels' });
data.addColumn({ type: 'number', label: 'Percentage' });
data.addColumn({ type: 'number', label: 'Remaining Percentage' });

data.addColumn({ type: 'number', label: 'Total' });
data.addColumn({ type: 'string', role: 'annotation' });

data.addRows(
[
['Property Without Power', parseInt((a[1]/a[0])*100), 100-parseInt((a[1]/a[0])*100), parseInt((a[1]/a[0])*100)/2, parseInt((a[1]/a[0])*100).toString()+'%'],
['Property Without Heat', parseInt((a[2]/a[0])*100), 100-parseInt((a[2]/a[0])*100), parseInt((a[2]/a[0])*100)/2, parseInt((a[2]/a[0])*100).toString()+'%'],
['Property Without Gas', parseInt((a[3]/a[0])*100), 100-parseInt((a[3]/a[0])*100), parseInt((a[3]/a[0])*100)/2, parseInt((a[3]/a[0])*100).toString()+'%']
]);

var options = {//title:"Work Order Progress Tracking",
			   vAxis : {textColor: '#ffffff', viewWindow: { min: 0, max: 110}},
			   titleTextStyle: {color: '#000000', fontSize:20},
			   tooltip: {trigger:'none'},
			   series: [
						//{ type: 'bars',color: '#003366'  },
						{ type: 'bars',color: '#ACC5FF'  },
						{ type: 'bars',color: '#D0E8FF' , visibleInLegend:false},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'}],
				isStacked:true
						
				};

        var chart = new google.visualization.ComboChart(document.getElementById('chart_div2'));
        chart.draw(data, options);
      }
      google.setOnLoadCallback(drawVisualization2);
    </script>



    <script type="text/javascript">
      function drawVisualization4() {
        // Some raw data (not necessarily accurate)

var a = [
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0],
[0, 0, 0, 0, 0, 0, 0, 0]
];
//alert (a[0][1]-a[0][2]-a[0][3]);

var data = new google.visualization.DataTable();
data.addColumn({ type: 'string', label: 'Labels' });
data.addColumn({ type: 'number', label: 'Complete work order' });
data.addColumn({ type: 'number', label: 'Issue work order' });
data.addColumn({ type: 'number', label: 'Complete Assessment' });

data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });
data.addColumn({ type: 'number', label: 'Line Series' });
data.addColumn({ type: 'string', role: 'annotation' });

data.addRows(
[
['1-2 units', a[0][7], a[0][6], a[0][5]-a[0][6]-a[0][7], 0, null, a[0][7], null, a[0][7]+a[0][6], null, a[0][5], null, a[0][5]+a[0][1]-a[0][2]-a[0][3]+16, null],
['3-6 units', a[1][7], a[1][6], a[1][5]-a[1][6]-a[1][7], 0, null, a[1][7], null, a[1][7]+a[1][6], null, a[1][5], null, a[1][5]+a[1][1]-a[1][2]-a[1][3]+16, null],
['7-20 units', a[2][7], a[2][6], a[2][5]-a[2][6]-a[2][7], 0, null, a[2][7], null, a[2][7]+a[2][6], null, a[2][5], null, a[2][5]+a[2][1]-a[2][2]-a[2][3]+16, null],
['21-100 units', a[3][7], a[3][6], a[3][5]-a[3][6]-a[3][7], 0, null, a[3][7], null, a[3][7]+a[3][6], null, a[3][5], null, a[3][5]+a[3][1]-a[3][2]-a[3][3]+16, null],
['More than 100 units', a[4][7], a[4][6], a[4][5]-a[4][6]-a[4][7], 0, null, a[4][7], null, a[4][7]+a[4][6], null, a[4][5], null, a[4][5]+a[4][1]-a[4][2]-a[4][3]+16, null]


]);

var options = {//title:"Work Order Progress Tracking",
			   titleTextStyle: {color: '#000000', fontSize:20},
  			   vAxis : {textColor: '#ffffff'},//, viewWindow: { min: 0, max: 2000}
			   tooltip: {trigger:'none'},
			   series: [
						//{ type: 'bars',color: '#003366'  },
						{ type: 'bars',color: '#3E70D7'  },
						{ type: 'bars',color: '#ACC5FF'  },
						{ type: 'bars',color: '#D0E8FF' },
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'},
						{ type: 'line', lineWidth: 0, visibleInLegend:false, pointSize: 0, targetAxisIndex: 0,color:'#000000'}],
				isStacked:true
						
				};

        var chart = new google.visualization.ComboChart(document.getElementById('chart_div4'));
        chart.draw(data, options);
      }
      google.setOnLoadCallback(drawVisualization4);
    </script>
