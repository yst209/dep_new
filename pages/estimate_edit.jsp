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
	<h2>Update Cost Estimate Report</h2>
	<p>
		<form:form method="post" action="estimate_save.html" modelAttribute="estimateForm">
		    <table>
		    <tr>
		    	<th>No.</th>
		        <th>Project ID</th>
		        <th>Current Milestone</th>
		        <th>Status</th>
		        <th>Date Received</th>
		        <th>Date Completed</th>
		        <th>Budget</th>
		        <th width="30">Latest Submitted Estimate</th>
		        <th>Reconciled</th>
		        <th>Note</th>

		    </tr>
		    <c:forEach items="${estimateForm.projects}" var="project" varStatus="status">
		        <tr>
		            <td align="center">${status.count}</td>
		            <td>${project.projectId} <input type="hidden" name="projects[${status.index}].projectId" value="${project.projectId}"/></td>
		            <td>${project.milestone} <input type="hidden" name="projects[${status.index}].milestone" value="${project.milestone}"/></td>
		            <td><input name="projects[${status.index}].status" value="${project.status}" size="8"/></td>
		            <td><input name="projects[${status.index}].dateReceived" value="${project.dateReceived}" size="10"/></td>
		            <td><input name="projects[${status.index}].dateCompleted" value="${project.dateCompleted}" size="10"/></td>
		            <td><input name="projects[${status.index}].budget" value="${project.budget}" size="10"/></td>
		            <td><input name="projects[${status.index}].latestSubmittedEstimate" value="${project.latestSubmittedEstimate}" size="10"/></td>
		            <td><input name="projects[${status.index}].reconciled" value="${project.reconciled}" size="3"/></td>
		            <td><input name="projects[${status.index}].note" value="${project.note}" size="60"/></td>
		            <td><input type="hidden" name="projects[${status.index}].dataPeriod" value="${project.dataPeriod}" size="10"/></td>
		            <td><font color="red">
		            		<form:errors path="budgetArray[${status.index}]"/><form:errors path="latestSubmittedEstimateArray[${status.index}]"/>
		            		<form:errors path="dateReceivedArray[${status.index}]"/><form:errors path="dateCompletedArray[${status.index}]"/>
		            	</font>
		            </td>


		        </tr>
		    </c:forEach>
		</table>
		<br/>
		<input type="submit" value="Save" />
		 
		</form:form>
	</p>
</center>
</body>
</html>