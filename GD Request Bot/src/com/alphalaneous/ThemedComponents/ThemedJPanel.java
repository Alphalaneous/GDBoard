package com.alphalaneous.ThemedComponents;

import com.alphalaneous.Defaults;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ThemedJPanel extends JPanel {



	private String colorChoice;

	private static ArrayList<ThemedJPanel> panels = new ArrayList<>();

	public ThemedJPanel(){
		super();
		panels.add(this);
	}

	public ThemedJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		panels.add(this);
	}

	public ThemedJPanel(LayoutManager layout) {
		super(layout);
		panels.add(this);
	}

	public ThemedJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		panels.add(this);
	}
	public void setColor(String color){
		colorChoice = color;
		setBackground(Defaults.colors.get(color));
	}
	public void refresh(){
		setBackground(Defaults.colors.get(colorChoice));
	}
	public static void refreshAll(){
		for(ThemedJPanel panel : panels){
			panel.refresh();
		}
	}
}
