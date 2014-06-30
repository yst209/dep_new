<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Schedule Control Group Log</title>
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
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
						Month:
					</td>
					<td>
						<form:select path="reportMonth">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${dataPeriodSelectList}" />
						</form:select>
					</td>
				</tr>
			</table><br/>

			<input type="submit" value="Download Report" title="submit" />
		</form:form>
	</p>
	<br/><br/><hr/><br/>
	<p>
		<h3>Edit Mode</h3>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="SCGLogInfo"  method="POST" action="/dep/scg_log/scg_edit">
			<table>
				<tr>
					<td>
						Month:
					</td>
					<td>
						<form:select path="editMonth">
						<form:option value="0" label="--- Select ---" />
						<form:options items="${dataPeriodSelectList}" />
						</form:select>
					</td>
				</tr>
			</table><br/>

			<input type="submit" value="Edit Report" title="submit" />
		</form:form>
	</p>
	
</center>
</body>
</html>