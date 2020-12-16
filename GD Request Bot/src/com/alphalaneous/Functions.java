package com.alphalaneous;

import com.alphalaneous.Panels.CommentsPanel;
import com.alphalaneous.Panels.InfoPanel;
import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.Panels.SongPanel;
import com.alphalaneous.SettingsPanels.BlockedSettings;
import com.alphalaneous.SettingsPanels.GeneralSettings;
import com.alphalaneous.SettingsPanels.OutputSettings;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.SettingsWindow;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Functions {

	private static LinkedHashMap<LevelData, Integer> undoQueue = new LinkedHashMap<>(5) {
		protected boolean removeEldestEntry(Map.Entry<LevelData, Integer> eldest) {
			return size() > 5;
		}
	};
	private static boolean didUndo = false;

	private static boolean onCool = false;

	public static void skipFunction() {
		if (RequestsOld.bwomp) {
			Thread bwompThread;
			bwompThread = new Thread(() -> {
				try {
					BufferedInputStream inp = new BufferedInputStream(BotHandler.class
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
		if (Main.programLoaded) {
			if (Requests.levels.size() != 0) {
				int select = LevelsPanel.getSelectedID();
				if (didUndo) {
					undoQueue.clear();
					didUndo = false;
				}
				undoQueue.put(Requests.levels.get(LevelsPanel.getSelectedID()), LevelsPanel.getSelectedID());

				Requests.levels.remove(LevelsPanel.getSelectedID());
				LevelsPanel.removeButton();
				if (Requests.levels.size() > 0) {
					StringSelection selection = new StringSelection(
							String.valueOf(Requests.levels.get(0).getLevelID()));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
				if (select == 0 && Requests.levels.size() > 0) {
					if (!GeneralSettings.nowPlayingOption) {

						if(!onCool) {
							new Thread(() ->{
								onCool = true;
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								onCool = false;
							}).start();
							new Thread(() -> {
								if (Requests.levels.get(0).getContainsImage()) {
									Main.sendMessage(Utilities.format("$NOW_PLAYING_MESSAGE$",
											Requests.levels.get(0).getName(),
											Requests.levels.get(0).getLevelID(),
											Requests.levels.get(0).getRequester()) + " " + Utilities.format("$IMAGE_HACK$"));
								} else if (Requests.levels.get(0).getContainsVulgar()) {
									Main.sendMessage(Utilities.format("$NOW_PLAYING_MESSAGE$",
											Requests.levels.get(0).getName(),
											Requests.levels.get(0).getLevelID(),
											Requests.levels.get(0).getRequester()) + " " + Utilities.format("$VULGAR_LANGUAGE$"));
								} else {
									Main.sendMessage(Utilities.format("$NOW_PLAYING_MESSAGE$",
											Requests.levels.get(0).getName(),
											Requests.levels.get(0).getLevelID(),
											Requests.levels.get(0).getRequester()));
								}
							}).start();
						}
					}
					if (Requests.levels.get(0).getContainsImage()) {
						Utilities.notify("Image Hack", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() + ") possibly contains the image hack!");
					} else if (Requests.levels.get(0).getContainsVulgar()) {
						Utilities.notify("Vulgar Language", Requests.levels.get(0).getName() + " (" + Requests.levels.get(0).getLevelID() + ") contains vulgar language!");
					}
				}

				Functions.saveFunction();
			}
			OutputSettings.setOutputStringFile(RequestsOld.parseInfoString(OutputSettings.outputString, 0));
			LevelsPanel.setOneSelect();

			new Thread(() -> {
				CommentsPanel.unloadComments(true);
				if (Requests.levels.size() != 0) {
					CommentsPanel.loadComments(0, false);
				}
			}).start();

			SongPanel.refreshInfo();
			InfoPanel.refreshInfo();
			LevelsPanel.setName(Requests.levels.size());

		}


	}

	public static void undoFunction() {
		if (undoQueue.size() != 0) {
			didUndo = true;
			LevelData data = (LevelData) undoQueue.keySet().toArray()[0];
			int position = (int) undoQueue.values().toArray()[0];
			RequestsOld.forceAdd(data);
			LevelsPanel.createButton(data.getName(), data.getAuthor(), data.getLevelID(), data.getDifficulty(), data.getEpic(), data.getFeatured(), data.getStars(), data.getRequester(), data.getVersion(), data.getPlayerIcon(), data.getCoins(), data.getVerifiedCoins());
			RequestsOld.movePosition(Requests.levels.size() - 1, position);
			undoQueue.remove(data);
		}
	}

	public static void randomFunction() {
		if (Main.programLoaded) {
			Random random = new Random();
			int num = 0;
			if (Requests.levels.size() != 0) {
				if (didUndo) {
					undoQueue.clear();
					didUndo = false;
				}
				undoQueue.put(Requests.levels.get(LevelsPanel.getSelectedID()), LevelsPanel.getSelectedID());
				Requests.levels.remove(LevelsPanel.getSelectedID());
				LevelsPanel.removeButton(LevelsPanel.getSelectedID());
				Functions.saveFunction();

				CommentsPanel.unloadComments(true);

				if (Requests.levels.size() != 0) {
					while (true) {
						try {
							num = random.nextInt(Requests.levels.size());
							break;
						} catch (Exception ignored) {
						}
					}

					LevelsPanel.setSelect(num);

					new Thread(() -> CommentsPanel.loadComments(0, false)).start();
					StringSelection selection = new StringSelection(
							String.valueOf(Requests.levels.get(num).getLevelID()));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
					if (!GeneralSettings.nowPlayingOption) {
						int finalNum = num;
						new Thread(() -> Main.sendMessage(Utilities.format("$NOW_PLAYING_MESSAGE$",
								Requests.levels.get(finalNum).getName(),
								Requests.levels.get(finalNum).getLevelID(),
								Requests.levels.get(finalNum).getRequester()))).start();

					}
					if (Requests.levels.get(num).getContainsImage()) {
						Utilities.notify("Image Hack", Requests.levels.get(num).getName() + " (" + Requests.levels.get(num).getLevelID() + ") possibly contains the image hack!");
					} else if (Requests.levels.get(num).getContainsVulgar()) {
						Utilities.notify("Vulgar Language", Requests.levels.get(num).getName() + " (" + Requests.levels.get(num).getLevelID() + ") contains vulgar language!");
					}
				}
			}
			OutputSettings.setOutputStringFile(RequestsOld.parseInfoString(OutputSettings.outputString, num));
			SongPanel.refreshInfo();
			InfoPanel.refreshInfo();
			Functions.saveFunction();
			LevelsPanel.setName(Requests.levels.size());
		}
	}

	public static void copyFunction() {
		if (Requests.levels.size() != 0) {
			StringSelection selection = new StringSelection(
					String.valueOf(Requests.levels.get(LevelsPanel.getSelectedID()).getLevelID()));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}
	}

	public static void saveFunction() {
		//public static void forceAdd(String name, String author, long levelID, String difficulty, boolean epic, boolean featured, int stars, String requester,
		// int gameVersion, int coins, String description, int likes, int downloads, String length, int levelVersion, int songID, String songName, String songAuthor, int objects, long original){
		try {
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\saved.txt");
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			FileWriter fooWriter = new FileWriter(file.toFile(), false);
			StringBuilder message = new StringBuilder();
			for (int i = 0; i < Requests.levels.size(); i++) {
				message.append(Requests.levels.get(i).getName())
						.append(",").append(Requests.levels.get(i).getAuthor())
						.append(",").append(Requests.levels.get(i).getLevelID())
						.append(",").append(Requests.levels.get(i).getDifficulty())
						.append(",").append(Requests.levels.get(i).getEpic())
						.append(",").append(Requests.levels.get(i).getFeatured())
						.append(",").append(Requests.levels.get(i).getStars())
						.append(",").append(Requests.levels.get(i).getRequester())
						.append(",").append(Requests.levels.get(i).getVersion())
						.append(",").append(Requests.levels.get(i).getCoins())
						.append(",").append(new String(Base64.getEncoder().encode(Requests.levels.get(i).getDescription().toString().getBytes())))
						.append(",").append(Requests.levels.get(i).getLikes())
						.append(",").append(Requests.levels.get(i).getDownloads())
						.append(",").append(Requests.levels.get(i).getLength())
						.append(",").append(Requests.levels.get(i).getLevelVersion())
						.append(",").append(Requests.levels.get(i).getSongID())
						.append(",").append(new String(Base64.getEncoder().encode(Requests.levels.get(i).getSongName().toString().getBytes())))
						.append(",").append(Requests.levels.get(i).getSongAuthor())
						.append(",").append(Requests.levels.get(i).getObjects())
						.append(",").append(Requests.levels.get(i).getOriginal())
						.append(",").append(Requests.levels.get(i).getContainsVulgar())
						.append(",").append(Requests.levels.get(i).getContainsImage())
						.append(",").append(Requests.levels.get(i).getPassword())
						.append(",").append(Requests.levels.get(i).getUpload())
						.append(",").append(Requests.levels.get(i).getUpdate())
						.append(",").append(Requests.levels.get(i).getVerifiedCoins())
						.append("\n");
			}
			fooWriter.write(message.toString());
			fooWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void blockFunction() {
		if (Main.programLoaded) {
			if (LevelsPanel.getSelectedID() == 0 && Requests.levels.size() > 1) {
				StringSelection selection = new StringSelection(
						String.valueOf(Requests.levels.get(1).getLevelID()));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
			if (Requests.levels.size() != 0) {

				new Thread(() -> {

					String option = DialogBox.showDialogBox("$BLOCK_ID_TITLE$", "$BLOCK_ID_INFO$", "$BLOCK_ID_SUBINFO$", new String[]{"$YES$", "$NO$"}, new Object[]{Requests.levels.get(LevelsPanel.getSelectedID()).getName(), Requests.levels.get(LevelsPanel.getSelectedID()).getLevelID()});

					if (option.equalsIgnoreCase("YES")) {
						BlockedSettings.addButton(Requests.levels.get(LevelsPanel.getSelectedID()).getLevelID());
						Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");

						try {
							if (!Files.exists(file)) {
								Files.createFile(file);
							}
							Files.write(
									file,
									(Requests.levels.get(LevelsPanel.getSelectedID()).getLevelID() + "\n").getBytes(),
									StandardOpenOption.APPEND);
						} catch (IOException e1) {
							DialogBox.showDialogBox("Error!", e1.toString(), "There was an error writing to the file!", new String[]{"OK"});

						}
						Requests.levels.remove(LevelsPanel.getSelectedID());
						LevelsPanel.removeButton();
						Functions.saveFunction();
						LevelsPanel.setOneSelect();
						new Thread(() -> {
							CommentsPanel.unloadComments(true);
							if (Requests.levels.size() > 0) {
								CommentsPanel.loadComments(0, false);
							}
						}).start();
						LevelsPanel.setName(Requests.levels.size());

					}
					SongPanel.refreshInfo();
					InfoPanel.refreshInfo();
					SettingsWindow.run = true;
				}).start();
			}
		}
	}

	public static void clearFunction() {
		if (Main.programLoaded) {
			new Thread(() -> {

				String option = DialogBox.showDialogBox("$CLEAR_QUEUE_TITLE$", "$CLEAR_QUEUE_INFO$", "$CLEAR_QUEUE_SUBINFO$", new String[]{"$CLEAR_ALL$", "$CANCEL$"});
				if (option.equalsIgnoreCase("CLEAR_ALL")) {
					if (Requests.levels.size() != 0) {
						for (int i = 0; i < Requests.levels.size(); i++) {
							LevelsPanel.removeButton();
						}
						Requests.levels.clear();
						undoQueue.clear();
						Functions.saveFunction();
						SongPanel.refreshInfo();
						InfoPanel.refreshInfo();
						CommentsPanel.unloadComments(true);
					}
					LevelsPanel.setOneSelect();
					SettingsWindow.run = true;
				} else if (option.equalsIgnoreCase("Inactives")) {
					if (Requests.levels.size() != 0) {


						for (int i = Requests.levels.size() - 1; i >= 0; i--) {
							if (!Requests.levels.get(i).getViewership()) {
								Requests.levels.remove(i);
								LevelsPanel.removeButton(i);
							}
						}

						Functions.saveFunction();
						SongPanel.refreshInfo();
						InfoPanel.refreshInfo();
						CommentsPanel.unloadComments(true);
						CommentsPanel.loadComments(0, false);
					}
					LevelsPanel.setOneSelect();
					SettingsWindow.run = true;
				}
				LevelsPanel.setName(Requests.levels.size());
			}).start();

		}

	}

	public static void requestsToggleFunction() {
		if (Main.programLoaded) {
			if (Requests.requestsEnabled) {
				Requests.requestsEnabled = false;
				Main.sendMessage(Utilities.format("$REQUESTS_OFF_TOGGLE_MESSAGE$"));

			} else {
				Requests.requestsEnabled = true;
				Main.sendMessage(Utilities.format("$REQUESTS_ON_TOGGLE_MESSAGE$"));
			}
		}
	}
}
