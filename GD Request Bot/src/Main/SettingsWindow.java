package Main;

import Main.InnerWindows.BlockedGDUserSettings;
import com.jidesoft.swing.ResizablePanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Set;
import javax.swing.*;
import Main.SettingsPanels.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

public class SettingsWindow {
	private static int width = 622;
	private static int height = 622;
	public static ResizablePanel window;
	static{
		try {
			if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
				window = new InnerWindow("Settings", 0, 0, width-2, height,
						"\uE713", true).createPanel();
			}
			else{
				window = new InnerWindow("Settings", 0, 0, width-2, height,
						"\uE713", false).createPanel();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static JPanel content = new JPanel();
	private static JPanel blankSpace = new JPanel();

	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();
	private static JPanel general = GeneralSettings.createPanel();
	private static JPanel overlay = OutputSettings.createPanel();
	private static JPanel accounts = AccountSettings.createPanel();
	private static JPanel commands = CommandSettings.createPanel();
	private static JPanel points = ChannelPointSettings.createPanel();
	private static JPanel cheers = CheerSettings.createPanel();
	private static JPanel requests = RequestSettings.createPanel();
	private static JPanel shortcuts = ShortcutSettings.createPanel();
	private static JPanel personalization = PersonalizationSettings.createPanel();
	private static JPanel blocked = BlockedSettings.createPanel();
	private static JPanel blockedUsers = BlockedUserSettings.createPanel();
	private static JPanel blockedCreators = BlockedGDUserSettings.createPanel();

	private static JPanel loggedIDs = RequestsLog.createPanel();
	public static JFrame frame = new JFrame();
	public static JPanel windowed = WindowedSettings.createPanel();

	public static boolean run = true;
	static void createPanel() {
		frame.setSize(800,800);
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard - Settings");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});
		try {
			if(Settings.getSettings("settings").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")){
				frame.setLocation((int)Defaults.screenSize.getWidth()/2 - width/2, 200);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setUndecorated(true);
		frame.setSize(width + 200,height+32 + 200);
		frame.setLayout(null);
		frame.setBackground(new Color(255, 255, 255, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		blankSpace.setBounds(1, 31, 208, 20);
		blankSpace.setBackground(Defaults.MAIN);

		buttons.setBounds(1, 51, 208, height-20);
		buttons.setBackground(Defaults.MAIN);

		content.setBounds(209, 31, 412, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);

		content.add(general);
		content.add(overlay);
		content.add(accounts);
		content.add(commands);
		content.add(points);
		content.add(cheers);
		content.add(requests);
		content.add(shortcuts);
		content.add(personalization);
		content.add(blocked);
		content.add(blockedUsers);
		content.add(blockedCreators);
		content.add(loggedIDs);
		content.add(windowed);

		general.setVisible(true);
		overlay.setVisible(false);
		accounts.setVisible(false);
		commands.setVisible(false);
		points.setVisible(false);
		cheers.setVisible(false);
		requests.setVisible(false);
		shortcuts.setVisible(false);
		personalization.setVisible(false);
		blocked.setVisible(false);
		blockedUsers.setVisible(false);
		blockedCreators.setVisible(false);
		windowed.setVisible(false);
		loggedIDs.setVisible(false);


		JButton general = createButton("General");
		general.setBackground(Defaults.SELECT);
		general.setUI(selectUI);
		JButton outputs = createButton("Outputs");
		JButton accounts = createButton("Accounts");
		JButton commands = createButton("Commands");
		JButton points = createButton("Channel Points");
		JButton cheers = createButton("Cheers");
		JButton requests = createButton("Requests");
		JButton shortcuts = createButton("Shortcuts");
		JButton personalization = createButton("Personalization");
		JButton blocked = createButton("Blocked IDs");
		JButton blockedUsers = createButton("Blocked Users");
		JButton blockedCreators = createButton("Blocked Creators");
		JButton loggedIDs = createButton("Logged IDs");
		JButton windowed = createButton("Windowed");


		buttons.add(general);
		buttons.add(outputs);
		buttons.add(accounts);
		buttons.add(commands);
		buttons.add(points);
		buttons.add(cheers);
		buttons.add(requests);
		buttons.add(shortcuts);
		buttons.add(personalization);
		buttons.add(blocked);
		buttons.add(blockedUsers);
		buttons.add(blockedCreators);
		buttons.add(loggedIDs);
		if(Settings.windowedMode){
			buttons.add(windowed);
		}
		toggleVisible();
		window.add(blankSpace);
		window.add(buttons);
		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).refreshListener();
		if(Settings.windowedMode){
			frame.add(window);
		}
		else {
			Overlay.addToFrame(window);
		}
	}
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		blankSpace.setBackground(Defaults.MAIN);
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		buttons.setBackground(Defaults.MAIN);
		content.setBackground(Defaults.SUB_MAIN);
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				if(!((JButton) component).getUI().equals(selectUI)) {
					component.setBackground(Defaults.MAIN);
				}
				else{
					component.setBackground(Defaults.SELECT);
				}

			}
		}

	}

	static void toggleVisible() {

		((InnerWindow) window).toggle();
	}

	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();

	}

	private static JButton createButton(String text) {

		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);

		JButton button = new JButton();
		JLabel label = new JLabel(text);

		label.setBounds(20, 9, 208, 20);
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));

		button.setLayout(null);
		button.add(label);
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setPreferredSize(new Dimension(208, 38));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				for (Component component2 : button.getComponents()) {
					if (component2 instanceof JLabel) {
						for (Component componentA : content.getComponents()) {
							if (componentA instanceof JPanel) {
								componentA.setVisible(false);
							}
						}
						switch (((JLabel) component2).getText()) {
							case "General":
								general.setVisible(true);
								break;
							case "Outputs":
								overlay.setVisible(true);
								break;
							case "Accounts":
								accounts.setVisible(true);
								break;
							case "Shortcuts":
								shortcuts.setVisible(true);
								break;
							case "Personalization":
								personalization.setVisible(true);
								break;
							case "Blocked IDs":
								blocked.setVisible(true);
								break;
							case "Blocked Users":
								blockedUsers.setVisible(true);
								break;
							case "Blocked Creators":
								blockedCreators.setVisible(true);
								break;
							case "Commands":
								commands.setVisible(true);
								break;
							case "Channel Points":
								points.setVisible(true);
								break;
							case "Cheers":
								cheers.setVisible(true);
								break;
							case "Requests":
								requests.setVisible(true);
								break;
							case "Windowed":
								windowed.setVisible(true);
								break;
							case "Logged IDs":
								loggedIDs.setVisible(true);
								break;
						}
						break;
					}
				}
				for (Component component : buttons.getComponents()) {
					if (component instanceof JButton) {
						((JButton) component).setUI(defaultUI);
						component.setBackground(Defaults.MAIN);
						buttons.updateUI();
						
					}
				}

				button.setUI(selectUI);
				button.setBackground(Defaults.SELECT);
			}
		});
		return button;
	}
	//region SetLocation
	static void setLocation(Point point){
		if(Settings.windowedMode){
			frame.setLocation(point);
		}
		else {
			window.setLocation(point);
		}
	}
	//endregion
	//region SetSettings
	public static void setSettings(){
		if(Settings.windowedMode){
			Settings.setWindowSettings("Settings", frame.getX() + "," + frame.getY() + "," + false + "," + frame.isVisible());
		}
		else {
			Settings.setWindowSettings("Settings", window.getX() + "," + window.getY() + "," + false + "," + window.isVisible());
		}

	}
	//endregion
}
