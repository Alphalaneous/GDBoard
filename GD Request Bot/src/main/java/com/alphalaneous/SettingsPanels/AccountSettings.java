package com.alphalaneous.SettingsPanels;

import com.alphalaneous.*;
import com.alphalaneous.Components.*;
import com.alphalaneous.Panels.ContextButton;
import com.alphalaneous.Panels.ContextMenu;
import com.alphalaneous.Windows.DialogBox;
import com.alphalaneous.Windows.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Base64;

import static com.alphalaneous.Defaults.settingsButtonUI;

public class AccountSettings {

    private static AccountPanel geometryDashPanel = null;
    private static AccountPanel twitchPanel = null;
    private static final CurvedButton loginButton = new CurvedButton("$LOGIN$");
    private static final CurvedButton cancelButton = new CurvedButton("$CANCEL$");
    private static final LangLabel usernameLabel = new LangLabel("$USERNAME$");
    private static final LangLabel passwordLabel = new LangLabel("$PASSWORD$");
    private static final LangLabel disclaimerLabel = new LangLabel("$DISCLAIMER$");

    private static final FancyTextArea usernameTextArea = new FancyTextArea(false, false);
    private static final FancyPasswordField passwordTextArea = new FancyPasswordField();
    private static final Color red = new Color(255, 0, 0);
    private static final JPanel logonFrame = new JPanel();
    private static final CurvedButtonAlt firstLoginButton = new CurvedButtonAlt("GD Account Login");

    public static JPanel createPanel(){
        SettingsPage settingsPage = new SettingsPage("$ACCOUNTS_SETTINGS$");

        logonFrame.setSize(485, 230);
        //logonFrame.setResizable(false);
        //ogonFrame.setTitle("Log into GD");
        //logonFrame.setIconImage(Assets.loquibot.getImage());
        logonFrame.setBackground(Defaults.COLOR3);
        logonFrame.setLayout(null);


        disclaimerLabel.setBounds(10, 140, 464, 30);
        disclaimerLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        disclaimerLabel.setForeground(Defaults.FOREGROUND_B);
        logonFrame.add(disclaimerLabel);

        usernameLabel.setBounds(10, 10, 464, 30);
        usernameLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        usernameLabel.setForeground(Defaults.FOREGROUND_A);
        logonFrame.add(usernameLabel);

        passwordLabel.setBounds(10, 70, 464, 30);
        passwordLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        passwordLabel.setForeground(Defaults.FOREGROUND_A);
        logonFrame.add(passwordLabel);

        usernameTextArea.setBounds(10, 40, 464, 30);
        logonFrame.add(usernameTextArea);

        passwordTextArea.setBounds(10, 100, 464, 30);
        logonFrame.add(passwordTextArea);

        loginButton.setBounds(10, 180, 230, 40);
        loginButton.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        loginButton.setUI(settingsButtonUI);
        loginButton.setBackground(Defaults.COLOR2);
        loginButton.setForeground(Defaults.FOREGROUND_A);
        loginButton.setPreferredSize(new Dimension(232, 40));
        loginButton.refresh();
        loginButton.addActionListener(e -> {
            try {
                boolean successfulLogin = GDAPI.login(usernameTextArea.getText(), String.valueOf(passwordTextArea.getPassword()));
                if(successfulLogin){
                    LoadGD.isAuth = true;
                    refreshGD(usernameTextArea.getText());
                    Settings.writeSettings("p", new String(Base64.getEncoder().encode(xor(new String(passwordTextArea.getPassword())).getBytes())));
                    Settings.writeSettings("GDUsername", usernameTextArea.getText());
                    Settings.writeSettings("GDLogon", "true");
                    DialogBox.closeDialogBox();
                }
                else {
                    usernameLabel.setForeground(red);
                    passwordLabel.setForeground(red);
                    Settings.writeSettings("GDLogon", "false");
                    LoadGD.isAuth = false;
                }
            } catch (Exception f) {
                f.printStackTrace();
                usernameLabel.setForeground(red);
                usernameLabel.setTextLang("Failed");
            }
        });
        logonFrame.add(loginButton);

        cancelButton.setBounds(244, 180, 230, 40);
        cancelButton.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        cancelButton.setUI(settingsButtonUI);
        cancelButton.setBackground(Defaults.COLOR2);
        cancelButton.setForeground(Defaults.FOREGROUND_A);
        cancelButton.setPreferredSize(new Dimension(230, 40));
        cancelButton.refresh();
        cancelButton.addActionListener(e -> DialogBox.closeDialogBox());

        logonFrame.add(cancelButton);

        geometryDashPanel = new AccountPanel(
                () -> Window.addContextMenu(createGDContextMenu()));
        twitchPanel = new AccountPanel(
                () -> Window.addContextMenu(createTwitchContextMenu()));

        firstLoginButton.setBounds(10,10,300,80);
        firstLoginButton.setFont(Defaults.MAIN_FONT.deriveFont(20f));
        firstLoginButton.setUI(settingsButtonUI);
        firstLoginButton.setBackground(Defaults.COLOR2);
        firstLoginButton.setForeground(Defaults.FOREGROUND_A);

        geometryDashPanel.add(firstLoginButton);
        firstLoginButton.addActionListener(e -> {
            //logonFrame.setLocationRelativeTo(null);
            DialogBox.showDialogBox(logonFrame);
            //logonFrame.setVisible(true);
        });

        if(Settings.getSettings("onboarding").exists()) {
            refreshTwitch(TwitchAccount.display_name);
        }

        SettingsComponent geometryDashComponent = new SettingsComponent(geometryDashPanel, new Dimension(475,100)){
            @Override
            protected void refreshUI(){
                geometryDashPanel.refresh();
                refreshLoginPanel();
            }
            @Override
            protected void resizeComponent(Dimension dimension){
                geometryDashPanel.resizeComponent(dimension.width);
            }
        };
        SettingsComponent twitchComponent = new SettingsComponent(twitchPanel, new Dimension(475,100)){
            @Override
            protected void refreshUI(){
                twitchPanel.refresh();
            }
            @Override
            protected void resizeComponent(Dimension dimension){
                twitchPanel.resizeComponent(dimension.width);
            }
        };

        settingsPage.addComponent(geometryDashComponent);
        settingsPage.addComponent(twitchComponent);

        return settingsPage;
    }

