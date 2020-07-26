package Main;

import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.WindowedSettings;
import com.jhlabs.image.GaussianFilter;
import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static Main.Defaults.defaultUI;

public class Windowed {
	private static int width = 465;
	private static int height = 600;
	//public static ResizablePanel window = new InnerWindow("GDBoard - 0", Settings.getWindowWLoc().x, Settings.getWindowWLoc().y, width-2, height,
	//		"\uF137", true).createPanel();
	private static JPanel content = new JPanel(null);
	private static JPanel buttonPanel = new JPanel();
	private static JButtonUI selectUI = new JButtonUI();

	public static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	public static boolean showingMore = true;
	private static RoundedJButton showMore = createButton("\uE00F", "Show More");
	private static JButton showComments;

	public static void refresh(){
		frame.invalidate();
		frame.validate();
	}

	public static void setOnTop(boolean onTop){
		frame.setAlwaysOnTop(onTop);
		frame.setFocusableWindowState(!onTop);
	}
	static void createPanel() {
		if(WindowedSettings.onTopOption){
			setOnTop(true);
		}
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.close();
			}
		});
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard - 0");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});
		frame.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent evt) {
				Component c = (Component)evt.getSource();
				content.setBounds(0,0,frame.getWidth()-10, frame.getHeight()-38);
				//window.setBounds(-5,-35,frame.getWidth(), frame.getHeight());
				//((InnerWindow) window).resetDimensions(frame.getWidth()-10, frame.getHeight());
				buttonPanel.setBounds(frame.getWidth()-68, 0, 50, frame.getHeight());
				if(showingMore) {
					LevelsWindow.resizeButtons(frame.getWidth() - 375, frame.getHeight() - 152);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 375, frame.getHeight() - 152);
					CommentsWindow.getComWindow().setBounds(frame.getWidth() - 375, 0, CommentsWindow.getComWindow().getWidth(), frame.getHeight() + 2);
					CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), frame.getHeight() + 2);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				}
				else {
					LevelsWindow.resizeButtons(frame.getWidth()-75, frame.getHeight() - 152);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth()-75 , frame.getHeight() - 152);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				}
				refresh();
			}
		});
		frame.setSize(width + 200,height+32 + 200);
		frame.setMinimumSize(new Dimension(465*Defaults.relativeWidth, 600*Defaults.relativeHeight));
		frame.setLayout(null);
		mainFrame.setBounds(20, 20, width + 200, height + 32 + 200);
		mainFrame.setLayout(null);
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBackground(new Color(0, 0, 0));
		content.setBounds(5,35,width-2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);
		try {
			if(Settings.getSettings("window").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")){
				frame.setLocation((int)Defaults.screenSize.getWidth()/2 - width/2, 200);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		LevelsWindow.getReqWindow().setBounds(0, 0, LevelsWindow.getReqWindow().getWidth(), LevelsWindow.getReqWindow().getHeight());
		CommentsWindow.getComWindow().setBounds(400, 0, CommentsWindow.getComWindow().getWidth(), height);
		CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), height);

		CommentsWindow.getComWindow().setVisible(false);

		InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight()+ 1, InfoWindow.getInfoWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());

		buttonPanel.setBounds(width-58, 0, 50, frame.getHeight());
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		JButton skip = createButton("\uEB9D", "Next/Skip Level");
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//((InnerWindow) window).moveToFront();
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
				//((InnerWindow) window).moveToFront();
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
				//((InnerWindow) window).moveToFront();
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
				//((InnerWindow) window).moveToFront();
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
				//((InnerWindow) window).moveToFront();
				Functions.clearFunction();
			}
		});
		buttonPanel.add(clear);
		JButton toggleRequests = createButton("\uE71A", "Toggle Requests");
		toggleRequests.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				SettingsWindow.run = false;
				//((InnerWindow) window).moveToFront();
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
				//((InnerWindow) window).moveToFront();
				SettingsWindow.toggleVisible();
			}
		});
		buttonPanel.add(settings);
		new Thread(() -> {
			CommentsWindow.loadComments(0,false);
		}).start();
		CommentsWindow.getComWindow().setVisible(true);


		showComments = createButton("\uE134", "Hide Comments");
		showComments.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showingMore = !showingMore;
				System.out.println(showingMore);
				if(!showingMore){
					CommentsWindow.unloadComments(true);
					((RoundedJButton) showComments).setTooltip("Show Comments");
					frame.setMinimumSize(new Dimension(465, 600));
					CommentsWindow.getComWindow().setVisible(false);
					LevelsWindow.resizeButtons(frame.getWidth()-75, frame.getHeight()-152);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth()-75, frame.getHeight()-152);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight()+ 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					refresh();
				}
				else{
					((RoundedJButton) showComments).setTooltip("Hide Comments");
					if(frame.getWidth() < 765) {
						frame.setSize(frame.getWidth()+300, frame.getHeight());
					}
					frame.setMinimumSize(new Dimension(765, 600));
					CommentsWindow.loadComments(0, false);
					CommentsWindow.getComWindow().setVisible(true);
					CommentsWindow.getComWindow().setBounds(frame.getWidth() - 375, 0, CommentsWindow.getComWindow().getWidth(), frame.getHeight() + 2);
					CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), frame.getHeight() + 2);
					LevelsWindow.resizeButtons(frame.getWidth()-375, frame.getHeight()-152);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth()-375, frame.getHeight()-152);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight()+ 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					refresh();
				}
			}
		});
		buttonPanel.add(showComments);


		JButton donate = createButton("\uE006", "Donate");
		donate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.paypal.me/xAlphalaneous");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		buttonPanel.add(donate);


		JButton discord = createButton("\uE13A", "Discord");
		discord.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://discord.gg/x2awccH");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		buttonPanel.add(discord);

		content.add(LevelsWindow.getReqWindow());
		content.add(CommentsWindow.getComWindow());
		content.add(InfoWindow.getInfoWindow());
		content.add(buttonPanel);
		//window.add(content);
		//((InnerWindow) window).setPinVisible();
		//((InnerWindow) window).setMinimize(true);
		//((InnerWindow) window).refreshListener();
		//mainFrame.add(window);
		frame.getContentPane().add(content);
	}
	static void refreshUI() {
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
		CommentsWindow.getComWindow().setBounds(400, 0, CommentsWindow.getComWindow().getWidth(), 600);

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

		//((InnerWindow) window).toggle();
	}

	static void setVisible() {
		//((InnerWindow) window).setVisible();

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
			Settings.writeSettings("windowSize", frame.getWidth() + "," + frame.getHeight());
			Settings.writeSettings("showMore", String.valueOf(showingMore));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void loadSettings(){
		try {
			if(!Settings.getSettings("windowSize").equalsIgnoreCase("")){
				String[] dim = Settings.getSettings("windowSize").split(",");
				int newW = Integer.parseInt(dim[0]);
				int newH = Integer.parseInt(dim[1]);
				width = newW;
				height = newH;
				frame.setSize( newW, newH);
				content.setBounds(0,0,newW-10, newH-38);
				buttonPanel.setBounds(newW-68, 0, 50, frame.getHeight());
				LevelsWindow.resizeButtons(newW-375, newH-152);
				LevelsWindow.getReqWindow().setBounds(0, 0, newW-375, newH-152);
				CommentsWindow.getComWindow().setBounds(newW-375, 0, CommentsWindow.getComWindow().getWidth(), newH+2);
				CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), newH+2);
				InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight()+ 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				refresh();
			}
			if(!Settings.getSettings("showMore").equalsIgnoreCase("")) {
				if(!Settings.getSettings("showMore").equalsIgnoreCase("true")){
					CommentsWindow.unloadComments(true);
					((RoundedJButton) showComments).setTooltip("Show Comments");
					showingMore = false;
					CommentsWindow.getComWindow().setVisible(false);
					LevelsWindow.resizeButtons(frame.getWidth()-75, frame.getHeight()-152);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth()-75, frame.getHeight()-152);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight()+ 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					refresh();
				}
				else{
					frame.setMinimumSize(new Dimension(765*Defaults.relativeWidth, 600*Defaults.relativeHeight));
				}
			}
			else{
				CommentsWindow.unloadComments(true);
				((RoundedJButton) showComments).setTooltip("Show Comments");
				showingMore = false;
				CommentsWindow.getComWindow().setVisible(false);
				LevelsWindow.resizeButtons(frame.getWidth()-75, frame.getHeight()-152);
				LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth()-75, frame.getHeight()-152);
				InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight()+ 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				refresh();
			}
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//endregion
}
