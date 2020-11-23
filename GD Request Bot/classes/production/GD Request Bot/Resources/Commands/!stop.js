function command(){
    if(isChaos){
        if(isModChaos && !isMod){
           return;
        }
        GD.doChaos('speed', '0');
    }
    else if(user === 'Alphalaneous'){
        GD.doChaos('speed', '0');
    }
}