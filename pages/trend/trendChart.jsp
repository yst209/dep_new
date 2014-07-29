<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" /><!-- IE8 only -->
<head>
	<title>Construction Trend Chart Report</title>
	<link rel="stylesheet" href="/dep/pages/resource/bootstrap.min.css">
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<script src="/dep/pages/resource/bootstrap.min.js"></script>
<body>
<center>
	<h2>Construction Trend Chart Report</h2>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="trendInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
		<c:url value="/dep/trend/trendResult" var="theAction" />
	
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="trendInfo" method="POST" action="/dep/trend/trendResult">
			<table>
			<tr>
				<td>
				    <div class="col-sm-2"><nobr>Bureau:</nobr></div>
				</td>
				<td>
					<form:select path="bureau" class="form-control">
						<form:option value="NONE" label="--- Select ---" class="form-control"/>
						<form:options items="${bureauSelectList}" />
					</form:select>				
				</td>
			</tr>
			<tr>
				<td>
				     <div class="col-sm-2"><nobr>Operating Bureau:</nobr></div>
				</td>
				<td>
				    <form:select path="operatingBureau" class="form-control">
						<form:option value="NONE" label="--- Select ---" />
						<form:options items="${opBureauSelectList}" />
					</form:select>
				</td>
			</tr>
			<tr>
				<td>
				     <div class="col-sm-2"><nobr>Compared Month:</nobr></div>
				</td>
				<td>
				     <form:select path="comparedMonth" class="form-control">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${comparedMonthSelectList}" />
					</form:select>
				</td>
			</tr>                                                
			<tr>
				<td>
				     <div class="col-sm-2"><nobr>Current Month:</nobr></div>
				</td>
				<td>
				    <form:select path="currentMonth" class="form-control">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${currentMonthSelectList}" />
					</form:select>
				</td>
			</tr>                                                
			<tr>
				<td>
				     <div class="col-sm-2"><nobr>Trend Chart End Period:</nobr></div>
				</td>
				<td>
				    <form:select path="endPeriod" class="form-control">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${endPeriodSelectList}" />
					</form:select>
				</td>
			</tr>                                                
			<tr>
				<td><br/><br/>
					<div style="margin-left:150px;">
						<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</td>
			</tr>
			</table>
		</form:form>
	</p>
</center>
</body>
</html>