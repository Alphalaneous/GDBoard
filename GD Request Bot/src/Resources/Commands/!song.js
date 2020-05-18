function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(user === 'alphalaneous'){
	    Levels.bwomp();
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
		return '@' + user + ' The song name for level ' + intArg + ' is ' + Levels.getLevel(intArg-1, 'songName') + ' by ' + Levels.getLevel(intArg-1, 'songAuthor') + ' (' + Levels.getLevel(intArg-1, 'songID') + ')';
	}
	else{
	return '';
	}
}