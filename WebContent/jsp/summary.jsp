<%@page import="classes.Tweet"%>
<%@page import="classes.Instances"%>
<%@page import="webapp.Main"%>
<%@page import="resources.lda.*"%>
<%@page import="java.text.DecimalFormat" %>
<%@page import="java.util.ArrayList" %>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="/TwitterSummarizer/css/tableless.css"/>
	<title>Top Trending Stories</title>
	<%
			Main main = (Main) pageContext.findAttribute("it");
			ArrayList<Instances> instances =	main.getTopTweets();
	%>
</head>
<body>
<div id="mainContainer">
	<div id="header">
	Twitter Topic Generator Web App - Summary
	</div>
	<div id="content">
		<%
			for(Instances i: instances){
				String label = i.classLabel;
				if (main.isInt(label)){
					label = "Top Tweets";
				}
		%>
		<div class="classes">
		<div class="focus"><%=label %></div>
		<%	for(Tweet t : i.getTweetsAsList()){%>
			<hr class="hrRule">
			<div class="tweet"><%=t.text%></div>
		<%}%>
		</div>		
		<%
		}
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