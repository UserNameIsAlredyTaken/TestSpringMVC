<%--
  Created by IntelliJ IDEA.
  User: danil
  Date: 09.01.2018
  Time: 21:07
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Camos</title>
</head>
<body>
<a href="index.jsp">Back to main menu</a>
<c:if test="${!empty listCamos}">
    <table class="tg">
        <tr>
            <th width="80">ID</th>
            <th width="120">Name</th>
            <th width="120">Nessesary LvL</th>
            <th width="120">Stealth degree</th>

        </tr>
        <c:forEach items="${listCamos}" var="camo">
            <tr>
                <td>${camo.id}</td>
                <td>${camo.name}</td>
                <td>${camo.stealthDegree}</td>
                <td>${camo.necessaryLvl}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>
