


ASM.drawTable = function( ){
	var tableHTML = "<table>";
	tableHTML += "<tr><th>Navn</th><th>Url</th><th>Ping</th><th>Alive</th></tr>";
	for ( var int = 0; int < ASM.systems.length; int++) {
		tableHTML += "<tr>";
		tableHTML += "<td>" + ASM.systems[int].name + "</td>" ;
		tableHTML += "<td>" + ASM.systems[int].url + "</td>" ;
		tableHTML += "<td><div id=" + ASM.createSystemPingId(int) + " >" + ASM.systems[int].ping + "</div></td>" ;
		tableHTML += "<td>" + ASM.systems[int].alive + "</td>" ;
		tableHTML += "<td><div id=" + ASM.createSystemImgId(int) + " hidden=\"true\" ><img src=\"resources/images/spinner2.gif\" /></div></td>"; 
		tableHTML += "</tr>";
	}
	
	tableHTML = tableHTML + "</table>";
	$("#systemDisplay").html(tableHTML);
};


ASM.refreshTable = function(){
	/*for ( var int = 0; int < ASM.systems.length; int++) {
		var imgId = "#" + ASM.createSystemImgId(int);
		var pingId = "#" + ASM.createSystemPingId(int);
		$(imgId).show();
		ASM.systems[int].ping = Math.random() * 100;
		$(pingId).html(ASM.systems[int].ping);
		$(imgId).hide();
		
	}*/
	
	ASM.refreshSystem(0);
};

ASM.refreshSystem= function (id){
	if(id >= ASM.systems.length)
		return;
	var imgId = "#" + ASM.createSystemImgId(id);
	$(imgId).show();
	setTimeout(function() {ASM.continueRefreshSystem(id); },1000  );
};


ASM.continueRefreshSystem = function(id){
	var imgId = "#" + ASM.createSystemImgId(id);
	var pingId = "#" + ASM.createSystemPingId(id);
	ASM.systems[id].ping = Math.random() * 100;
	$(pingId).html(ASM.systems[id].ping);
	$(imgId).hide();
	ASM.refreshSystem(id+1);
	
};

ASM.createSystemImgId = function (id){
	return "systemImg" + id;
};


ASM.createSystemPingId = function (id){
	return "systemPing" + id;
};
