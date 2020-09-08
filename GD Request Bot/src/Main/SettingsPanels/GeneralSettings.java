package Main.SettingsPanels;

import Main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.io.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class GeneralSettings {
	public static boolean isChaos = false;
	public static boolean isChaosChaos = false;
	public static boolean lowCPUMode = false;
	public static boolean followersOption = false;
	public static boolean subsOption = false;
	public static boolean silentOption = false;
	public static boolean repeatedOption = false;
	public static boolean repeatedOptionAll = false;
	public static boolean updatedRepeatedOption = false;
	public static boolean nowPlayingOption = false;
	public static boolean queueFullOption = false;
	public static boolean confirmOption = false;
	public static boolean autoDownloadOption = false;
	public static boolean queueLimitBoolean = false;
	public static boolean userLimitOption = false;
	public static boolean userLimitStreamOption = false;
	public static boolean streamerBypassOption = false;
	public static boolean modsBypassOption = false;

	private static LangLabel versionLabel = new LangLabel("");
	private static CheckboxButton followers = createButton("$FOLLOWERS_ONLY$", 50);
	private static CheckboxButton subOnly = createButton("$SUBSCRIBERS_ONLY$", 80);
	private static CheckboxButton silentMode = createButton("$SILENT_MODE$", 110);

	private static CheckboxButton nowPlaying = createButton("$DISABLE_NOW_PLAYING$", 140);
	private static CheckboxButton queueFull = createButton("$DISABLE_QUEUE_FULL$", 170);
	private static CheckboxButton confirm = createButton("$DISABLE_CONFIRMATION$", 200);
	private static CheckboxButton repeated = createButton("$DISABLE_REPEATED$", 230);
	private static CheckboxButton repeatedAll = createButton("$DISABLE_REPEATED_ALL$", 260);
	private static CheckboxButton allowUpdatedRepeated = createButton("$ALLOW_UPDATED_REPEATED$", 290);

	private static CheckboxButton autoDownload = createButton("$AUTOMATIC_SONG_DOWNLOADS$", 320);
	private static CheckboxButton lowCPU = createButton("$LOW_CPU_MODE$", 350);
	private static CheckboxButton chaos = createButton("$CHAOS_MODE$", 380);
	private static CheckboxButton chaosChaos = createButton("$CHAOS_CHAOS_MODE$", 410);
	private static CheckboxButton streamerBypass = createButton("$STREAMER_BYPASS$", 440);
	private static CheckboxButton modsBypass = createButton("$MODS_BYPASS$", 470);

	private static CheckboxButton queueLimitText = createButton("$MAX_QUEUE_SIZE$", 500);
	private static CheckboxButton userLimitText = createButton("$REQUEST_LIMIT_QUEUE$", 572);
	private static CheckboxButton userLimitStreamText = createButton("$STREAM_REQUEST_LIMIT$", 647);

	public static int queueLimit = 0;
	public static int userLimit = 0;
	public static int userLimitStream = 0;
	public static int queueLevelLength = 10;


	private static FancyTextArea queueSizeInput = new FancyTextArea(true, false);
	private static FancyTextArea userLimitInput = new FancyTextArea(true, false);
	private static FancyTextArea userLimitStreamInput = new FancyTextArea(true, false);

	private static LangLabel queueCommandLabel = new LangLabel("$QUEUE_COMMAND_LABEL$");

	private static FancyTextArea queueCommandLength = new FancyTextArea(true, false);

	private static JPanel mainPanel = new JPanel(null);
	private static JPanel panel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(panel);


	public static JPanel createPanel() {


		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 800);
		panel.setPreferredSize(new Dimension(415, 800));
		panel.setBackground(Defaults.SUB_MAIN);

		InputStream is;
		try {
			is = new FileInputStream(Defaults.saveDirectory + "\\GDBoard\\version.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			versionLabel.setTextLangFormat("$GDBOARD_VERSION$", br.readLine().replaceAll("version=", ""));

		} catch (IOException e) {
			versionLabel.setTextLangFormat("$GDBOARD_VERSION$", "unknown");
		}

		versionLabel.setForeground(Defaults.FOREGROUND2);
		versionLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		versionLabel.setBounds(25,20,345,versionLabel.getPreferredSize().height+5);

		followers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				followersOption = followers.getSelectedState();
			}
		});

		subOnly.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				subsOption = subOnly.getSelectedState();
			}
		});

		silentMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				silentOption = silentMode.getSelectedState();
			}
		});

		nowPlaying.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				nowPlayingOption = nowPlaying.getSelectedState();
			}
		});
		queueFull.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				queueFullOption = queueFull.getSelectedState();
			}
		});
		confirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				confirmOption = confirm.getSelectedState();
			}
		});
		repeated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				repeatedOption = repeated.getSelectedState();
			}
		});

		repeatedAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				repeatedOptionAll = repeatedAll.getSelectedState();
			}
		});
		allowUpdatedRepeated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				updatedRepeatedOption = allowUpdatedRepeated.getSelectedState();
			}
		});

		autoDownload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				autoDownloadOption = autoDownload.getSelectedState();
			}
		});
		lowCPU.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				lowCPUMode = lowCPU.getSelectedState();
			}
		});

		chaos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				isChaos = chaos.getSelectedState();
			}
		});

		chaosChaos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				isChaosChaos = chaosChaos.getSelectedState();
			}
		});


		streamerBypass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				streamerBypassOption = streamerBypass.getSelectedState();
			}
		});
		modsBypass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				modsBypassOption = modsBypass.getSelectedState();
			}
		});



		queueLimitText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				queueLimitBoolean = queueLimitText.getSelectedState();
				if(!queueLimitBoolean){
					queueSizeInput.setEditable(false);
				}
				else{
					queueSizeInput.setEditable(true);
				}
			}
		});
		queueSizeInput.setEditable(false);
		queueSizeInput.setBounds(25,533,345, 32);
		queueSizeInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		queueSizeInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					queueLimit = Integer.parseInt(queueSizeInput.getText());
				}
				catch (NumberFormatException f){
					queueLimit = 0;
				}
				System.out.println(queueLimit);
			}
		});

		userLimitText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				userLimitOption = userLimitText.getSelectedState();
				if(!userLimitOption){
					userLimitInput.setEditable(false);
				}
				else{
					userLimitInput.setEditable(true);
				}
			}
		});

		userLimitInput.setEditable(false);
		userLimitInput.setBounds(25,605,345, 32);
		userLimitInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		userLimitInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					userLimit = Integer.parseInt(userLimitInput.getText());
				}
				catch (NumberFormatException f){
					userLimit = 0;
				}
				System.out.println(userLimit);
			}
		});

		userLimitStreamText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				userLimitStreamOption = userLimitStreamText.getSelectedState();
				if(!userLimitStreamOption){
					userLimitStreamInput.setEditable(false);
				}
				else{
					userLimitStreamInput.setEditable(true);
				}
			}
		});

		userLimitStreamInput.setEditable(false);
		userLimitStreamInput.setBounds(25,680,345, 32);
		userLimitStreamInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		userLimitStreamInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					userLimitStream = Integer.parseInt(userLimitStreamInput.getText());
				}
				catch (NumberFormatException f){
					userLimitStream = 0;
				}
			}
		});

		queueCommandLabel.setForeground(Defaults.FOREGROUND);
		queueCommandLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		queueCommandLabel.setBounds(25,728,345,queueCommandLabel.getPreferredSize().height+5);

		queueCommandLength.setText("10");
		queueCommandLength.setBounds(25,756,345, 32);
		queueCommandLength.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		queueCommandLength.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					queueLevelLength = Integer.parseInt(queueCommandLength.getText());
				}
				catch (NumberFormatException f){
					queueLevelLength = 10;
				}
			}
		});


		panel.add(followers);
		panel.add(subOnly);
		panel.add(silentMode);
		panel.add(nowPlaying);
		panel.add(queueFull);
		panel.add(confirm);
		panel.add(repeated);
		panel.add(repeatedAll);
		panel.add(allowUpdatedRepeated);
		panel.add(autoDownload);
		panel.add(lowCPU);
		panel.add(chaos);
		panel.add(chaosChaos);
		panel.add(streamerBypass);
		panel.add(modsBypass);
		panel.add(versionLabel);
		panel.add(queueLimitText);
		panel.add(queueSizeInput);
		panel.add(userLimitText);
		panel.add(userLimitInput);
		panel.add(userLimitStreamText);
		panel.add(userLimitStreamInput);
		panel.add(queueCommandLabel);
		panel.add(queueCommandLength);

		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
		scrollPane.setBounds(0, 0, 412 , 622);
		scrollPane.setPreferredSize(new Dimension(412, 532));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		mainPanel.setBounds(0,0,415, 622);
		mainPanel.add(scrollPane);
		return mainPanel;
	}

	public static void loadSettings(){
		followersOption = Boolean.parseBoolean(Settings.getSettings("followers"));
		subsOption = Boolean.parseBoolean(Settings.getSettings("subscribers"));
		silentOption = Boolean.parseBoolean(Settings.getSettings("silentMode"));
		nowPlayingOption = Boolean.parseBoolean(Settings.getSettings("disableNP"));
		queueFullOption = Boolean.parseBoolean(Settings.getSettings("disableQF"));
		confirmOption = Boolean.parseBoolean(Settings.getSettings("disableConfirm"));
		repeatedOption = Boolean.parseBoolean(Settings.getSettings("repeatedRequests"));
		repeatedOptionAll = Boolean.parseBoolean(Settings.getSettings("repeatedRequestsAll"));
		updatedRepeatedOption = Boolean.parseBoolean(Settings.getSettings("updatedRepeated"));
		lowCPUMode = Boolean.parseBoolean(Settings.getSettings("lowCPUMode"));
		isChaos = Boolean.parseBoolean(Settings.getSettings("isChaos"));
		isChaosChaos = Boolean.parseBoolean(Settings.getSettings("isChaosChaos"));
		streamerBypassOption = Boolean.parseBoolean(Settings.getSettings("streamerBypass"));
		modsBypassOption = Boolean.parseBoolean(Settings.getSettings("modsBypass"));

		autoDownloadOption = Boolean.parseBoolean(Settings.getSettings("autoDL"));
		queueLimitBoolean = Boolean.parseBoolean(Settings.getSettings("queueLimitEnabled"));
		if(!Settings.getSettings("queueLimit").equalsIgnoreCase("")) {
			queueLimit = Integer.parseInt(Settings.getSettings("queueLimit"));
			queueSizeInput.setText(String.valueOf(queueLimit));
		}
		userLimitOption = Boolean.parseBoolean(Settings.getSettings("userLimitEnabled"));
		if(!Settings.getSettings("userLimit").equalsIgnoreCase("")) {
			userLimit = Integer.parseInt(Settings.getSettings("userLimit"));
			userLimitInput.setText(String.valueOf(userLimit));
		}
		userLimitStreamOption = Boolean.parseBoolean(Settings.getSettings("userLimitStreamEnabled"));
		if(!Settings.getSettings("userLimitStream").equalsIgnoreCase("")) {
			userLimitStream = Integer.parseInt(Settings.getSettings("userLimitStream"));
			userLimitStreamInput.setText(String.valueOf(userLimitStream));
		}
		if(!Settings.getSettings("queueLevelLength").equalsIgnoreCase("")) {
			queueLevelLength = Integer.parseInt(Settings.getSettings("queueLevelLength"));
			queueCommandLength.setText(String.valueOf(queueLevelLength));
		}
		followers.setChecked(followersOption);
		silentMode.setChecked(silentOption);
		nowPlaying.setChecked(nowPlayingOption);
		queueFull.setChecked(queueFullOption);
		confirm.setChecked(confirmOption);

		confirm.setChecked(confirmOption);
		subOnly.setChecked(subsOption);
		repeated.setChecked(repeatedOption);
		repeatedAll.setChecked(repeatedOptionAll);
		allowUpdatedRepeated.setChecked(updatedRepeatedOption);
		autoDownload.setChecked(autoDownloadOption);
		queueLimitText.setChecked(queueLimitBoolean);
		userLimitText.setChecked(userLimitOption);
		userLimitStreamText.setChecked(userLimitStreamOption);
		lowCPU.setChecked(lowCPUMode);
		chaos.setChecked(isChaos);
		streamerBypass.setChecked(streamerBypassOption);
		modsBypass.setChecked(modsBypassOption);

		if(!queueLimitBoolean){
			queueSizeInput.setEditable(false);
		}
		else{
			queueSizeInput.setEditable(true);
		}
		if(!userLimitOption){
			userLimitInput.setEditable(false);
		}
		else{
			userLimitInput.setEditable(true);
		}
		if(!userLimitStreamOption){
			userLimitStreamInput.setEditable(false);
		}
		else{
			userLimitStreamInput.setEditable(true);
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("followers", String.valueOf(followersOption));
			Settings.writeSettings("subscribers", String.valueOf(subsOption));
			Settings.writeSettings("silentMode", String.valueOf(silentOption));
			Settings.writeSettings("disableNP", String.valueOf(nowPlayingOption));
			Settings.writeSettings("disableQF", String.valueOf(queueFullOption));
			Settings.writeSettings("disableConfirm", String.valueOf(confirmOption));
			Settings.writeSettings("autoDL", String.valueOf(autoDownloadOption));
			Settings.writeSettings("queueLimitEnabled", String.valueOf(queueLimitBoolean));
			Settings.writeSettings("queueLimit", String.valueOf(queueLimit));
			Settings.writeSettings("userLimitEnabled", String.valueOf(userLimitOption));
			Settings.writeSettings("userLimit", String.valueOf(userLimit));
			Settings.writeSettings("userLimitStreamEnabled", String.valueOf(userLimitStreamOption));
			Settings.writeSettings("userLimitStream", String.valueOf(userLimitStream));
			Settings.writeSettings("repeatedRequests", String.valueOf(repeatedOption));
			Settings.writeSettings("repeatedRequestsAll", String.valueOf(repeatedOptionAll));
			Settings.writeSettings("updatedRepeated", String.valueOf(updatedRepeatedOption));
			Settings.writeSettings("lowCPUMode", String.valueOf(lowCPUMode));
			Settings.writeSettings("isChaos", String.valueOf(isChaos));
			Settings.writeSettings("isChaosChaos", String.valueOf(isChaosChaos));
			Settings.writeSettings("streamerBypass", String.valueOf(streamerBypassOption));
			Settings.writeSettings("modsBypass", String.valueOf(modsBypassOption));
			Settings.writeSettings("queueLevelLength", String.valueOf(queueLevelLength));



		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error writing to the file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
	}

	@SuppressWarnings("unused")
	private static JLabel createLabel(String text, int y){
		JLabel label = new JLabel(text);
		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		label.setBounds(25,y,label.getPreferredSize().width + 5,30);
		label.setForeground(Defaults.FOREGROUND);

		return label;
	}
	private static CheckboxButton createButton(String text, int y){

		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,345,30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.refresh();
		return button;
	}
	public static void refreshUI() {
		panel.setBackground(Defaults.SUB_MAIN);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());

		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel && !(((LangLabel) component).getIdentifier().equals("GDBOARD_VERSION"))) {
				component.setForeground(Defaults.FOREGROUND);

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
