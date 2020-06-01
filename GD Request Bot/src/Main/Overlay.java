package Main;

import Main.InnerWindows.*;
import Main.SettingsPanels.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Overlay {

	// --------------------
	// Create JFrame Object

	public static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	static boolean isVisible = true;

	static void setFrame() {

		// --------------------
		// default frame stuff


		frame.setFocusableWindowState(false);
		frame.setUndecorated(true);
		if(!Settings.windowedMode) {
			frame.setBackground(new Color(0, 0, 0, 100));
			frame.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					isVisible = false;
					setWindowsInvisible();

				}
			});
		}
		else{
			frame.setBackground(new Color(0, 0, 0, 0));

		}
		frame.setBounds(Defaults.screenSize);
		frame.setLayout(null);
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBounds(0, 0, Defaults.screenSize.width, Defaults.screenSize.height);
		mainFrame.setBackground(new Color(0, 0, 0, 0));
		mainFrame.setLayout(null);
		frame.toBack();
		frame.add(mainFrame);


	}

	public static void addToFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.add(component, 0);

		// --------------------
	}
	static void removeFromFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.remove(component);

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
		frame.setAlwaysOnTop(true);
		SettingsWindow.toFront();
	}

	static void refreshUI(boolean color) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (Settings.windowedMode) {
			frame.getContentPane().setBackground(Defaults.SUB_MAIN);
		} else {
			mainFrame.setBounds(0, 0, Defaults.screenSize.width, Defaults.screenSize.height);
			frame.setBounds(Defaults.screenSize);
		}
		frame.invalidate();
		frame.revalidate();
		if(Settings.windowedMode) {
			Windowed.refreshUI();
		}
		else {
			CommentsWindow.refreshUI();
			ActionsWindow.refreshUI();
			SongWindow.refreshUI();
			MainBar.refreshUI(color);
			//Randomizer.refreshUI();
		}
		SettingsWindow.refreshUI();
		LevelsWindow.refreshUI();
		InfoWindow.refreshUI();
		AccountSettings.refreshUI();
		PersonalizationSettings.refreshUI();
		BlockedSettings.refreshUI();
		BlockedUserSettings.refreshUI();
		GeneralSettings.refreshUI();
		CommandSettings.refreshUI();

		ShortcutSettings.refreshUI();
		RequestSettings.refreshUI();
		OutputSettings.refreshUI();
	}
	public static JFrame getWindow(){
		return frame;
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
		//Randomizer.setInvisible();
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
		//Randomizer.setVisible();
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}

}

