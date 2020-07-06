function command(){
	if(isMod){
		Levels.movePosition(Levels.getPosFromID(xArgs[0]), xArgs[1]);
	}
}