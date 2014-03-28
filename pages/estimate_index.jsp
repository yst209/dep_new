<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Cost Estimate Report</title>
</head>
<script type="text/javascript"
	src="<c:url value="/resources/jquery-1.7.js" />"></script>
<body>
<center>
	<h2>Cost Estimate Report</h2>
	<!-- one way to display error messages – globally -->
	<spring:hasBindErrors name="EstimateInfo">
		<font color="red">
			<c:forEach items="${errors.allErrors}" var="error">
				<spring:message code="${error.code}" text="${error.defaultMessage}"/><br/>
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	<p>
		<h3>Report Mode</h3>
		<!-- note second way of displaying error messages – by field -->
		<form:form commandName="EstimateInfo" method="POST" action="estimate_submit.html">
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
		<form:form commandName="EstimateInfo"  method="POST" action="estimate_edit.html">
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

			<input type="submit" value="Edit" title="submit" />
		</form:form>
	</p>
	
</center>
</body>
</html>