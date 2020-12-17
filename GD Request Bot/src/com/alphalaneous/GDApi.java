package com.alphalaneous;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class GDApi {


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

	public static void getGDLevel(long ID) {
		String response = "";
		int tries = 0;
		while (tries < 5) {
			try {
				ByteBuf data = wrappedBuffer(StandardCharsets.UTF_8.encode("levelID=" + ID + "&secret=Wmfd2893gb7&gameVersion=21&binaryVersion=35"));
				HttpClient client = HttpClient.create()
						.baseUrl("http://www.boomlings.com/database")
						.headers(h -> {
							h.add("Content-Type", "application/x-www-form-urlencoded");
							h.add("Content-Length", data.readableBytes());
						});


				response = Objects.requireNonNull(client.post()
						.uri("/downloadGJLevel22.php")
						.send(Mono.just(data))
						.responseSingle((responceHeader, responceBody) -> {
							if (responceHeader.status().equals(HttpResponseStatus.OK)) {
								return responceBody.asString().defaultIfEmpty("");
							} else {
								return Mono.error(new RuntimeException(responceHeader.status().toString()));
							}
						}).block());
				System.out.println(response);
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

	}
	/*public static ArrayList<LevelData> downloadGJLevel(long ID){

	}*/
}
