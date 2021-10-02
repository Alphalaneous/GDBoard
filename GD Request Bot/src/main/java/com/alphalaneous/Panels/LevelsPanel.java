package com.alphalaneous.Panels;

import com.alphalaneous.Components.ScrollbarUI;
import com.alphalaneous.Components.SmoothScrollPane;
import com.alphalaneous.Defaults;
import com.alphalaneous.RequestFunctions;
import com.alphalaneous.Settings;
import com.alphalaneous.Tabs.RequestsTab;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;

public class LevelsPanel extends JPanel {

    private final JPanel buttonPanel = new JPanel();
    private int buttonWidth = 400;

    private final GridBagConstraints gbc = new GridBagConstraints();
    private final JPanel borderPanel = new JPanel(new BorderLayout());
    private final JScrollPane scrollPane = new SmoothScrollPane(borderPanel);

    public LevelsPanel(){
        setOpaque(false);
        setLayout(new BorderLayout());
        setBackground(new Color(0,0,0,0));
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(8, 9, 0, 2);
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        borderPanel.add(buttonPanel, BorderLayout.NORTH);
        borderPanel.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBackground(new Color(0,0,0,0));
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));

        scrollPane.getViewport().setOpaque(false);
        borderPanel.setBackground(Defaults.COLOR);
        buttonPanel.setBackground(Defaults.COLOR);
        add(scrollPane);
    }

    public void addButton(LevelButton button){
        buttonPanel.add(button, gbc);
    }
    public void addButton(BasicLevelButton button){
        buttonPanel.add(button, gbc);
    }
    public void addButton(LevelButton button, int pos){
        buttonPanel.add(button, gbc, pos);
    }
    public void clearRequests(){
        buttonPanel.removeAll();
    }
    public int getQueueSize(){
        return buttonPanel.getComponentCount();
    }
    /*public void refreshButtons(){
        buttonPanel.removeAll();

        for(LevelData data : Requests.levels){
            LevelButton button = data.getLevelButton();
            buttonPanel.add(button, gbc);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buttonPanel.updateUI();
    }*/

    public void resizePanel(int width, int height){
        buttonWidth = width - 18;
        Component[] comp = buttonPanel.getComponents();
        for (Component component : comp) {
            if (component instanceof LevelButton) {
                ((LevelButton) component).resizeButton(width);
            }
        }
        setBounds(0, 0, width, height+8);
    }

    public void updateUI(long ID, boolean vulgar, boolean image, boolean analyzed) {
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

    public void setWindowName(int count) {
        Window.setTitle("loquibot - " + count);
    }

    public LevelButton getButton(int i) {
        return ((LevelButton) buttonPanel.getComponent(i));
    }
    public BasicLevelButton getButtonBasic(int i) {
        return ((BasicLevelButton) buttonPanel.getComponent(i));
    }

    public void removeRequest(int pos){
        buttonPanel.remove(pos);
    }

    public void movePosition(int position, int newPosition) {
        long selectID = -1;
        if (newPosition >= RequestsTab.getQueueSize()) {
            newPosition = RequestsTab.getQueueSize() - 1;
        }
        for (int i = 0; i < RequestsTab.getQueueSize(); i++) {
            if(!Settings.getSettings("basicMode").asBoolean()) {
                if (getButton(i).selected) {
                    selectID = RequestsTab.getRequest(i).getLevelData().getGDLevel().id();
                }
            }
            else {
                if (getButtonBasic(i).selected) {
                    selectID = RequestsTab.getRequestBasic(i).getID();
                }
            }
        }
        System.out.println("Position: " + position + " | newPosition: " + newPosition);
        buttonPanel.add(buttonPanel.getComponents()[position], gbc, newPosition);
        for (int i = 0; i < RequestsTab.getQueueSize(); i++) {
            if(!Settings.getSettings("basicMode").asBoolean()) {
                if (selectID == RequestsTab.getRequest(i).getLevelData().getGDLevel().id()) {
                    setSelect(i, false, false);
                }
            }
            else{
                if (selectID == RequestsTab.getRequestBasic(i).getID()) {
                    setSelect(i, false, false);
                }
            }
        }
        RequestFunctions.saveFunction();
    }
    public void setSelect(int position){
        setSelect(position, false, true);
    }
    public void setSelect(int position, boolean refresh){
        setSelect(position, refresh, true);
    }
    public void setSelect(int position, boolean refresh, boolean resetScroll){
        deselectAll();
        if(buttonPanel.getComponentCount() != 0) {
            if(!Settings.getSettings("basicMode").asBoolean()) {
                LevelButton button;
                if (buttonPanel.getComponentCount() == 1) {
                    button = ((LevelButton) buttonPanel.getComponents()[0]);
                } else {
                    button = ((LevelButton) buttonPanel.getComponents()[position]);
                }
                button.select(refresh);
                if (resetScroll) {
                    if (position == 0) {
                        scrollPane.getViewport().setViewPosition(new Point(0, 0));
                    } else {
                        scrollPane.getViewport().setViewPosition(new Point(0, button.getY()));
                    }
                }
            }
            else {
                BasicLevelButton button;
                if (buttonPanel.getComponentCount() == 1) {
                    button = ((BasicLevelButton) buttonPanel.getComponents()[0]);
                } else {
                    button = ((BasicLevelButton) buttonPanel.getComponents()[position]);
                }
                button.select();
                if (resetScroll) {
                    if (position == 0) {
                        scrollPane.getViewport().setViewPosition(new Point(0, 0));
                    } else {
                        scrollPane.getViewport().setViewPosition(new Point(0, button.getY()));
                    }
                }
            }
        }
    }

    public int getButtonWidth(){
        return buttonWidth;
    }

    public void deselectAll(){
        for(int i = 0; i < buttonPanel.getComponents().length; i++){
            if(buttonPanel.getComponents()[i] instanceof LevelButton){
                ((LevelButton)buttonPanel.getComponents()[i]).deselect();
            }
            if(buttonPanel.getComponents()[i] instanceof BasicLevelButton){
                ((BasicLevelButton)buttonPanel.getComponents()[i]).deselect();
            }
        }
    }

    public int findButton(JButton button){
        for(int i = 0; i < buttonPanel.getComponents().length; i++){
            if(buttonPanel.getComponents()[i] instanceof JButton){
                if(buttonPanel.getComponents()[i].equals(button)){
                    return i;
                }
            }
        }
        return -1;
    }

    public void refreshUI(){
        //int i = 0;
        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof LevelButton) {
                /*if (LevelButton.selectedID == i) {
                    ((LevelButton) component).select();
                } else {
                    component.setBackground(Defaults.MAIN);
                }*/
                ((LevelButton) component).refresh();
            }
            //i++;
        }
        scrollPane.getVerticalScrollBar().setUI(new ScrollbarUI());
        borderPanel.setBackground(Defaults.COLOR3);
        buttonPanel.setBackground(Defaults.COLOR3);
    }
}
