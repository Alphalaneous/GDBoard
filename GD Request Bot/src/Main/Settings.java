package Main;

import java.awt.Point;

class Settings {

	static String oauth;
	static Point requestsWLoc = new Point(10,10);
	static String channel;

	static boolean hasOauth() {
		return true;
	}

	static void setOAuth(String oauth) {
		Settings.oauth = oauth;
	}
	static void setChannel (String channel){
		Settings.channel = "#" + channel.toLowerCase();
	}
	static Point getRequestsWLoc() {
		return requestsWLoc;
	}
	
}
