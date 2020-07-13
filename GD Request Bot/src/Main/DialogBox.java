package Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class DialogBox {

	private static JButtonUI defaultUI = new JButtonUI();
	private static boolean active = false;
	private static JDialog frame = null;

	public static String showDialogBox(String title, String info, String subInfo, String[] options){
		final String[] value = {null};


		if(!active) {
			active = true;

			frame = new JDialog();
			JPanel textPanel = new JPanel();
			JPanel titlePanel = new JPanel();
			textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
			titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
			JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 6, 6));
			frame.setUndecorated(true);
			frame.setLayout(null);
			JLabel titleLabel = new JLabel(title);
			JLabel infoLabel = new JLabel(info);
			JLabel subInfoLabel = new JLabel(subInfo);

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

			if(Defaults.loaded.get()) {
				defaultUI.setBackground(Defaults.BUTTON);
				defaultUI.setHover(Defaults.BUTTON_HOVER);
				defaultUI.setSelect(Defaults.SELECT);
				frame.getRootPane().setBorder(new LineBorder(Defaults.ACCENT, 1));
				frame.getContentPane().setBackground(Defaults.TOP);
				titleLabel.setForeground(Defaults.FOREGROUND);
				infoLabel.setForeground(Defaults.FOREGROUND);
				subInfoLabel.setForeground(Defaults.FOREGROUND);
				frame.setLocation((int) (Defaults.screenSize.getX() + Defaults.screenSize.getWidth() / 2 - 200), (int) (Defaults.screenSize.getY() + Defaults.screenSize.getHeight() / 2 - 100));

			}
			else{
				defaultUI.setBackground(new Color(50, 50, 50));
				defaultUI.setHover(new Color(80, 80, 80));
				defaultUI.setSelect(new Color(70, 70, 70));
				frame.getRootPane().setBorder(new LineBorder(new Color(0, 108, 230), 1));
				frame.getContentPane().setBackground(Color.BLACK);
				titleLabel.setForeground(Color.WHITE);
				infoLabel.setForeground(Color.WHITE);
				subInfoLabel.setForeground(Color.WHITE);
				Rectangle screenSize;
				try {
					 screenSize = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getScreenDevices()[Integer.parseInt(Settings.getSettings("monitor"))].getDefaultConfiguration().getBounds();
				} catch (IOException e) {
					screenSize = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getScreenDevices()[0].getDefaultConfiguration().getBounds();
				}
				frame.setLocation((int) (screenSize.getX() + screenSize.getWidth() / 2 - 200), (int) (screenSize.getY() + screenSize.getHeight() / 2 - 100));

			}


			for (int i = 0; i < options.length; i++) {
				JButton button = createButton(options[i]);
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						value[0] = button.getText();
					}
				});
				buttonPanel.add(button);
			}



			frame.setResizable(false);
			frame.add(titlePanel);
			frame.add(textPanel);
			frame.add(buttonPanel);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);

			while (value[0] == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			frame.setVisible(false);
			frame.dispose();
			active = false;
			return value[0];
		}
		else {
			frame.requestFocus();
		}
		return "";
	}
	private static JButton createButton(String text) {

		JButton button = new JButton(text);

		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		button.setUI(defaultUI);

		if(Defaults.loaded.get()){
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
		frame.dispose();
		active = false;
	}
}
