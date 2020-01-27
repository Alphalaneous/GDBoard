package Main;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

class KeyListener {

	// TODO: Switch to https://github.com/kwhat/jnativehook

	static void hook() throws AWTException {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {

			@Override
			public void keyPressed(GlobalKeyEvent event) {
				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_HOME) {
					if (Overlay.isVisible) {
						Overlay.setWindowsInvisible();
						
					} else {
						Overlay.setWindowsVisible();
						Overlay.frame.toFront();
					}
				}
			}
		});
		Robot r = new Robot();
		Thread thread = new Thread(() -> {
			ControllerManager controllers = new ControllerManager();
			controllers.initSDLGamepad();

			while(true) {
				  ControllerState currState = controllers.getState(0);
				  if(currState.leftStickClick) {
					  if (Overlay.isVisible) {
							Overlay.setWindowsInvisible();

						} else {
							Overlay.setWindowsVisible();
							Overlay.frame.toFront();
						}
				  }
				  if(currState.rightStickClick) {
					  r.keyPress(KeyEvent.VK_CONTROL);
					  r.keyPress(KeyEvent.VK_V);
					  try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  r.keyRelease(KeyEvent.VK_V);
					  r.keyRelease(KeyEvent.VK_CONTROL);
				  }
				  try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
		});
		thread.start();
	}
}
