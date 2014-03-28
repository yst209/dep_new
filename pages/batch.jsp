<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Batch Process Automation Tool</title>
</head>
<script type="text/javascript"
	src="<c:url value="/resources/jquery-1.7.js" />"></script>
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
		<form:form commandName="batchInfo" method="POST" action="/dep/batch/batchResult">
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
					<td>
						<div style="margin-left:140px;margin-top:40px;margin-bottom:60px;">
							<input type="submit" title="submit" />
						</div>
					</td>
				</tr>
				<tr>
					<td>
						Default Folders on lfkbedcdev01:<br/>
						C:\PC_Coordinator\CobraCSVFiles\<br/>
						C:\PC_Coordinator\Reports\<br/>
						C:\BackupFolder\<br/>
					</td>
				</tr>
			</table><br/>


		</form:form>
	</p>
</center>
</body>
</html>