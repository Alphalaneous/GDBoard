package Main;

import Main.InnerWindows.LevelsWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Assets {

	public static HashMap<String, ImageIcon> difficultyIconsNormal = new HashMap<>();
	public static HashMap<String, ImageIcon> difficultyIconsFeature = new HashMap<>();
	public static HashMap<String, ImageIcon> difficultyIconsEpic = new HashMap<>();

	private static int width = 40;
	private static int height = 40;


	static void loadAssets() throws IOException {
		String[] difficulties = {"NA", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
				"hard demon", "insane demon", "extreme demon"};
		for(String difficulty : difficulties){
			difficultyIconsNormal.put(difficulty, new ImageIcon(ImageIO
					.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
							.getResource("Resources/DifficultyIcons/Normal/" + difficulty + ".png")))
					.getScaledInstance(width, height, Image.SCALE_SMOOTH)));

			if(!difficulty.equalsIgnoreCase("NA")) {
				difficultyIconsFeature.put(difficulty, new ImageIcon(ImageIO
						.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
								.getResource("Resources/DifficultyIcons/Featured/" + difficulty + ".png")))
						.getScaledInstance(width, height, Image.SCALE_SMOOTH)));

				difficultyIconsEpic.put(difficulty, new ImageIcon(ImageIO
						.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
								.getResource("Resources/DifficultyIcons/Epic/" + difficulty + ".png")))
						.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
			}
		}
	}
}
