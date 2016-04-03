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
	<title>Lda Result</title>
	<%
			Main main = (Main) pageContext.findAttribute("it");
			Statistics stats = main.getLda().getTheTheta().getStats();
			String[] topics = main.getLda().getTheTheta().getTopics();
			DecimalFormat format = new DecimalFormat("##.##");
			 
	%>
	<script type="text/javascript">
	function webcall(){
		var dom = document.getElementById("processing");
		dom.innerHTML = "<p class=\"message\">LDA is running in the server to generate top stories page, once done, you will be redirected to it</p>	<p>If any of the classes is null, you will be redirected to first page and asked for bigger sample</p>";
		document.topstories.submit();
	}
	</script>
</head>
<body>
<div id="mainContainer">
	<div id="header">
	Twitter Topic Generator Web Application
	</div>
	<div id="content">
		<p>Lda Perfomance on provided dataset</p>
		<div id="confusionMatrix">
			<p class="center">Confusion Matrix</p>
			<table class="metricstable">
				<tr>
				<td></td>
				<%
				for(String topic: topics)
					out.println("<td>" + topic + "</td>");
				%>
				</tr>
				<%
				for(int index=0; index<topics.length; index++){
					out.println("<tr>");
					out.println("<td>" +topics[index]+"<td>");
					int[] row = stats.getRow(index);
					for (Integer i: row)
						out.println("<td>"+ i +"</td>");
					out.println("</tr>");
				}%>
			</table>
		</div>
		<div id="metrics">
			<p class="center">Metrics Table</p>
			<table class="metricstable">
				<tr>
				<td>Metric</td>
				<%
				for(String topic: topics)
					out.println("<td>" + topic + "</td>");
				%>
				</tr>
				<tr>
				<td>Accuracy</td>
				<%
					for(Stat s: stats.stats)
						out.println("<td>"+format.format(s.getAccuracy())+"</td>");
				%>
				</tr>
				<tr>
				<td>Precision</td>
				<%
					for(Stat s: stats.stats)
						out.println("<td>"+format.format(s.getPrecision())+"</td>");
				%>
				</tr>
				<tr>
				<td>Recall</td>
				<%
					for(Stat s: stats.stats)
						out.println("<td>"+format.format(s.getRecall())+"</td>");
				%>
				</tr>
				<tr>
				<td>F-measure</td>
				<%
					for(Stat s: stats.stats)
						out.println("<td>"+format.format(s.getFmeasure())+"</td>");
				%>
				</tr>				
			</table>
		</div>
		<br/>
		<div id="cleardiv">
		<br/>
		</div>
		<div id="labels">
			<span id="news" class="label">News</span> 
			<span id="sports" class="label">Sports</span> 
			<span id="finance" class="label">Finance</span> 
			<span id="entertainment" class="label">Entertainment</span> 
			<span id="tech" class="label">Technology</span> 
			<span id="personal" class="label">Personal</span> 
		</div>
		
		<br/>
			<div id="tweetHeader">
			<span >Lda analysed tweet</span>
			<span class="misHeader">Original</span>
			<span class="misHeader">Classified</span>
			</div>
			
		<div id="sampleMisclassified">
			<%
			ArrayList<String> misclassified = main.getLda().getWordtopic().sampleMisclassified();
			for(String tweet: misclassified)
				out.println(tweet);
			%>
		</div>
		<p>
		<a href="http://localhost/TwitterSummarizer/rest/webapp/allMisclassified">Show all misclassified</a>
		</p>
	</div>
	<br/>
	<div id="classifyTweet">
		<form name="classifyTweet" action="/TwitterSummarizer/rest/webapp/classify" method="post">
		<input type="submit" class="button" value="Classify a tweet"/>
		<input type="text" value="enter a sample tweet" name="tweet" size="50"/>
		</form>
	</div>
	<br/>
	<div id="processTopStories">
		<form name="topstories" action="/TwitterSummarizer/rest/webapp/mainsummary" method="post">
		<input type="button" class="button" value="Begin Processing Summaries" onclick="webcall()"/>
		<input type="text" value="0" name="numStories" size="5"/>
		<div id="processing"></div>
		</form>
	</div>
	<div id="footer">
		~~Twitter Summarizer CS 750 Term Project by kb ~~
	</div>
</div>



</body>
</html>