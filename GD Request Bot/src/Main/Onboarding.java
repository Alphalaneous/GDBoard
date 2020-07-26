package Main;

import Main.SettingsPanels.ShortcutSettings;
import org.jnativehook.keyboard.SwingKeyAdapter;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class Onboarding {
	static int width = 465;
	static int height = 512;
	private static JPanel window = new InnerWindow("Startup", 0, 0, width - 2, height,
			"\uF137", true).createPanel();
	private static JPanel content = new JPanel(null);
	private static JButtonUI defaultUI = new JButtonUI();
	public static int openKeybind = 36;
	static JFrame frame = new JFrame();

	static void createPanel() {
		URL iconURL = Windowed.class.getResource("/Resources/Icons/windowIcon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		Image newIcon = icon.getImage().getScaledInstance(120, 120,  Image.SCALE_SMOOTH);
		frame.setIconImage(newIcon);
		frame.setTitle("GDBoard Startup");
		Onboarding.setLocation(new Point(Defaults.screenSize.width / 2 - width / 2, Defaults.screenSize.height / 2 - height / 2));
		frame.setUndecorated(true);
		frame.setSize(width+5, 700 + 32);
		frame.setPreferredSize(new Dimension(width+5, 700 + 32));
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setBackground(new Color(255, 255, 255, 0));
		frame.getContentPane().setSize(width, 700 + 32);
		frame.pack();

		content.setBounds(5, 35, width - 2, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);

		JTextPane mainText = new JTextPane();
		StyledDocument doc = mainText.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		mainText.setText("Thank You for using GDBoard! Here are a few \nthings to get you started!");
		mainText.setBounds(25, 20, width - 50, mainText.getPreferredSize().height + 15);
		mainText.setOpaque(false);
		mainText.setEditable(false);
		mainText.setForeground(Defaults.FOREGROUND);
		mainText.setBackground(new Color(0, 0, 0, 0));
		mainText.setFont(Defaults.MAIN_FONT.deriveFont(18f));

		JTextPane infoText = new JTextPane();
		StyledDocument doc2 = infoText.getStyledDocument();
		SimpleAttributeSet center2 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
		doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);
		infoText.setText("Before we begin, make sure GDBoard is VIP or Mod in your chat! This will prevent it from getting caught up in Twitch's default chat limits.\n\nGDBoard has tons of settings to tailor requests just for you, but can also work with defaults, just press next, log in, and boom, it's ready to go!");
		infoText.setBounds(25, 100, width - 50, 300);
		infoText.setOpaque(false);
		infoText.setEditable(false);
		infoText.setForeground(Defaults.FOREGROUND);
		infoText.setBackground(new Color(0, 0, 0, 0));
		infoText.setFont(Defaults.MAIN_FONT.deriveFont(13f));

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		JLabel authInfo = new JLabel("Press Next to Log In with Twitch and start GDBoard!");
		authInfo.setFont(Defaults.MAIN_FONT.deriveFont(12f));
		authInfo.setBounds(25, height - 80, width - 50, authInfo.getPreferredSize().height + 5);
		authInfo.setForeground(Defaults.FOREGROUND);
		CurvedButton moveOn = new CurvedButton("Click here if Success and GDBoard hasn't moved on");
		moveOn.setBackground(Defaults.BUTTON);
		moveOn.setBounds(25, height - 140, width - 55, 30);
		moveOn.setPreferredSize(new Dimension(width - 55, 30));
		moveOn.setUI(defaultUI);
		moveOn.setForeground(Defaults.FOREGROUND);
		moveOn.setBorder(BorderFactory.createEmptyBorder());
		moveOn.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		moveOn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Onboarding.setInvisible();
				ShortcutSettings.loadKeybind("Open", openKeybind);
				try {
					Settings.writeSettings("openKeybind", String.valueOf(openKeybind));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Settings.writeSettings("onboarding", "false");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Main.programStarting = false;
			}
		});
		moveOn.refresh();
		moveOn.setVisible(false);
		CurvedButton button = new CurvedButton("Next");

		button.setBackground(Defaults.BUTTON);
		button.setBounds(25, height - 45, width - 55, 30);
		button.setPreferredSize(new Dimension(width - 55, 30));
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					//moveOn.setVisible(true);
					APIs.setOauth();
					Thread thread = new Thread(() -> {
						while (!APIs.success.get()) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						Onboarding.setInvisible();
						ShortcutSettings.loadKeybind("Open", openKeybind);
						try {
							Settings.writeSettings("openKeybind", String.valueOf(openKeybind));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						try {
							Settings.writeSettings("onboarding", "false");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						Main.programStarting = false;
					});
					thread.start();
				} catch (Exception ignored) {
				}
			}
		});
		button.refresh();

		content.add(mainText);
		content.add(authInfo);
		content.add(button);
		content.add(moveOn);
		content.add(infoText);
		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).refreshListener();
		frame.add(window);
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		content.setBackground(Defaults.SUB_MAIN);
	}

	static void toggleVisible() {

		((InnerWindow) window).toggle();
	}

	private static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();

	}

	@SuppressWarnings("unused")
	private static JButton createButton(String icon, String tooltip) {
		JButton button = new RoundedJButton(icon, tooltip);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		if (!Settings.windowedMode) {
			button.setBackground(Defaults.BUTTON);
		} else {
			button.setBackground(Defaults.MAIN);
		}
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(20f));
		return button;
	}

	//region SetLocation
	static void setLocation(Point point) {
		frame.setLocation(point);
	}

	/** @noinspection SameParameterValue*/ //endregion
	private static JPanel createKeybindButton(int y, String text, String setting) {
		JPanel panel = new JPanel(null);
		panel.setBounds(0, y, width, 36);
		panel.setBackground(Defaults.SUB_MAIN);
		FancyTextArea input = new FancyTextArea(false, false);
		DefaultStyledDocument doc = new DefaultStyledDocument();
		input.setEditable(false);
		input.addKeyListener(new SwingKeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 17 || e.getKeyCode() == 18) {
					if (text.equalsIgnoreCase("Open Keybind")) {
						input.setText("Home");
						try {
							Settings.writeSettings(setting, String.valueOf(36));
							ShortcutSettings.loadKeybind(text.split(" ")[0], 36);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						openKeybind = 36;
					}
				} else {
					input.setText(KeyEvent.getKeyText(e.getKeyCode()));
					try {
						Settings.writeSettings(setting, String.valueOf(e.getKeyCode()));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					openKeybind = e.getKeyCode();
				}
				panel.requestFocusInWindow();
			}
		});
		input.setBounds(width - 140, 1, 100, 32);

		input.setDocument(doc);

		JLabel keybindButton = new JLabel(text);
		keybindButton.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		keybindButton.setBounds(25, 3, keybindButton.getPreferredSize().width + 5, keybindButton.getPreferredSize().height + 5);
		keybindButton.setForeground(Defaults.FOREGROUND);
		panel.add(keybindButton);
		panel.add(input);
		return panel;
	}

	static void loadSettings() throws IOException {

		if (!Settings.getSettings("openKeybind").equalsIgnoreCase("") && !Settings.getSettings("openKeybind").equalsIgnoreCase("-1")) {
			openKeybind = Integer.parseInt(Settings.getSettings("openKeybind"));
		}
		for (Component component : content.getComponents()) {
			if (component instanceof JPanel) {
				for (Component component1 : ((JPanel) component).getComponents()) {
					if (component1 instanceof JLabel) {
						if (((JLabel) component1).getText().equalsIgnoreCase("Open Keybind")) {
							if (!KeyEvent.getKeyText(ShortcutSettings.openKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
								((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(openKeybind));
							} else {
								((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
							}
						}
					}
				}
			}
		}
	}
}
