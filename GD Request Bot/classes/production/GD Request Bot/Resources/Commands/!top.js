function command(){
	if(isMod){
	    if(Levels.getPosFromID(xArgs[0]) != -1){
		    Levels.movePosition(Levels.getPosFromID(xArgs[0]), 0);
        	return Utilities.format("$TOP_MESSAGE$", user, xArgs[0].toString())
		}
		else{
        	return Utilities.format("$TOP_FAILED_MESSAGE$", user, xArgs[0].toString());
		}
	}
}