package Main;

import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import java.nio.charset.StandardCharsets;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class APIs {
/*
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
    }*/

	public static ArrayList<Comment> getGDComments(int page, boolean top, String ID) throws IOException {
		ByteBuf data = wrappedBuffer(StandardCharsets.UTF_8.encode("levelID=" + ID + "&page=" + page + "&secret=Wmfd2893gb7&gameVersion=21&binaryVersion=35&mode=" + (top ? 1 : 0)));
		HttpClient client = HttpClient.create()
				.baseUrl("http://www.boomlings.com/database")
				.headers(h -> {
					h.add("Content-Type", "application/x-www-form-urlencoded");
					h.add("Content-Length", data.readableBytes());
				});
		String responce = client.post()
				.uri("/getGJComments21.php")
				.send(Mono.just(data))
				.responseSingle((responceHeader, responceBody) -> {
					if(responceHeader.status().equals(HttpResponseStatus.OK)){
						return responceBody.asString().defaultIfEmpty("");
					}
					else{
						return Mono.error(new RuntimeException(responceHeader.status().toString()));
					}
				}).block();
		String[] comments = responce.split("\\|");

		ArrayList<Comment> commentsData = new ArrayList<>();

		for(String comment : comments){
			String[] comData = comment.split("~");
			String decoded = new String(Base64.getDecoder().decode(comData[1].replace("-", "+").replace("_", "/")));
			Comment commentA = new Comment(comData[14], decoded, comData[5], comData[9]);

			commentsData.add(commentA);
			System.out.println(comment);
		}
		return commentsData;
	}
	static void getViewers(){
		Thread thread = new Thread(() -> {
			while(true) {
				try {
					URL url = new URL("https://tmi.twitch.tv/group/user/" + Settings.getSettings("channel").toLowerCase() + "/chatters");
					URLConnection conn = url.openConnection();
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder builder = new StringBuilder();
					String x;
					while ((x = br.readLine()) != null) {
						builder.append(x).append("\n");
					}
					JsonObject viewers = JsonObject.readFrom(builder.toString());
					String[] types = {"broadcaster", "vips", "staff", "moderators", "admins", "global_mods", "viewers"};
					for(int i = 0; i < Requests.levels.size(); i++){
						LevelsWindow.getButton(i).setViewership(false);
					}
					for (String type : types) {
						JsonArray viewerList = viewers.get("chatters").asObject().get(type).asArray();
						for (int i = 0; i < viewerList.size(); i++) {
							String viewer = viewerList.get(i).asString().replaceAll("\"", "");
							System.out.println(viewer);

							for(int k = 0; k < Requests.levels.size(); k++) {
								System.out.println("R: " + LevelsWindow.getButton(k).getRequester());
								if (LevelsWindow.getButton(k).getRequester().equalsIgnoreCase(viewer)){
									LevelsWindow.getButton(k).setViewership(true);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(120000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	static boolean isNotFollowing(String user) {

		try {
			JsonObject isFollowing = null;
			try {
				isFollowing = twitchAPI("https://api.twitch.tv/helix/users/follows?from_id=" + getIDs(user) + "&to_id=" + getIDs(Settings.getSettings("channel")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (user.equalsIgnoreCase(Settings.getSettings("channel"))) {
					return false;
				}
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
		catch (Exception e){
			JOptionPane.showMessageDialog(Overlay.frame, "If this pops up turn followers only off and report it to Alphalaneous please", "Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}
	}

	private static String getChannel() {
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

	private static String getIDs(String username) {
		JsonObject userID = twitchAPI("https://api.twitch.tv/helix/users?login=" + username.toLowerCase());
		assert userID != null;
		return userID.get("data").asArray().get(0).asObject().get("id").toString().replaceAll("\"", "");
	}
	@SuppressWarnings("unused")
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
					String channel = "";
							channel = APIs.getChannel();
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
