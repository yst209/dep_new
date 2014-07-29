<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<head>
	<title>Schedule Control Group Log</title>
	<link rel="stylesheet" href="/dep/pages/resource/bootstrap.min.css">
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<script src="/dep/pages/resource/bootstrap.min.js"></script>
<body>
<center>

	<h2>Schedule Control Group Log</h2>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="SCGLogInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
		<h3>Report Mode</h3>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="SCGLogInfo" method="POST" action="/dep/scg_log/scg_submit">
			<table>
				<tr>
					<td>
						<div class="col-sm-2">Month:</div>
					</td>
					<td>
						<form:select path="reportMonth" class="form-control">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${dataPeriodSelectList}" />
						</form:select>
					</td>
				</tr>
			</table><br/>

			<button type="submit" class="btn btn-primary">Download Report</button>
		</form:form>
	</p>
	<br/><br/><div><hr style="border:1px solid #C0C0C0;"/></div><br/>
	<p>
		<h3>Edit Mode</h3>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="SCGLogInfo"  method="POST" action="/dep/scg_log/scg_edit">
			<table>
				<tr>
					<td>
						<div class="col-sm-2">Month:</div>
					</td>
					<td>
						<form:select path="editMonth" class="form-control">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${dataPeriodSelectList}" />
						</form:select>
					</td>
				</tr>
			</table><br/>

			<button type="submit" class="btn btn-primary">Edit Report</button>
		</form:form>
	</p>
	
</center>
</body>
</html>