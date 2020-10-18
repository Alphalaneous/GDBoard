package Main;

import Main.SettingsPanels.AccountSettings;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.AuthenticatedGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class LoadGD {

	public static String username = "";
	public static String password = "";
	public static AnonymousGDClient anonClient = GDClientBuilder.create().buildAnonymous();
	public static AuthenticatedGDClient authClient;

	public static boolean isAuth = false;
	public static boolean timeout = false;
	public static boolean loaded = false;
	public static boolean reload = true;

	public static void load(boolean load) throws IOException {
		if(Settings.getSettings("GDLogon").equalsIgnoreCase("true")) {
			username = Settings.getSettings("GDUsername");
			password = xor(new String(Base64.getDecoder().decode(Settings.getSettings("p").getBytes())), 15);
			try {
				authClient = GDClientBuilder.create().buildAuthenticated(new GDClientBuilder.Credentials(username, password)).block();
				isAuth = true;
			} catch (Exception e) {
				e.printStackTrace();
				isAuth = false;
			}
			AccountSettings.refreshGD(username);
		}
		loaded = true;
		reload = true;
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
