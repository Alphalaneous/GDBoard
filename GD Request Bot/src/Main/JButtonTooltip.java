package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonTooltip extends JButton {
    JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(6,6);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            //Draws the rounded opaque panel with borders.
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint background
            graphics.setColor(new Color(255,255,255,40));
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint border
        }
    };
    JLabel tooltipLabel = new JLabel();
    JButtonTooltip(String text, int y, String tooltip, JButtonUI ui){

        tooltipLabel.setText(tooltip);
        tooltipLabel.setForeground(Defaults.FOREGROUND);
        tooltipLabel.setFont(new Font("bahnschrift", Font.BOLD, 14));
        tooltipLabel.setBounds(5,7,tooltipLabel.getPreferredSize().width, tooltipLabel.getPreferredSize().height);
        panel.setBounds(0, y + 40, tooltipLabel.getPreferredSize().width + 10, tooltipLabel.getPreferredSize().height + 10);
        panel.add(tooltipLabel);
        panel.setLayout(null);
        panel.setVisible(false);
        panel.setOpaque(false);
        panel.setBackground(Defaults.TOP);
        panel.setForeground(Defaults.FOREGROUND);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setVisible(true);
            }
            public void mouseExited(MouseEvent e) {
                panel.setVisible(false);
            }
        });
        this.setUI(ui);
        this.setText(text);
        Overlay.addToFrame(panel);
    }
    public void setTooltipLocation(int x, int y){
        panel.setBounds(x -  (tooltipLabel.getPreferredSize().width + 10)/2 , y + 40, tooltipLabel.getPreferredSize().width + 10, tooltipLabel.getPreferredSize().height + 10);
    }
}
