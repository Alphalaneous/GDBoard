package Main;

import Main.SettingsPanels.ShortcutSettings;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.SwingKeyAdapter;

import javax.swing.*;
import java.awt.*;


public class KeyListener extends SwingKeyAdapter {
	private static boolean keyReleased = false;
	private static boolean usePlatformer = false;
	private static boolean ctrlPressed = false;

	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println(e.getRawCode());
		if(usePlatformer) {
			if (e.getRawCode() == 65) {
				GDMod.run("speed", "-0.9");
			}
			if (e.getRawCode() == 68) {
				GDMod.run("speed", "0.9");
			}
		}
		if(e.getRawCode() == 81 && ctrlPressed){
			usePlatformer = true;
			GDMod.run("speed", "0");
		}
		if(e.getRawCode() == 69){
			usePlatformer = false;
		}
		if(e.getRawCode() == 162 || e.getRawCode() == 163){
			ctrlPressed = true;
		}

		if (keyReleased) {
			int key = e.getRawCode();

			if(key == 187){
				key = 61;
			}
			else if(key == 189){
				key = 45;
			}
			else if(key == 190){
				key = 46;
			}
			else if(key == 188){
				key = 44;
			}
			else if(key == 186){
				key = 59;
			}
			else if(key == 220){
				key = 92;
			}
			else if(key == 221){
				key = 93;
			}
			else if(key == 219){
				key = 91;
			}
			else if(key == 191){
				key = 47;
			}
			else if(key == 46){
				key = 127;
			}
			else if(key == 45){
				key = 155;
			}
			if(!ShortcutSettings.focused) {
				if (key == ShortcutSettings.openKeybind) {

					if (!Overlay.frame.getBackground().equals(new Color(0, 0, 0, 0))) {

						if (!Settings.windowedMode) {
							Overlay.setWindowsInvisible();
						}
						Overlay.frame.toFront();
						Overlay.frame.requestFocus();

					} else {
						if (!Settings.windowedMode) {
							Overlay.setWindowsVisible();
						}
						Overlay.frame.toFront();
						Overlay.frame.requestFocus();


					}
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
					MouseLock.doLock = !MouseLock.doLock;
				}
			}
			keyReleased = false;
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		keyReleased = true;
		if(usePlatformer) {
			if (e.getRawCode() == 65) {
				GDMod.run("speed", "0");

			}
			if (e.getRawCode() == 68) {
				GDMod.run("speed", "0");

			}
		}
		if(e.getRawCode() == 162 || e.getRawCode() == 163){
			ctrlPressed = false;
		}
	}
}
