package com.alphalaneous.Components;

import com.alphalaneous.Defaults;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SettingsComponent extends JPanel {

    private static final ArrayList<SettingsComponent> components = new ArrayList<>();

    public SettingsComponent(JComponent panel, Dimension dimension){
        setLayout(null);
        setPreferredSize(new Dimension(dimension.width, dimension.height+15));
        setBackground(Defaults.COLOR3);
        panel.setBounds(30,0,panel.getPreferredSize().width, panel.getPreferredSize().height);
        add(panel);
        components.add(this);
    }

    protected void resizeComponent(Dimension dimension){

    }
    protected void refreshUI(){

    }
    public static void refreshAll(){
        for(SettingsComponent component : components){
            component.refreshUI();
            component.setBackground(Defaults.COLOR3);
        }
    }
}
