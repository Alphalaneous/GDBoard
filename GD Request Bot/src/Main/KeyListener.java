package Main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import javax.swing.*;

class KeyListener {

    private static boolean openKeyReleased = false;

    static void hook() throws AWTException {
        //TODO custom keybinds
        //region Keyboard Listener
        int keyCode = Settings.keybind;
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
        keyboardHook.addKeyListener(new GlobalKeyAdapter() {


            @Override
            public void keyReleased(GlobalKeyEvent event) {
                if (event.getVirtualKeyCode() == keyCode) {
                    openKeyReleased = true;
                }
            }

            @Override
            public void keyPressed(GlobalKeyEvent event) {
                if (event.getVirtualKeyCode() == keyCode) {

                    if (Overlay.isVisible) {
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
        });
        //endregion

        //region Controller Listener

        Robot r = new Robot();
        Thread thread = new Thread(() -> {
            ControllerManager controllers = new ControllerManager();
            controllers.initSDLGamepad();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                ControllerState currState = controllers.getState(0);
                if (currState.leftStickClick) {
                    if (Overlay.isVisible) {
                        if (!Settings.windowedMode) {
                            Overlay.setWindowsInvisible();
                        }
                    } else {
                        if (!Settings.windowedMode) {
                            Overlay.setWindowsVisible();
                        }
                        Overlay.frame.toFront();
                    }
                }
                if (currState.rightStickClick) {
                    r.keyPress(KeyEvent.VK_CONTROL);
                    r.keyPress(KeyEvent.VK_V);

                    r.keyRelease(KeyEvent.VK_V);
                    r.keyRelease(KeyEvent.VK_CONTROL);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        //endregion
    }
}
