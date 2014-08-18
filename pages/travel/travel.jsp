<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<head>
	<title>Search Flights</title>
	<link rel="stylesheet" href="/dep/pages/resource/bootstrap.min.css">
	<link rel="stylesheet" href="/dep/pages/resource/datepicker.css">

</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<script src="/dep/pages/resource/bootstrap.min.js"></script>
<script src="/dep/pages/resource/bootstrap-datepicker.js"></script>
<body>
<center>
<style>
span label {
    width: 100px;
    font-weight: normal !important;
}
</style>
<h2>Search Flights</h2><br/>

<p>
<!-- note second way of displaying error messages – by field -->
<form:form id="form" commandName="travelInfo" method="POST" action="/dep/travel">
	<table>
	<tr>
		<td>
		    Dates:
		</td>
		<td>
		    From: <form:input path="fromDate"  class="span2" id="dpd1"/>
            To: <form:input path="toDate"  class="span2" id="dpd2"/>
		</td>
	</tr>		
	<!-- <tr>
		<td>
		    Flight Type:
		</td>
		<td>
		    <form:radiobuttons path="flightType" items="${flightTypeSelectList}"/>
		</td>
	</tr>	 -->
	<tr>
		<td>
		    From:
		</td>
		<td>
		    <form:select path="from" class="form-control">
		    <form:options items="${fromSelectList}" />
		    </form:select>
		</td>
	</tr>
	<tr>
		<td>
		     To:
		</td>
		<td>
		     <form:select path="to" class="form-control">
		     <form:options items="${toSelectList}" />
		     </form:select>
		</td>
	</tr>
	<tr>
		<td>
		    Class:
		</td>
		<td>
		    <form:select path="classType" class="form-control">
		    <form:options items="${classSelectList}" />
		    </form:select>
		</td>
	</tr>
	<tr>
		<td>
		     Adults:
		</td>
		<td>
		     <form:select path="adults" class="form-control">
		     <form:options items="${adultsSelectList}" />
		     </form:select>
		</td>
	</tr>
	<tr>
		<td>
		    Children:
		</td>
		<td>
		    <form:select path="children" class="form-control">
		    <form:options items="${childrenSelectList}" />
		    </form:select>
		</td>
	</tr>                                                
	<tr>
		<td>
		<div style="width:120px;"/>
		</td>
		<td><br/><br/>
			<div style="margin-left:100px;height:100px;width:200px;">
	            <button type="submit" class="btn btn-primary" data-loading-text="Processing...">Search All Sites</button>
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

$('[type=submit]').on('click', function() {
    var $this = $(this).button('loading');
    //setTimeout(function() {
    //    $this.button('reset');
    //}, 2000);
});
</script> 
</p>
</center>
</body>
</html>
