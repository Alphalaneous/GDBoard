package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class JButtonTooltip extends JButton {
    private JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(10,10);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
                RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHints(qualityHints);

                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 7, width-1, height-8, arcs.width, arcs.height);

                RoundRectangle2D.Double fill = new RoundRectangle2D.Double(0, 7, width-1, height-8, arcs.width, arcs.height);
                Polygon arrow = new Polygon();
                arrow.addPoint(panel.getWidth()/2-20, 18);
                arrow.addPoint(panel.getWidth()/2, 0);
                arrow.addPoint(panel.getWidth()/2+20, 18);
                Area area = new Area(fill);
                area.add(new Area(arrow));
                graphics.fill(area);
                graphics.setColor(Defaults.OUTLINE);
                graphics.draw(area);
            //Draws the rounded opaque panel with borders.
            /*graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 3, width-1, height-4, arcs.width, arcs.height);//paint background
            graphics.setColor(new Color(255,255,255,40));
            graphics.drawRoundRect(0, 3, width-1, height-4, arcs.width, arcs.height);//paint border*/
        }
    };
    private JLabel tooltipLabel = new JLabel();
    JButtonTooltip(String text, int y, String tooltip, JButtonUI ui){

        tooltipLabel.setText(tooltip);
        tooltipLabel.setForeground(Defaults.FOREGROUND);
        tooltipLabel.setFont(Defaults.MAIN_FONT.deriveFont(14f));
        tooltipLabel.setBounds(11,13,tooltipLabel.getPreferredSize().width+ 5, tooltipLabel.getPreferredSize().height + 5);
        panel.setBounds(0, y + 30, tooltipLabel.getPreferredSize().width + 22, tooltipLabel.getPreferredSize().height + 20);
        panel.add(tooltipLabel);
        panel.setLayout(null);
        panel.setVisible(false);
        panel.setOpaque(false);
        panel.setBackground(Defaults.TOP);
        panel.setForeground(Defaults.OUTLINE);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setBounds(getLocationOnScreen().x - Defaults.screenSize.x -  (tooltipLabel.getPreferredSize().width + 22)/2  + (getWidth() / 2) , 70 + 30, tooltipLabel.getPreferredSize().width + 22, tooltipLabel.getPreferredSize().height + 20);
                panel.setVisible(true);
                Overlay.addToFrame(panel);
            }
            public void mouseExited(MouseEvent e) {
                panel.setVisible(false);
                Overlay.removeFromFrame(panel);
            }
        });
        this.setUI(ui);
        this.setText(text);
    }
    public void refreshUI(){
        panel.setBackground(Defaults.TOP);
        panel.setForeground(Defaults.OUTLINE);
        tooltipLabel.setForeground(Defaults.FOREGROUND);
    }
}
