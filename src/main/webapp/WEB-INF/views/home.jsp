<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
<link rel="stylesheet" type="text/css" href="resources/css/main.css" />

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>

<script type="text/javascript">
	var ASM = {};

	$(document).ready(function() {
		ASM.systems = new Array();
		<c:forEach items="${systeminfo}" var="system">
		systemInfo = new Object();
		systemInfo.name = '${system.name}';
		systemInfo.url = '${system.url}';
		systemInfo.ping = '${system.ping}';
		systemInfo.alive = '${system.alive}';
		systemInfo.timeout = '${system.timeout}';
		ASM.systems.push(systemInfo);
		</c:forEach>

		ASM.drawTable();
	});
</script>
<script type="text/javascript" src="resources/js/main.js"></script>
</head>
<body>
	<h1>Hello world!</h1>

	<P>The time on the server is ${serverTime}.</P>
	<div id="systemDisplay" class="display"></div>

</body>
</html>
