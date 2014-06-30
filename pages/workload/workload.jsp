<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Workload Report</title>
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<body>
<center>
	<h3>Forecast Workload Report</h3>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="workloadInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
	<c:url value="http://www.google.com" var="http://www.google.com"></c:url>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="workloadInfo" method="POST" action="/dep/workload/workloadResult">
			Project Stage:
			<form:select path="stage">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${stageSelectList}" />
			</form:select><br />

			Master Program:
			<form:select path="masterProgram">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${masterProgramSelectList}" />
			</form:select><br />

			Managed By:
			<form:select path="managedBy">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${managedBySelectList}" />
			</form:select><br />
			
			Chart End Period:
			<form:select path="endPeriod">
				<form:option value="0" label="--- Select ---" />
				<form:options items="${endPeriodSelectList}" />
			</form:select><br />
			
			<br />

			<input type="submit" title="submit" />
		</form:form>
	</p>
</center>
</body>
</html>