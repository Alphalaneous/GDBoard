package Main;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JButtonUI extends BasicButtonUI implements java.io.Serializable, MouseListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border m_borderRaised = UIManager.getBorder("Button.border");

	private Border m_borderLowered = UIManager.getBorder("Button.borderPressed");

	private Color m_backgroundNormal = UIManager.getColor("Button.background");

	private Color m_backgroundPressed = UIManager.getColor("Button.pressedBackground");

	private boolean mouseHover = false;

	private Color bgColor = Defaults.TOP;
	private Color hoverColor = Defaults.HOVER;
	private Color selectColor = Defaults.SELECT;

	public void installUI(JComponent c) {
		super.installUI(c);

		c.addMouseListener(this);
		c.addKeyListener(this);
	}

	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		c.removeMouseListener(this);
		c.removeKeyListener(this);
	}

	public Dimension getPreferredSize(JComponent c) {
		Dimension d = super.getPreferredSize(c);
		if (m_borderRaised != null) {
			Insets ins = m_borderRaised.getBorderInsets(c);
			d.setSize(d.width + ins.left + ins.right, d.height + ins.top + ins.bottom);
		}
		return d;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		JComponent c = (JComponent) e.getComponent();
		c.setBackground(selectColor);
	}

	public void mouseReleased(MouseEvent e) {
		JComponent c = (JComponent) e.getComponent();
		if (mouseHover) {
			c.setBackground(hoverColor);
		} else {
			c.setBackground(bgColor);
		}
	}

	public void mouseEntered(MouseEvent e) {
		mouseHover = true;
		JComponent c = (JComponent) e.getComponent();
		c.setBackground(hoverColor);
		c.repaint();
	}

	public void mouseExited(MouseEvent e) {
		mouseHover = false;
		JComponent c = (JComponent) e.getComponent();
		c.setBackground(bgColor);
		c.repaint();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
			JComponent c = (JComponent) e.getComponent();
			c.setBorder(m_borderLowered);
			c.setBackground(m_backgroundPressed);
		}
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
			JComponent c = (JComponent) e.getComponent();
			c.setBorder(m_borderRaised);
			c.setBackground(m_backgroundNormal);
		}
	}
	void setSelect(Color color) {
		selectColor = color;
	}
	public void setBackground(Color color) {
		bgColor = color;
	}
	void setHover(Color color) {
		hoverColor = color;
	}
}