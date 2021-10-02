package com.alphalaneous.Components;

import com.alphalaneous.Defaults;
import com.alphalaneous.Function;
import com.alphalaneous.Panels.SettingsTitle;
import com.alphalaneous.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.alphalaneous.Defaults.settingsButtonUI;

public class ListView extends JPanel {

    private static final ArrayList<ListView> listViewArrayList = new ArrayList<>();
    private final JPanel borderPanel = new JPanel(new BorderLayout());
    private final JPanel buttonsPanel = new JPanel();
    private final SmoothScrollPane scrollPane = new SmoothScrollPane(borderPanel);
    private final JPanel titlePane = new JPanel();
    private final SettingsTitle settingsTitle;
    private final GridBagConstraints gbc = new GridBagConstraints();

    public ListView(String title){

        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        buttonsPanel.setLayout(new WrapLayout());
        setBounds(0,0,100,100);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(2, 2, 2, 2);
        //gbc.weightx = 1;
        //gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.gridheight = 1;
        //gbc.fill = GridBagConstraints.RELATIVE;

        scrollPane.setBounds(0,80,100,100);
        scrollPane.setOpaque(false);

        settingsTitle = new SettingsTitle(title);
        settingsTitle.setOpaque(false);
        settingsTitle.setBounds(0,0,settingsTitle.getPreferredSize().width, 80);

        titlePane.setPreferredSize(new Dimension(titlePane.getWidth(),80));
        titlePane.setLayout(null);
        titlePane.add(settingsTitle);

        borderPanel.add(buttonsPanel, BorderLayout.NORTH);
        borderPanel.setOpaque(false);


        add(titlePane);
        add(scrollPane);

        listViewArrayList.add(this);
    }

    private FancyTextArea input;
    private CurvedButtonAlt button;
    public void addInput(String icon, FancyTextArea input, Function function){
        this.input = input;
        input.setBounds(getWidth()-350-input.getWidth(), 30, input.getWidth(), input.getHeight());
        addButton(icon, function);
        titlePane.add(input);
    }

    public void addButton(String icon, Function function){
        CurvedButtonAlt button = new CurvedButtonAlt(icon);
        this.button = button;
        button.setBackground(Defaults.COLOR2);
        button.setFont(Defaults.SYMBOLS.deriveFont(18f));
        button.setForeground(Defaults.FOREGROUND_A);
        button.setUI(settingsButtonUI);
        button.setBounds(getWidth()-330, 31, 30,30);
        button.addActionListener(e -> {
            if(function != null) function.run();
        });
        titlePane.add(button);
    }

    public String getInputText(){
        if(input != null){
            return input.getText();
        }
        return null;
    }

    public void addElement(JComponent component){
        buttonsPanel.add(component, gbc);
        buttonsPanel.updateUI();
    }
    public void removeElement(JComponent component){
        buttonsPanel.remove(component);
        buttonsPanel.updateUI();
    }
    public void clearElements(){
        buttonsPanel.removeAll();
        buttonsPanel.updateUI();
    }
    public Component[] getAddedComponents(){
        return getComponents();
    }

    public void resizeComponent(Dimension dimension){
        int subtractWidth = 280;
        int subtractHeight = 39;
        if(input != null) input.setBounds(dimension.width-350-input.getWidth(), 30, input.getWidth(), input.getHeight());
        if(button != null) button.setBounds(dimension.width-330, 31, 30,30);
        scrollPane.setPreferredSize(new Dimension(dimension.width - subtractWidth,dimension.height - subtractHeight-80));
        //borderPanel.setPreferredSize(new Dimension(dimension.width - subtractWidth, buttonsPanel.getHeight()));
        buttonsPanel.setSize(new Dimension(dimension.width - subtractWidth-10, buttonsPanel.getPreferredSize().height));
        settingsTitle.setPreferredSize(new Dimension(settingsTitle.getWidth(),80));
        titlePane.setPreferredSize(new Dimension(dimension.width - subtractWidth,80));
        setBounds(0,0,dimension.width - subtractWidth, dimension.height - subtractHeight);
        updateUI();
    }
    public void refreshUI(){
        setBackground(Defaults.COLOR3);
        for(Component component : buttonsPanel.getComponents()){
            if(component instanceof ListButton){
                component.setBackground(Defaults.COLOR2);
                ((ListButton)component).setUI(settingsButtonUI);
                ((ListButton)component).setLForeground(Defaults.FOREGROUND_A);
            }
        }
        if(button != null) {
            button.setForeground(Defaults.FOREGROUND_A);
            button.setBackground(Defaults.COLOR2);
            button.setUI(settingsButtonUI);
        }
        scrollPane.setBackground(Defaults.COLOR3);
        titlePane.setBackground(Defaults.COLOR6);
        buttonsPanel.setBackground(Defaults.COLOR3);
    }
    public static void refreshAll(){
        for(ListView view : listViewArrayList){
            view.refreshUI();
        }
    }
    public static void resizeAll(Dimension dimension){
        for(ListView view : listViewArrayList){
            view.resizeComponent(dimension);
        }
    }
}
