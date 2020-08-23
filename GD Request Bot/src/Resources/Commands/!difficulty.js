function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
	    return Utilities.format("$DIFFICULTY_COMMAND_MESSAGE$", user,
	    Levels.getLevel(intArg-1, 'name'),
	    Levels.getLevel(intArg-1, 'id'),
	    Levels.getLevel(intArg-1, 'difficulty'));
	}
	else{
	return '';
	}
}