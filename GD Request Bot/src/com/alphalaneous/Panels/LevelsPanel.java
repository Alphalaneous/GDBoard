package com.alphalaneous.Panels;

import com.alphalaneous.Components.ScrollbarUI;
import com.alphalaneous.Defaults;
import com.alphalaneous.Functions;
import com.alphalaneous.LevelData;
import com.alphalaneous.Requests;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;

public class LevelsPanel {

	private static JPanel buttonPanel = new JPanel();
	private static int selectedID = 0;
	private static int buttonWidth = 400;

	private static int prevSelectedID = 0;
	private static GridBagConstraints gbc = new GridBagConstraints();
	private static JPanel borderPanel = new JPanel(new BorderLayout());
	private static JScrollPane scrollPane = new JScrollPane(borderPanel);
	private static JPanel root = new JPanel(new BorderLayout());


	public static void createPanel(){

		buttonPanel.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		borderPanel.add(buttonPanel, BorderLayout.NORTH);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		borderPanel.setBackground(Defaults.MAIN);
		root.add(scrollPane);

	}
	public static void addButton(LevelData data){
		LevelButton button = new LevelButton(data);

		button.addActionListener(e -> {
			prevSelectedID = selectedID;
			selectedID = findButton(button);
		});
		buttonPanel.add(button, gbc);
		if(buttonPanel.getComponents().length == 1){
			button.select();
		}
		buttonPanel.updateUI();
	}

	public static void resizeButtons(int width){
		buttonWidth = width - 15;
		Component[] comp = buttonPanel.getComponents();
		for (Component component : comp) {
			if (component instanceof LevelButton) {
				((LevelButton) component).resizeButton(width);
			}
		}
	}

	public static void updateUI(long ID, boolean vulgar, boolean image, boolean analyzed) {
		while (true) {
			for (Component component : buttonPanel.getComponents()) {
				if (component instanceof LevelButton) {
					if (((LevelButton) component).ID == ID) {
						((LevelButton) component).setAnalyzed(analyzed, image, vulgar);
						((LevelButton) component).refresh(image, vulgar);
						return;
					}
				}
			}
		}
	}

	public static void refreshSelectedLevel(long ID) {
		if (getSelectedID() == Requests.getPosFromID(ID)) {
			setSelect(getSelectedID());
		}
	}

	public static boolean isScrollbarVisible(){
		return scrollPane.getVerticalScrollBar().isShowing();
	}
	public static JPanel getReqWindow() {
		return root;
	}

	public static void setName(int count) {
		Window.frame.setTitle("GDBoard - " + count);
	}

	public static void removeButton(){
		buttonPanel.remove(selectedID);
		if(selectedID != 0) {
			selectedID = selectedID - 1;
		}
		buttonPanel.updateUI();
	}
	public static void removeButton(int position){
		buttonPanel.remove(position);
		selectedID = 0;
		buttonPanel.updateUI();
	}
	public static LevelButton getButton(int i) {
		return ((LevelButton) buttonPanel.getComponent(i));
	}

	public static void movePosition(int position, int newPosition) {
		long selectID = -1;
		if (newPosition >= Requests.levels.size()) {
			newPosition = Requests.levels.size() - 1;
		}
		for (int i = 0; i < Requests.levels.size(); i++) {
			if (getButton(i).selected) {
				selectID = Requests.levels.get(i).getLevelID();
			}
		}
		System.out.println("Position: " + position + " | newPosition: " + newPosition);
		buttonPanel.add(getButton(position), gbc, newPosition);
		LevelData data = Requests.levels.get(position);
		Requests.levels.remove(position);
		Requests.levels.add(newPosition, data);
		buttonPanel.invalidate();
		buttonPanel.validate();
		for (int i = 0; i < Requests.levels.size(); i++) {
			if (selectID == Requests.levels.get(i).getLevelID()) {
				LevelsPanel.setSelect(i);
			}
		}
		Functions.saveFunction();

	}
	public static void setSelect(int position){
		deselectAll();

		if(buttonPanel.getComponentCount() != 0) {
			LevelButton button = ((LevelButton) buttonPanel.getComponents()[position]);
			button.select();
			if(position == 0){
				scrollPane.getViewport().setViewPosition(new Point(0,0));
			}
			else {
				scrollPane.getViewport().setViewPosition(new Point(0, button.getY()));
			}
			selectedID = position;
		}
	}

	public static int getSize(){
		return buttonPanel.getComponentCount();
	}

	public static int getSelectedID(){
		return selectedID;
	}

	public static int getPrevSelectedID(){
		return prevSelectedID;
	}

	public static int getButtonWidth(){
		return buttonWidth;
	}

	public static void deselectAll(){
		for(int i = 0; i < buttonPanel.getComponents().length; i++){
			if(buttonPanel.getComponents()[i] instanceof LevelButton){
				((LevelButton)buttonPanel.getComponents()[i]).deselect();
			}
		}
	}

	public static int findButton(JButton button){
		for(int i = 0; i < buttonPanel.getComponents().length; i++){
			if(buttonPanel.getComponents()[i] instanceof JButton){
				if(buttonPanel.getComponents()[i].equals(button)){
					return i;
				}
			}
		}
		return -1;
	}

	public static void refreshUI(){
		borderPanel.setBackground(Defaults.MAIN);
		root.setBackground(Defaults.MAIN);
		int i = 0;
		for (Component component : buttonPanel.getComponents()) {
			if (component instanceof LevelButton) {
				if (selectedID == i) {
					((LevelButton) component).select();
				} else {
					component.setBackground(Defaults.MAIN);
				}
				((LevelButton) component).refresh();
			}
			i++;
		}
		if (scrollPane != null) {
			scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
			scrollPane.setBackground(Defaults.MAIN);
			scrollPane.getViewport().setBackground(Defaults.MAIN);

		}
	}

}
