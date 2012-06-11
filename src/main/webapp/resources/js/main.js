ASM = {};

ASM.gridDisplay = function() {

	function drawGridView() {
		var gridHTML = '<div class="displayGridMain">';
		gridHTML += drawGridHeader(0);
		for ( var int = 1; int < ASM.systems.length; int++) {
			if (ASM.isSystemInfoHeadline(int)) {
				gridHTML += '</div><br />';
				gridHTML += drawGridHeader(int);
			} else {
				gridHTML += drawGridCell(int);
			}
		}
		gridHTML += "</div>";
		$("#systemDisplay2").html(gridHTML);
	}

	function drawGridHeader(int) {
		var html ='<div class="displayGridSubheading">'; 
		html += '<div clasS="displayGridSubHeader">' + ASM.systems[int].name + '</div>' ;
		return html;
	}

	function drawGridCell(int) {
		if(isHttpGet(int)){
			return drawHttpGetCell(int);
		}else if(isJdbc(int)){
			return drawJdbcCell(int);
		}else if(isJmxServer(int)){
			return drawJmxServerInstanceCell(int);
		}else if(isJmxApp(int)){
			return drawJmxAppInstanceCell(int);
		}
	}
	
	function isHttpGet(int){
		if(ASM.systems[int].type == "HTTP-GET"){
			return true;
		}
		return false;
	}
	
	function isJdbc(int){
		if(ASM.systems[int].type == "JDBC"){
			return true;
		}
		return false;
	}
	
	function isJmxServer(int){
		if(ASM.systems[int].type == "JMXSERVER"){
			return true;
		}
		return false;
	}
	
	function isJmxApp(int){
		if(ASM.systems[int].type == "JMXAPPS"){
			return true;
		}
		return false;
	}
			
	function drawHttpGetCell(int){
		var html = '<div class="displayGridCell isgrey"  id="' +   ASM.createSystemPingId(int)   +   '" >';
		html  +=  ASM.systems[int].name;
		html += '<div id="' + ASM.createSystemImgId(int) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div></div>';
		return html;
	}
	
	function drawJdbcCell(int){
		var html = '<div class="displayGridCell isgrey"  id="' +   ASM.createSystemPingId(int)   +   '" >';
		html  +=  ASM.systems[int].name;
		html += '<div id="' + ASM.createSystemImgId(int) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div></div>';
		return html;
	}

	function drawJmxServerInstanceCell(int){
		var html = '<div  id="' +   ASM.createSystemPingId(int)   +   '" >';
		html  +=  ASM.systems[int].name;
		html += '<div id="' + ASM.createSystemImgId(int) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div></div>';
		return html;
		
	}
	
	
	function drawJmxAppInstanceCell(int){
		var html = '<div  id="' +   ASM.createSystemPingId(int)   +   '" >';
		html  +=  ASM.systems[int].name;
		html += '<div id="' + ASM.createSystemImgId(int) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div></div>';
		return html;		
	}
	
	this.updateSystemStatus = function(id) {
		if(isHttpGet(id)){
			updateHttpGetSystem(id);
		}else if(isJdbc(id)){
			updateJdbcSystem(id);
		}else if(isJmxServer(id)){
			updateJmxServerSystem(id);
		}else if(isJmxApp(id)){
			updateJmxAppSystem(id);
		}
	};

	function updateHttpGetSystem(id){
		var pingId = "#" + ASM.createSystemPingId(id);
		var sysCell = $(pingId);
		var imgId = "#" + ASM.createSystemImgId(id);
		$(imgId).hide();
		if (ASM.systems[id].alive === true) {
			sysCell.removeClass("isgrey");
			sysCell.removeClass("isred");
			sysCell.addClass("isgreen");
		} else {
			sysCell.removeClass("isgrey");
			sysCell.removeClass("isgreen");
			sysCell.addClass("isred");
		}
	}
	
	function updateJdbcSystem(id){
		//same as httpget
		updateHttpGetSystem(id);
	}
	
	function updateJmxServerSystem(id){
		var sysid = "#"+ ASM.createSystemPingId(id);
		sysdiv = $(sysid);
		
		var html ="";
		var newData = ASM.systems[id].data;
		var allServers = ASM.systems[id].allServers;
		if(allServers === undefined || allServers === null){
			allServers = {};
		}
		var allServersKeys = Object.keys(allServers);
		var keys = Object.keys(newData);
		
		for(var i =  0 ; i< allServersKeys.length; i++){
			allServers[allServersKeys[i]] = "DEAD"; 
		}
		for(var i =  0 ; i< keys.length; i++){
			allServers[keys[i]] = newData[keys[i]]; 
		}
		ASM.systems[id].allServers = allServers;
		allServersKeys = Object.keys(allServers);
		for(var i =  0 ; i< allServersKeys.length; i++){
			html += "<div " ;
			if (allServers[allServersKeys[i]]=="RUNNING"){
				html += 'class="displayGridCell isgreen" >' ;
			}else if(allServers[allServersKeys[i]]=="STUCK") {
				html += 'class="displayGridCell isyellow" >' ;
			}else{
				html += 'class="displayGridCell isred" >' ;
			}
			html += allServersKeys[i];
			html += '</div>';
		}
		html += '<div id="' + ASM.createSystemImgId(id) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div>';
		sysdiv.html(html);
	}
	
	function updateJmxAppSystem(id){
		var sysid = "#"+ ASM.createSystemPingId(id);
		sysdiv = $(sysid);
		
		var html ="";
		var data = ASM.systems[id].data;
		var keys = Object.keys(data);
		for(var i =  0 ; i< keys.length; i++){
			html += "<div " ;
			if (data[keys[i]]==1){
				html += 'class="displayGridCell isgreen" >' ;
			}else{
				html += 'class="displayGridCell isred" >' ;
			}
			html += keys[i];
			html += '</div>';
		}
		html += '<div id="' + ASM.createSystemImgId(id) + '" hidden="true" >';
		html += '<img src="/autosysmonitor/resources/images/spinner2.gif" />';
		html += '</div>';
		sysdiv.html(html);
		
	}
	
	this.render = function() {
		drawGridView();
	};
	
	this.startUpdateSystem = function(id){
		var imgId = "#" + ASM.createSystemImgId(id);
		$(imgId).show();
	};
	
};


