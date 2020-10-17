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
		if(!reload){
			return;
		}
		reload = false;
		new Thread(() -> {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!loaded){
				timeout = true;
				isAuth = false;
				try {
					Settings.writeSettings("loadGD", "false");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		}).start();
		boolean doesFail = false;
		if((!Settings.getSettings("loadGD").equalsIgnoreCase("false") && !Settings.getSettings("GDLoaded").equalsIgnoreCase("true")) || load) {
			Path gameFile = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\CCGameManager.dat");
			String[] gameText = new String[0];
			Scanner sc = null;
			try {
				sc = new Scanner(gameFile.toFile());
			} catch (FileNotFoundException e) {
				isAuth = false;
				doesFail = true;
				e.printStackTrace();
			}
			assert sc != null;
			try {
				gameText = prettyFormat(decompress(Base64.getDecoder().decode(xor(sc.nextLine(), 11).replace("-", "+").replace("_", "/").replace("\0", "")))).split("\n");
				sc.close();
			} catch (Exception e) {
				isAuth = false;
				doesFail = true;
				e.printStackTrace();
			}
			if(!doesFail) {
				for (int i = 0; i < gameText.length; i++) {
					String text = gameText[i];
					if (!timeout) {
						if (text.contains("<k>GJA_002</k>")) {
							password = gameText[i + 1].replace("    <s>", "").replace("</s>", "").replace("\0", "").replace("\r", "").replace("\n", "");
							Settings.writeSettings("p", new String(Base64.getEncoder().encode(xor(password, 15).getBytes())));

						}
						if (text.contains("<k>GJA_001</k>")) {
							username = gameText[i + 1].replace("    <s>", "").replace("</s>", "").replace("\0", "").replace("\r", "").replace("\n", "");
							Settings.writeSettings("GDUsername", username);
						}
					} else {
						isAuth = false;
						break;
					}
				}
				try {
					if (!timeout) {
						authClient = GDClientBuilder.create().buildAuthenticated(new GDClientBuilder.Credentials(username, password)).block();
						isAuth = true;
						Settings.writeSettings("GDLoaded", "true");
					}
				} catch (Exception e) {
					e.printStackTrace();
					isAuth = false;
				}
				gameText = null;
			}
		}
		else if(!Settings.getSettings("GDLoaded").equalsIgnoreCase("false") && !Settings.getSettings("loadGD").equalsIgnoreCase("false") || !doesFail){
			username = Settings.getSettings("GDUsername");
			password = xor(new String(Base64.getDecoder().decode(Settings.getSettings("p").getBytes())), 15);
			try {
				authClient = GDClientBuilder.create().buildAuthenticated(new GDClientBuilder.Credentials(username, password)).block();
				isAuth = true;
			}
			catch (Exception e){
				e.printStackTrace();
				isAuth = false;
			}
		}
		else{
			isAuth = false;
		}
		loaded = true;
		reload = true;
		AccountSettings.refreshGD(username);
	}
	private static String prettyFormat(String input) {
		try {
			Source xmlInput = new StreamSource(new StringReader(input));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Exception e) {
			return null;
		}
	}

	private static String xor(String inputString, int xorKey) {

		StringBuilder outputString = new StringBuilder();

		int len = inputString.length();

		for (int i = 0; i < len; i++) {
			outputString.append((char) (inputString.charAt(i) ^ xorKey));
		}
		return outputString.toString();
	}
	private static String decompress(byte[] compressed) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
		GZIPInputStream gis;
		gis = new GZIPInputStream(bis);
		BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		gis.close();
		bis.close();
		return sb.toString();
	}
}
