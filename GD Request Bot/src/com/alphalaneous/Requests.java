package com.alphalaneous;

import com.alphalaneous.Panels.InfoPanel;
import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.SettingsPanels.GeneralSettings;
import com.alphalaneous.SettingsPanels.OutputSettings;
import com.alphalaneous.SettingsPanels.RequestSettings;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.entity.GDLevelData;
import com.github.alex1304.jdash.entity.GDUser;
import com.github.alex1304.jdash.exception.GDClientException;
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Requests {

	public static ArrayList<LevelData> levels = new ArrayList<>();
	private static HashMap<Long, Integer> addedLevels = new HashMap<>();
	private static HashMap<String, Integer> userStreamLimitMap = new HashMap<>();
	static HashMap<Long, String> globallyBlockedIDs = new HashMap<>();
	private static boolean processingLevel = false;
	public static boolean requestsEnabled = true;
	private static Path logged = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
	private static Path disallowed = Paths.get(Defaults.saveDirectory + "\\GDBoard\\disallowedStrings.txt");
	private static Path allowed = Paths.get(Defaults.saveDirectory + "\\GDBoard\\allowedStrings.txt");
	private static SpriteFactory spriteFactory;

	static {
		try {
			spriteFactory = SpriteFactory.create();
		} catch (SpriteLoadException e) {
			e.printStackTrace();
		}
	}

	public static void addRequest(long IDa, String user, boolean isMod, boolean isSub, String message, String messageID, boolean isCommand) {
		new Thread(() -> {
			try {
				if (!Main.allowRequests) {
					processingLevel = false;
					return;
				}
				if (!requestsEnabled) {
					sendUnallowed(Utilities.format("$REQUESTS_OFF_MESSAGE$", user));
					return;
				}
				long ID = IDa;
				GDLevel level = null;

				ArrayList<String> arguments = new ArrayList<>();
				arguments.add(""); //Accidentally started array at one due to value I thought existed, easier to add dummy value than change everything.
				Matcher argMatcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message);
				while (argMatcher.find()) {
					arguments.add(argMatcher.group(1).toLowerCase().trim());
				}
				String levelNameS; //Starting level name for search
				String usernameS; //starting username for search
				if (isCommand) {
					Matcher IDMatcher = Pattern.compile("(\\d+)").matcher(arguments.get(1));
					if (IDMatcher.matches() && arguments.size() <= 2) {
						ID = Long.parseLong(IDMatcher.group(1));
						if (ID > 999999999 || ID < 1) {
							processingLevel = false;
							return;
						}

						try {
							if (LoadGD.isAuth) {
								level = Objects.requireNonNull(LoadGD.authClient.getLevelById(ID).block());
							} else {
								level = Objects.requireNonNull(LoadGD.anonClient.getLevelById(ID).block());
							}
						} catch (MissingAccessException | NumberFormatException e) {
							sendUnallowed(Utilities.format("$LEVEL_ID_DOESNT_EXIST_MESSAGE$", user));
							return;
						} catch (GDClientException e) {
							sendError(Utilities.format("$SEARCH_FAILED$", user));
							return;
						}
					}
					else if(arguments.size() > 2) {
						boolean inQuotes= false;
						if (arguments.get(2).equalsIgnoreCase("by") || message.contains(" by ")) {
							if (arguments.get(2).equalsIgnoreCase("by")) {
								levelNameS = arguments.get(1).trim();
								if(levelNameS.startsWith("\"") && levelNameS.endsWith("\"")){
									inQuotes = true;
								}
								levelNameS = levelNameS.replace("\"", "");
								usernameS = arguments.get(3).trim().replace("\"", "");
							} else if (message.toLowerCase().contains(" by ")) {
								String messageS = message.split(" ", 2)[1];
								String[] argumentsS = messageS.split(" by ", 2);

								levelNameS = argumentsS[0];
								if(levelNameS.startsWith("\"") && levelNameS.endsWith("\"")){
									inQuotes = true;
								}
								levelNameS = levelNameS.replace("\"", "");
								usernameS = argumentsS[1].trim().replace("\"", "");
							} else {
								processingLevel = false;
								return; //todo command formatted wrong
							}
							GDUser gdUser;
							try {
								if (LoadGD.isAuth) {
									gdUser = LoadGD.authClient.searchUser(usernameS).block();
								} else {
									gdUser = LoadGD.anonClient.searchUser(usernameS).block();
								}
							} catch (MissingAccessException e) {
								sendUnallowed(Utilities.format("$LEVEL_USER_DOESNT_EXIST_MESSAGE$", user));
								return;
							} catch (GDClientException e) {
								sendError(Utilities.format("$SEARCH_FAILED$", user));
								return;
							}
							if (gdUser == null) {
								sendUnallowed(Utilities.format("$LEVEL_USER_DOESNT_EXIST_MESSAGE$", user));
								return;
							}
							try {
								outerLoop:
								for (int j = 0; j < 10; j++) {
									Object[] pages;
									if (LoadGD.isAuth) {
										pages = Objects.requireNonNull(LoadGD.authClient.getLevelsByUser(gdUser, j).block()).asList().toArray();
									} else {
										pages = Objects.requireNonNull(LoadGD.anonClient.getLevelsByUser(gdUser, j).block()).asList().toArray();
									}
									for (int i = 0; i < 10; i++) {
										if(!inQuotes){
											if (((GDLevel) pages[i]).getName().toLowerCase()
													.startsWith(levelNameS)) {
												level = (GDLevel) pages[i];
												break outerLoop;
											}
										}else {
											if (((GDLevel) pages[i]).getName().toLowerCase()
													.equalsIgnoreCase(levelNameS)) {
												level = (GDLevel) pages[i];
												break outerLoop;
											}
										}
									}
								}
								if (level == null) {
									sendUnallowed(Utilities.format("$LEVEL_DOESNT_EXIST_MESSAGE$", user));
									return;
								}
							} catch (IndexOutOfBoundsException e) {
								sendUnallowed(Utilities.format("$LEVEL_DOESNT_EXIST_MESSAGE$", user));
								return;
							} catch (MissingAccessException e) {
								sendUnallowed(Utilities.format("$LEVEL_USER_DOESNT_EXIST_MESSAGE$", user));
								return;
							} catch (GDClientException e) {
								sendError(Utilities.format("$SEARCH_FAILED$", user));
								return;
							}
							ID = level.getId();

						}
						else {
							String messageS = message.split(" ", 2)[1].replace("\"", "");
							try {
								if (LoadGD.isAuth) {
									level = Objects.requireNonNull(LoadGD.authClient.searchLevels(messageS, LevelSearchFilters.create(), 0)
											.block()).asList().get(0);
								} else {
									level = Objects.requireNonNull(LoadGD.anonClient.searchLevels(messageS, LevelSearchFilters.create(), 0)
											.block()).asList().get(0);
								}
							} catch (MissingAccessException e) {
								sendUnallowed(Utilities.format("$LEVEL_DOESNT_EXIST_MESSAGE$", user));
								return;
							} catch (GDClientException e) {
								sendError(Utilities.format("$SEARCH_FAILED$", user));
								return;
							}
							ID = level.getId();
						}
					}
					else {
						String messageS = message.split(" ", 2)[1].replace("\"", "");

						try {
							if (LoadGD.isAuth) {
								level = Objects.requireNonNull(LoadGD.authClient.searchLevels(messageS, LevelSearchFilters.create(), 0)
										.block()).asList().get(0);
							} else {
								level = Objects.requireNonNull(LoadGD.anonClient.searchLevels(messageS, LevelSearchFilters.create(), 0)
										.block()).asList().get(0);
							}
						} catch (MissingAccessException e) {
							sendUnallowed(Utilities.format("$LEVEL_DOESNT_EXIST_MESSAGE$", user));
							return;
						} catch (GDClientException e) {
							sendError(Utilities.format("$SEARCH_FAILED$", user));
							return;
						}
						ID = level.getId();
					}
				}
				else{
					if (ID > 999999999 || ID < 1) {
						processingLevel = false;
						return;
					}
					try {
						if (LoadGD.isAuth) {
							level = Objects.requireNonNull(LoadGD.authClient.getLevelById(ID).block());
						} else {
							level = Objects.requireNonNull(LoadGD.anonClient.getLevelById(ID).block());
						}
					} catch (MissingAccessException | NumberFormatException e) {
						sendUnallowed(Utilities.format("$LEVEL_ID_DOESNT_EXIST_MESSAGE$", user));
						return;
					} catch (GDClientException e) {
						sendError(Utilities.format("$SEARCH_FAILED$", user));
						return;
					}
				}

				/*while (processingLevel) {
					Thread.sleep(100);
				}*/
				processingLevel = true;

				for (int k = 0; k < levels.size(); k++) {

					if (ID == levels.get(k).getLevelID()) {
						int j = k + 1;
						if (!GeneralSettings.disableShowPositionOption) {
							sendUnallowed(Utilities.format("$ALREADY_IN_QUEUE_MESSAGE$", user, j));
						} else {
							sendUnallowed(Utilities.format("$ALREADY_IN_QUEUE_MESSAGE_ALT$", user));
						}
						return;
					}
				}

				boolean bypass = (GeneralSettings.modsBypassOption && isMod) || (user.equalsIgnoreCase(TwitchAccount.login) && GeneralSettings.streamerBypassOption);

				if (Main.programLoaded && !bypass) {
					if (checkList(ID, "\\GDBoard\\blocked.txt")) {
						sendUnallowed(Utilities.format("$BLOCKED_LEVEL_MESSAGE$", user));
						return;
					}
					if (checkList(user, "\\GDBoard\\blockedUsers.txt")) {
						processingLevel = false;
						return;
					}
					if (Files.exists(logged) && (GeneralSettings.repeatedOptionAll && !GeneralSettings.updatedRepeatedOption) && Main.programLoaded) {
						Scanner sc = new Scanner(logged.toFile());
						while (sc.hasNextLine()) {
							if (String.valueOf(ID).equals(sc.nextLine().split(",")[0])) {
								sc.close();
								sendUnallowed(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", user));
								return;
							}
						}
						sc.close();

					}
					if (GeneralSettings.subsOption) {
						if (!(isSub || isMod)) {
							sendUnallowed(Utilities.format("$REQUESTS_SUBSCRIBE_MESSAGE$", user));
							return;
						}
					}
					if (GeneralSettings.followersOption) {
						if (APIs.isNotFollowing(user)) {
							sendUnallowed(Utilities.format("$FOLLOW_MESSAGE$", user));
							return;
						}
					}
					if (ID < RequestSettings.minID && RequestSettings.minIDOption) {
						sendUnallowed(Utilities.format("$MIN_ID_MESSAGE$", user, RequestSettings.minID));
						return;
					}
					if (ID > RequestSettings.maxID && RequestSettings.maxIDOption) {
						sendUnallowed(Utilities.format("$MAX_ID_MESSAGE$", user, RequestSettings.maxID));
						return;
					}
					if (GeneralSettings.queueLimitBoolean && (levels.size() >= GeneralSettings.queueLimit)) {
						if (!GeneralSettings.queueFullOption) {
							sendUnallowed(Utilities.format("$QUEUE_FULL_MESSAGE$", user));
						}
						return;
					}
					if (globallyBlockedIDs.containsKey(ID)) {
						sendUnallowed(Utilities.format("$GLOBALLY_BLOCKED_LEVEL_MESSAGE$", user, globallyBlockedIDs.get(ID)));
						return;
					}
					if (GeneralSettings.userLimitOption) {
						int size = 0;
						for (LevelData levelA : levels) {
							if (levelA.getRequester().equalsIgnoreCase(user)) {
								size++;
							}
						}
						if (size >= GeneralSettings.userLimit) {
							sendUnallowed(Utilities.format("$MAXIMUM_LEVELS_MESSAGE$", user));
							return;
						}
					}
					if (GeneralSettings.userLimitStreamOption) {
						if (userStreamLimitMap.containsKey(user)) {
							if (userStreamLimitMap.get(user) >= GeneralSettings.userLimitStream) {
								sendUnallowed(Utilities.format("$MAXIMUM_LEVELS_STREAM_MESSAGE$", user));
								return;
							}
						}
					}
					if (addedLevels.containsKey(ID) && (GeneralSettings.repeatedOption && !GeneralSettings.updatedRepeatedOption)) {
						sendUnallowed(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", user));
						return;
					}
				}
				if (userStreamLimitMap.containsKey(user)) {
					userStreamLimitMap.put(user, userStreamLimitMap.get(user) + 1);
				} else {
					userStreamLimitMap.put(user, 1);
				}

				LevelData levelData = new LevelData();
				Thread parse ;
				if (Main.programLoaded && !bypass) {
					if (checkList(level.getCreatorName(), "\\GDBoard\\blockedGDUsers.txt")) {
						sendUnallowed(Utilities.format("$BLOCKED_CREATOR_MESSAGE$", user));
						return;
					}
					if (RequestSettings.allowOption) {
						if (Files.exists(allowed)) {
							Scanner sc = new Scanner(allowed.toFile());
							boolean hasWord = false;
							while (sc.hasNextLine()) {
								if (level.getName().toLowerCase().contains(sc.nextLine().toLowerCase())) {
									hasWord = true;
									sc.close();
									break;
								}
							}
							if (!hasWord) {
								sendUnallowed(Utilities.format("$BLOCKED_NAME_MESSAGE$", user));
								return;
							}
							sc.close();
						}
					}
					if (RequestSettings.disallowOption) {
						if (Files.exists(disallowed)) {
							Scanner sc = new Scanner(disallowed.toFile());
							while (sc.hasNextLine()) {
								if (level.getName().toLowerCase().contains(sc.nextLine().toLowerCase())) {
									sc.close();
									sendUnallowed(Utilities.format("$BLOCKED_NAME_MESSAGE$", user));
									return;
								}
							}
							sc.close();
						}
					}
					if (RequestSettings.ratedOption && !(level.getStars() > 0)) {
						sendUnallowed(Utilities.format("$STAR_RATED_MESSAGE$", user));
						return;
					}
					if (RequestSettings.unratedOption && level.getStars() > 0) {
						sendUnallowed(Utilities.format("$UNRATED_MESSAGE$", user));
						return;
					}
					if (RequestSettings.minObjectsOption && level.getObjectCount() < RequestSettings.minObjects) {
						sendUnallowed(Utilities.format("$FEW_OBJECTS_MESSAGE$", user));
						return;
					}
					if (RequestSettings.maxObjectsOption && level.getObjectCount() > RequestSettings.maxObjects) {
						sendUnallowed(Utilities.format("$MANY_OBJECTS_MESSAGE$", user));
						return;
					}
					if (level.getObjectCount() != 0) {
						if (RequestSettings.minLikesOption && level.getObjectCount() < RequestSettings.minLikes) {
							sendUnallowed(Utilities.format("$FEW_LIKES_MESSAGE$", user));
							return;
						}
						if (RequestSettings.maxLikesOption && level.getObjectCount() > RequestSettings.maxLikes) {
							sendUnallowed(Utilities.format("$MANY_LIKES_MESSAGE$", user));
							return;
						}
					}
				}
				levelData.setRequester(user);
				levelData.setAuthor(Objects.requireNonNull(level).getCreatorName());
				levelData.setName(level.getName());
				levelData.setMessage(message);
				if (messageID != null) {
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
					if (Files.exists(logged) && GeneralSettings.updatedRepeatedOption) {
						Scanner sc = new Scanner(logged.toFile());
						while (sc.hasNextLine()) {
							String levelLine = sc.nextLine();

							if (String.valueOf(ID).equals(levelLine.split(",")[0])) {
								int version;
								if (levelLine.split(",").length == 1) {
									version = 1;
								} else {
									version = Integer.parseInt(levelLine.split(",")[1]);
								}
								if (version >= levelData.getLevelVersion()) {
									sc.close();
									sendUnallowed(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", user));
									return;
								}
							}
						}
						sc.close();
					}
					if (addedLevels.containsKey(ID) && (GeneralSettings.updatedRepeatedOption)) {
						if (addedLevels.get(ID) >= levelData.getLevelVersion()) {
							sendUnallowed(Utilities.format("$REQUESTED_BEFORE_MESSAGE$", user));
							return;
						}
					}
					if (RequestSettings.excludedDifficulties.contains(levelData.getDifficulty().toLowerCase()) && RequestSettings.disableOption) {
						sendUnallowed(Utilities.format("$DIFFICULTY_MESSAGE$", user));
						return;
					}
					if (RequestSettings.excludedLengths.contains(levelData.getLength().toLowerCase()) && RequestSettings.disableLengthOption) {
						sendUnallowed(Utilities.format("$LENGTH_MESSAGE$", user));
						return;
					}
				}

				GDUser gdUser;
				GDUserIconSet iconSet;
				try {
					if (LoadGD.isAuth) {
						gdUser = LoadGD.authClient.searchUser(levelData.getAuthor()).block();
					} else {
						gdUser = LoadGD.anonClient.searchUser(levelData.getAuthor()).block();
					}
					assert gdUser != null;
					iconSet = new GDUserIconSet(gdUser, spriteFactory);
				} catch (MissingAccessException e) {
					if (LoadGD.isAuth) {
						gdUser = LoadGD.authClient.searchUser("RobTop").block();
					} else {
						gdUser = LoadGD.anonClient.searchUser("RobTop").block();
					}
					assert gdUser != null;
					iconSet = new GDUserIconSet(gdUser, spriteFactory);
				}
				try {
					BufferedImage icon = iconSet.generateIcon(gdUser.getMainIconType());
					Image imgScaled = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					ImageIcon imgNew = new ImageIcon(imgScaled);
					levelData.setPlayerIcon(imgNew);
				}
				catch (IllegalArgumentException e){
					levelData.setPlayerIcon(null);
				}

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

				GDLevel finalLevel2 = level;
				if (LoadGD.isAuth) {
					GDLevel finalLevel = level;
					long finalID = ID;
					parse = new Thread(() -> {
						Object object;
						try {
							object = finalLevel2.download().block();
							if (!(finalLevel.getStars() > 0) && finalLevel.getGameVersion() / 10 >= 2) {
								parse(((GDLevelData) Objects.requireNonNull(object)).getData(), finalID);
							}
							levelData.setPassword(((GDLevelData) Objects.requireNonNull(object)).getPass());
							levelData.setUpload(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getUploadTimestamp()));
							levelData.setUpdate(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getLastUpdatedTimestamp()));
							InfoPanel.refreshInfo();
							Functions.saveFunction();
							LevelsPanel.refreshSelectedLevel(finalID);
						} catch (Exception ignored) {
						}
					});
				} else {
					GDLevel finalLevel1 = level;
					long finalID1 = ID;
					parse = new Thread(() -> {
						Object object = finalLevel2.download().block();

						if (!(finalLevel1.getStars() > 0) && finalLevel1.getGameVersion() / 10 >= 2) {
							parse(((GDLevelData) Objects.requireNonNull(object)).getData(), finalID1);
						}
						levelData.setPassword(((GDLevelData) Objects.requireNonNull(object)).getPass());
						levelData.setUpload(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getUploadTimestamp()));
						levelData.setUpdate(String.valueOf(((GDLevelData) Objects.requireNonNull(object)).getLastUpdatedTimestamp()));
						InfoPanel.refreshInfo();
						LevelsPanel.refreshSelectedLevel(finalID1);
						Functions.saveFunction();
					});
				}
				parse.start();

				levels.add(levelData);
				LevelsPanel.createButton(levelData.getName(), levelData.getAuthor(), levelData.getLevelID(),
						levelData.getDifficulty(), levelData.getEpic(), levelData.getFeatured(), levelData.getStars(),
						levelData.getRequester(), levelData.getVersion(), levelData.getPlayerIcon(), levelData.getCoins(),
						levelData.getVerifiedCoins());

				LevelsPanel.setName(Requests.levels.size());
				Functions.saveFunction();
				if (Main.sendMessages) {
					if (!GeneralSettings.confirmOption) {
						if (levels.size() != 1) {
							if (!GeneralSettings.disableShowPositionOption) {
								sendSuccess(Utilities.format("$CONFIRMATION_MESSAGE$",
										levelData.getRequester(),
										levelData.getName(),
										levelData.getLevelID(),
										levels.size()), GeneralSettings.confirmWhisperOption, user);
							} else {
								sendSuccess(Utilities.format("$CONFIRMATION_MESSAGE_ALT$",
										levelData.getRequester(),
										levelData.getName(),
										levelData.getLevelID()), GeneralSettings.confirmWhisperOption, user);
							}
						}
					}
				}
				if (levels.size() == 1) {
					StringSelection selection = new StringSelection(String.valueOf(Requests.levels.get(0).getLevelID()));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
					if (Main.sendMessages) {
						if (!GeneralSettings.nowPlayingOption) {
							sendSuccess(Utilities.format("$NOW_PLAYING_TOP_MESSAGE$",
									Requests.levels.get(0).getRequester(),
									Requests.levels.get(0).getName(),
									Requests.levels.get(0).getLevelID()));
						}
					}
				}
				addedLevels.put(ID, levelData.getLevelVersion());
				OutputSettings.setOutputStringFile(RequestsOld.parseInfoString(OutputSettings.outputString, 0));
				Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");

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
					Files.write(file, (ID + "," + levelData.getLevelVersion() + "\n").getBytes(), StandardOpenOption.APPEND);

				} else {
					BufferedReader fileA = new BufferedReader(new FileReader(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt"));
					StringBuilder inputBuffer = new StringBuilder();
					String line;
					while ((line = fileA.readLine()) != null) {
						inputBuffer.append(line);
						inputBuffer.append('\n');
					}
					fileA.close();

					FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
					fileOut.write(inputBuffer.toString().replace(value, ID + "," + levelData.getLevelVersion()).getBytes());
					fileOut.close();
				}

				processingLevel = false;
			} catch (Exception e) {
				e.printStackTrace();
				sendError(Utilities.format("$REQUEST_ERROR$", user));
				processingLevel = false;
			}
		}).start();

	}

	private static void sendError(String message) {
		Main.sendMessage("🔴 | " + message);
		processingLevel = false;
	}

	private static void sendUnallowed(String message) {
		Main.sendMessage("🟡 | " + message);
		processingLevel = false;
	}

	private static void sendSuccess(String message, boolean whisper, String user) {
		Main.sendMessage("🟢 | " + message, whisper, user);
		processingLevel = false;
	}

	private static void sendSuccess(String message) {
		Main.sendMessage("🟢 | " + message);
		processingLevel = false;
	}

	private static boolean checkList(Object object, String path) {
		String value = String.valueOf(object);
		if (object instanceof String) {
			value = (String) object;
		}
		Path pathA = Paths.get(Defaults.saveDirectory + path);
		if (Files.exists(pathA)) {
			Scanner sc;
			try {
				sc = new Scanner(pathA.toFile());
				while (sc.hasNextLine()) {
					if (value.equals(sc.nextLine())) {
						sc.close();
						return true;
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static void parse(byte[] level, long levelID) {
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
				if (k == 0) {
					if (Requests.levels.get(0).getContainsImage()) {
						Utilities.notify("Image Hack", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() + ") possibly contains the image hack!");
					} else if (Requests.levels.get(0).getContainsVulgar()) {
						Utilities.notify("Vulgar Language", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() + ") contains vulgar language!");
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
	public static void request(String user, boolean isMod, boolean isSub, String message, String messageID) {
		addRequest(0, user, isMod, isSub, message, messageID, true);
	}
	public static int getPosFromID(long ID) {
		for (int i = 0; i < LevelsPanel.getSize(); i++) {
			if (LevelsPanel.getButton(i).getID() == ID) {
				return i;
			}
		}
		return -1;
	}
}
