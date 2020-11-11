package com.alphalaneous;

import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.Windows.DialogBox;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.auth.Scopes;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
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

	public static ArrayList<Comment> getGDComments(int page, boolean top, long ID) {
		String response = "";
		int tries = 0;
		while (tries < 5) {
			try {
				ByteBuf data = wrappedBuffer(StandardCharsets.UTF_8.encode("levelID=" + ID + "&page=" + page + "&secret=Wmfd2893gb7&gameVersion=21&binaryVersion=35&mode=" + (top ? 1 : 0)));
				HttpClient client = HttpClient.create()
						.baseUrl("http://www.boomlings.com/database")
						.headers(h -> {
							h.add("Content-Type", "application/x-www-form-urlencoded");
							h.add("Content-Length", data.readableBytes());
						});


				response = Objects.requireNonNull(client.post()
						.uri("/getGJComments21.php")
						.send(Mono.just(data))
						.responseSingle((responceHeader, responceBody) -> {
							if (responceHeader.status().equals(HttpResponseStatus.OK)) {
								return responceBody.asString().defaultIfEmpty("");
							} else {
								return Mono.error(new RuntimeException(responceHeader.status().toString()));
							}
						}).block());
				break;
			} catch (Exception ignored) {
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tries++;
		}
		int pages = (((Integer.parseInt(response.split("#")[1].split(":")[0]) - 1) / 10) + 1);
		if (page > pages) {
			return null;
		}
		response = response.split("#")[0].trim();
		String[] comments = response.split("\\|");
		ArrayList<Comment> commentsData = new ArrayList<>();
		for (String comment : comments) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				String[] comData = comment.split("~");
				String decoded = new String(Base64.getDecoder().decode(comData[1].replace("-", "+").replace("_", "/")));
				Comment commentA = new Comment(comData[14], decoded, comData[5], comData[9]);
				commentsData.add(commentA);
			} catch (Exception ignored) {
			}
		}
		return commentsData;
	}

	static ArrayList<String> allViewers = new ArrayList<>();

	static void setAllViewers() {
		try {
			URL url = new URL("https://tmi.twitch.tv/group/user/" + Settings.getSettings("channel").toLowerCase() + "/chatters");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String x;
			while ((x = br.readLine()) != null) {
				builder.append(x).append("\n");
			}
			JSONObject viewers = new JSONObject(builder.toString());
			String[] types = {"broadcaster", "vips", "staff", "moderators", "admins", "global_mods", "viewers"};
			allViewers.clear();
			for (String type : types) {
				JSONArray viewerList = viewers.getJSONObject("chatters").getJSONArray(type);
				for (int i = 0; i < viewerList.length(); i++) {
					String viewer = viewerList.get(i).toString().replaceAll("\"", "");
					allViewers.add(viewer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void getViewers() {
		new Thread(() -> {
			while (true) {
				try {
					URL url = new URL("https://tmi.twitch.tv/group/user/" + Settings.getSettings("channel").toLowerCase() + "/chatters");
					URLConnection conn = url.openConnection();
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					StringBuilder builder = new StringBuilder();
					String x;
					while ((x = br.readLine()) != null) {
						builder.append(x).append("\n");
					}
					JSONObject viewers =new JSONObject(builder.toString());
					String[] types = {"broadcaster", "vips", "staff", "moderators", "admins", "global_mods", "viewers"};
					for (int i = 0; i < Requests.levels.size(); i++) {
						LevelsPanel.getButton(i).setViewership(false);
					}

					for (String type : types) {
						if(viewers.get("chatters") != null) {
							JSONArray viewerList = viewers.getJSONObject("chatters").getJSONArray(type);
							for (int i = 0; i < viewerList.length(); i++) {
								String viewer = viewerList.get(i).toString().replaceAll("\"", "");
								for (int k = 0; k < Requests.levels.size(); k++) {
									if (LevelsPanel.getButton(k).getRequester().equalsIgnoreCase(viewer)) {
										LevelsPanel.getButton(k).setViewership(true);
									}
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
		}).start();
	}

	static boolean isNotFollowing(String user) {

		try {

			JSONObject isFollowing = twitchAPI("https://api.twitch.tv/helix/users/follows?from_id=" + getIDs(user) + "&to_id=" + getIDs(Settings.getSettings("channel")));
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
		} catch (Exception e) {
			DialogBox.showDialogBox("Error!", e.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			return true;
		}
	}

	public static String fetchURL(String url) {
		StringBuilder response = new StringBuilder();
		try {
			URL ids = new URL(url);
			Scanner s = new Scanner(ids.openStream());
			while (s.hasNextLine()) {
				response.append(s.nextLine()).append(" ");
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	public static long getFollowerCount() {
		JSONObject followCountJson;
		followCountJson = twitchAPI("https://api.twitch.tv/helix/users/follows?to_id=" + getIDs(Settings.getSettings("channel")));
		assert followCountJson != null;
		String total = followCountJson.get("total").toString();
		return Long.parseLong(total);
	}

	public static String getChannel() {

			JSONObject nameObj = twitchAPI("https://api.twitch.tv/helix/users");
			assert nameObj != null;
			return nameObj.getJSONArray("data").getJSONObject(0).get("display_name").toString().replaceAll("\"", "");

	}

	public static String getPFP() {

			JSONObject nameObj = twitchAPI("https://api.twitch.tv/helix/users");
			assert nameObj != null;

			//String url = String.valueOf(nameObj.asObject().get("data").asArray().get(0).asObject().get("profile_image_url")).replaceAll("\"", "");

		return nameObj.getJSONArray("data").getJSONObject(0).get("profile_image_url").toString().replaceAll("\"", "");

	}


	private static JSONObject twitchAPI(String URL) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(URL)
				.build();
		Request newReq = request.newBuilder()
				.addHeader("Client-ID", "fzwze6vc6d2f7qodgkpq2w8nnsz3rl")
				.addHeader("Authorization", "Bearer " + Settings.getSettings("oauth"))
				.build();
		try (Response response = client.newCall(newReq).execute()) {
			assert response.body() != null;
			return new JSONObject(response.body().string());

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

	private static JSONObject twitchAPI(String URL, boolean v5) {

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
			assert response.body() != null;
			return new JSONObject(response.body().string());

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
		JSONObject userID = twitchAPI("https://api.twitch.tv/helix/users?login=" + username.toLowerCase());
		assert userID != null;
		return userID.getJSONArray("data").getJSONObject(0).get("id").toString().replaceAll("\"", "");
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
			return new JSONObject(x).get("client_id").toString().replace("\"", "");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	static AtomicBoolean success = new AtomicBoolean(false);

	public static void setOauth(boolean threaded) {
		if (!threaded) {
			setOauthPrivate(true);
		} else {
			Thread thread = new Thread(() -> {
				setOauthPrivate(true);
			});
			thread.start();
		}
	}

	public static void setOauth() {
		Thread thread = new Thread(() -> setOauthPrivate(true));
		thread.start();
	}
	public static void setOauthNR() {
		Thread thread = new Thread(() -> setOauthPrivate(false));
		thread.start();
	}

	private static void setOauthPrivate(boolean refresh) {
		success.set(false);
		try {
			Twitch twitch = new Twitch();
			URI callbackUri = new URI("http://127.0.0.1:23522");

			twitch.setClientId("fzwze6vc6d2f7qodgkpq2w8nnsz3rl");
			URI authUrl = new URI(twitch.auth().getAuthenticationUrl(
					twitch.getClientId(), callbackUri, Scopes.USER_READ
			) + "chat:edit+channel:moderate+channel:read:redemptions+channel:read:subscriptions+chat:read+whispers:read+whispers:edit+user_read&force_verify=true");
			Runtime rt = Runtime.getRuntime();
			rt.exec("rundll32 url.dll,FileProtocolHandler " + authUrl);
			if (twitch.auth().awaitAccessToken()) {
				Settings.writeSettings("oauth", twitch.auth().getAccessToken());
				if(refresh) {
					Main.refreshBot();
				}
				success.set(true);
			} else {
				System.out.println(twitch.auth().getAuthenticationError());

			}
			if(!GDBoardBot.initialConnect){
				GDBoardBot.initialConnect = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
