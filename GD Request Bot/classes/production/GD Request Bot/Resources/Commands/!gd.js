function command(){
	if((user === 'alphalaneous' || (isMod && isChaos)) || isChaosChaos){
		GD.run(xArgs);
	}
}