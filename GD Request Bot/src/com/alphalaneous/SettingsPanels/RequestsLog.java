package com.alphalaneous.SettingsPanels;

import com.alphalaneous.Components.*;
import com.alphalaneous.Defaults;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.SettingsWindow;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;

import static com.alphalaneous.Defaults.settingsButtonUI;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class RequestsLog {
	private static JPanel blockedSettingsPanel = new JPanel();
	private static JPanel blockedListPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(blockedListPanel);
	private static RoundedJButton clearLogs = new RoundedJButton("\uE107", "$CLEAR_LOGS_TOOLTIP$");

	private static int i = 0;
	private static double height = 0;

	public static JPanel createPanel() {

		blockedSettingsPanel.setBackground(Defaults.TOP);
		blockedSettingsPanel.setLayout(null);

		LangLabel label = new LangLabel("$LOGGED_IDS$");
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.SEGOE.deriveFont(14f));
		label.setBounds(25, 20, label.getPreferredSize().width + 5, label.getPreferredSize().height + 5);


		clearLogs.setBackground(Defaults.BUTTON);
		clearLogs.setBounds(370, 16, 30, 30);
		clearLogs.setFont(Defaults.SYMBOLS.deriveFont(18f));
		clearLogs.setForeground(Defaults.FOREGROUND);
		clearLogs.setUI(settingsButtonUI);
		clearLogs.addActionListener(e ->
			new Thread(() -> {
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
					String option = DialogBox.showDialogBox("$CLEAR_LOGS_DIALOG_TITLE$", "<html> $CLEAR_LOGS_DIALOG_INFO$ <html>", "", new String[]{"$YES$", "$NO$"});

					if (option.equalsIgnoreCase("YES")) {
						if (Files.exists(file)) {
							try {
								Files.delete(file);
								clear();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
					SettingsWindow.run = true;
				}).start()
		);

		blockedSettingsPanel.add(label);
		blockedSettingsPanel.add(clearLogs);
		blockedListPanel.setDoubleBuffered(true);
		blockedListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
		//blockedListPanel.setBounds(0, 0, 415, 0);
		blockedListPanel.setPreferredSize(new Dimension(400, 0));
		blockedListPanel.setBackground(Defaults.SUB_MAIN);
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

		blockedSettingsPanel.setBounds(0, 0, 415, 622);
		blockedSettingsPanel.add(scrollPane);
		return blockedSettingsPanel;

	}
	public static void clear(){
		blockedListPanel.removeAll();
		height = 0;
		blockedListPanel.setBounds(0, 0, 400, (int) (height + 4));
		blockedListPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
		scrollPane.updateUI();

	}
	private static void removeID(String ID) {
		i--;
		if (i % 4 == 0 && i !=0) {
			height = height - 39;
			blockedListPanel.setBounds(0, 0, 400, (int) (height + 4));
			blockedListPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));

			scrollPane.updateUI();
		}
		scrollPane.updateUI();
		for (Component component : blockedListPanel.getComponents()) {
			if (component instanceof CurvedButton) {
				if (((CurvedButton) component).getLText().equalsIgnoreCase(ID)) {
					blockedListPanel.remove(component);
					blockedListPanel.updateUI();
				}
			}
		}
	}

	public static void addButton(long ID) {
		i++;
		if ((i-1) % 4 == 0) {
			height = height + 39;
			blockedListPanel.setBounds(0, 0, 400, (int) (height + 4));
			blockedListPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
		}

		Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt");
		CurvedButton button = new CurvedButton(String.valueOf(ID));

		button.setBackground(Defaults.BUTTON);
		button.setUI(settingsButtonUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setPreferredSize(new Dimension(85, 35));
		button.addActionListener(e -> {
				SettingsWindow.run = false;

				new Thread(() ->{

				String option = DialogBox.showDialogBox("$REMOVE_LOG_DIALOG_TITLE$", "<html> $REMOVE_LOG_DIALOG_INFO$ <html>", "", new String[]{"$YES$", "$NO$"}, new Object[]{button.getLText()});

				if (option.equalsIgnoreCase("YES")) {
					if (Files.exists(file)) {
						try {
							Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_tempLog_");
							PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
							Files.lines(file)
									.filter(line -> !line.contains(button.getLText()))
									.forEach(out::println);
							out.flush();
							out.close();
							Files.delete(file);
							Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\requestsLog.txt"), StandardCopyOption.REPLACE_EXISTING);

						} catch (IOException ex) {

							JOptionPane.showMessageDialog(Window.frame, "There was an error writing to the file!", "Error", JOptionPane.ERROR_MESSAGE);
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
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
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
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
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
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
}
