<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Main page</title>
</head>
<body>
<form method="get" action="<%=request.getContextPath().concat("/user")%>">
    login:<input type="text" name="login"/>
    </br>
    password:<input type="text" name="password"/>
    <input type="submit" value="Apply"/>
</form>
</body>
</html>