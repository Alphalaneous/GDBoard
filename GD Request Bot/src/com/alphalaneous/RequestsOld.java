package com.alphalaneous;

import com.alphalaneous.Panels.CommentsPanel;
import com.alphalaneous.Panels.InfoPanel;
import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.Panels.SongPanel;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.SettingsPanels.*;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.AuthenticatedGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.*;
import com.github.alex1304.jdash.exception.MissingAccessException;
import com.github.alex1304.jdash.exception.SpriteLoadException;
import com.github.alex1304.jdash.graphics.SpriteFactory;
import com.github.alex1304.jdash.util.GDUserIconSet;
import com.github.alex1304.jdash.util.LevelSearchFilters;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;


public class RequestsOld {

	private static HashMap<Long, Integer> addedLevels = new HashMap<>();
	static boolean bwomp = false;
	private static String os = (System.getProperty("os.name")).toUpperCase();
	private static HashMap<String, Integer> userStreamLimitMap = new HashMap<>();
	static HashMap<Long, String> globallyBlockedIDs = new HashMap<>();
	public static boolean enableRequests = true;

	private static String[] gdCommands = {"!gd", "!kill", "!block", "!blockuser", "!unblock", "!unblockuser", "!clear", "!info", "!move", "!next", "!position", "!queue", "!remove", "!request", "!song", "!stop", "!toggle", "!top", "!wronglevel"};

	static void forceAdd(LevelData data){
		Requests.levels.add(data);
	}


	static void forceAdd(String name, String author, long levelID, String difficulty, boolean epic, boolean featured, int stars, String requester, int gameVersion, int coins, String description, int likes, int downloads, String length, int levelVersion, int songID, String songName, String songAuthor, int objects, long original, boolean vulgar, boolean image, int password, String upload, String update, boolean verifiedCoins) {


		LevelData levelData = new LevelData();
		levelData.setName(name);
		levelData.setAuthor(author);
		levelData.setLevelID(levelID);
		levelData.setDifficulty(difficulty);
		levelData.setEpic(epic);
		if (featured) {
			levelData.setFeatured();
		}
		levelData.setMessage("Reloaded");
		levelData.setVeririedCoins(verifiedCoins);
		levelData.setStars(stars);
		levelData.setRequester(requester);
		levelData.setVersion(gameVersion);
		levelData.setCoins(coins);
		levelData.setDescription(description);
		levelData.setLikes(likes);
		levelData.setDownloads(downloads);
		levelData.setSongURL("");
		levelData.setLength(length);
		levelData.setLevelVersion(levelVersion);
		levelData.setPassword(password);
		levelData.setSongID(songID);
		levelData.setSongName(songName);
		levelData.setSongAuthor(songAuthor);
		levelData.setObjects(objects);
		levelData.setOriginal(original);
		if(update != null) {
			levelData.setUpdate(update);
			levelData.setUpload(upload);
		}
		GDUserIconSet iconSet;
		GDUser user;

		try {
			if (LoadGD.isAuth) {
				user = LoadGD.authClient.searchUser(levelData.getAuthor()).block();
			} else {
				user = LoadGD.anonClient.searchUser(levelData.getAuthor()).block();
			}
			assert user != null;
			iconSet = new GDUserIconSet(user, spriteFactory);
			BufferedImage icon = iconSet.generateIcon(user.getMainIconType());
			Image imgScaled = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon imgNew = new ImageIcon(imgScaled);
			levelData.setPlayerIcon(imgNew);
		} catch (MissingAccessException e) {
			user = LoadGD.anonClient.searchUser("RobTop").block();
			assert user != null;
			iconSet = new GDUserIconSet(user, spriteFactory);
			BufferedImage icon = iconSet.generateIcon(user.getMainIconType());
			Image imgScaled = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			ImageIcon imgNew = new ImageIcon(imgScaled);
			levelData.setPlayerIcon(imgNew);

		} catch (Exception e){
			levelData.setPlayerIcon(null);
		}
		if (vulgar) {
			levelData.setContainsVulgar();
		}
		if (image) {
			levelData.setContainsImage();
		}

		LevelsPanel.createButton(name, author, levelID, difficulty, epic, featured, stars, requester, gameVersion, levelData.getPlayerIcon(), coins, verifiedCoins);
		Requests.levels.add(levelData);
		if(Requests.levels.size() == 1){
			if(Requests.levels.get(0).getContainsImage()){
				Utilities.notify("Image Hack", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() +") possibly contains the image hack!");
			}
			else if(Requests.levels.get(0).getContainsVulgar()){
				Utilities.notify("Vulgar Language", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() +") contains vulgar language!");
			}
		}
		LevelsPanel.setName(Requests.levels.size());
		levelData.setAnalyzed();
		LevelsPanel.updateUI(levelID, vulgar, image, true);
		OutputSettings.setOutputStringFile(parseInfoString(OutputSettings.outputString, 0));
		InfoPanel.refreshInfo();
	}

