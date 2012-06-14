var ASM;
if (!ASM) {
	ASM = {};
}

ASM.init = function() {
	ASM.display = new ASM.GridDisplay();
}();

ASM.refreshTable = function() {
	if (ASM.isRefreshing) {
		return;
	}
	ASM.isRefreshing = true;
	ASM.refreshSystem(0);
};

ASM.refreshSystem = function(id) {
	if (id >= ASM.systems.length) {
		ASM.isRefreshing = false;
		return;
	}
	ASM.systems[id].update(function() {
		ASM.refreshSystem(id + 1);
	});

};

ASM.getSystems = function() {
	var req = $.ajax({
		url : "/autosysmonitor/allSystems",
		type : "GET",
		contentType : "application/json; charset=utf-8"
	});
	req.done(function(data, code, jqXHR) {
		ASM.setAllSystems(data);
	});
	req.fail(function(jqXHR, textStatus) {
		alert("Kunne ikke lese inn systemliste fra server");
	});
};

ASM.setAllSystems = function(data) {
	ASM.display.generateScaffold(data);
	ASM.systems = [];
	for ( var i = 0; i < data.length; i++) {
		if (ASM.isHeader(data[i])) {
			continue;
		}
		if (ASM.isJmxServer(data[i])) {
			ASM.systems.push(ASM.jmxServer(data[i]));
		}
		if (ASM.isJdbc(data[i])) {
			ASM.systems.push(ASM.jdbcSystem(data[i]));
		}
		if (ASM.isHttpGet(data[i])) {
			ASM.systems.push(ASM.httpGetSystem(data[i]));
		}

	}
};

ASM.setInterval = function() {
	if (ASM.intervalHandle !== undefined || ASM.intervalHandle !== null) {
		clearInterval(ASM.intervalHandle);
	}
	var interval = $("#refeshInterval");
	ASM.refreshInterval = parseInt(interval.val()) * 1000;
	ASM.intervalHandle = setInterval(function() {
		ASM.refreshTable();
	}, ASM.refreshInterval);
	var check = $('#setActiveCheckbox');
	check.prop("checked", true);
};

ASM.setRefreshActive = function() {
	if ($('#setActiveCheckbox:checked').val() !== undefined) {
		// is checked
		ASM.setInterval();
		ASM.refreshTable();

	} else {
		if (ASM.intervalHandle !== undefined || ASM.intervalHandle !== null) {
			clearInterval(ASM.intervalHandle);
		}
	}
};

ASM.isHttpGet = function(system) {
	if (system.type == "HTTP-GET") {
		return true;
	}
	return false;
};

ASM.isJdbc = function(system) {
	if (system.type == "JDBC") {
		return true;
	}
	return false;
};

ASM.isJmxServer = function(system) {
	if (system.type == "JMXSERVER") {
		return true;
	}
	return false;
};

ASM.isJmxApp = function(system) {
	if (system.type == "JMXAPPS") {
		return true;
	}
	return false;
};

ASM.isHeader = function(system) {
	if (system.type == "HEADER")
		return true;
	return false;
};

ASM.isSystemInfoHeadline = function(id) {
	if (ASM.systems[id].type == "HEADER") {
		return true;
	} else {
		return false;
	}
};

ASM.createSystemCellId = function(system) {
	return "systemCell" + system.name;
};
