function command(){
	if(isMod){
		if(Levels.getPosFromID(xArgs[0]) != -1){
        	Levels.movePosition(Levels.getPosFromID(xArgs[0]), xArgs[1]-1);
        	return '@' + user + " " + xArgs[0].toString() + " has been moved to position " + xArgs[1].toString() + "!";
        }
        else{
        	return '@' + user + " Failed to move " + xArgs[0].toString() + "!";
        }
	}
}