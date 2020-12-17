package com.alphalaneous;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @noinspection WeakerAccess
 */
public class LevelData {

	private String requester;
	private int password;
	private String message;
	private String messageID;
	private String author;
	private String name;
	private String difficulty;
	private String description;
	private long likes;
	private boolean viewership = true;
	private boolean verifiedCoins = false;
	private long downloads;
	private long levelID;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public int getPassword() {
		return password;
	}

	public void setPassword(int password) {
		this.password = password;
	}

	public boolean getViewership() {
		return viewership;
	}

	public void setViewership(boolean viewership) {
		this.viewership = viewership;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getLikes() {
		return likes;
	}

	public void setLikes(long likes) {
		this.likes = likes;
	}

	public long getDownloads() {
		return downloads;
	}

	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}

	public long getLevelID() {
		return levelID;
	}

	public void setLevelID(long levelID) {
		this.levelID = levelID;
	}

	public boolean getFeatured() {
		return featured;
	}

	public boolean getEpic() {
		return epic;
	}

	public void setEpic(boolean epic) {
		this.epic = epic;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSongAuthor() {
		return songAuthor;
	}

	public void setSongAuthor(String songAuthor) {
		this.songAuthor = songAuthor;
	}

	public int getSongID() {
		return songID;
	}

	public void setSongID(int songID) {
		this.songID = songID;
	}

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

	public void setStars(int stars) {
		this.stars = stars;
	}

	public boolean getVerifiedCoins() {
		return verifiedCoins;
	}

	public URL getSongURL() throws MalformedURLException {
		return new URL(downloadURL);
	}

	public void setSongURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean getNotPersist() {
		return !persist;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public int getObjects() {
		return objects;
	}

	public void setObjects(int objects) {
		this.objects = objects;
	}

	public long getOriginal() {
		return original;
	}

	public void setOriginal(long original) {
		this.original = original;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getLevelVersion() {
		return levelVersion;
	}

	public void setLevelVersion(int levelVersion) {
		this.levelVersion = levelVersion;
	}

	public ImageIcon getPlayerIcon() {
		return playerIcon;
	}

	public void setPlayerIcon(ImageIcon playerIcon) {
		this.playerIcon = playerIcon;
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

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public void setVeririedCoins(boolean verifiedCoins) {
		this.verifiedCoins = verifiedCoins;
	}

	public void setFeatured() {
		this.featured = true;
	}
}
