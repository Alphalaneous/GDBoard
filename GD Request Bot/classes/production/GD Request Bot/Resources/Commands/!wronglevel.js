function command(){
    var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	return Levels.removeLatest(user, isMod);
}