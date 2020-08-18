package Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import static Main.Defaults.defaultUI;

public class DialogBox {

	private static boolean active = false;
	private static JDialog frame = null;
	private static boolean progressBar = false;
	private static JProgressBar loadingBar;
	private static boolean setFocus = true;

	public static String showDialogBox(String title, String info, String subInfo, String[] options){
		progressBar = false;
		return showDialogBox(title,info,subInfo,options,false, new Object[]{});
	}
	public static String showDialogBox(String title, String info, String subInfo, String[] options, Object[] args){
		progressBar = false;
		return showDialogBox(title,info,subInfo,options,false, args);
	}
	public static void setUnfocusable(){
		setFocus = false;
	}
	public static String showDialogBox(String title, String info, String subInfo, String[] options, boolean progressBar, Object[] args){
		final String[] value = {null};
		DialogBox.progressBar = progressBar;

		if(!active) {
			active = true;
			frame = new JDialog();
			frame.setFocusableWindowState(setFocus);
			frame.setFocusable(setFocus);
			JPanel textPanel = new JPanel();
			JPanel titlePanel = new JPanel();
			textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
			titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
			JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 6, 6));
			frame.setUndecorated(true);
			frame.setLayout(null);
			LangLabel titleLabel = new LangLabel("");
			titleLabel.setTextLangFormat(title, args);
			LangLabel infoLabel = new LangLabel(info);
			LangLabel subInfoLabel = new LangLabel(subInfo);

			JDialog finalFrame = frame;
			MouseInputAdapter mia = new MouseInputAdapter() {
				Point location;
				Point pressed;

				public void mousePressed(MouseEvent me) {
					pressed = me.getLocationOnScreen();
					location = finalFrame.getLocation();
				}

				public void mouseDragged(MouseEvent me) {
					Point dragged = me.getLocationOnScreen();
					int x = (int) (location.getX() + dragged.getX() - pressed.getX());
					int y = (int) (location.getY() + dragged.getY() - pressed.getY());
					finalFrame.setLocation(x, y);
				}
			};
			frame.getContentPane().addMouseListener(mia);
			frame.getContentPane().addMouseMotionListener(mia);

			titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			subInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));


			titlePanel.setBounds(30, 25, 340, 110);
			titlePanel.setBackground(new Color(0, 0, 0, 0));

			textPanel.setBounds(30, 70, 340, 110);
			textPanel.setBackground(new Color(0, 0, 0, 0));

			buttonPanel.setBounds(30, 140, 340, 35);
			buttonPanel.setBackground(new Color(0, 0, 0, 0));


			titlePanel.add(titleLabel);
			textPanel.add(infoLabel);
			textPanel.add(subInfoLabel);

			frame.setSize(new Dimension(400, 200));
			frame.setPreferredSize(new Dimension(400, 400));



				frame.getRootPane().setBorder(new LineBorder(Defaults.ACCENT, 1));
				frame.getContentPane().setBackground(Defaults.TOP);
				titleLabel.setForeground(Defaults.FOREGROUND);
				infoLabel.setForeground(Defaults.FOREGROUND);
				subInfoLabel.setForeground(Defaults.FOREGROUND);
				frame.setLocation((int) (Defaults.screenSize.getX() + Defaults.screenSize.getWidth() / 2 - 200), (int) (Defaults.screenSize.getY() + Defaults.screenSize.getHeight() / 2 - 100));



			if(progressBar){
				loadingBar = new JProgressBar();
				loadingBar.setValue(0);
				loadingBar.setStringPainted(false);
				loadingBar.setBounds(35,110,330,2);
				loadingBar.setForeground(new Color(0, 255, 12).darker());
				loadingBar.setBorderPainted(false);
				loadingBar.setBorder(BorderFactory.createEmptyBorder());
				loadingBar.setVisible(true);
			}
			else{
				loadingBar.setVisible(false);
			}

			for (int i = 0; i < options.length; i++) {
				LangButton button = createButton(options[i]);
				button.setForeground(Defaults.FOREGROUND);
				button.setBackground(Defaults.MAIN);
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						value[0] = button.getIdentifier();
					}
				});
				buttonPanel.add(button);
			}



			frame.setResizable(false);
			frame.add(titlePanel);
			frame.add(textPanel);
			frame.add(loadingBar);
			frame.add(buttonPanel);
			frame.setAlwaysOnTop(true);
			frame.invalidate();
			frame.revalidate();
			frame.setVisible(true);

			while (value[0] == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			frame.setVisible(false);
			frame.removeAll();
			frame.dispose();
			active = false;
			setFocus = true;
			return value[0];
		}
		else {
			frame.requestFocus();
		}
		return "";
	}
	private static LangButton createButton(String text) {

		LangButton button = new LangButton(text);

		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		button.setUI(defaultUI);

		if(Defaults.programLoaded.get()){
			button.setForeground(Defaults.FOREGROUND);
			button.setBackground(Defaults.BUTTON);

		}
		else{
			button.setForeground(Color.WHITE);
			button.setBackground(new Color(50, 50, 50));
		}

		button.setBorder(BorderFactory.createEmptyBorder());

		return button;
	}
	public static void closeDialogBox(){
		frame.setVisible(false);
		frame.removeAll();
		frame.dispose();
		active = false;
		setFocus = true;

	}
	public static void setProgress(int progress){
		if(progressBar){
			if(loadingBar != null) {
				loadingBar.setValue(progress);
			}
		}
	}
}
