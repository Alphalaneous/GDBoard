package Main;

import com.jidesoft.swing.Resizable;
import com.jidesoft.swing.ResizablePanel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

class CommentsWindow {
    private static JPanel panel = new JPanel();
    private static JButtonUI defaultUI = new JButtonUI();
    private static int height = 350;
    private static int width = 300;
    private static ResizablePanel window = new InnerWindow("Comments", 10, 500, width, height,
            "\uEBDB") {
        @Override
        protected Resizable createResizable() {

            return new Resizable(this) {
                @Override
                public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {

                    if (!(newH < 100 || newH > 800)) {
                        if (newX + width >= 672 && newX <= 1248 && newY <= 93) {
                            newY = 93;
                        }
                        setBounds(getX(), newY, getWidth(), newH);
                        height = newH;
                        resetDimensions(width, newH - 32);
                        scrollPane.setBounds(1, 31, width + 1, newH - 62);
                        buttons.setBounds(1, newH - 31, width, 30);
                        scrollPane.updateUI();
                    }
                }
            };
        }
    }.createPanel();
    private static JButtonUI newUI = new JButtonUI();
    private static JScrollPane scrollPane = new JScrollPane(panel);
    private static JPanel buttons = new JPanel();
    private static boolean topC = false;
    private static int page = 0;

