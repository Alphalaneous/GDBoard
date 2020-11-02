package Main;

import Main.InnerWindows.LevelsWindow;
import Main.SettingsPanels.*;
import com.jidesoft.swing.ResizablePanel;
import org.jdesktop.swingx.border.DropShadowBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class InnerWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	private String title;
	private double x;
	private double y;
	private int width;
	private int height;
	private final String icon;
	private JButtonUI defaultUI = new JButtonUI();
	private JLabel titleText = new JLabel();

	public InnerWindow(final String title, final int width, final int height, final String icon) {
		double y1;
		double x1;
		double ratio = 1920 / Defaults.screenSize.getWidth();
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.icon = icon;
	}

	public String getName(){
		return title;
	}
	public String getIcon(){
		return icon;
	}

	public JPanel createPanel() {

		setDoubleBuffered(true);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				moveToFront();
			}
		});


		final boolean[] isDragging = {false};
		final boolean[] exited = {false};
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		setBounds((int) x, (int) y, width+2, height + 32);
		setOpaque(false);


		return this;
	}

	public void refreshUI() {

		defaultUI.setBackground(Defaults.TOP);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);

	}
	public void resetDimensions(int width, int height) {
		this.height = height;
		this.width = width;


	}

	public void setSettings(){
		Settings.setWindowSettings(title, getX() + "," + getY() + "," + false + "," + isVisible());
	}

	public void moveToFront() {
		Overlay.moveToFront(this);
	}

}
