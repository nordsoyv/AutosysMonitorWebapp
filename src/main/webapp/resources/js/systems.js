var ASM;
if (!ASM) {
	ASM = {};
}

ASM.JdbcSystem = function(system) {
	var name;
	var url;
	var alive;
	var ping;
	var timeout;
	var data = {};
	var type;

	var toDTO = function() {
		var dto = {};
		dto.name = name;
		dto.url = url;
		dto.alive = alive;
		dto.ping = ping;
		dto.timeout = timeout;
		dto.data = {};
		dto.type = type;
		return dto;
	};

	var setState = function(system) {
		name = system.name;
		url = system.url;
		alive = system.alive;
		ping = system.ping;
		timeout = system.timeout;
		data = system.data;
		type = system.type;
	};

	// will alter the display to indicate that an update is running
	var startUpdate = function() {
	};

	var stopUpdate = function() {
	};

	// get updated data from server
	this.update = function(callback) {

		var jsonDTO = JSON.stringify(toDTO());
		startUpdate();
		var request = $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonDTO,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		request.done(function(data, code, jqXHR) {
			setState(data);
			stopUpdate();
			draw();
			callback();
		});
		request.fail(function(jqHXR, textStatus) {
			alive = false;
			stopUpdate();
			draw();
			callback();
		});
	};

	// draw
	var draw = function() {
		var idString = "#" + ASM.createSystemCellId({
			name : name
		});
		var cellDiv = $(idString);
		cellDiv.empty();
		var jdbcDiv = $(document.createElement('div'));
		jdbcDiv.addClass('displayGridCell');
		if (alive) {
			jdbcDiv.addClass('isgreen');
		} else {
			jdbcDiv.addClass('isred');
		}
		jdbcDiv.text(name);
		jdbcDiv.appendTo(cellDiv);

	};

	this.draw = draw;

	setState(system);

};

ASM.JMXServer = function(system) {

	var name;
	var url;
	var alive;
	var ping;
	var timeout;
	var data = {};
	var type;
	var allServers = {};

	var toDTO = function() {
		var dto = {};
		dto.name = name;
		dto.url = url;
		dto.alive = alive;
		dto.ping = ping;
		dto.timeout = timeout;
		dto.data = {};
		dto.type = type;
		return dto;
	};

	var setState = function(system) {
		name = system.name;
		url = system.url;
		alive = system.alive;
		ping = system.ping;
		timeout = system.timeout;
		data = system.data;
		type = system.type;

		var allServersKeys = Object.keys(allServers);
		var keys = Object.keys(data);

		for ( var i = 0; i < allServersKeys.length; i++) {
			allServers[allServersKeys[i]] = "DEAD";
		}
		for ( var i = 0; i < keys.length; i++) {
			allServers[keys[i]] = data[keys[i]];
		}
	};

	// will alter the display to indicate that an update is running
	var startUpdate = function() {
	};

	var stopUpdate = function() {
	};

	// get updated data from server
	this.update = function(callback) {

		var jsonDTO = JSON.stringify(toDTO());
		startUpdate();
		var request = $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonDTO,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		request.done(function(data, code, jqXHR) {
			setState(data);
			stopUpdate();
			draw();
			callback();
		});
		request.fail(function(jqHXR, textStatus) {
			alive = false;
			stopUpdate();
			draw();
			callback();
		});
	};

	// draw
	var draw = function() {
		var idString = "#" + ASM.createSystemCellId({
			name : name
		});
		var cellDiv = $(idString);
		cellDiv.empty();
		var allKeys = Object.keys(allServers);

		for ( var i = 0; i < allKeys.length; i++) {
			var serverDiv = $(document.createElement('div'));
			serverDiv.addClass('displayGridCell');
			if (allServers[allKeys[i]] == "RUNNING") {
				serverDiv.addClass('isgreen');
			} else if (allServers[allKeys[i]] == "DEAD") {
				serverDiv.addClass('isred');
			} else if (allServers[allKeys[i]] == "STUCK") {
				serverDiv.addClass('isyellow');
			} else {
				serverDiv.addClass('isgrey');
			}
			serverDiv.text(allKeys[i]);
			serverDiv.appendTo(cellDiv);

		}

	};
	this.draw = draw;

	setState(system);

};