    static void createPanel() {
        panel.setLayout(null);
        panel.setBounds(0, 0, width, height);
        buttons.setLayout(null);
        buttons.setBounds(1, height + 1, width, 30);
        buttons.setBackground(Defaults.TOP);

        JButton top = createButton("\uE8E1", 90);

        buttons.add(top);

        top.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((InnerWindow) window).moveToFront();
                super.mousePressed(e);
                topC = true;
                page = 0;
                try {
                    unloadComments(false);
                    loadComments(0, true);
                } catch (Exception ignored) {
                }

            }
        });

        JButton new1 = createButton("\uE823", 120);

        buttons.add(new1);

        new1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((InnerWindow) window).moveToFront();
                super.mousePressed(e);
                topC = false;
                page = 0;
                try {
                    unloadComments(false);
                    loadComments(0, false);
                } catch (Exception ignored) {
                }

            }
        });

        JButton prev = createButton("\uE760", 0);

        buttons.add(prev);

        prev.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((InnerWindow) window).moveToFront();
                super.mousePressed(e);
                if (page != 0) {
                    page--;
                    try {
                        unloadComments(false);
                        loadComments(page, topC);
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        JButton next = createButton("\uE761", 30);

        buttons.add(next);

        next.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((InnerWindow) window).moveToFront();
                super.mousePressed(e);
                page++;
                    unloadComments(false);
                    if(!loadComments(page, topC)){
                        page--;
                        try {
                            loadComments(page, topC);
                        } catch (Exception f) {
                            f.printStackTrace();
                        }
                    }
                }
        });


        newUI.setBackground(Defaults.MAIN);
        newUI.setHover(Defaults.HOVER);
        panel.setBackground(Defaults.SUB_MAIN);
        panel.setPreferredSize(new Dimension(width, height - 30));


        scrollPane.setBackground(Defaults.SUB_MAIN);
        scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBounds(1, 31, width + 1, height - 30);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(1, height));
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color color = new Color(0, 0, 0, 0);

                g2.setPaint(color);
                g2.fillRect(r.x, r.y, r.width, r.height);
                g2.dispose();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color color = new Color(0, 0, 0, 0);


                g2.setPaint(color);
                g2.fillRect(r.x, r.y, r.width, r.height);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        });
        window.add(scrollPane);
        window.add(buttons);
        ((InnerWindow) window).refreshListener();
        Overlay.addToFrame(window);
    }

    static void unloadComments(boolean reset) {
        if (reset) {
            topC = false;
            page = 0;
        }
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(width, height - 30));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.removeAll();
        panel.updateUI();
    }

    static boolean loadComments(int page, boolean top) {

        panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 4));

        GetComments getComments = new GetComments();
        try {
            ArrayList<String> commentText = getComments.getComments(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID(), page, top)
                    .get(0);
            ArrayList<String> commenterText = getComments
                    .getComments(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID(), page, top).get(1);
            ArrayList<String> percent = getComments
                    .getComments(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID(), page, top).get(2);
            ArrayList<String> likes = getComments
                    .getComments(Requests.levels.get(LevelsWindow.getSelectedID()).getLevelID(), page, top).get(3);
            panel.setVisible(false);

            int panelHeight = 0;
            assert commentText != null;
            try {
                for (int i = 0; i < commentText.size() / 4; i++) {
                    assert commenterText != null;
                    JPanel cmtPanel = new JPanel(null);
                    cmtPanel.setBackground(Defaults.MAIN);

                    JLabel commenter = new JLabel();
                    commenter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    int finalI = i;
                    commenter.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                try {
                                    Desktop.getDesktop().browse(new URI("http://www.gdbrowser.com/profile/" + commenterText.get(finalI)));
                                } catch (IOException | URISyntaxException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            super.mouseEntered(e);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            super.mouseExited(e);
                        }
                    });
                    commenter.setFont(new Font("bahnschrift", Font.BOLD, 14));
                    commenter.setBounds(9, 4, (int) (width * 0.5), 18);
                    JLabel percentLabel = new JLabel();
                    percentLabel.setFont(new Font("bahnschrift", Font.BOLD, 14));
                    JLabel likeIcon = new JLabel();
                    if(Integer.parseInt(likes.get(i)) < 0){
                        likeIcon.setText("\uE8E0");
                    }
                    else{
                        likeIcon.setText("\uE8E1");
                    }

                    likeIcon.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
                    likeIcon.setBounds(width - 20, 4, (int) (width * 0.5), 18);


                    JLabel likesLabel = new JLabel();
                    likesLabel.setFont(new Font("bahnschrift", Font.BOLD, 10));



                    JLabel comment = new JLabel();
                    comment.setFont(new Font("bahnschrift", Font.PLAIN, 12));
                    comment.setBounds(9, 24, width - 6, 60);

                    cmtPanel.add(commenter);
                    cmtPanel.add(comment);
                    cmtPanel.add(percentLabel);
                    cmtPanel.add(likesLabel);
                    cmtPanel.add(likeIcon);

                    commenter.setForeground(Defaults.FOREGROUND);
                    percentLabel.setForeground(Defaults.FOREGROUND2);
                    likesLabel.setForeground(Defaults.FOREGROUND);
                    likeIcon.setForeground(Defaults.FOREGROUND);

                    comment.setOpaque(false);
                    String commentTextFormat = String.format("<html><div WIDTH=%d>%s</div></html>", width-8, commentText.get(i));
                    comment.setForeground(Defaults.FOREGROUND);
                    comment.setText(commentTextFormat);
                    if(commenterText.get(i).equalsIgnoreCase(Requests.levels.get(LevelsWindow.getSelectedID()).getAuthor())){
                        commenter.setForeground(new Color(16, 164,0));
                    }
                    commenter.setText(commenterText.get(i));
                    if (!percent.get(i).equalsIgnoreCase("0")) {
                        percentLabel.setText(percent.get(i) + "%");
                        percentLabel.setBounds(commenter.getPreferredSize().width + 20, 4, percentLabel.getPreferredSize().width + 5, 18);
                    }
                    likesLabel.setText(likes.get(i));
                    likesLabel.setBounds(width - likesLabel.getPreferredSize().width - 26, 4, likesLabel.getPreferredSize().width + 5, 18);
                    comment.setBounds(9, 24, width - 8, comment.getPreferredSize().height);
                    commenter.setBounds(9, 4, commenter.getPreferredSize().width, 18);
                    panel.add(cmtPanel);
                    panelHeight = panelHeight + 32 + comment.getPreferredSize().height;
                    cmtPanel.setPreferredSize(new Dimension(width, 28 + comment.getPreferredSize().height));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((InnerWindow)window).refreshListener();
            panel.setPreferredSize(new Dimension(width, panelHeight));
            panel.updateUI();
            panel.setVisible(true);
            scrollPane.getVerticalScrollBar().setValue(0);
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
        }
        catch (Exception ignored){
            return false;
        }
        return true;
    }
    private static JButton createButton(String icon, int x){
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 20));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setForeground(Defaults.FOREGROUND);
        button.setBackground(Defaults.TOP);
        button.setUI(defaultUI);
        button.setBounds(x, 0, 30, 30);
        return button;
    }
    static void refreshUI() {
        ((InnerWindow) window).refreshUI();
        newUI.setBackground(Defaults.MAIN);
        newUI.setHover(Defaults.HOVER);
        newUI.setSelect(Defaults.SELECT);
        defaultUI.setBackground(Defaults.TOP);
        defaultUI.setHover(Defaults.HOVER);
        defaultUI.setSelect(Defaults.SELECT);
        scrollPane.getViewport().setBackground(Defaults.SUB_MAIN);
        for (Component component : panel.getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(Defaults.MAIN);
                for (Component component1 : ((JPanel) component).getComponents()) {
                    if (component1 instanceof JLabel) {
                        if(((JLabel) component1).getText().contains("%")){
                            component1.setForeground(Defaults.FOREGROUND2);
                        }
                        else if(((JLabel) component1).getText().equalsIgnoreCase(Requests.levels.get(LevelsWindow.getSelectedID()).getAuthor())){
                            component1.setForeground(new Color(16, 164,0));
                        }
                        else {
                            component1.setForeground(Defaults.FOREGROUND);
                        }
                    }
                    if (component1 instanceof JTextPane) {
                        component1.setForeground(Defaults.FOREGROUND);
                    }
                }
            }
        }
        for (Component component : buttons.getComponents()) {
            if (component instanceof JButton) {
                component.setBackground(Defaults.TOP);
                component.setForeground(Defaults.FOREGROUND);
            }
        }
        panel.setBackground(Defaults.SUB_MAIN);
        buttons.setBackground(Defaults.TOP);
    }

    static void toggleVisible() {
        ((InnerWindow) window).toggle();
    }

    static void setInvisible() {
        ((InnerWindow) window).setInvisible();
    }

    static void setVisible() {
        ((InnerWindow) window).setVisible();
    }
}
class GetComments {
    private ArrayList<ArrayList<String>> Comments = new ArrayList<>();
    private ArrayList<String> commentContent = new ArrayList<>();
    private ArrayList<String> commenters = new ArrayList<>();
    private ArrayList<String> percent = new ArrayList<>();
    private ArrayList<String> likes = new ArrayList<>();

