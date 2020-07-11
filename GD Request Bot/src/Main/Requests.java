package Main;

import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
import Main.InnerWindows.SongWindow;
import Main.SettingsPanels.*;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.AuthenticatedGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.*;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.exception.SpriteLoadException;
import com.github.alex1304.jdash.graphics.SpriteFactory;
import com.github.alex1304.jdash.util.GDUserIconSet;
import com.github.alex1304.jdash.util.LevelSearchFilters;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;


public class Requests {

	private static String os = (System.getProperty("os.name")).toUpperCase();
	public static ArrayList<LevelData> levels = new ArrayList<>();
	static ArrayList<Long> addedLevels = new ArrayList<Long>();
	private static HashMap<String, Integer> userStreamLimitMap = new HashMap<>();

	static void addRequest(long ID, String requester) {
		OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));


		if (MainBar.requests) {
			Path blocked = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
			Path logged = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
			Path blockedUser = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blockedUsers.txt");
			if(Main.loaded) {
				if(GeneralSettings.followersOption){
					if(APIs.isNotFollowing(requester)) {
						Main.sendMessage("@" + requester + " Please follow to send levels!");
						return;
					}
				}
				for (int k = 0; k < levels.size(); k++) {

					if (ID == levels.get(k).getLevelID()) {
						int j = k + 1;
						Main.sendMessage(
								"@" + requester + " Level is already in the queue at position " + j + "!");
						System.out.println("Level Already Exists");
						return;
					}
				}
				if (GeneralSettings.queueLimitBoolean && (levels.size() >= GeneralSettings.queueLimit)) {
					System.out.println(GeneralSettings.queueLimit + ", " + (levels.size()));
					if (!GeneralSettings.queueFullOption) {
						Main.sendMessage("@" + requester + " The queue is full!");
					}
					return;
				}

				if (GeneralSettings.userLimitOption) {
					int size = 0;
					for (LevelData level : levels) {
						if (level.getRequester().toString().equalsIgnoreCase(requester)) {
							size++;
						}
					}
					if (size >= GeneralSettings.userLimit) {
						Main.sendMessage("@" + requester + " You have the maximum amount of levels in the queue!");
						return;
					}
				}
				if (GeneralSettings.userLimitStreamOption) {
					if (userStreamLimitMap.containsKey(requester)) {
						if (userStreamLimitMap.get(requester) >= GeneralSettings.userLimitStream) {
							Main.sendMessage("@" + requester + " You've reached the maximum amount of levels for the stream!");
							return;
						}
					}
				}
				if (Files.exists(logged) && GeneralSettings.repeatedOptionAll && Main.loaded) {
					Scanner sc = null;
					try {
						sc = new Scanner(logged.toFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					while (sc.hasNextLine()) {
						if (String.valueOf(ID).equals(sc.nextLine())) {
							sc.close();
							Main.sendMessage("@" + requester + " That level has been requested before!");
							return;
						}
					}
					sc.close();
				}
				if (addedLevels.contains(ID) && GeneralSettings.repeatedOption) {
					Main.sendMessage("@" + requester + " That level has been requested before!");
					return;
				}

				if (Files.exists(blocked)) {
					Scanner sc = null;
					try {
						sc = new Scanner(blocked.toFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					while (sc.hasNextLine()) {
						if (String.valueOf(ID).equals(sc.nextLine())) {
							sc.close();
							Main.sendMessage("@" + requester + " That Level is Blocked!");
							return;
						}
					}
					sc.close();
				}

					if (Files.exists(blockedUser)) {
						Scanner sc = null;
						try {
							sc = new Scanner(blockedUser.toFile());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						while (sc.hasNextLine()) {
							if (requester.equalsIgnoreCase(sc.nextLine())) {
								sc.close();
								return;
							}
						}
						sc.close();
					}

			}
			if (userStreamLimitMap.containsKey(requester)) {
				userStreamLimitMap.put(requester, userStreamLimitMap.get(requester) + 1);
			} else {
				userStreamLimitMap.put(requester, 1);
			}
			System.out.println(userStreamLimitMap.get(requester));

			AuthenticatedGDClient client = null;
			AnonymousGDClient clientAnon = null;
			GDLevel level;
			GDUser user = null;

			if(LoadGD.isAuth) {
				client = (AuthenticatedGDClient) LoadGD.client;
				try {
					level = client.getLevelById(ID).block();
				} catch (MissingAccessException | NumberFormatException e) {
					Main.sendMessage("@" + requester + " That level ID doesn't exist!");
					return;
				} catch (Exception e) {
					Main.sendMessage("@" + requester + " Level search failed... (Servers down?)");
					return;
				}
			}
			else{
				clientAnon = (AnonymousGDClient) LoadGD.client;
				try {
					level = clientAnon.getLevelById(ID).block();
				} catch (MissingAccessException | NumberFormatException e) {
					Main.sendMessage("@" + requester + " That level ID doesn't exist!");
					return;
				} catch (Exception e) {
					Main.sendMessage("@" + requester + " Level search failed... (Servers down?)");
					return;
				}
			}

			LevelData levelData = new LevelData();
			// --------------------
			Thread parse;
			if(Main.loaded) {
				if (level != null && RequestSettings.ratedOption && !(level.getStars() > 0)) {
					Main.sendMessage("@" + requester + " Please send star rated levels only!");
					return;
				}
				if (level != null && RequestSettings.unratedOption && level.getStars() > 0) {
					Main.sendMessage("@" + requester + " Please send unrated levels only!");
					return;
				}
				if(level != null && RequestSettings.minObjectsOption && level.getObjectCount() < RequestSettings.minObjects){
					Main.sendMessage("@" + requester + " That level has too few objects!");
					return;
				}
				if(level != null && RequestSettings.maxObjectsOption && level.getObjectCount() > RequestSettings.maxObjects){
					Main.sendMessage("@" + requester + " That level has too many objects!");
					return;
				}
				if(level.getObjectCount() != 0) {
					if (level != null && RequestSettings.minLikesOption && level.getObjectCount() < RequestSettings.minLikes) {
						Main.sendMessage("@" + requester + " That level has too few likes!");
						return;
					}
					if (level != null && RequestSettings.maxLikesOption && level.getObjectCount() > RequestSettings.maxLikes) {
						Main.sendMessage("@" + requester + " That level has too many likes!");
						return;
					}
				}
			}
			levelData.setRequester(requester);
			levelData.setAuthor(Objects.requireNonNull(level).getCreatorName());
			levelData.setName(level.getName());
			levelData.setDifficulty(level.getDifficulty().toString());
			levelData.setDescription(level.getDescription());
			levelData.setLikes(level.getLikes());
			levelData.setDownloads(level.getDownloads());
			levelData.setSongURL(Objects.requireNonNull(level.getSong().block()).getDownloadURL());
			levelData.setLength(level.getLength().toString());
			levelData.setLevelID(ID);
			levelData.setVersion(level.getGameVersion());
			levelData.setLevelVersion(level.getLevelVersion());

			levelData.setEpic(level.isEpic());
			levelData.setSongID((int) Objects.requireNonNull(level.getSong().block()).getId());
			levelData.setStars(level.getStars());
			levelData.setSongName(Objects.requireNonNull(level.getSong().block()).getSongTitle());
			levelData.setSongAuthor(Objects.requireNonNull(level.getSong().block()).getSongAuthorName());
			levelData.setObjects(Objects.requireNonNull(level.getObjectCount()));
			levelData.setOriginal(Objects.requireNonNull(level.getOriginalLevelID()));
			levelData.setCoins(Objects.requireNonNull(level.getCoinCount()));
			GDUserIconSet iconSet = null;
			try {
				if(LoadGD.isAuth) {
					user = client.searchUser(levelData.getAuthor().toString()).block();
				}
				else {
					user = clientAnon.searchUser(levelData.getAuthor().toString()).block();
				}

				try {
					iconSet = new GDUserIconSet(user, SpriteFactory.create());
				} catch (SpriteLoadException e) {
					e.printStackTrace();
				}
			}
			catch (MissingAccessException e){
				user = client.searchUser("RobTop").block();
				try {
					iconSet = new GDUserIconSet(user, SpriteFactory.create());
				} catch (SpriteLoadException e1) {
					e1.printStackTrace();
				}
			}
			BufferedImage icon = iconSet.generateIcon(user.getMainIconType());
			Image imgScaled = icon.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
			ImageIcon imgNew = new ImageIcon(imgScaled);
			levelData.setPlayerIcon(imgNew);
			//String[] videoInfo = APIs.getYTVideo(ID);

            /*if(videoInfo.length != 0) {
                levelData.setVideoInfo(videoInfo[0], videoInfo[1], videoInfo[2], videoInfo[3]);
            }*/

			if (level.getFeaturedScore() > 0) {
				levelData.setFeatured();
			}

			if (level.isDemon()) {
				if (level.getDifficulty().toString().equalsIgnoreCase("EASY")) {
					levelData.setDifficulty("easy demon");
				} else if (level.getDifficulty().toString().equalsIgnoreCase("NORMAL")) {
					levelData.setDifficulty("medium demon");
				} else if (level.getDifficulty().toString().equalsIgnoreCase("HARD")) {
					levelData.setDifficulty("hard demon");
				} else if (level.getDifficulty().toString().equalsIgnoreCase("HARDER")) {
					levelData.setDifficulty("insane demon");
				} else if (level.getDifficulty().toString().equalsIgnoreCase("INSANE")) {
					levelData.setDifficulty("extreme demon");
				}
			}
			System.out.println(levelData.getDifficulty());
			if(Main.loaded) {
				if (RequestSettings.excludedDifficulties.contains(levelData.getDifficulty().toString().toLowerCase()) && RequestSettings.disableOption) {
					Main.sendMessage("@" + requester + " That difficulty is disabled!");
					return;
				}
				if (RequestSettings.excludedLengths.contains(levelData.getLength().toString().toLowerCase()) && RequestSettings.disableLengthOption) {
					Main.sendMessage("@" + requester + " That length is disabled!");
					return;
				}
			}

			if (levelData.getDescription().toString().toLowerCase().contains("nong")) {
				String[] words = levelData.getDescription().toString().split(" ");
				for (String word : words) {
					if (isValidURL(word)){
						levelData.setSongURL(word);
					}
				}
			}
			if(LoadGD.isAuth) {
				AuthenticatedGDClient finalClient = client;
				parse = new Thread(() -> {
					Object object = Objects.requireNonNull(finalClient.getLevelById(ID).block()).download().block();
					if (!(level.getStars() > 0) && level.getGameVersion() / 10 >= 2) {
						parse(((GDLevelData) Objects.requireNonNull(object)).getData(), ID);
					}
					levelData.setPassword(((GDLevelData) Objects.requireNonNull(object)).getPass());
					levelData.setUpload(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getUploadTimestamp()));
					levelData.setUpdate(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getLastUpdatedTimestamp()));
					InfoWindow.refreshInfo();


				});
			}
			else{
				AnonymousGDClient finalClient = clientAnon;
				parse = new Thread(() -> {
					Object object = Objects.requireNonNull(finalClient.getLevelById(ID).block()).download().block();
					if (!(level.getStars() > 0) && level.getGameVersion() / 10 >= 2) {
						parse(((GDLevelData) Objects.requireNonNull(object)).getData(), ID);
					}
					levelData.setPassword(((GDLevelData) Objects.requireNonNull(object)).getPass());
					levelData.setUpload(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getUploadTimestamp()));
					levelData.setUpdate(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getLastUpdatedTimestamp()));
					InfoWindow.refreshInfo();


				});
			}
			parse.start();
			if(os.contains("WIN")) {
				if (GeneralSettings.autoDownloadOption) {
					Thread songDL = new Thread(() -> {
						Path songFile = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + levelData.getSongID() + ".mp3");
						if (!Files.exists(songFile)) {
							try {
								FileUtils.copyURLToFile(levelData.getSongURL(), songFile.toFile());
							} catch (IOException ignored) {
							}
						}
					});
					songDL.start();
				}
			}
			levels.add(levelData);
			LevelsWindow.createButton(levelData.getName().toString(), levelData.getAuthor().toString(), levelData.getLevelID(), levelData.getDifficulty().toString(), levelData.getEpic(), levelData.getFeatured(), levelData.getStars(), levelData.getRequester().toString(), levelData.getVersion(), levelData.getPlayerIcon());
			LevelsWindow.setName(Requests.levels.size());
			Functions.saveFunction();
			if(Main.doMessage) {
				Main.sendMessage("@" + levelData.getRequester() + " " + levelData.getName() + " ("
						+ levelData.getLevelID() + ") has been added to the queue at position " + levels.size() + "!");
			}
			if (levels.size() == 1) {
				StringSelection selection = new StringSelection(String.valueOf(Requests.levels.get(0).getLevelID()));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				if(Main.doMessage) {
					if (!GeneralSettings.nowPlayingOption) {
						Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
								+ Requests.levels.get(0).getLevelID() + "). Requested by "
								+ Requests.levels.get(0).getRequester());
					}
				}
			}
			OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
			addedLevels.add(ID);
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
			try {
				boolean exists = false;
				if (!Files.exists(file)) {
					Files.createFile(file);
				}
				if(Files.exists(logged)){
					Scanner sc = new Scanner(logged.toFile());
					while (sc.hasNextLine()) {
						if (String.valueOf(ID).equals(sc.nextLine())) {
							sc.close();
							exists = true;
							break;
						}
						Thread.sleep(5);
					}
					sc.close();
				}
				if(!exists) {
					Files.write(
							file,
							(ID + "\n").getBytes(),
							StandardOpenOption.APPEND);
					RequestsLog.addButton(ID);

				}
			} catch (IOException e1) {
				DialogBox.showDialogBox("Error!", e1.toString(), "There was an error writing to the file!", new String[]{"OK"});

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			Main.sendMessage("@" + requester + " Requests are off!");
		}
	}

	@SuppressWarnings("unused")
	public static String getLevel(int level, String attribute) {

		String result = "";
		try {
			if (attribute.equals("name")) {
				result = levels.get(level).getName().toString();
			}
			if (attribute.equals("id")) {
				result = String.valueOf(levels.get(level).getLevelID());
			}
			if (attribute.equals("author")) {
				result = levels.get(level).getAuthor().toString();
			}
			if (attribute.equals("requester")) {
				result = levels.get(level).getRequester().toString();
			}
			if (attribute.equals("difficulty")) {
				result = levels.get(level).getDifficulty().toString();
			}
			if (attribute.equals("likes")) {
				result = String.valueOf(levels.get(level).getLikes());
			}
			if (attribute.equals("downloads")) {
				result = String.valueOf(levels.get(level).getDownloads());
			}
			if (attribute.equals("description")) {
				result = levels.get(level).getDescription().toString();
			}
			if (attribute.equals("songName")) {
				result = levels.get(level).getSongName().toString();
			}
			if (attribute.equals("songID")) {
				result = String.valueOf(levels.get(level).getSongID());
			}
			if (attribute.equals("songAuthor")) {
				result = levels.get(level).getSongAuthor().toString();
			}
			if (attribute.equals("songURL")) {
				result = String.valueOf(levels.get(level).getSongURL());
			}
			if (attribute.equals("stars")) {
				result = String.valueOf(levels.get(level).getStars());
			}
			if (attribute.equals("epic")) {
				result = String.valueOf(levels.get(level).getEpic());
			}
			if (attribute.equals("featured")) {
				result = String.valueOf(levels.get(level).getFeatured());
			}
			if (attribute.equals("version")) {
				result = String.valueOf(levels.get(level).getVersion());
			}
			if (attribute.equals("length")) {
				result = levels.get(level).getLength().toString();
			}
		}
		catch (Exception e){
			result = "Exception: " + e.toString();
		}
		return result;
	}
	public static int getSize(){
		return levels.size();
	}

	@SuppressWarnings("unused")
	public static void kill(){
		GDMod.run("kill");
	}
	public static void crash(){
		try {
			ProcessBuilder pb = new ProcessBuilder("taskkill", "/IM", "GeometryDash.exe", "/F").redirectErrorStream(true);
			pb.start();

		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static void toggleRequests(){
		Functions.requestsToggleFunction();
	}

	@SuppressWarnings("unused")
	public static void clear(){
		for (int i = 0; i < Requests.levels.size(); i++) {
			LevelsWindow.removeButton();
		}
		Requests.levels.clear();
		Functions.saveFunction();

		SongWindow.refreshInfo();
		InfoWindow.refreshInfo();
		CommentsWindow.unloadComments(true);
		LevelsWindow.setName(Requests.levels.size());
	}
	public static String remove(String user, boolean isMod, int intArg){
		String response = "";
		for (int i = 0; i < Requests.levels.size(); i++) {
			try {
				if (Requests.levels.get(i).getLevelID() == Requests.levels.get(intArg - 1).getLevelID()
						&& (isMod || String.valueOf(user).equalsIgnoreCase(Requests.levels.get(i).getRequester().toString()))) {
					response = "@" + user + ", " + Requests.levels.get(i).getName()+ " (" + Requests.levels.get(i).getLevelID() + ") has been removed!";
					LevelsWindow.removeButton(i);
					Requests.levels.remove(i);
					Functions.saveFunction();
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
					LevelsWindow.setOneSelect();
					Thread thread = new Thread(() -> {
						CommentsWindow.unloadComments(true);
						CommentsWindow.loadComments(0, false);
					});
					thread.start();
					if (i == 0) {
						StringSelection selection = new StringSelection(
								String.valueOf(Requests.levels.get(0).getLevelID()));
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
				}
			} catch (Exception ignored) {
			}
		}
		LevelsWindow.setName(Requests.levels.size());
		return response;
	}
	public static String removeLatest(String user){
		String response = "";
		for (int i = Requests.levels.size()-1; i >= 0; i--) {
			try {
				if (String.valueOf(user).equalsIgnoreCase(Requests.levels.get(i).getRequester().toString())) {
					response = "@" + user + ", " + Requests.levels.get(i).getName()+ " (" + Requests.levels.get(i).getLevelID() + ") has been removed!";
					LevelsWindow.removeButton(i);
					Requests.levels.remove(i);
					Functions.saveFunction();
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
					LevelsWindow.setOneSelect();
					Thread thread = new Thread(() -> {
						CommentsWindow.unloadComments(true);
						CommentsWindow.loadComments(0, false);
					});
					thread.start();
					if (i == 0) {
						StringSelection selection = new StringSelection(
								String.valueOf(Requests.levels.get(0).getLevelID()));
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
					break;
				}
			} catch (Exception ignored) {
			}
		}
		LevelsWindow.setName(Requests.levels.size());
		return response;
	}
	private static Thread rickThread = null;

	@SuppressWarnings("unused")
	public static void rick(){
		System.out.println("ricked");
		if (rickThread != null) {
			rickThread.stop();
		}
		rickThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
						.getResource("/Resources/rick.mp3").openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (JavaLayerException | NullPointerException | IOException f) {
				f.printStackTrace();
				DialogBox.showDialogBox("Error!", f.toString(), "There was an error playing the sound!", new String[]{"OK"});

			}
		});
		rickThread.start();
	}

	@SuppressWarnings("unused")
	public static void stopRick(){
		if (rickThread != null && rickThread.isAlive()) {
			rickThread.stop();
		}
	}
	static boolean bwomp = false;

	@SuppressWarnings("unused")
	public static void toggleBwomp(){
		bwomp = !bwomp;
	}

	private static Thread knockThread = null;

	@SuppressWarnings("unused")
	public static void knock(){
		if (knockThread != null) {
			knockThread.stop();
		}
		knockThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
						.getResource("/Resources/knock.mp3").openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (JavaLayerException | NullPointerException | IOException f) {
				f.printStackTrace();
				DialogBox.showDialogBox("Error!", f.toString(), "There was an error playing the sound!", new String[]{"OK"});

			}
		});
		knockThread.start();
	}

	@SuppressWarnings("unused")
	public static void stopKnock(){
		if (knockThread != null && knockThread.isAlive()) {
			knockThread.stop();
		}
	}
	private static Thread bwompThread = null;

	@SuppressWarnings("unused")
	public static void bwomp(){
		if (bwompThread != null) {
			bwompThread.stop();
		}
		bwompThread = new Thread(() -> {
			try {
				BufferedInputStream inp = new BufferedInputStream(ServerChatBot.class
						.getResource("/Resources/bwomp.mp3").openStream());
				Player mp3player = new Player(inp);
				mp3player.play();
			} catch (JavaLayerException | NullPointerException | IOException f) {
				f.printStackTrace();
				DialogBox.showDialogBox("Error!", f.toString(), "There was an error playing the sound!", new String[]{"OK"});


			}
		});
		bwompThread.start();
	}

	@SuppressWarnings("unused")
	public static void stopBwomp(){
		if (bwompThread != null && bwompThread.isAlive()) {
			bwompThread.stop();
		}
	}

	public static void movePosition(int position, int newPosition){
		LevelsWindow.movePosition(position, newPosition);
	}

	public static int getPosFromID(long ID){
		for(int i = 0; i < LevelsWindow.getSize(); i++){
			if(LevelsWindow.getButton(i).getID() == ID){
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unused")
	public static String block(String user, String[] arguments){
		String response;
		try {
			boolean start = false;
			int blockedID = Integer.parseInt(arguments[1]);
			if(blockedID == Requests.levels.get(0).getLevelID() && Requests.levels.size() > 1){
					StringSelection selection = new StringSelection(
							String.valueOf(Requests.levels.get(1).getLevelID()));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
					start = true;
			}
			for(int i = 0; i < Requests.levels.size(); i++){
				if(Requests.levels.get(i).getLevelID() == blockedID){
					LevelsWindow.removeButton(i);
					Requests.levels.remove(i);
					InfoWindow.refreshInfo();
					SongWindow.refreshInfo();
					CommentsWindow.unloadComments(true);
					CommentsWindow.loadComments(0, false);
					Functions.saveFunction();
					break;
				}
			}
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			Scanner sc = new Scanner(file.toFile());
			while (sc.hasNextLine()) {
				if (String.valueOf(blockedID).equals(sc.nextLine())) {
					sc.close();
					return "@" + user + " ID Already Blocked!";
				}
			}
			sc.close();

			try {
				Files.write(file, (blockedID + "\n").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			response = "@" + user + " Successfully blocked " + arguments[1];
			BlockedSettings.addButton(Long.parseLong(arguments[1]));
			if(start){
				LevelsWindow.setOneSelect();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "@" + user + " Block failed!";
		}
		return response;
	}

	@SuppressWarnings("unused")
	public static String unblock(String user, String[] arguments){
		String unblocked = arguments[1];
		String response = "";
		try {
			boolean exists = false;
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
			if (Files.exists(file)) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					if (String.valueOf(unblocked).equals(sc.nextLine())) {
						exists = true;
						break;
					}
				}
				sc.close();

				if (exists) {
					Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_temp_");
					PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
					Files.lines(file)
							.filter(line -> !line.contains(unblocked))
							.forEach(out::println);
					out.flush();
					out.close();
					Files.delete(file);
					Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\blocked.txt"), StandardCopyOption.REPLACE_EXISTING);
					BlockedSettings.removeID(arguments[1]);
					response = "@" + user + " Successfully unblocked " + arguments[1];
					BlockedSettings.removeID(unblocked);
				} else {
					response = "@" + user + " That level isn't blocked!";
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			response = "@" + user + " unblock failed!";
		}
		return response;
	}

	@SuppressWarnings("unused")
	public static String blockUser(String user, String[] arguments){
		String response;
		try {
			String blockedUser = arguments[1];
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blockedUsers.txt");
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			Scanner sc = new Scanner(file.toFile());
			while (sc.hasNextLine()) {
				if (String.valueOf(blockedUser).equals(sc.nextLine())) {
					sc.close();
					return "@" + user + " User Already Blocked!";
				}
			}
			sc.close();
			try {
				Files.write(file, (blockedUser + "\n").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			response = "@" + user + " Successfully blocked " + arguments[1];
			BlockedUserSettings.addButton(arguments[1]);

		} catch (Exception e) {
			e.printStackTrace();
			response = "@" + user + " Block failed!";
		}
		return response;
	}

	@SuppressWarnings("unused")
	public static String unblockUser(String user, String[] arguments){
		String response = "";
		String unblocked = arguments[1];

		try {
			boolean exists = false;
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blockedUsers.txt");
			if (Files.exists(file)) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					if (String.valueOf(unblocked).equals(sc.nextLine())) {
						exists = true;
						break;
					}
				}
				sc.close();
				if (exists) {
					Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_temp_");
					PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
					Files.lines(file)
							.filter(line -> !line.contains(unblocked))
							.forEach(out::println);
					out.flush();
					out.close();
					Files.delete(file);
					Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\blockedUsers.txt"), StandardCopyOption.REPLACE_EXISTING);
					BlockedSettings.removeID(arguments[1]);
					response = "@" + user + " Successfully unblocked " + arguments[1];
					BlockedUserSettings.removeUser(unblocked);
				} else {
					response = "@" + user + " That user isn't blocked!";
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			response = "@" + user + " unblock failed!";
		}
		return response;
	}

	@SuppressWarnings("unused")

	public static String getHelp(String command){
		String info = null;
		boolean infoExists = false;
		try {
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/commands/info.txt"))) {
				Scanner sc2 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/commands/info.txt").toFile());
				while (sc2.hasNextLine()) {
					String line = sc2.nextLine();
					System.out.println(line);
					if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(command)) {
						infoExists = true;
						info = line.split("=")[1];
						break;
					}
				}
				sc2.close();
			}
			if (!infoExists) {
				InputStream is = Main.class
						.getClassLoader().getResourceAsStream("Resources/Commands/info.txt");
				assert is != null;
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(command)) {
						info = line.split("=")[1];
						break;
					}
				}
				is.close();
				isr.close();
				br.close();
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		if(info == null){
			return "There is no info for this command!";
		}
		return info;
	}

	@SuppressWarnings("unused")
	public static String getHelp(){
		System.out.println("here");
		StringBuilder message = new StringBuilder();
		try {
			URI uri = Main.class.getResource("/Resources/Commands/").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = ServerChatBot.fileSystem.getPath("/Resources/Commands/");
			} else {
				myPath = Paths.get(uri);
			}

			message.append("List of Commands | Type !help <command> for more help.");
			Stream<Path> walk = Files.walk(myPath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file = path.toString().split("/");
				String fileName = file[file.length - 1];
				if(fileName.endsWith(".js")) {
					if(!fileName.equalsIgnoreCase("!rick.js") && !fileName.equalsIgnoreCase("!stoprick.js") && !fileName.equalsIgnoreCase("!kill.js")) {
						System.out.println(fileName);
						message.append(" | ").append(fileName, 0, fileName.length() - 3);
					}
				}
			}
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/commands/");
			if(Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.endsWith(".js")) {
						message.append(" | ").append(fileName, 0, fileName.length() - 3);
					}
				}
			}
		}
		catch (IOException | URISyntaxException e){
			e.printStackTrace();
		}
		return message.toString();
	}

	@SuppressWarnings("unused")
	public static String getOAuth(){
		try {
			return Settings.getSettings("oauth");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unused")
	public static String getClientID(){
		return "fzwze6vc6d2f7qodgkpq2w8nnsz3rl";
	}

	public static void endGDBoard(){
		Main.close();
	}

	@SuppressWarnings("unused")
	public static String request(String user, boolean isMod, boolean isSub, String[] arguments){
		String response = "";
		Matcher m = null;
		if (GeneralSettings.subsOption) {
			if (!isSub) {
				if (!isMod) {
					return  "@" + user + " Please subscribe to request levels!";
				}
			}
		}
		if (arguments.length > 1) {
			try {
				m = Pattern.compile("(\\d+)").matcher(arguments[1]);
			} catch (Exception ignored) {
			}
			assert m != null;
			if (m.matches() && arguments.length <= 2) {
				try {
					Requests.addRequest(Long.parseLong(m.group(1)), String.valueOf(user));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				StringBuilder message = new StringBuilder();
				for (int i = 1; i < arguments.length; i++) {
					if (arguments.length - 1 == i) {
						message.append(arguments[i]);
					} else {
						message.append(arguments[i]).append(" ");
					}
				}
				if (message.toString().contains(" by ")) {
					String level1 = message.toString().split("by ")[0].toUpperCase();
					String username = message.toString().split("by ")[1];
					AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
					try {
						outerLoop:
						for (int j = 0; j < 10; j++) {
							Object[] levelPage = Objects.requireNonNull(client.getLevelsByUser(Objects.requireNonNull(client.searchUser(username).block()), j)
									.block()).asList().toArray();
							for (int i = 0; i < 10; i++) {
								if (((GDLevel) levelPage[i]).getName().toUpperCase()
										.startsWith(level1.substring(0, level1.length() - 1))) {
									Requests.addRequest(((GDLevel) levelPage[i]).getId(),
											String.valueOf(user));
									break outerLoop;
								}
							}
						}

					} catch (IndexOutOfBoundsException ignored) {
					} catch (MissingAccessException e) {
						response = "@" + user + " That level or user doesn't exist!";
						e.printStackTrace();
					}
				} else {

					AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
					try {
						Requests.addRequest(Objects.requireNonNull(client.searchLevels(message.toString(), LevelSearchFilters.create(), 0)
												.block().asList().get(0).getId()),
								String.valueOf(user));
					} catch (MissingAccessException e) {
						response = "@" + user + " That level doesn't exist!";
					} catch (Exception e){
						response = "@" + user + " An unknown error occured";

					}
				}
			}
		} else {
			response = "@" + user + " Please specify an ID!!";
		}
		return response;
		//}
	}

	private static void parse(byte[] level, long levelID) {
		all:
		for (int k = 0; k < Requests.levels.size(); k++) {
			if (Requests.levels.get(k).getLevelID() == levelID) {
				String decompressed = null;
				try {
					decompressed = decompress(level);
				} catch (IOException e) {
					e.printStackTrace();
				}
				assert decompressed != null;
				int imageIDCount = 0;
				String color = "";
				String[] values = decompressed.split(";");
				Requests.levels.get(k).setObjects(values.length);
				if((values.length < RequestSettings.minObjects) && RequestSettings.minObjectsOption){
					Main.sendMessage("@" + Requests.levels.get(k).getRequester() + " Your level has been removed for containing too few objects!");
					LevelsWindow.removeButton(k);
					Requests.levels.remove(k);
					return;
				}
				if((values.length > RequestSettings.maxObjects) && RequestSettings.maxObjectsOption){
					Main.sendMessage("@" + Requests.levels.get(k).getRequester() + " Your level has been removed for containing too many objects!");
					LevelsWindow.removeButton(k);
					Requests.levels.remove(k);
					return;
				}
				for (String value1 : values) {
					if (value1.startsWith("1,1110") || value1.startsWith("1,211") || value1.startsWith("1,914")) {
						String value = value1.replaceAll("(,[^,]*),", "$1;");
						String[] attributes = value.split(";");
						double scale = 0;
						boolean hsv = false;
						boolean zOrder = false;
						String tempColor = "";
						String text = "";
						for (String attribute : attributes) {

							if (attribute.startsWith("32")) {
								if (Double.parseDouble(attribute.split(",")[1]) < 1) {
									scale = Double.parseDouble(attribute.split(",")[1]);
								}
							}
							if (attribute.startsWith("41")) {
								hsv = true;
							}
							if (attribute.startsWith("21")) {
								tempColor = attribute.split(",")[1];
							}
							if (attribute.startsWith("25")) {
								zOrder = true;
							}
							if (attribute.startsWith("31")) {
								String formatted = attribute.split(",")[1].replace("_", "/").replace("-", "+");
								text = new String(Base64.getDecoder().decode(formatted));
							}
						}
						InputStream is = Main.class.getClassLoader()
								.getResourceAsStream("Resources/blockedWords.txt");
						assert is != null;
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						String line;

						try {
							out:
							while ((line = br.readLine()) != null) {
								String[] text1 = text.toUpperCase().split(" ");

								for (String s : text1) {
									if (s.equalsIgnoreCase(line)) {
										System.out.println("Contains Vulgar");
										Requests.levels.get(k).setContainsVulgar();
										break out;
									}
								}
							}
							if (scale != 0.0 && hsv) {
								if (tempColor.equalsIgnoreCase(color) && !zOrder) {
									imageIDCount++;
								}
							}
							if (imageIDCount >= 1000) {
								Requests.levels.get(k).setContainsImage();
							}
							color = tempColor;
						} catch (IOException e) {
							e.printStackTrace();
							break all;
						}
					}
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Requests.levels.get(k).setAnalyzed();
					LevelsWindow.updateUI(Requests.levels.get(k).getLevelID(), Requests.levels.get(k).getContainsVulgar(), Requests.levels.get(k).getContainsImage(), true);
				}
				catch (IndexOutOfBoundsException ignored){
				}
				System.out.println("Analyzed " + k);
			}
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static String decompress(byte[] compressed) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
		GZIPInputStream gis = new GZIPInputStream(bis);
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

	static String parseInfoString(String text, int level) {
		if (Requests.levels.size() != 0) {
			text = text.replaceAll("(?i)%levelName%", levels.get(level).getName().toString())
					.replaceAll("(?i)%levelID%", String.valueOf(levels.get(level).getLevelID()))
					.replaceAll("(?i)%levelAuthor%", levels.get(level).getAuthor().toString())
					.replaceAll("(?i)%requester%", levels.get(level).getRequester().toString())
					.replaceAll("(?i)%songName%", levels.get(level).getSongName().toString())
					.replaceAll("(?i)%songID%", String.valueOf(levels.get(level).getSongID()))
					.replaceAll("(?i)%songArtist%", levels.get(level).getSongAuthor().toString())
					.replaceAll("(?i)%likes%", String.valueOf(levels.get(level).getLikes()))
					.replaceAll("(?i)%downloads%", String.valueOf(levels.get(level).getDownloads()))
					.replaceAll("(?i)%description%", levels.get(level).getDescription().toString())
					.replaceAll("(?i)%queueSize%", String.valueOf(levels.size()))
					.replaceAll("(?i)%s%", "");
			return text;
		} else {
			return OutputSettings.noLevelString.replaceAll("(?i)%s%", "");
		}
	}

	private static boolean isValidURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}

	}
}
