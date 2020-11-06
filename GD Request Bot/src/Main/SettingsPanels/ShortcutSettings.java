package Main.SettingsPanels;

import Main.*;
import Main.Components.CheckboxButton;
import Main.Components.FancyTextArea;
import Main.Components.LangLabel;
import org.jnativehook.keyboard.SwingKeyAdapter;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.*;


public class ShortcutSettings {
	private static JPanel panel = new JPanel();
	private static JPanel openPanel = createKeybindButton(25, "$OPEN_SHORTCUT$", "openKeybind");
	private static JPanel skipPanel = createKeybindButton(75, "$SKIP_SHORTCUT$", "skipKeybind");
	private static JPanel randPanel = createKeybindButton(125, "$RANDOM_SHORTCUT$", "randomKeybind");
	private static JPanel copyPanel = createKeybindButton(175, "$COPY_SHORTCUT$", "copyKeybind");
	private static JPanel blockPanel = createKeybindButton(225, "$BLOCK_SHORTCUT$", "blockKeybind");
	private static JPanel clearPanel = createKeybindButton(275, "$CLEAR_SHORTCUT$", "clearKeybind");
	public static boolean focused = false;

	public static int openKeybind = 0;
	public static int skipKeybind = 0;
	public static int randKeybind = 0;
	public static int copyKeybind = 0;
	public static int blockKeybind = 0;
	public static int clearKeybind = 0;
	public static int lockKeybind = 0;


	public static JPanel createPanel() {

		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setLayout(null);


		panel.add(openPanel);
		panel.add(skipPanel);
		panel.add(randPanel);
		panel.add(copyPanel);
		panel.add(blockPanel);
		panel.add(clearPanel);

		return panel;

	}

