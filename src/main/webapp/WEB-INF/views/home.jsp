<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>AutosysMonitor</title>
<link rel="stylesheet" type="text/css"
	href="/autosysmonitor/resources/css/main.css" />


<script type="text/javascript"	src="/autosysmonitor/resources/js/lib/json2.js"></script>
<script type="text/javascript"	src="/autosysmonitor/resources/js/lib/jquery-1.7.2.js"></script>
<script type="text/javascript"  src="/autosysmonitor/resources/js/global.js"></script>
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
	<br /> 
	<input type="hidden" id="refeshInterval" value="120" step="1" min="1" style="text-align: right"  />
	<!--  button id="setIntervalButton">Sett intervall</button-->
	<button id="refreshButton">Refresh</button>
	<label>Aktiv:<input type="checkbox" id="setActiveCheckbox" checked="checked"></label>
	<p>Systemer som sjekkes:</p>
	<!-- div id="systemDisplay" class="display"></div-->
	<div id="systemDisplay2" class="displayGrid"></div>

	
</body>
</html>
