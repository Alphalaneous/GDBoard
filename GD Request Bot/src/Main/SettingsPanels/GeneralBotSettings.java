package Main.SettingsPanels;

import Main.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Set;

import static Main.Defaults.settingsButtonUI;

public class GeneralBotSettings {
	public static boolean silentOption = false;
	private static JPanel panel = new JPanel(null);
	private static LangLabel versionLabel = new LangLabel("");

	private static CheckboxButton silentChatMode = createButton("$SILENT_MODE$", 50);
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
		versionLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		versionLabel.setBounds(25,20,345,versionLabel.getPreferredSize().height+5);
		silentChatMode.setChecked(false);
		silentChatMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				silentOption = silentChatMode.getSelectedState();
			}
		});


		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		panel.add(versionLabel);
		panel.add(silentChatMode);
		return panel;
		
	}
	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.refresh();
		return button;
	}
	public static void loadSettings(){

		if(!Settings.getSettings("silentMode").equalsIgnoreCase("")) {
			silentOption = Boolean.parseBoolean(Settings.getSettings("silentMode"));
			silentChatMode.setChecked(silentOption);
			Windowed.setOnTop(silentOption);
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("silentMode", String.valueOf(silentOption));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void refreshUI() {
		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}
		}
	}
	public static void destroyPanels(){
		try {
			Reflections innerReflections = new Reflections("Main.InnerWindows", new SubTypesScanner(false));
			Set<Class<?>> innerClasses =
					innerReflections.getSubTypesOf(Object.class);
			for (Class<?> Class : innerClasses) {
				Method method = Class.getMethod("destroyPanel");
				method.invoke(null);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
