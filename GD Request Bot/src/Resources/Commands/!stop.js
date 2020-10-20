function command(){
    if((user === 'Alphalaneous' || (isMod && isChaos)) || isChaosChaos){
        GD.run('speed', '0')
    }
}