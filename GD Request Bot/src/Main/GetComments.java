package Main;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

class GetComments {
	private ArrayList<ArrayList<String>> Comments = new ArrayList<>();
	private ArrayList<String> commentContent = new ArrayList<>();
	private ArrayList<String> commenters = new ArrayList<>();

	ArrayList<ArrayList<String>> getComments(String levelID) throws IOException {
		System.out.println("Here");
		URL gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?top");
		URLConnection con = gdAPI.openConnection();
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String message = "{\"Comments\" : " + IOUtils.toString(br) + "}";
		JSONObject obj = new JSONObject(message);
		JSONArray arr = obj.getJSONArray("Comments");
		for (int i = 0; i < arr.length(); i++) {
			commentContent.add(arr.getJSONObject(i).getString("content"));
			Comments.add(commentContent);
			commenters.add(arr.getJSONObject(i).getString("username"));
			Comments.add(commenters);

		}
		return Comments;

	}
}
