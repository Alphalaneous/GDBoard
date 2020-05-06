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
        return '@' + user + ' You don\'t have any levels in the queue!';
    }
    if(intArg > userPosition.length){
        return '@' + user + ' You only have ' + userPosition.length +  ' levels in the queue!';
    }
    var pos = userPosition[intArg-1]+1;
    return '@' + user + ', ' + Levels.getLevel(userPosition[intArg-1], 'name') + ' is at position ' + pos + ' in the queue!';
}