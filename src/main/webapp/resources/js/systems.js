

ASM.JMXServer = function(system ){
	
	var name;
	var url;
	var alive;
	var ping;
	var timeout;
	var data;
	var type;
	var allServers = {};
	
	setState(system);
	
	var toDTO = function(){
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
	
	var setState= function (system){
		name = system.name;
		url = system.url;
		alive = system.alive;
		ping = system.ping;
		timeout = system.timeout;
		data = system.data;
		type = system.type;
	};
	
	// will alter the display to indicate that an update is running
	var startUpdate = function(){};
	
	var stopUpdate = function(){};
	
	
	// get updated data from server
	this.update = function(callback){
/*		
		var allServers = ASM.systems[id].allServers;
		delete ASM.systems[id].allServers;
		var jsonSystem = JSON.stringify(ASM.systems[id]);
		ASM.systems[id].allServers = allServers;
		ASM.display.startUpdateSystem(id);
		var date = new Date();
		ASM.currStartTime = date.getTime();
		ASM.currRequest = $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonSystem,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		ASM.currRequest.done(function(data, code, jqXHR) {
			ASM.systemPingOk(id, data);
		});
		ASM.currRequest.fail(function(jqXHR, textStatus) {
			ASM.systemPingFail(id, jqXHR, textStatus);
		});
		
	*/
		var jsonDTO = JSON.stringify(toDTO());
		startUpdate();
		var request =   $.ajax({
			url : "/autosysmonitor/pingSystem",
			type : "POST",
			data : jsonDTO,
			dataType : "json",
			contentType : "application/json; charset=utf-8"
		});
		request.done(function(data, code, jqXHR){
			setState(data);
			stopUpdate();
			draw();
			callback();
		});
		request.fail(function(jqHXR,textStatus){
			alive = false;
			stopUpdate();
			draw();
			callback();
		});
		
		
	};
	
	//draw
	var draw = function(){};
	this.draw = draw;
	
	
	/*
	function drawJmxServerInstanceCell(int){
		var html = '<div  id="' +   ASM.createSystemPingId(int)   +   '" >';
		html  +=  ASM.systems[int].name;
		html += '<div id="' + ASM.createSystemImgId(int) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div></div>';
		return html;
		
	}
	
	*/
	
	
	
};
