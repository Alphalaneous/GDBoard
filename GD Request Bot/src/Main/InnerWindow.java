package Main;

import com.jidesoft.swing.ResizablePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class InnerWindow extends ResizablePanel {

	// --------------------
	// Constructor

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String title;
	private final double x;
	private final double y;
	private int width;
	private int height;
	private final String iconLocation;

	// --------------------
	// Starting values and Initialization

	private boolean isPinPressed = false;
	private boolean toggleState = true;
	private JButton pinButton;
	private JPanel topBar = new JPanel(null);

	// --------------------
	// JButtonUI Changes

	private JButtonUI defaultUI = new JButtonUI();

	InnerWindow(final String title, final int x, final int y, final int width, final int height, final String iconLocation) {
		double y1;
		double x1;
		double ratio = 1920 / Defaults.screenSize.getWidth();
		System.out.println(ratio);
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
		this.x = x1;
		this.y = y1;
		this.width = width;
		this.height = height;
		this.iconLocation = iconLocation;
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
				double ratio = 1920 / Defaults.screenSize.getWidth();
				if(x + width >= 672/ratio && x <= 672/ratio + 576 && y <= 93) {
					p.setLocation(p.getX(), 93);
					setLocation(p);
				}
				
		     }
		};

		topBar.addMouseListener(mia);
		topBar.addMouseMotionListener(mia);
		
		BufferedImage iconImage = null;
		try {
			iconImage = ImageIO.read(new File(iconLocation));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert iconImage != null;
		Image iconImageScaled = iconImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
		ImageIcon iconImageFinal = new ImageIcon(iconImageScaled);
		JLabel windowIcon = new JLabel(iconImageFinal);
		
		windowIcon.setBounds(3,0,30,30);
		
		topBar.add(windowIcon);

		// --------------------
		// Title text on Top Bar

		JLabel titleText = new JLabel(title);
		titleText.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		titleText.setBounds(35, 0, width - 60, 30);
		titleText.setForeground(Defaults.FOREGROUND);
		topBar.add(titleText);

		// --------------------
		// X Icon Image on Button

		BufferedImage xImage = null;
		try {
			xImage = ImageIO.read(new File("src/resources/WindowIcons/X.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert xImage != null;
		Image xIconScaled = xImage.getScaledInstance(17, 17, Image.SCALE_SMOOTH);
		ImageIcon xIcon = new ImageIcon(xIconScaled);

		JButton closeButton = new JButton(xIcon);
		closeButton.setBounds(width - 30, 0, 30, 30);
		closeButton.setMargin(new Insets(0, 0, 0, 0));
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

		// --------------------
		// Pin Icon Image on Button

		BufferedImage pinImage = null;
		try {
			pinImage = ImageIO.read(new File("src/resources/WindowIcons/pin.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert pinImage != null;
		Image pinScaled = pinImage.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
		ImageIcon pinIcon = new ImageIcon(pinScaled);

		// --------------------
		// Pressed Pin Icon Image on Button

		BufferedImage origPinPressed = null;
		try {
			origPinPressed = ImageIO.read(new File("src/resources/WindowIcons/pinPressed.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		assert origPinPressed != null;
		Image pinPressed = origPinPressed.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
		ImageIcon pinIconPressed = new ImageIcon(pinPressed);

		pinButton = new JButton(pinIcon);
		pinButton.setBounds(width - 60, 0, 30, 30);
		pinButton.setMargin(new Insets(0, 0, 0, 0));
		pinButton.setBorder(BorderFactory.createEmptyBorder());
		pinButton.setBackground(Defaults.TOP);
		pinButton.setUI(defaultUI);

		// --------------------
		// Pin Switching

		pinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				moveToFront();
				if (isPinPressed) {

					// --------------------
					// Set to unpressed Pin Icon

					pinButton.setIcon(pinIcon);
					isPinPressed = false;

				} else {

					// --------------------
					// Set to pressed Pin Icon
					pinButton.setIcon(pinIconPressed);
					isPinPressed = true;

				}
			}
		});

		topBar.add(pinButton);

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
		for (Component component : topBar.getComponents()) {
			if (component instanceof JButton) {
				component.setBackground(Defaults.TOP);
			}
			if(component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);
			}
		}
		Point p = new Point();
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

		int middle = (int) (Defaults.screenSize.getWidth() / 2);
		if(x + width >= middle-290 && x <= middle+290 && y <= 93) {
			p.setLocation(p.getX(), 93);
			setLocation(p);
		}
	}
	public void resetDimensions(int width, int height){
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
