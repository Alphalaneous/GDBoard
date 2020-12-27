function command(){
    if(isMod){
        if(xArgs.length == 0){
        	return Utilities.format("$BLOCK_NO_ID_MESSAGE$", user);
        }
	    return Levels.block(user, args);
	}
}