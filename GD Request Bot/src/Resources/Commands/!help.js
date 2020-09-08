function command(){
    if(args.length > 1){
        return Levels.getHelp(user, isMod, args[1]);
    }
    else{
	    return Levels.getHelp(user, isMod);
	}
}