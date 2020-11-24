package com.alphalaneous;

import com.alphalaneous.SettingsPanels.PersonalizationSettings;
import com.alphalaneous.SettingsPanels.ShortcutSettings;
import com.alphalaneous.Windows.Window;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.SwingKeyAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class KeyListener extends SwingKeyAdapter {
	private static boolean keyReleased = false;
	private static boolean usePlatformer = false;
	private static boolean ctrlPressed = false;
	private static boolean goLeft = true;
	private static boolean goRight = true;


	public void nativeKeyPressed(NativeKeyEvent e) {
		if (usePlatformer) {
			if (e.getRawCode() == 65) {
				if (goLeft) {
					GDMod.runNew("speed", "-0.9");
					goLeft = false;
				}
			}
			if (e.getRawCode() == 68) {
				if (goRight) {
					GDMod.runNew("speed", "0.9");
					goRight = false;
				}
			}
		}
		if (e.getRawCode() == 81 && ctrlPressed) {
			usePlatformer = true;
			GDMod.runNew("speed", "0");
		}
		if (e.getRawCode() == 69) {
			usePlatformer = false;
		}
		if (e.getRawCode() == 162 || e.getRawCode() == 163) {
			ctrlPressed = true;
		}
		//System.out.println(e.getRawCode());


		if (keyReleased) {

			int key = e.getRawCode();

			if (key == 187) {
				key = 61;
			} else if (key == 189) {
				key = 45;
			} else if (key == 190) {
				key = 46;
			} else if (key == 188) {
				key = 44;
			} else if (key == 186) {
				key = 59;
			} else if (key == 220) {
				key = 92;
			} else if (key == 221) {
				key = 93;
			} else if (key == 219) {
				key = 91;
			} else if (key == 191) {
				key = 47;
			} else if (key == 46) {
				key = 127;
			} else if (key == 45) {
				key = 155;
			}
			if (!ShortcutSettings.focused) {
				if (key == ShortcutSettings.openKeybind) {
					Window.frame.setAlwaysOnTop(true);
					Window.frame.setAlwaysOnTop(PersonalizationSettings.onTopOption);
				}
				if (key == ShortcutSettings.skipKeybind) {
					Functions.skipFunction();
				}
				if (key == ShortcutSettings.randKeybind) {
					Functions.randomFunction();
				}
				if (key == ShortcutSettings.copyKeybind) {
					Functions.copyFunction();
				}
				if (key == ShortcutSettings.blockKeybind) {
					Functions.blockFunction();
				}
				if (key == ShortcutSettings.clearKeybind) {
					Functions.clearFunction();
				}
				if (key == ShortcutSettings.lockKeybind) {
				}
			}
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/actions/keybinds.txt"))) {
				Scanner sc3 = null;
				try {
					sc3 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/actions/keybinds.txt").toFile());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				assert sc3 != null;
				while (sc3.hasNextLine()) {
					String line = sc3.nextLine();
					if (line.split("=")[0].replace(" ", "").equalsIgnoreCase(String.valueOf(e.getRawCode()))) {
						Path path = Paths.get(Defaults.saveDirectory + "/GDBoard/actions/" + line.split("=")[1] + ".js");
						if (Files.exists(path))
							new Thread(() -> {
								try {
									String response = Command.run(Files.readString(path, StandardCharsets.UTF_8), false);
									if (!response.equalsIgnoreCase("")) {
										Main.sendMessage(response);
									}
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}).start();
						break;
					}
				}
				sc3.close();
			}

			keyReleased = false;
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		keyReleased = true;
		if (usePlatformer) {
			if (e.getRawCode() == 65) {
				GDMod.runNew("speed", "0");
				goLeft = true;
			}
			if (e.getRawCode() == 68) {
				GDMod.runNew("speed", "0");
				goRight = true;
			}
		}
		if (e.getRawCode() == 162 || e.getRawCode() == 163) {
			ctrlPressed = false;
		}
	}
}
