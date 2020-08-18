function command(){
	if(isMod){
		if(Levels.getPosFromID(xArgs[0]) != -1){
        	Levels.movePosition(Levels.getPosFromID(xArgs[0]), xArgs[1]-1);
        	return Utilities.format("$MOVE_MESSAGE$", user, xArgs[0].toString(), xArgs[1].toString())
        }
        else{
        	return Utilities.format("$MOVE_FAILED_MESSAGE$", user, xArgs[0].toString());
        }
	}
}