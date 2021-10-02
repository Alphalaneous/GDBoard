package com.alphalaneous;

import com.alphalaneous.Panels.*;
import com.alphalaneous.SettingsPanels.*;
import com.alphalaneous.Tabs.RequestsTab;
import jdash.common.DemonDifficulty;
import jdash.common.Difficulty;
import jdash.common.Length;
import jdash.common.entity.GDLevel;
import jdash.common.entity.GDSong;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


public class RequestsUtils {

	static boolean bwomp = false;
	private static final String[] gdCommands = {"!gd", "!kill", "!block", "!blockuser", "!unblock", "!unblockuser", "!clear", "!info", "!move", "!next", "!position", "!queue", "!remove", "!request", "!song", "!stop", "!toggle", "!top", "!wronglevel"};

	static void forceAdd(LevelData data) {
		//Requests.levels.add(data);
	}

	public static void forceAddBasic(long ID, String requester){
		RequestsTab.addRequest(new BasicLevelButton(ID, requester));
		RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());

	}
	public static void forceAdd(String name, String author, long levelID, String difficulty, String demonDifficulty, boolean isDemon, boolean isAuto, boolean epic, int featuredScore, int stars, int requestedStars, String requester, int gameVersion, int coins, String description, int likes, int downloads, String length, int levelVersion, long songID, String songName, String songAuthor, int objects, long original, boolean vulgar, boolean image, boolean verifiedCoins) {

		GDLevel level = new GDLevel() {
			@Override
			public long id() {
				return levelID;
			}

			@Override
			public String name() {
				return name;
			}

			@Override
			public long creatorPlayerId() {
				return 0;
			}

			@Override
			public String description() {
				return description;
			}

			@Override
			public Difficulty difficulty() {
				return Difficulty.valueOf(difficulty);
			}

			@Override
			public DemonDifficulty demonDifficulty() {
				return DemonDifficulty.valueOf(demonDifficulty);
			}

			@Override
			public int stars() {
				return stars;
			}

			@Override
			public int featuredScore() {
				return featuredScore;
			}

			@Override
			public boolean isEpic() {
				return epic;
			}

			@Override
			public int downloads() {
				return downloads;
			}

			@Override
			public int likes() {
				return likes;
			}

			@Override
			public Length length() {
				Length length1;
				switch (length){
					case "SHORT": length1 = Length.SHORT; break;
					case "MEDIUM": length1 = Length.MEDIUM; break;
					case "LONG": length1 = Length.LONG; break;
					case "XL": length1 = Length.XL; break;
					default: length1 = Length.TINY; break;
				}
				return length1;
			}

			@Override
			public int coinCount() {
				return coins;
			}

			@Override
			public boolean hasCoinsVerified() {
				return verifiedCoins;
			}

			@Override
			public int levelVersion() {
				return levelVersion;
			}

			@Override
			public int gameVersion() {
				return gameVersion;
			}

			@Override
			public int objectCount() {
				return objects;
			}

			@Override
			public boolean isDemon() {
				return isDemon;
			}

			@Override
			public boolean isAuto() {
				return isAuto;
			}

			@Override
			public Optional<Long> originalLevelId() {
				return Optional.of(original);
			}

			@Override
			public int requestedStars() {
				return requestedStars;
			}

			@Override
			public Optional<Long> songId() {
				return Optional.of(songID);
			}

			@Override
			public Optional<GDSong> song() {
				return Optional.of(new GDSong() {
					@Override
					public long id() {
						return songID;
					}

					@Override
					public String title() {
						return songName;
					}

					@Override
					public String artist() {
						return songAuthor;
					}

					@Override
					public Optional<String> size() {
						return Optional.empty();
					}

					@Override
					public Optional<String> downloadUrl() {
						return Optional.empty();
					}
				});
			}

			@Override
			public Optional<String> creatorName() {
				return Optional.of(author);
			}
		};
		LevelData levelData = new LevelData();
		levelData.setLevelData(level);
		levelData.setEpic(epic);
		if (featuredScore > 0) {
			levelData.setFeatured();
		}
		levelData.setMessage("Reloaded");
		levelData.setRequester(requester);

		if (vulgar) {
			levelData.setContainsVulgar();
		}
		if (image) {
			levelData.setContainsImage();
		}
		RequestsTab.addRequest(new LevelButton(levelData));
		if (RequestsTab.getQueueSize() == 1) {
			//RequestFunctions.containsBadStuffCheck();
		}
		RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());
		//levelData.setAnalyzed();
		//RequestsTab.getLevelsPanel().updateUI(levelID, vulgar, image, true);
		//OutputSettings.setOutputStringFile(parseInfoString(OutputSettings.outputString, 0));
		RequestsTab.refreshInfoPanel();
	}

	@SuppressWarnings("unused")
	public static String getLevel(int level, String attribute) {

		String result = "";
		try {
			switch (attribute) {
				case "name":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().name();
					break;
				case "id":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().id());
					break;
				case "author":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().creatorName().get();
					break;
				case "requester":
					result = RequestsTab.getRequest(level).getLevelData().getRequester();
					break;
				case "difficulty":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().difficulty().toString();
					break;
				case "likes":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().likes());
					break;
				case "downloads":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().downloads());
					break;
				case "description":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().description();
					break;
				case "songName":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().song().get().title();
					break;
				case "songID":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().song().get().id());
					break;
				case "songArtist":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().song().get().artist();
					break;
				case "songURL":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().song().get().downloadUrl().get();
					break;
				case "stars":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().stars());
					break;
				case "epic":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getEpic());
					break;
				case "version":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().levelVersion());
					break;
				case "length":
					result = RequestsTab.getRequest(level).getLevelData().getGDLevel().length().toString();
					break;
				case "coins":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().coinCount());
					break;
				case "objects":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().objectCount());
					break;
				case "original":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().originalLevelId());
					break;
				case "image":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getContainsImage());
					break;
				case "vulgar":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getContainsVulgar());
					break;
				case "password":
					result = String.valueOf(RequestsTab.getRequest(level).getLevelData().getPassword());
					break;
				default :
					result = "Error: Invalid type.";
			}
		} catch (Exception e) {
			result = "Exception: " + e;
		}
		return result;
	}

	@SuppressWarnings("unused")
	public static int getSelection() {
		return LevelButton.selectedID;
	}

	public static int getSize() {
		return RequestsTab.getQueueSize();
	}

	@SuppressWarnings("unused")
	public static void toggleRequests() {
		RequestFunctions.requestsToggleFunction();
	}

	@SuppressWarnings("unused")
	public static void clear() {
		/*for (int i = 0; i < Requests.levels.size(); i++) {
			LevelsPanel.removeButton();
		}*/
		RequestsTab.clearRequests();
		RequestFunctions.saveFunction();

		RequestsTab.refreshInfoPanel();
		RequestsTab.unloadComments(true);
		RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());
	}

	public static String remove(String user, boolean isMod, int intArg) {
		if (intArg - 1 == LevelButton.selectedID) {
			return "";
		}
		String response = "";
		for (int i = 0; i < RequestsTab.getQueueSize(); i++) {
			try {
				if (RequestsTab.getRequest(i).getLevelData().getGDLevel().id() == RequestsTab.getRequest(intArg - 1).getLevelData().getGDLevel().id()
						&& (isMod || String.valueOf(user).equalsIgnoreCase(RequestsTab.getRequest(i).getRequester()))) {
					response = "@" + user + ", " + RequestsTab.getRequest(i).getLevelData().getGDLevel().name() + " (" + RequestsTab.getRequest(i).getLevelData().getGDLevel().id() + ") has been removed!";
					int sel = LevelButton.selectedID-1;
					RequestsTab.removeRequest(i);

					RequestFunctions.saveFunction();
					RequestsTab.getLevelsPanel().setSelect(sel);
					RequestsTab.refreshInfoPanel();
					new Thread(() -> {
						RequestsTab.unloadComments(true);
						RequestsTab.loadComments(0, false);
					}).start();
					if (i == 0) {
						StringSelection selection = new StringSelection(
								String.valueOf(RequestsTab.getRequest(0).getLevelData().getGDLevel().id()));
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, selection);
					}
				}
			} catch (Exception ignored) {
			}
		}
		RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());
		return response;
	}

	@SuppressWarnings("unused")
	public static String removeLatest(String user) {
		String response = "";
		for (int i = RequestsTab.getQueueSize() - 1; i >= 0; i--) {
			try {
				if (String.valueOf(user).equalsIgnoreCase(RequestsTab.getRequest(i).getRequester())) {
					if (i == LevelButton.selectedID) {
						return "";
					}
					response = "@" + user + ", " + RequestsTab.getRequest(i).getLevelData().getGDLevel().name() + " (" + RequestsTab.getRequest(i).getLevelData().getGDLevel().id() + ") has been removed!";
					RequestsTab.removeRequest(i);
					RequestFunctions.saveFunction();
					break;
				}
			} catch (Exception ignored) {
			}
		}
		RequestsTab.getLevelsPanel().setWindowName(RequestsTab.getQueueSize());
		return response;
	}

	@SuppressWarnings("unused")
	public static long testForID(String message) {
		Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(message);
		if (m.find()) {
			try {
				String[] msgs = message.split(" ");
				String mention = "";
				for (String s : msgs) {
					if (s.contains("@")) {
						mention = s;
						break;
					}
				}
				if (!mention.contains(m.group(1))) {
					return Long.parseLong(m.group(1).replaceFirst("^0+(?!$)", ""));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	@SuppressWarnings("unused")
	public static void toggleBwomp() {
		bwomp = !bwomp;
	}

	@SuppressWarnings("unused")
	public static void bwomp() {
		Board.bwomp();
	}

	@SuppressWarnings("unused")
	public static void stopBwomp() {
		Board.stopBwomp();
	}

	@SuppressWarnings("unused")
	public static void movePosition(int position, int newPosition) {
		System.out.println(newPosition);
		RequestsTab.getLevelsPanel().movePosition(position, newPosition);
	}

	public static int getPosFromID(long ID) {
		for (int i = 0; i < RequestsTab.getQueueSize(); i++) {
			if (RequestsTab.getLevelsPanel().getButton(i).getID() == ID) {
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unused")
	public static String block(String user, String[] arguments) {
		String response;
		try {
			boolean start = false;
			int blockedID = Integer.parseInt(arguments[1]);
			if (blockedID == RequestsTab.getRequest(0).getLevelData().getGDLevel().id() && RequestsTab.getQueueSize() > 1) {
				StringSelection selection = new StringSelection(
						String.valueOf(RequestsTab.getRequest(1).getLevelData().getGDLevel().id()));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				start = true;
			}
			for (int i = 0; i < RequestsTab.getQueueSize(); i++) {
				if (RequestsTab.getRequest(i).getLevelData().getGDLevel().id() == blockedID) {
					RequestsTab.removeRequest(i);
					RequestsTab.refreshInfoPanel();
					new Thread(() -> {
						RequestsTab.unloadComments(true);
						RequestsTab.loadComments(0, false);
					}).start();
					RequestFunctions.saveFunction();
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
					return Utilities.format("$BLOCK_EXISTS_MESSAGE$", user);
				}
			}
			sc.close();

			response = Utilities.format("$BLOCK_MESSAGE$", user, arguments[1]);
			BlockedIDSettings.addBlockedLevel(arguments[1]);
			if (start) {
				RequestsTab.getLevelsPanel().setSelect(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = Utilities.format("$BLOCK_FAILED_MESSAGE$", user);
		}
		return response;
	}

	@SuppressWarnings("unused")
	public static String unblock(String user, String[] arguments) {
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
					BlockedIDSettings.removeBlockedLevel(arguments[1]);
					response = Utilities.format("$UNBLOCK_MESSAGE$", user, arguments[1]);
				} else {
					response = Utilities.format("$UNBLOCK_DOESNT_EXISTS_MESSAGE$", user);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			response = Utilities.format("$UNBLOCK_FAILED_MESSAGE$", user);
		}
		return response;
	}

	@SuppressWarnings("unused")
	public static String blockUser(String user, String[] arguments) {
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
					return Utilities.format("$BLOCK_USER_EXISTS_MESSAGE$", user);
				}
			}
			sc.close();

			response = Utilities.format("$BLOCK_USER_MESSAGE$", user, arguments[1]);
			BlockedUserSettings.addBlockedUser(arguments[1]);

		} catch (Exception e) {
			e.printStackTrace();
			response = Utilities.format("$BLOCK_USER_FAILED_MESSAGE$", user);
		}
		return response;
	}

	@SuppressWarnings("unused")
	public static String unblockUser(String user, String[] arguments) {
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

					response = Utilities.format("$UNBLOCK_USER_MESSAGE$", user, arguments[1]);
					BlockedUserSettings.removeBlockedUser(unblocked);
				} else {
					response = Utilities.format("$UNBLOCK_USER_DOESNT_EXISTS_MESSAGE$", user);

				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			response = Utilities.format("$UNBLOCK_USER_FAILED_MESSAGE$", user);
		}
		return response;
	}

	@SuppressWarnings("unused")

	public static String getHelp(String user, boolean isMod, String command) {
		String info = null;
		boolean infoExists = false;
		try {
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/commands/info.txt"))) {
				Scanner sc2 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/commands/info.txt").toFile());
				while (sc2.hasNextLine()) {
					String line = sc2.nextLine();
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
					if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(command)) {
						info = line.split("=")[1];
						break;
					}
				}
				is.close();
				isr.close();
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (info == null) {
			info = "$HELP_NO_INFO$";
		}
		return Utilities.format("$DEFAULT_MENTION$", user) + " " + info;
	}

	@SuppressWarnings("unused")
	public static String getHelp(String user, boolean isMod) {

		ArrayList<String> existingCommands = new ArrayList<>();
		ArrayList<String> disabledCommands = new ArrayList<>();
		ArrayList<String> modCommands = new ArrayList<>();

		try {
			URI uri = Objects.requireNonNull(Main.class.getResource("/Commands/")).toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = BotHandler.fileSystem.getPath("/Commands/");
			} else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/commands/");
			if (Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.endsWith(".js")) {
						existingCommands.add(fileName.substring(0, fileName.length() - 3).toLowerCase());
					}
				}
			}
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file;
				if (uri.getScheme().equals("jar")) {
					file = path.toString().split("/");
				} else {
					file = path.toString().split("\\\\");
				}
				String fileName = file[file.length - 1];
				if (fileName.endsWith(".js")) {
					if (!fileName.equalsIgnoreCase("!rick.js") &&
							!fileName.equalsIgnoreCase("!stoprick.js") &&
							!fileName.equalsIgnoreCase("!kill.js") &&
							!fileName.equalsIgnoreCase("!eval.js") &&
							!fileName.equalsIgnoreCase("!stop.js") &&
							!fileName.equalsIgnoreCase("!end.js") &&
							!fileName.equalsIgnoreCase("!kill.js") &&
							!fileName.equalsIgnoreCase("!popup.js") &&
							!fileName.equalsIgnoreCase("!gd.js") &&
							!fileName.equalsIgnoreCase("!what.js")) {
						String substring = fileName.substring(0, fileName.length() - 3);
						if (!existingCommands.contains(substring)) {
							existingCommands.add(substring.toLowerCase());
						}
					}
				}
			}
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/disable.txt"))) {
				Scanner sc2 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/disable.txt").toFile());
				while (sc2.hasNextLine()) {
					String line = sc2.nextLine();
					disabledCommands.add(line.toLowerCase());
				}
				if (!Settings.getSettings("gdMode").asBoolean()) {
					for (String command : gdCommands) {
						disabledCommands.add(command.toLowerCase());
					}
				}
				sc2.close();
			}
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/mod.txt"))) {
				Scanner sc2 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/mod.txt").toFile());
				while (sc2.hasNextLine()) {
					String line = sc2.nextLine();
					modCommands.add(line.toLowerCase());
				}
				sc2.close();
			}
			InputStream is = Main.class
					.getClassLoader().getResourceAsStream("Commands/mod.txt");
			assert is != null;
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				modCommands.add(line.toLowerCase());
			}
			is.close();
			isr.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		existingCommands.sort(String.CASE_INSENSITIVE_ORDER);

		StringBuilder message = new StringBuilder();
		message.append("$LIST_COMMANDS_START_MESSAGE$");


		for (String command : existingCommands) {
			if (!isMod && modCommands.contains(command)) {
				continue;
			}
			if (disabledCommands.contains(command)) {
				continue;
			}
			message.append(" | ").append(command);

		}
		return message.toString();
	}

	@SuppressWarnings("unused")
	public static Setting getOAuth() {
		return Settings.getSettings("oauth");
	}

	@SuppressWarnings("unused")
	public static String getClientID() {
		return "fzwze6vc6d2f7qodgkpq2w8nnsz3rl";
	}

	@SuppressWarnings("unused")
	public static void endGDBoard() {
		Main.close();
	}

	static String parseInfoString(String text, int level) {

		if (RequestsTab.getQueueSize() != 0) {

			Optional<String> creatorNameOptional = RequestsTab.getRequest(level).getLevelData().getGDLevel().creatorName();
			String creatorName = "";
			if(creatorNameOptional.isPresent()){
				creatorName = creatorNameOptional.get();
			}
			Optional<GDSong> songOptional = RequestsTab.getRequest(level).getLevelData().getGDLevel().song();
			String songTitle = "";
			String songArtist = "";
			String songID = "";

			if(songOptional.isPresent()){
				songTitle = songOptional.get().title();
				songArtist = songOptional.get().artist();
				songID = String.valueOf(songOptional.get().id());
			}

			text = text.replaceAll("(?i)%levelName%", RequestsTab.getRequest(level).getLevelData().getGDLevel().name())
					.replaceAll("(?i)%levelID%", String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().name()))
					.replaceAll("(?i)%levelAuthor%", creatorName)
					.replaceAll("(?i)%requester%", RequestsTab.getRequest(level).getLevelData().getRequester())
					.replaceAll("(?i)%songName%", songTitle)
					.replaceAll("(?i)%songID%", songID)
					.replaceAll("(?i)%songArtist%", songArtist)
					.replaceAll("(?i)%likes%", String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().likes()))
					.replaceAll("(?i)%downloads%", String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().downloads()))
					.replaceAll("(?i)%description%", RequestsTab.getRequest(level).getLevelData().getGDLevel().description())
					.replaceAll("(?i)%coins%", String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().coinCount()))
					.replaceAll("(?i)%objects%", String.valueOf(RequestsTab.getRequest(level).getLevelData().getGDLevel().objectCount()))
					.replaceAll("(?i)%queueSize%", String.valueOf(RequestsTab.getQueueSize()))
					.replaceAll("(?i)%s%", "");
			return text;
		} else {
			return Settings.getSettings("noLevelsString").asString().replaceAll("(?i)%s%", "");
		}
	}
}
