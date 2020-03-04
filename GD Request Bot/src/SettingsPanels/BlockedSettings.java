package SettingsPanels;

import Main.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

import javax.swing.*;

public class BlockedSettings {
	private static JPanel panel = new JPanel();

    public static JPanel createPanel() {

        panel.setDoubleBuffered(true);
        panel.setBounds(0, 0, 415, 622);
        panel.setBackground(Defaults.SUB_MAIN);
        loadIDs();
        return panel;

    }
    public static void loadIDs(){
    	for (Component component : panel.getComponents()){
    		if (component instanceof JButton){
    			panel.remove(component);
			}
		}
		File file = new File(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");
		if (file.exists()) {
			Scanner sc = null;
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			assert sc != null;
			while (sc.hasNextLine()) {
				System.out.println("test");
				JButtonUI defaultUI = new JButtonUI();
				defaultUI.setBackground(Defaults.BUTTON);
				defaultUI.setHover(Defaults.HOVER);
				defaultUI.setSelect(Defaults.SELECT);
				JButton button = new JButton(sc.nextLine());

				button.setBackground(Defaults.BUTTON);
				button.setUI(defaultUI);
				button.setForeground(Defaults.FOREGROUND);
				button.setBorder(BorderFactory.createEmptyBorder());
				button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
				button.setPreferredSize(new Dimension(100, 30));
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						Object[] options = {"Yes", "No"};
						int n = JOptionPane.showOptionDialog(Overlay.frame,
								"Unblock " + button.getText() + "?",
								"Unblock ID? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
						if (n == 0) {
							if (file.exists()) {
								try {
									System.out.println("test");
									File temp = new File(System.getenv("APPDATA") + "\\GDBoard\\_temp_");
									PrintWriter out = new PrintWriter(new FileWriter(temp));
									Files.lines(file.toPath())
											.filter(line -> !line.contains(button.getText()))
											.forEach(out::println);
									out.flush();
									out.close();
									file.delete();
									temp.renameTo(file);

								} catch (IOException ex) {
									ex.printStackTrace();
								}
							}
							panel.remove(button);
							panel.updateUI();
						}
					}
				});
				panel.add(button);

			}
			sc.close();
		}
	}
}
