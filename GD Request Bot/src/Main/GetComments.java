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
    private ArrayList<String> percent = new ArrayList<>();
    private ArrayList<String> likes = new ArrayList<>();

    ArrayList<ArrayList<String>> getComments(String levelID, int page, boolean top) throws IOException {
        URL gdAPI;
        if (top) {
            gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?page=" + page + "&top");
        } else {
            gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?page=" + page);
        }
        URLConnection con = gdAPI.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String message = "{\"Comments\" : " + IOUtils.toString(br) + "}";
        JSONObject obj = new JSONObject(message);
        JSONArray arr;
        try {
            arr = obj.getJSONArray("Comments");

            assert arr != null;
            for (int i = 0; i < arr.length(); i++) {
                try {
                    commentContent.add(StringEscapeUtils.unescapeHtml4(arr.getJSONObject(i).getString("content")));
                    Comments.add(commentContent);
                    commenters.add(arr.getJSONObject(i).getString("username"));
                    Comments.add(commenters);
                    try {
                        percent.add(StringEscapeUtils.unescapeHtml4(arr.getJSONObject(i).getString("percent")));
                    } catch (Exception e) {
                        percent.add("0");
                    }
                    Comments.add(percent);
                    likes.add(arr.getJSONObject(i).getString("likes"));
                    Comments.add(likes);
                } catch (Exception ignored) {
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        return Comments;
    }
}
