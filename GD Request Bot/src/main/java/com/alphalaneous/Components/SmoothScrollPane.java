package com.alphalaneous.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

public class SmoothScrollPane extends JScrollPane {

    boolean verticalScrollEnabled = true;
    boolean horizontalScrollEnabled = false;


    public SmoothScrollPane(JComponent component){
        super(component);
        setBorder(BorderFactory.createEmptyBorder());
        getViewport().setBackground(new Color(0,0,0,0));
        setBackground(new Color(0,0,0,0));
        getVerticalScrollBar().setBackground(new Color(0,0,0,0));
        //setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(30);
        getVerticalScrollBar().setOpaque(false);
        getHorizontalScrollBar().setOpaque(false);
        setOpaque(false);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        getVerticalScrollBar().setUI(new ScrollbarUI());
        getHorizontalScrollBar().setUI(new ScrollbarUI());
        getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(8, 0));

        setWheelScrollingEnabled(false);
        setDoubleBuffered(true);
        /*setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {

            }
        });*/
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if((e.isShiftDown() && horizontalScrollEnabled) || !verticalScrollEnabled){
                    new Thread(() -> {
                        for (int i = 0; i < 30; i++) {
                            int pos = getHorizontalScrollBar().getValue() + e.getWheelRotation() * 3;
                            getHorizontalScrollBar().setValue(pos);
                            try {
                                Thread.sleep(2, 500);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }

                        }
                    }).start();
                }
                else {
                    new Thread(() -> {
                        for (int i = 0; i < 30; i++) {
                            int pos = getVerticalScrollBar().getValue() + e.getWheelRotation() * 3;
                            getVerticalScrollBar().setValue(pos);
                            try {
                                Thread.sleep(2, 500);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }

                        }
                    }).start();
                }
            }
        });
    }

    private boolean isVerticalScrollBarfNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getHeight() > viewRect.getHeight();
    }

    private boolean isHorizontalScrollBarNecessary() {
        Rectangle viewRect = viewport.getViewRect();
        Dimension viewSize = viewport.getViewSize();
        return viewSize.getWidth() > viewRect.getWidth();
    }

    public void setVerticalScrollEnabled(boolean enabled){
        verticalScrollEnabled = enabled;
        if(enabled) setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        else setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

    }
    public void setHorizontalScrollEnabled(boolean enabled){
        horizontalScrollEnabled = enabled;
        if(enabled) setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        else setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }
}