    ArrayList<ArrayList<String>> getComments(String levelID, int page, boolean top) throws IOException {
        URL gdAPI;
        if (top) {
            gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?page=" + page + "&top");
        } else {
            gdAPI = new URL("https://gdbrowser.com/api/comments/" + levelID + "?page=" + page);
        }
        URLConnection con = gdAPI.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String message = "{\"Comments\" : " + IOUtils.toString(br) + "}";
        JSONObject obj = new JSONObject(message);
        JSONArray arr;
        try {
            arr = obj.getJSONArray("Comments");

            assert arr != null;
            for (int i = 0; i < arr.length(); i++) {
                try {
                    commentContent.add(StringEscapeUtils.unescapeHtml4(arr.getJSONObject(i).getString("content")));
                    Comments.add(commentContent);
                    commenters.add(arr.getJSONObject(i).getString("username"));
                    Comments.add(commenters);
                    try {
                        percent.add(StringEscapeUtils.unescapeHtml4(arr.getJSONObject(i).getString("percent")));
                    } catch (Exception e) {
                        percent.add("0");
                    }
                    Comments.add(percent);
                    likes.add(arr.getJSONObject(i).getString("likes"));
                    Comments.add(likes);
                } catch (Exception ignored) {
                    break;
                }
            }
        } catch (Exception ignored) {

        }
        return Comments;
    }
}

