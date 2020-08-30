function command(){
    if((user === 'alphalaneous' || (isMod && isChaos)) || isChaosChaos){
        GD.run('kill');
    }
}