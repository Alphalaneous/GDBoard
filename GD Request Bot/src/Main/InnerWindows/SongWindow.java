package Main.InnerWindows;

import Main.*;
import com.jidesoft.swing.ResizablePanel;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

public class SongWindow {
	private static int height = 90;
	private static int width = 300;
	private static ResizablePanel window = new InnerWindow("Music - Newgrounds Audio", Settings.getSongWLoc().x, Settings.getSongWLoc().y, width, height, "\uEC4F", false).createPanel();
	private static JPanel panel = new JPanel();
	private static JLabel songName = new JLabel();
	private static JLabel songAuthorID = new JLabel();
	private static JButtonUI defaultUI = new JButtonUI();
	private static JButton play;
	private static JButton stop;
	private static CurvedButton persist = new CurvedButton("Make music persist?");

	public static void createPanel() {
		panel.setBounds(1, 31, width, height);
		panel.setBackground(Defaults.MAIN);
		panel.setLayout(null);
		final Thread[] thread = new Thread[1];
		
		
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);

		play = createButton("\uE768", 110, "Play");
		play.addMouseListener(new MouseAdapter() {
			@Override
			@SuppressWarnings("deprecation")
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();

				if (Requests.levels.size() != 0) {

					if(thread[0] != null) {
						thread[0].stop();
					}
					thread[0] = new Thread(() -> {
						try {
							Player mp3player;
							BufferedInputStream inp = new BufferedInputStream(Requests.levels.get(LevelsWindow.getSelectedID()).getSongURL().openStream());
							mp3player = new Player(inp);
							mp3player.play();
						} catch (IOException | JavaLayerException | NullPointerException f) {
							JOptionPane.showMessageDialog(null, "There was an error playing the music!", "Error",  JOptionPane.ERROR_MESSAGE);
						}
					});
					thread[0].start();
				}
			}
		});

		stop = createButton("\uE15B", 55, "Stop");
		stop.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				if (thread[0].isAlive()) {
					thread[0].stop();
				}
			}
		});
		persist.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(Requests.levels.get(LevelsWindow.getSelectedID()).getNotPersist()) {
					persist.setLText("Remove persist?");
					Requests.levels.get(LevelsWindow.getSelectedID()).setPersist(true);
				}
				else{
					persist.setLText("Make music persist?");
					Requests.levels.get(LevelsWindow.getSelectedID()).setPersist(false);
				}
			}
		});
		persist.setBounds(20,height-40,260,30);
		persist.setBackground(Defaults.BUTTON);
		persist.setUI(defaultUI);
		persist.setForeground(Defaults.FOREGROUND);
		persist.setBorder(BorderFactory.createEmptyBorder());
		persist.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		persist.refresh();
		persist.setVisible(false);

		songName.setFont(Defaults.MAIN_FONT.deriveFont(20f));
		songName.setBounds(10, 7, width, 30);
		songName.setForeground(Defaults.FOREGROUND);

		songAuthorID.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		songAuthorID.setBounds(10, height - 28, width/2, 20);
		songAuthorID.setForeground(Defaults.FOREGROUND);
		
		panel.add(persist);
		panel.add(songName);
		panel.add(songAuthorID);
		panel.add(play);
		panel.add(stop);
		window.add(panel);
		((InnerWindow)window).refreshListener();
		
		
		Overlay.addToFrame(window);
		refreshInfo();
	}
	public static void setPin(boolean pin){
		((InnerWindow) window).setPin(pin);
	}
	private static RoundedJButton createButton(String icon, int i, String tooltip) {
		RoundedJButton button = new RoundedJButton(icon, tooltip);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		button.setBounds(width - i, height - 55, 50, 50);
		button.setBackground(Defaults.BUTTON);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.SYMBOLS.deriveFont(20f));
		return button;
	}
	public String getName(){
		return "Music";
	}
	public String getIcon(){
		return "\uEC4F";
	}
	public static void refreshInfo() {
		if(!Settings.windowedMode) {
			if (Requests.levels.size() == 0) {
				songName.setText("N/A");
				songAuthorID.setText("N/A");
				stop.setVisible(false);
				play.setVisible(false);
				persist.setVisible(false);
			} else {
				songName.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getSongName());

				if (songName.getText().equalsIgnoreCase("Custom") && Requests.levels.get(LevelsWindow.getSelectedID()).getSongAuthor().equalsIgnoreCase("")) {
					songAuthorID.setText("");
					stop.setVisible(false);
					play.setVisible(false);
					persist.setVisible(true);
				} else {
					songAuthorID.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getSongAuthor() + " (" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ")");
					play.setVisible(true);
					stop.setVisible(true);
					persist.setVisible(false);
				}
			}
		}
	}
	
	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		panel.setBackground(Defaults.MAIN);
		songName.setForeground(Defaults.FOREGROUND);
		songAuthorID.setForeground(Defaults.FOREGROUND);
		for(Component component : panel.getComponents())
			if(component instanceof JButton) {
				component.setBackground(Defaults.BUTTON);
				component.setForeground(Defaults.FOREGROUND);
			}
	}

	public static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	
	public static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	//region SetLocation
	public static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	public static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion

	public static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}

