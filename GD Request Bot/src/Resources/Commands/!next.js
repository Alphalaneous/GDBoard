function command(){
    if(user === 'alphalaneous'){
	    Levels.knock();
	}
	if(Levels.getSize() > 1){
		return '@' + user + ' The next level is ' + Levels.getLevel(1, 'name') + ' by ' + Levels.getLevel(1, 'author') + ' (' + Levels.getLevel(1, 'id') + ') Requested by ' + Levels.getLevel(0, 'requester');
	}
}