package Main;

import Main.InnerWindows.CommentsWindow;
import Main.InnerWindows.InfoWindow;
import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.WindowedSettings;

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

import static Main.Defaults.defaultUI;

public class Windowed {
	private static int width = 465;
	private static int height = 630;
	public static boolean isGD = true;
	public static CheckboxButton gdToggle = new CheckboxButton("$GD_MODE$");
	private static JPanel content = new JPanel(null);
	private static JPanel actionsPanel = new JPanel(new GridLayout(10, 1, 10, 10));
	private static RoundedJButton switchButton = new RoundedJButton("\uF1CB", "$SWITCH_PAGES$");
	private static boolean gdPage = true;
	private static JPanel buttonPanel = new JPanel();
	private static JPanel iconPanel = new JPanel(null);
	private static JDialog attributions = new JDialog();
	private static JButtonUI selectUI = new JButtonUI();
	public static JPanel toolBar = new JPanel(null);
	public static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	public static JFrame frame = new JFrame();
	private static JLayeredPane mainFrame = new JLayeredPane();
	public static boolean showingMore = true;
	private static JButtonUI buttonUI = new JButtonUI();
	private static JButton showComments;
	private static JLabel AlphalaneousText = new JLabel("Alphalaneous");
	private static JLabel EncodedLuaText = new JLabel("EncodedLua");


	public static void refresh() {
		frame.invalidate();
		frame.validate();
	}

	public static void setOnTop(boolean onTop) {
		frame.setAlwaysOnTop(onTop);
		frame.setFocusableWindowState(!onTop);
	}

