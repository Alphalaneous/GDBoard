function command(){
	return Requests.request(user, isMod, isSub, message, messageID);
}