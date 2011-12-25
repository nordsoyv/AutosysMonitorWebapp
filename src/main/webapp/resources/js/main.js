


ASM.drawTable = function( ){
	var tableHTML = "<table>";
	tableHTML += "<tr><th>Navn</th><th>Url</th><th>Ping</th><th>Alive</th></tr>";
	for ( var int = 0; int < ASM.systems.length; int++) {
		tableHTML += "<tr>";
		tableHTML += "<td>" + ASM.systems[int].name + "</td>" ;
		tableHTML += "<td>" + ASM.systems[int].url + "</td>" ;
		tableHTML += "<td><div id=" + ASM.createSystemPingId(int) + " >" + ASM.systems[int].ping + "</div></td>" ;
		tableHTML += "<td><div id=" + ASM.createSystemAliveId(int) + " >" + ASM.systems[int].alive + "</div></td>" ;
		//tableHTML += "<td><img id=" + ASM.createSystemAliveId(int) + " src=\"resources/images/Red-ball.png\" /></td>" ;
		tableHTML += "<td><div id=" + ASM.createSystemImgId(int) + " hidden=\"true\" ><img src=\"resources/images/spinner2.gif\" /></div></td>"; 
		tableHTML += "</tr>";
	}
	
	tableHTML = tableHTML + "</table>";
	$("#systemDisplay").html(tableHTML);
};


ASM.refreshTable = function(){
	ASM.refreshSystem(0);
};

ASM.refreshSystem= function (id){
	if(id >= ASM.systems.length)
		return;
	var imgId = "#" + ASM.createSystemImgId(id);
	$(imgId).show();
	var date = new Date();
	ASM.currStartTime = date.getTime();
	ASM.currRequest = $.ajax( { url : ASM.systems[id].url,
								timeout : ASM.systems[id].timeout });
	ASM.currRequest.done( function(msg) {ASM.systemPingOk(id); }  );
	ASM.currRequest.fail(function(jqXHR, textStatus) {ASM.systemPingFail(id); }  );
};

ASM.systemPingOk= function (id){
	ASM.systems[id].alive = true;
	var aliveId = "#" + ASM.createSystemAliveId(id);
	var aliveimg =$(aliveId);
	//aliveimg.src("resources/images/Green-ball.png");
	ASM.systemPingContinue(id);
};

ASM.systemPingFail= function (id){
	ASM.systems[id].alive = false;
	var aliveId = "#" + ASM.createSystemAliveId(id);
	var aliveimg =$(aliveId);
	//aliveimg.src("resources/images/Red-ball.png");
	ASM.systemPingContinue(id);
};

ASM.systemPingContinue = function (id){
	var imgId = "#" + ASM.createSystemImgId(id);
	var pingId = "#" + ASM.createSystemPingId(id);
	var aliveId = "#" + ASM.createSystemAliveId(id);
	var time = new Date();
	ASM.systems[id].ping =  time.getTime() - ASM.currStartTime;
	$(pingId).html(ASM.systems[id].ping);
	$(aliveId).html(ASM.systems[id].alive);
	$(imgId).hide();
	ASM.refreshSystem(id+1);
};

ASM.createSystemImgId = function (id){
	return "systemImg" + id;
};


ASM.createSystemPingId = function (id){
	return "systemPing" + id;
};

ASM.createSystemAliveId = function (id){
	return "systemAlive" + id;	
};
