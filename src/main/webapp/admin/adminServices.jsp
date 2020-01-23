<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<section>
	<h4>Login Successfully logged in!</h4>
	<h2>Admin Home</h2>
	<form action="initializeDB.html" method="post">
		<input type="submit" value="Initialize Database">
	</form>
	<ul>
	<li><a href="processInvoice" >Process Invoices</a></li>
	<li><a href="DisplayReport.html">Display Report</a></li>
	<li><a href="/adminController/logout.html">Logout</a></li>
    </ul>
</section>