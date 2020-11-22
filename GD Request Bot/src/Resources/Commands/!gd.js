function command(){
    if(user === 'Alphalaneous' || isChaos){

        if((isModChaos && !isMod) || !(user === 'Alphalaneous')){
           return;
        }
	    GD.doChaos(xArgs);
	}
}