    private static void refreshLoginPanel(){
        disclaimerLabel.setForeground(Defaults.FOREGROUND_B);
        usernameLabel.setForeground(Defaults.FOREGROUND_A);
        passwordLabel.setForeground(Defaults.FOREGROUND_A);
        logonFrame.setBackground(Defaults.COLOR3);
        cancelButton.setUI(settingsButtonUI);
        cancelButton.setBackground(Defaults.COLOR2);
        cancelButton.setLForeground(Defaults.FOREGROUND_A);
        loginButton.setUI(settingsButtonUI);
        loginButton.setBackground(Defaults.COLOR2);
        loginButton.setLForeground(Defaults.FOREGROUND_A);
        firstLoginButton.setUI(settingsButtonUI);
        firstLoginButton.setBackground(Defaults.COLOR2);
        firstLoginButton.setForeground(Defaults.FOREGROUND_A);
    }

    public static ContextMenu createGDContextMenu(){
        ContextMenu geometryDashContextMenu = new ContextMenu();
        geometryDashContextMenu.addButton(new ContextButton("Refresh Login", () -> {
            //logonFrame.setLocationRelativeTo(null);
            DialogBox.showDialogBox(logonFrame);
        }));
       // geometryDashContextMenu.addButton(new ContextButton("Logout", () -> logonFrame.setVisible(true)));
        //todo log out GD account, refresh account panel
        return geometryDashContextMenu;
    }
    public static ContextMenu createTwitchContextMenu(){
        ContextMenu twitchContextMenu = new ContextMenu();
        twitchContextMenu.addButton(new ContextButton("Refresh Login", () -> new Thread(APIs::setOauth).start()));
        return twitchContextMenu;
    }

