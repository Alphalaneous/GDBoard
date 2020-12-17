package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.*;
import com.alphalaneous.Defaults;
import com.alphalaneous.ThemedComponents.ThemedCheckbox;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.SettingsWindow;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

import static com.alphalaneous.Defaults.settingsButtonUI;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class BlockedSettings {
	private static JPanel blockedSettingsPanel = new JPanel();
	private static JPanel blockedListPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(blockedListPanel);
	private static FancyTextArea blockedInput = new FancyTextArea(true, false);
	private static RoundedJButton addID = new RoundedJButton("\uECC8", "$ADD_ID_TOOLTIP$");
	private static int i = 0;
	private static double height = 0;

	public static JPanel createPanel() {

		blockedSettingsPanel.setBackground(Defaults.TOP);
		blockedSettingsPanel.setLayout(null);

		LangLabel label = new LangLabel("$BLOCKED_IDS$");
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.SEGOE.deriveFont(14f));
		label.setBounds(25, 20, label.getPreferredSize().width + 5, label.getPreferredSize().height + 5);

		blockedInput.setBounds(260, 15, 100, 32);
		blockedInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		addID.setBackground(Defaults.BUTTON);
		addID.setBounds(370, 16, 30, 30);
		addID.setFont(Defaults.SYMBOLS.deriveFont(22f));
		addID.setForeground(Defaults.FOREGROUND);
		addID.setUI(settingsButtonUI);

		blockedSettingsPanel.add(addID);
		blockedSettingsPanel.add(blockedInput);
		blockedSettingsPanel.add(label);
		blockedListPanel.setDoubleBuffered(true);
		blockedListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

		blockedListPanel.setBounds(0, 0, 400, 0);
		blockedListPanel.setPreferredSize(new Dimension(400, 0));
		blockedListPanel.setBackground(Defaults.SUB_MAIN);
		addID.addActionListener(e -> {


			try {
				Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
				if (!Files.exists(file)) {
					Files.createFile(file);
				}
				boolean goThrough = true;
				Scanner sc = new Scanner(file.toFile());
				while (sc.hasNextLine()) {
					if (String.valueOf(blockedInput.getText()).equals(sc.nextLine())) {
						goThrough = false;
						break;
					}
				}
				sc.close();
				if (goThrough) {
					if (!blockedInput.getText().equalsIgnoreCase("")) {

						Files.write(file, (blockedInput.getText() + "\n").getBytes(), StandardOpenOption.APPEND);
						addButton(Long.parseLong(blockedInput.getText()));
						blockedInput.setText("");
						blockedListPanel.updateUI();
					}
				}
			} catch (IOException e1) {
				DialogBox.showDialogBox("Error!", e1.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
			}

		});
		scrollPane.setBounds(0, 60, 412, 562);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
		scrollPane.setPreferredSize(new Dimension(412, 562));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());

		File file = new File(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
		if (file.exists()) {
			Scanner sc = null;
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			assert sc != null;
			while (sc.hasNextLine()) {
				addButton(Long.parseLong(sc.nextLine()));
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			sc.close();
		}
		blockedSettingsPanel.setBounds(0, 0, 415, 622);
		blockedSettingsPanel.add(scrollPane);
		return blockedSettingsPanel;

	}

	public static void removeID(String ID) {
		i--;
		if (i % 4 == 0 && i != 0) {
			height = height - 39;
			blockedListPanel.setBounds(0, 0, 400, (int) (height + 4));
			blockedListPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
			scrollPane.updateUI();
		}
		for (Component component : blockedListPanel.getComponents()) {
			if (component instanceof CurvedButton) {
				System.out.println(((CurvedButton) component).getLText());
				if (((CurvedButton) component).getLText().equalsIgnoreCase(ID)) {
					blockedListPanel.remove(component);
					blockedListPanel.updateUI();
				}
			}
		}
	}

	public static void addButton(long ID) {
		i++;
		if ((i - 1) % 4 == 0) {
			height = height + 39;
			blockedListPanel.setBounds(0, 0, 400, (int) (height + 4));
			blockedListPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
		}
		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\blocked.txt");
		CurvedButton button = new CurvedButton(String.valueOf(ID));

		button.setBackground(Defaults.BUTTON);
		button.setUI(settingsButtonUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setPreferredSize(new Dimension(85, 35));
		button.addActionListener(e -> {
			SettingsWindow.run = false;
			new Thread(() -> {

				String option = DialogBox.showDialogBox("$UNBLOCK_ID_DIALOG_TITLE$", "<html> $UNBLOCK_ID_DIALOG_INFO$ <html>", "", new String[]{"$YES$", "$NO$"}, new Object[]{button.getLText()});

				if (option.equalsIgnoreCase("YES")) {
					if (Files.exists(file)) {
						try {
							Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_temp_");
							PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
							Files.lines(file)
									.filter(line -> !line.contains(button.getLText()))
									.forEach(out::println);
							out.flush();
							out.close();
							Files.delete(file);
							Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\blocked.txt"), StandardCopyOption.REPLACE_EXISTING);

						} catch (IOException ex) {

							DialogBox.showDialogBox("Error!", ex.toString(), "Please report to Alphalaneous.", new String[]{"OK"});
						}
					}
					removeID(button.getLText());
				}
				SettingsWindow.run = true;
			}).start();

		});
		button.refresh();
		blockedListPanel.add(button);

	}

	public static void refreshUI() {

		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		blockedSettingsPanel.setBackground(Defaults.TOP);
		for (Component component : blockedSettingsPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
				component.setForeground(Defaults.FOREGROUND);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if (component instanceof JTextArea) {
				((FancyTextArea) component).refresh_();
			}
		}
		blockedListPanel.setBackground(Defaults.SUB_MAIN);
		for (Component component : blockedListPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JTextArea) {
				((FancyTextArea) component).refresh_();
			}
			if (component instanceof ThemedCheckbox) {
				((ThemedCheckbox) component).refresh();
			}
		}
	}
}
