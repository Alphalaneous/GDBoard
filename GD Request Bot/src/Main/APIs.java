package Main;

import SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Json.JsonObject;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class APIs {

    static String[] getYTVideo(String query){
        JsonObject videoInfo;
        String[] info = new String[0];
        URL url;
        try {
            url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&order=relevance&videoDefinition=high&maxResults=1&q=id " + query + "&type=video&key=AIzaSyBUa-HlZLDWBBtvT-fxfJvFxW9Dtw-3Q68");
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String x;
        StringBuilder y = new StringBuilder();
        while((x = br.readLine()) != null) {
            y.append("\n").append(x);
        }
        System.out.println(y);
        videoInfo = JsonObject.readFrom(y.toString());
        String videoTitle = videoInfo.asObject().get("items").asArray().get(0).asObject().get("snippet").asObject().get("title").asString();
        String videoID = videoInfo.asObject().get("items").asArray().get(0).asObject().get("id").asObject().get("videoId").asString();
        String channelTitle = videoInfo.asObject().get("items").asArray().get(0).asObject().get("snippet").asObject().get("channelTitle").asString();
        String thumbnailURL = videoInfo.asObject().get("items").asArray().get(0).asObject().get("snippet").asObject().get("thumbnails").asObject().get("high").asObject().get("url").asString();
            info = new String[]{videoTitle, videoID, channelTitle, thumbnailURL};
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    static boolean isNotFollowing(String user) {


        JsonObject isFollowing = null;
        try {
            isFollowing = twitchAPI("https://api.twitch.tv/helix/users/follows?from_id=" + getIDs(user) + "&to_id=" + getIDs(Settings.getSettings("channel")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isFollowing != null) {
            String str = isFollowing.get("total").toString();
            System.out.println(str);
            return !str.equalsIgnoreCase("1");
        } else {
            return true;
        }
    }

    static String getChannel() {
        JsonObject nameObj = twitchAPI("https://api.twitch.tv/kraken/user", true);
        assert nameObj != null;
        return String.valueOf(nameObj.get("display_name")).replaceAll("\"", "");
    }

    private static JsonObject twitchAPI(String URL) {
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
            conn.setRequestProperty("Authorization", "Bearer " + Settings.oauth);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String x = br.readLine();
            return JsonObject.readFrom(x);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JsonObject twitchAPI(String URL, boolean v5) {
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
            conn.setRequestProperty("Authorization", "OAuth " + Settings.oauth);
            if (v5) {
                conn.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String x = br.readLine();
            return JsonObject.readFrom(x);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getIDs(String username) {
        JsonObject userID = twitchAPI("https://api.twitch.tv/helix/users?login=" + username.toLowerCase());
        assert userID != null;
        return userID.get("data").asArray().get(0).asObject().get("id").toString().replaceAll("\"", "");
    }

    public static String getClientID() {
        try {
            URL url = new URL("https://id.twitch.tv/oauth2/validate");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "OAuth " + Settings.oauth);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String x = br.readLine();
            return JsonObject.readFrom(x).get("client_id").toString().replace("\"", "");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    static AtomicBoolean success = new AtomicBoolean(false);
    public static void setOauth() {

        Thread thread = new Thread(() -> {
            try {
                Twitch twitch = new Twitch();
                URI callbackUri = new URI("http://127.0.0.1:23522");

                twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
                URI authUrl = new URI(twitch.auth().getAuthenticationUrl(
                        twitch.getClientId(), callbackUri, Scopes.USER_READ
                ) + "chat:edit+chat:read+whispers:read+whispers:edit+user_read&force_verify=true");
                Runtime rt = Runtime.getRuntime();
                rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);
                if (twitch.auth().awaitAccessToken()) {
                    Settings.oauth = twitch.auth().getAccessToken();
                    Settings.writeSettings("oauth", twitch.auth().getAccessToken());
                    String channel = APIs.getChannel();
                    Settings.channel = channel;
                    Settings.writeSettings("channel", channel);
                    AccountSettings.refreshChannel(channel);
                    success.set(true);
                    try {
                        GDBoardBot.restart();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(twitch.auth().getAuthenticationError());

                }
            } catch (Exception ignored) {
            }

        });
        thread.start();
    }
}
