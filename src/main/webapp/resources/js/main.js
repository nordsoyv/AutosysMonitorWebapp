ASM.drawTable = function() {
	var tableHTML = "<table>";
	tableHTML += "<tr><th>Navn</th><th>Url</th><th>Ping</th><th>Alive</th><th width=\"16px\" ></th></tr>";
	for ( var int = 0; int < ASM.systems.length; int++) {
		if (ASM.isSystemInfoHeadline(int)){
			tableHTML += ASM.drawHeadingTableRow(int);
		}else{
			tableHTML += ASM.drawSystemTableRow(int);
		}
	}

	tableHTML = tableHTML + "</table>";
	$("#systemDisplay").html(tableHTML);
};


ASM.drawSystemTableRow = function(int ){
	var tableHTML = "<tr>";
	tableHTML += "<td>" + ASM.systems[int].name + "</td>";
	tableHTML += "<td>" + ASM.systems[int].url + "</td>";
	tableHTML += "<td><div id=" + ASM.createSystemPingId(int) + " >" + ASM.systems[int].ping + "</div></td>";
	tableHTML += "<td><img id=" + ASM.createSystemAliveId(int) + " src=\"resources/images/Red-ball.png\" /></td>";
	tableHTML += "<td><div id=" + ASM.createSystemImgId(int) + " hidden=\"true\" ><img src=\"resources/images/spinner2.gif\" /></div></td>";
	tableHTML += "</tr>";
	return tableHTML;
};

ASM.drawHeadingTableRow = function(int ){
	var tableHTML = "<tr>";
	tableHTML += "<td>" + ASM.systems[int].name.substring(1,ASM.systems[int].name.length) + "</td>";
	tableHTML += "<td>----------------------</td>";
	tableHTML += "<td></td>";
	tableHTML += "<td></td>";
	tableHTML += "<td></td>";
	tableHTML += "</tr>";
	return tableHTML;
};


ASM.refreshTable = function() {
	if(ASM.isRefreshing){
		return;
	}
	ASM.isRefreshing = true;
	ASM.refreshSystem(0);
};

ASM.refreshSystem = function(id) {
	if (id >= ASM.systems.length){
		ASM.isRefreshing = false;
		return;
	}
	if (ASM.isSystemInfoHeadline(id) ) {
		//Dette er en overskrift , skip til neste
		ASM.refreshSystem(id+1);
	}
	var jsonSystem = JSON.stringify(ASM.systems[id]);
	var imgId = "#" + ASM.createSystemImgId(id);
	$(imgId).show();
	var date = new Date();
	ASM.currStartTime = date.getTime();
	ASM.currRequest = $.ajax({
		url : "pingSystem",
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
	ASM.systems[id] = data;
	ASM.systemPingContinue(id);
};

ASM.systemPingFail = function(id, jqXHR, textStatus) {
	ASM.systems[id].alive = false;
	ASM.systemPingContinue(id);
};

ASM.systemPingContinue = function(id) {
	var imgId = "#" + ASM.createSystemImgId(id);
	var pingId = "#" + ASM.createSystemPingId(id);
	var aliveId = "#" + ASM.createSystemAliveId(id);
	var aliveimg = $(aliveId);
	$(pingId).html(ASM.systems[id].ping);
	$(aliveId).html(ASM.systems[id].alive);
	$(imgId).hide();
	if (ASM.systems[id].alive === true) {
		aliveimg.attr('src', "resources/images/Green-ball.png");
	} else {
		aliveimg.attr('src', "resources/images/Red-ball.png");
	}
	ASM.refreshSystem(id + 1);
};

ASM.setInterval= function(){
	if(ASM.intervalHandle !== undefined || ASM.intervalHandle !== null){
		clearInterval(ASM.intervalHandle);
	}
	var interval = $("#refeshInterval");
	ASM.refreshInterval = parseInt(interval.val());
	ASM.intervalHandle = setInterval( function (){ ASM.refreshTable();  }  ,   ASM.refreshInterval);
	var check = $('#setActiveCheckbox');
	check.prop("checked", true);
};

ASM.setRefreshActive = function (){
	if($('#setActiveCheckbox:checked').val() !== undefined){
		//is checked
		ASM.setInterval();
		ASM.refreshTable();
		
	}else{
		if(ASM.intervalHandle !== undefined || ASM.intervalHandle !== null){
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

ASM.isSystemInfoHeadline = function (id){
	if (ASM.systems[id].name.substring(0, 1) === "-") {
		return true;
	}else{
		return false;
	}
};