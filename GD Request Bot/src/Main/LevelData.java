package Main;

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

	public String getSongURL(){
		return downloadURL;
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
	//private int coins;
	//private boolean verifiedCoins;
	private boolean featured;
	private boolean epic;
	private String downloadURL;

	private String songName;
	private String songAuthor;
	private String songID;
	//private String songSize;
	//private ArrayList<GDObject> levelData;
	private boolean containsVulgar;
	private boolean containsImage;
	private boolean analyzed = false;
	private int stars;
	private String length;
	private boolean persist;
	private double version;

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

    //void setSongSize(String songSize) {
	//	this.songSize = songSize;
	//}
}