	public static void createPanel() {
		setOnTop(WindowedSettings.onTopOption);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.close();
			}
		});
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
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
				Component c = (Component) evt.getSource();
				content.setBounds(0, 30, frame.getWidth() - 10, frame.getHeight() - 68);
				actionsPanel.setBounds(0, 30, frame.getWidth() - 16, frame.getHeight() - 68);

				//window.setBounds(-5,-35,frame.getWidth(), frame.getHeight());
				//((InnerWindow) window).resetDimensions(frame.getWidth()-10, frame.getHeight());
				buttonPanel.setBounds(frame.getWidth() - 70, 0, 50, frame.getHeight() - 200);
				iconPanel.setBounds(frame.getWidth() - 70, frame.getHeight() - 125, 50, 50);
				toolBar.setBounds(0, 0, frame.getWidth(), 30);
				gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
				switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


				if (showingMore) {
					LevelsWindow.resizeButtons(frame.getWidth() - 375, frame.getHeight() - 182);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 375, frame.getHeight() - 182);
					CommentsWindow.getComWindow().setBounds(frame.getWidth() - 375, 0, CommentsWindow.getComWindow().getWidth(), frame.getHeight() - 30 + 2);
					CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), frame.getHeight() - 30 + 2);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				} else {
					LevelsWindow.resizeButtons(frame.getWidth() - 75, frame.getHeight() - 182);
					LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 75, frame.getHeight() - 182);
					InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
					InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
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
		frame.setSize(width + 200, height + 32 + 230);
		frame.setMinimumSize(new Dimension(465, 630));
		frame.setLayout(null);
		mainFrame.setBounds(20, 20, width + 200, height + 32 + 200);
		mainFrame.setLayout(null);
		mainFrame.setDoubleBuffered(true);
		mainFrame.setBackground(new Color(0, 0, 0));
		content.setBounds(0, 30, width - 2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);
		if (Settings.getSettings("window").equalsIgnoreCase("") && Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			frame.setLocation((int) Defaults.screenSize.getWidth() / 2 - width / 2, 200);
		}

		LevelsWindow.getReqWindow().setBounds(0, 0, LevelsWindow.getReqWindow().getWidth(), LevelsWindow.getReqWindow().getHeight());
		CommentsWindow.getComWindow().setBounds(400, 0, CommentsWindow.getComWindow().getWidth(), height);
		CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), height);

		CommentsWindow.getComWindow().setVisible(true);

		InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, InfoWindow.getInfoWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());

		buttonPanel.setBounds(width - 60, 0, 50, frame.getHeight() - 200);
		iconPanel.setBounds(width - 60, frame.getHeight() - 125, 50, 50);

		buttonPanel.setBackground(Defaults.SUB_MAIN);
		JButton skip = createButton("\uEB9D", "$SKIP_LEVEL_TOOLTIP$");
		skip.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					//((InnerWindow) window).moveToFront();
					Functions.skipFunction();
				}
			}
		});
		buttonPanel.add(skip);
		//endregion

		//region Create Random Button

		JButton randNext = createButton("\uF158", "$NEXT_RANDOM_TOOLTIP$");
		randNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					//((InnerWindow) window).moveToFront();
					Functions.randomFunction();
				}

			}
		});
		buttonPanel.add(randNext);
		//endregion

		//region Create Copy Button
		JButton copy = createButton("\uF0E3", "$CLIPBOARD_TOOLTIP$");
		copy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					//((InnerWindow) window).moveToFront();
					Functions.copyFunction();
				}
			}
		});
		buttonPanel.add(copy);
		//endregion

		//region Create Block Button
		JButton block = createButton("\uF140", "$BLOCK_TOOLTIP$");
		block.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					//((InnerWindow) window).moveToFront();
					Functions.blockFunction();
				}
			}
		});
		buttonPanel.add(block);
		//endregion

		//region Create Clear Button
		JButton clear = createButton("\uE107", "$CLEAR_TOOLTIP$");
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					SettingsWindow.run = false;
					//((InnerWindow) window).moveToFront();
					Functions.clearFunction();
				}
			}
		});
		buttonPanel.add(clear);
		JButton toggleRequests = createButton("\uE71A", "$TOGGLE_REQUESTS_TOOLTIP$");
		toggleRequests.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					SettingsWindow.run = false;
					//((InnerWindow) window).moveToFront();
					Functions.requestsToggleFunction();
					toggleRequests.setText(MainBar.stopReqs.getText());
				}
			}
		});
		buttonPanel.add(toggleRequests);

		showComments = createButton("\uE134", "$HIDE_COMMENTS_TOOLTIP$");
		showComments.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					showingMore = !showingMore;
					toolBar.setBounds(0, 0, frame.getWidth(), 30);
					gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
					switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


					if (!showingMore) {
						CommentsWindow.unloadComments(true);
						((RoundedJButton) showComments).setTooltip("$SHOW_COMMENTS_TOOLTIP$");
						frame.setMinimumSize(new Dimension(465, 630));
						CommentsWindow.getComWindow().setVisible(false);
						LevelsWindow.resizeButtons(frame.getWidth() - 75, frame.getHeight() - 182);
						LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 75, frame.getHeight() - 182);
						InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
						InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
						refresh();
					} else {
						((RoundedJButton) showComments).setTooltip("$HIDE_COMMENTS_TOOLTIP$");
						if (frame.getWidth() < 765) {
							frame.setSize(frame.getWidth() + 300, frame.getHeight());
						}
						frame.setMinimumSize(new Dimension(765, 630));
						CommentsWindow.loadComments(0, false);
						CommentsWindow.getComWindow().setVisible(true);
						CommentsWindow.getComWindow().setBounds(frame.getWidth() - 375, 0, CommentsWindow.getComWindow().getWidth(), frame.getHeight() - 20 + 2);
						CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), frame.getHeight() - 30 + 2);
						LevelsWindow.resizeButtons(frame.getWidth() - 375, frame.getHeight() - 182);
						LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 375, frame.getHeight() - 182);
						InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
						InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
						refresh();
					}
				}
			}
		});
		buttonPanel.add(showComments);


		toolBar.setBounds(0, 0, width, 30);
		toolBar.setBackground(Defaults.TOP);
		gdToggle.setOpaque(false);
		gdToggle.setBackground(new Color(0, 0, 0, 0));
		gdToggle.setForeground(Defaults.FOREGROUND);
		gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
		gdToggle.setPreferredSize(new Dimension(80, 30));
		gdToggle.setFont(Defaults.SEGOE.deriveFont(14f));
		gdToggle.setChecked(true);
		gdToggle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				isGD = gdToggle.getSelectedState();
				if (isGD) {
					switchButton.setVisible(true);
				} else {
					switchButton.setVisible(false);
					gdPage = false;
					content.setVisible(false);
					actionsPanel.setVisible(true);
				}
			}
		});
		gdToggle.refresh();


		buttons.setBackground(Defaults.TOP);
		buttons.setBounds(0, 0, 400, 30);

		JButton settingsA = createButton("$SETTINGS_BUTTON$");
		settingsA.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					SettingsWindow.run = false;
					if (!SettingsWindow.frame.isVisible()) {
						SettingsWindow.frame.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - SettingsWindow.frame.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - SettingsWindow.frame.getHeight() / 2);
						SettingsWindow.toggleVisible();
					} else {
						SettingsWindow.frame.toFront();
					}
					if (SettingsWindow.frame.getExtendedState() == JFrame.ICONIFIED) {
						SettingsWindow.frame.setExtendedState(JFrame.NORMAL);
					}
				}
			}
		});

		JButton donateA = createButton("$DONATE_BUTTON$");
		donateA.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Runtime rt = Runtime.getRuntime();
							rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://www.paypal.me/xAlphalaneous");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});


		JButton discordA = createButton("$DISCORD_BUTTON$");
		discordA.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						try {
							Runtime rt = Runtime.getRuntime();
							rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://discord.gg/x2awccH");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});


		buttons.add(settingsA);
		buttons.add(donateA);
		buttons.add(discordA);

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
			int prevRand = 0;
			for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file = path.toString().split("\\\\");
				String fileName = file[file.length - 1];
				if (fileName.endsWith(".js")) {
					JButton action = new CurvedButtonAlt(fileName.substring(0, fileName.length() - 3));
					int rand = random.nextInt(6);

					prevRand = rand;
					action.setBackground(colors[rand]);
					action.setFont(Defaults.MAIN_FONT.deriveFont(14f));
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
									if (!response.equalsIgnoreCase("") && response != null) {
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

		JLabel Alphalaneous = new JLabel(new ImageIcon(makeRoundedCorner(convertToBufferedImage(Assets.Alphalaneous.getImage()), 0)));
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

		JLabel EncodedLua = new JLabel(new ImageIcon(makeRoundedCorner(convertToBufferedImage(Assets.EncodedLua.getImage()), 0)));
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

		content.add(LevelsWindow.getReqWindow());
		content.add(CommentsWindow.getComWindow());
		content.add(InfoWindow.getInfoWindow());
		content.add(iconPanel);
		content.add(buttonPanel);
		frame.getContentPane().add(toolBar);
		frame.getContentPane().add(content);
		frame.getContentPane().add(actionsPanel);
	}

	public static void destroyWindow() {
		frame.setVisible(false);
		frame.dispose();
	}

	static void refreshUI() {
		AlphalaneousText.setForeground(Defaults.FOREGROUND);
		EncodedLuaText.setForeground(Defaults.FOREGROUND);
		attributions.getContentPane().setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		selectUI.setSelect(Defaults.SELECT);
		buttonUI.setBackground(Defaults.TOP);
		buttonUI.setHover(Defaults.BUTTON_HOVER);
		buttonUI.setSelect(Defaults.SELECT);
		content.setBackground(Defaults.SUB_MAIN);
		actionsPanel.setBackground(Defaults.MAIN);
		buttonPanel.setBackground(Defaults.SUB_MAIN);
		buttons.setBackground(Defaults.TOP);
		toolBar.setBackground(Defaults.TOP);
		gdToggle.setForeground(Defaults.FOREGROUND);
		gdToggle.refresh();
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

	}

	public static void resetCommentSize() {
		CommentsWindow.getComWindow().setBounds(400, 0, CommentsWindow.getComWindow().getWidth(), 600);

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

	public static void refreshButtons() {
		for (Component component : buttons.getComponents()) {
			if (component instanceof LangButton) {

			}
		}
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
	static void setLocation(Point point) {
		frame.setLocation(point);
	}

	//endregion
	//region SetSettings
	public static void setSettings() {
		Settings.setWindowSettings("Window", frame.getX() + "," + frame.getY() + "," + false + "," + true);
		try {
			Settings.writeSettings("windowState", String.valueOf(frame.getExtendedState()));
			Settings.writeSettings("windowSize", frame.getWidth() + "," + frame.getHeight());
			Settings.writeSettings("showMore", String.valueOf(showingMore));
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			content.setBounds(0, 30, newW - 10, newH - 38);
			actionsPanel.setBounds(0, 30, newW - 16, newH - 38);
			buttonPanel.setBounds(newW - 70, 0, 50, frame.getHeight() - 200);
			iconPanel.setBounds(newW - 70, frame.getHeight() - 125, 50, 50);

			toolBar.setBounds(0, 0, frame.getWidth(), 30);
			gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
			switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


			LevelsWindow.resizeButtons(newW - 375, newH - 152);
			LevelsWindow.getReqWindow().setBounds(0, 0, newW - 375, newH - 152);
			CommentsWindow.getComWindow().setBounds(newW - 375, 0, CommentsWindow.getComWindow().getWidth(), newH + 2);
			CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), newH + 2);
			InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
			InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
			refresh();
		}
		else{
			int newW = 465;
			int newH = 630;
			width = newW;
			height = newH;
			frame.setSize(newW, newH);
			content.setBounds(0, 30, newW - 10, newH - 38);
			actionsPanel.setBounds(0, 30, newW - 16, newH - 38);
			buttonPanel.setBounds(newW - 70, 0, 50, frame.getHeight() - 200);
			iconPanel.setBounds(newW - 70, frame.getHeight() - 125, 50, 50);

			toolBar.setBounds(0, 0, frame.getWidth(), 30);
			gdToggle.setBounds(frame.getWidth() - 180, 0, 100, 30);
			switchButton.setBounds(frame.getWidth() - 50, 3, 25, 25);


			LevelsWindow.resizeButtons(newW - 375, newH - 152);
			LevelsWindow.getReqWindow().setBounds(0, 0, newW - 375, newH - 152);
			CommentsWindow.getComWindow().setBounds(newW - 375, 0, CommentsWindow.getComWindow().getWidth(), newH + 2);
			CommentsWindow.resetDimensions(CommentsWindow.getComWindow().getWidth(), newH + 2);
			InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
			InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
			refresh();
		}
		if (!Settings.getSettings("showMore").equalsIgnoreCase("")) {
			if (!Settings.getSettings("showMore").equalsIgnoreCase("true")) {
				CommentsWindow.unloadComments(true);
				((RoundedJButton) showComments).setTooltip("$SHOW_COMMENTS_TOOLTIP$");
				showingMore = false;
				CommentsWindow.getComWindow().setVisible(false);
				LevelsWindow.resizeButtons(frame.getWidth() - 75, frame.getHeight() - 182);
				LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 75, frame.getHeight() - 182);
				InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
				refresh();
			} else {
				frame.setMinimumSize(new Dimension(765, 630));
			}
		} else {
			CommentsWindow.unloadComments(true);
			((RoundedJButton) showComments).setTooltip("$SHOW_COMMENTS_TOOLTIP$");
			showingMore = false;
			CommentsWindow.getComWindow().setVisible(false);
			LevelsWindow.resizeButtons(frame.getWidth() - 75, frame.getHeight() - 182);
			LevelsWindow.getReqWindow().setBounds(0, 0, frame.getWidth() - 75, frame.getHeight() - 182);
			InfoWindow.resetDimensions(LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
			InfoWindow.getInfoWindow().setBounds(0, LevelsWindow.getReqWindow().getHeight() + 1, LevelsWindow.getReqWindow().getWidth(), InfoWindow.getInfoWindow().getHeight());
			refresh();
		}
	}

	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
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
