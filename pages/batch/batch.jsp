<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<head>
	<title>Batch Process Automation Tool</title>
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<body>
<center>
<h2>Batch Process Automation Tool</h2>
<!-- one way to display error messages – globally -->
<spring:hasBindErrors name="batchInfo">
	<font color="red">
	                <c:forEach items="${errors.allErrors}" var="error">
	                                <spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
	                </c:forEach>
	</font>
</spring:hasBindErrors>
<p>
<!-- note second way of displaying error messages – by field -->
<form:form id="form" commandName="batchInfo" method="POST" action="/dep/batch/batchResult">
	<table>
	<tr>
		<td>
		    Data Period:
		</td>
		<td>
		    <form:select path="datePeriod">
		    <form:option value="0" label="--- Select ---" />
		    <form:options items="${dataPeriodSelectList}" />
		    </form:select>
		</td>
	</tr>
	<tr>
		<td>
		     PC Coordinator:
		</td>
		<td>
		     <form:select path="coordinator">
		     <form:option value="NONE" label="--- Select ---" />
		     <form:options items="${coordinatorSelectList}" />
		     </form:select>
		</td>
	</tr>
	<tr>
		<td><br/>
		     <form:checkbox path="updateTotalsOnly" /> Update Totals Only
		</td>
	</tr>                                                     
	<tr>
		<td><br/><br/>
			<div style="margin-left:140px;">
	            <input id="submit" type="submit" title="Run Update" value="Run Update"/>
	            <img id="loader" src="/dep/pages/images/loader.gif" title="loading icon" alt="loading icon"/>
			</div>                   
		</td>
	</tr>
	<tr>
		<td><br/><br/><br/><br/>
            Default Folders on lfkbedcdev01:<br/>
            \\lfkbedcdev01\PC_Coordinator\CobraCSVFiles\<br/>
            \\lfkbedcdev01\PC_Coordinator\Reports\<br/>
            \\lfkbedcdev01\BackupFolder\<br/>
		</td>
	</tr>
	</table><br/>
</form:form>
<script> 

function init(){
	$("#loader").hide();
}

$(document).ready(init);

$("#form").submit(function(event) {

    /* stop form from submitting normally */

    $("#submit").hide();
    $("#loader").show();
    
    //event.preventDefault();
    
});
</script> 

</p>
</center>
</body>
</html>
