package Main;

public class Comment {

	public String getUsername() {
		return String.valueOf(username);
	}

	public String getComment() {
		return String.valueOf(comment);
	}

	public String getLikes() {
		return String.valueOf(likes);
	}
	public String getPercent(){
		return String.valueOf(percent);
	}

	private String username;
	private String comment;
	private String likes;
	private String percent;

	Comment(String username, String comment, String likes, String percent){
		this.username = username;
		this.comment = comment;
		this.likes= likes;
		this.percent = percent;
	}
}
