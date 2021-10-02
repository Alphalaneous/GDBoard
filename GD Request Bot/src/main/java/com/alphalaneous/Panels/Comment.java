package com.alphalaneous.Panels;

import com.alphalaneous.Defaults;
import com.alphalaneous.GDAPI;
import com.alphalaneous.Tabs.RequestsTab;
import jdash.common.IconType;
import jdash.common.entity.GDComment;
import jdash.common.entity.GDUser;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Optional;

public class Comment extends JPanel {

    private final JLabel content;
    private final JLabel commentAuthorLabel;
    private final JLabel likesLabel;
    private final JLabel likeIcon;
    private final JLabel playerIcon;
    private final JLabel percentLabel;
    private String creatorName;
    private String commentAuthor = "";
    private int percent = 0;

    public Comment(GDComment comment, int width){

        setLayout(null);

        int likes = comment.likes();
        commentAuthor = "";
        creatorName = "";

        String commentContent = String.format("<html><div WIDTH=%d>%s</div></html>", width - 15, StringEscapeUtils.unescapeHtml4(comment.content()));

        GDUser commentAuthorUser = null;

        Optional<Integer> percentOptional = comment.percentage();
        Optional<GDUser> commentAuthorOptional = comment.author();
        Optional<String> creatorNameOptional = RequestsTab.getRequest(LevelButton.selectedID).getLevelData().getGDLevel().creatorName();

        if (commentAuthorOptional.isPresent()){
            commentAuthorUser = commentAuthorOptional.get();
            commentAuthor = commentAuthorUser.name();
        }

        percentOptional.ifPresent(integer -> percent = integer);
        creatorNameOptional.ifPresent(s -> creatorName = s);

        content = new JLabel(commentContent);
        commentAuthorLabel = new JLabel(commentAuthor);
        likesLabel = new JLabel(String.valueOf(likes));
        likeIcon = new JLabel();
        playerIcon = new JLabel();
        percentLabel = new JLabel();
        if(percent != 0) percentLabel.setText(percent + "%");

        commentAuthorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        commentAuthorLabel.setFont(Defaults.MAIN_FONT.deriveFont(12f));
        commentAuthorLabel.setForeground(Defaults.FOREGROUND_A);
        commentAuthorLabel.setBounds(30, 2, commentAuthorLabel.getPreferredSize().width, 18);
        if (commentAuthor.equalsIgnoreCase(creatorName)) commentAuthorLabel.setForeground(new Color(47, 62, 195));

        String finalCommentAuthor = commentAuthor;
        commentAuthorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Runtime rt = Runtime.getRuntime();
                        rt.exec("rundll32 url.dll,FileProtocolHandler "
                                + "https://www.gdbrowser.com/profile/" + finalCommentAuthor);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                int center = (commentAuthorLabel.getPreferredSize().width) / 2;
                commentAuthorLabel.setFont(Defaults.MAIN_FONT.deriveFont(13f));
                commentAuthorLabel.setBounds(30 + center - (commentAuthorLabel.getPreferredSize().width) / 2, 2,
                        commentAuthorLabel.getPreferredSize().width + 5, 18);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                commentAuthorLabel.setFont(Defaults.MAIN_FONT.deriveFont(12f));
                commentAuthorLabel.setBounds(30, 2, commentAuthorLabel.getPreferredSize().width, 18);
            }
        });

        percentLabel.setFont(Defaults.MAIN_FONT.deriveFont(12f));
        percentLabel.setForeground(Defaults.FOREGROUND_B);
        percentLabel.setBounds(commentAuthorLabel.getPreferredSize().width + 42, 2,
                percentLabel.getPreferredSize().width + 5, 18);

        likeIcon.setText("\uF138");
        likeIcon.setBounds(width - 16, 2, 20, 20);
        likeIcon.setFont(Defaults.SYMBOLS.deriveFont(14f));
        likeIcon.setForeground(Defaults.FOREGROUND_A);

        if (likes < 0) {
            likeIcon.setText("\uF139");
            likeIcon.setBounds(width - 16, 7, 20, 20);
        }

        likesLabel.setFont(Defaults.MAIN_FONT.deriveFont(10f));
        likesLabel.setForeground(Defaults.FOREGROUND_A);
        likesLabel.setBounds(width - likesLabel.getPreferredSize().width - 22, 6, likesLabel.getPreferredSize().width + 5, 18);

        playerIcon.setIcon(GDAPI.getIcon(IconType.CUBE, 1, 1, 1, false));
        playerIcon.setBounds(2, -5, 30 + 2, 30 + 2);
        if(commentAuthorUser != null) {
            Optional<IconType> mainIconTypeOptional = commentAuthorUser.mainIconType();
            Optional<Integer> mainIconIDOptional = commentAuthorUser.mainIconId();

            if (mainIconTypeOptional.isPresent() && mainIconIDOptional.isPresent()) {
                GDUser finalCommentAuthorUser = commentAuthorUser;
                new Thread(() -> playerIcon.setIcon(GDAPI.getIcon(mainIconTypeOptional.get(),
                        mainIconIDOptional.get(),
                        finalCommentAuthorUser.color1Id(),
                        finalCommentAuthorUser.color2Id(),
                        finalCommentAuthorUser.hasGlowOutline()))).start();
            }
        }


        content.setFont(Defaults.MAIN_FONT.deriveFont(12f));
        content.setForeground(Defaults.FOREGROUND_A);
        content.setBounds(9, 24, width - 15, content.getPreferredSize().height);

        add(commentAuthorLabel);
        add(content);
        add(percentLabel);
        add(likesLabel);
        add(likeIcon);
        add(playerIcon);

        setOpaque(false);
        setBackground(new Color(0,0,0,0));
        setPreferredSize(new Dimension(width, 28 + content.getPreferredSize().height));
    }
    public int getContentHeight(){
        return content.getPreferredSize().height;
    }

    public void refresh(){
        setBackground(Defaults.COLOR3);
        content.setForeground(Defaults.FOREGROUND_A);
        commentAuthorLabel.setForeground(Defaults.FOREGROUND_A);
        if (commentAuthor.equalsIgnoreCase(creatorName)) commentAuthorLabel.setForeground(new Color(47, 62, 195));
        likeIcon.setForeground(Defaults.FOREGROUND_A);
        likesLabel.setForeground(Defaults.FOREGROUND_A);
        percentLabel.setForeground(Defaults.FOREGROUND_B);
    }
}
