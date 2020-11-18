package com.alphalaneous.Windows;

import com.alphalaneous.Components.*;
import com.alphalaneous.Panels.InfoPanel;
import com.alphalaneous.Panels.LevelsPanel;
import com.alphalaneous.SettingsPanels.PersonalizationSettings;
import com.alphalaneous.SettingsPanels.WindowedSettings;
import com.alphalaneous.*;
import com.alphalaneous.Panels.CommentsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static com.alphalaneous.Defaults.defaultUI;

public class Window {
	private static int width = 465;
	private static int height = 630;
	private static JPanel content = new JPanel(null);
	private static JPanel actionsPanel = new JPanel(new GridLayout(10, 1, 10, 10));
	private static RoundedJButton switchButton = new RoundedJButton("\uF1CB", "$SWITCH_PAGES$");
	private static boolean gdPage = true;
	private static JPanel buttonPanel = new JPanel();
	private static JPanel sideButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
	private static JPanel iconPanel = new JPanel(null);
	private static JFrame attributions = new JFrame();
	private static JButtonUI selectUI = new JButtonUI();
	private static JPanel toolBar = new JPanel(null);
	private static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	public static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	public static boolean showingMore = true;
	private static JButtonUI buttonUI = new JButtonUI();
	private static JButton showComments;
	private static JLabel AlphalaneousText = new JLabel("Alphalaneous");
	private static JLabel EncodedLuaText = new JLabel("EncodedLua");
	private static String selectedUsername;
	private static int selectedID;

	private static JPanel infoPanel = new JPanel(null);
	private static FancyTextArea message = new FancyTextArea(false, false);
	private static JPanel modButtons = new JPanel();


	public static void refresh() {
		frame.invalidate();
		frame.validate();
	}

	public static void setOnTop(boolean onTop) {
		frame.setFocusableWindowState(!onTop);
		frame.setAlwaysOnTop(onTop);
	}

