package Main;

import Main.SettingsPanels.AccountSettings;
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
	public static Object client;
	public static boolean isAuth = false;
	public static boolean timeout = false;
	public static boolean loaded = false;


	public static void load() throws IOException {

		new Thread(() -> {
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeout = true;
			isAuth = false;
			try {
				Settings.writeSettings("loadGD", "false");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		if(!Settings.getSettings("loadGD").equalsIgnoreCase("false")) {
			Path gameFile = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\CCGameManager.dat");
			String[] gameText = new String[0];
			Scanner sc = null;
			try {
				sc = new Scanner(gameFile.toFile());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			assert sc != null;
			try {
				gameText = prettyFormat(decompress(Base64.getDecoder().decode(xor(sc.nextLine()).replace("-", "+").replace("_", "/").replace("\0", "")))).split("\n");
				sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < gameText.length; i++) {
				String text = gameText[i];
				if (!timeout) {
					if (text.contains("<k>GJA_002</k>")) {
						password = gameText[i + 1].replace("    <s>", "").replace("</s>", "").replace("\0", "").replace("\r", "").replace("\n", "");
					}
					if (text.contains("<k>GJA_001</k>")) {
						username = gameText[i + 1].replace("    <s>", "").replace("</s>", "").replace("\0", "").replace("\r", "").replace("\n", "");
					}
				}
				else{
					break;
				}
			}
			try {
				if (!timeout) {
					client = GDClientBuilder.create().buildAuthenticated(new GDClientBuilder.Credentials(username, password)).block();
					isAuth = true;
				}
			} catch (Exception e) {
				if (!timeout) {
					client = GDClientBuilder.create().buildAnonymous();
					isAuth = false;
				}
			}
		}
		loaded = true;;
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
			throw new RuntimeException(e); // simple exception handling, please review it
		}
	}

	private static String xor(String inputString) {
		int xorKey = 11;

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