	private static SpriteFactory spriteFactory;

	static {
		try {
			spriteFactory = SpriteFactory.create();
		} catch (SpriteLoadException e) {
			e.printStackTrace();
		}
	}


	public static void addRequest(long ID, String requester, boolean isMod, String message, String messageID) {
		if(ID > 999999999 || ID < 1){
			return;
		}
		if(!Main.allowRequests){
			return;
		}
		OutputSettings.setOutputStringFile(parseInfoString(OutputSettings.outputString, 0));

		if (enableRequests) {
			Path blocked = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
			Path logged = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
			Path blockedUser = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blockedUsers.txt");
			Path blockedGDUser = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blockedGDUsers.txt");
			Path disallowed = Paths.get(Defaults.saveDirectory + "\\GDBoard\\disallowedStrings.txt");
			Path allowed = Paths.get(Defaults.saveDirectory + "\\GDBoard\\allowedStrings.txt");

			boolean bypass;
			bypass = GeneralSettings.modsBypassOption && isMod;
			if(requester.equalsIgnoreCase(Settings.getSettings("channel")) && GeneralSettings.streamerBypassOption){
				bypass = true;
			}
			for (int k = 0; k < Requests.levels.size(); k++) {

				if (ID == Requests.levels.get(k).getLevelID()) {
					int j = k + 1;
					if(!GeneralSettings.disableShowPositionOption) {
						Main.sendMessage(Utilities.format("$ALREADY_IN_QUEUE_MESSAGE$", requester, j));
					}
					else {
						Main.sendMessage(Utilities.format("$ALREADY_IN_QUEUE_MESSAGE_ALT$", requester));
					}
					return;
				}
			}
			if (Main.programLoaded && !bypass) {
				if (GeneralSettings.followersOption) {
					if (APIs.isNotFollowing(requester)) {
						Main.sendMessage(Utilities.format("$FOLLOW_MESSAGE$", requester));
						return;
					}
				}
				if (ID < RequestSettings.minID && RequestSettings.minIDOption) {
					Main.sendMessage(Utilities.format("$MIN_ID_MESSAGE$", requester, RequestSettings.minID));
					return;
				}
				if (ID > RequestSettings.maxID && RequestSettings.maxIDOption) {
					Main.sendMessage(Utilities.format("$MAX_ID_MESSAGE$", requester, RequestSettings.maxID));
					return;
				}
				if (GeneralSettings.queueLimitBoolean && (Requests.levels.size() >= GeneralSettings.queueLimit)) {
					if (!GeneralSettings.queueFullOption) {
						Main.sendMessage(Utilities.format("$QUEUE_FULL_MESSAGE$", requester));
					}
					return;
				}
				if(globallyBlockedIDs.containsKey(ID)){
					Main.sendMessage(Utilities.format("$GLOBALLY_BLOCKED_LEVEL_MESSAGE$", requester, globallyBlockedIDs.get(ID)));
					return;
				}
				if (GeneralSettings.userLimitOption) {
					int size = 0;
					for (LevelData level : Requests.levels) {
						if (level.getRequester().equalsIgnoreCase(requester)) {
							size++;
						}
					}
					if (size >= GeneralSettings.userLimit) {
						Main.sendMessage(Utilities.format("$MAXIMUM_LEVELS_MESSAGE$", requester));
						return;
					}
				}
				if (GeneralSettings.userLimitStreamOption) {
					if (userStreamLimitMap.containsKey(requester)) {
						if (userStreamLimitMap.get(requester) >= GeneralSettings.userLimitStream) {
							Main.sendMessage(Utilities.format("$MAXIMUM_LEVELS_STREAM_MESSAGE$", requester));
							return;
						}
					}
				}
				if (Files.exists(logged) && (GeneralSettings.repeatedOptionAll && !GeneralSettings.updatedRepeatedOption) && Main.programLoaded) {
					Scanner sc = null;
					try {
						sc = new Scanner(logged.toFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					assert sc != null;
					while (sc.hasNextLine()) {
						if (String.valueOf(ID).equals(sc.nextLine().split(",")[0])) {
							sc.close();
							Main.sendMessage(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", requester));
							return;
						}
					}
					sc.close();
				}
				if (addedLevels.containsKey(ID) && (GeneralSettings.repeatedOption && !GeneralSettings.updatedRepeatedOption)) {
					Main.sendMessage(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", requester));
					return;
				}

				if (Files.exists(blocked)) {
					Scanner sc = null;
					try {
						sc = new Scanner(blocked.toFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					assert sc != null;
					while (sc.hasNextLine()) {
						if (String.valueOf(ID).equals(sc.nextLine())) {
							sc.close();
							Main.sendMessage(Utilities.format("$BLOCKED_LEVEL_MESSAGE$", requester));
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
					assert sc != null;
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

			GDLevel level;
			GDUser user;

			if (LoadGD.isAuth) {
				try {
					level = LoadGD.authClient.getLevelById(ID).block();
				} catch (MissingAccessException | NumberFormatException e) {
					Main.sendMessage(Utilities.format("$LEVEL_ID_DOESNT_EXIST_MESSAGE$", requester));
					return;
				} catch (Exception e) {
					e.printStackTrace();
					Main.sendMessage(Utilities.format("$SEARCH_FAILED$", requester));
					return;
				}
			} else {
				try {
					level = LoadGD.anonClient.getLevelById(ID).block();
				} catch (MissingAccessException | NumberFormatException e) {
					Main.sendMessage(Utilities.format("$LEVEL_ID_DOESNT_EXIST_MESSAGE$", requester));
					return;
				} catch (Exception e) {
					e.printStackTrace();
					Main.sendMessage(Utilities.format("$SEARCH_FAILED$", requester));
					return;
				}
			}

			LevelData levelData = new LevelData();
			// --------------------
			Thread parse = null;
			if (Main.programLoaded && !bypass) {
				if (level != null && Files.exists(blockedGDUser)) {
					Scanner sc = null;
					try {
						sc = new Scanner(blockedGDUser.toFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					assert sc != null;
					while (sc.hasNextLine()) {
						if (level.getCreatorName().equalsIgnoreCase(sc.nextLine())) {
							Main.sendMessage(Utilities.format("$BLOCKED_CREATOR_MESSAGE$", requester));
							sc.close();
							return;
						}
					}
					sc.close();
				}
				if (level != null && RequestSettings.allowOption) {
					if (Files.exists(allowed)) {
						Scanner sc = null;
						try {
							sc = new Scanner(allowed.toFile());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						boolean hasWord = false;
						assert sc != null;
						while (sc.hasNextLine()) {
							if (level.getName().toLowerCase().contains(sc.nextLine().toLowerCase())) {
								hasWord = true;
								sc.close();
								break;
							}
						}
						if (!hasWord) {
							Main.sendMessage(Utilities.format("$BLOCKED_NAME_MESSAGE$", requester));
							return;
						}
						sc.close();
					}
				}
				if (level != null && RequestSettings.disallowOption) {
					if (Files.exists(disallowed)) {
						Scanner sc = null;
						try {
							sc = new Scanner(disallowed.toFile());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						assert sc != null;
						while (sc.hasNextLine()) {
							if (level.getName().toLowerCase().contains(sc.nextLine().toLowerCase())) {
								sc.close();
								Main.sendMessage(Utilities.format("$BLOCKED_NAME_MESSAGE$", requester));
								return;
							}
						}
						sc.close();
					}
				}

				if (level != null && RequestSettings.ratedOption && !(level.getStars() > 0)) {
					Main.sendMessage(Utilities.format("$STAR_RATED_MESSAGE$", requester));
					return;
				}
				if (level != null && RequestSettings.unratedOption && level.getStars() > 0) {
					Main.sendMessage(Utilities.format("$UNRATED_MESSAGE$", requester));
					return;
				}
				if (level != null && RequestSettings.minObjectsOption && level.getObjectCount() < RequestSettings.minObjects) {
					Main.sendMessage(Utilities.format("$FEW_OBJECTS_MESSAGE$", requester));
					return;
				}
				if (level != null && RequestSettings.maxObjectsOption && level.getObjectCount() > RequestSettings.maxObjects) {
					Main.sendMessage(Utilities.format("$MANY_OBJECTS_MESSAGE$", requester));
					return;
				}
				assert level != null;
				if (level.getObjectCount() != 0) {
					if (RequestSettings.minLikesOption && level.getObjectCount() < RequestSettings.minLikes) {
						Main.sendMessage(Utilities.format("$FEW_LIKES_MESSAGE$", requester));
						return;
					}
					if (RequestSettings.maxLikesOption && level.getObjectCount() > RequestSettings.maxLikes) {
						Main.sendMessage(Utilities.format("$MANY_LIKES_MESSAGE$", requester));
						return;
					}
				}
			}
			levelData.setRequester(requester);
			levelData.setAuthor(Objects.requireNonNull(level).getCreatorName());
			levelData.setName(level.getName());
			levelData.setMessage(message);
			if(messageID != null) {
				levelData.setMessageID(messageID);
			}
			levelData.setDifficulty(level.getDifficulty().toString());
			levelData.setDescription(level.getDescription());
			levelData.setLikes(level.getLikes());
			levelData.setDownloads(level.getDownloads());
			levelData.setSongURL(Objects.requireNonNull(level.getSong().block()).getDownloadURL());
			levelData.setLength(level.getLength().toString());
			levelData.setLevelID(ID);
			levelData.setVersion(level.getGameVersion());
			levelData.setLevelVersion(level.getLevelVersion());
			levelData.setVeririedCoins(level.hasCoinsVerified());
			levelData.setEpic(level.isEpic());
			levelData.setSongID((int) Objects.requireNonNull(level.getSong().block()).getId());
			levelData.setStars(level.getStars());
			levelData.setSongName(Objects.requireNonNull(level.getSong().block()).getSongTitle());
			levelData.setSongAuthor(Objects.requireNonNull(level.getSong().block()).getSongAuthorName());
			levelData.setObjects(level.getObjectCount());
			levelData.setOriginal(level.getOriginalLevelID());
			levelData.setCoins(level.getCoinCount());


			if (Files.exists(logged) && (GeneralSettings.updatedRepeatedOption && Main.programLoaded && !bypass)) {
				Scanner sc = null;
				try {
					sc = new Scanner(logged.toFile());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				assert sc != null;
				while (sc.hasNextLine()) {
					String levelLine = sc.nextLine();

					if (String.valueOf(ID).equals(levelLine.split(",")[0])) {
						int version;
						if(levelLine.split(",").length == 1){
							version = 1;
						}
						else{
							version = Integer.parseInt(levelLine.split(",")[1]);
						}
						if(version >= levelData.getLevelVersion()) {
							sc.close();
							Main.sendMessage(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", requester));
							return;
						}
					}
				}
				sc.close();
			}
			if (addedLevels.containsKey(ID) && (GeneralSettings.updatedRepeatedOption && Main.programLoaded && !bypass)) {
				if(addedLevels.get(ID) >= levelData.getLevelVersion()) {
					Main.sendMessage(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", requester));
					return;
				}
			}


			GDUserIconSet iconSet;
			try {
				if (LoadGD.isAuth) {
					user = LoadGD.authClient.searchUser(levelData.getAuthor()).block();
				} else {
					user = LoadGD.anonClient.searchUser(levelData.getAuthor()).block();
				}
				assert user != null;
				iconSet = new GDUserIconSet(user, spriteFactory);
			} catch (MissingAccessException e) {
				user = LoadGD.anonClient.searchUser("RobTop").block();
				assert user != null;
				iconSet = new GDUserIconSet(user, spriteFactory);
			}
			BufferedImage icon = iconSet.generateIcon(user.getMainIconType());
			Image imgScaled = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
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
			if (Main.programLoaded && !bypass) {
				if (RequestSettings.excludedDifficulties.contains(levelData.getDifficulty().toLowerCase()) && RequestSettings.disableOption) {
					Main.sendMessage(Utilities.format("$DIFFICULTY_MESSAGE$", requester));
					return;
				}
				if (RequestSettings.excludedLengths.contains(levelData.getLength().toLowerCase()) && RequestSettings.disableLengthOption) {
					Main.sendMessage(Utilities.format("$LENGTH_MESSAGE$", requester));
					return;
				}
			}

			if (levelData.getDescription().toLowerCase().contains("nong")) {
				String[] words = levelData.getDescription().split(" ");
				for (String word : words) {
					if (isValidURL(word)) {
						levelData.setSongURL(word);
					}
				}
			}
			if (LoadGD.isAuth) {
				AuthenticatedGDClient finalClient = LoadGD.authClient;
				parse = new Thread(() -> {
					Object object;
					try {
						object = Objects.requireNonNull(finalClient.getLevelById(ID).block()).download().block();

						if (!(level.getStars() > 0) && level.getGameVersion() / 10 >= 2) {
							parse(((GDLevelData) Objects.requireNonNull(object)).getData(), ID);
						}
						levelData.setPassword(((GDLevelData) Objects.requireNonNull(object)).getPass());
						levelData.setUpload(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getUploadTimestamp()));
						levelData.setUpdate(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getLastUpdatedTimestamp()));
						InfoPanel.refreshInfo();
						Functions.saveFunction();
						LevelsPanel.refreshSelectedLevel(ID);
					} catch (Exception e) {
						LoadGD.isAuth = false;

					}
				});
			}
			if (!LoadGD.isAuth) {
				AnonymousGDClient finalClient = LoadGD.anonClient;
				parse = new Thread(() -> {
					Object object = Objects.requireNonNull(finalClient.getLevelById(ID).block()).download().block();
					if (!(level.getStars() > 0) && level.getGameVersion() / 10 >= 2) {
						parse(((GDLevelData) Objects.requireNonNull(object)).getData(), ID);
					}
					levelData.setPassword(((GDLevelData) Objects.requireNonNull(object)).getPass());
					levelData.setUpload(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getUploadTimestamp()));
					levelData.setUpdate(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getLastUpdatedTimestamp()));
					InfoPanel.refreshInfo();
					LevelsPanel.refreshSelectedLevel(ID);
					Functions.saveFunction();
				});

			}
			parse.start();
			if (os.contains("WIN")) {
				if (GeneralSettings.autoDownloadOption) {
					new Thread(() -> {
						Path songFile = Paths.get(System.getenv("LOCALAPPDATA") + "\\GeometryDash\\" + levelData.getSongID() + ".mp3");
						if (!Files.exists(songFile)) {
							try {
								FileUtils.copyURLToFile(levelData.getSongURL(), songFile.toFile());
							} catch (IOException ignored) {
							}
						}
					}).start();
				}
			}
			Requests.levels.add(levelData);
			LevelsPanel.createButton(levelData.getName(), levelData.getAuthor(), levelData.getLevelID(),
					levelData.getDifficulty(), levelData.getEpic(), levelData.getFeatured(), levelData.getStars(),
					levelData.getRequester(), levelData.getVersion(), levelData.getPlayerIcon(), levelData.getCoins(),
					levelData.getVerifiedCoins());

			LevelsPanel.setName(Requests.levels.size());
			Functions.saveFunction();
			if (Main.sendMessages) {
				if(!GeneralSettings.confirmOption) {
					if (Requests.levels.size() != 1) {
						if (!GeneralSettings.disableShowPositionOption) {
							Main.sendMessage(Utilities.format("$CONFIRMATION_MESSAGE$",
									levelData.getRequester(),
									levelData.getName(),
									levelData.getLevelID(),
									Requests.levels.size()), GeneralSettings.confirmWhisperOption, requester);
						} else {
							Main.sendMessage(Utilities.format("$CONFIRMATION_MESSAGE_ALT$",
									levelData.getRequester(),
									levelData.getName(),
									levelData.getLevelID()), GeneralSettings.confirmWhisperOption, requester);
						}
					}
				}
			}
			if (Requests.levels.size() == 1) {
				StringSelection selection = new StringSelection(String.valueOf(Requests.levels.get(0).getLevelID()));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				if (Main.sendMessages) {
					if (!GeneralSettings.nowPlayingOption) {
						Main.sendMessage(Utilities.format("$NOW_PLAYING_TOP_MESSAGE$",
								Requests.levels.get(0).getRequester(),
								Requests.levels.get(0).getName(),
								Requests.levels.get(0).getLevelID()));
					}
				}
			}
			addedLevels.put(ID, levelData.getLevelVersion());
			OutputSettings.setOutputStringFile(parseInfoString(OutputSettings.outputString, 0));
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
			try {
				boolean exists = false;
				if (!Files.exists(file)) {
					Files.createFile(file);
				}
				String value = null;
				if (Files.exists(logged)) {
					Scanner sc = new Scanner(logged.toFile());
					while (sc.hasNextLine()) {
						value = sc.nextLine();
						if (String.valueOf(ID).equals(value.split(",")[0])) {
							sc.close();
							exists = true;
							break;
						}
						Thread.sleep(5);
					}
					sc.close();
				}
				if (!exists) {
					Files.write(
							file,
							(ID + "," + levelData.getLevelVersion()  + "\n").getBytes(),
							StandardOpenOption.APPEND);

				}
				else {
					BufferedReader fileA = new BufferedReader(new FileReader(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt"));
					StringBuilder inputBuffer = new StringBuilder();
					String line;
					while ((line = fileA.readLine()) != null) {
						inputBuffer.append(line);
						inputBuffer.append('\n');
					}
					fileA.close();

					FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
					fileOut.write(inputBuffer.toString().replace(value, ID+","+levelData.getLevelVersion()).getBytes());
					fileOut.close();
				}

			} catch (IOException e1) {
				DialogBox.showDialogBox("Error!", e1.toString(), "There was an error writing to the file!", new String[]{"OK"});

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			Main.sendMessage(Utilities.format("$REQUESTS_OFF_MESSAGE$", requester));
		}
	}

	@SuppressWarnings("unused")
	public static String getLevel(int level, String attribute) {

		String result = "";
		try {
			switch (attribute) {
				case "name":
					result = Requests.levels.get(level).getName();
					break;
				case "id":
					result = String.valueOf(Requests.levels.get(level).getLevelID());
					break;
				case "author":
					result = Requests.levels.get(level).getAuthor();
					break;
				case "requester":
					result = Requests.levels.get(level).getRequester();
					break;
				case "difficulty":
					result = Requests.levels.get(level).getDifficulty();
					break;
				case "likes":
					result = String.valueOf(Requests.levels.get(level).getLikes());
					break;
				case "downloads":
					result = String.valueOf(Requests.levels.get(level).getDownloads());
					break;
				case "description":
					result = Requests.levels.get(level).getDescription();
					break;
				case "songName":
					result = Requests.levels.get(level).getSongName();
					break;
				case "songID":
					result = String.valueOf(Requests.levels.get(level).getSongID());
					break;
				case "songAuthor":
					result = Requests.levels.get(level).getSongAuthor();
					break;
				case "songURL":
					result = String.valueOf(Requests.levels.get(level).getSongURL());
					break;
				case "stars":
					result = String.valueOf(Requests.levels.get(level).getStars());
					break;
				case "epic":
					result = String.valueOf(Requests.levels.get(level).getEpic());
					break;
				case "version":
					result = String.valueOf(Requests.levels.get(level).getVersion());
					break;
				case "length":
					result = Requests.levels.get(level).getLength();
					break;
				case "coins":
					result = String.valueOf(Requests.levels.get(level).getCoins());
					break;
				case "objects":
					result = String.valueOf(Requests.levels.get(level).getObjects());
					break;
				case "original":
					result = String.valueOf(Requests.levels.get(level).getOriginal());
					break;
				case "image":
					result = String.valueOf(Requests.levels.get(level).getContainsImage());
					break;
				case "vulgar":
					result = String.valueOf(Requests.levels.get(level).getContainsVulgar());
					break;
				case "password":
					result = String.valueOf(Requests.levels.get(level).getPassword());
					break;
				case "levelVersion":
					result = String.valueOf(Requests.levels.get(level).getLevelVersion());
					break;
				case "upload":
					result = Requests.levels.get(level).getUpload();
					break;
				case "update":
					result = Requests.levels.get(level).getUpdate();
					break;
			}
		} catch (Exception e) {
			result = "Exception: " + e.toString();
		}
		return result;
	}

	@SuppressWarnings("unused")
	public static int getSelection(){
		return LevelsPanel.getSelectedID();
	}

	public static int getSize() {
		return Requests.levels.size();
	}

	@SuppressWarnings("unused")
	public static void kill() {
		GDMod.run("kill");
	}

	@SuppressWarnings("unused")
	public static void crash() {
		try {
			ProcessBuilder pb = new ProcessBuilder("taskkill", "/IM", "GeometryDash.exe", "/F").redirectErrorStream(true);
			pb.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static void toggleRequests() {
		Functions.requestsToggleFunction();
	}

	@SuppressWarnings("unused")
	public static void clear() {
		for (int i = 0; i < Requests.levels.size(); i++) {
			LevelsPanel.removeButton();
		}
		Requests.levels.clear();
		Functions.saveFunction();

		SongPanel.refreshInfo();
		InfoPanel.refreshInfo();
		CommentsPanel.unloadComments(true);
		LevelsPanel.setName(Requests.levels.size());
	}

	public static String remove(String user, boolean isMod, int intArg) {
		if(intArg-1 == LevelsPanel.getSelectedID()){
			return "";
		}
		String response = "";
		for (int i = 0; i < Requests.levels.size(); i++) {
			try {
				if (Requests.levels.get(i).getLevelID() == Requests.levels.get(intArg - 1).getLevelID()
						&& (isMod || String.valueOf(user).equalsIgnoreCase(Requests.levels.get(i).getRequester()))) {
					response = "@" + user + ", " + Requests.levels.get(i).getName() + " (" + Requests.levels.get(i).getLevelID() + ") has been removed!";
					LevelsPanel.removeButton(i);
					Requests.levels.remove(i);
					Functions.saveFunction();
					SongPanel.refreshInfo();
					InfoPanel.refreshInfo();
					LevelsPanel.setOneSelect();
					new Thread(() -> {
						CommentsPanel.unloadComments(true);
						CommentsPanel.loadComments(0, false);
					}).start();
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
		LevelsPanel.setName(Requests.levels.size());
		return response;
	}

	@SuppressWarnings("unused")
	public static String removeLatest(String user) {
		String response = "";
		for (int i = Requests.levels.size() - 1; i >= 0; i--) {
			try {
				if (String.valueOf(user).equalsIgnoreCase(Requests.levels.get(i).getRequester())) {
					if(i == LevelsPanel.getSelectedID()){
						return "";
					}
					response = "@" + user + ", " + Requests.levels.get(i).getName() + " (" + Requests.levels.get(i).getLevelID() + ") has been removed!";
					LevelsPanel.removeButton(i);
					Requests.levels.remove(i);
					Functions.saveFunction();
					SongPanel.refreshInfo();
					InfoPanel.refreshInfo();
					LevelsPanel.setOneSelect();
					new Thread(() -> {
						CommentsPanel.unloadComments(true);
						CommentsPanel.loadComments(0, false);
					}).start();
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
		LevelsPanel.setName(Requests.levels.size());
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
	public static void rick() {
		Board.rick();
	}

	@SuppressWarnings("unused")
	public static void stopRick() {
		Board.stopRick();
	}

	@SuppressWarnings("unused")
	public static void toggleBwomp() {
		bwomp = !bwomp;
	}

	@SuppressWarnings("unused")
	public static void knock() {
		Board.knock();
	}

	@SuppressWarnings("unused")
	public static void stopKnock() {
		Board.stopKnock();
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
		LevelsPanel.movePosition(position, newPosition);
	}

	public static int getPosFromID(long ID) {
		for (int i = 0; i < LevelsPanel.getSize(); i++) {
			if (LevelsPanel.getButton(i).getID() == ID) {
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
			if (blockedID == Requests.levels.get(0).getLevelID() && Requests.levels.size() > 1) {
				StringSelection selection = new StringSelection(
						String.valueOf(Requests.levels.get(1).getLevelID()));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				start = true;
			}
			for (int i = 0; i < Requests.levels.size(); i++) {
				if (Requests.levels.get(i).getLevelID() == blockedID) {
					LevelsPanel.removeButton(i);
					Requests.levels.remove(i);
					InfoPanel.refreshInfo();
					SongPanel.refreshInfo();
					new Thread(() -> {
						CommentsPanel.unloadComments(true);
						CommentsPanel.loadComments(0, false);
					}).start();
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
					return Utilities.format("$BLOCK_EXISTS_MESSAGE$", user);
				}
			}
			sc.close();

			try {
				Files.write(file, (blockedID + "\n").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			response = Utilities.format("$BLOCK_MESSAGE$", user, arguments[1]);
			BlockedSettings.addButton(Long.parseLong(arguments[1]));
			if (start) {
				LevelsPanel.setOneSelect();
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
					response = Utilities.format("$UNBLOCK_MESSAGE$", user, arguments[1]);
					BlockedSettings.removeID(unblocked);
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
			try {
				Files.write(file, (blockedUser + "\n").getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			response = Utilities.format("$BLOCK_USER_MESSAGE$", user, arguments[1]);
			BlockedUserSettings.addButton(arguments[1]);

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
					response = Utilities.format("$UNBLOCK_USER_MESSAGE$", user, arguments[1]);
					BlockedUserSettings.removeUser(unblocked);
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
			URI uri = Main.class.getResource("/Resources/Commands/").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = BotHandler.fileSystem.getPath("/Resources/Commands/");
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
						existingCommands.add(fileName.substring(0, fileName.length()-3).toLowerCase());
					}
				}
			}
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file;
				if(uri.getScheme().equals("jar")){
					file = path.toString().split("/");
				}
				else{
					file = path.toString().split("\\\\");
				}
				String fileName = file[file.length - 1];
				if (fileName.endsWith(".js")) {
					if(!fileName.equalsIgnoreCase("!rick.js") &&
							!fileName.equalsIgnoreCase("!stoprick.js") &&
							!fileName.equalsIgnoreCase("!kill.js") &&
							!fileName.equalsIgnoreCase("!eval.js") &&
							!fileName.equalsIgnoreCase("!stop.js") &&
							!fileName.equalsIgnoreCase("!end.js") &&
							!fileName.equalsIgnoreCase("!kill.js") &&
							!fileName.equalsIgnoreCase("!popup.js") &&
							!fileName.equalsIgnoreCase("!gd.js")) {
						String substring = fileName.substring(0, fileName.length() - 3);
						if(!existingCommands.contains(substring)) {
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
				if(!GeneralSettings.gdModeOption){
					for(String command : gdCommands){
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
						.getClassLoader().getResourceAsStream("Resources/Commands/mod.txt");
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

		}
		catch (Exception e){
			e.printStackTrace();
		}
		existingCommands.sort(String.CASE_INSENSITIVE_ORDER);

		StringBuilder message = new StringBuilder();
		message.append("$LIST_COMMANDS_START_MESSAGE$");


		for(String command : existingCommands){
			if(!isMod && modCommands.contains(command)){
				continue;
			}
			if(disabledCommands.contains(command)){
				continue;
			}
			message.append(" | ").append(command);

		}
		return message.toString();
	}

	@SuppressWarnings("unused")
	public static String getOAuth() {
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

	//@SuppressWarnings("unused")
	/*public static String request(String user, boolean isMod, boolean isSub, String[] arguments) {
		String response = "";
		if(GeneralSettings.gdModeOption) {

			Matcher m = null;

			if (arguments.length > 1) {
				try {
					m = Pattern.compile("(\\d+)").matcher(arguments[1]);
				} catch (Exception ignored) {
				}
				assert m != null;
				if (m.matches() && arguments.length <= 2) {
					try {
						Requests.addRequest(Long.parseLong(m.group(1)), user, isMod, "Sent via Command", null);
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
												user, isMod, "Sent via Command", null);
										break outerLoop;
									}
								}
							}

						} catch (IndexOutOfBoundsException ignored) {
						} catch (MissingAccessException e) {
							response = Utilities.format("$LEVEL_USER_DOESNT_EXIST_MESSAGE$", user);
							e.printStackTrace();
						}
					} else {

						AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
						try {
							Requests.addRequest(Objects.requireNonNull(client.searchLevels(message.toString(), LevelSearchFilters.create(), 0)
											.block()).asList().get(0).getId(),
									user, isMod, "Sent via Command", null);
						} catch (MissingAccessException e) {
							response = Utilities.format("$LEVEL_DOESNT_EXIST_MESSAGE$", user);
						} catch (Exception e) {
							e.printStackTrace();
							response = Utilities.format("$REQUEST_ERROR$", user);

						}
					}
				}
			} else {
				response = Utilities.format("$SPECIFY_ID_MESSAGE$", user);
			}
		}
		return response;
		//}*/
	//}

	static void parse(byte[] level, long levelID) {
		boolean image = false;
		all:
		for (int k = 0; k < Requests.levels.size(); k++) {

			if (Requests.levels.get(k).getLevelID() == levelID) {
				StringBuilder decompressed = null;
				try {
					decompressed = decompress(level);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int imageIDCount = 0;
				String color = "";
				assert decompressed != null;
				String[] values = decompressed.toString().split(";");
				Requests.levels.get(k).setObjects(values.length);
				if ((values.length < RequestSettings.minObjects) && RequestSettings.minObjectsOption) {
					Main.sendMessage(Utilities.format("$TOO_FEW_OBJECTS_MESSAGE$", Requests.levels.get(k).getRequester()));
					LevelsPanel.removeButton(k);
					Requests.levels.remove(k);
					return;
				}
				if ((values.length > RequestSettings.maxObjects) && RequestSettings.maxObjectsOption) {
					Main.sendMessage(Utilities.format("$TOO_MANY_OBJECTS_MESSAGE$", Requests.levels.get(k).getRequester()));
					LevelsPanel.removeButton(k);
					Requests.levels.remove(k);
					return;
				}
				for (String value1 : values) {
					if (GeneralSettings.lowCPUMode) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
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
								image = true;
							}
							color = tempColor;
							is.close();
							isr.close();
							br.close();
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
					URL ids = new URL("https://raw.githubusercontent.com/Alphatism/GDBoard/Master/GD%20Request%20Bot/External/false%20positives.txt");
					Scanner s = new Scanner(ids.openStream());
					while (s.hasNextLine()) {
						String lineA = s.nextLine();
						if (lineA.equalsIgnoreCase(String.valueOf(levelID))) {
							image = false;
							break;
						}
					}
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (image) {
					Requests.levels.get(k).setContainsImage();
				}
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Requests.levels.get(k).setAnalyzed();
					LevelsPanel.updateUI(Requests.levels.get(k).getLevelID(), Requests.levels.get(k).getContainsVulgar(), Requests.levels.get(k).getContainsImage(), true);
				} catch (IndexOutOfBoundsException ignored) {
				}
				if(k == 0){
					if(Requests.levels.get(0).getContainsImage()){
						Utilities.notify("Image Hack", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() +") possibly contains the image hack!");
					}
					else if(Requests.levels.get(0).getContainsVulgar()){
						Utilities.notify("Vulgar Language", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() +") contains vulgar language!");
					}
				}
				break;
			}
		}
	}

	private static StringBuilder decompress(byte[] compressed) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
		GZIPInputStream gis = new GZIPInputStream(bis);
		BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		bis.close();
		br.close();
		gis.close();
		bis.close();
		return sb;
	}

	static String parseInfoString(String text, int level) {
		if (Requests.levels.size() != 0) {
			text = text.replaceAll("(?i)%levelName%", Requests.levels.get(level).getName())
					.replaceAll("(?i)%levelID%", String.valueOf( Requests.levels.get(level).getLevelID()))
					.replaceAll("(?i)%levelAuthor%",  Requests.levels.get(level).getAuthor())
					.replaceAll("(?i)%requester%",  Requests.levels.get(level).getRequester())
					.replaceAll("(?i)%songName%",  Requests.levels.get(level).getSongName())
					.replaceAll("(?i)%songID%", String.valueOf( Requests.levels.get(level).getSongID()))
					.replaceAll("(?i)%songArtist%",  Requests.levels.get(level).getSongAuthor())
					.replaceAll("(?i)%likes%", String.valueOf( Requests.levels.get(level).getLikes()))
					.replaceAll("(?i)%downloads%", String.valueOf( Requests.levels.get(level).getDownloads()))
					.replaceAll("(?i)%description%",  Requests.levels.get(level).getDescription())
					.replaceAll("(?i)%coins%", String.valueOf( Requests.levels.get(level).getCoins()))
					.replaceAll("(?i)%objects%", String.valueOf( Requests.levels.get(level).getObjects()))
					.replaceAll("(?i)%queueSize%", String.valueOf( Requests.levels.size()))
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
