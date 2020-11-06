package Main.SettingsPanels;

import Main.*;
import Main.Components.*;
import Main.Windows.CommandEditor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static Main.Defaults.settingsButtonUI;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class CommandSettings {
	private static int i = 0;
	private static double height = 0;
	private static JLabel commandLabel = new JLabel();
	private static LangLabel sliderValue = new LangLabel("");
	private static JPanel commandsPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(commandsPanel);
	private static JPanel panel = new JPanel();
	private static JPanel titlePanel = new JPanel();
	private static RoundedJButton addCommand = new RoundedJButton("\uECC8", "$ADD_COMMAND_TOOLTIP$");
	private static String[] gdCommands = {"!gd", "!kill", "!block", "!blockuser", "!unblock", "!unblockuser", "!clear", "!info", "!move", "!next", "!position", "!queue", "!remove", "!request", "!song", "!stop", "!toggle", "!top", "!wronglevel"};


	public static JPanel createPanel() {

		LangLabel label = new LangLabel("$COMMANDS_LIST$");
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.SEGOE.deriveFont(14f));
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
				CommandEditor.showEditor("commands", "", false);
			}
		});

		panel.add(addCommand);


		titlePanel.setBounds(0, 0, 415, 50);
		titlePanel.setLayout(null);
		titlePanel.setDoubleBuffered(true);
		titlePanel.setBackground(Defaults.TOP);


		commandLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		commandLabel.setForeground(Defaults.FOREGROUND);
		commandLabel.setBounds(50,17,commandLabel.getPreferredSize().width+5, commandLabel.getPreferredSize().height + 5);
		titlePanel.add(commandLabel);


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
			URI uri = Main.class.getResource("/Resources/Commands/").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = BotHandler.fileSystem.getPath("/Resources/Commands/");
			} else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/commands/");
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
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file;
				if(uri.getScheme().equals("jar")){
					file = path.toString().split("/");
				}
				else{
					file = path.toString().split("\\\\");
				}
				String fileName = file[file.length - 1];
				if (fileName.endsWith(".js")) {
					if(!fileName.equalsIgnoreCase("!rick.js") &&
							!fileName.equalsIgnoreCase("!stoprick.js") &&
							!fileName.equalsIgnoreCase("!eval.js") &&
							!fileName.equalsIgnoreCase("!end.js") &&
							!fileName.equalsIgnoreCase("!popup.js") &&
							!fileName.equalsIgnoreCase("b!addcom.js")&&
							!fileName.equalsIgnoreCase("b!editcom.js")&&
							!fileName.equalsIgnoreCase("b!delcom.js")&&
							!fileName.equalsIgnoreCase("b!addpoint.js")&&
							!fileName.equalsIgnoreCase("b!editpoint.js")&&
							!fileName.equalsIgnoreCase("b!delpoint.js")){
						String substring = fileName.substring(0, fileName.length() - 3);
						if(!existingCommands.containsKey(substring)) {
							existingCommands.put(substring, new ButtonInfo( path, true));
						}
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		TreeMap<String, ButtonInfo> sorted = new TreeMap<>(existingCommands);

		for(Map.Entry<String,ButtonInfo> entry : sorted.entrySet()) {
			boolean exists = false;
			String key = entry.getKey();
			ButtonInfo value = entry.getValue();
			if(!GeneralSettings.gdModeOption){
				for(String command : gdCommands){
					if(key.equalsIgnoreCase(command)){
						exists = true;
						break;
					}
				}
			}
			if(!exists) {
				addButton(key, value.isDefault);
			}
		}
		panel.setBounds(0, 0, 415, 622);
		panel.add(scrollPane);
		return panel;
	}
	public static void refresh(){

		commandsPanel.removeAll();
		height = 0;
		commandsPanel.setBounds(0, 0, 400, (int) (height + 4));
		commandsPanel.setPreferredSize(new Dimension(400, (int) (height + 4)));

		HashMap<String, ButtonInfo> existingCommands = new HashMap<>();
		try {
			URI uri = Main.class.getResource("/Resources/Commands/").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = BotHandler.fileSystem.getPath("/Resources/Commands/");
			} else {
				myPath = Paths.get(uri);
			}
			Stream<Path> walk = Files.walk(myPath, 1);
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/commands/");
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
			for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
				Path path = it.next();
				String[] file;
				if(uri.getScheme().equals("jar")){
					file = path.toString().split("/");
				}
				else{
					file = path.toString().split("\\\\");
				}
				String fileName = file[file.length - 1];
				if (fileName.endsWith(".js")) {

					if(!fileName.equalsIgnoreCase("!rick.js") &&
							!fileName.equalsIgnoreCase("!stoprick.js") &&
							!fileName.equalsIgnoreCase("!eval.js") &&
							!fileName.equalsIgnoreCase("!end.js") &&
							!fileName.equalsIgnoreCase("!popup.js") &&
							!fileName.equalsIgnoreCase("b!addcom.js")&&
							!fileName.equalsIgnoreCase("b!editcom.js")&&
							!fileName.equalsIgnoreCase("b!delcom.js")&&
							!fileName.equalsIgnoreCase("b!addpoint.js")&&
							!fileName.equalsIgnoreCase("b!editpoint.js")&&
							!fileName.equalsIgnoreCase("b!delpoint.js")){
						String substring = fileName.substring(0, fileName.length() - 3);
						if(!existingCommands.containsKey(substring)) {
							existingCommands.put(substring, new ButtonInfo( path, true));
						}
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		TreeMap<String, ButtonInfo> sorted = new TreeMap<>(existingCommands);

		for(Map.Entry<String,ButtonInfo> entry : sorted.entrySet()) {
			boolean exists = false;
			String key = entry.getKey();
			ButtonInfo value = entry.getValue();
			if(!GeneralSettings.gdModeOption){
				for(String command : gdCommands){
					if(key.equalsIgnoreCase(command)){
						exists = true;
						break;
					}
				}
			}
			if(!exists) {
				addButton(key, value.isDefault);
			}
		}
	}
	public static class ButtonInfo{

		public Path path;
		boolean isDefault;

		ButtonInfo(Path path, boolean isDefault){
			this.path = path;
			this.isDefault = isDefault;
		}

	}
	public static void addButton(String command, boolean isDefault) {
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
		if(isDefault) {
			button.setForeground(Defaults.FOREGROUND2);
		}
		else{
			button.setForeground(Defaults.FOREGROUND);
		}
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.setPreferredSize(new Dimension(170, 35));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				CommandEditor.showEditor("commands", command, isDefault);
			}
		});
		button.refresh();
		commandsPanel.add(button);
	}
	public static void refreshUI(){
		panel.setBackground(Defaults.TOP);
		titlePanel.setBackground(Defaults.TOP);
		commandLabel.setForeground(Defaults.FOREGROUND);
		sliderValue.setForeground(Defaults.FOREGROUND);
		commandsPanel.setBackground(Defaults.SUB_MAIN);

		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
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
