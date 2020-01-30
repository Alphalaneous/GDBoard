package Main;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
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

	ArrayList<ArrayList<String>> getComments(String levelID, int page, boolean top) throws IOException {
		System.out.println("Here");
		URL gdAPI = null;
		if(top) {
			gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?page=" + page + "&top");
			System.out.println("top");
		}else{
			gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?page=" + page);
		}
		System.out.println("Page: " + page);
		URLConnection con = gdAPI.openConnection();
		InputStream is = con.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String message = "{\"Comments\" : " + IOUtils.toString(br) + "}";
		JSONObject obj = new JSONObject(message);
		JSONArray arr = obj.getJSONArray("Comments");
		for (int i = 0; i < 10; i++) {
			try {
				commentContent.add(StringEscapeUtils.unescapeHtml4(arr.getJSONObject(i).getString("content")));
				//System.out.println(commentContent.get(i));
				Comments.add(commentContent);
				commenters.add(arr.getJSONObject(i).getString("username"));
				Comments.add(commenters);
			}
			catch (Exception e){
				break;
			}
		}
		return Comments;
	}
}
