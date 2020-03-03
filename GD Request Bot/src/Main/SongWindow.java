package Main;

import com.jidesoft.swing.ResizablePanel;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class SongWindow {
	private static int height = 90;
	private static int width = 300;
	private static ResizablePanel window = new InnerWindow("Music - Newgrounds Audio", Settings.getSongWLoc().x, Settings.getSongWLoc().y, width, height, "\uEC4F").createPanel();
	private static JPanel panel = new JPanel();
	private static JLabel songName = new JLabel();
	private static JLabel songAuthorID = new JLabel();
	private static JButtonUI defaultUI = new JButtonUI();
	private static JButton play;
	private static JButton stop;
	private static CurvedButton persist = new CurvedButton("Make music persist?");

	static void createPanel() {
		panel.setBounds(1, 31, width, height);
		panel.setBackground(Defaults.MAIN);
		panel.setLayout(null);
		final Thread[] thread = new Thread[1];
		
		
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);

		//TODO Song persistence button

		play = createButton("\uF5B0", 110);
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
							BufferedInputStream inp;
							inp = new BufferedInputStream(new URL(Requests.levels.get(LevelsWindow.getSelectedID()).getSongURL()).openStream());
							mp3player = new Player(inp);
							mp3player.play();
						} catch (IOException | JavaLayerException | NullPointerException f) {
							f.printStackTrace();
						}
					});
					thread[0].start();
				}
			}
		});

		stop = createButton("\uE009", 55);
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
				if(!Requests.levels.get(LevelsWindow.getSelectedID()).getPersist()) {
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
		persist.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		persist.refresh();
		persist.setVisible(false);

		songName.setFont(new Font("bahnschrift", Font.PLAIN, 20));
		songName.setBounds(10, 7, width, 30);
		songName.setForeground(Defaults.FOREGROUND);

		songAuthorID.setFont(new Font("bahnschrift", Font.PLAIN, 14));
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
	}
	static void setPin(boolean pin){
		((InnerWindow) window).setPin(pin);
	}
	private static RoundedJButton createButton(String icon, int i) {
		RoundedJButton button = new RoundedJButton(icon);
		button.setPreferredSize(new Dimension(50, 50));
		button.setUI(defaultUI);
		button.setBounds(width - i, height - 55, 50, 50);
		button.setBackground(Defaults.BUTTON);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
		return button;
	}

	static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			songName.setText("N/A");
			songAuthorID.setText("N/A");
			stop.setVisible(false);
			play.setVisible(false);
			
		} else {
			songName.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getSongName());

			if(songName.getText().equalsIgnoreCase("Custom") && Requests.levels.get(LevelsWindow.getSelectedID()).getSongAuthor().equalsIgnoreCase("")){
				songAuthorID.setText("");
				stop.setVisible(false);
				play.setVisible(false);
				persist.setVisible(true);
			}
			else{
				songAuthorID.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getSongAuthor() + " (" + Requests.levels.get(LevelsWindow.getSelectedID()).getSongID() + ")");
				play.setVisible(true);
				stop.setVisible(true);
				persist.setVisible(false);
			}
		}
	}
	
	static void refreshUI() {
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

	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	
	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	//region SetLocation
	static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}

