package Main.SettingsPanels;

import Main.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
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
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class CommandSettings {
	private static int i = 0;
	private static double height = 0;
	private static String command;
	private static JLabel commandLabel = new JLabel();
	private static JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 1200, 0);
	private static LangLabel sliderValue = new LangLabel("");
	private static JPanel commandsPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(commandsPanel);
	private static JPanel panel = new JPanel();
	private static JPanel commandPanelView = new JPanel(null);
	private static JPanel titlePanel = new JPanel();
	private static RSyntaxTextArea codeInput = new RSyntaxTextArea();
	private static JScrollPane codePanel = new JScrollPane(codeInput);
	private static CheckboxButton disable = createButton("$DISABLE_COMMAND$", 290);
	private static CheckboxButton modOnly = createButton("$MOD_ONLY$", 320);
	private static CheckboxButton whisper = createButton("$SEND_AS_WHISPER$", 350);
	private static RoundedJButton backButton = new RoundedJButton("\uE112", "$BACK_BUTTON$");
	private static RoundedJButton addCommand = new RoundedJButton("\uECC8", "$ADD_COMMAND_TOOLTIP$");


	public static JPanel createPanel() {

		LangLabel label = new LangLabel("$COMMANDS_LIST$");
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
				CommandEditor.showEditor("commands", "", false);
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
		commandLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
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


		sliderValue.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		sliderValue.setTextLangFormat("$COOLDOWN$", 0);
		sliderValue.setForeground(Defaults.FOREGROUND);
		sliderValue.setBounds(25,390,sliderValue.getPreferredSize().width+5, sliderValue.getPreferredSize().height + 5);

		UIDefaults sliderDefaults = new UIDefaults();

		sliderDefaults.put("Slider.thumbWidth", 20);
		sliderDefaults.put("Slider.thumbHeight", 20);
		sliderDefaults.put("Slider:SliderThumb.backgroundPainter", (Painter<JComponent>) (g, c, w, h) -> {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(2f));
			g.setColor(Defaults.ACCENT);
			g.fillOval(1, 1, w-3, h-3);
			g.setColor(Defaults.MAIN);
			g.drawOval(1, 1, w-4, h-4);
		});
		sliderDefaults.put("Slider:SliderTrack.backgroundPainter", (Painter<JComponent>) (g, c, w, h) -> {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(2f));
			g.setColor(Defaults.BUTTON);
			g.fillRoundRect(0, 6, w-1, 8, 8, 8);
		});
		sliderDefaults.put("Slider:SliderThumb[MouseOver].backgroundPainter", (Painter<JComponent>) (g, c, w, h) -> {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setStroke(new BasicStroke(2f));
			g.setColor(Defaults.ACCENT);
			g.fillOval(1, 1, w-3, h-3);
			g.setColor(Defaults.TOP);
			g.drawOval(1, 1, w-4, h-4);
		});

		slider.setMinorTickSpacing(5);
		slider.setMinorTickSpacing(100);
		slider.putClientProperty("Nimbus.Overrides",sliderDefaults);
		slider.putClientProperty("Nimbus.Overrides.InheritDefaults",false);
		slider.setBounds(25,410, 365,30);
		slider.setBackground(Defaults.SUB_MAIN);
		slider.setBorder(BorderFactory.createEmptyBorder());
		slider.addChangeListener(e -> {
			if(slider.getValue() == 10){
				sliderValue.setTextLangFormat("$COOLDOWN_SINGULAR$",(double)slider.getValue()/10);

			}
			else {
				sliderValue.setTextLangFormat("$COOLDOWN$",(double)slider.getValue()/10);
			}
			sliderValue.setBounds(25, 390, sliderValue.getPreferredSize().width + 5, sliderValue.getPreferredSize().height + 5);
			try {
				int cooldown = -1;
				if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt"))) {
					Scanner sc3 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt").toFile());
					while (sc3.hasNextLine()) {
						String line = sc3.nextLine();
						if (line.split("=")[0].trim().equalsIgnoreCase(command)) {
							cooldown = Integer.parseInt(line.split("=")[1].trim());
							break;
						}
					}
					sc3.close();
				}
				else{
					Files.createFile(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt"));
				}
				if(cooldown != -1) {
					BufferedReader file = new BufferedReader(new FileReader(Defaults.saveDirectory + "/GDBoard/cooldown.txt"));
					StringBuilder inputBuffer = new StringBuilder();
					String line;
					while ((line = file.readLine()) != null) {
						inputBuffer.append(line);
						inputBuffer.append('\n');
					}
					file.close();

					FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "/GDBoard/cooldown.txt");
					fileOut.write(inputBuffer.toString().replace(command + " = " + cooldown, command + " = " + slider.getValue()).getBytes());
					fileOut.close();
				}
				else{
					BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt").toFile(), true));
					writer.newLine();
					writer.write(command + " = " + slider.getValue());
					writer.close();

				}
			}
			catch (Exception f){
				f.printStackTrace();
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
		disable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (disable.getSelectedState()) {
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\disable.txt");
					try {
						if (!Files.exists(file)) {
							Files.createFile(file);
						}
						Files.write(
								file,
								(command + "\n").getBytes(),
								StandardOpenOption.APPEND);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					boolean exists = false;
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\disable.txt");
					try {
						if (Files.exists(file)) {
							Scanner sc = new Scanner(file);
							while (sc.hasNextLine()) {
								if (String.valueOf(command).equals(sc.nextLine())) {
									exists = true;
									break;
								}
							}
							sc.close();
							if (exists) {
								Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_tempDisable_");
								PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
								Files.lines(file)
										.filter(line -> !line.contains(command))
										.forEach(out::println);
								out.flush();
								out.close();
								Files.delete(file);
								Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\disable.txt"), StandardCopyOption.REPLACE_EXISTING);
							}
						}
					}
					catch (Exception f){
						f.printStackTrace();
					}
				}
			}
		});
		modOnly.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (modOnly.getSelectedState()) {
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\mod.txt");
					try {
						if (!Files.exists(file)) {
							Files.createFile(file);
						}
						Files.write(
								file,
								(command + "\n").getBytes(),
								StandardOpenOption.APPEND);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					boolean exists = false;
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\mod.txt");
					try {
						if (Files.exists(file)) {
							Scanner sc = new Scanner(file);
							while (sc.hasNextLine()) {
								if (String.valueOf(command).equals(sc.nextLine())) {
									exists = true;
									break;
								}
							}
							sc.close();
							if (exists) {
								Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_tempMod_");
								PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
								Files.lines(file)
										.filter(line -> !line.contains(command))
										.forEach(out::println);
								out.flush();
								out.close();
								Files.delete(file);
								Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\mod.txt"), StandardCopyOption.REPLACE_EXISTING);
							}
						}
					}
					catch (Exception f){
						f.printStackTrace();
					}
				}
			}
		});
		whisper.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (whisper.getSelectedState()) {
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\whisper.txt");
					try {
						if (!Files.exists(file)) {
							Files.createFile(file);
						}
						Files.write(
								file,
								(command + "\n").getBytes(),
								StandardOpenOption.APPEND);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					boolean exists = false;
					Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\whisper.txt");
					try {
						if (Files.exists(file)) {
							Scanner sc = new Scanner(file);
							while (sc.hasNextLine()) {
								if (String.valueOf(command).equals(sc.nextLine())) {
									exists = true;
									break;
								}
							}
							sc.close();
							if (exists) {
								Path temp = Paths.get(Defaults.saveDirectory + "\\GDBoard\\_tempWhisper_");
								PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
								Files.lines(file)
										.filter(line -> !line.contains(command))
										.forEach(out::println);
								out.flush();
								out.close();
								Files.delete(file);
								Files.move(temp, temp.resolveSibling(Defaults.saveDirectory + "\\GDBoard\\whisper.txt"), StandardCopyOption.REPLACE_EXISTING);
							}
						}
					}
					catch (Exception f){
						f.printStackTrace();
					}
				}
			}
		});



		commandPanelView.add(disable);
		commandPanelView.add(modOnly);
		commandPanelView.add(whisper);
		commandPanelView.setBounds(0, 0, 415, 622);
		commandPanelView.add(titlePanel);
		commandPanelView.add(sliderValue);
		commandPanelView.add(codePanel);
		commandPanelView.add(slider);
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
			URI uri = Main.class.getResource("/Resources/Commands/").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = ServerChatBot.fileSystem.getPath("/Resources/Commands/");
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
				String[] file = path.toString().split("/");
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
						if(!existingCommands.containsKey(fileName.substring(0, fileName.length()-3))) {
							existingCommands.put(fileName.substring(0, fileName.length()-3), new ButtonInfo( path, true));

						}
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

			addButton(key, value.path, value.isDefault);
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
			URI uri = Main.class.getResource("/Resources/Commands/").toURI();
			Path myPath;
			if (uri.getScheme().equals("jar")) {
				myPath = ServerChatBot.fileSystem.getPath("/Resources/Commands/");
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
				String[] file = path.toString().split("/");
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
						if(!existingCommands.containsKey(fileName.substring(0, fileName.length()-3))) {
							existingCommands.put(fileName.substring(0, fileName.length()-3), new ButtonInfo( path, true));
						}
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

			addButton(key, value.path, value.isDefault);
		}
	}
	public static class ButtonInfo{

		public Path path;
		public boolean isDefault;

		ButtonInfo(Path path, boolean isDefault){
			this.path = path;
			this.isDefault = isDefault;
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

	private static void showCommandPanel(String command, Path path){
		codeInput.discardAllEdits();
		CommandSettings.command = command;
		commandLabel.setText(command);
		commandLabel.setBounds(50,15,commandLabel.getPreferredSize().width+5, commandLabel.getPreferredSize().height + 5);
		StringBuilder function = new StringBuilder();
		try {
			Path comPath = Paths.get(Defaults.saveDirectory + "/GDBoard/commands/" + command.split("\\\\")[command.split("\\\\").length-1] + ".js");
			if (Files.exists(comPath)) {
				codeInput.setText(String.valueOf(Files.readString(comPath, StandardCharsets.UTF_8)));
			}
			else {
				InputStream is = Main.class
						.getClassLoader().getResourceAsStream(path.toString().substring(1));
				assert is != null;
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String line;
				while ((line = br.readLine()) != null) {
					function.append(line).append("\n");
				}
				is.close();
				isr.close();
				br.close();
				codeInput.setText(String.valueOf(function));
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}


		try {
			Path file = Paths.get(Defaults.saveDirectory + "\\GDBoard\\whisper.txt");
			if (Files.exists(file)) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					if (String.valueOf(CommandSettings.command).equals(sc.nextLine())) {
						whisper.setChecked(true);
						break;
					}
					else{
						whisper.setChecked(false);
					}
				}
				sc.close();
			}
			Path file2 = Paths.get(Defaults.saveDirectory + "\\GDBoard\\mod.txt");
			if (Files.exists(file2)) {
				Scanner sc = new Scanner(file2);
				while (sc.hasNextLine()) {
					if (String.valueOf(CommandSettings.command).equals(sc.nextLine())) {
						modOnly.setChecked(true);
						break;
					}
					else{
						modOnly.setChecked(false);
					}
				}
				sc.close();
			}
			Path file3 = Paths.get(Defaults.saveDirectory + "\\GDBoard\\disable.txt");
			if (Files.exists(file3)) {
				Scanner sc = new Scanner(file3);
				while (sc.hasNextLine()) {
					if (String.valueOf(CommandSettings.command).equals(sc.nextLine())) {
						disable.setChecked(true);
						break;
					}
					else{
						disable.setChecked(false);
					}
				}
				sc.close();
			}
			int cooldown = 0;
			boolean coolExists = false;
			if (Files.exists(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt"))) {
				Scanner sc3 = new Scanner(Paths.get(Defaults.saveDirectory + "/GDBoard/cooldown.txt").toFile());
				while (sc3.hasNextLine()) {
					String line = sc3.nextLine();
					if (line.split(" = ")[0].replace(" ", "").equalsIgnoreCase(command)) {
						coolExists = true;
						cooldown = Integer.parseInt(line.split(" = ")[1].replace(" ", ""));
						break;
					}
				}
				sc3.close();
			}
			if(!coolExists){
				InputStream is = Main.class
						.getClassLoader().getResourceAsStream("Resources/Commands/cooldown.txt");
				assert is != null;
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null) {
					if (line.split(" = ")[0].replace(" ", "").equalsIgnoreCase(command)) {
						cooldown = Integer.parseInt(line.split( " = ")[1].replace(" ", ""));
						break;
					}
				}
				is.close();
				isr.close();
				br.close();
			}

			slider.setValue(cooldown);
		}
		catch (Exception f){
			f.printStackTrace();
		}



		codeInput.setEditable(false);
		codeInput.setCaretPosition(0);
		codePanel.getVerticalScrollBar().setValue(0);
		scrollPane.setVisible(false);
		commandPanelView.setVisible(true);
	}
	public static void addButton(String command, Path path, boolean isDefault) {
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
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
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
		sliderValue.setForeground(Defaults.FOREGROUND);
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
