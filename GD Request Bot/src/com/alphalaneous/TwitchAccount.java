package com.alphalaneous;

import org.json.JSONObject;

public class TwitchAccount {

	public static String broadcaster_type;
	public static String offline_image_url;
	public static String description;
	public static String created_at;
	public static String profile_image_url;
	public static String id;
	public static String login;
	public static String display_name;
	public static String type;
	public static long view_count;
	public static String email;


	public static void setInfo() {
		JSONObject data = APIs.getInfo();

		broadcaster_type = data.getString("broadcaster_type");
		offline_image_url = data.getString("offline_image_url");
		description = data.getString("description");
		created_at = data.getString("created_at");
		profile_image_url = data.getString("profile_image_url");
		id = data.getString("id");
		login = data.getString("login");
		display_name = data.getString("display_name");
		type = data.getString("type");
		view_count = data.getLong("view_count");
	}
}
