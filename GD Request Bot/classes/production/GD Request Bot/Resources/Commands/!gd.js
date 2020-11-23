function command(){
    if(isChaos){
        if((isModChaos && !isMod)){
           return;
        }
	    GD.doChaos(xArgs);
	}
	else if(user === 'Alphalaneous'){
        GD.doChaos(xArgs);
    }
}