ASM.tableDisplay = function() {

	function drawTable() {
		var tableHTML = "<table>";
		tableHTML += "<tr><th>Navn</th><th>Url</th><th>Ping</th><th>Alive</th><th width=\"16px\" ></th></tr>";
		for ( var int = 0; int < ASM.systems.length; int++) {
			if (ASM.isSystemInfoHeadline(int)) {
				tableHTML += drawHeadingTableRow(int);
			} else {
				tableHTML += drawSystemTableRow(int);
			}
		}

		tableHTML = tableHTML + "</table>";
		$("#systemDisplay").html(tableHTML);
	}

	function drawSystemTableRow(int) {
		var tableHTML = "<tr>";
		tableHTML += "<td>" + ASM.systems[int].name + "</td>";
		tableHTML += "<td>" + ASM.systems[int].url + "</td>";
		tableHTML += "<td><div id=" + ASM.createSystemPingId(int) + " >" + ASM.systems[int].ping + "</div></td>";
		tableHTML += "<td><img id=" + ASM.createSystemAliveId(int) + " src=\"/autosysmonitor/resources/images/Red-ball.png\" /></td>";
		tableHTML += "<td><div id=" + ASM.createSystemImgId(int)
				+ " hidden=\"true\" ><img src=\"/autosysmonitor/resources/images/spinner2.gif\" /></div></td>";
		tableHTML += "</tr>";
		return tableHTML;
	}

	function drawHeadingTableRow(int) {
		var tableHTML = "<tr>";
		tableHTML += "<td>" + ASM.systems[int].name.substring(1, ASM.systems[int].name.length) + "</td>";
		tableHTML += "<td>----------------------</td>";
		tableHTML += "<td></td>";
		tableHTML += "<td></td>";
		tableHTML += "<td></td>";
		tableHTML += "</tr>";
		return tableHTML;
	}
	
	this.render = function() {
		drawTable();
	};
	
	
	this.updateSystemStatus = function(id) {
		var imgId = "#" + ASM.createSystemImgId(id);
		var pingId = "#" + ASM.createSystemPingId(id);
		var aliveId = "#" + ASM.createSystemAliveId(id);
		var aliveimg = $(aliveId);
		$(pingId).html(ASM.systems[id].ping);
		$(aliveId).html(ASM.systems[id].alive);
		$(imgId).hide();
		if (ASM.systems[id].alive === true) {
			aliveimg.attr('src', "/autosysmonitor/resources/images/Green-ball.png");
		} else {
			aliveimg.attr('src', "/autosysmonitor/resources/images/Red-ball.png");
		}
	};
	
	this.startUpdateSystem = function(id){
		var imgId = "#" + ASM.createSystemImgId(id);
		$(imgId).show();
	};
	
};

ASM.init = function() {
	ASM.display = new ASM.gridDisplay();
	//ASM.display = new ASM.tableDisplay();
}();

ASM.renderSystemsView = function() {
	ASM.display.render();
};

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
	if (ASM.isSystemInfoHeadline(id)) {
		// Dette er en overskrift , skip til neste
		ASM.refreshSystem(id + 1);
		return;
	}
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
};

ASM.systemPingOk = function(id, data) {
	if(ASM.systems[id].type == "JMXSERVER"){
		//need to preserve old serverlist
		var allServers =ASM.systems[id].allServers;
		ASM.systems[id] = data;
		ASM.systems[id].allServers = allServers;
	}else{
		ASM.systems[id] = data;
	}
	ASM.systemPingContinue(id);
};

ASM.systemPingFail = function(id, jqXHR, textStatus) {
	ASM.systems[id].alive = false;
	ASM.systemPingContinue(id);
};

ASM.systemPingContinue = function(id) {
	ASM.display.updateSystemStatus(id);
	ASM.refreshSystem(id + 1);
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
	ASM.systems = data;
	ASM.renderSystemsView();
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

ASM.createSystemImgId = function(id) {
	return "systemImg" + id;
};

ASM.createSystemPingId = function(id) {
	return "systemPing" + id;
};

ASM.createSystemAliveId = function(id) {
	return "systemAlive" + id;
};

ASM.isSystemInfoHeadline = function(id) {
	if(ASM.systems[id].type == "HEADER" ){
		return true;
	}else{
		return false;
	}
	
/*	var name = ASM.systems[id].name;
	if (name.substring(0, 1) == "-") {
		
	} else {
		return false;
	} */
};
