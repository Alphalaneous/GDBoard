package Main.SettingsPanels;

import Main.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static Main.Defaults.settingsButtonUI;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class ChannelPointSettings {
	private static int i = 0;
	private static double height = 0;
	private static String command;
	private static JLabel commandLabel = new JLabel();
	private static JPanel commandsPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(commandsPanel);
	private static JPanel panel = new JPanel();
	private static JPanel commandPanelView = new JPanel(null);
	private static JPanel titlePanel = new JPanel();
	private static RSyntaxTextArea codeInput = new RSyntaxTextArea();
	private static JScrollPane codePanel = new JScrollPane(codeInput);
	private static RoundedJButton backButton = new RoundedJButton("\uE112", "$BACK_BUTTON$");
	private static RoundedJButton addCommand = new RoundedJButton("\uECC8", "$ADD_CHANNEL_POINTS_TOOLTIP$");


	public static JPanel createPanel() {

		LangLabel label = new LangLabel("$POINTS_LIST$");
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		label.setBounds(25, 20, label.getPreferredSize().width + 5, label.getPreferredSize().height + 5);

		panel.add(label);

		addCommand.setBackground(Defaults.BUTTON);
		addCommand.setBounds(370, 16, 30, 30);
		addCommand.setFont(Defaults.SYMBOLS.deriveFont(18f));
		addCommand.setForeground(Defaults.FOREGROUND);
		addCommand.setUI(settingsButtonUI);
		addCommand.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CommandEditor.showEditor("points", "", false);
			}
		});

		panel.add(addCommand);


		codeInput.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);

		codeInput.setCurrentLineHighlightColor(Defaults.BUTTON);
		titlePanel.setBounds(0, 0, 415, 50);
		titlePanel.setLayout(null);
		titlePanel.setDoubleBuffered(true);
		titlePanel.setBackground(Defaults.TOP);

		codePanel.getVerticalScrollBar().setUI(new ScrollbarUI());
		codePanel.getHorizontalScrollBar().setUI(new ScrollbarUI());
		codePanel.getVerticalScrollBar().setOpaque(false);
		codePanel.getHorizontalScrollBar().setOpaque(false);
		codeInput.setTabSize(4);
		codeInput.setBackground(Defaults.MAIN);
		codeInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
		codeInput.setForeground(Defaults.FOREGROUND);
		codePanel.setBorder(BorderFactory.createEmptyBorder());

		codePanel.setBounds(10,70,395, 200);
		commandLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		commandLabel.setForeground(Defaults.FOREGROUND);
		commandLabel.setBounds(50,17,commandLabel.getPreferredSize().width+5, commandLabel.getPreferredSize().height + 5);
		titlePanel.add(commandLabel);

		backButton.setBackground(Defaults.BUTTON);
		backButton.setBounds(10, 10, 30, 30);
		backButton.setFont(Defaults.SYMBOLS.deriveFont(14f));
		backButton.setForeground(Defaults.FOREGROUND);
		backButton.setUI(settingsButtonUI);
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				showMainPanel();
			}
		});

		titlePanel.add(backButton);



		commandPanelView.setBounds(0, 0, 415, 622);
		commandPanelView.add(titlePanel);
		commandPanelView.add(codePanel);
		commandPanelView.setBackground(Defaults.SUB_MAIN);
		commandPanelView.setVisible(false);

		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		commandsPanel.setDoubleBuffered(true);
		commandsPanel.setBounds(0, 0, 400, 0);
		commandsPanel.setPreferredSize(new Dimension(400, 0));
		commandsPanel.setBackground(Defaults.SUB_MAIN);
		commandsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
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
		HashMap<String, ButtonInfo> existingCommands = new HashMap<>();
		try {
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/points/");
			if (Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.endsWith(".js")) {
						existingCommands.put(fileName.substring(0, fileName.length()-3), new ButtonInfo( path, false));
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		TreeMap<String, ButtonInfo> sorted = new TreeMap<>();
		sorted.putAll(existingCommands);

		for(Map.Entry<String,ButtonInfo> entry : sorted.entrySet()) {
			String key = entry.getKey();
			ButtonInfo value = entry.getValue();

			addButton(key, value.path);
		}
		panel.setBounds(0, 0, 415, 622);
		panel.add(scrollPane);
		panel.add(commandPanelView);
		return panel;
	}
	private static void showMainPanel(){
		commandPanelView.setVisible(false);
		scrollPane.setVisible(true);

	}
	public static void refresh(){

		commandsPanel.removeAll();
		height = 0;
		commandsPanel.setBounds(0, 0, 400, (int) (height + 4));
		commandsPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));

		HashMap<String, ButtonInfo> existingCommands = new HashMap<>();
		try {
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/points/");
			if (Files.exists(comPath)) {
				Stream<Path> walk1 = Files.walk(comPath, 1);
				for (Iterator<Path> it = walk1.iterator(); it.hasNext(); ) {
					Path path = it.next();
					String[] file = path.toString().split("\\\\");
					String fileName = file[file.length - 1];
					if (fileName.endsWith(".js")) {
						existingCommands.put(fileName.substring(0, fileName.length()-3), new ButtonInfo( path, false));
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		TreeMap<String, ButtonInfo> sorted = new TreeMap<>();
		sorted.putAll(existingCommands);

		for(Map.Entry<String,ButtonInfo> entry : sorted.entrySet()) {
			String key = entry.getKey();
			ButtonInfo value = entry.getValue();

			addButton(key, value.path);
		}
	}
	public static class ButtonInfo{

		public Path path;

		ButtonInfo(Path path, boolean isDefault){
			this.path = path;
		}

	}
	public static void removeButton(String command) {
		i--;
		if (i % 2 == 0) {
			height = height - 39;
			commandsPanel.setBounds(0, 0, 400, (int) (height + 4));
			commandsPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
			scrollPane.updateUI();
		}
		for (int i = commandsPanel.getComponents().length-1; i >= 0; i--) {
			if (commandsPanel.getComponents()[i] instanceof CurvedButton) {
				System.out.println(((CurvedButton) commandsPanel.getComponents()[i]).getLText());
				if (((CurvedButton) commandsPanel.getComponents()[i]).getLText().equalsIgnoreCase(command)) {
					commandsPanel.remove(commandsPanel.getComponents()[i]);
					commandsPanel.updateUI();
					break;
				}
			}
		}
	}


	public static void addButton(String command, Path path) {
		i++;
		if ((i-1) % 2 == 0) {
			height = height + 39;

			commandsPanel.setBounds(0, 0, 400, (int) (height + 4));
			commandsPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));
			if(i > 0) {
				scrollPane.updateUI();
			}
		}
		CurvedButton button = new CurvedButton(command);
		button.setBackground(Defaults.BUTTON);
		button.setUI(settingsButtonUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setPreferredSize(new Dimension(170, 35));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CommandEditor.showEditor("points", command, false);
			}
		});
		button.refresh();
		commandsPanel.add(button);
	}

	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		button.refresh();
		return button;
	}
	public static void refreshUI(){
		panel.setBackground(Defaults.TOP);
		titlePanel.setBackground(Defaults.TOP);
		commandLabel.setForeground(Defaults.FOREGROUND);
		commandsPanel.setBackground(Defaults.SUB_MAIN);
		commandPanelView.setBackground(Defaults.SUB_MAIN);

		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		codeInput.setForeground(Defaults.FOREGROUND);
		codeInput.setBackground(Defaults.MAIN);
		codeInput.setCurrentLineHighlightColor(Defaults.BUTTON);
		codePanel.getVerticalScrollBar().setUI(new ScrollbarUI());
		codePanel.getHorizontalScrollBar().setUI(new ScrollbarUI());
		backButton.setBackground(Defaults.BUTTON);
		backButton.setForeground(Defaults.FOREGROUND);
		for (Component component : commandsPanel.getComponents()) {
			if (component instanceof CurvedButton) {

				if(component.getForeground().equals(Defaults.FOREGROUND2)) {
					component.setForeground(Defaults.FOREGROUND2);
				}
				else{
					component.setForeground(Defaults.FOREGROUND);
				}
				component.setBackground(Defaults.BUTTON);
				((CurvedButton) component).refresh();
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
		for (Component component : commandPanelView.getComponents()) {
			if (component instanceof JButton) {

				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				component.setForeground(Defaults.FOREGROUND);
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
		refresh();
	}
}
