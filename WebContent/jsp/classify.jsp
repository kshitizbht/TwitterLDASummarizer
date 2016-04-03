<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet" href="/TwitterSummarizer/css/tableless.css"/>
	<title>Classifying stories</title>
	<%
			String result = (String) pageContext.findAttribute("it");
	%>
</head>
<body>
<div id="mainContainer">
	<div id="header">
	Twitter Topic Generator Web App - Classifying
	</div>
	<div id="content">
			<%
			String[] res = result.split("::");
			out.println("<p>Original           ::"+res[0] +" </p>");
			out.println("<p>Lda Classlabelling ::"+res[1] +" </p>");
			out.println("<p>"+res[2]+" </p>");
			%>
	</div>
	<div id="cleardiv">
	</div>
	<div id="footer">
	~~Twitter Summarizer CS 750 Term Project by kb ~~
	</div>	
</div>
</body>
</html>