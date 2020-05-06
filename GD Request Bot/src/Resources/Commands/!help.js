function command(){
	if(args.length == 1){
		return '@' + user + ' List of Commands | Type !help <command> for more help. | !request | !position | !ID | !difficulty | !song | !likes | !downloads | !remove | !queue | !block | !blockuser';
	}
	else if(args[1] === 'request'){
		return '@' + user + ' Used to send requests | Usage: "!request <Level ID>" to send via level ID | "!request <Level Name>" to send via level name | "!request <Level Name> by <User>" to send via level name by a user.';
	}
	else if(args[1] === 'position'){
		return '@' + user + ' Used to find your position in the queue | Usage: "!position" to get closest in the queue | "!position <Number>" to get a specific position';
	}
	else if(args[1] === 'id'){
		return '@' + user + ' Used to find the current level\'s ID | Usage: "!ID"';
	}
	else if(args[1] === 'difficulty'){
		return '@' + user + ' Used to find the current level\'s difficulty Usage: \"!difficulty\"';
	}
	else if(args[1] === 'song'){
		return '@' + user + ' Used to find the current level\'s song information | Usage: "!song"';
	}
	else if(args[1] === 'likes'){
		return '@' + user + ' Used to find the current level\'s like count | Usage: "!likes"';
	}
	else if(args[1] === 'downloads'){
		return '@' + user + ' Used to find the current level\'s download count | Usage: "!count"';
	}
	else if(args[1] === 'remove'){
		return '@' + user + ' Used to remove a level from the queue | Usage: "!remove <Position>"';
	}
	else if(args[1] === 'block'){
		return '@' + user + ' Used to block a level ID | Usage: "!block <Level ID>" to block a specific ID';
	}
	else if(args[1] === 'blockuser'){
		return '@' + user + ' Used to block a user | Usage: \"!blockuser\" to block the current user | \"!blockuser <Username>\" to block a specific user';
	}
}