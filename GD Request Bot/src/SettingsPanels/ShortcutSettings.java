package SettingsPanels;

import Main.Defaults;
import Main.Overlay;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ShortcutSettings {
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		JTextArea textField = new JTextArea();
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true);

		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Overlay.getWindow().setFocusableWindowState(true);
				Overlay.getWindow().requestFocus();
				textField.requestFocus();
				keyboardHook.addKeyListener(new GlobalKeyAdapter() {
					@Override
					public void keyPressed(GlobalKeyEvent event) {
						System.out.println(event.getKeyChar());
					}
				});
			}
		});
		textField.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e){
				Overlay.getWindow().requestFocus();
				textField.requestFocus();
			}

			@Override
			public void focusLost(FocusEvent e) {
				Overlay.getWindow().setFocusableWindowState(false);
				keyboardHook.shutdownHook();
			}
		});
		textField.setPreferredSize(new Dimension(100,50));
		panel.add(textField);

		return panel;

	}
}