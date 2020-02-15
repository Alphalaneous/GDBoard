package Main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Overlay {

	// --------------------
	// Create JFrame Object
	
	static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	static boolean isVisible = true;

	static void setFrame() {

		//TODO Windowed Mode

		// --------------------
		// default frame stuff
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(Defaults.screenSize);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setFocusableWindowState(false);
		frame.setBackground(new Color(0, 0, 0, 100));
		frame.setLayout(null);
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				isVisible = false;
				setWindowsInvisible();

			}
		});
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBounds(0,0,1920,1080);
		mainFrame.setBackground(new Color(0,0,0,0));
		mainFrame.setLayout(null);
		
		frame.add(mainFrame);

	}

	static void addToFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.add(component, 0);
		mainFrame.updateUI();

		// --------------------
	}
	
	static void moveToFront(JComponent component) {
		mainFrame.moveToFront(component);
		
	}
	
	static void alwaysFront(JComponent component) {
		mainFrame.setLayer(component, 2);
	}

	static void setVisible() {

		frame.setVisible(true);
	}

	static void refreshUI() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.setSize(Defaults.screenSize);
		frame.invalidate();
		frame.revalidate();
		SettingsWindow.refreshUI();
		LevelsWindow.refreshUI();
		InfoWindow.refreshUI();
		CommentsWindow.refreshUI();
		ActionsWindow.refreshUI();
		SongWindow.refreshUI();
		MainBar.refreshUI();
	}
	
	static void setWindowsInvisible() {
		isVisible = false;
		frame.setBackground(new Color(0, 0, 0, 0));
		CommentsWindow.setInvisible();
		ActionsWindow.setInvisible();
		InfoWindow.setInvisible();
		LevelsWindow.setInvisible();
		SongWindow.setInvisible();
		SettingsWindow.setInvisible();
		MainBar.setInvisible();
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

	static void setWindowsVisible() {
		isVisible = true;
		frame.setBackground(new Color(0, 0, 0, 100));
		CommentsWindow.setVisible();
		ActionsWindow.setVisible();
		InfoWindow.setVisible();
		LevelsWindow.setVisible();
		SongWindow.setVisible();
		SettingsWindow.setVisible();
		MainBar.setVisible();
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

}

