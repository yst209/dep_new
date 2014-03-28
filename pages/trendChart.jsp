<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Construction Trend Chart Report</title>
</head>
<script type="text/javascript"
	src="<c:url value="/resources/jquery-1.7.js" />"></script>
<body>
<center>
	<h3>Old Construction Trend Chart Report</h3>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="trendInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
	<c:url value="http://www.google.com" var="http://www.google.com"></c:url>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="trendInfo" method="POST" action="/dep/trend/trendResult">
			Bureau:
			<form:select path="bureau">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${bureauSelectList}" />
			</form:select><br />
			Operating Bureau:
			<form:select path="operatingBureau">
				<form:option value="NONE" label="--- Select ---" />
				<form:options items="${opBureauSelectList}" />
			</form:select><br />
			Compared Month:
			<form:select path="comparedMonth">
				<form:option value="0" label="--- Select ---" />
				<form:options items="${comparedMonthSelectList}" />
			</form:select><br />
			Current Month:
			<form:select path="currentMonth">
				<form:option value="0" label="--- Select ---" />
				<form:options items="${currentMonthSelectList}" />
			</form:select><br />
			Trend Chart End Period:
			<form:select path="endPeriod">
				<form:option value="0" label="--- Select ---" />
				<form:options items="${endPeriodSelectList}" />
			</form:select><br /><br />
			
			<input type="submit" title="submit" />
		</form:form>
	</p>
</center>
</body>
</html>