package Main;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.awt.*;


public class KeyListener {
    private static boolean openKeyReleased = false;
    private static int keyCode = Settings.keybind;
    public static void hook(){
    GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(false);
    keyboardHook.addKeyListener(new GlobalKeyAdapter() {
        @Override
        public void keyPressed(GlobalKeyEvent e) {
            System.out.println(e.getVirtualKeyCode());
            if (e.getVirtualKeyCode() == keyCode) {

                if (!Overlay.frame.getBackground().equals(new Color(0, 0, 0, 0))) {
                    if (openKeyReleased) {
                        if (!Settings.windowedMode) {
                            Overlay.setWindowsInvisible();
                        }
                        Overlay.frame.toFront();
                        Overlay.frame.requestFocus();
                        openKeyReleased = false;
                    }


                } else {
                    if (openKeyReleased) {
                        if (!Settings.windowedMode) {
                            Overlay.setWindowsVisible();
                        }
                        Overlay.frame.toFront();
                        Overlay.frame.requestFocus();
                        SettingsWindow.toFront();
                        openKeyReleased = false;
                    }

                }
            }
        }
        @Override
        public void keyReleased(GlobalKeyEvent e) {
            if (e.getVirtualKeyCode() == keyCode) {
                openKeyReleased = true;
            }
        }

    });
}
}
