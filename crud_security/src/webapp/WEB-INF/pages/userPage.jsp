<%@ page import="web.model.User" %><%--
  Created by IntelliJ IDEA.
  User: 7
  Date: 26.05.2020
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Current user</title>
</head>
<body>
<div align="center">
    <h2>Current user</h2>
</div>
    <table style="with: 50%">
        <tr><td>
            <a>Welcome!! You have logged in.</a></td></tr>
        <tr></tr><tr><td></td><td></td><td><a href="${pageContext.request.contextPath}/login"><b>Logout</b></a></td></tr>
    </table>
    <br>
    <div>    <!-- buttons holder -->
        <button onclick="location.href='${pageContext.request.contextPath}/admin'">  View all users  </button>
    </div>
</body>
</html>