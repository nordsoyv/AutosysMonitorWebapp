"use strict";
var ASM;
if (!ASM) {
	ASM = {};
}


ASM.GridDisplay = function() {

	
	var generateScaffold= function(systems){
		var mainDiv = $(document.createElement('div'));
		mainDiv.addClass("displayGridMain");
		var currentHeader = null;
		for ( var i = 0; i < systems.length; i++) {
			if (ASM.isHeader(systems[i])) {
				if(currentHeader != null){
					mainDiv.append(currentHeader);
				}
				currentHeader = generateHeader(systems[i]);
			} else {
				generateGridCell(systems[i]).appendTo(currentHeader);
			}
		}
		if(currentHeader != null){
			currentHeader.appendTo(mainDiv);
		}
		$("#systemDisplay2").empty().append(mainDiv);
		
	};
	
	this.generateScaffold = generateScaffold;
	
	var generateGridCell = function(system){
		var cellDiv =$(document.createElement('div'));
		cellDiv.attr("id" , ASM.createSystemCellId(system));
		cellDiv.addClass("displayGridCellWrapper");
		return cellDiv;
	};
	
	var generateHeader = function(system){
		var header = $(document.createElement('div'));
		header.addClass("displayGridSubheading");
		var heading = $(document.createElement('div'));
		heading.addClass('displayGridSubHeader').text(system.name);
		header.append(heading);
		return header;
	};

/*	this.draw = function() {
		drawGridView();
	};
	*/
	
	
	this.startUpdateSystem = function(id){
		var imgId = "#" + ASM.createSystemImgId(id);
		$(imgId).show();
	};
	
};