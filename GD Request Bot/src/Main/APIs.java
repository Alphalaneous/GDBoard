package Main;

import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.AccountSettings;
import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
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
import java.util.Map;
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

	public static ArrayList<Comment> getGDComments(int page, boolean top, long ID) throws IOException {
		ByteBuf data = wrappedBuffer(StandardCharsets.UTF_8.encode("levelID=" + ID + "&page=" + page + "&secret=Wmfd2893gb7&gameVersion=21&binaryVersion=35&mode=" + (top ? 1 : 0)));
		HttpClient client = HttpClient.create()
				.baseUrl("http://www.boomlings.com/database")
				.headers(h -> {
					h.add("Content-Type", "application/x-www-form-urlencoded");
					h.add("Content-Length", data.readableBytes());
				});
		StringBuilder responce = new StringBuilder(client.post()
				.uri("/getGJComments21.php")
				.send(Mono.just(data))
				.responseSingle((responceHeader, responceBody) -> {
					if(responceHeader.status().equals(HttpResponseStatus.OK)){
						return responceBody.asString().defaultIfEmpty("");
					}
					else{
						return Mono.error(new RuntimeException(responceHeader.status().toString()));
					}
				}).block());
		String[] comments = responce.toString().split("\\|");

		ArrayList<Comment> commentsData = new ArrayList<>();

		for(String comment : comments){
			String[] comData = comment.split("~");
			StringBuilder decoded = new StringBuilder(new String (Base64.getDecoder().decode(comData[1].replace("-", "+").replace("_", "/"))));
			Comment commentA = new Comment(new StringBuilder(comData[14]), decoded, new StringBuilder(comData[5]), new StringBuilder(comData[9]));

			commentsData.add(commentA);
		}
		responce = null;
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

							for(int k = 0; k < Requests.levels.size(); k++) {
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
			if (user.equalsIgnoreCase(Settings.getSettings("channel"))) {
				return false;
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
			DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			return true;
		}
	}

	public static String getChannel() {
		try {
			JsonObject nameObj = twitchAPI("https://api.twitch.tv/helix/users");
			return String.valueOf(nameObj.asObject().get("data").asArray().get(0).asObject().get("display_name")).replaceAll("\"", "");
		}
		catch (Exception e){
			DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			return "error";
		}
	}

	public static String getPFP() {
		try {
			JsonObject nameObj = twitchAPI("https://api.twitch.tv/helix/users");
			return String.valueOf(nameObj.asObject().get("data").asArray().get(0).asObject().get("profile_image_url")).replaceAll("\"", "");
		}
		catch (Exception e){
			DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			return "error";
		}
	}


	private static JsonObject twitchAPI(String URL) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(URL)
				.build();
		Request newReq = request.newBuilder()
				.addHeader("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl")
				.addHeader("Authorization", "Bearer " + Settings.getSettings("oauth"))
				.build();
		try (Response response = client.newCall(newReq).execute()) {
			return JsonObject.readFrom(response.body().string());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		/*try {
			URL url = new URL(URL);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setRequestProperty("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
			conn.setRequestProperty("Authorization", "Bearer " + Settings.oauth);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1);
			String x = br.readLine();
			System.out.println(x);
			return JsonObject.readFrom(x);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}*/
	}

	private static JsonObject twitchAPI(String URL, boolean v5) throws IOException {

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(URL)
				.build();
		Request newReq = request.newBuilder()
				.addHeader("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl")
				.addHeader("Authorization", "OAuth " + Settings.getSettings("oauth"))
				.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0")
				.build();

		try (Response response = client.newCall(newReq).execute()) {
			return JsonObject.readFrom(response.body().string());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		/*try {
			URL url = new URL(URL);
			HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
			conn.setRequestProperty("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
			conn.setRequestProperty("Authorization", "OAuth " + Settings.oauth);
			if (v5) {
				conn.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()), 1);
			String x = br.readLine();
			return JsonObject.readFrom(x);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}*/
	}

	public static String getIDs(String username) {
		JsonObject userID = null;
		try {
			userID = twitchAPI("https://api.twitch.tv/helix/users?login=" + username.toLowerCase());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(userID.toString());
		assert userID != null;
		return userID.get("data").asArray().get(0).asObject().get("id").toString().replaceAll("\"", "");
	}
	@SuppressWarnings("unused")
	public static String getClientID() {
		try {
			URL url = new URL("https://id.twitch.tv/oauth2/validate");
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Authorization", "OAuth " + Settings.getSettings("oauth"));
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String x = br.readLine();
			return JsonObject.readFrom(x).get("client_id").toString().replace("\"", "");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	static AtomicBoolean success = new AtomicBoolean(false);
	public static void setOauth(boolean threaded) {
		if(!threaded){
			setOauthPrivate();
		}
		else {
			Thread thread = new Thread(() -> {
				setOauthPrivate();
			});
			thread.start();
		}
	}
	public static void setOauth(){
		Thread thread = new Thread(() -> {
			setOauthPrivate();
		});
		thread.start();
	}
	private static void setOauthPrivate(){
		success.set(false);
		try {
			Twitch twitch = new Twitch();
			URI callbackUri = new URI("http://127.0.0.1:23522");

			twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
			URI authUrl = new URI(twitch.auth().getAuthenticationUrl(
					twitch.getClientId(), callbackUri, Scopes.USER_READ
			) + "chat:edit+channel:moderate+channel:read:redemptions+chat:read+whispers:read+whispers:edit+user_read&force_verify=true");
			Runtime rt = Runtime.getRuntime();
			rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);
			if (twitch.auth().awaitAccessToken()) {
				Settings.writeSettings("oauth", twitch.auth().getAccessToken());
				success.set(true);
			} else {
				System.out.println(twitch.auth().getAuthenticationError());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
