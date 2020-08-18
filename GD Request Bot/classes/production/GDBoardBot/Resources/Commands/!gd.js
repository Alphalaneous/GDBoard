function command(){
	if(user === 'alphalaneous' || (isMod && isChaos)){
		GD.run(xArgs);
	}
}