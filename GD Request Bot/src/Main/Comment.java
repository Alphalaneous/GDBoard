package Main;

public class Comment {

	public String getUsername() {
		return username;
	}

	public String getComment() {
		return comment;
	}

	public String getLikes() {
		return likes;
	}
	public String getPercent(){
		return percent;
	}

	String username;
	String comment;
	String likes;
	String percent;

	Comment(String username, String comment, String likes, String percent){
		this.username = username;
		this.comment = comment;
		this.likes= likes;
		this.percent = percent;
	}
}
