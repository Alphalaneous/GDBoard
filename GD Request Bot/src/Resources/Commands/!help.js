function command(){
    if(args.length > 1){
        return '@' + user + ' ' + Levels.getHelp(args[1]);
    }
    else{
	    return '@' + user + ' ' + Levels.getHelp();
	}
}