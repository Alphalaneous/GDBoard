package Main;

import SettingsPanels.ShortcutSettings;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.SwingKeyAdapter;

import java.awt.*;


public class KeyListener extends SwingKeyAdapter {
    private static boolean keyReleased = false;


    public void nativeKeyPressed(NativeKeyEvent e) {

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
                    SettingsWindow.toFront();


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
            keyReleased = false;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        keyReleased = true;
    }
}
