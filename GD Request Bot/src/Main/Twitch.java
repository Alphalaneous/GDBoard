package Main;

import java.util.Random;

public class Twitch {

	public static String getFollowDate(String user){
		return null;
	}
	public static String getFollowAge(String user){
		return null;
	}
	public static long getFollowers(){
		return APIs.getFollowerCount();
	}
	public static String getRandomViewer(){
		APIs.setAllViewers();
		Random random = new Random();
		int num = random.nextInt(APIs.allViewers.size()-1);
		return APIs.allViewers.get(num);
	}
	public static String[] getViewers(){
		APIs.setAllViewers();
		return (String[]) APIs.allViewers.toArray();
	}
}
