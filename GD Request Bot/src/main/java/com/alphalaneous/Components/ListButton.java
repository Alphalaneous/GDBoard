package com.alphalaneous.Components;

import com.alphalaneous.Defaults;
import com.alphalaneous.Function;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.alphalaneous.Defaults.settingsButtonUI;

public class ListButton extends CurvedButton{

    public ListButton(String label, int width) {
        super(label);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setBackground(Defaults.COLOR2);
        setUI(settingsButtonUI);
        setForeground(Defaults.FOREGROUND_A);
        setBorder(BorderFactory.createEmptyBorder());
        setFont(Defaults.MAIN_FONT.deriveFont(14f));
        setPreferredSize(new Dimension(width, 35));
        refresh();
    }
}
