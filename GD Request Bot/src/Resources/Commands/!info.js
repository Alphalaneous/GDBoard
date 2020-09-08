function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
	    return Utilities.format("$INFO_COMMAND_MESSAGE$", user,
	    Levels.getLevel(intArg-1, 'name'),
	    Levels.getLevel(intArg-1, 'id'),
	    Levels.getLevel(intArg-1, 'author'),
	    Levels.getLevel(intArg-1, 'requester'),
	    Levels.getLevel(intArg-1, 'downloads'),
	    Levels.getLevel(intArg-1, 'likes'),
	    Levels.getLevel(intArg-1, 'objects'),
	    Levels.getLevel(intArg-1, 'difficulty'));
	}
}