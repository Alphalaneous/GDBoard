package com.alphalaneous;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/** @noinspection WeakerAccess*/
public class LevelData {

	public String getMessage(){
		return message;
	}

	public String getMessageID(){
		return messageID;
	}

	public int getPassword() {
		return password;
	}

	public boolean getViewership() {
		return viewership;
	}

	public String  getRequester() {
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

	public String getSongName() {
		return songName;
	}

	public String getSongAuthor() {
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

	public String getLength(){
		return length;
	}

	public boolean getNotPersist(){
		return !persist;
	}

	public int getVersion() { return version; }

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

	public ImageIcon getPlayerIcon() {
		return playerIcon;
	}

	private String requester;
	private int password;
	private String message;
	private String messageID;
	private String author;
	private String name;
	private String difficulty;
	private String description;
	private long likes;
	private String channelName;
	private String thumbnailURL;
	private boolean viewership = true;
	private boolean verifiedCoins = false;
	private long downloads;
	private long levelID;
	private String videoTitle;
	private String videoURL;
	private boolean featured;
	private boolean epic;
	private String downloadURL;
	private String songName;
	private String songAuthor;
	private int songID;
	private boolean containsVulgar;
	private boolean containsImage;
	private boolean analyzed = false;
	private int stars;
	private String length;
	private boolean persist;
	private int version;
	private String upload;
	private String update;
	private int objects;
	private long original;
	private int coins;
	private int levelVersion;
	private ImageIcon playerIcon;


	public void setMessage(String message){
		this.message = message;
	}

	public void setMessageID(String messageID){
		this.messageID = messageID;
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
		this.requester = new String(requester);
	}

	public void setAuthor(String author) {
		this.author = new String(author);
	}

	public void setName(String name) {
		this.name = new String(name);
	}

	public void setPassword(int password) {
		this.password = password;
	}


	public void setDifficulty(String difficulty) {
		this.difficulty = new String(difficulty);
	}

	public void setDescription(String description) {
		this.description = new String(description);
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
		this.songName = new String(songName);
	}

	public void setSongAuthor(String songAuthor) {
		this.songAuthor = new String(songAuthor);
	}

	public void setSongID(int songID) {
		this.songID = songID;
	}

	public void setSongURL(String downloadURL) {
		this.downloadURL = new String(downloadURL);
    }

	public void setLength(String length) {
		this.length = new String(length);
    }

	public void setUpload(String upload) {
		this.upload = new String(upload);
	}

	public void setUpdate(String update) {
		this.update = new String(update);
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
