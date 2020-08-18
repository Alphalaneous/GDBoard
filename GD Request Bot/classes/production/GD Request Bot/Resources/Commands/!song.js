function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
	    return Utilities.format("$SONG_MESSAGE$", user,
	        Levels.getLevel(intArg-1, 'songName'),
	        Levels.getLevel(intArg-1, 'songAuthor'),
	        Levels.getLevel(intArg-1, 'songID'))
	}
	else {
	    return '';
	}
}