package Main;

import SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Json.JsonObject;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

public class TwitchAPI {

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
        JsonObject nameObj = twitchAPI("https://api.twitch.tv/kraken/channel", true);
        assert nameObj != null;
        return String.valueOf(nameObj.get("name")).replaceAll("\"", "");
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

    private static String getIDs(String username) {
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
                ) + "chat:edit+chat:read+whispers:read+whispers:edit+channel_read&force_verify=true");
                Runtime rt = Runtime.getRuntime();
                rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);
                if (twitch.auth().awaitAccessToken()) {
                    Settings.setOAuth(twitch.auth().getAccessToken());
                    Settings.writeSettings("oauth", twitch.auth().getAccessToken());
                    String channel = TwitchAPI.getChannel();
                    Settings.channel = channel;
                    Settings.setChannel(channel);
                    Settings.writeSettings("channel", channel);
                    AccountSettings.refreshChannel();
                    success.set(true);
                    try {
                        GDBoardBot.start();
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
