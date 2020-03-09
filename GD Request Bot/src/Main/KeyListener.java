package Main;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;


public class KeyListener implements NativeKeyListener {
    private static boolean openKeyReleased = false;
    private int keyCode = Settings.keybind;

    KeyListener() {
    }
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println(e.getRawCode());
        if (e.getRawCode() == keyCode) {

            if (!Overlay.frame.getBackground().equals(new Color(0, 0, 0, 0))) {
                if (openKeyReleased) {
                    if (!Settings.windowedMode) {
                        Overlay.setWindowsInvisible();
                    } else {
                        Overlay.frame.toFront();
                        Overlay.frame.requestFocus();
                    }
                    openKeyReleased = false;
                }


            } else {
                if (openKeyReleased) {
                    if (!Settings.windowedMode) {
                        Overlay.setWindowsVisible();
                    }
                    Overlay.frame.toFront();
                    Overlay.frame.requestFocus();
                    openKeyReleased = false;
                }

            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getRawCode() == keyCode) {
            openKeyReleased = true;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}
