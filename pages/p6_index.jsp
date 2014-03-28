<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<html>
<head>
	<title>Primavera Metadata</title>
</head>
<script type="text/javascript"
	src="<c:url value="/resources/jquery-1.7.js" />"></script>
<body>
<center>
	<h2>Primavera Metadata Report</h2>
	<!-- one way to display error messages – globally -->
	<p>
		<!-- note second way of displaying error messages – by field -->
		<form:form method="POST" action="p6_submit.html">

			<input type="submit" value="Download Report" title="submit" />
		</form:form>
	</p>
	<br/><br/>
</center>
</body>
</html>