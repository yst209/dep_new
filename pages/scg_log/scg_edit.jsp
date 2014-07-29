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
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<head>
	<title>Schedule Control Group Log</title>
	<link rel="stylesheet" href="/dep/pages/resource/bootstrap.min.css">
</head>
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<script src="/dep/pages/resource/bootstrap.min.js"></script>
<body>
<center>
	<h2>Update Schedule Control Group Log</h2>
	<p>
	
		<form:form method="post" action="/dep/scg_log/scg_save" modelAttribute="scgForm">
			<button type="submit" class="btn btn-primary" data-loading-text="Saving...">Save Changes</button><br/><br/>
		    <table class="table table-striped">
		    <tr>
		    	<th>No.</th>
		        <th align="left">Project ID</th>
		        <th align="left">Project Name</th>
		        <th align="left"><nobr>Portfolio Manager</nobr></th>
		        <th align="left">Stage</th>
		        <th>SCG Lead</th>
		        <th>Claim</th>
		        <th>Comments</th>

		    </tr>
		    <c:forEach items="${scgForm.projects}" var="project" varStatus="status">
		        <tr height="40">
		            <td align="center">${status.count}</td>
		            <td width="9%">${project.projectId} <input type="hidden" name="projects[${status.index}].projectId" value="${project.projectId}"/></td>
		            <td width="30%">${project.projectName}</td>
		            <td>${project.portfolioManager}</td>
		            <td>${project.stage}</td>
		            <td><input name="projects[${status.index}].SCGLead" value="${project.SCGLead}" size="13"/></td>
		            <td><input name="projects[${status.index}].claim" value="${project.claim}" size="5"/></td>
		            <td><input name="projects[${status.index}].comments" value="${project.comments}" size="60"/></td>
		            <td><input type="hidden" name="projects[${status.index}].dataPeriod" value="${project.dataPeriod}" size="10" class="form-control"/></td>


		        </tr>
		    </c:forEach>
		</table>
		<br/>
		<button type="submit" class="btn btn-primary" data-loading-text="Saving...">Save Changes</button>
		 
		</form:form>
<script> 
$('[type=submit]').on('click', function() {
    var $this = $(this).button('loading');
});
</script> 
	</p>
</center>
</body>
</html>