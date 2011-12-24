


ASM.drawTable = function( ){
	tableHTML = "<table>";
	tableHTML += "<tr><th>Navn</th><th>Url</th><th>Ping</th><th>Alive</th></tr>";
	for ( var int = 0; int < ASM.systems.length; int++) {
		tableHTML += "<tr>";
		tableHTML += "<td>" + ASM.systems[int].name + "</td>" ;
		tableHTML += "<td>" + ASM.systems[int].url + "</td>" ;
		tableHTML += "<td>" + ASM.systems[int].ping + "</td>" ;
		tableHTML += "<td>" + ASM.systems[int].alive + "</td>" ;
		tableHTML += "</tr>";
	}
	
	tableHTML = tableHTML + "</table>";
	$("#systemDisplay").html(tableHTML);
};

