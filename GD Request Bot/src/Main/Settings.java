package Main;

import java.awt.Point;

public class Settings {

	//cccccTODO Settings content, OAuth, Blocked IDs, Blocked Users, Requests status, Requests Limit, Followers Only, Subscribers only, Cheer requirements

	//private static boolean requests = true;
	//private static String oauth;
	private static Point requestsWLoc = new Point(10,10);

	//public static boolean isRequests() {
	//	return requests;
	//}
	static String getOAuth() {
		return "meuwul05bn5t246c9tmg490z735oqv";
	}
	static boolean hasOauth() {
		return true;
	}
	//public static void setRequests(boolean requests) {
	//	Settings.requests = requests;
	//}
	//static void setOAuth(String oauth) {
	//	Settings.oauth = oauth;
	//}
	static Point getRequestsWLoc() {
		return requestsWLoc;
	}
	
}
