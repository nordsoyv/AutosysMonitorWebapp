<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>AutosysMonitor</title>
<link rel="stylesheet" type="text/css" href="resources/css/main.css" />

<script type="text/javascript"
	src="resources/js/jquery-1.7.1.min.js"></script>

<script type="text/javascript">
	ASM = {};

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

		$("#refreshButton").click(function() {
			ASM.refreshTable();
		});
		
		ASM.drawTable();
	});
</script>
<script type="text/javascript" src="resources/js/main.js"></script>
</head>
<body>
	<h1>AutosysMonitor</h1>
	<p>Systemer som sjekkes:</p>
	<div id="systemDisplay" class="display"></div>
	<button id="refreshButton">Refresh</button><br />
	Refresh intervall:<input type="text" id="refeshInterval"  value="5000" />
</body>
</html>
