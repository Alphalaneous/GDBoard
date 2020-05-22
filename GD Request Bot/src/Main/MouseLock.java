package Main;

import com.sun.jna.platform.WindowUtils;

import java.awt.*;

class MouseLock {
	static boolean doLock = false;

	static void startLock() {

		Thread thread = new Thread(() -> {

			while (true) {
				if (doLock && !Overlay.isVisible) {
					Rectangle rect = new Rectangle(0, 0, 0, 0);
					WindowUtils.getAllWindows(true).forEach(desktopWindow -> {
						Robot r = null;
						try {
							r = new Robot();
						} catch (AWTException e) {
							e.printStackTrace();
						}
						if (desktopWindow.getTitle().contains("Geometry Dash")) {
							rect.setRect(desktopWindow.getLocAndSize());
							Point p = MouseInfo.getPointerInfo().getLocation();
							assert r != null;
							if (p.x <= rect.getX() + 15 && p.y > rect.getY() && p.y < rect.getY() + rect.getHeight()) {
								r.mouseMove((int) rect.getX() + 15, p.y);
							}
							if (p.x >= rect.getX() + rect.getWidth() - 15 && p.y > rect.getY() && p.y < rect.getY() + rect.getHeight()) {
								r.mouseMove((int) (rect.getX() + rect.getWidth() - 15), p.y);
							}
							if (p.y <= rect.getY() + 10 && p.x > rect.getX() && p.x < rect.getX() + rect.getWidth()) {
								r.mouseMove(p.x,(int) rect.getY() + 10);
							}
							if (p.y >= rect.getY() + rect.getHeight() - 15 && p.x > rect.getX() && p.x < rect.getX() + rect.getWidth()) {
								r.mouseMove(p.x, (int) (rect.getY() + rect.getHeight() - 15));
							}
							if(p.x <= rect.getX() + 15 && p.y <= rect.getY() + 15){
								r.mouseMove((int)rect.getX() + 15, (int)rect.getY() + 15);
							}
							if(p.x <= rect.getX() + 15 && p.y >= rect.getY() + rect.getHeight() - 15){
								r.mouseMove((int)rect.getX() + 15, (int)(rect.getY() + rect.getHeight()) - 15);
							}
							if(p.x >= rect.getX() + rect.getWidth() - 15 && p.y >= rect.getY() + rect.getHeight() - 15){
								r.mouseMove((int)(rect.getX() + rect.getWidth())- 15, (int)(rect.getY() + rect.getHeight()) - 15);
							}
							if(p.x >= rect.getX() + rect.getWidth() - 15 && p.y <= rect.getY() + 15){
								r.mouseMove((int)(rect.getX() + rect.getWidth()) - 15, (int)rect.getY() + 15);
							}
						}
					});
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
}
