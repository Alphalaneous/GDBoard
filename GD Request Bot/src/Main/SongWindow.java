package Main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SongWindow {
	private static int height = 85;
	private static int width = 300;
	private static JPanel window = new InnerWindow("Music - Newgrounds Audio", 1920 - width - 10, 600, width, height, "src/resources/Icons/note.png").createPanel();
	private static JPanel panel = new JPanel();
	private static JLabel songName = new JLabel();
	private static JLabel songAuthor = new JLabel();
	private static JLabel songID = new JLabel();
	private static JButtonUI defaultUI = new JButtonUI();

	static void createPanel() {
		ArrayList<PlaySong> songs = new ArrayList<PlaySong>();
		
		panel.setBounds(1, 31, width, height);
		panel.setBackground(Defaults.MAIN);
		panel.setLayout(null);
		
		
		
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		JButton play = new RoundedJButton("Play");
		play.setPreferredSize(new Dimension(50, 50));
		play.setUI(defaultUI);
		play.setBounds(width - 110, height - 55, 50,50);
		play.setBackground(Defaults.BUTTON);
		play.setForeground(Defaults.FOREGROUND);
		play.setBorder(BorderFactory.createEmptyBorder());
		play.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		play.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (Requests.levels.size() != 0) {
					if (songs.size() > 0) {
						songs.get(0).stop();
						songs.clear();
					}
					songs.add(new PlaySong());
					songs.get(0).setSong(Requests.levels.get(LevelsWindow2.getSelectedID()).getSongID());
					songs.get(0).start();
				}
			}
		});
		JButton stop = new RoundedJButton("Stop");
		stop.setPreferredSize(new Dimension(50, 50));
		stop.setUI(defaultUI);
		stop.setBounds(width - 55, height - 55, 50,50);
		stop.setBackground(Defaults.BUTTON);
		stop.setForeground(Defaults.FOREGROUND);
		stop.setBorder(BorderFactory.createEmptyBorder());
		stop.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		stop.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				if (songs.size() > 0) {
					songs.get(0).stop();
					songs.clear();
				}
			}
		});
		
		songName.setFont(new Font("bahnschrift", Font.PLAIN, 20));
		songName.setBounds(10, 5, width, 30);
		songName.setForeground(Defaults.FOREGROUND);
		
		songAuthor.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		songAuthor.setBounds(width - songAuthor.getPreferredSize().width - 10, 5, width, 20);
		songAuthor.setForeground(Defaults.FOREGROUND);
		
		songID.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		songID.setBounds(10, height - 30, width, 20);
		songID.setForeground(Defaults.FOREGROUND);
		
		
		panel.add(songName);
		panel.add(songAuthor);
		panel.add(songID);
		panel.add(play);
		panel.add(stop);
		window.add(panel);
		((InnerWindow)window).refreshListener();
		
		
		Overlay.addToFrame(window);
	}
	static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			songName.setText("N/A");
			songAuthor.setText("N/A");
			songAuthor.setBounds(width - songAuthor.getPreferredSize().width - 10, 5, width, 20);
			songID.setText("N/A");
			
		} else {
			songName.setText(Requests.levels.get(LevelsWindow2.getSelectedID()).getSongName());
			songAuthor.setText(Requests.levels.get(LevelsWindow2.getSelectedID()).getSongAuthor());
			songAuthor.setBounds(width - songAuthor.getPreferredSize().width - 10, 5, width, 20);
			songID.setText(Requests.levels.get(LevelsWindow2.getSelectedID()).getSongID());
			
		}

	}
	
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		panel.setBackground(Defaults.MAIN);
		songName.setForeground(Defaults.FOREGROUND);
		songAuthor.setForeground(Defaults.FOREGROUND);
		songID.setForeground(Defaults.FOREGROUND);
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

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}
