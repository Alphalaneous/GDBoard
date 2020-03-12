package Main;

import SettingsPanels.ShortcutSettings;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.awt.*;


public class KeyListener {
    private static boolean keyReleased = false;

    public static void hook() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override
            public void keyPressed(GlobalKeyEvent e) {
                System.out.println(e.getVirtualKeyCode());
                if(keyReleased) {
                    if (e.getVirtualKeyCode() == ShortcutSettings.openKeybind) {

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
                    if (e.getVirtualKeyCode() == ShortcutSettings.skipKeybind) {
                        Functions.skipFunction();
                    }
                    if (e.getVirtualKeyCode() == ShortcutSettings.randKeybind) {
                        Functions.randomFunction();
                    }
                    if (e.getVirtualKeyCode() == ShortcutSettings.copyKeybind) {
                        Functions.copyFunction();
                    }
                    if (e.getVirtualKeyCode() == ShortcutSettings.blockKeybind) {
                        Functions.blockFunction();
                    }
                    if (e.getVirtualKeyCode() == ShortcutSettings.clearKeybind) {
                        Functions.clearFunction();
                    }
                    keyReleased = false;
                }
            }
            @Override
            public void keyReleased(GlobalKeyEvent e) {
                keyReleased = true;
            }
        });
    }
}
