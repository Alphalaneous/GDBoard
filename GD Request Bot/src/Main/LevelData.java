package Main;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/** @noinspection WeakerAccess*/
public class LevelData {



	public int getPassword() {
		return password;
	}

	public boolean getViewership() {
		return viewership;
	}

	public StringBuilder getRequester() {
		return requester;
	}

	public StringBuilder getAuthor() {
		return author;
	}

	public StringBuilder getName() {
		return name;
	}

	public StringBuilder getDifficulty() {
		return difficulty;
	}

	public StringBuilder getDescription() {
		return description;
	}

	public long getLikes() {
		return likes;
	}

	public long getDownloads() {
		return downloads;
	}

	public long getLevelID() {
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

	public StringBuilder getSongName() {
		return songName;
	}

	public StringBuilder getSongAuthor() {
		return songAuthor;
	}

	public int getSongID() {
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

	public boolean getVerifiedCoins(){
		return verifiedCoins;
	}

	public URL getSongURL() throws MalformedURLException {
		return new URL(downloadURL.toString());
	}

	public StringBuilder getLength(){
		return length;
	}

	public boolean getNotPersist(){
		return !persist;
	}

	public int getVersion() { return version; }

	public StringBuilder getVideoTitle() {return videoTitle;}

	public StringBuilder getVideoURL() { return videoURL;}

	public StringBuilder getChannelName() { return channelName;}

	public StringBuilder getThumbnailURL() { return thumbnailURL; }

	public StringBuilder getUpload() {
		return upload;
	}

	public StringBuilder getUpdate() {
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

	public ImageIcon getPlayerIcon() {
		return playerIcon;
	}

	private StringBuilder requester;
	private int password;
	private StringBuilder author;
	private StringBuilder name;
	private StringBuilder difficulty;
	private StringBuilder description;
	private long likes;
	private StringBuilder channelName;
	private StringBuilder thumbnailURL;
	private boolean viewership = true;
	private boolean verifiedCoins = false;
	private long downloads;
	private long levelID;
	private StringBuilder videoTitle;
	private StringBuilder videoURL;;
	private boolean featured;
	private boolean epic;
	private StringBuilder downloadURL;
	private StringBuilder songName;
	private StringBuilder songAuthor;
	private int songID;
	private boolean containsVulgar;
	private boolean containsImage;
	private boolean analyzed = false;
	private int stars;
	private StringBuilder length;
	private boolean persist;
	private int version;
	private StringBuilder upload;
	private StringBuilder update;
	private int objects;
	private long original;
	private int coins;
	private int levelVersion;
	private ImageIcon playerIcon;


	public void setVideoInfo(StringBuilder title, StringBuilder ID, StringBuilder channel, StringBuilder thumbnail){
		this.videoTitle = title;
		this.videoURL = new StringBuilder("https://www.youtube.com/watch?v").append(ID);
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
		this.requester = new StringBuilder(requester);
	}

	public void setAuthor(String author) {
		this.author = new StringBuilder(author);
	}

	public void setName(String name) {
		this.name = new StringBuilder(name);
	}

	public void setPassword(int password) {
		this.password = password;
	}


	public void setDifficulty(String difficulty) {
		this.difficulty = new StringBuilder(difficulty);
	}

	public void setDescription(String description) {
		this.description = new StringBuilder(description);
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}

	public void setLevelID(long levelID) {
		this.levelID = levelID;
	}

	public void setVeririedCoins(boolean verifiedCoins){
		this.verifiedCoins = verifiedCoins;
	}

	public void setVersion(int version) {this.version = version; }
	//void setCoins(String coins) {
	//	this.coins = Integer.parseInt(coins);
	//}

	//void setVerifiedCoins(boolean verifiedCoins) {
	//	this.verifiedCoins = verifiedCoins;
	//}
	public void setViewership(boolean viewership) {
		this.viewership = viewership;
	}

	public void setFeatured() {
		this.featured = true;
	}

	public void setEpic(boolean epic) {
		this.epic = epic;
	}

	public void setSongName(String songName) {
		this.songName = new StringBuilder(songName);
	}

	public void setSongAuthor(String songAuthor) {
		this.songAuthor = new StringBuilder(songAuthor);
	}

	public void setSongID(int songID) {
		this.songID = songID;
	}

	public void setSongURL(String downloadURL) {
		this.downloadURL = new StringBuilder(downloadURL);
    }

	public void setLength(String length) {
		this.length = new StringBuilder(length);
    }

	public void setUpload(String upload) {
		this.upload = new StringBuilder(upload);
	}

	public void setUpdate(String update) {
		this.update = new StringBuilder(update);
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

	public void setPlayerIcon(ImageIcon playerIcon) {
		this.playerIcon = playerIcon;
	}

	//void setSongSize(String songSize) {
	//	this.songSize = songSize;
	//}
}
