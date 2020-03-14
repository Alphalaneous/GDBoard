package Main;

import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TwitchAPI {

    public static boolean isNotFollowing(User user){


        JsonObject isFollowing = twitchAPI("https://api.twitch.tv/helix/users/follows?from_id=" + getIDs(user.toString()) + "&to_id=" + getIDs(Settings.channel.toLowerCase()));
        if(isFollowing != null) {
            String str = isFollowing.get("total").toString();
            System.out.println(str);
            return !str.equalsIgnoreCase("1");
        }
        else {
            return true;
        }
    }
    public static JsonObject twitchAPI(String URL){
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
            conn.setRequestProperty("Authorization", "Bearer " + Settings.oauth);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String x = br.readLine();
            System.out.println(x);
            return JsonObject.readFrom(x);
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public static String getIDs(String username){
        JsonObject userID = twitchAPI("https://api.twitch.tv/helix/users?login=" + username.toLowerCase());
        assert userID != null;
        return userID.get("data").asArray().get(0).asObject().get("id").toString().replaceAll("\"", "");
    }
    public static String getClientID(){
        try {
            URL url = new URL("https://id.twitch.tv/oauth2/validate");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "OAuth " + Settings.oauth);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String x = br.readLine();
            return JsonObject.readFrom(x).get("client_id").toString().replace("\"", "");
        }
        catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
