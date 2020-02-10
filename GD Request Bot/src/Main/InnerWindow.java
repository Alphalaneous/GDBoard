package Main;

import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class InnerWindow extends ResizablePanel {

	// --------------------
	// Constructor

	private static final long serialVersionUID = 1L;
	private final String title;
	private final double x;
	private final double y;
	private int width;
	private int height;
	private final String icon;

	// --------------------
	// Starting values and Initialization

	private boolean isPinPressed = false;
	private boolean toggleState = true;
	private JButton closeButton = new JButton("\uE894");
	private JButton pinButton = new JButton("\uE840");
	private JLabel pinButtonFill = new JLabel("  \uE842");
	private JPanel topBar = new JPanel(null);
	private JLabel windowIcon = new JLabel();

	// --------------------
	// JButtonUI Changes

	private JButtonUI defaultUI = new JButtonUI();

	//TODO Top of screen sliding in

	InnerWindow(final String title, final int x, final int y, final int width, final int height, final String icon) {
		double y1;
		double x1;
		double ratio = 1920 / Defaults.screenSize.getWidth();
		this.title = title;
		x1 = x / ratio;
		y1 = y / ratio;
		if(x + width >= Defaults.screenSize.getWidth() + 1) {
			x1 = Defaults.screenSize.getWidth() + 1 - width;
		}
		if(x <= -1) {
			x1 = -1;
		}
		if(y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
			y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
		}
		if(y <= -1) {
			y1 = -1;
		}
		if(x + width >= Defaults.screenSize.getWidth() + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
			x1 = Defaults.screenSize.getWidth() + 1 - width;
			y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
		}
		if(x + width >= Defaults.screenSize.getWidth() + 1 &&y <= -1) {
			x1 = Defaults.screenSize.getWidth() + 1 - width;
			y1 = -1;
		}
		if(x <= -1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
			x1 = -1;
			y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
		}
		if(x <= -1 && y <= -1) {
			x1 = -1;
			y1 = -1;
		}
		int middle = (int) (Defaults.screenSize.getWidth() / 2);
		if(x + width >= middle-290 && x <= middle+290 && y <= 93) {
			y1 = 93;
		}
		this.x = x1;
		this.y = y1;
		this.width = width;
		this.height = height;
		this.icon = icon;
	}

	// --------------------

	ResizablePanel createPanel() {



		// --------------------
		// Mouse Listener to stop allowing clicking through the panel to close the
		// window

		setDoubleBuffered(true);
        addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mouseClicked(e);
				moveToFront();
			}
		});

		// --------------------
		// Panel that holds everything

		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		setBounds((int)x, (int)y, width + 2, height + 32);
		setOpaque(false);
		setBorder(BorderFactory.createLineBorder(new Color(255,255,255,50)));

		// --------------------
		// Top Bar Panel (holds X, title, and Pin)

		topBar.setBackground(Defaults.TOP);
		topBar.setBounds(1, 1, width, 30);

		// --------------------
		// Frame movement when dragging Top Bar
		MouseInputAdapter mia = new MouseInputAdapter(){
		    Point location;
		    Point pressed;

		    public void mousePressed(MouseEvent me){
		    	moveToFront();
		        pressed = me.getLocationOnScreen();
		        location = getLocation();
		    }

		    public void mouseDragged(MouseEvent me){
		        Point dragged = me.getLocationOnScreen();
		        double x = location.x + dragged.getX() - pressed.getX();
		        double y = location.y + dragged.getY() - pressed.getY();
		        Point p = new Point();
		        p.setLocation(x, y);
		        setLocation(p);
		        if(x + width >= Defaults.screenSize.getWidth() + 1) {
		        	p.setLocation(Defaults.screenSize.getWidth() + 1 - width, p.getY());
					setLocation(p);
				}
				if(x <= -1) {
					p.setLocation(-1, p.getY());
					setLocation(p);
				}
				if(y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
					p.setLocation(p.getX(), Defaults.screenSize.getHeight() + 1 - height - 32);
					setLocation(p);
				}
				if(y <= -1) {
					p.setLocation(p.getX(), -1);
					setLocation(p);
				}
				if(x + width >= Defaults.screenSize.getWidth() + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
					setLocation((int) Defaults.screenSize.getWidth() + 1 - width, (int) Defaults.screenSize.getHeight() + 1 - height - 32);
				}
				if(x + width >= Defaults.screenSize.getWidth() + 1 &&y <= -1) {
					setLocation((int) Defaults.screenSize.getWidth() + 1 - width, -1);
				}
				if(x <= -1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
					setLocation(-1, (int) Defaults.screenSize.getHeight() + 1 - height - 32);
				}
				if(x <= -1 && y <= -1) {
					setLocation(-1, -1);
				}
				int middle = (int) (Defaults.screenSize.getWidth() / 2);
				if(x + width >= middle-290 && x <= middle+290 && y <= 93) {
					p.setLocation(p.getX(), 93);
					setLocation(p);
				}
				
		     }
		};

		topBar.addMouseListener(mia);
		topBar.addMouseMotionListener(mia);

		windowIcon.setForeground(Defaults.FOREGROUND);
		windowIcon.setText(icon);
		windowIcon.setBounds(10,0,30,30);
		windowIcon.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		topBar.add(windowIcon);

		// --------------------
		// Title text on Top Bar

		JLabel titleText = new JLabel(title);
		titleText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		titleText.setBounds(35, 0, width - 60, 30);
		titleText.setForeground(Defaults.FOREGROUND);
		topBar.add(titleText);

		closeButton.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		closeButton.setBounds(width - 30, 0, 30, 30);
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setForeground(Defaults.FOREGROUND);
		closeButton.setBorder(BorderFactory.createEmptyBorder());
		closeButton.setBackground(Defaults.TOP);
		closeButton.setUI(defaultUI);

		// --------------------
		// InnerWindow Closing

		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggle();
			}
		});

		topBar.add(closeButton);

		pinButton.setBounds(width - 60, 0, 30, 30);
		pinButton.setMargin(new Insets(0, 0, 0, 0));
		pinButton.setBorder(BorderFactory.createEmptyBorder());
		pinButton.setBackground(Defaults.TOP);
		pinButton.setForeground(Defaults.FOREGROUND);
		pinButton.setUI(defaultUI);
		pinButton.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		pinButtonFill.setBounds(width - 60, 0, 30, 30);
		pinButtonFill.setBorder(BorderFactory.createEmptyBorder());
		pinButtonFill.setForeground(Defaults.FOREGROUND);
		pinButtonFill.setBackground(new Color(0,0,0,0));
		pinButtonFill.setFont(new Font("Segoe MDL2 Assets", Font.PLAIN, 14));
		pinButtonFill.setVisible(false);

		// --------------------
		// Pin Switching

		pinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				moveToFront();
				if (isPinPressed) {

					// --------------------
					// Set to unpressed Pin Icon

					pinButtonFill.setVisible(false);
					isPinPressed = false;

				} else {

					// --------------------
					// Set to pressed Pin Icon
					pinButtonFill.setVisible(true);
					isPinPressed = true;

				}
			}
		});

		topBar.add(pinButton);
		pinButton.add(pinButtonFill);
		// --------------------

		add(topBar);
		return this;
	}
	void refreshListener() {
		Component[] components = getComponents();
        for (Component component : components)
        {
        	component.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mousePressed(MouseEvent e) {
    				super.mouseClicked(e);
    				moveToFront();
    			}
    		});
        }
	}
	public void refreshUI() {
		defaultUI.setBackground(Defaults.TOP);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		topBar.setBackground(Defaults.TOP);
		closeButton.setForeground(Defaults.FOREGROUND);
		windowIcon.setForeground(Defaults.FOREGROUND);
		pinButton.setForeground(Defaults.FOREGROUND);
		pinButtonFill.setForeground(Defaults.FOREGROUND);
		for (Component component : topBar.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
			}
			if(component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}

		double y1;
		double x1;
		double ratioX = 1920 / Defaults.screenSize.getWidth();
		double ratioY = 1080 / Defaults.screenSize.getHeight();
		int x = getX();
		int y = getY();
		x1 = x / ratioX;
		y1 = y / ratioY;
		if(x + width >= Defaults.screenSize.getWidth() + 1) {
			x1 = Defaults.screenSize.getWidth() + 1 - width;
		}
		if(x <= -1) {
			x1 = -1;
		}
		if(y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
			y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
		}
		if(y <= -1) {
			y1 = -1;
		}
		if(x + width >= Defaults.screenSize.getWidth() + 1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
			x1 = Defaults.screenSize.getWidth() + 1 - width;
			y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
		}
		if(x + width >= Defaults.screenSize.getWidth() + 1 &&y <= -1) {
			x1 = Defaults.screenSize.getWidth() + 1 - width;
			y1 = -1;
		}
		if(x <= -1 && y + height + 32 >= Defaults.screenSize.getHeight() + 1) {
			x1 = -1;
			y1 = Defaults.screenSize.getHeight() + 1 - height - 32;
		}
		if(x <= -1 && y <= -1) {
			x1 = -1;
			y1 = -1;
		}
		int middle = (int) (Defaults.screenSize.getWidth() / 2);
		if(x + width >= middle-290 && x <= middle+290 && y <= 93) {
			y1 = 93;
		}
		setLocation((int)x1, (int)y1);
	}
	void resetDimensions(int width, int height){
		this.height = height;
		this.width = width;

	}
	public void setVisible() {
			topBar.setVisible(true);
			setBorder(BorderFactory.createLineBorder(new Color(255,255,255,50)));
			if(toggleState) {
			setVisible(true);
		}
	}

	void setInvisible() {
		if (!isPinPressed) {
			setVisible(false);
		}
		topBar.setVisible(false);
		setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0)));

	}
	void toggle() {
		if(toggleState) {
			setVisible(false);
			toggleState = false;
		}
		else {
			setVisible(true);
			toggleState = true;
		}
	}
	//boolean getPinState() {
	//	return isPinPressed;
	//}
	void moveToFront() {
		Overlay.moveToFront(this);
	}
	void setPinVisible() {
		pinButton.setVisible(false);
	}
}