	private static JPanel createKeybindButton(int y, String text, String setting) {
		JPanel panel = new JPanel(null);
		panel.setBounds(0, y, 415, 36);
		panel.setBackground(Defaults.SUB_MAIN);
		FancyTextArea input = new FancyTextArea(false, false);
		DefaultStyledDocument doc = new DefaultStyledDocument();
		input.setEditable(false);
		input.addKeyListener(new SwingKeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 17 || e.getKeyCode() == 18 || e.getKeyCode() == 10) {
						input.setText("");

							Settings.writeSettings(setting, "-1");
							loadKeybind(text, -1);
				} else {
					input.setText(KeyEvent.getKeyText(e.getKeyCode()));
						Settings.writeSettings(setting, String.valueOf(e.getKeyCode()));
						loadKeybind(text, e.getKeyCode());
				}
				panel.requestFocusInWindow();
			}
		});
        /*input.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(isFocused[0]){
                    if (!(e.getButton() < 4)) {
                        input.setText("Mouse " + e.getButton());
                    }
                }
            }
        });*/
        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
            }
        });
		input.setBounds(285, 1, 100, 32);

		input.setDocument(doc);

		LangLabel keybindButton = new LangLabel(text);
		keybindButton.setFont(Defaults.SEGOE.deriveFont(14f));
		keybindButton.setBounds(25, 3, keybindButton.getPreferredSize().width + 5, keybindButton.getPreferredSize().height + 5);
		keybindButton.setForeground(Defaults.FOREGROUND);


		panel.add(keybindButton);
		panel.add(input);
		return panel;
	}

	@SuppressWarnings("unused")
	private static CheckboxButton createButton(String text, int x, int y, int width) {

		CheckboxButton button = new CheckboxButton(text, ShortcutSettings.class);
		button.setBounds(25, y, width, 30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.refresh();
		return button;
	}

	public static void loadSettings() {


				if (!Settings.getSettings("onboarding").equalsIgnoreCase("false")) {
					openKeybind = Onboarding.openKeybind;
				} else if (!Settings.getSettings("openKeybind").equalsIgnoreCase("") && !Settings.getSettings("openKeybind").equalsIgnoreCase("-1")) {
					openKeybind = Integer.parseInt(Settings.getSettings("openKeybind"));
				}
				if (!Settings.getSettings("skipKeybind").equalsIgnoreCase("") && !Settings.getSettings("skipKeybind").equalsIgnoreCase("-1")) {
					skipKeybind = Integer.parseInt(Settings.getSettings("skipKeybind"));
				}
				if (!Settings.getSettings("randomKeybind").equalsIgnoreCase("") && !Settings.getSettings("randomKeybind").equalsIgnoreCase("-1")) {
					randKeybind = Integer.parseInt(Settings.getSettings("randomKeybind"));
				}
				if (!Settings.getSettings("copyKeybind").equalsIgnoreCase("") && !Settings.getSettings("copyKeybind").equalsIgnoreCase("-1")) {
					copyKeybind = Integer.parseInt(Settings.getSettings("copyKeybind"));
				}
				if (!Settings.getSettings("blockKeybind").equalsIgnoreCase("") && !Settings.getSettings("blockKeybind").equalsIgnoreCase("-1")) {
					blockKeybind = Integer.parseInt(Settings.getSettings("blockKeybind"));
				}
				if (!Settings.getSettings("clearKeybind").equalsIgnoreCase("") && !Settings.getSettings("clearKeybind").equalsIgnoreCase("-1")) {
					clearKeybind = Integer.parseInt(Settings.getSettings("clearKeybind"));
				}

				for (Component component : panel.getComponents()) {
					if (component instanceof JPanel) {
						for (Component component1 : ((JPanel) component).getComponents()) {
							if (component1 instanceof LangLabel) {
								if (((LangLabel) component1).getIdentifier().equalsIgnoreCase("OPEN_SHORTCUT")) {
									if (!KeyEvent.getKeyText(openKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(openKeybind));
									} else {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
									}
								}
								if (((LangLabel) component1).getIdentifier().equalsIgnoreCase("SKIP_SHORTCUT")) {
									if (!KeyEvent.getKeyText(skipKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(skipKeybind));
									} else {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
									}
								}
								if (((LangLabel) component1).getIdentifier().equalsIgnoreCase("RANDOM_SHORTCUT")) {
									if (!KeyEvent.getKeyText(randKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(randKeybind));
									} else {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
									}
								}
								if (((LangLabel) component1).getIdentifier().equalsIgnoreCase("COPY_SHORTCUT")) {
									if (!KeyEvent.getKeyText(copyKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(copyKeybind));
									} else {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
									}
								}
								if (((LangLabel) component1).getIdentifier().equalsIgnoreCase("BLOCK_SHORTCUT")) {
									if (!KeyEvent.getKeyText(blockKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(blockKeybind));
									} else {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
									}
								}
								if (((LangLabel) component1).getIdentifier().equalsIgnoreCase("CLEAR_SHORTCUT")) {
									if (!KeyEvent.getKeyText(clearKeybind).equalsIgnoreCase("Unknown keyCode: 0x0")) {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText(KeyEvent.getKeyText(clearKeybind));
									} else {
										((FancyTextArea) ((JPanel) component).getComponent(1)).setText("");
									}

								}
							}
						}
					}


		}
	}

	public static void loadKeybind(String setting, int keybind) {
		if (setting.equalsIgnoreCase("$OPEN_SHORTCUT$")) {
			openKeybind = keybind;
		}
		if (setting.equalsIgnoreCase("$SKIP_SHORTCUT$")) {
			skipKeybind = keybind;
		}
		if (setting.equalsIgnoreCase("$RANDOM_SHORTCUT$")) {
			randKeybind = keybind;
		}
		if (setting.equalsIgnoreCase("$COPY_SHORTCUT$")) {
			copyKeybind = keybind;
		}
		if (setting.equalsIgnoreCase("$BLOCK_SHORTCUT$")) {
			blockKeybind = keybind;
		}
		if (setting.equalsIgnoreCase("$CLEAR_SHORTCUT$")) {
			clearKeybind = keybind;
		}
	}

	public static void refreshUI() {

		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JPanel) {
				for (Component component2 : ((JPanel) component).getComponents()) {
					if (component2 instanceof JButton) {
						for (Component component3 : ((JButton) component2).getComponents()) {
							if (component3 instanceof JLabel) {
								component3.setForeground(Defaults.FOREGROUND);
							}
						}
						component2.setBackground(Defaults.MAIN);
					}
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);

					}
					if (component2 instanceof JTextArea) {
						((FancyTextArea) component2).refreshAll();
					}

				}
				component.setBackground(Defaults.SUB_MAIN);
			}
		}
	}
}