ASM.drawTable = function() {
	var tableHTML = "<table>";
	tableHTML += "<tr><th>Navn</th><th>Url</th><th>Ping</th><th>Alive</th></tr>";
	for ( var int = 0; int < ASM.systems.length; int++) {
		tableHTML += "<tr>";
		tableHTML += "<td>" + ASM.systems[int].name + "</td>";
		tableHTML += "<td>" + ASM.systems[int].url + "</td>";
		tableHTML += "<td><div id=" + ASM.createSystemPingId(int) + " >" + ASM.systems[int].ping + "</div></td>";
		tableHTML += "<td><img id=" + ASM.createSystemAliveId(int) + " src=\"resources/images/Red-ball.png\" /></td>";
		tableHTML += "<td><div id=" + ASM.createSystemImgId(int) + " hidden=\"true\" ><img src=\"resources/images/spinner2.gif\" /></div></td>";
		tableHTML += "</tr>";
	}

	tableHTML = tableHTML + "</table>";
	$("#systemDisplay").html(tableHTML);
};

ASM.refreshTable = function() {
	ASM.refreshSystem(0);
};

ASM.refreshSystem = function(id) {
	if (id >= ASM.systems.length)
		return;
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
	// var time = new Date();
	// ASM.systems[id].ping = time.getTime() - ASM.currStartTime;
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

ASM.createSystemImgId = function(id) {
	return "systemImg" + id;
};

ASM.createSystemPingId = function(id) {
	return "systemPing" + id;
};

ASM.createSystemAliveId = function(id) {
	return "systemAlive" + id;
};
