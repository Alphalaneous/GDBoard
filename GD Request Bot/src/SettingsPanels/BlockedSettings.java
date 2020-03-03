package SettingsPanels;

import Main.Defaults;
import Main.Overlay;
import lc.kra.system.keyboard.GlobalKeyboardHook;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class BlockedSettings {
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		JTextArea textField = new JTextArea();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Overlay.getWindow().setFocusableWindowState(true);
				Overlay.getWindow().requestFocus();
				textField.requestFocus();
			}
		});
		textField.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e){
			}

			@Override
			public void focusLost(FocusEvent e) {
				Overlay.getWindow().setFocusableWindowState(false);
			}
		});
		textField.setPreferredSize(new Dimension(100,50));
		panel.add(textField);
		return panel;
		
	}
}
