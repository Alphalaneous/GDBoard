package com.alphalaneous;

import com.alphalaneous.SettingsPanels.AccountSettings;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.AuthenticatedGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;

import java.util.Base64;

public class LoadGD {

	public static String username = "";
	private static String password = "";
	static AnonymousGDClient anonClient = GDClientBuilder.create().buildAnonymous();
	public static AuthenticatedGDClient authClient;

	public static boolean isAuth = false;
	static boolean loaded = false;

	static void load() {
		new Thread(() -> {
		if(Settings.getSettings("GDLogon").equalsIgnoreCase("true")) {
			username = Settings.getSettings("GDUsername");
			password = xor(new String(Base64.getDecoder().decode(Settings.getSettings("p").getBytes())), 15);
			try {
				authClient = GDClientBuilder.create().buildAuthenticated(new GDClientBuilder.Credentials(username, password)).block();
				isAuth = true;
			} catch (Exception e) {
				e.printStackTrace();
				Settings.writeSettings("GDLogon", "false");
				isAuth = false;
			}
			AccountSettings.refreshGD(username);
		}
		else{
			Settings.writeSettings("GDLogon", "false");
			isAuth = false;
		}
		loaded = true;
		}).start();
	}

	private static String xor(String inputString, int xorKey) {

		StringBuilder outputString = new StringBuilder();

		int len = inputString.length();

		for (int i = 0; i < len; i++) {
			outputString.append((char) (inputString.charAt(i) ^ xorKey));
		}
		return outputString.toString();
	}
}
