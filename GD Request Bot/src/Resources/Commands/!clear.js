function command(){
    if(isMod){
	    Levels.clear();
	    return Utilities.format('$CLEAR_MESSAGE$', user);
	}
}