	public static void createPanel() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.close();
			}
		});
		URL iconURL = Window.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH);
		buttonUI.setBackground(Defaults.TOP);
		buttonUI.setHover(Defaults.BUTTON_HOVER);
		buttonUI.setSelect(Defaults.SELECT);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard - 0");
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				content.setBounds(40, 0, frame.getWidth() - 10, frame.getHeight() - 38);
				actionsPanel.setBounds(30, 0, frame.getWidth() - 16, frame.getHeight() - 38);

				//window.setBounds(-5,-35,frame.getWidth(), frame.getHeight());
				//((InnerWindow) window).resetDimensions(frame.getWidth()-10, frame.getHeight());
				buttonPanel.setBounds(frame.getWidth() - 110, 0, 50, frame.getHeight() - 70);
				sideButtons.setBounds(0, -5, 40, frame.getHeight() - 15);

				iconPanel.setBounds(frame.getWidth() - 110, frame.getHeight() - 95, 50, 50);
				toolBar.setBounds(0, 0, frame.getWidth(), 30);
				//gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
				switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


				if (showingMore) {
					LevelsPanel.resizeButtons(frame.getWidth() - 415);
					LevelsPanel.getReqWindow().setBounds(0, 0, frame.getWidth() - 415, frame.getHeight() - 152);
					CommentsPanel.getComWindow().setBounds(frame.getWidth() - 415, 0, CommentsPanel.getComWindow().getWidth(), frame.getHeight() + 2);
					CommentsPanel.resetDimensions(CommentsPanel.getComWindow().getWidth(), frame.getHeight() + 2);
					InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
					InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
				} else {
					LevelsPanel.resizeButtons(frame.getWidth() - 115);
					LevelsPanel.getReqWindow().setBounds(0, 0, frame.getWidth() - 115, frame.getHeight() - 152);
					InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
					InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
				}
				refresh();
			}

			public void componentMoved(ComponentEvent evt) {
				GraphicsDevice[] screens = GraphicsEnvironment
						.getLocalGraphicsEnvironment()
						.getScreenDevices();
				int frameX = frame.getX();
				int frameY = frame.getY();
				Point mouse = new Point(frameX, frameY);
				for (GraphicsDevice screen : screens) {
					if (screen.getDefaultConfiguration().getBounds().contains(mouse)) {
						Defaults.screenNum = Integer.parseInt(screen.getIDstring().replaceAll("Display", "").replace("\\", ""));
					}
				}
			}
		});
		frame.setSize(width + 250, height + 32 + 230);
		frame.setMinimumSize(new Dimension(515, 630));
		frame.setLayout(null);
		mainFrame.setBounds(20, 20, width + 200, height + 32 + 200);
		mainFrame.setLayout(null);
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBackground(new Color(0, 0, 0));
		content.setBounds(40, 0, width - 2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);
		if (Settings.getSettings("window").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			frame.setLocation((int) Defaults.screenSize.getWidth() / 2 - width / 2, 200);
		}

		LevelsPanel.getReqWindow().setBounds(0, 0, LevelsPanel.getReqWindow().getWidth(), LevelsPanel.getReqWindow().getHeight());
		CommentsPanel.getComWindow().setBounds(400, 0, CommentsPanel.getComWindow().getWidth(), height);
		CommentsPanel.resetDimensions(CommentsPanel.getComWindow().getWidth(), height);

		CommentsPanel.getComWindow().setVisible(true);

		InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, InfoPanel.getInfoWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());

		sideButtons.setBackground(Defaults.TOP);


		buttonPanel.setBounds(width - 60, 0, 50, frame.getHeight() - 70);
		sideButtons.setBounds(0, -5, 40, frame.getHeight() - 15);
		iconPanel.setBounds(width - 60, frame.getHeight() - 95, 50, 50);

		buttonPanel.setBackground(Defaults.SUB_MAIN);
		JButton skip = createButton("\uEB9D", "$SKIP_LEVEL_TOOLTIP$");
		skip.addActionListener(e -> {

					//((InnerWindow) window).moveToFront();
					Functions.skipFunction();


		});
		buttonPanel.add(skip);

		JButton undo = createButton("\uE10E", "$UNDO_LEVEL_TOOLTIP$");
		undo.addActionListener(e -> {
			Functions.undoFunction();
		});
		buttonPanel.add(undo);

		//region Create Random Button

		JButton randNext = createButton("\uF158", "$NEXT_RANDOM_TOOLTIP$");
		randNext.addActionListener(e -> {
					//((InnerWindow) window).moveToFront();
					Functions.randomFunction();


		});
		buttonPanel.add(randNext);
		//endregion

		//region Create Copy Button
		JButton copy = createButton("\uF0E3", "$CLIPBOARD_TOOLTIP$");
		copy.addActionListener(e -> {
					//((InnerWindow) window).moveToFront();
					Functions.copyFunction();


		});
		buttonPanel.add(copy);
		//endregion

		//region Create Block Button
		JButton block = createButton("\uF140", "$BLOCK_TOOLTIP$");
		block.addActionListener(e -> {
					//((InnerWindow) window).moveToFront();
					Functions.blockFunction();


		});
		buttonPanel.add(block);
		//endregion

		//region Create Clear Button
		JButton clear = createButton("\uE107", "$CLEAR_TOOLTIP$");
		clear.addActionListener(e -> {
					SettingsWindow.run = false;
					//((InnerWindow) window).moveToFront();
					Functions.clearFunction();

		});
		buttonPanel.add(clear);
		JButton toggleRequests = createButton("\uE71A", "$TOGGLE_REQUESTS_TOOLTIP$");
		toggleRequests.addActionListener(e -> {
					SettingsWindow.run = false;
					//((InnerWindow) window).moveToFront();
					Functions.requestsToggleFunction();
					if(Requests.enableRequests){
						toggleRequests.setText("\uE71A");
					}
					else{
						toggleRequests.setText("\uE102");
					}


		});
		buttonPanel.add(toggleRequests);

		showComments = createButton("\uE134", "$HIDE_COMMENTS_TOOLTIP$");
		showComments.addActionListener(e -> {

					showingMore = !showingMore;
					toolBar.setBounds(0, 0, frame.getWidth(), 30);
					//gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
					switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


					if (!showingMore) {
						CommentsPanel.unloadComments(true);
						((RoundedJButton) showComments).setTooltip("$SHOW_COMMENTS_TOOLTIP$");
						frame.setMinimumSize(new Dimension(515, 630));
						CommentsPanel.getComWindow().setVisible(false);
						LevelsPanel.resizeButtons(frame.getWidth() - 115);
						LevelsPanel.getReqWindow().setBounds(0, 0, frame.getWidth() - 115, frame.getHeight() - 152);
						InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
						InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
						refresh();
					} else {
						((RoundedJButton) showComments).setTooltip("$HIDE_COMMENTS_TOOLTIP$");
						if (frame.getWidth() < 765) {
							frame.setSize(frame.getWidth() + 300, frame.getHeight());
						}
						frame.setMinimumSize(new Dimension(815, 630));
						CommentsPanel.loadComments(0, false);
						CommentsPanel.getComWindow().setVisible(true);
						CommentsPanel.getComWindow().setBounds(frame.getWidth() - 415, 0, CommentsPanel.getComWindow().getWidth(), frame.getHeight() + 2);
						CommentsPanel.resetDimensions(CommentsPanel.getComWindow().getWidth(), frame.getHeight() + 2);
						LevelsPanel.resizeButtons(frame.getWidth() - 415);
						LevelsPanel.getReqWindow().setBounds(0, 0, frame.getWidth() - 415, frame.getHeight() - 152);
						InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
						InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
						refresh();
					}


		});
		buttonPanel.add(showComments);
		JFrame moderationFrame = new JFrame();
		moderationFrame.setLayout(null);
		moderationFrame.setTitle("GDBoard - Moderation");
		moderationFrame.setIconImage(newIcon);
		moderationFrame.setSize(500,300);
		moderationFrame.setResizable(false);

		infoPanel.setBackground(Defaults.MAIN);
		message.setEditable(false);
		message.setBounds(6,55,471, 130);
		message.setLineWrap(true);
		message.setWrapStyleWord(true);
		JLabel username = new JLabel();
		username.setForeground(Defaults.FOREGROUND);
		username.setBounds(7,5,473, 40);
		username.setFont(Defaults.SEGOE.deriveFont(24f));
		JLabel levelName = new JLabel();
		levelName.setForeground(Defaults.FOREGROUND);
		levelName.setBounds(473,0,473, 40);
		levelName.setFont(Defaults.SEGOE.deriveFont(14f));

		JLabel levelID = new JLabel();
		levelID.setForeground(Defaults.FOREGROUND);
		levelID.setBounds(473,16,473, 40);
		levelID.setFont(Defaults.SEGOE.deriveFont(14f));

		infoPanel.add(levelName);
		infoPanel.add(levelID);
		infoPanel.add(username);
		infoPanel.add(message);

		modButtons.setBackground(Defaults.TOP);

		CurvedButtonAlt delete = createCurvedButton("$DELETE$");
		delete.addActionListener(e -> {
				Main.sendMessage("/delete " + Requests.levels.get(selectedID).getMessageID());

		});

		CurvedButtonAlt timeout = createCurvedButton("$TIMEOUT$");
		timeout.addActionListener(e -> {
				Main.sendMessage("/timeout " + selectedUsername + " 600");

		});

		CurvedButtonAlt ban = createCurvedButton("$BAN$");
		ban.addActionListener(e -> {Main.sendMessage("/ban " + selectedUsername);

		});

		CurvedButtonAlt purge = createCurvedButton("$PURGE$");
		purge.addActionListener(e -> {
				Main.sendMessage("/timeout " + selectedUsername + " 1");

		});
		modButtons.add(delete);
		modButtons.add(purge);
		modButtons.add(timeout);
		modButtons.add(ban);

		infoPanel.setBounds(0,0,486, 195);
		modButtons.setBounds(0,195,486, 105);

		moderationFrame.add(infoPanel);
		moderationFrame.add(modButtons);


		JButton moderate = createButton("\uED15", "$MODERATE_TOOLTIP$");
		moderate.addActionListener(e -> {
					if(Requests.levels.size() != 0) {
						selectedUsername = String.valueOf(Requests.levels.get(LevelsPanel.getSelectedID()).getRequester());
						selectedID = LevelsPanel.getSelectedID();
						username.setText(String.valueOf(Requests.levels.get(LevelsPanel.getSelectedID()).getRequester()));
						levelName.setText(String.valueOf(Requests.levels.get(LevelsPanel.getSelectedID()).getName()));
						levelName.setBounds(473 - levelName.getPreferredSize().width, 0, 473, 40);
						levelID.setText("(" + Requests.levels.get(LevelsPanel.getSelectedID()).getLevelID() + ")");
						levelID.setBounds(473 - levelID.getPreferredSize().width, 16, 473, 40);
						message.setText(Requests.levels.get(LevelsPanel.getSelectedID()).getMessage());
						message.clearUndo();
						moderationFrame.setVisible(true);
						moderationFrame.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - moderationFrame.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - moderationFrame.getHeight() / 2);
					}


		});
		buttonPanel.add(moderate);

		toolBar.setBounds(0, 0, width, 30);
		toolBar.setBackground(Defaults.TOP);
		//gdToggle.setOpaque(false);
		//gdToggle.setBackground(new Color(0, 0, 0, 0));
		//gdToggle.setForeground(Defaults.FOREGROUND);
		//gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
		//gdToggle.setPreferredSize(new Dimension(80, 30));
		//gdToggle.setFont(Defaults.SEGOE.deriveFont(14f));
		//gdToggle.setChecked(true);
		//gdToggle.addMouseListener(new MouseAdapter() {
		//	@Override
		//	public void mouseReleased(MouseEvent e) {
		//		isGD = gdToggle.getSelectedState();
		//		if (isGD) {
		//			switchButton.setVisible(true);
		//		} else {
		//			switchButton.setVisible(false);
		//			gdPage = false;
		//			content.setVisible(false);
		//			actionsPanel.setVisible(true);
		//		}
		//	}
		//});
		//gdToggle.refresh();


		buttons.setBackground(Defaults.TOP);
		buttons.setBounds(0, 0, 400, 30);

		ImageIcon settingsIcon = Assets.settings;

		HighlightButton settingsA = new HighlightButton(settingsIcon.getImage());
		settingsA.addActionListener(e -> {
					SettingsWindow.run = false;
					if (!SettingsWindow.frame.isVisible()) {
						SettingsWindow.frame.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - SettingsWindow.frame.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - SettingsWindow.frame.getHeight() / 2);
						SettingsWindow.frame.setVisible(true);
					} else {
						SettingsWindow.frame.toFront();
					}
					if (SettingsWindow.frame.getExtendedState() == JFrame.ICONIFIED) {
						SettingsWindow.frame.setExtendedState(JFrame.NORMAL);


			}
		});
		ImageIcon channelPointsIcon = Assets.channelPoints;

		HighlightButton channelPoints = new HighlightButton(channelPointsIcon.getImage());
		channelPoints.addActionListener(e -> {
					SettingsWindow.run = false;
					if (!SettingsWindow.frame.isVisible()) {
						SettingsWindow.frame.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - SettingsWindow.frame.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - SettingsWindow.frame.getHeight() / 2);
						SettingsWindow.openPage("chatbot");
						SettingsWindow.click("CHANNEL_POINTS_SETTINGS");
						SettingsWindow.frame.setVisible(true);
					} else {
						SettingsWindow.openPage("chatbot");
						SettingsWindow.click("CHANNEL_POINTS_SETTINGS");
						SettingsWindow.frame.toFront();
					}
					if (SettingsWindow.frame.getExtendedState() == JFrame.ICONIFIED) {
						SettingsWindow.frame.setExtendedState(JFrame.NORMAL);
					}


		});

		ImageIcon commandsIcon = Assets.commands;

		HighlightButton commands = new HighlightButton(commandsIcon.getImage());
		commands.addActionListener(e -> {
					SettingsWindow.run = false;
					if (!SettingsWindow.frame.isVisible()) {
						SettingsWindow.frame.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - SettingsWindow.frame.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - SettingsWindow.frame.getHeight() / 2);
						SettingsWindow.openPage("chatbot");
						SettingsWindow.click("COMMANDS_SETTINGS");
						SettingsWindow.frame.setVisible(true);
					} else {
						SettingsWindow.openPage("chatbot");
						SettingsWindow.click("COMMANDS_SETTINGS");
						SettingsWindow.frame.toFront();
					}
					if (SettingsWindow.frame.getExtendedState() == JFrame.ICONIFIED) {
						SettingsWindow.frame.setExtendedState(JFrame.NORMAL);
					}


		});

		ImageIcon requestsIcon = Assets.requests;


		HighlightButton requests = new HighlightButton(requestsIcon.getImage());
		requests.addActionListener(e -> {
					SettingsWindow.run = false;
					if (!SettingsWindow.frame.isVisible()) {
						SettingsWindow.frame.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - SettingsWindow.frame.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - SettingsWindow.frame.getHeight() / 2);
						SettingsWindow.openPage("gd");
						SettingsWindow.click("GENERAL_SETTINGS");
						SettingsWindow.frame.setVisible(true);
					} else {
						SettingsWindow.openPage("gd");
						SettingsWindow.click("GENERAL_SETTINGS");
						SettingsWindow.frame.toFront();
					}
					if (SettingsWindow.frame.getExtendedState() == JFrame.ICONIFIED) {
						SettingsWindow.frame.setExtendedState(JFrame.NORMAL);
					}


		});

		ImageIcon donateIcon = Assets.donate;

		HighlightButton donateA = new HighlightButton(donateIcon.getImage());
		donateA.addActionListener(e -> {
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Runtime rt = Runtime.getRuntime();
							rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.paypal.me/xAlphalaneous");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}


		});
		ImageIcon discordIcon = Assets.discord;

		HighlightButton discordA = new HighlightButton(discordIcon.getImage());

		discordA.addActionListener(e -> {
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Runtime rt = Runtime.getRuntime();
							rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://discord.gg/x2awccH");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}

		});


		sideButtons.add(settingsA);
		sideButtons.add(commands);
		sideButtons.add(channelPoints);
		sideButtons.add(requests);
		sideButtons.add(donateA);
		sideButtons.add(discordA);

		switchButton.setFont(Defaults.SYMBOLS.deriveFont(14f));
		switchButton.setUI(defaultUI);
		switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);
		switchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				gdPage = !gdPage;
				if (gdPage) {
					content.setVisible(true);
					actionsPanel.setVisible(false);

				} else {
					content.setVisible(false);
					actionsPanel.setVisible(true);
				}
			}
		});

		//toolBar.add(gdToggle);
		toolBar.add(buttons);
		//toolBar.add(switchButton);

		actionsPanel.setBackground(Defaults.MAIN);
		actionsPanel.setBounds(0, 30, width - 8, height);
		actionsPanel.setVisible(false);
		Color[] colors = new Color[]{new Color(189, 11, 0), new Color(35, 183, 0), new Color(221, 148, 0), new Color(0, 14, 191), new Color(0, 145, 131), new Color(212, 0, 214)};
		Random random = new Random();

		Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/actions/");
		if (Files.exists(comPath)) {
			Stream<Path> walk1 = null;
			try {
				walk1 = Files.walk(comPath, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			assert walk1 != null;
			for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file = path.toString().split("\\\\");
				String fileName = file[file.length - 1];
				if (fileName.endsWith(".js")) {
					JButton action = new CurvedButtonAlt(fileName.substring(0, fileName.length() - 3));
					int rand = random.nextInt(6);

					action.setBackground(colors[rand]);
					action.setFont(Defaults.SEGOE.deriveFont(14f));
					JButtonUI separateUI = new JButtonUI();
					separateUI.setBackground(colors[rand]);
					separateUI.setHover(colors[rand].brighter());
					separateUI.setSelect(colors[rand]);

					action.setUI(separateUI);
					action.setForeground(Defaults.FOREGROUND);
					action.setBorder(BorderFactory.createEmptyBorder());
					action.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							new Thread(() -> {

								try {
									String response = Command.run(Files.readString(path, StandardCharsets.UTF_8), false);
									if (!response.equalsIgnoreCase("")) {
										Main.sendMessage(response);
									}
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}).start();
						}
					});
					actionsPanel.add(action);
				}
			}
		}
		iconPanel.setBackground(new Color(0, 0, 0, 0));
		iconPanel.setOpaque(false);

		attributions.setTitle("GDBoard - Attributions");
		attributions.setResizable(false);
		attributions.setSize(new Dimension(400, 280));
		attributions.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		attributions.getContentPane().setBackground(Defaults.MAIN);
		attributions.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				attributions.setVisible(false);
			}
		});
		attributions.setLayout(null);


		JLabel GDBoardIcon = new JLabel(Assets.GDBoard);
		GDBoardIcon.setBounds(0, 0, 50, 50);
		GDBoardIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel Alphalaneous = new JLabel(new ImageIcon(makeRoundedCorner(convertToBufferedImage(Assets.Alphalaneous.getImage()))));
		Alphalaneous.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		Alphalaneous.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Utilities.openLink("https://twitter.com/alphalaneous");
			}
		});

		AlphalaneousText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		AlphalaneousText.setForeground(Defaults.FOREGROUND);
		AlphalaneousText.setFont(Defaults.SEGOE.deriveFont(30f));
		AlphalaneousText.setBounds(130, 32, 300, 40);
		AlphalaneousText.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Utilities.openLink("https://twitter.com/alphalaneous");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Font originalFont = AlphalaneousText.getFont();
				Map attributes = originalFont.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				AlphalaneousText.setFont(originalFont.deriveFont(attributes));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				AlphalaneousText.setFont(Defaults.SEGOE.deriveFont(30f));
			}
		});

		JLabel AlphalaneousSubtext = new JLabel("Client Development");
		AlphalaneousSubtext.setForeground(Defaults.FOREGROUND2);
		AlphalaneousSubtext.setFont(Defaults.SEGOE.deriveFont(15f));
		AlphalaneousSubtext.setBounds(130, 72, 300, 40);

		JLabel EncodedLua = new JLabel(new ImageIcon(makeRoundedCorner(convertToBufferedImage(Assets.EncodedLua.getImage()))));
		EncodedLua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		EncodedLua.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Utilities.openLink("https://twitter.com/EncodedLua");
			}
		});

		EncodedLuaText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		EncodedLuaText.setForeground(Defaults.FOREGROUND);
		EncodedLuaText.setFont(Defaults.SEGOE.deriveFont(30f));
		EncodedLuaText.setBounds(130, 122, 300, 40);

		EncodedLuaText.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Utilities.openLink("https://twitter.com/EncodedLua");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Font originalFont = EncodedLuaText.getFont();
				Map attributes = originalFont.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				EncodedLuaText.setFont(originalFont.deriveFont(attributes));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				EncodedLuaText.setFont(Defaults.SEGOE.deriveFont(30f));
			}
		});

		JLabel EncodedLuaSubtext = new JLabel("Server Development");
		EncodedLuaSubtext.setForeground(Defaults.FOREGROUND2);
		EncodedLuaSubtext.setFont(Defaults.SEGOE.deriveFont(15f));
		EncodedLuaSubtext.setBounds(130, 162, 300, 40);

		Alphalaneous.setBounds(30, 30, 80, 80);
		EncodedLua.setBounds(30, 120, 80, 80);

		attributions.setIconImage(newIcon);
		attributions.add(Alphalaneous);
		attributions.add(AlphalaneousText);
		attributions.add(AlphalaneousSubtext);
		attributions.add(EncodedLua);
		attributions.add(EncodedLuaText);
		attributions.add(EncodedLuaSubtext);

		GDBoardIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				attributions.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - attributions.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - attributions.getHeight() / 2);

				attributions.setVisible(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				GDBoardIcon.setIcon(new ImageIcon(Assets.GDBoard.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				GDBoardIcon.setIcon(Assets.GDBoard);
			}
		});
		iconPanel.add(GDBoardIcon);

		content.add(LevelsPanel.getReqWindow());
		content.add(CommentsPanel.getComWindow());
		content.add(InfoPanel.getInfoWindow());
		content.add(iconPanel);
		content.add(buttonPanel);
		frame.getContentPane().add(sideButtons);
		frame.getContentPane().add(content);
		frame.getContentPane().add(actionsPanel);
	}

	public static void refreshUI() {
		AlphalaneousText.setForeground(Defaults.FOREGROUND);
		EncodedLuaText.setForeground(Defaults.FOREGROUND);
		attributions.getContentPane().setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		buttonUI.setBackground(Defaults.TOP);
		buttonUI.setHover(Defaults.BUTTON_HOVER);
		buttonUI.setSelect(Defaults.SELECT);
		sideButtons.setBackground(Defaults.TOP);
		content.setBackground(Defaults.SUB_MAIN);
		actionsPanel.setBackground(Defaults.MAIN);
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		buttons.setBackground(Defaults.TOP);
		toolBar.setBackground(Defaults.TOP);
		//gdToggle.setForeground(Defaults.FOREGROUND);
		//gdToggle.refresh();
		infoPanel.setBackground(Defaults.MAIN);
		message.refreshAll();
		modButtons.setBackground(Defaults.TOP);
		switchButton.setBackground(Defaults.MAIN);
		switchButton.setForeground(Defaults.FOREGROUND);
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof JButton) {
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					component.setBackground(Defaults.BUTTON);
				} else {
					component.setBackground(Defaults.MAIN);
				}
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		for (Component component : sideButtons.getComponents()) {
			if (component instanceof HighlightButton) {
				((HighlightButton) component).refresh();
			}
		}
		for (Component component : modButtons.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.MAIN);
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		for (Component component : infoPanel.getComponents()) {
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}

	}
	public static void focus(){
		frame.setAlwaysOnTop(true);
		frame.setAlwaysOnTop(PersonalizationSettings.onTopOption);
	}

	public static void resetCommentSize() {
		CommentsPanel.getComWindow().setBounds(400, 0, CommentsPanel.getComWindow().getWidth(), 600);

	}

	private static LangButton createButton(String text) {
		LangButton button = new LangButton(text);
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setBackground(Defaults.TOP);
		button.setUI(buttonUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setOpaque(true);
		button.setPreferredSize(new Dimension(button.getPreferredSize().width + 14, 30));
		return button;
	}

	private static CurvedButtonAlt createCurvedButton(String text) {
		CurvedButtonAlt button = new CurvedButtonAlt(text);
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setOpaque(true);
		button.setPreferredSize(new Dimension(button.getPreferredSize().width+10, 50));
		return button;
	}

	private static RoundedJButton createButton(String icon, String tooltip) {
		RoundedJButton button = new RoundedJButton(icon, tooltip);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			button.setBackground(Defaults.BUTTON);
		} else {
			button.setBackground(Defaults.MAIN);
		}
		button.setOpaque(false);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		return button;
	}

	//region SetLocation
	public static void setLocation(Point point) {
		frame.setLocation(point);
	}

	//endregion
	//region SetSettings
	public static void setSettings() {
		Settings.setWindowSettings("Window", frame.getX() + "," + frame.getY() + "," + false + "," + true);
			Settings.writeSettings("windowState", String.valueOf(frame.getExtendedState()));
			Settings.writeSettings("windowSize", frame.getWidth() + "," + frame.getHeight());
			Settings.writeSettings("showMore", String.valueOf(showingMore));


	}
	public static void showAttributions(){
		attributions.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - attributions.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - attributions.getHeight() / 2);
		attributions.setVisible(true);
	}
	public static void loadSettings() {
		if (!Settings.getSettings("windowState").equalsIgnoreCase("")) {
			int windowState = Integer.parseInt(Settings.getSettings("windowState"));
			frame.setExtendedState(windowState);
		}

		if (!Settings.getSettings("windowSize").equalsIgnoreCase("")) {
			String[] dim = Settings.getSettings("windowSize").split(",");
			int newW = Integer.parseInt(dim[0]);
			int newH = Integer.parseInt(dim[1]);
			width = newW;
			height = newH;
			frame.setSize(newW, newH);
			content.setBounds(40, 0, newW - 10, newH - 8);
			actionsPanel.setBounds(30, 0, newW - 16, newH - 8);
			buttonPanel.setBounds(newW - 60, 0, 50, frame.getHeight() - 70);
			sideButtons.setBounds(0, -5, 40, frame.getHeight() - 15);
			iconPanel.setBounds(newW - 60, frame.getHeight() - 95, 50, 50);

			toolBar.setBounds(0, 0, frame.getWidth(), 30);
			//gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
			switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


			LevelsPanel.resizeButtons(newW - 415);
			LevelsPanel.getReqWindow().setBounds(0, 0, newW - 415, newH - 152);
			CommentsPanel.getComWindow().setBounds(newW - 415, 0, CommentsPanel.getComWindow().getWidth(), newH + 2);
			CommentsPanel.resetDimensions(CommentsPanel.getComWindow().getWidth(), newH + 2);
			InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
			InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
			refresh();
		}
		else{
			int newW = 465;
			int newH = 630;
			width = newW;
			height = newH;
			frame.setSize(newW, newH);
			content.setBounds(40, 0, newW - 10, newH - 8);
			actionsPanel.setBounds(30, 30, newW - 16, newH - 8);
			buttonPanel.setBounds(newW - 110, 0, 50, frame.getHeight() - 70);
			sideButtons.setBounds(0, -5, 40, frame.getHeight() - 15);
			iconPanel.setBounds(newW - 110, frame.getHeight() - 95, 50, 50);

			toolBar.setBounds(0, 0, frame.getWidth(), 30);
			//gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
			switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


			LevelsPanel.resizeButtons(newW - 415);
			LevelsPanel.getReqWindow().setBounds(0, 0, newW - 415, newH - 152);
			CommentsPanel.getComWindow().setBounds(newW - 415, 0, CommentsPanel.getComWindow().getWidth(), newH + 2);
			CommentsPanel.resetDimensions(CommentsPanel.getComWindow().getWidth(), newH + 2);
			InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
			InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
			refresh();
		}
		if (!Settings.getSettings("showMore").equalsIgnoreCase("")) {
			if (!Settings.getSettings("showMore").equalsIgnoreCase("true")) {
				CommentsPanel.unloadComments(true);
				((RoundedJButton) showComments).setTooltip("$SHOW_COMMENTS_TOOLTIP$");
				showingMore = false;
				CommentsPanel.getComWindow().setVisible(false);
				LevelsPanel.resizeButtons(frame.getWidth() - 115);
				LevelsPanel.getReqWindow().setBounds(0, 0, frame.getWidth() - 115, frame.getHeight() - 182);
				InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
				InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
				refresh();
			} else {
				frame.setMinimumSize(new Dimension(815, 630));
			}
		} else {
			CommentsPanel.unloadComments(true);
			((RoundedJButton) showComments).setTooltip("$SHOW_COMMENTS_TOOLTIP$");
			showingMore = false;
			CommentsPanel.getComWindow().setVisible(false);
			LevelsPanel.resizeButtons(frame.getWidth() - 115);
			LevelsPanel.getReqWindow().setBounds(0, 0, frame.getWidth() - 115, frame.getHeight() - 182);
			InfoPanel.resetDimensions(LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
			InfoPanel.getInfoWindow().setBounds(0, LevelsPanel.getReqWindow().getHeight() + 1, LevelsPanel.getReqWindow().getWidth(), InfoPanel.getInfoWindow().getHeight());
			refresh();
		}
	}

	private static BufferedImage makeRoundedCorner(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new Ellipse2D.Double(0, 0, 80.0, 80.0));
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}

	public static BufferedImage convertToBufferedImage(Image image) {
		BufferedImage newImage = new BufferedImage(
				image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
}
