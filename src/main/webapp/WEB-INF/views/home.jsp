<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>AutosysMonitor</title>
<link rel="stylesheet" type="text/css" href="resources/css/main.css" />

<script type="text/javascript" src="resources/js/jquery-1.7.1.min.js"></script>

<script type="text/javascript">
	ASM = {};

	$(document).ready(function() {
		ASM.getSystems();

		$("#refreshButton").click(function() {
			ASM.refreshTable();
		});

		$("#setIntervalButton").click(function() {
			ASM.setInterval();
		});

		$("#setActiveCheckbox").click(function() {
			ASM.setRefreshActive();
		});

		//kaller denne her for å sette intervall ved oppstart
		ASM.setInterval();
		ASM.isRefreshing = false;
	});
</script>
<script type="text/javascript" src="resources/js/main.js"></script>
</head>
<body>
	<h1>AutosysMonitor</h1>
	<p>Systemer som sjekkes:</p>
	<div id="systemDisplay" class="display"></div>
	<button id="refreshButton">Refresh alle</button>
	<br /> Refresh intervall:
	<input type="number" id="refeshInterval" value="5000" />
	<button id="setIntervalButton">Sett intervall</button>
	Aktive:
	<input type="checkbox" id="setActiveCheckbox" checked="checked">
</body>
</html>
