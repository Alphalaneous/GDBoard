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
	public static ImageIcon verifiedCoin;
	public static ImageIcon GDBoard;
	public static ImageIcon unverifiedCoin;
	public static ImageIcon Alphalaneous;
	public static ImageIcon EncodedLua;
	static {
		try {
			verifiedCoin = new ImageIcon(ImageIO
						.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
								.getResource("Resources/GDAssets/verifiedCoin.png")))
						.getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			unverifiedCoin = new ImageIcon(ImageIO
					.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
							.getResource("Resources/GDAssets/unverifiedCoin.png")))
					.getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			GDBoard = new ImageIcon(ImageIO
					.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
							.getResource("Resources/Icons/windowIcon.png")))
					.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			Alphalaneous = new ImageIcon(ImageIO
					.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
							.getResource("Resources/Icons/Alphalaneous.png")))
					.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
			EncodedLua = new ImageIcon(ImageIO
					.read(Objects.requireNonNull(LevelsWindow.class.getClassLoader()
							.getResource("Resources/Icons/EncodedLua.png")))
					.getScaledInstance(80, 80, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private static int width = 30;
	private static int height = 30;


	static void loadAssets() throws IOException {
		String[] difficulties = {"NA", "auto", "easy", "normal", "hard", "harder", "insane", "easy demon", "medium demon",
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
