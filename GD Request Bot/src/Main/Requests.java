package Main;

import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

class Requests {

	static ArrayList<LevelData> levels = new ArrayList<>();
	private static AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();

	static void addRequest(String ID, String requester) throws IOException {

		GDLevel level = client.getLevelById(Integer.parseInt(ID)).block();

		LevelData levelData = new LevelData();

		boolean goThrough = true;
		boolean valid = true;

		// --------------------

		try {
			levelData.setRequester(requester);
			assert level != null;
			levelData.setAuthor(level.getCreatorName());
			levelData.setName(level.getName());
			levelData.setDifficulty(level.getDifficulty().toString());
			levelData.setDescription(level.getDescription());
			levelData.setLikes(String.valueOf(level.getLikes()));
			levelData.setDownloads(String.valueOf(level.getDownloads()));
			//levelData.setLength(level.getLength().toString());
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
			levelData.setLevelID(ID);
			//levelData.setCoins(String.valueOf(level.getCoinCount()));
			//levelData.setVerifiedCoins(level.hasCoinsVerified());
			if (level.getFeaturedScore() > 0) {
				levelData.setFeatured();
			}
			levelData.setEpic(level.isEpic());
			levelData.setSongName(Objects.requireNonNull(level.getSong().block()).getSongTitle());
			levelData.setSongAuthor(Objects.requireNonNull(level.getSong().block()).getSongAuthorName());
			levelData.setSongID(String.valueOf(Objects.requireNonNull(level.getSong().block()).getId()));
			//levelData.setSongSize(String.valueOf(level.getSong().block().getSongSize()));
			levelData.setStars(level.getStars());
			Thread parse = new Thread(() -> {
				try {
					if (!(level.getStars() > 0)) {
						ParseLevel.parse(Objects.requireNonNull(Objects
								.requireNonNull(client.getLevelById(Long.parseLong(ID)).block()).download().block())
								.getData(), ID);
						levelData.setAnalyzed();
						System.out.println(levelData.getLevelID() + " " + levelData.getAnalyzed());
						LevelsWindow2.updateUI(levelData.getLevelID(), levelData.getContainsVulgar(), levelData.getContainsImage(), levelData.getAnalyzed());
						/*int ID1110count = 0;

						for (int i = 0; i < levelData.getLevelData().size(); i++) {

							File file = new File("blockedWords.txt");
							Scanner sc = new Scanner(file);

							while (sc.hasNextLine()) {
								if (levelData.getLevelData().get(i).getObjectText().toUpperCase()
										.contains(sc.nextLine().toUpperCase())) {
									// Main.sendMessage("@" + levelData.getRequester() + " That Level is Blocked!");
									System.out.println("Contains Vulgar");
									levelData.setContainsVulgar(true);
									break;
								}
							}
							if (levelData.getLevelData().get(i).getID() == 1110
									|| levelData.getLevelData().get(i).getID() == 211
											&& levelData.getLevelData().get(i).getScaling() <= 0.1
											&& levelData.getLevelData().get(i).getScaling() != 0.0 && levelData.getLevelData().get(i).getColor1HSVEnabled() == 1) {
								double color = levelData.getLevelData().get(i).getColor1();
								if (levelData.getLevelData().get(i).getColor1() == color) {
									ID1110count++;
								}
							}
							/*System.out.println("ID: " + levelData.getLevelData().get(i).getID() + "; Color 1:"
									+ levelData.getLevelData().get(i).getColor1() + "; X: "
									+ levelData.getLevelData().get(i).getX() + "; Y: "
									+ levelData.getLevelData().get(i).getY() + "; Scale: "
									+ levelData.getLevelData().get(i).getScaling());
							sc.close();

						}
						if (ID1110count >= 1000) {
							System.out.println("Contains Image Hack");

							levelData.setContainsImage(true);
						}
						*/
						// clear up memory

						//LevelsWindow.refreshRequests();
					}
				} catch (IllegalArgumentException | IOException e) {
					e.printStackTrace();
					System.out.println("Couldn't Analyze Level");
				}

			});
			parse.start();
			

		} catch (Exception e) {
			e.printStackTrace();
			// --------------------
			// If it fails, level doesn't exist, don't go through

			goThrough = false;
			System.out.println("Error adding level");
		}

		if (goThrough) {

			// --------------------
			// Loop through levels to check if level is already in queue

			for (int k = 0; k < levels.size(); k++) {

				if (levelData.getLevelID().equals(levels.get(k).getLevelID())) {

					int j = k + 1;
					Main.sendMessage(
							"@" + levelData.getRequester() + " Level is already in the queue at position " + j + "!");
					System.out.println("Level Already Exists");
					valid = false;
					break;
				}

			}
			File file = new File("blocked.txt");
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				if (levelData.getLevelID().equals(sc.nextLine())) {
					//Main.sendMessage("@" + levelData.getRequester() + " That Level is Blocked!");
					System.out.println("Blocked ID");
					valid = false;

					break;
				}
			}
			sc.close();
			if (valid) {

				// --------------------
				// Adds level to queue array "levels" and refreshes LevelsWindow

				levels.add(levelData);
				Main.sendMessage("@" + levelData.getRequester() + " " + levelData.getName() + " ("
						+ levelData.getLevelID() + ") has been added to the queue at position " + levels.size() + "!");
				if (levels.size() == 1) {
					StringSelection selection = new StringSelection(Requests.levels.get(0).getLevelID());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
					Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
							+ Requests.levels.get(0).getLevelID() + "). Requested by "
							+ Requests.levels.get(0).getRequester());
				}
				LevelsWindow2.createButton(levelData.getName(), levelData.getAuthor(), levelData.getLevelID(), levelData.getDifficulty(), levelData.getEpic(), levelData.getFeatured());
			}
		}
	}
	static ArrayList<LevelData> getLevelData(){
		return levels;
	}
	
}
