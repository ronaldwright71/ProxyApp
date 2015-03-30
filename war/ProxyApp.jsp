<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="ProxyApp.css">
    <title>Ron's Web App Proxy - Tools </title>
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="proxyapp/proxyapp.nocache.js"></script>
  </head>

  <body>
<form>
<input type="hidden" name="nameFieldh" value="hello"/>
</form>  
<% 	String nameFieldx = request.getParameter("nameFieldh") + "";
	if (nameFieldx.compareTo("null") == 0) nameFieldx = ""; 
	pageContext.setAttribute("nameFieldh", nameFieldx); %>
    <!-- web app will not function without JavaScript enabled -->
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>

    <h1>Web Application Tools and Proxy Service</h1>

    <table align="center">
      <tr>
        <td colspan="2" style="font-weight:bold;">Please enter your name:</td>        
      </tr>
      <tr>
        <td id="nameFieldContainer"></td>
        <td id="sendButtonContainer"></td>
      </tr>
      <tr>
      	<td id ="hostingButtonContainer"></td>
      	<td> <form action="/proxyapp/hosting/hostingServlet" method = "get">
      			<input type="submit" name="button1" value="Button 1" />
      		</form>
      	</td>
      <tr>
        <td>${fn:escapeXml(nameFieldh)}</td>
        <td id="afaIkContainer">${fn:escapeXml(afaIk)}</td>
      </tr>
      <tr>
        <td colspan="2" style="color:red;" id="errorLabelContainer"></td>
      </tr>
    </table>
  </body>
</html>
