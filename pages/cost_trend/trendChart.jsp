<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" /><!-- IE8 only -->
<head>
	<title>Project Cost Trend Chart Report</title>
</head>
<body>
<center>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
	<h3>Project Cost Trend Chart Report</h3>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="costTrendInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
		<c:url value="/dep/cost_trend/trendResult" var="theAction" />
	
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="costTrendInfo" method="POST" action="/dep/cost_trend/trendResult">
			Portfolio Manager:
			<form:select path="portfolioManager" onchange="setProjects()">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${PMSelectList}" />
			</form:select><br />
			Project ID:
			<form:select path="projectId">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${projectSelectList}" />
			</form:select><br />
			Contract Type:
			<form:select path="contractType">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${contractTypeSelectList}" />
			</form:select><br />
			
			<input type="submit" title="submit" />
		</form:form>
	</p>
	
<script> 

$(document).ready(init);

var selectedProject="";

function init(){
	$("#loader").hide();
    if ('onhashchange' in window) {
        // Fix "project ID list not updated" issue when back button is clicked.
        //alert(selectedPM);
     	if($('#portfolioManager').children('option:selected').val() != "NONE")
     	{
           setProjects(); 
     	}
	}

}

$("#form").submit(function(event) {

    /* stop form from submitting normally */

    $("#submit").hide();
    $("#loader").show();
    
    //event.preventDefault();
});


function errorAlert(e, jqxhr)
{
	//alert("Your request was not successful: " + e.responseText);
	alert("Your session has expired. Please refresh the page again. ");
}
	
function getProjects(_url, PM){
	$.ajax({
			 type: 'POST',
			 url: _url,
			 data : PM,
			 dataType : 'text',	//The type of data that you're expecting back from the server.
			 contentType: 'application/json', //The type of data sent to the server.
			 mimeType: 'application/json',
			 success: function(data) { 
		    	var projects = JSON.parse(data);
		    	var curOption ="<option value='NONE'>--- Select ---</option>";
				for(var i = 0; i < projects.length; i++) {//can't use forEach in IE8
					if(projects[i] == selectedProject)//Remember what was selected last time
						curOption+="<option value='" + projects[i] + "' selected>" + projects[i] + "</option>";
					else
						curOption+="<option value='" + projects[i] + "'>" + projects[i] + "</option>";
		    	}
		    	$('#projectId').html("");
		    	$(curOption).appendTo($('#projectId'));
		    	$('#projectId').show();
			 },
			 error: errorAlert
	}) // close ajax
}


function setProjects(){
var selectedPM = $('#portfolioManager').children('option:selected').val();//text()->label, val()->value
selectedProject = $('#projectId').children('option:selected').val();
$('#projectId').html(""); // reset the AMs list
getProjects('/dep/cost_trend/getProjectsByPM/' , selectedPM);
}


</script>	
	
</center>
</body>
</html>