<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<head>
	<title>Compare Airline Tickets</title>
	<link rel="stylesheet" href="/dep/pages/resource/bootstrap.min.css">

</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<script src="/dep/pages/resource/bootstrap.min.js"></script>
<body>
<center>
<h2>Compare Airline Tickets</h2><br/>

<p>
<!-- note second way of displaying error messages – by field -->



<form:form id="form" method="POST" action="/dep/travel/evaair" target="_blank">
	<table>
	<tr>
		<td>
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${pricelineLink}" class="btn btn-primary">
					Priceline
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${orbitzLink}" class="btn btn-primary" role="button">
					Orbitz
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${cheapticketsLink}" class="btn btn-primary" role="button" target="_blank">
					CheapTickets
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${vayamaLink}" class="btn btn-primary" role="button" target="_blank">
					Vayama
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${expediaLink}" class="btn btn-primary" role="button" target="_blank">
					Expedia
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${fareboomLink}" class="btn btn-primary" role="button" target="_blank">
					Fareboom
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${kayakLink}" class="btn btn-primary" role="button" target="_blank">
					Kayak
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${lastminutetravelLink}" class="btn btn-primary" role="button" target="_blank">
					Last Minute Travel
				</a>
			</div>                   
			<div style="margin-left:100px;height:50px;width:200px;">
				<a href="${onetravelLink}" class="btn btn-primary" role="button" target="_blank">
					OneTravel
				</a>
			</div>               
		</td>
		<td>
			<div style="margin-left:100px;height:50px;width:200px;">
	            <button id="eva" type="submit" class="btn btn-primary btn-lg">Eva Airline</button>
			</div>
		</td>
	</tr>		
	</table><br/><br/>
</form:form>
<script> 
$(document).ready(function() {
	$('.datepicker').datepicker();
    // disabling dates
    var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

    var checkin = $('#dpd1').datepicker({
      onRender: function(date) {
        return date.valueOf() < now.valueOf() ? 'disabled' : '';
      }
    }).on('changeDate', function(ev) {
      if (ev.date.valueOf() > checkout.date.valueOf()) {
        var newDate = new Date(ev.date)
        newDate.setDate(newDate.getDate() + 1);
        checkout.setValue(newDate);
      }
      checkin.hide();
      $('#dpd2')[0].focus();
    }).data('datepicker');
    var checkout = $('#dpd2').datepicker({
      onRender: function(date) {
        return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
      }
    }).on('changeDate', function(ev) {
      checkout.hide();
    }).data('datepicker');
	
	
  });


</script> 
</p>
</center>
</body>
</html>
