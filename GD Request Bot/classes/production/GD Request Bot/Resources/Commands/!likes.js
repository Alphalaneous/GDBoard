function command(){
	var intArg = parseInt(args[1]);
	if(isNaN(intArg) || args.length == 1){
		intArg = 1;
	}
	if(Levels.getSize() > 0 && intArg <= Levels.getSize()){
		return '@' + user + ' Level ' + intArg + ' in the queue has ' + Levels.getLevel(intArg-1, 'likes') + ' likes!';
	}
	else{
	return '';
	}
}