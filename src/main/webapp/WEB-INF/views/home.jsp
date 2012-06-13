<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>AutosysMonitor</title>
<link rel="stylesheet" type="text/css"
	href="/autosysmonitor/resources/css/main.css" />


<script type="text/javascript"	src="/autosysmonitor/resources/js/json2.js"></script>
<script type="text/javascript"	src="/autosysmonitor/resources/js/jquery-1.7.2.js"></script>
<script type="text/javascript"  src="/autosysmonitor/resources/js/griddisplay.js"></script>
<script type="text/javascript"  src="/autosysmonitor/resources/js/systems.js"></script>
<script type="text/javascript"	src="/autosysmonitor/resources/js/main.js"></script>
<script type="text/javascript">
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

</head>
<body>
	<h1>AutosysMonitor</h1>
	<br /> Refresh intervall:
	<input type="number" id="refeshInterval" value="60" step="1" min="1" style="text-align: right" />
	<button id="setIntervalButton">Sett intervall</button>
	Aktiv:
	<input type="checkbox" id="setActiveCheckbox" checked="checked">
	<p>Systemer som sjekkes:</p>
	<!-- div id="systemDisplay" class="display"></div-->
	<div id="systemDisplay2" class="displayGrid"></div>

	<!-- button id="refreshButton">Refresh alle</button-->
</body>
</html>
