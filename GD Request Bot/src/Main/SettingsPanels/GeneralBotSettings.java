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
	public static boolean multiOption = false;
	public static boolean antiDox = true;

	private static JPanel panel = new JPanel(null);
	private static LangLabel versionLabel = new LangLabel("");

	private static CheckboxButton silentChatMode = createButton("$SILENT_MODE$", 50);
	private static CheckboxButton multiThreadMode = createButton("$MULTI_THREAD$", 80);
	private static CheckboxButton antiDoxMode = createButton("$ANTI_DOX$", 140);
	private static LangLabel antiDoxInfo = new LangLabel("<html> $ANTI_DOX_INFO$ </html>");
	private static LangLabel multiThreadInfo = new LangLabel("<html> $MULTI_THREAD_INFO$ </html>");


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


		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		panel.add(versionLabel);
		panel.add(silentChatMode);
		panel.add(multiThreadMode);
		panel.add(antiDoxMode);
		panel.add(antiDoxInfo);
		panel.add(multiThreadInfo);
		return panel;
		
	}
	private static CheckboxButton createButton(String text, int y){
		CheckboxButton button = new CheckboxButton(text);
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
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("silentMode", String.valueOf(silentOption));
			Settings.writeSettings("multiMode", String.valueOf(multiOption));
			Settings.writeSettings("antiDox", String.valueOf(antiDox));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void refreshUI() {
		panel.setBackground(Defaults.SUB_MAIN);
		versionLabel.setForeground(Defaults.FOREGROUND2);
		antiDoxInfo.setForeground(Defaults.FOREGROUND2);
		multiThreadInfo.setForeground(Defaults.FOREGROUND2);
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
