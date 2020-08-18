function command(){
	if(Levels.getSize() > 1){

	    return Utilities.format("$NEXT_MESSAGE$", user,
	    Levels.getLevel(1, 'name'),
	    Levels.getLevel(1, 'author'),
	    Levels.getLevel(1, 'id'),
	    Levels.getLevel(1, 'requester'));
	}
}