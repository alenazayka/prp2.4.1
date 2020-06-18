<%--
  Created by IntelliJ IDEA.
  User: 7
  Date: 26.05.2020
  Time: 19:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<form method="post" action="${pageContext.request.contextPath}/login">
    <input type="text" name="login"/>
    <input type="password" name="password"/>
    <input type="submit" name="submit">
</form>
</body>
</html>