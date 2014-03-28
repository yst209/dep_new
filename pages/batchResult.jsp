<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="ChartDirector.*" %>
<%@page import="dep.web.TrendController" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core">
      
<style>
.tooltip 
{
    visibility: hidden; 
    position: absolute; 
    top: 50px;  
    left: 920px; 
    z-index: 2; 
    background-color: #689CDA;
    opacity:0.9;filter:alpha(opacity=90);
	border: solid 1px #2B67AF;
    color:#FFFFFF;
    font: normal 8pt arial; 
    padding: 10px; 
    text-align: left;
}
</style>
<script>
</script>
<body>
	<c:choose>
		<c:when test="${batchInfo.updateTotalsOnly}">
			<b>Update totals has been run successfully.</b><br/>
		</c:when>
		<c:when test="${fn:length(batchInfo.validatedProjectList)>0}">
		    <table>
		      <TH>Processed Projects:</th>
		      <c:forEach items="${batchInfo.validatedProjectList}" var="project">
		        <tr>
		          <td><c:out value="${project}" /><td>
		        </tr>
		      </c:forEach>
		    </table>
		</c:when>
		<c:otherwise>
			<b>No projects have been processed successfully.</b><br/>
		</c:otherwise>
    </c:choose>
    
	<br/>
	
	<c:choose>
		<c:when test="${fn:length(batchInfo.errorProjectList)>0}">
		    <table>
		      <th align="left">Error occurred in CSV files:</th>
		      <c:forEach items="${batchInfo.errorProjectList}" var="project">
		        <tr>
		          <td><c:out value="${project}" /><td>
		        </tr>
		      </c:forEach>
		    </table>
	    </c:when>
		<c:otherwise>
		</c:otherwise>
    </c:choose>
  		    
  	<br/>
    <a href="/dep/batch.html">Back</a><br/>
	
 
 </body>
</html>