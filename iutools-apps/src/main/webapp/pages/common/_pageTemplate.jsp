<% String IUTOOLS_JS_VERSION=(new java.util.Date()).toLocaleString(); %>

<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">  
  <% out.println("<title>iutools: "+pageTitle+"</title>\n"); %>
  <link rel="stylesheet" href="css/styles.css?v2">
  <link rel="stylesheet" href="css/design-styles.css">
  <link rel="preconnect" href="https://fonts.gstatic.com">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,300;0,400;0,500;0,600;1,300;1,400;1,500;1,600&display=swap" rel="stylesheet">
  <script src="js/vendors/jquery/jquery-3.3.1.min.js"></script>
</head>

<body>

<div id="header" class="header">
  <div id="header_inner">
    <a href="index.html">Computer Resources for Inuktut</a> 
  </div>
</div>

<div id="top_links">
  <div id="top_links_inner">
    <div id="feedback_link"><a href="mailto:alain.desilets@nrc-cnrc.gc.ca;contact@inuktitutcomputing.ca?subject=Inuktitut Tools Feedback">Send Feedback</a></div>
    <div id="other_tools"><a href="index.html">Other Inuktut Tools</a></div>
  </div>
</div>

<div id="page_title">
<h1><%= pageTitle %></h1>
<p><em><%= pageUsage %></em></p>
</div>
<!--
   Setup the "main" part for this type of page
-->
<main>
<% pageContext.include("pages/" + pageName + "/_view.jsp"); %>
</main>

<div id="footer" class="footer">
  Copyright, National Research Council of Canada, 2017
</div>
<div id="sponsors">
  <a href="https://nrc.canada.ca/en" target="_blank"><img src="imgs/NRC-ID_138x138.jpg" alt="logo of National Research Council Canada - Conseil national de recherches Canada"></a>
  <a href="https://www.pirurvik.ca/" target="_blank"><img src="imgs/Pirurvik_logo_2.jpg" alt="logo of pirurvik"></a>
</div>

<!--
   Load scripts that are common to all page types
-->
<script src="js/vendors/log4javascript.js"></script>
<script src="js/debug/Debug.js?version=<%= IUTOOLS_JS_VERSION %>"></script>
<script src="js/debug/DebugConfig.js?version=<%= IUTOOLS_JS_VERSION %>"></script>
<script src="js/controllers/RunWhen.js?version=<%= IUTOOLS_JS_VERSION %>"></script>
<script src="js/controllers/WidgetController.js?version=<%= IUTOOLS_JS_VERSION %>"></script>
<script src="js/controllers/IUToolsController.js?version=<%= IUTOOLS_JS_VERSION %>"></script>
<script src="js/controllers/gist/WordGistController.js?version=<%= IUTOOLS_JS_VERSION %>"></script>

<!-- Include the code that creates and configures the controller for this page -->
<% pageContext.include("pages/" + pageName + "/_controller.jsp"); %>

</body>

</html>