function command(){
	if(isMod){
	    if(Levels.getPosFromID(xArgs[0]) != -1){
		    Levels.movePosition(Levels.getPosFromID(xArgs[0]), 0);
		    return '@' + user + " " + xArgs[0].toString() + " has been moved to the top!";
		}
		else{
		    return '@' + user + " Failed to move " + xArgs[0].toString() + "!";

		}
	}
}