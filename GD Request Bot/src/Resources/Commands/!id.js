function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
		return '@' + user + ' The level at position ' + intArg + ' is ' + Levels.getLevel(intArg-1, 'name') + ' by ' + Levels.getLevel(intArg-1, 'author') + ' (' + Levels.getLevel(intArg-1, 'id') + ') Requested by ' + Levels.getLevel(intArg-1, 'requester');
	}
	else{
	return '';
	}
}