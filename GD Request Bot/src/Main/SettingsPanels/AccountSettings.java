package Main.SettingsPanels;

import Main.*;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.exception.GDLoginFailedException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Base64;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import static Main.Defaults.settingsButtonUI;

public class AccountSettings {
	private static LangLabel channelText = new LangLabel("");
	private static LangLabel geometryText = new LangLabel("");
	private static RoundedJButton refreshTwitch = new RoundedJButton("\uE149", "$REFRESH_TWITCH$");
	private static RoundedJButton refreshGD = new RoundedJButton("\uE149", "$REFRESH_GD$");
	private static JPanel panel = new JPanel();

	private static JFrame logon = new JFrame();
	private static LangLabel usernameLabel = new LangLabel("$USERNAME$");
	private static LangLabel passwordLabel = new LangLabel("$PASSWORD$");
	private static LangLabel disclaimerLabel = new LangLabel("$DISCLAIMER$");

	private static FancyTextArea usernameInput = new FancyTextArea(false, false);
	private static FancyPasswordField passwordInput = new FancyPasswordField();

	private static CurvedButton loginButton = new CurvedButton("$LOGIN$");
	private static CurvedButton cancelButton = new CurvedButton("$CANCEL$");

	private static Color red = new Color(255,0,0);
	public static JPanel createPanel() {

		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		panel.setLayout(null);

		channelText.setTextLangFormat("$TWITCH$", Settings.getSettings("channel"));
		geometryText.setTextLangFormat("$GEOMETRY_DASH$","NA");
		channelText.setForeground(Defaults.FOREGROUND);
		channelText.setFont(Defaults.SEGOE.deriveFont(14f));
		channelText.setBounds(25,50,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);
		geometryText.setForeground(Defaults.FOREGROUND);
		geometryText.setFont(Defaults.SEGOE.deriveFont(14f));
		geometryText.setBounds(25,20,geometryText.getPreferredSize().width+5,geometryText.getPreferredSize().height+5);


		refreshTwitch.setBackground(Defaults.BUTTON);
		refreshTwitch.setBounds(365,48,25,25);
		refreshTwitch.setPreferredSize(new Dimension(25,25));
		refreshTwitch.setUI(settingsButtonUI);
		refreshTwitch.setForeground(Defaults.FOREGROUND);
		refreshTwitch.setBorder(BorderFactory.createEmptyBorder());
		refreshTwitch.setFont(Defaults.SYMBOLS.deriveFont(14f));
		refreshTwitch.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Thread thread = new Thread(() -> {
					try {
						APIs.setOauth(false);
					}
					catch (Exception ignored){
					}
				});
				thread.start();
			}
		});

		logon.setSize(500,270);
		logon.setResizable(false);
		logon.setTitle("Log into GD");
		logon.setIconImage(Assets.GDBoard.getImage());
		logon.getContentPane().setBackground(Defaults.MAIN);
		logon.setLayout(null);



		disclaimerLabel.setBounds(10,140,464,30);
		disclaimerLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		disclaimerLabel.setForeground(Defaults.FOREGROUND2);
		logon.add(disclaimerLabel);

		usernameLabel.setBounds(10,10,464,30);
		usernameLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		usernameLabel.setForeground(Defaults.FOREGROUND);
		logon.add(usernameLabel);

		passwordLabel.setBounds(10,70,464,30);
		passwordLabel.setFont(Defaults.SEGOE.deriveFont(14f));
		passwordLabel.setForeground(Defaults.FOREGROUND);
		logon.add(passwordLabel);

		usernameInput.setBounds(10,40,464, 30);
		logon.add(usernameInput);

		passwordInput.setBounds(10,100,464, 30);
		logon.add(passwordInput);

		//System.out.println(new String(passwordInput.getPassword()));
		loginButton.setBounds(10,180, 230, 40);
		loginButton.setFont(Defaults.SEGOE.deriveFont(14f));
		loginButton.setUI(settingsButtonUI);
		loginButton.setBackground(Defaults.BUTTON);
		loginButton.setForeground(Defaults.FOREGROUND);
		loginButton.setPreferredSize(new Dimension(232, 40));
		loginButton.refresh();
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					try{
						LoadGD.authClient = GDClientBuilder.create().buildAuthenticated(new GDClientBuilder.Credentials(usernameInput.getText(), new String(passwordInput.getPassword()))).block();
						LoadGD.isAuth = true;
						refreshGD(usernameInput.getText());
						Settings.writeSettings("p", new String(Base64.getEncoder().encode(xor(new String(passwordInput.getPassword()), 15).getBytes())));
						Settings.writeSettings("GDUsername", usernameInput.getText());
						Settings.writeSettings("GDLogon", "true");
						logon.setVisible(false);
					}
					catch (GDLoginFailedException | IOException f){
						usernameLabel.setForeground(red);
						passwordLabel.setForeground(red);
						try {
							Settings.writeSettings("GDLogon", "false");
						} catch (IOException ignored) {
						}
						LoadGD.isAuth = false;
					}
				}
			}
		});
		logon.add(loginButton);

		cancelButton.setBounds(244,180, 230, 40);
		cancelButton.setFont(Defaults.SEGOE.deriveFont(14f));
		cancelButton.setUI(settingsButtonUI);
		cancelButton.setBackground(Defaults.BUTTON);
		cancelButton.setForeground(Defaults.FOREGROUND);
		cancelButton.setPreferredSize(new Dimension(230, 40));
		cancelButton.refresh();
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					logon.setVisible(false);
				}
			}
		});

		logon.add(cancelButton);

		refreshGD.setBackground(Defaults.BUTTON);
		refreshGD.setBounds(365,18,25,25);
		refreshGD.setPreferredSize(new Dimension(25,25));
		refreshGD.setUI(settingsButtonUI);
		refreshGD.setForeground(Defaults.FOREGROUND);
		refreshGD.setBorder(BorderFactory.createEmptyBorder());
		refreshGD.setFont(Defaults.SYMBOLS.deriveFont(14f));
		refreshGD.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					usernameInput.setText("");
					usernameInput.requestFocus();
					passwordInput.setText("");
					usernameInput.clearUndo();
					passwordInput.clearUndo();
					usernameLabel.setForeground(Defaults.FOREGROUND);
					passwordLabel.setForeground(Defaults.FOREGROUND);
					logon.setLocation(Defaults.screenSize.x + Defaults.screenSize.width / 2 - logon.getWidth() / 2, Defaults.screenSize.y + Defaults.screenSize.height / 2 - logon.getHeight() / 2);
					logon.setVisible(true);
				}
			}
		});
		
		panel.add(channelText);
		panel.add(geometryText);
		panel.add(refreshTwitch);
		panel.add(refreshGD);
		return panel;

	}
	public static void refreshTwitch(String channel){
		channelText.setTextLangFormat("$TWITCH$", channel);

		channelText.setBounds(25,50,channelText.getPreferredSize().width+5,channelText.getPreferredSize().height+5);

	}
	public static void refreshGD(String username){
		if(LoadGD.isAuth) {
			geometryText.setTextLangFormat("$GEOMETRY_DASH$", username);
			geometryText.setBounds(25, 20, geometryText.getPreferredSize().width + 5, geometryText.getPreferredSize().height + 5);
		}

	}
	public static void refreshUI() {
		refreshTwitch.setBackground(Defaults.BUTTON);
		refreshTwitch.setForeground(Defaults.FOREGROUND);
		refreshGD.setBackground(Defaults.BUTTON);
		refreshGD.setForeground(Defaults.FOREGROUND);
		panel.setBackground(Defaults.SUB_MAIN);

		logon.getContentPane().setBackground(Defaults.MAIN);
		disclaimerLabel.setForeground(Defaults.FOREGROUND2);

		if(usernameLabel.getForeground() != red) {
			usernameLabel.setForeground(Defaults.FOREGROUND);
			passwordLabel.setForeground(Defaults.FOREGROUND);
		}
		usernameInput.refreshAll();
		passwordInput.refreshAll();

		loginButton.setBackground(Defaults.BUTTON);
		loginButton.setForeground(Defaults.FOREGROUND);
		loginButton.refresh();

		cancelButton.setBackground(Defaults.BUTTON);
		cancelButton.setForeground(Defaults.FOREGROUND);
		cancelButton.refresh();

		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel) {
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
	private static String xor(String inputString, int xorKey) {

		StringBuilder outputString = new StringBuilder();

		int len = inputString.length();

		for (int i = 0; i < len; i++) {
			outputString.append((char) (inputString.charAt(i) ^ xorKey));
		}
		return outputString.toString();
	}
}
