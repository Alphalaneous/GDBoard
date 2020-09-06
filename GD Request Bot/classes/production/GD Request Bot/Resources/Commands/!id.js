function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
        return Utilities.format("$ID_MESSAGE$", user,
	        intArg|0,
    	    Levels.getLevel(intArg-1, 'name'),
    	    Levels.getLevel(intArg-1, 'author'),
    	    Levels.getLevel(intArg-1, 'id'),
    	    Levels.getLevel(intArg-1, 'requester'));
    } else {
	    return '';
	}
}