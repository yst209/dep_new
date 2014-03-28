<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Staffload Report</title>
	<script src="jsp/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">

	function doAjaxPost() {
	// get the form values
		
		var portfolioManager = $("#portfolioManager").val();


		if(portfolioManager!="NONE"){
			alert("something");
		}
		else{
			alert("NONE");
			var obj = jQuery("#accountableManager");
			removeAllOptions(obj);
		}
		
		function removeAllOptions(selectbox){
			alert(selectbox);
		    var i;
		    for(i=selectbox.options.length-1;i>=0;i--)
		    {
		        selectbox.remove(i);
		        alert("removeAllOptions");
		    }
		}
		
		//refresh value         
		$("#portfolioManager").selectmenu("refresh");

		//refresh and force rebuild
		$("#accountableManager").selectmenu("refresh", true);

	}
</script>
</head>

<body>
<center>
	<h3>Staffload Report</h3>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="staffloadInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
	<c:url value="http://www.google.com" var="http://www.google.com"></c:url>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="staffloadInfo" method="POST" action="staffloadResult.html">

			Master Program:
			<form:select path="masterProgram">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${masterProgramSelectList}" />
			</form:select><br />
			
			Portfolio Manager:
			<form:select path="portfolioManager">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${PMSelectList}" />
			</form:select><br />
			
			Accountable Manager:
			<form:select path="accountableManager">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${AMSelectList}" />
			</form:select><br />

			Chart Scale:
			<form:select path="chartScale">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${chartScaleSelectList}" />
			</form:select><br />
			
			End Fiscal Year:
			<form:select path="endFiscalYear">
				<form:option value="0" label="--- Select ---" />
				<form:options items="${endFiscalYearSelectList}" />
			</form:select><br />
			
			<br />
<input type="button" value="test" onclick="doAjaxPost()">
<div id="info" style="color: green;"/>
			<input type="submit" title="submit" />
		</form:form>
	</p>
</center>
</body>
</html>