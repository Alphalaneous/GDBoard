function command(){
    if(user === 'Alphalaneous' || isChaos){
        if(isModChaos && !isMod){
           return;
        }
	    GD.doChaos(xArgs);
	}
}