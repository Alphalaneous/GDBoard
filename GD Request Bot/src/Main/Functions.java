package Main;

import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
import Main.InnerWindows.SongWindow;
import Main.SettingsPanels.BlockedSettings;
import Main.SettingsPanels.GeneralSettings;
import Main.SettingsPanels.OutputSettings;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;

public class Functions {

	public static void skipFunction() {
		if(Requests.bwomp){
			Thread bwompThread;

			System.out.println("bwomped");
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
		if(Main.programLoaded) {
			if (Requests.levels.size() != 0) {
				int select = LevelsWindow.getSelectedID();
				Requests.levels.remove(LevelsWindow.getSelectedID());
				LevelsWindow.removeButton();
				if (Requests.levels.size() > 0 ) {
					StringSelection selection = new StringSelection(
							String.valueOf(Requests.levels.get(0).getLevelID()));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
				if (select == 0 && Requests.levels.size() > 0) {
					if (!GeneralSettings.nowPlayingOption) {
						if(Requests.levels.get(0).getContainsImage()){
							Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
									+ Requests.levels.get(0).getLevelID() + "). Requested by "
									+ Requests.levels.get(0).getRequester() + " (Image Hack)");
						}
						else if(Requests.levels.get(0).getContainsVulgar()){
							Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
									+ Requests.levels.get(0).getLevelID() + "). Requested by "
									+ Requests.levels.get(0).getRequester() + " (Vulgar Language)");
						}
						else{
							Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
									+ Requests.levels.get(0).getLevelID() + "). Requested by "
									+ Requests.levels.get(0).getRequester());
						}
					}
				}
				Functions.saveFunction();
			}
			OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
			LevelsWindow.setOneSelect();

			new Thread(() -> {
				CommentsWindow.unloadComments(true);
				if (Requests.levels.size() != 0) {
					CommentsWindow.loadComments(0, false);
				}
			}).start();

			SongWindow.refreshInfo();
			InfoWindow.refreshInfo();
			LevelsWindow.setName(Requests.levels.size());

		}
	}

	public static void randomFunction(){
		if(Main.programLoaded) {
			Random random = new Random();
			int num = 0;
			try {
				num = random.nextInt(Requests.levels.size() - 2) + 1;
			} catch (Exception ignored) {

			}

			if (Requests.levels.size() != 0) {


				Requests.levels.remove(LevelsWindow.getSelectedID());
				LevelsWindow.removeButton();
				Functions.saveFunction();

				if (Requests.levels.size() == 1) {
					LevelsWindow.setOneSelect();
				} else {
					LevelsWindow.setSelect(num);
				}
				new Thread(() -> {
					CommentsWindow.unloadComments(true);
					if (Requests.levels.size() != 0) {
						CommentsWindow.loadComments(0, false);
					}
				}).start();
				if (Requests.levels.size() != 0) {
					System.out.println(num);
					StringSelection selection = new StringSelection(
							String.valueOf(Requests.levels.get(num).getLevelID()));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
					if (!GeneralSettings.nowPlayingOption) {
						Main.sendMessage("Now Playing " + Requests.levels.get(num).getName() + " ("
								+ Requests.levels.get(num).getLevelID() + "). Requested by "
								+ Requests.levels.get(num).getRequester());
					}
				}
			}
			OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, num));
			SongWindow.refreshInfo();
			InfoWindow.refreshInfo();
			Functions.saveFunction();
			LevelsWindow.setName(Requests.levels.size());
		}
	}

	public static void copyFunction() {
		if (Requests.levels.size() != 0) {
			StringSelection selection = new StringSelection(
					String.valueOf(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID()));
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}
	}

	public static void saveFunction() {
		try {
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\saved.txt");
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			FileWriter fooWriter = new FileWriter(file.toFile(), false);
			StringBuilder message = new StringBuilder();
			for (int i = 0; i < Requests.levels.size(); i++) {
				message.append(Requests.levels.get(i).getLevelID()).append(",").append(Requests.levels.get(i).getRequester()).append("\n");
			}
			fooWriter.write(message.toString());
			fooWriter.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	public static void blockFunction() {
		if(Main.programLoaded) {
			if(LevelsWindow.getSelectedID() == 0 && Requests.levels.size() > 1){
				StringSelection selection = new StringSelection(
						String.valueOf(Requests.levels.get(1).getLevelID()));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
			if (Requests.levels.size() != 0) {

				new Thread(()->{

				String option = DialogBox.showDialogBox("Block " + Requests.levels.get(LevelsWindow.getSelectedID()).getName() + " (" + Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID() + ")?", "This will block the selected level from being added.", "This can be undone in settings.", new String[]{"Yes", "No"});

				if (option.equalsIgnoreCase("yes")) {
					BlockedSettings.addButton(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID());
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");

					try {
						if (!Files.exists(file)) {
							Files.createFile(file);
						}
						Files.write(
								file,
								(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID() + "\n").getBytes(),
								StandardOpenOption.APPEND);
					} catch (IOException e1) {
						DialogBox.showDialogBox("Error!", e1.toString(), "There was an error writing to the file!", new String[]{"OK"});

					}
					Requests.levels.remove(LevelsWindow.getSelectedID());
					LevelsWindow.removeButton();
					Functions.saveFunction();
					LevelsWindow.setOneSelect();
					new Thread(() -> {
						CommentsWindow.unloadComments(true);
						if (Requests.levels.size() > 0) {
							CommentsWindow.loadComments(0, false);
						}
					}).start();
					LevelsWindow.setName(Requests.levels.size());

				}
				SongWindow.refreshInfo();
				InfoWindow.refreshInfo();
				SettingsWindow.run = true;
				}).start();
			}
		}
	}

	public static void clearFunction() {
		if(Main.programLoaded) {
			new Thread(()->{

			String option = DialogBox.showDialogBox("Clear the Queue?", "This will clear the levels from the queue.", "Do you want to clear the queue?", new String[]{"Clear All", "Cancel"});
			if (option.equalsIgnoreCase("Clear All")) {
				if (Requests.levels.size() != 0) {
					for (int i = 0; i < Requests.levels.size(); i++) {
						LevelsWindow.removeButton();
					}
					Requests.levels.clear();
					Functions.saveFunction();
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
					CommentsWindow.unloadComments(true);
				}
				LevelsWindow.setOneSelect();
				SettingsWindow.run = true;
			}
			else if (option.equalsIgnoreCase("Inactives")){
				if (Requests.levels.size() != 0) {


					for (int i = Requests.levels.size()-1; i >= 0; i--) {
						if(!Requests.levels.get(i).getViewership()){
							Requests.levels.remove(i);
							LevelsWindow.removeButton(i);
						}
					}

					Functions.saveFunction();
					SongWindow.refreshInfo();
					InfoWindow.refreshInfo();
					CommentsWindow.unloadComments(true);
					CommentsWindow.loadComments(0,false);
				}
				LevelsWindow.setOneSelect();
				SettingsWindow.run = true;
			}
			LevelsWindow.setName(Requests.levels.size());
			}).start();

		}

	}

	static void requestsToggleFunction() {
		if (Main.programLoaded) {
			if (MainBar.requests) {
				MainBar.stopReqs.setText("\uE768");
				MainBar.requests = false;
				Main.sendMessage("/me Requests are now off!");

			} else {
				MainBar.stopReqs.setText("\uE71A");
				MainBar.requests = true;
				Main.sendMessage("/me Requests are now on!");


			}
		}
	}
}
