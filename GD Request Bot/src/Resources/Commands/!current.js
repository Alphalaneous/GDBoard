function command(){
	if(Levels.getSize() > 0){
		return Utilities.format("$CURRENT_MESSAGE$", user,
		Levels.getLevel(0, 'name'),
		Levels.getLevel(0, 'author'),
		Levels.getLevel(0, 'id'),
		Levels.getLevel(0, 'requester'));
	}
}