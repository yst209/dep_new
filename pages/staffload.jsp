<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Staffload Report</title>
	<script src="jsp/jquery-1.9.1.min.js"></script>
	
</head>

<body>
<center>
	<h2>Staffload Report</h2>
	<div align="left" style="margin-left:400px;">
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

<script>
function function1() {
    if(document.getElementById("projectStatus").value=="Future")
    	document.getElementById("managerDiv").style.display = 'none';
    else
    	document.getElementById("managerDiv").style.display = 'block';
}

</script>

			Project Status:
			<form:select path="projectStatus" onchange="function1()">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${projectStatusSelectList}" />
			</form:select> *If Future is selected, manager selections below will be ignored.<br />
			
			<div id="managerDiv" style="displayx:block;">
			Portfolio Manager:
			<form:select path="portfolioManager">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${PMSelectList}" />
			</form:select>
			
			OR  Accountable Manager:
			<form:select path="accountableManager">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${AMSelectList}" />
			</form:select><br />
			</div>

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

			<div style="margin-left:240px;"><input type="submit" title="submit" /></div>
		</form:form>
	</p>
	</div>
</center>
</body>
</html>