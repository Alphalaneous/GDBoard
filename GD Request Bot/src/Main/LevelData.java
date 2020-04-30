package Main;

class LevelData {




	String getRequester() {
		return requester;
	}

	String getAuthor() {
		return author;
	}

	String getName() {
		return name;
	}

	String getDifficulty() {
		return difficulty;
	}

	String getDescription() {
		return description;
	}

	String getLikes() {
		return likes;
	}

	String getDownloads() {
		return downloads;
	}

	String getLevelID() {
		return levelID;
	}

	//int getCoins() {
	//	return coins;
	//}

	//boolean getVerifiedCoins() {
	//	return verifiedCoins;
	//}

	boolean getFeatured() {
		return featured;
	}

	boolean getEpic() {
		return epic;
	}

	String getSongName() {
		return songName;
	}

	String getSongAuthor() {
		return songAuthor;
	}

	String getSongID() {
		return songID;
	}

	//String getSongSize() {
	//	return songSize;
	//}
	
	//ArrayList<GDObject> getLevelData(){
	//	return levelData;
	//}
	
	boolean getContainsVulgar() {
		return containsVulgar;
	}
	
	boolean getContainsImage() {
		return containsImage;
	}
	
	boolean getAnalyzed() {
		return analyzed;
	}
	
	int getStars() {
		return stars;
	}

	String getSongURL(){
		return downloadURL;
	}

	String getLength(){
		return length;
	}

	boolean getNotPersist(){
		return !persist;
	}

	double getVersion() { return version; }

	String getVideoTitle() {return videoTitle;}

	String getVideoURL() { return videoURL;}

	String getChannelName() { return channelName;}

	String getThumbnailURL() { return thumbnailURL; }

	private String requester;
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

	void setVideoInfo(String title, String ID, String channel, String thumbnail){
		this.videoTitle = title;
		this.videoURL = "https://www.youtube.com/watch?v" + ID;
		this.channelName = channel;
		this.thumbnailURL = thumbnail;
	}

	void setStars(int stars) {
		this.stars = stars;
	}
	
	void setAnalyzed() {
		this.analyzed = true;
	}
	
	void setContainsVulgar() {
		this.containsVulgar = true;
	}
	
	void setContainsImage() {
		this.containsImage = true;
	}

	//void setLevelData(ArrayList<GDObject> levelData) {
	//	this.levelData = levelData;
	//}
	void setPersist(boolean persist){
		this.persist = persist;
	}

	void setRequester(String requester) {
		this.requester = requester;
	}

	void setAuthor(String author) {
		this.author = author;
	}

	void setName(String name) {
		this.name = name;
	}

	void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	void setDescription(String description) {
		this.description = description;
	}

	void setLikes(String likes) {
		this.likes = likes;
	}

	void setDownloads(String downloads) {
		this.downloads = downloads;
	}

	void setLevelID(String levelID) {
		this.levelID = levelID;
	}

	void setVersion(double version) {this.version = version; }
	//void setCoins(String coins) {
	//	this.coins = Integer.parseInt(coins);
	//}

	//void setVerifiedCoins(boolean verifiedCoins) {
	//	this.verifiedCoins = verifiedCoins;
	//}

	void setFeatured() {
		this.featured = true;
	}

	void setEpic(boolean epic) {
		this.epic = epic;
	}

	void setSongName(String songName) {
		this.songName = songName;
	}

	void setSongAuthor(String songAuthor) {
		this.songAuthor = songAuthor;
	}

	void setSongID(String songID) {
		this.songID = songID;
	}

    void setSongURL(String downloadURL) {
		this.downloadURL = downloadURL;
    }

    void setLength(String length) {
		this.length = length;
    }

    //void setSongSize(String songSize) {
	//	this.songSize = songSize;
	//}
}
