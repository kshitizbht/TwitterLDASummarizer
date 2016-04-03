<%@page import="java.util.HashMap"%>
<%@page import="webapp.Main"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<link type="text/css" rel="stylesheet" href="/TwitterSummarizer/css/tableless.css"/>
	<title>Classes Division</title>
	<%
			Main main = (Main) pageContext.findAttribute("it");
			HashMap<String,Integer> hist = main.getHistogram();

	%>
	<script type="text/javascript">
	//Histogram Generator from http://www.xul.fr/en/css/histogram.php
	function makeGraph()
	{
	    var container = document.getElementById("graph");
	    var labels = document.getElementById("labels");
	    var dnl = container.getElementsByTagName("li");
		var lft = 350;
		var max = 0;
	    for(var i = 0; i < dnl.length; i++)
	    {
	        var item = dnl.item(i);
	        var value = item.innerHTML;
	        var content = value.split(":");
	        value = parseInt(content[0])/10 ;
			if(parseInt(max) < value)
				max = value;
	        item.style.top=(380 - value) + "px";
	        item.style.left = (i * 80 + 20 + lft) + "px";
	        item.style.height = value + "px";
	        item.innerHTML = content[0];
	        item.style.visibility="visible";	
	        //left = (i * 50 + 58 +lft) + "px";
			left = item.style.left;
	        labels.innerHTML = labels.innerHTML + 
	           "<span style='position:absolute;left:"+ 
	           left+"'>" + content[1] + "</span>";
	    }
		max = parseInt(max) + 190;
		container.style.height = max+ "px";
	}
	function setTrainingSizeToZero(){
		var txtField = document.lda.trainingSize
		txtField.value = 0;
	}
	
	function submitForm(){
		var progressbar = document.getElementById("progressBar");
		progressbar.innerHTML = "<h2 class='message'>Processing Request...</h2><p>It might take a while for LDA to process all the data</p>";
		return true;
	}
	</script>
</head>
<body onload="makeGraph()">
<div id="mainContainer">
	<div id="header">
	Twitter Topic Generator Web Application
	</div>
	<div id="content">
	<h1>Class Distribution</h1>
	<div id="graph">
		Frequency
		<ul>
		<%	
		for(String key: hist.keySet())
			out.println("<li>"+hist.get(key)+":" + key+"</li>");
		%>			
		</ul>
	</div>
	<div id="labels">
		
	</div>
	<br/>
	<br/>
	<div id="tweetInfo">
		<p>Total number of tweets before preprocessing ::<%=main.getPreprocessor().getTotalNumOfTweets()%> </p>
		<p>Total number of tweets after preprocessing ::<%=main.getPreprocessor().getFinalNumOfTweets()%></p>
		<p>Vocabulary Size ::<%=main.getVocab().getWord2id().size()%></p>
	</div>
		<form name="lda" action="/TwitterSummarizer/rest/webapp/processlda" method="post">
		<span id="trainingSize">Sampling Size<input type="text" name="trainingSize" value="500"> 
		<input type="submit" class="button" onclick = "setTrainingSizeToZero()" value = "Use All Data"/>
		 </span>
		<br/>
		<p>Leave 0 to use full set(default is 500)</p>
		<input type="submit" class="button" value = "Process LDA" onclick="return submitForm()"/>
	</form>
	</div>
	<div id="progressBar">
	
	</div>
	<div id="footer">
		~~Twitter Summarizer CS 750 Term Project by kb ~~
	</div>
</div>



</body>
</html>