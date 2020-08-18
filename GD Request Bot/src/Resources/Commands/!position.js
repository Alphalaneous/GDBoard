function command(){
var intArg = parseInt(args[1]);
    if(isNaN(intArg) || args.length == 1){
    	intArg = 1;
    }
    if(intArg < 1){
        intArg = 1;
    }
    var userPosition = [];
    for(var i = 0; i < Levels.getSize(); i++){
        if(Levels.getLevel(i, 'requester') === user){
            userPosition.push(i);
        }
    }
    if(userPosition.length == 0){
        return Utilities.format("$POSITION_NONE_MESSAGE$", user);
    }
    if(intArg > userPosition.length){
        return Utilities.format("$POSITION_WRONG_MESSAGE$", user, userPosition.length);
    }
    var pos = userPosition[intArg-1]+1;
    return Utilities.format("$POSITION_MESSAGE$", user,
    Levels.getLevel(userPosition[intArg-1], 'name'), pos);
}