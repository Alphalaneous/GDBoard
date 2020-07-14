package Main;

import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.WindowedSettings;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class Windowed {
	private static int width = 465;
	private static int height = 512;
	public static JPanel window = new InnerWindow("GDBoard - 0", Settings.getWindowWLoc().x, Settings.getWindowWLoc().y, width-2, height,
			"\uF137", true).createPanel();
	private static JPanel content = new JPanel(null);
	private static JPanel buttonPanel = new JPanel();
	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();
	public static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	public static boolean showingMore = false;
	private static RoundedJButton showMore = createButton("\uE00F", "Show More");

	private static JPanel commentsWindow;


	public static void setOnTop(boolean onTop){
		frame.setAlwaysOnTop(onTop);
		frame.setFocusableWindowState(!onTop);
	}
	static void createPanel() {
		if(WindowedSettings.onTopOption){
			setOnTop(true);
		}
		try {
			if(Settings.getSettings("windowed").equalsIgnoreCase("true")){
				commentsWindow = CommentsWindow.getComWindow();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.close();
			}
		});
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard - 0");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});
		frame.setUndecorated(true);
		frame.setSize(width + 200,height+32 + 200);
		frame.setLayout(null);
		frame.setBackground(new Color(255, 255, 255, 0));
		mainFrame.setBounds(0, 0, width + 200, height + 32 + 200);
		mainFrame.setLayout(null);
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBackground(new Color(0, 0, 0));
		content.setBounds(1,31,width-2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);
		try {
			if(Settings.getSettings("window").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")){
				frame.setLocation((int)Defaults.screenSize.getWidth()/2 - width/2, 200);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		JScrollPane levelsWindow = LevelsWindow.getReqWindow();
		levelsWindow.setBounds(0, 0, levelsWindow.getWidth(), levelsWindow.getHeight());
		commentsWindow.setBounds(400, 0, commentsWindow.getWidth(), 512);
		commentsWindow.setVisible(false);
		JPanel infoWindow = InfoWindow.getInfoWindow();
		infoWindow.setBounds(0, levelsWindow.getHeight()+ 1, infoWindow.getWidth(), infoWindow.getHeight());

		buttonPanel.setBounds(width-58, 0, 50, 512);
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		JButton skip = createButton("\uEB9D", "Next/Skip Level");
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				Functions.skipFunction();
			}
		});
		buttonPanel.add(skip);
		//endregion

		//region Create Random Button

		JButton randNext = createButton("\uF158", "Next Random Level");
		randNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				Functions.randomFunction();

			}
		});
		buttonPanel.add(randNext);
		//endregion

		//region Create Copy Button
		JButton copy = createButton("\uF0E3", "Copy to Clipboard");
		copy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				Functions.copyFunction();
			}
		});
		buttonPanel.add(copy);
		//endregion

		//region Create Block Button
		JButton block = createButton("\uF140", "Block Level");
		block.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				Functions.blockFunction();
			}
		});
		buttonPanel.add(block);
		//endregion

		//region Create Clear Button
		JButton clear = createButton("\uE107", "Clear Queue");
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				Functions.clearFunction();
			}
		});
		buttonPanel.add(clear);
		JButton toggleRequests = createButton("\uE71A", "Toggle Requests");
		toggleRequests.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				Functions.requestsToggleFunction();
				toggleRequests.setText(MainBar.stopReqs.getText());
			}
		});
		buttonPanel.add(toggleRequests);
		JButton settings = createButton("\uE713", "Settings");
		settings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				SettingsWindow.toggleVisible();
			}
		});
		buttonPanel.add(settings);
		showMore.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				((InnerWindow) window).moveToFront();
				showingMore = !showingMore;
				if(showingMore) {
					showMore.setText("\uE00E");
					showMore.setTooltip("Show Less");
					width = 765;
					CommentsWindow.loadComments(0,false);
					commentsWindow.setVisible(true);
				}
				else{
					showMore.setText("\uE00F");
					showMore.setTooltip("Show More");
					width = 465;
					commentsWindow.setVisible(false);
				}
				frame.setSize(new Dimension(width+200, frame.getHeight()));
				mainFrame.setBounds(0, 0, width+200, mainFrame.getHeight());
				content.setBounds(1,31,width-2, content.getHeight());
				window.setBounds(0,0,width, window.getHeight());
				((InnerWindow) window).resetDimensions(width-2, window.getHeight());

				buttonPanel.setBounds(width-58, 0, 50, 512);
			}
		});
		buttonPanel.add(showMore);
		content.add(levelsWindow);
		content.add(commentsWindow);
		content.add(infoWindow);
		content.add(buttonPanel);
		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).setMinimize(true);
		((InnerWindow) window).refreshListener();
		mainFrame.add(window, 1);
		frame.add(mainFrame);
	}
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		content.setBackground(Defaults.SUB_MAIN);
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof JButton) {
				if (!Settings.windowedMode) {
					component.setBackground(Defaults.BUTTON);
				} else {
					component.setBackground(Defaults.MAIN);
				}
				component.setForeground(Defaults.FOREGROUND);
			}
		}

	}
	static void resetCommentSize(){
		commentsWindow.setBounds(400, 0, commentsWindow.getWidth(), 512);

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
	static void toggleVisible() {

		((InnerWindow) window).toggle();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();

	}
	private static RoundedJButton createButton(String icon, String tooltip) {
		RoundedJButton button = new RoundedJButton(icon, tooltip);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		if (!Settings.windowedMode) {
			button.setBackground(Defaults.BUTTON);
		} else {
			button.setBackground(Defaults.MAIN);
		}
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		return button;
	}

	//region SetLocation
	static void setLocation(Point point){
		frame.setLocation(point);
	}
	//endregion
	//region SetSettings
	public static void setSettings(){
		Settings.setWindowSettings("Window", frame.getX() + "," + frame.getY() + "," + false + "," + true);
		try {
			Settings.writeSettings("showMore", String.valueOf(showingMore));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("showMore").equalsIgnoreCase("")){
				showingMore = Boolean.parseBoolean(Settings.getSettings("showMore"));
				if(showingMore){
					showMore.setText("\uE00E");
					showMore.setTooltip("Show Less");
					width = 765;
					CommentsWindow.loadComments(0,false);
					commentsWindow.setVisible(true);
					frame.setSize(new Dimension(width+200, frame.getHeight()));
					mainFrame.setBounds(0, 0, width+200, mainFrame.getHeight());
					content.setBounds(1,31,width-2, content.getHeight());
					window.setBounds(0,0,width, window.getHeight());
					((InnerWindow) window).resetDimensions(width-2, window.getHeight());
					buttonPanel.setBounds(width-58, 0, 50, 512);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//endregion
}
