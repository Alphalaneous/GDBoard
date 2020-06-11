package Main;

import java.net.MalformedURLException;
import java.net.URL;

/** @noinspection WeakerAccess*/
public class LevelData {



	public String getPassword() {
		return password;
	}


	public String getRequester() {
		return requester;
	}

	public String getAuthor() {
		return author;
	}

	public String getName() {
		return name;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public String getDescription() {
		return description;
	}

	public String getLikes() {
		return likes;
	}

	public String getDownloads() {
		return downloads;
	}

	public String getLevelID() {
		return levelID;
	}

	//int getCoins() {
	//	return coins;
	//}

	//boolean getVerifiedCoins() {
	//	return verifiedCoins;
	//}

	public boolean getFeatured() {
		return featured;
	}

	public boolean getEpic() {
		return epic;
	}

	public String getSongName() {
		return songName;
	}

	public String getSongAuthor() {
		return songAuthor;
	}

	public String getSongID() {
		return songID;
	}

	//String getSongSize() {
	//	return songSize;
	//}
	
	//ArrayList<GDObject> getLevelData(){
	//	return levelData;
	//}

	public boolean getContainsVulgar() {
		return containsVulgar;
	}

	public boolean getContainsImage() {
		return containsImage;
	}

	public boolean getAnalyzed() {
		return analyzed;
	}

	public int getStars() {
		return stars;
	}

	public URL getSongURL() throws MalformedURLException {
		return new URL(downloadURL);
	}

	public String getLength(){
		return length;
	}

	public boolean getNotPersist(){
		return !persist;
	}

	public double getVersion() { return version; }

	public String getVideoTitle() {return videoTitle;}

	public String getVideoURL() { return videoURL;}

	public String getChannelName() { return channelName;}

	public String getThumbnailURL() { return thumbnailURL; }

	public String getUpload() {
		return upload;
	}

	public String getUpdate() {
		return update;
	}

	public int getObjects() {
		return objects;
	}

	public long getOriginal() {
		return original;
	}

	public int getCoins() {
		return coins;
	}
	public int getLevelVersion() {
		return levelVersion;
	}

	private String requester;
	private String password;
	private String author;
	private String name;
	private String difficulty;
	private String description;
	private String likes;
	private String downloads;
	private String levelID;
	private String videoTitle;
	private String videoURL;
	private String channelName;
	private String thumbnailURL;
	private boolean featured;
	private boolean epic;
	private String downloadURL;
	private String songName;
	private String songAuthor;
	private String songID;
	private boolean containsVulgar;
	private boolean containsImage;
	private boolean analyzed = false;
	private int stars;
	private String length;
	private boolean persist;
	private double version;
	private String upload;
	private String update;
	private int objects;
	private long original;
	private int coins;
	private int levelVersion;


	public void setVideoInfo(String title, String ID, String channel, String thumbnail){
		this.videoTitle = title;
		this.videoURL = "https://www.youtube.com/watch?v" + ID;
		this.channelName = channel;
		this.thumbnailURL = thumbnail;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public void setAnalyzed() {
		this.analyzed = true;
	}

	public void setContainsVulgar() {
		this.containsVulgar = true;
	}

	public void setContainsImage() {
		this.containsImage = true;
	}

	//void setLevelData(ArrayList<GDObject> levelData) {
	//	this.levelData = levelData;
	//}
	public void setPersist(boolean persist){
		this.persist = persist;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public void setDownloads(String downloads) {
		this.downloads = downloads;
	}

	public void setLevelID(String levelID) {
		this.levelID = levelID;
	}

	public void setVersion(double version) {this.version = version; }
	//void setCoins(String coins) {
	//	this.coins = Integer.parseInt(coins);
	//}

	//void setVerifiedCoins(boolean verifiedCoins) {
	//	this.verifiedCoins = verifiedCoins;
	//}

	public void setFeatured() {
		this.featured = true;
	}

	public void setEpic(boolean epic) {
		this.epic = epic;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public void setSongAuthor(String songAuthor) {
		this.songAuthor = songAuthor;
	}

	public void setSongID(String songID) {
		this.songID = songID;
	}

	public void setSongURL(String downloadURL) {
		this.downloadURL = downloadURL;
    }

	public void setLength(String length) {
		this.length = length;
    }

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public void setObjects(int objects) {
		this.objects = objects;
	}

	public void setOriginal(long original) {
		this.original = original;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public void setLevelVersion(int levelVersion) {
		this.levelVersion = levelVersion;
	}

	//void setSongSize(String songSize) {
	//	this.songSize = songSize;
	//}
}
