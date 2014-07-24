<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
      xmlns:c="http://java.sun.com/jsp/jstl/core"
      xmlns:form="http://www.springframework.org/tags/form"
      xmlns:spring="http://www.springframework.org/tags"
      xmlns:ice="http://www.icesoft.com/icefaces/component" xmlns:jsp="http://java.sun.com/JSP/Page">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Project Supporting Roles<</title>
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<body>
<center>
	<h2>Update Project Supporting Roles</h2>
	<p>
	
		<form:form method="post" action="/dep/scg_log/roles_save" modelAttribute="scgForm">
			<input type="submit" value="Save Changes" /><br/><br/>
		    <table>
		    <tr>
		    	<th>No.</th>
		        <th align="left">Project ID</th>
		        <th align="left">Project Name</th>
		        <th align="left"><nobr>SCG Lead</nobr></th>
		        <th align="left">Project Controls Lead</th>
		        <th align="left">Permits Lead</th>
		        <th align="left">Substainability Manager</th>
		        <th align="left">Cost Estimating Manager</th>

		    </tr>
		    <c:forEach items="${scgForm.supportingRoleProjects}" var="project" varStatus="status">
		        <tr height="40">
		            <td align="center">${status.count}</td>
		            <td width="9%">${project.projectId} <input type="hidden" name="supportingRoleProjects[${status.index}].projectId" value="${project.projectId}"/></td>
		            <td width="30%">${project.projectName}</td>
		            <td>${project.SCGLead}</td>
		            <td><input name="supportingRoleProjects[${status.index}].projectControlsLead" value="${project.projectControlsLead}" size="16"/></td>
		            <td><input name="supportingRoleProjects[${status.index}].permitsLead" value="${project.permitsLead}" size="15"/></td>
		            <td><input name="supportingRoleProjects[${status.index}].substainabilityManager" value="${project.substainabilityManager}" size="20"/></td>
		            <td><input name="supportingRoleProjects[${status.index}].costEstimatingManager" value="${project.costEstimatingManager}" size="20"/></td>
		        </tr>
		    </c:forEach>
		</table>
		<br/>
		<input type="submit" value="Save Changes" />
		 
		</form:form>
	</p>
</center>
</body>
</html>