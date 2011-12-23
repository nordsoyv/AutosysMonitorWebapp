drawTable = function( sys){
	tableHTML = "<table>";
	for ( var int = 0; int < sys.length; int++) {
		tableHTML += "<tr>";
		tableHTML += "<td>" + sys[int].name + "</td>" ;
		tableHTML += "<td>" + sys[int].url + "</td>" ;
		tableHTML += "<td>" + sys[int].ping + "</td>" ;
		tableHTML += "<td>" + sys[int].alive + "</td>" ;
		tableHTML += "</tr>";
	}
	
	tableHTML = tableHTML + "</table>";
	$("#systemDisplay").html(tableHTML);
};

