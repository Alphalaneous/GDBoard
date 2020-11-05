package Main.SettingsPanels;

import Main.*;
import Main.Components.CheckboxButton;
import Main.Components.FancyTextArea;
import Main.Components.LangLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class GeneralBotSettings {
	public static boolean silentOption = false;
	public static boolean multiOption = false;
	public static boolean antiDox = true;
	public static int cooldown = 0;
	private static JPanel panel = new JPanel(null);
	private static LangLabel versionLabel = new LangLabel("");

	private static CheckboxButton silentChatMode = createButton("$SILENT_MODE$", 50);
	private static CheckboxButton multiThreadMode = createButton("$MULTI_THREAD$", 80);
	private static CheckboxButton antiDoxMode = createButton("$ANTI_DOX$", 140);
	private static LangLabel antiDoxInfo = new LangLabel("<html> $ANTI_DOX_INFO$ </html>");
	private static LangLabel multiThreadInfo = new LangLabel("<html> $MULTI_THREAD_INFO$ </html>");
	private static LangLabel cooldownLabel = new LangLabel("$GLOBAL_COOLDOWN_LABEL$");

	private static FancyTextArea globalCooldownInput = new FancyTextArea(true, false);


	public static JPanel createPanel() {

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
		versionLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		versionLabel.setBounds(25,20,345,40);

		antiDoxInfo.setForeground(Defaults.FOREGROUND2);
		antiDoxInfo.setFont(Defaults.SEGOE.deriveFont(12f));
		antiDoxInfo.setBounds(25,165,345,40);

		multiThreadInfo.setForeground(Defaults.FOREGROUND2);
		multiThreadInfo.setFont(Defaults.SEGOE.deriveFont(12f));
		multiThreadInfo.setBounds(25,105,345,40);

		cooldownLabel.setForeground(Defaults.FOREGROUND);
		cooldownLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		cooldownLabel.setBounds(25,200,345,40);

		silentChatMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				silentOption = silentChatMode.getSelectedState();
			}
		});
		multiThreadMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				multiOption = multiThreadMode.getSelectedState();
			}
		});
		antiDoxMode.setChecked(true);
		antiDoxMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				antiDox = antiDoxMode.getSelectedState();
			}
		});
		globalCooldownInput.setBounds(25,235,365, 32);
		globalCooldownInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		globalCooldownInput.setText("0");
		globalCooldownInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					cooldown = Integer.parseInt(globalCooldownInput.getText());
				}
				catch (NumberFormatException f){
					cooldown = 0;
				}
			}
		});

		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		panel.add(versionLabel);
		panel.add(silentChatMode);
		panel.add(multiThreadMode);
		panel.add(antiDoxMode);
		panel.add(antiDoxInfo);
		panel.add(multiThreadInfo);
		panel.add(globalCooldownInput);
		panel.add(cooldownLabel);
		return panel;
		
	}
	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text, GeneralBotSettings.class);
		button.setBounds(25,y,365,30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SEGOE.deriveFont(14f));
		button.refresh();
		return button;
	}
	public static void loadSettings(){

		if(!Settings.getSettings("silentMode").equalsIgnoreCase("")) {
			silentOption = Boolean.parseBoolean(Settings.getSettings("silentMode"));
			silentChatMode.setChecked(silentOption);
		}
		if(!Settings.getSettings("antiDox").equalsIgnoreCase("")) {
			antiDox = Boolean.parseBoolean(Settings.getSettings("antiDox"));
			antiDoxMode.setChecked(antiDox);
		}
		if(!Settings.getSettings("multiMode").equalsIgnoreCase("")) {
			multiOption = Boolean.parseBoolean(Settings.getSettings("multiMode"));
			multiThreadMode.setChecked(multiOption);
		}
		if(!Settings.getSettings("globalCooldown").equalsIgnoreCase("")) {
			cooldown = Integer.parseInt(Settings.getSettings("globalCooldown"));
			globalCooldownInput.setText(String.valueOf(cooldown));
		}
	}
	public static void setSettings(){

			Settings.writeSettings("silentMode", String.valueOf(silentOption));
			Settings.writeSettings("multiMode", String.valueOf(multiOption));
			Settings.writeSettings("antiDox", String.valueOf(antiDox));
			Settings.writeSettings("globalCooldown", String.valueOf(cooldown));

	}

	public static void refreshUI() {
		panel.setBackground(Defaults.SUB_MAIN);
		versionLabel.setForeground(Defaults.FOREGROUND2);
		antiDoxInfo.setForeground(Defaults.FOREGROUND2);
		multiThreadInfo.setForeground(Defaults.FOREGROUND2);
		globalCooldownInput.refreshAll();
		cooldownLabel.setForeground(Defaults.FOREGROUND);

		for (Component component : panel.getComponents()) {
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
}
