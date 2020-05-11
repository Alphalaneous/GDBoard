function command(){
    var intArg = parseInt(args[1]);
    if(isNaN(intArg) || args.length == 1){
    	intArg = 1;
    }
    var pages = ((Levels.getSize()-1/10) + 1)|0;

    if(intArg > pages){
        return '@' + user + ' No levels on page ' + intArg;
    }
    if(intArg < 1){
        intArg = 1;
    }
    if(Levels.getSize() == 0){
        return '@' + user + ' There are no levels in the queue!';
    }
    var message = 'Page ' + intArg + ' of ' + pages + ' of the queue | ';
    for(var i = (intArg - 1)*10; i < intArg * 10; i++){
    if(i < Levels.getSize()){
        message = message.concat(i+1 + ': ' + Levels.getLevel(i, 'name') + ' (' + Levels.getLevel(i, 'id') + '), ');
        }
    }
    return message;
}