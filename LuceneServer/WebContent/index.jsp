<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.demo.lucene.WebSearch,org.apache.lucene.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<%!String query;%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Lucene Example Page</title>
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
				<%
					java.util.Date d = new java.util.Date();
					WebSearch s = new WebSearch();
				%>
				<h3>Welcome to Car Document Search: </h3>
				<hr>
				<p>
					<small>Today's date is <%=d.toString()%>!
					</small>
				</p>
				<p>
					<b>Search our current Car Database:</b>
				</p>
				
				<form action="index.jsp" method="post">
					<div class="row">
						<div class="col-sm-9">
			    			<div class="input-group">
			      				<input class="form-control" type="text" name="query" value="" placeholder="Car Information" size=20 maxlength=20>
			      				<span class="input-group-btn">
			        				<input class="btn btn-success" type="submit">
			      				</span>
			    			</div><!-- /input-group -->
			  			</div><!-- /.col-lg-6 -->
					</div> 
				</form>
				
				<br>
			
				<ul class="bg-success" style="padding: 10px 10px 10px 30px; width: 335px;">
					<li><small>Search with all the words: <b>+Word +Word</b></small></li>
					<li><small>Search with the exact phrase: <b>"A Word"</b></small></li>
					<li><small>Search with at least one of the words: <b>This
								is a Word</b></small></li>
					<li><small>Without the word: <b>Word -NoWord</b></small></li>
					<li><small>With the approximate phrase: <b>"This are
								Words"~</b>
					</small></li>
				</ul>
			
				<%
					if (request.getParameter("query") != null
							&& request.getParameter("query").length() > 1) {
				%>
				
				<br>
				
				<div class="panel panel-success">
					<div class="panel-heading">
						<h4>
							Results for '<%=request.getParameter("query")%>':
						</h4>
					</div>
					
					<%
						query = request.getParameter("query");
						String result = s.search(query);
					%>
					<div class="panel-body">
						<p><%=result%></p>
					</div>
				</div>
				
				<%
					} else {
				%>
				<p class="alert alert-danger" style="width: 335px">
					<small>Please Enter a Search Query!</small>
				</p>
				<%
					}
				%>
			</div>
			<div class="col-sm-6">
				<br>
				<img src="img/muster.jpg" alt="ExampleCertificate">
			</div>
			
		</div>
	</div>

</body>
</html>