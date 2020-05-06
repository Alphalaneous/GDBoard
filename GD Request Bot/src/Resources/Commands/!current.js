function command(){
	if(Levels.getSize() > 0){
		return '@' + user + ' The current level is ' + Levels.getLevel(0, 'name') + ' by ' + Levels.getLevel(0, 'author') + ' (' + Levels.getLevel(0, 'id') + ') Requested by ' + Levels.getLevel(0, 'requester');
	}
}