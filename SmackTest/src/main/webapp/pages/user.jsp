<%@ page import="connections.Connect" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="js/myfunction.js"></script>
<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>

<html>
<head>
    <title>User page</title>
</head>
<body>
<sql:setDataSource var="dataSource" user="${connect.login}" password="${connect.password}" url="${connect.connectionString}" driver="${connect.driverName}"/>
<sql:query var="result" dataSource="${dataSource}">
    select *
    from user
    where login="${login}";
</sql:query>
<sql:query var="result2" dataSource="${dataSource}">
    select *
    from user
    where login!="${login}";
</sql:query>
hello ${login}
<form method="get" action="<%=request.getContextPath().concat("/user")%>">
    <input type="hidden" name="user" value="${user}">
    <input type="submit" value="exit"/>
</form>
</br>
your table:
</br>
<table width="100%" border="2">
    <c:forEach items="${result.rows}" var="note">
        <tr>
            <td> action:<c:out value="${note.action}"/> </td>
            <td> time:<c:out value="${note.time}"/> </td>
        </tr>
    </c:forEach>
</table>
<form method="post" action="<%=request.getContextPath().concat("/user")%>">
    Action:<input type="text" name="action"/>
    Time:<input type="time" name="time"/>
    <input type="hidden" name="login" value="${login}">
    <input type="submit" value="add action"/>
</form>
all users:
<br>
<table width="100%" border="2">
    <c:forEach items="${result2.rows}" var="note2">
        <tr>
            <td> user:<c:out value="${note2.login}"/> </td>
            <td> time:<c:out value="${note2.time}"/> </td>
        </tr>
    </c:forEach>
</table>
<form method="post" action="<%=request.getContextPath().concat("/user")%>">
    Receiver:<input type="text" name="receiver"/>
    Message:<input type="text" name="msg"/>
    Time:<input type="time" name="time"/>
    <input type="hidden" name="login" value="${login}">
    <input type="submit" value="Send"/>
</form>
    ${msg}
</body>
</html>