    public static void refreshTwitch(String channel) {
        twitchPanel.refreshInfo(channel, "Twitch", new ImageIcon(makeRoundedCorner(TwitchAccount.profileImage).getScaledInstance(60,60, Image.SCALE_SMOOTH)));
    }

    public static void refreshGD(String username) {
        if (LoadGD.isAuth) {
            ImageIcon icon = GDAPI.getIcon(GDAPI.getGDUserProfile(username), 120);
            geometryDashPanel.refreshInfo(username, "Geometry Dash", icon, -30, 0);
            firstLoginButton.setVisible(false);
        }
        else{
            firstLoginButton.setVisible(true);
        }
    }
    public static BufferedImage makeRoundedCorner(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(0, 0, w, h));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }


    private static class AccountPanel extends JPanel {

        JLabel accountNameLabel;
        JLabel accountTypeLabel;
        JLabel accountImageLabel;
        CurvedButton dropDownButton;


        AccountPanel(Function dropDownFunction){

            setLayout(null);
            accountNameLabel = new JLabel();
            accountNameLabel.setBounds(100,25,470,30);
            accountNameLabel.setFont(Defaults.MAIN_FONT.deriveFont(20f));
            accountTypeLabel = new JLabel();
            accountTypeLabel.setBounds(100,50,470,30);
            accountTypeLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
            accountImageLabel = new JLabel();
            accountImageLabel.setBounds(20,20,60,60);

            dropDownButton = new CurvedButton("\uF666 \uF666 \uF666");

            dropDownButton.setUI(Defaults.defaultUI);
            dropDownButton.setOpaque(false);
            dropDownButton.setFont(Defaults.SYMBOLS.deriveFont(6f));
            dropDownButton.setBounds(420,35,30,30);
            dropDownButton.setPreferredSize(new Dimension(30,30));
            dropDownButton.refresh();
            dropDownButton.addActionListener(e -> dropDownFunction.run());
            setPreferredSize(new Dimension(475,100));
            setOpaque(false);

        }
        public void refreshInfo(String accountName, String accountType, ImageIcon accountImage, int xShift, int yShift){
            accountNameLabel.setText(accountName);
            accountTypeLabel.setText(accountType);
            accountImageLabel.setIcon(accountImage);
            accountImageLabel.setBounds(20+xShift,20+yShift,60+-xShift,60+-yShift);
            add(accountImageLabel);
            add(accountNameLabel);
            add(accountTypeLabel);
            add(dropDownButton);
            updateUI();
        }

        public void resizeComponent(int width){
            setPreferredSize(new Dimension(width-340,100));
            setBounds(30,0,getPreferredSize().width, getPreferredSize().height);
            dropDownButton.setBounds(width-400,35,30,30);
            accountNameLabel.setBounds(100,25,width,30);
            accountTypeLabel.setBounds(100,50,width,30);
            firstLoginButton.setBounds(10,10,width-360,80);
        }

        public void logout(){
            remove(accountImageLabel);
            remove(accountNameLabel);
            remove(accountTypeLabel);
        }

        public void refreshInfo(String accountName, String accountType, ImageIcon accountImage){
            refreshInfo(accountName, accountType, accountImage, 0,0);
        }

        public void refresh(){
            setBackground(Defaults.COLOR);
            accountNameLabel.setForeground(Defaults.FOREGROUND_A);
            accountTypeLabel.setForeground(Defaults.FOREGROUND_B);
            dropDownButton.setBackground(Defaults.COLOR);
            dropDownButton.setForeground(Defaults.FOREGROUND_A);
            dropDownButton.refresh();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g.setColor(getBackground());

            RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(qualityHints);
            g2.fillRoundRect(0, 0, getSize().width, getSize().height, 30, 30);

            super.paintComponent(g);
        }
    }
    private static String xor(String inputString) {

        StringBuilder outputString = new StringBuilder();

        int len = inputString.length();

        for (int i = 0; i < len; i++) {
            outputString.append((char) (inputString.charAt(i) ^ 15));
        }
        return outputString.toString();
    }
}
