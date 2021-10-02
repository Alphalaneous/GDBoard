package com.alphalaneous.ThemedComponents;

import com.alphalaneous.Assets;
import com.alphalaneous.Components.CurvedButtonAlt;
import com.alphalaneous.Components.JButtonUI;
import com.alphalaneous.Components.LangLabel;
import com.alphalaneous.Defaults;
import com.alphalaneous.Function;
import com.alphalaneous.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ThemedConfigCheckbox extends JPanel{

    private static final ArrayList<ThemedConfigCheckbox> buttons = new ArrayList<>();

    private final JLabel descriptionText = new JLabel();
    private final CurvedButtonAlt configButton = new CurvedButtonAlt("\uF00F");
    private final LangLabel text = new LangLabel("");
    private final JLabel check = new JLabel("\uE922");
    private final JLabel checkSymbol = new JLabel("\uE73E");
    private final JLabel hover = new JLabel("\uE922");
    private boolean isChecked = false;
    private final JButtonUI emptyUI = new JButtonUI(){{
        setBackground(new Color(0,0, 0,0));
        setHover(Defaults.COLOR2);
        setSelect(new Color(0,0, 0,0));

    }};



    public ThemedConfigCheckbox(String label, String description, Function function) {
        setLayout(null);
        text.setTextLang(label);
        text.setForeground(Defaults.FOREGROUND_A);
        check.setFont(Defaults.SYMBOLSalt.deriveFont(16f));
        checkSymbol.setForeground(Color.WHITE);
        checkSymbol.setFont(Defaults.SYMBOLSalt.deriveFont(16f));
        hover.setForeground(Defaults.FOREGROUND_A);
        hover.setFont(Defaults.SYMBOLSalt.deriveFont(16f));
        checkSymbol.setVisible(false);
        hover.setVisible(false);

        descriptionText.setText("<html><div WIDTH=450>" + Language.setLocale(description) + "</div></html>");
        descriptionText.setFont(Defaults.MAIN_FONT.deriveFont(13f));
        descriptionText.setOpaque(false);

        configButton.setFont(Defaults.SYMBOLS.deriveFont(18f));
        //configButton.setUI(Defaults.settingsButtonUI);
        configButton.setContentAreaFilled(false);
        configButton.setUI(emptyUI);
        configButton.setBackground(new Color(0,0,0,0));
        configButton.setOpaque(false);
        configButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        configButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                configButton.setFont(Defaults.SYMBOLS.deriveFont(22f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                configButton.setFont(Defaults.SYMBOLS.deriveFont(18f));
            }
        });
        configButton.setForeground(Defaults.FOREGROUND_A);
        configButton.addActionListener(e -> {
            if(function != null) function.run();
        });

        add(configButton);
        add(descriptionText);

        add(hover);
        add(checkSymbol);
        add(check);
        add(text);
        setBackground(Defaults.COLOR);
        setOpaque(false);
        check.setForeground(Color.LIGHT_GRAY);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    check.setText("\uE73B");
                    check.setForeground(Color.LIGHT_GRAY);
                    hover.setVisible(false);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (isChecked) {
                        check.setText("\uE922");
                        check.setForeground(Color.LIGHT_GRAY);
                        checkSymbol.setVisible(false);
                        isChecked = false;
                    } else {
                        check.setText("\uE73B");
                        check.setForeground(Defaults.ACCENT);
                        checkSymbol.setVisible(true);
                        isChecked = true;
                    }
                }
                hover.setVisible(true);
            }

            public void mouseEntered(MouseEvent e) {
                hover.setVisible(true);
            }

            public void mouseExited(MouseEvent e) {
                if (!isChecked) {
                    check.setText("\uE922");
                    check.setForeground(Color.LIGHT_GRAY);
                    checkSymbol.setVisible(false);
                } else {
                    check.setText("\uE73B");
                    check.setForeground(Defaults.ACCENT);
                    checkSymbol.setVisible(true);
                }
                hover.setVisible(false);
            }
        });
        buttons.add(this);
    }

    public static void refreshAll() {
        for (ThemedConfigCheckbox button : buttons) {
            button.refresh();
        }
    }

    public void setText(String textA) {
        text.setTextLang(textA);
    }

    public boolean getSelectedState() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
        if (!isChecked) {
            check.setText("\uE922");
            check.setForeground(Color.LIGHT_GRAY);
            checkSymbol.setVisible(false);
        } else {
            check.setText("\uE73B");
            check.setForeground(Defaults.ACCENT);
            checkSymbol.setVisible(true);
        }
    }

    public void resize(int width) {
        text.setBounds(50, 13, width, text.getPreferredSize().height + 5);
        descriptionText.setBounds(50, 33, width, descriptionText.getPreferredSize().height + 5);
        check.setBounds(20, 20, 30, 30);
        checkSymbol.setBounds(20, 20, 30, 30);
        hover.setBounds(20, 20, 30, 30);
        configButton.setBounds(width-50, 20, 30, 30);

    }

    public void refresh() {
        if (!isChecked) {
            check.setForeground(Color.LIGHT_GRAY);
        } else {
            check.setForeground(Defaults.ACCENT);
        }
        setBackground(Defaults.COLOR);
        descriptionText.setForeground(Defaults.FOREGROUND_B);
        text.setForeground(Defaults.FOREGROUND_A);
        text.setFont(getFont());
        text.setBounds(50, 13, getWidth(), text.getPreferredSize().height + 5);
        descriptionText.setBounds(50, 33, getWidth(), descriptionText.getPreferredSize().height + 5);
        configButton.setBounds(getWidth()-50, 20, 30, 30);

        check.setBounds(20, 20, 30, 30);
        checkSymbol.setBounds(20, 20, 30, 30);
        hover.setForeground(Defaults.FOREGROUND_A);
        hover.setBounds(20, 20, 30, 30);
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g.setColor(getBackground());

        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(qualityHints);
        g2.fillRoundRect(0, 0, getSize().width, getSize().height, 20, 20);


        super.paintComponent(g);
    }

}
