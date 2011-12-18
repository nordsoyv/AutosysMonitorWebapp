<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<table>
<c:forEach var="system" items="${systeminfo}">
<tr>
<td>${system.name}</td>
<td>${system.url}</td>
<td>${system.ping}</td>
<td>${system.alive}</td>
</tr>
</c:forEach>
</table>

</body>
</html>
