package Main;

import Main.InnerWindows.*;
import Main.SettingsPanels.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import static Main.Defaults.defaultUI;
import static Main.Defaults.settingsButtonUI;

public class Overlay {

	// --------------------
	// Create JFrame Object

	public static JDialog frame = new JDialog();
	private static JLayeredPane mainFrame = new JLayeredPane();
	static boolean isVisible = true;

	public static void createOverlay() {
		MainBar.createBar();
		frame.setUndecorated(true);
				frame.setFocusableWindowState(false);
				frame.getRootPane().setOpaque(false);
				frame.setBackground(new Color(0, 0, 0, 100));
				URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
				ImageIcon icon = new ImageIcon(iconURL);
				Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
				frame.setIconImage(newIcon);
				frame.setTitle("GDBoard - Settings");
				frame.setBounds(Defaults.screenSize);
				frame.setLayout(null);
				mainFrame.setDoubleBuffered(true);
				mainFrame.setBounds(0, 0, Defaults.screenSize.width, Defaults.screenSize.height);
				mainFrame.setOpaque(false);
				mainFrame.setBackground(new Color(0, 0, 0, 0));
				mainFrame.setLayout(null);
				frame.toBack();
				frame.add(mainFrame);
				frame.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						isVisible = false;
						setWindowsInvisible();

					}
				});
	}
	public static void destroyOverlay(){
		frame.setVisible(false);
		MainBar.destroyBar();
		mainFrame.removeAll();
		frame.removeAll();
		frame.dispose();
		frame = new JDialog();
	}
	public static void addToFrame(JComponent component) {

		// --------------------
		// Add components to JFrame from elsewhere

		mainFrame.add(component, 0);

		// --------------------
	}
	public static void removeFromFrame(JComponent component) {

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

	public static void setVisible() {
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
	}

	public static void refreshUI(boolean color) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			frame.getContentPane().setBackground(Defaults.SUB_MAIN);
		} else {
			mainFrame.setBounds(0, 0, Defaults.screenSize.width, Defaults.screenSize.height);
			frame.setBounds(Defaults.screenSize);
			frame.invalidate();
			frame.revalidate();
		}
		if(Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			Windowed.refreshUI();
		}
		else {
			ActionsWindow.refreshUI();
			SongWindow.refreshUI();
			MainBar.refreshUI(color);
			//Randomizer.refreshUI();
		}
		for(FancyTooltip toolTip : FancyTooltip.tooltips){
			toolTip.refresh();
		}
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		settingsButtonUI.setBackground(Defaults.BUTTON);
		settingsButtonUI.setHover(Defaults.BUTTON_HOVER);
		settingsButtonUI.setSelect(Defaults.SELECT);
		CommentsWindow.refreshUI();
		SettingsWindow.refreshUI();
		LevelsWindow.refreshUI();
		InfoWindow.refreshUI();
		AccountSettings.refreshUI();
		PersonalizationSettings.refreshUI();
		GeneralBotSettings.refreshUI();
		BlockedSettings.refreshUI();
		BlockedUserSettings.refreshUI();
		BlockedCreatorSettings.refreshUI();
		GeneralSettings.refreshUI();
		CommandSettings.refreshUI();
		ChannelPointSettings.refreshUI();
		CheerSettings.refreshUI();

		ShortcutSettings.refreshUI();
		RequestSettings.refreshUI();
		CommandEditor.refreshUI();
		RequestsLog.refreshUI();
		WindowedSettings.refreshUI();
		OutputSettings.refreshUI();
	}
	static void revalidate(){
		frame.invalidate();
		frame.validate();
	}

	public static JDialog getWindow(){
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
	}

	static void setWindowsVisible() {
		isVisible = true;
		frame.setBackground(new Color(0, 0, 0, 100));
		revalidate();
		CommentsWindow.setVisible();
		ActionsWindow.setVisible();
		InfoWindow.setVisible();
		LevelsWindow.setVisible();
		SongWindow.setVisible();
		SettingsWindow.setVisible();
		MainBar.setVisible();

		//Randomizer.setVisible();
	}

}

