"use strict";
var ASM;
if (!ASM) {
	ASM = {};
}

ASM.baseSystem = function(system) {
	var that = {};

	that.name;
	that.url;
	that.alive;
	that.ping;
	that.timeout;
	that.data = {};
	that.type;

	that.toDTO = function() {
		var dto = {};
		dto.name = that.name;
		dto.url = that.url;
		dto.alive = that.alive;
		dto.ping = that.ping;
		dto.timeout = that.timeout;
		dto.data = {};
		dto.type = that.type;
		return dto;
	};

	that.setState = function(system) {
		that.name = system.name;
		that.url = system.url;
		that.alive = system.alive;
		that.ping = system.ping;
		that.timeout = system.timeout;
		that.data = system.data;
		that.type = system.type;
	};

	// will alter the display to indicate that an update is running
	that.startUpdate = function() {
		var idString = "#" + ASM.createSystemCellId({
			name : that.name
		});
		var cellDiv = $(idString);
		var updateDiv = $(document.createElement('div'));
		updateDiv.addClass('displayCellUpdate').appendTo(cellDiv);

	};

	that.stopUpdate = function() {
	};

	that.update = function(callback) {
	};
	that.draw = function() {
	};

	that.setState(system);

	return that;
};

ASM.httpGetSystem = function(system) {
	var that = ASM.baseSystem(system);

	// get updated data from server
	that.update = function(callback) {

		var jsonDTO = JSON.stringify(that.toDTO());
		that.startUpdate();
		var request = $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonDTO,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		request.done(function(data, code, jqXHR) {
			that.setState(data);
			that.stopUpdate();
			that.draw();
			callback();
		});
		request.fail(function(jqHXR, textStatus) {
			that.alive = false;
			that.stopUpdate();
			that.draw();
			callback();
		});
	};

	that.draw = function() {
		var idString = "#" + ASM.createSystemCellId({
			name : that.name
		});
		var cellDiv = $(idString);
		cellDiv.empty();
		var httpGetDiv = $(document.createElement('div'));
		httpGetDiv.addClass('displayGridCell');
		if (that.alive) {
			httpGetDiv.addClass('isAlive');
		} else {
			httpGetDiv.addClass('isDead');
		}
		httpGetDiv.text(that.name);
		httpGetDiv.attr('title', that.url);
		httpGetDiv.appendTo(cellDiv);

	};

	return that;

};

ASM.jdbcSystem = function(system) {
	var that = ASM.baseSystem(system);

	// get updated data from server
	that.update = function(callback) {

		var jsonDTO = JSON.stringify(that.toDTO());
		that.startUpdate();
		var request = $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonDTO,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		request.done(function(data, code, jqXHR) {
			that.setState(data);
			that.stopUpdate();
			that.draw();
			callback();
		});
		request.fail(function(jqHXR, textStatus) {
			that.alive = false;
			that.stopUpdate();
			that.draw();
			callback();
		});
	};

	// draw
	that.draw = function() {
		var idString = "#" + ASM.createSystemCellId({
			name : that.name
		});
		var cellDiv = $(idString);
		cellDiv.empty();
		var jdbcDiv = $(document.createElement('div'));
		jdbcDiv.addClass('displayGridCell');
		if (that.alive) {
			jdbcDiv.addClass('isAlive');
		} else {
			jdbcDiv.addClass('isDead');
		}
		jdbcDiv.text(that.name);
		jdbcDiv.attr('title', that.url);
		jdbcDiv.appendTo(cellDiv);

	};

	that.setState(system);
	return that;

};

ASM.jmxServer = function(system) {

	var that = ASM.baseSystem(system);
	that.allServers = {};

	var super_setState = that.setState;
	that.setState = function(system) {
		super_setState(system);
		that.ip = that.url.split(":")[0];

		var allServersKeys = Object.keys(that.allServers);
		var keys = Object.keys(that.data);

		for ( var i = 0; i < allServersKeys.length; i++) {
			that.allServers[allServersKeys[i]] = "DEAD";
		}
		for ( var i = 0; i < keys.length; i++) {
			that.allServers[keys[i]] = that.data[keys[i]];
		}
	};

	// get updated data from server
	that.update = function(callback) {

		var jsonDTO = JSON.stringify(that.toDTO());
		that.startUpdate();
		var request = $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonDTO,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		request.done(function(data, code, jqXHR) {
			that.setState(data);
			that.stopUpdate();
			that.draw();
			callback();
		});
		request.fail(function(jqHXR, textStatus) {
			that.alive = false;
			that.stopUpdate();
			that.draw();
			callback();
		});
	};

	// draw
	that.draw = function() {
		var idString = "#" + ASM.createSystemCellId({
			name : that.name
		});
		var cellDiv = $(idString);
		cellDiv.empty();
		var allKeys = Object.keys(that.allServers);

		for ( var i = 0; i < allKeys.length; i++) {
			var serverDiv = $(document.createElement('div'));
			serverDiv.addClass('displayGridCell');
			if (that.allServers[allKeys[i]] == "RUNNING") {
				serverDiv.addClass('isAlive');
			} else if (that.allServers[allKeys[i]] == "DEAD") {
				serverDiv.addClass('isDead');
			} else if (that.allServers[allKeys[i]] == "STUCK") {
				serverDiv.addClass('isUnstable');
			} else {
				serverDiv.addClass('isUnknown');
			}
			serverDiv.text(allKeys[i]);
			serverDiv.attr('title', that.ip);
			serverDiv.appendTo(cellDiv);

		}

	};

	that.setState(system);
	return that;

};
